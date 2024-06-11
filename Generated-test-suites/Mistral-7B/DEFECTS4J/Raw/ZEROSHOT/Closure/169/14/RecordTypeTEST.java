import static org.junit.Assert.*;
import static com.google.javascript.rhino.testing.TypeEquivalenceTester.assertTypesEquivalent;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeFactory;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.PrototypeObjectType;
import com.google.javascript.rhino.jstype.UnionType;
import com.google.javascript.rhino.jstype.annotations.DoNotSerialize;
import com.google.javascript.rhino.testing.PropertyTester;
import com.google.javascript.rhino.testing.TypeEquivalenceTester;

import java.util.Map;
import java.util.SortedMap;

/**
 * Tests for {@link RecordType}.
 */
public class RecordTypeTest {

  private ErrorReporter t;

  @Before
  public void setup() {
    t = new ErrorReporter();
  }

  @Test
  public void testRecordTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry(t, null);
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", JSTypeFactory.STRING));
    properties.put("age", new RecordProperty("age", JSTypeFactory.NUMBER));

    RecordType recordType = new RecordType(registry, properties, true);
    assertEquals(JSTypeFactory.RECORD, recordType.getType());
    assertEquals("Record", recordType.getName());
    assertEquals(2, recordType.getPropertyCount());

    // Test getters
    assertEquals("name", recordType.getPropertyNames()[0]);
    assertEquals("age", recordType.getPropertyNames()[1]);
    assertEquals(JSTypeFactory.STRING, recordType.getPropertyType("name"));
    assertEquals(JSTypeFactory.NUMBER, recordType.getPropertyType("age"));

    // Test setters
    JSType newStringType = JSTypeFactory.STRING.subtype(JSTypeFactory.BOOLEAN);
    recordType.setPropertyType("name", newStringType, null);
    assertEquals(newStringType, recordType.getPropertyType("name"));

