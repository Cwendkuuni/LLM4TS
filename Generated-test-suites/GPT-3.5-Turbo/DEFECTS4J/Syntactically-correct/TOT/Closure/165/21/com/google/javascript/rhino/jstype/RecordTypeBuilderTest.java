import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeBuilderTest {
  private RecordTypeBuilder recordTypeBuilder;

  @Before
  public void setUp() {
    recordTypeBuilder = new RecordTypeBuilder(new JSTypeRegistry());
  }

  @Test
  public void testAddProperty_emptyRecord() {
    // Test empty record
    JSType result = recordTypeBuilder.build();
    assertEquals("Expected result to be Object Type for empty record",
        JSTypeNative.OBJECT_TYPE, result);
  }

  @Test
  public void testAddProperty_validProperty() {
    // Test adding a valid property
    recordTypeBuilder.addProperty("name", JSTypeNative.STRING_TYPE, null);
    JSType result = recordTypeBuilder.build();

    assertNotNull("Expected result to be not null for valid property",
        result);

    assertTrue("Expected result to be RecordType for valid property",
        result instanceof RecordType);

    RecordType recordType = (RecordType) result;
    assertTrue("Expected recordType to contain property 'name'",
        recordType.getProperties().containsKey("name"));
    assertEquals("Expected recordType property 'name' to have type STRING",
        JSTypeNative.STRING_TYPE,
        recordType.getProperties().get("name").getType());
  }

  @Test
  public void testAddProperty_duplicateProperty() {
    // Test adding a duplicate property
    recordTypeBuilder.addProperty("name", JSTypeNative.STRING_TYPE, null);
    RecordTypeBuilder result = recordTypeBuilder.addProperty("name",
        JSTypeNative.NUMBER_TYPE, null);

    assertNull("Expected result to be null for duplicate property",
        result);

    JSType recordType = recordTypeBuilder.build();
    assertNotNull("Expected result to be not null for duplicate property",
        recordType);

    assertTrue("Expected recordType to contain property 'name'",
        recordType.getProperties().containsKey("name"));
    assertEquals("Expected recordType property 'name' to have type STRING",
        JSTypeNative.STRING_TYPE,
        recordType.getProperties().get("name").getType());
  }

  @Test
  public void testAddProperty_multipleProperties() {
    // Test adding multiple properties
    recordTypeBuilder.addProperty("name", JSTypeNative.STRING_TYPE, null);
    recordTypeBuilder.addProperty("age", JSTypeNative.NUMBER_TYPE, null);
    recordTypeBuilder.addProperty("isStudent", JSTypeNative.BOOLEAN_TYPE, null);
    JSType result = recordTypeBuilder.build();

    assertNotNull("Expected result to be not null for multiple properties",
        result);

    assertTrue("Expected result to be RecordType for multiple properties",
        result instanceof RecordType);

    RecordType recordType = (RecordType) result;
    assertEquals("Expected recordType to contain 3 properties",
        3, recordType.getProperties().size());

    assertTrue("Expected recordType to contain property 'name'",
        recordType.getProperties().containsKey("name"));
    assertEquals("Expected recordType property 'name' to have type STRING",
        JSTypeNative.STRING_TYPE,
        recordType.getProperties().get("name").getType());

    assertTrue("Expected recordType to contain property 'age'",
        recordType.getProperties().containsKey("age"));
    assertEquals("Expected recordType property 'age' to have type NUMBER",
        JSTypeNative.NUMBER_TYPE,
        recordType.getProperties().get("age").getType());

    assertTrue("Expected recordType to contain property 'isStudent'",
        recordType.getProperties().containsKey("isStudent"));
    assertEquals("Expected recordType property 'isStudent' to have type BOOLEAN",
        JSTypeNative.BOOLEAN_TYPE,
        recordType.getProperties().get("isStudent").getType());
  }
}