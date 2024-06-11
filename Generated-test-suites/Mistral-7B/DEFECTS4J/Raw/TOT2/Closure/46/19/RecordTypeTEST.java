import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

  private static final JSTypeRegistry registry = new JSTypeRegistry();

  @Test
  public void testRecordTypeCreation() {
    RecordProperty recordProp1 = new RecordProperty("name", registry.getStringType());
    RecordProperty recordProp2 = new RecordProperty("age", registry.getNumberType());

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", recordProp1);
    properties.put("age", recordProp2);

    RecordType recordType = new RecordType(registry, properties);

    assertEquals("Expected record type creation to succeed.", recordType, recordType);
  }

  @Test(expected = IllegalStateException.class)
  public void testNullRecordProperty() {
    RecordProperty recordProp1 = new RecordProperty("name", registry.getStringType());
    RecordProperty recordProp2 = null;

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", recordProp1);
    properties.put("age", recordProp2);

    try {
      new RecordType(registry, properties);
      fail("Expected an IllegalStateException.");
    } catch (IllegalStateException e) {
      // Expected
    }
  }

  @Test
  public void testIsEquivalentTo() {
    RecordProperty recordProp1 = new RecordProperty("name", registry.getStringType());
    RecordProperty recordProp2 = new RecordProperty("name", registry.getStringType());
    RecordProperty recordProp3 = new RecordProperty("age", registry.getNumberType());

    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("name", recordProp1);
    properties1.put("age", recordProp3);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("name", recordProp1);
    properties2.put("name", recordProp2); // Duplicate property
    properties2.put("age", recordProp3);

    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);

    // Record types with the same properties should be equivalent.
    assertTrue("Unexpectedly, record types are not equivalent.",
        recordType1.isEquivalentTo(recordType2));

    // Record types with different properties should not be equivalent.
    RecordType recordType3 = new RecordType(registry, Maps.newHashMap());
    assertFalse("Unexpectedly, record types are equivalent.",
        recordType1.isEquivalentTo(recordType3));
  }

  @Test
  public void testGetImplicitPrototype() {
    RecordType recordType = new RecordType(registry, Maps.newHashMap());
    ObjectType expectedPrototype = registry.getNativeObjectType(
        JSTypeNative.OBJECT_TYPE);

    assertEquals("Unexpectedly, the implicit prototype of the record type is not as expected.",
        expectedPrototype, recordType.getImplicitPrototype());
  }

  @Test
  public void testDefineProperty() {
    RecordProperty recordProp = new RecordProperty("property", registry.getNumberType());
    RecordType recordType = new RecordType(registry, Maps.newHashMap());

    recordType.defineProperty("property", recordProp.getType(), false, null);

    // Verify that the property was defined.
    assertNotNull("Expected property to be defined.", recordType.getPropertyNode("property"));
    assertEquals("Unexpected type for property.", recordProp.getType(), recordType.getPropertyType("property"));
  }

  @Test(expected = IllegalStateException.class)
  public void testDefinePropertyAfterFreeze() {
    RecordProperty recordProp = new RecordProperty("property", registry.getNumberType());
    RecordType recordType = new RecordType(registry, Maps.newHashMap());
    recordType.defineProperty("property", recordProp.getType(), false, null);
    recordType.isFrozen = true;

    try {
      recordType.defineProperty("property", recordProp.getType(), false, null);
      fail("Expected an IllegalStateException.");
    } catch (IllegalStateException e) {
      // Expected
    }
  }

  @Test
  public void testGetLeastSupertype() {
    RecordProperty recordProp1 = new RecordProperty("name", registry.getStringType());
    RecordProperty recordProp2 = new RecordProperty("age", registry.getNumberType());

    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("name", recordProp1);
    properties1.put("age", recordProp2);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("name", recordProp1);
    properties2.put("age", recordProp1); // Duplicate property
    properties2.put("city", new RecordProperty("city", registry.getStringType()));

    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);

    RecordType leastCommonSupertype = recordType1.getLeastSupertype(recordType2);

    // The least common supertype of the record types should be a record type
    // with the intersection of their properties.
    Map<String, RecordProperty> expectedProperties = Maps.newHashMap();
    expectedProperties.put("name", recordProp1);
    RecordType expectedRecordType = new RecordType(registry, expectedProperties);

    assertEquals("Unexpected least common supertype.", expectedRecordType, leastCommonSupertype);
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    RecordProperty recordProp1 = new RecordProperty("name", registry.getStringType());
    RecordProperty recordProp2 = new RecordProperty("age", registry.getNumberType());
    RecordProperty recordProp3 = new RecordProperty("city", registry.getStringType());

    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("name", recordProp1);
    properties1.put("age", recordProp2);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("name", recordProp1);
    properties2.put("age", recordProp2);
    properties2.put("city", recordProp3);

    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);

    // The greatest subtype of recordType1 with respect to recordType2 should
    // be a record type with the union of their properties.
    Map<String, RecordProperty> expectedProperties = Maps.newHashMap();
    expectedProperties.putAll(properties1);
    expectedProperties.putAll(properties2);
    RecordType expectedRecordType = new RecordType(registry, expectedProperties);

    assertEquals("Unexpected greatest subtype.", expectedRecordType, recordType1.getGreatestSubtypeHelper(recordType2));
  }

  @Test
  public void testIsSubtype() {
    RecordProperty recordProp1 = new RecordProperty("name", registry.getStringType());
    RecordProperty recordProp2 = new RecordProperty("age", registry.getNumberType());

    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("name", recordProp1);
    properties1.put("age", recordProp2);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("name", recordProp1);
    properties2.put("age", recordProp2);

    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);

    assertTrue("Unexpectedly, recordType1 is not a subtype of recordType2.", recordType1.isSubtype(recordType2));

    // A subtype of a record type should also be a subtype of any of its
    // supertypes.
    RecordType recordType3 = new RecordType(registry, Maps.newHashMap());
    recordType3.addProperty("name", recordProp1.getType(), null);
    recordType3.addProperty("age", recordProp2.getType(), null);
    assertTrue("Unexpectedly, recordType1 is not a subtype of recordType3.", recordType1.isSubtype(recordType3));

    // RecordType1 should not be a subtype of a type that is not a record type.
    ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    assertFalse("Unexpectedly, recordType1 is a subtype of an ObjectType.", recordType1.isSubtype(objectType));

    // A subtype of a record type should be a subtype of any of its properties.
    RecordType recordType4 = new RecordType(registry, Maps.newHashMap());
    recordType4.addProperty("name", recordProp1.getType(), null);
    assertTrue("Unexpectedly, recordType1 is not a subtype of recordType4.", recordType1.isSubtype(recordType4));
  }
}