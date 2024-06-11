import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.testing.Assertions;

import java.util.Map;

public class RecordTypeTest {

  private static JSTypeRegistry registry;

  @org.junit.BeforeClass
  public static void setUp() {
    registry = new JSTypeRegistry();
  }

  @org.junit.Test
  public void testRecordTypeWithSimpleProperties() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", String.class));
    properties.put("age", new RecordProperty("age", Integer.class));

    RecordType expectedType = new RecordType(registry, properties);

    JSType actualType = new RecordTypeBuilder(registry)
        .addProperty("name", String.class)
        .addProperty("age", Integer.class)
        .build();

    Assertions.assertEquals(expectedType, actualType);
  }

  @org.junit.Test
  public void testRecordTypeWithDuplicateProperties() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", String.class));
    properties.put("name", new RecordProperty("name", String.class));

    try {
      new RecordType(registry, properties);
      Assertions.fail("Expected an IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
    }
  }

  @org.junit.Test
  public void testRecordTypeWithNullProperties() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", null);

    try {
      new RecordType(registry, properties);
      Assertions.fail("Expected an IllegalStateException");
    } catch (IllegalStateException e) {
      Assertions.assertEquals("RecordProperty associated with a property should not be null!", e.getMessage());
    }
  }

  @org.junit.Test
  public void testRecordTypeEquivalence() {
    Map<String, RecordProperty> prop1 = Maps.newHashMap();
    prop1.put("name", new RecordProperty("name", String.class));
    prop1.put("age", new RecordProperty("age", Integer.class));

    Map<String, RecordProperty> prop2 = Maps.newHashMap();
    prop2.put("age", new RecordProperty("age", Integer.class));
    prop2.put("name", new RecordProperty("name", String.class));

    RecordType type1 = new RecordType(registry, prop1);
    RecordType type2 = new RecordType(registry, prop2);

    Assertions.assertTrue(type1.isEquivalentTo(type2));
  }

  @org.junit.Test
  public void testRecordTypeSubtyping() {
    Map<String, RecordProperty> prop1 = Maps.newHashMap();
    prop1.put("name", new RecordProperty("name", String.class));
    prop1.put("age", new RecordProperty("age", Integer.class));

    Map<String, RecordProperty> prop2 = Maps.newHashMap();
    prop2.put("name", new RecordProperty("name", String.class));
    prop2.put("age", new RecordProperty("age", Number.class));

    RecordType type1 = new RecordType(registry, prop1);
    RecordType type2 = new RecordType(registry, prop2);

    JSType subtype = type1.getGreatestSubtype(type2);
    Assertions.assertEquals(type2, subtype);

    // Test with empty record type
    JSType emptyRecordType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    JSType subtypeOfEmptyRecordType = type1.getGreatestSubtype(emptyRecordType);
    Assertions.assertEquals(type1, subtypeOfEmptyRecordType);
  }

  @org.junit.Test
  public void testRecordTypeFrozen() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", String.class));
    properties.put("age", new RecordProperty("age", Integer.class));

    RecordType recordType = new RecordType(registry, properties);
    recordType.defineProperty("gender", String.class, false, Node.NULL);

    Assertions.assertTrue(recordType.isFrozen());
    Assertions.assertFalse(recordType.defineProperty("gender", String.class, false, Node.NULL));
  }

  @org.junit.Test
  public void testRecordTypeSubtypingWithObjectType() {
    Map<String, RecordProperty> prop1 = Maps.newHashMap();
    prop1.put("name", new RecordProperty("name", String.class));
    prop1.put("age", new RecordProperty("age", Integer.class));

    RecordType type1 = new RecordType(registry, prop1);
    ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

    JSType subtype = type1.getGreatestSubtype(objectType);
    Assertions.assertEquals(objectType, subtype);
  }

  @org.junit.Test
  public void testRecordTypeSubtypingWithSameProperties() {
    Map<String, RecordProperty> prop1 = Maps.newHashMap();
    prop1.put("name", new RecordProperty("name", String.class));
    prop1.put("age", new RecordProperty("age", Integer.class));

    Map<String, RecordProperty> prop2 = Maps.newHashMap();
    prop2.put("name", new RecordProperty("name", String.class));
    prop2.put("age", new RecordProperty("age", Integer.class));

    RecordType type1 = new RecordType(registry, prop1);
    RecordType type2 = new RecordType(registry, prop2);

    JSType subtype = type1.getGreatestSubtype(type2);
    Assertions.assertEquals(type1, subtype);
  }

  @org.junit.Test
  public void testRecordTypeSubtypingWithDifferentOrderProperties() {
    Map<String, RecordProperty> prop1 = Maps.newHashMap();
    prop1.put("age", new RecordProperty("age", Integer.class));
    prop1.put("name", new RecordProperty("name", String.class));

    Map<String, RecordProperty> prop2 = Maps.newHashMap();
    prop2.put("name", new RecordProperty("name", String.class));
    prop2.put("age", new RecordProperty("age", Integer.class));

    RecordType type1 = new RecordType(registry, prop1);
    RecordType type2 = new RecordType(registry, prop2);

    JSType subtype = type1.getGreatestSubtype(type2);
    Assertions.assertEquals(type2, subtype);
  }
}