import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.rhino.Node;


public class RecordTypeBuilderTest {

  private RecordTypeBuilder builder;

  @Before
  public void setUp() {
    builder = new RecordTypeBuilder(new JSTypeRegistry());
  }

  @Test
  public void testAddProperty_ValidProperty() {
    String propertyName = "name";
    JSType propertyType = new JSType();
    Node propertyNode = new Node();

    RecordTypeBuilder result = builder.addProperty(propertyName, propertyType, propertyNode);

    assertNotNull("Result should not be null", result);
    // Verify that the property is added correctly
    assertEquals("The property should be added correctly", propertyType, builder.build().getProperty(propertyName));
  }

  @Test
  public void testAddProperty_DuplicateProperty() {
    String propertyName = "name";
    JSType propertyType = new JSType();
    Node propertyNode = new Node();

    // Add the property for the first time
    builder.addProperty(propertyName, propertyType, propertyNode);

    // Add the property for the second time, should return null
    RecordTypeBuilder result = builder.addProperty(propertyName, propertyType, propertyNode);

    assertNull("Result should be null", result);
  }

  @Test
  public void testBuild_EmptyRecord() {
    // Build an empty record
    JSType emptyRecord = builder.build();

    // Verify that it returns NATIVE_OBJECT_TYPE
    assertEquals("An empty record should return NATIVE_OBJECT_TYPE", JSTypeNative.OBJECT_TYPE, emptyRecord);
  }

  @Test
  public void testBuild_NonEmptyRecord() {
    // Add properties to the builder
    String propertyName1 = "name1";
    JSType propertyType1 = new JSType();
    Node propertyNode1 = new Node();

    String propertyName2 = "name2";
    JSType propertyType2 = new JSType();
    Node propertyNode2 = new Node();

    builder.addProperty(propertyName1, propertyType1, propertyNode1)
           .addProperty(propertyName2, propertyType2, propertyNode2);

    // Build the record
    JSType recordType = builder.build();

    // Verify that the properties are set correctly
    assertEquals("Property 1 should be set correctly", propertyType1, recordType.getProperty(propertyName1));
    assertEquals("Property 2 should be set correctly", propertyType2, recordType.getProperty(propertyName2));
  }

  // Add more tests for edge cases and error scenarios

}