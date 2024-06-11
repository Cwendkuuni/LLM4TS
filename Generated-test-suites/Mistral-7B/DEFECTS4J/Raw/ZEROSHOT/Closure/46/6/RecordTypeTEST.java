import static org.junit.Assert.*;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;

import java.util.Map;
import java.util.SortedMap;

/**
 * Test class for {@link RecordType} class.
 */
public class RecordTypeTest {

  private JSTypeRegistry registry;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
  }

  @Test
  public void testRecordTypeCreation() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("a", new RecordProperty("a", JSType.STRING));
    recordProperties.put("b", new RecordProperty("b", JSType.NUMBER));

    RecordType recordType = new RecordType(registry, recordProperties);
    assertEquals("RecordType", recordType.getClassName());
    assertEquals(2, recordType.getDeclaredPropertyNames().length);
    assertTrue(recordType.isRecordType());
  }

  @Test
  public void testIsEquivalentTo() {
    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("a", new RecordProperty("a", JSType.STRING));
    recordProperties1.put("b", new RecordProperty("b", JSType.NUMBER));

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("a", new RecordProperty("a", JSType.STRING));
    recordProperties2.put("b", new RecordProperty("b", JSType.NUMBER));
    recordProperties2.put("c", new RecordProperty("c", JSType.NUMBER));

    RecordType recordType1 = new RecordType(registry, recordProperties1);
    RecordType recordType2 = new RecordType(registry, recordProperties2);

    assertFalse(recordType1.isEquivalentTo(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
    assertFalse(recordType1.isEquivalentTo(registry.getNativeObjectType(JSTypeNative.NUMBER_TYPE)));
    assertFalse(recordType1.isEquivalentTo(recordType2));

    RecordType recordType3 = new RecordType(registry, recordProperties1);
    assertTrue(recordType1.isEquivalentTo(recordType3));

    RecordType recordType4 = new RecordTypeBuilder(registry)
        .addProperty("a", JSType.STRING)
        .addProperty("b", JSType.NUMBER)
        .build();

    assertTrue(recordType1.isEquivalentTo(recordType4));
  }

  @Test
  public void testIsSubtype() {
    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("a", new RecordProperty("a", JSType.STRING));
    recordProperties1.put("b", new RecordProperty("b", JSType.NUMBER));

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("a", new RecordProperty("a", JSType.STRING));
    recordProperties2.put("b", new RecordProperty("b", JSType.NUMBER));
    recordProperties2.put("c", new RecordProperty("c", JSType.NUMBER));

    RecordType recordType1 = new RecordType(registry, recordProperties1);
    RecordType recordType2 = new RecordType(registry, recordProperties2);

    assertFalse(recordType1.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
    assertFalse(recordType1.isSubtype(registry.getNativeObjectType(JSTypeNative.NUMBER_TYPE)));
    assertFalse(recordType2.isSubtype(recordType1));

    RecordType recordType3 = new RecordTypeBuilder(registry)
        .addProperty("a", JSType.STRING)
        .addProperty("b", JSType.NUMBER)
        .addProperty("c", JSType.NUMBER)
        .build();

    assertFalse(recordType1.isSubtype(recordType3));

    RecordType recordType4 = new RecordTypeBuilder(registry)
        .addProperty("a", JSType.STRING)
        .addProperty("b", JSType.NUMBER)
        .build();

    assertTrue(recordType1.isSubtype(recordType4));
  }

  @Test
  public void testGetLeastSupertype() {
    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("a", new RecordProperty("a", JSType.STRING));
    recordProperties1.put("b", new RecordProperty("b", JSType.NUMBER));

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("a", new RecordProperty("a", JSType.STRING));
    recordProperties2.put("b", new RecordProperty("b", JSType.NUMBER));
    recordProperties2.put("c", new RecordProperty("c", JSType.NUMBER));

    RecordType recordType1 = new RecordType(registry, recordProperties1);
    RecordType recordType2 = new RecordType(registry, recordProperties2);

    RecordType leastSupertype = recordType1.getLeastSupertype(recordType2);
    assertEquals(recordType1.getClass(), leastSupertype.getClass());
    assertEquals(2, leastSupertype.getDeclaredPropertyNames().length);

    RecordType recordType3 = new RecordTypeBuilder(registry)
        .addProperty("a", JSType.STRING)
        .addProperty("b", JSType.NUMBER)
        .addProperty("c", JSType.NUMBER)
        .build();

    RecordType greatestSubtype = recordType2.getGreatestSubtype(recordType1);
    assertEquals(recordType2.getClass(), greatestSubtype.getClass());
    assertEquals(3, greatestSubtype.getDeclaredPropertyNames().length);

    RecordType greatestLeastSupertype = greatestSubtype.getLeastSupertype(recordType3);
    assertEquals(3, greatestLeastSupertype.getDeclaredPropertyNames().length);
  }

  @Test
  public void testDefineProperty() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();

    RecordType recordType = new RecordType(registry, recordProperties);

    assertFalse(recordType.hasProperty("a"));
    assertFalse(recordType.hasProperty("b"));

    recordType.defineProperty("a", JSType.STRING, false, null);
    assertTrue(recordType.hasProperty("a"));
    assertEquals(JSType.STRING, recordType.getPropertyType("a"));

    recordType.defineProperty("b", JSType.NUMBER, false, null);
    assertTrue(recordType.hasProperty("b"));
    assertEquals(JSType.NUMBER, recordType.getPropertyType("b"));

    try {
      recordType.defineProperty("a", null, false, null);
      fail("Expected an IllegalStateException when defining property with null type.");
    } catch (IllegalStateException e) {
      // Pass
    }

    try {
      recordType.defineProperty("a", JSType.STRING, true, null);
      fail("Expected an IllegalStateException when defining property with a defined accessor.");
    } catch (IllegalStateException e) {
      // Pass
    }
  }

  @Test
  public void testGetImplicitPrototype() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    RecordType recordType = new RecordType(registry, recordProperties);

    ObjectType implicitPrototype = recordType.getImplicitPrototype();
    assertEquals(JSTypeNative.OBJECT_TYPE, implicitPrototype.getClass());
  }

  @Test
  public void testResolveInternal() {
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    RecordType recordType = new RecordType(registry, recordProperties);

    JSType resolvedType = recordType.resolveInternal(null, null);
    assertEquals(recordType.getClass(), resolvedType.getClass());

    recordProperties.put("a", new RecordProperty("a", JSType.STRING));
    recordProperties.put("b", new RecordProperty("b", JSType.NUMBER));

    recordType = new RecordType(registry, recordProperties);

    JSType typeA = JSType.getUnknownType();
    JSType typeB = JSType.NUMBER;

    recordType.getPropertyNode("a") = new Node(Node.ASSIGN, null, new Node(Node.IDENT, "a"));
    recordType.getPropertyNode("b") = new Node(Node.ASSIGN, null, new Node(Node.IDENT, "b"));

    recordType.resolveInternal(null, null);

    JSType resolvedPropertyTypeA = recordType.getPropertyType("a");
    assertEquals(JSType.STRING, resolvedPropertyTypeA);

    JSType resolvedPropertyTypeB = recordType.getPropertyType("b");
    assertEquals(JSType.NUMBER, resolvedPropertyTypeB);
  }

  @Test
  public void testUnionOfSubtypes() {
    Map<String, RecordProperty> recordProperties1 = Maps.newHashMap();
    recordProperties1.put("a", new RecordProperty("a", JSType.STRING));
    recordProperties1.put("b", new RecordProperty("b", JSType.NUMBER));

    Map<String, RecordProperty> recordProperties2 = Maps.newHashMap();
    recordProperties2.put("a", new RecordProperty("a", JSType.NUMBER));
    recordProperties2.put("c", new RecordProperty("c", JSType.BOOLEAN));

    RecordType recordType1 = new RecordType(registry, recordProperties1);
    RecordType recordType2 = new RecordType(registry, recordProperties2);

    UnionTypeBuilder unionBuilder = new UnionTypeBuilder(registry);

    ObjectType objectTypeA = new ObjectType(registry, null, null);
    objectTypeA.defineProperty("a", JSType.STRING, false, null);
    unionBuilder.addAlternate(objectTypeA);

    ObjectType objectTypeB = new ObjectType(registry, null, null);
    objectTypeB.defineProperty("a", JSType.NUMBER, false, null);
    unionBuilder.addAlternate(objectTypeB);

    ObjectType unionType = unionBuilder.build();

    assertEquals(recordType1.getClass(), recordType1.getClass());
    assertEquals(recordType2.getClass(), recordType2.getClass());
    assertEquals(UnionType.class, unionType.getClass());

    JSType greatestSubtype = recordType1.getGreatestSubtype(recordType2);
    assertEquals(unionType.getClass(), greatestSubtype.getClass());
  }
}