    // Test freezing
    recordType.freeze();
    try {
      recordType.setPropertyType("name", JSTypeFactory.NUMBER, null);
      fail();
    } catch (UnsupportedOperationException e) {
      // expected
    }
  }

  @Test
  public void testRecordTypeEquivalence() {
    JSTypeRegistry registry = new JSTypeRegistry(t, null);
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("name", new RecordProperty("name", JSTypeFactory.STRING));
    properties1.put("age", new RecordProperty("age", JSTypeFactory.NUMBER));

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("name", new RecordProperty("name", JSTypeFactory.STRING));
    properties2.put("age", new RecordProperty("age", JSTypeFactory.NUMBER));

    RecordType recordType1 = new RecordType(registry, properties1, true);
    RecordType recordType2 = new RecordType(registry, properties2, true);
    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, false));
    assertTrue(recordType2.checkRecordEquivalenceHelper(recordType1, false));

    // Test with unknowns
    properties1.put("unknown", new RecordProperty("unknown", JSTypeFactory.UNKNOWN));
    properties2.put("unknown", new RecordProperty("unknown", JSTypeFactory.NUMBER));
    RecordType recordTypeWithUnknown1 = new RecordType(registry, properties1, true);
    RecordType recordTypeWithUnknown2 = new RecordType(registry, properties2, true);
    assertTrue(recordTypeWithUnknown1.checkRecordEquivalenceHelper(recordTypeWithUnknown2, true));
    assertTrue(recordTypeWithUnknown2.checkRecordEquivalenceHelper(recordTypeWithUnknown1, true));

    // Test with mismatches
    properties1.put("age", new RecordProperty("age", JSTypeFactory.NUMBER));
    properties2.put("age", new RecordProperty("age", JSTypeFactory.STRING));
    RecordType recordTypeMismatch1 = new RecordType(registry, properties1, true);
    RecordType recordTypeMismatch2 = new RecordType(registry, properties2, true);
    assertFalse(recordTypeMismatch1.checkRecordEquivalenceHelper(recordTypeMismatch2, false));
    assertFalse(recordTypeMismatch2.checkRecordEquivalenceHelper(recordTypeMismatch1, false));
  }

  @Test
  public void testRecordTypeSubtyping() {
    JSTypeRegistry registry = new JSTypeRegistry(t, null);
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("name", new RecordProperty("name", JSTypeFactory.STRING));
    properties1.put("age", new RecordProperty("age", JSTypeFactory.NUMBER));

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("name", new RecordProperty("name", JSTypeFactory.STRING));
    properties2.put("age", new RecordProperty("age", JSTypeFactory.NUMBER));
    properties2.put("gender", new RecordProperty("gender", JSTypeFactory.STRING));

    RecordType recordType1 = new RecordType(registry, properties1, true);
    RecordType recordType2 = new RecordType(registry, properties2, true);

    // Test greatest subtype
    JSType greatestSubtype = recordType1.getGreatestSubtype(recordType2);
    assertEquals(recordType2, greatestSubtype);

    // Test isSubtype
    JSType recordType3 = new RecordType(registry, Maps.newHashMap(), true);
    assertFalse(recordType1.isSubtype(recordType3));

    // Test isSubtype with record type as argument
    RecordType recordType3Record = new RecordType(registry, Maps.newHashMap(), true);
    assertFalse(recordType1.isSubtype(recordType3Record));

    // Test isSubtype with JSType as argument
    assertTrue(recordType1.isSubtype(JSTypeFactory.RECORD));

    // Test isSubtype with ObjectType as argument
    assertTrue(recordType1.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));

    // Test isSubtype with another record type as argument
    RecordType recordType4 = new RecordType(registry, properties1, true);
    assertTrue(recordType1.isSubtype(recordType4));

    // Test isSubtype with a subtype of another record type as argument
    Map<String, RecordProperty> properties3 = Maps.newHashMap();
    properties3.putAll(properties1);
    properties3.put("gender", new RecordProperty("gender", JSTypeFactory.STRING));
    RecordType recordType5 = new RecordType(registry, properties3, true);
    assertTrue(recordType1.isSubtype(recordType5));

    // Test isSubtype with a supertype of another record type as argument
    Map<String, RecordProperty> properties4 = Maps.newHashMap();
    properties4.putAll(properties1);
    properties4.put("gender", new RecordProperty("gender", JSTypeFactory.NUMBER));
    RecordType recordType6 = new RecordType(registry, properties4, true);
    assertTrue(recordType6.isSubtype(recordType1));

    // Test isSubtype with a union type as argument
    UnionType unionType = new UnionType(registry, JSTypeFactory.STRING, JSTypeFactory.NUMBER);
    assertFalse(recordType1.isSubtype(unionType));

    // Test isSubtype with an interface as argument
    JSType recordType7Interface = registry.getNativeType(JSTypeNative.INTERFACE_TYPE);
    assertFalse(recordType1.isSubtype(recordType7Interface));
  }

  @Test
  public void testRecordTypeIsFrozen() {
    JSTypeRegistry registry = new JSTypeRegistry(t, null);
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", JSTypeFactory.STRING));
    properties.put("age", new RecordProperty("age", JSTypeFactory.NUMBER));

    RecordType recordType = new RecordType(registry, properties, true);
    assertTrue(recordType.isFrozen());

    // Test setPropertyType
    try {
      recordType.setPropertyType("name", JSTypeFactory.NUMBER, null);
      fail();
    } catch (UnsupportedOperationException e) {
      // expected
    }

    // Test defineProperty
    try {
      recordType.defineProperty("age", JSTypeFactory.STRING, true, null);
      fail();
    } catch (UnsupportedOperationException e) {
      // expected
    }
  }

  @Test
  public void testRecordTypePropertyAccess() {
    JSTypeRegistry registry = new JSTypeRegistry(t, null);
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", JSTypeFactory.STRING));
    properties.put("age", new RecordProperty("age", JSTypeFactory.NUMBER));

    RecordType recordType = new RecordType(registry, properties, true);
    assertNull(recordType.getProperty("unknown"));

    String name = "John";
    Number age = Number.valueOf(30);
    recordType.setProperty("name", name, null);
    recordType.setProperty("age", age, null);

    assertEquals("John", recordType.getProperty("name"));
    assertEquals(30, recordType.getProperty("age").toNumber().intValue());
  }

  @Test
  public void testRecordTypePropertyAccessWithInferredTypes() {
    JSTypeRegistry registry = new JSTypeRegistry(t, null);
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", null));
    properties.put("age", new RecordProperty("age", null));

    RecordType recordType = new RecordType(registry, properties, false);

    String name = "John";
    recordType.setProperty("name", name);

    assertEquals(name.getClass(), recordType.getPropertyType("name").getClass());
    assertEquals("John", recordType.getProperty("name"));

    Number age = Number.valueOf(30);
    recordType.setProperty("age", age);

    assertEquals(age.getClass(), recordType.getPropertyType("age").getClass());
    assertEquals(age.intValue(), recordType.getProperty("age").toNumber().intValue());
  }

  @Test
  public void testRecordTypePropertyAccessWithInferredAndDeclaredTypes() {
    JSTypeRegistry registry = new JSTypeRegistry(t, null);
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", JSTypeFactory.STRING));
    properties.put("age", new RecordProperty("age", null));

    RecordType recordType = new RecordType(registry, properties, true);

    String name = "John";
    recordType.setProperty("name", name);

    assertEquals(name.getClass(), recordType.getPropertyType("name").getClass());
    assertEquals("John", recordType.getProperty("name"));

    Number age = Number.valueOf(30);
    recordType.setProperty("age", age);

    assertEquals(age.getClass(), recordType.getPropertyType("age").getClass());
    assertEquals(age.intValue(), recordType.getProperty("age").toNumber().intValue());

    // Test setPropertyType
    recordType.setPropertyType("name", JSTypeFactory.STRING, null);
    recordType.setPropertyType("age", JSTypeFactory.NUMBER, null);

    assertEquals(JSTypeFactory.STRING, recordType.getPropertyType("name"));
    assertEquals(JSTypeFactory.NUMBER, recordType.getPropertyType("age"));
  }

  @Test
  public void testRecordTypePropertyAccessWithInferredTypesWithDefaultValues() {
    JSTypeRegistry registry = new JSTypeRegistry(t, null);
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", null, null, PropertyTester.createNode("name")));
    properties.put("age", new RecordProperty("age", null, null, PropertyTester.createNode("age")));

    RecordType recordType = new RecordType(registry, properties, false);

    String name = "John";
    recordType.setProperty("name", name);

    assertEquals(name.getClass(), recordType.getPropertyType("name").getClass());
    assertEquals("John", recordType.getProperty("name"));

    Number age = Number.valueOf(30);
    recordType.setProperty("age", age);

    assertEquals(age.getClass(), recordType.getPropertyType("age").getClass());
    assertEquals(age.intValue(), recordType.getProperty("age").toNumber().intValue());

    // Test setPropertyType
    recordType.setPropertyType("name", JSTypeFactory.STRING, null);
    recordType.setPropertyType("age", JSTypeFactory.NUMBER, null);

    assertEquals(JSTypeFactory.STRING, recordType.getPropertyType("name"));
    assertEquals(JSTypeFactory.NUMBER, recordType.getPropertyType("age"));

    // Test getPropertyNode
    Node nameNode = recordType.getPropertyNode("name");
    assertNotNull(nameNode);
    assertEquals("name", nameNode.getIdentifier().getName());

    Node ageNode = recordType.getPropertyNode("age");
    assertNotNull(ageNode);
    assertEquals("age", ageNode.getIdentifier().getName());
  }

  @Test
  public void testRecordTypeWithDoNotSerializeAnnotation() {
    JSTypeRegistry registry = new JSTypeRegistry(t, null);
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", JSTypeFactory.STRING));
    properties.put("age", new RecordProperty("age", JSTypeFactory.NUMBER));

    RecordType recordType = new RecordType(registry, properties, true);

    // Test with no annotation
    JSType recordTypeWithoutAnnotation = recordType;

    // Test with annotation on record type
    RecordType recordTypeWithAnnotation = new RecordType(registry, properties, true) {
      @DoNotSerialize
      public boolean isSynthetic() {
        return false;
      }
    };

    // Test with annotation on property
    RecordProperty namePropertyWithAnnotation = new RecordProperty("name", JSTypeFactory.STRING) {
      @DoNotSerialize
      public Node getPropertyNode() {
        return null;
      }
    };
    Map<String, RecordProperty> propertiesWithAnnotation = Maps.newHashMap(properties);
    propertiesWithAnnotation.put("name", namePropertyWithAnnotation);

    RecordType recordTypeWithPropertyAnnotation =
        new RecordType(registry, propertiesWithAnnotation, true);

    // Test equivalence
    assertTypesEquivalent(recordTypeWithoutAnnotation, recordTypeWithAnnotation);
    assertTypesEquivalent(recordType, recordTypeWithPropertyAnnotation);
  }
}