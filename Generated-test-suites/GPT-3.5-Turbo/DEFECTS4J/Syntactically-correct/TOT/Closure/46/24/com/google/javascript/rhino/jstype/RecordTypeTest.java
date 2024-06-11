import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.*;

public class RecordTypeTest {

  @Test
  public void testConstructor() {
    // Typical Use Case: Create a record type with two properties
    Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
    properties.put("a", new RecordTypeBuilder.RecordProperty(new JSType(), null));
    properties.put("b", new RecordTypeBuilder.RecordProperty(new JSType(), null));
    RecordType recordType = new RecordType(new JSTypeRegistry(), properties);

    // Verify that the properties are set correctly
    assertTrue(recordType.hasProperty("a"));
    assertTrue(recordType.hasProperty("b"));
    assertEquals(new JSType(), recordType.getPropertyType("a"));
    assertEquals(new JSType(), recordType.getPropertyType("b"));
  }

  @Test
  public void testIsEquivalentTo() {
    // Typical Use Case: Compare two identical record types
    Map<String, RecordTypeBuilder.RecordProperty> properties1 = new HashMap<>();
    properties1.put("a", new RecordTypeBuilder.RecordProperty(new JSType(), null));
    properties1.put("b", new RecordTypeBuilder.RecordProperty(new JSType(), null));
    RecordType recordType1 = new RecordType(new JSTypeRegistry(), properties1);

    Map<String, RecordTypeBuilder.RecordProperty> properties2 = new HashMap<>();
    properties2.put("a", new RecordTypeBuilder.RecordProperty(new JSType(), null));
    properties2.put("b", new RecordTypeBuilder.RecordProperty(new JSType(), null));
    RecordType recordType2 = new RecordType(new JSTypeRegistry(), properties2);

    assertTrue(recordType1.isEquivalentTo(recordType2));
  }

  @Test
  public void testGetLeastSupertype() {
    // Typical Use Case: Get the least supertype of two record types with common properties
    Map<String, RecordTypeBuilder.RecordProperty> properties1 = new HashMap<>();
    properties1.put("a", new RecordTypeBuilder.RecordProperty(new JSType(), null));
    properties1.put("b", new RecordTypeBuilder.RecordProperty(new JSType(), null));
    RecordType recordType1 = new RecordType(new JSTypeRegistry(), properties1);

    Map<String, RecordTypeBuilder.RecordProperty> properties2 = new HashMap<>();
    properties2.put("a", new RecordTypeBuilder.RecordProperty(new JSType(), null));
    properties2.put("b", new RecordTypeBuilder.RecordProperty(new JSType(), null));
    properties2.put("c", new RecordTypeBuilder.RecordProperty(new JSType(), null));
    RecordType recordType2 = new RecordType(new JSTypeRegistry(), properties2);

    RecordType leastSupertype = (RecordType) recordType1.getLeastSupertype(recordType2);

    // Verify that the least supertype contains properties from both record types
    assertTrue(leastSupertype.hasProperty("a"));
    assertTrue(leastSupertype.hasProperty("b"));
    assertTrue(leastSupertype.hasProperty("c"));
  }

  @Test
  public void testIsSubtype() {
    // Typical Use Case: Check if one record type is a subtype of another
    Map<String, RecordTypeBuilder.RecordProperty> properties1 = new HashMap<>();
    properties1.put("a", new RecordTypeBuilder.RecordProperty(new JSType(), null));
    properties1.put("b", new RecordTypeBuilder.RecordProperty(new JSType(), null));
    RecordType recordType1 = new RecordType(new JSTypeRegistry(), properties1);

    Map<String, RecordTypeBuilder.RecordProperty> properties2 = new HashMap<>();
    properties2.put("a", new RecordTypeBuilder.RecordProperty(new JSType(), null));
    properties2.put("b", new RecordTypeBuilder.RecordProperty(new JSType(), null));
    properties2.put("c", new RecordTypeBuilder.RecordProperty(new JSType(), null));
    RecordType recordType2 = new RecordType(new JSTypeRegistry(), properties2);

    assertTrue(recordType1.isSubtype(recordType2));
  }

  @Test
  public void testResolveInternal() {
    // Typical Use Case: Resolve the record type and replace properties with resolved types
    Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
    properties.put("a", new RecordTypeBuilder.RecordProperty(new JSType(), null));
    properties.put("b", new RecordTypeBuilder.RecordProperty(new JSType(), null));
    RecordType recordType = new RecordType(new JSTypeRegistry(), properties);

    StaticScope<JSType> scope = new StaticScope<>();
    ErrorReporter errorReporter = new ErrorReporter();

    // Resolve the record type
    JSType resolvedType = recordType.resolveInternal(errorReporter, scope);

    // Verify that the record type has been resolved and properties have been replaced with resolved types
    assertTrue(recordType.getPropertyType("a").isResolved());
    assertTrue(recordType.getPropertyType("b").isResolved());
    assertEquals(recordType.getPropertyType("a"), resolvedType.getPropertyType("a"));
    assertEquals(recordType.getPropertyType("b"), resolvedType.getPropertyType("b"));
  }

}