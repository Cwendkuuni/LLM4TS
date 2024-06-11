import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.PrototypeObjectType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import com.google.javascript.rhino.jstype.UnionType;
import com.google.javascript.rhino.testing.PolyMergedTypeTester;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class RecordTypeTest {

  @SuppressWarnings("unchecked")
  private static <T> T createRecordType(Map<String, RecordProperty> properties) {
    JSTypeRegistry registry = PolyMergedTypeTester.getRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    return (T) builder.build(properties);
  }

  @org.junit.Test
  public void testCreateRecordType() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("prop1", new RecordProperty("prop1", JSType.getStringType()));
    properties.put("prop2", new RecordProperty("prop2", JSType.getNumberType()));

    RecordType recordType = createRecordType(properties);
    assertEquals("RecordType", recordType.getClass().getSimpleName());
    assertEquals(2, recordType.getProperties().size());
  }

  @org.junit.Test
  public void testIsEquivalentTo() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("prop1", new RecordProperty("prop1", JSType.getBooleanType()));
    properties1.put("prop2", new RecordProperty("prop2", JSType.getNumberType()));

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("prop1", new RecordProperty("prop1", JSType.getBooleanType()));
    properties2.put("prop2", new RecordProperty("prop2", JSType.getNumberType()));
    properties2.put("prop3", new RecordProperty("prop3", JSType.getBooleanType()));

    RecordType recordType1 = createRecordType(properties1);
    RecordType recordType2 = createRecordType(properties2);

    assertTrue(recordType1.isEquivalentTo(recordType1));
    assertTrue(recordType1.isEquivalentTo(recordType2));

    RecordType recordType3 = new RecordType(null, null);
    assertFalse(recordType1.isEquivalentTo(recordType3));

    RecordType recordType4 = new RecordType(null, Maps.newHashMap());
    assertFalse(recordType1.isEquivalentTo(recordType4));

    RecordType recordType5 = new RecordType(null, properties1);
    assertTrue(recordType1.isEquivalentTo(recordType5));

    RecordType recordType6 = new RecordType(null, properties2);
    assertFalse(recordType1.isEquivalentTo(recordType6));
  }

  @org.junit.Test
  public void testGetImplicitPrototype() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("prop1", new RecordProperty("prop1", JSType.getBooleanType()));

    RecordType recordType = createRecordType(properties);
    assertEquals(JSTypeNative.OBJECT_TYPE, recordType.getImplicitPrototype());
  }

  @org.junit.Test
  public void testDefineProperty() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("prop1", new RecordProperty("prop1", JSType.getBooleanType()));

    RecordType recordType = createRecordType(properties);

    JSType type = JSType.getNumberType();
    Node node = Node.newIntLiteral(1);

    assertTrue(recordType.defineProperty("prop1", type, false, node));

    assertEquals(type, recordType.getPropertyType("prop1"));
  }

  @org.junit.Test
  public void testDefinePropertyFrozen() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("prop1", new RecordProperty("prop1", JSType.getBooleanType()));

    RecordType recordType = createRecordType(properties);
    recordType.setFrozen(true);

    JSType type = JSType.getNumberType();
    Node node = Node.newIntLiteral(1);

    assertFalse(recordType.defineProperty("prop1", type, false, node));
  }

  @org.junit.Test
  public void testGetGreatestSubtypeHelper() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("prop1", new RecordProperty("prop1", JSType.getBooleanType()));

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("prop1", new RecordProperty("prop1", JSType.getBooleanType()));
    properties2.put("prop2", new RecordProperty("prop2", JSType.getStringType()));

    RecordType recordType1 = createRecordType(properties1);
    RecordType recordType2 = createRecordType(properties2);

    JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
    assertTrue(recordType1.isEquivalentTo(greatestSubtype));

    Map<String, RecordProperty> properties3 = Maps.newHashMap();
    properties3.put("prop1", new RecordProperty("prop1", JSType.getBooleanType()));
    properties3.put("prop2", new RecordProperty("prop2", JSType.getNumberType()));

    RecordType recordType3 = createRecordType(properties3);

    greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType3);
    assertEquals(recordType3, greatestSubtype);

    JSType type1 = JSType.getBooleanType();
    JSType type2 = JSType.getNumberType();

    greatestSubtype = recordType1.getGreatestSubtypeHelper(type2);
    assertEquals(type2, greatestSubtype);
  }

  @org.junit.Test
  public void testIsSubtype() {
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("prop1", new RecordProperty("prop1", JSType.getBooleanType()));

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("prop1", new RecordProperty("prop1", JSType.getBooleanType()));
    propertiesB.put("prop2", new RecordProperty("prop2", JSType.getNumberType()));

    RecordType recordTypeA = createRecordType(propertiesA);
    RecordType recordTypeB = createRecordType(propertiesB);

    assertTrue(recordTypeA.isSubtype(recordTypeA));
    assertTrue(recordTypeA.isSubtype(recordTypeB));
    assertTrue(recordTypeB.isSubtype(recordTypeA));

    UnionType unionType = new UnionType(JSTypeNative.NULL_TYPE, JSTypeNative.OBJECT_TYPE);
    assertFalse(recordTypeA.isSubtype(unionType));

    ObjectType objectType = new ObjectType(JSTypeNative.OBJECT_TYPE, null);
    assertTrue(recordTypeA.isSubtype(objectType));
  }

  @org.junit.Test
  public void testGetPropertyType() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("prop1", new RecordProperty("prop1", JSType.getBooleanType()));

    RecordType recordType = createRecordType(properties);

    assertEquals(JSType.getBooleanType(), recordType.getPropertyType("prop1"));
  }

  @org.junit.Test
  public void testHasProperty() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("prop1", new RecordProperty("prop1", JSType.getBooleanType()));

    RecordType recordType = createRecordType(properties);

    assertTrue(recordType.hasProperty("prop1"));

    assertFalse(recordType.hasProperty("prop2"));
  }

  @org.junit.Test
  public void testIsPropertyTypeDeclared() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("prop1", new RecordProperty("prop1", JSType.getBooleanType()));

    RecordType recordType = createRecordType(properties);

    assertTrue(recordType.isPropertyTypeDeclared("prop1"));

    assertFalse(recordType.isPropertyTypeDeclared("prop2"));
  }

  @org.junit.Test
  public void testGetPropertyNode() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("prop1", new RecordProperty("prop1", JSType.getBooleanType()));

    RecordType recordType = createRecordType(properties);

    assertNull(recordType.getPropertyNode("prop1"));

    Node node = Node.newName("prop2");
    recordType.defineProperty("prop2", JSType.getBooleanType(), false, node);

    assertSame(node, recordType.getPropertyNode("prop2"));
  }

  @org.junit.Test
  public void testResolveInternal() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("prop1", new RecordProperty("prop1", JSType.getBooleanType()));

    RecordType recordType = createRecordType(properties);

    JSType type1 = JSType.getBooleanType();
    JSType type2 = JSType.getNumberType();

    JSType resolvedType = recordType.resolveInternal(null, null);

    assertEquals(recordType, resolvedType);

    JSType resolvedTypes[] = new JSType[1];
    recordType.resolveInternal(null, new StaticScope<JSType>(null, new TreeMap<String, JSType>()) {
      @Override
      public JSType getPropertyType(String propertyName) {
        if ("prop1".equals(propertyName)) {
          resolvedTypes[0] = type1;
          return type1;
        }
        return super.getPropertyType(propertyName);
      }
    }, resolvedTypes);

    assertEquals(type1, resolvedTypes[0]);

    JSType resolvedTypes2[] = new JSType[1];
    recordType.resolveInternal(null, new StaticScope<JSType>(null, new TreeMap<String, JSType>()) {
      @Override
      public JSType getPropertyType(String propertyName) {
        if ("prop1".equals(propertyName)) {
          resolvedTypes2[0] = type2;
          return type2;
        }
        return super.getPropertyType(propertyName);
      }
    }, resolvedTypes2);

    assertNull(resolvedTypes2[0]);
  }
}