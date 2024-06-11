import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.PrototypeObjectType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class RecordTypeTest {

  private JSTypeRegistry registry;
  private RecordType recordType;

  @SuppressWarnings("unchecked")
  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    RecordProperty prop1 = new RecordProperty("prop1", registry.getNativeStringType());
    RecordProperty prop2 = new RecordProperty("prop2", registry.getNativeNumberType());
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("prop1", prop1);
    properties.put("prop2", prop2);
    recordType = new RecordType(registry, properties);
  }

  @Test
  public void testIsEquivalentTo() {
    RecordType recordType1 = new RecordType(registry, properties);
    RecordType recordType2 = new RecordType(registry, properties);
    assertSame(recordType1, recordType2);
    assertTrue(recordType1.isEquivalentTo(recordType2));
  }

  @Test
  public void testIsEquivalentToDifferentRecordTypes() {
    Map<String, RecordProperty> prop2Map = Maps.newHashMap();
    prop2Map.putAll(properties);
    prop2Map.put("prop3", new RecordProperty("prop3", registry.getNativeNumberType()));

    RecordType recordType2 = new RecordType(registry, prop2Map);
    assertFalse(recordType1.isEquivalentTo(recordType2));
  }

  @Test
  public void testIsEquivalentToRecordWithFrozenProperties() {
    recordType.freeze();
    RecordType recordTypeFrozen = new RecordType(registry, properties);
    assertFalse(recordType.isEquivalentTo(recordTypeFrozen));
  }

  @Test
  public void testIsEquivalentToRecordWithNullRecordProperty() {
    Map<String, RecordProperty> nullProperties = Maps.newHashMap();
    RecordType recordTypeWithNullProperties = new RecordType(registry, nullProperties);
    assertFalse(recordType.isEquivalentTo(recordTypeWithNullProperties));
  }

  @Test
  public void testGetImplicitPrototype() {
    ObjectType implicitPrototype = recordType.getImplicitPrototype();
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), implicitPrototype);
  }

  @Test
  public void testDefineProperty() {
    String propertyName = "property";
    JSType propertyType = registry.getNativeStringType();
    boolean isInferred = true;
    Node propertyNode = Node.newStringLiteral("property");

    recordType.defineProperty(propertyName, propertyType, isInferred, propertyNode);

    JSType property = recordType.getPropertyType(propertyName);
    assertEquals(propertyType, property);
  }

  @Test(expected = IllegalStateException.class)
  public void testDefinePropertyWithNullRecordProperty() {
    recordType.defineProperty(null, registry.getNativeStringType(), true, Node.NULL);
  }

  @Test
  public void testGetGreatestSubtype() {
    RecordType recordType1 = new RecordType(registry, properties);
    RecordType recordType2 = new RecordType(registry, properties);
    RecordType recordType3 = new RecordType(registry, Maps.newHashMap());
    recordType3.defineProperty("prop3", registry.getNativeNumberType(), true, Node.NULL);

    JSType greatestSubtype = recordType1.getGreatestSubtype(recordType2);
    assertSame(recordType1, greatestSubtype);

    greatestSubtype = recordType1.getGreatestSubtype(recordType3);
    assertSame(recordType1, greatestSubtype);

    greatestSubtype = recordType3.getGreatestSubtype(recordType1);
    assertSame(recordType1, greatestSubtype);

    greatestSubtype = recordType3.getGreatestSubtype(recordType2);
    assertSame(recordType1, greatestSubtype);

    JSType unknownType = registry.getNativeType(JSTypeNative.UNKNOWN_TYPE);
    greatestSubtype = recordType1.getGreatestSubtype(unknownType);
    assertSame(recordType1, greatestSubtype);

    greatestSubtype = unknownType.getGreatestSubtype(recordType1);
    assertSame(recordType1, greatestSubtype);

    greatestSubtype = registry.getNativeType(JSTypeNative.OBJECT_TYPE)
        .getGreatestSubtype(recordType1);
    assertSame(recordType1, greatestSubtype);

    greatestSubtype = recordType1.getGreatestSubtype(registry.getNativeType(JSTypeNative.NO_TYPE));
    assertSame(registry.getNativeType(JSTypeNative.NO_TYPE), greatestSubtype);

    greatestSubtype = recordType1.getGreatestSubtype(registry.getNativeType(JSTypeNative.ARRAY_TYPE));
    assertSame(registry.getNativeType(JSTypeNative.NO_OBJECT_TYPE), greatestSubtype);
  }

  @Test
  public void testIsSubtype() {
    RecordType recordType1 = new RecordType(registry, properties);
    RecordType recordType2 = new RecordType(registry, Maps.newHashMap());
    recordType2.defineProperty("prop1", registry.getNativeStringType(), true, Node.NULL);

    assertTrue(recordType1.isSubtype(recordType1));
    assertTrue(recordType1.isSubtype(recordType2));
    assertFalse(recordType2.isSubtype(recordType1));

    UnionTypeBuilder unionTypeBuilder = new UnionTypeBuilder(registry);
    ObjectType unionType = unionTypeBuilder.build();
    assertTrue(recordType1.isSubtype(unionType));

    JSType unknownType = registry.getNativeType(JSTypeNative.UNKNOWN_TYPE);
    assertFalse(recordType1.isSubtype(unknownType));

    JSType arrayType = registry.getNativeType(JSTypeNative.ARRAY_TYPE);
    assertFalse(recordType1.isSubtype(arrayType));

    JSType arrayTypeWithRecordProperty = new RecordType(registry,
            Maps.newHashMap("item", registry.getNativeStringType()));
    assertFalse(recordType1.isSubtype(arrayTypeWithRecordProperty));
  }

  @Test
  public void testResolveInternal() {
    String propertyName = "property";
    JSType propertyType = registry.getNativeStringType();

    recordType.defineProperty(propertyName, propertyType, false, Node.NULL);

    JSType resolvedType = recordType.resolveInternal(null, null);
    JSType resolvedPropertyType = resolvedType.getPropertyType(propertyName);
    assertEquals(propertyType, resolvedPropertyType);
  }

  @Test
  public void testToString() {
    String toString = recordType.toString();
    assertTrue(toString.contains("RecordType"));
    assertTrue(toString.contains("properties=" + properties.toString()));
  }

  @Test
  public void testFreeze() {
    recordType.freeze();
    RecordType frozenRecordType = new RecordType(registry, properties);
    assertFalse(frozenRecordType.isFrozen());
  }

  @Test
  public void testFreezeTwice() {
    recordType.freeze();
    recordType.freeze();
    assertTrue(recordType.isFrozen());
  }

  @Test
  public void testHasProperty() {
    assertTrue(recordType.hasProperty("prop1"));
    assertFalse(recordType.hasProperty("prop3"));
  }

  @Test
  public void testGetPropertyType() {
    assertEquals(registry.getNativeStringType(), recordType.getPropertyType("prop1"));
    assertNull(recordType.getPropertyType("prop3"));
  }

  @Test
  public void testGetPropertyNode() {
    assertNull(recordType.getPropertyNode("prop1"));
    assertNull(recordType.getPropertyNode("prop3"));
  }
}