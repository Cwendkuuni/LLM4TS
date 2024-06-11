import static org.junit.Assert.*;
import org.junit.Test;

public class RecordTypeTest {

  @Test
  public void testDefineProperty() {
    // Create a new RecordType object
    RecordType recordType = new RecordType();

    // Define a property with a string type
    recordType.defineProperty("name", "String");

    // Check if the property is defined correctly
    assertEquals("String", recordType.getPropertyType("name"));

    // Define a property with a number type
    recordType.defineProperty("age", "Number");

    // Check if the property is defined correctly
    assertEquals("Number", recordType.getPropertyType("age"));
  }

  @Test
  public void testIsSubtype() {
    // Create two RecordType objects
    RecordType recordType1 = new RecordType();
    recordType1.defineProperty("name", "String");
    recordType1.defineProperty("age", "Number");

    RecordType recordType2 = new RecordType();
    recordType2.defineProperty("name", "String");
    recordType2.defineProperty("age", "Number");

    // Check if recordType1 is a subtype of recordType2
    assertTrue(recordType1.isSubtype(recordType2));

    // Define an additional property in recordType1
    recordType1.defineProperty("gender", "String");

    // Check if recordType1 is still a subtype of recordType2
    assertTrue(recordType1.isSubtype(recordType2));
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    // Create two RecordType objects
    RecordType recordType1 = new RecordType();
    recordType1.defineProperty("name", "String");
    recordType1.defineProperty("age", "Number");

    RecordType recordType2 = new RecordType();
    recordType2.defineProperty("name", "String");

    // Get the greatest subtype of recordType1 and recordType2
    RecordType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);

    // Check if the greatest subtype has the expected properties
    assertTrue(greatestSubtype.hasProperty("name"));
    assertTrue(greatestSubtype.hasProperty("age"));

    // Define an additional property in recordType2
    recordType2.defineProperty("gender", "String");

    // Get the greatest subtype again
    greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);

    // Check if the greatest subtype has the updated properties
    assertTrue(greatestSubtype.hasProperty("name"));
    assertTrue(greatestSubtype.hasProperty("age"));
    assertTrue(greatestSubtype.hasProperty("gender"));
  }

  @Test
  public void testIsEquivalentTo() {
    // Create two RecordType objects
    RecordType recordType1 = new RecordType();
    recordType1.defineProperty("name", "String");
    recordType1.defineProperty("age", "Number");

    RecordType recordType2 = new RecordType();
    recordType2.defineProperty("name", "String");
    recordType2.defineProperty("age", "Number");

    // Check if the two record types are equivalent
    assertTrue(recordType1.isEquivalentTo(recordType2));

    // Define an additional property in recordType1
    recordType1.defineProperty("gender", "String");

    // Check if the two record types are still equivalent
    assertTrue(recordType1.isEquivalentTo(recordType2));

    // Define an additional property in recordType2
    recordType2.defineProperty("profession", "String");

    // Check if the two record types are no longer equivalent
    assertFalse(recordType1.isEquivalentTo(recordType2));
  }
}