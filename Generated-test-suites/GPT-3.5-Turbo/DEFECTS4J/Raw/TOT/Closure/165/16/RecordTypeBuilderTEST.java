import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeBuilderTest {
  
  private JSTypeRegistry registry;
  private RecordTypeBuilder builder;
  private JSType objectType;
  private String propertyName;
  private JSType propertyType;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    builder = new RecordTypeBuilder(registry);
    objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    propertyName = "property";
    propertyType = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
  }

  /**
   * Test the addProperty method when adding a new property
   * to the record type.
   */
  @Test
  public void testAddProperty_WhenNewPropertyAdded_ReturnsBuilder() {
    RecordTypeBuilder result = builder.addProperty(propertyName, propertyType, null);
    assertNotNull(result);
  }

  /**
   * Test the addProperty method when adding a duplicate property 
   * to the record type. It should return null.
   */
  @Test
  public void testAddProperty_WhenDuplicatePropertyAdded_ReturnsNull() {
    builder.addProperty(propertyName, propertyType, null);
    RecordTypeBuilder result = builder.addProperty(propertyName, propertyType, null);
    assertNull(result);
  }

  /**
   * Test the build method when the record type is empty.
   * It should return the object type.
   */
  @Test
  public void testBuild_WhenEmptyRecord_ReturnsObjectType() {
    JSType result = builder.build();
    assertEquals(objectType, result);
  }

  /**
   * Test the build method when the record type is not empty.
   * It should return a new RecordType.
   */
  @Test
  public void testBuild_WhenNotEmptyRecord_ReturnsRecordType() {
    builder.addProperty(propertyName, propertyType, null);
    JSType result = builder.build();
    assertTrue(result instanceof RecordType);
  }

  /**
   * Test the build method when the record type is not empty.
   * It should return a new RecordType with the added property.
   */
  @Test
  public void testBuild_WhenNotEmptyRecord_ReturnsRecordTypeWithProperty() {
    builder.addProperty(propertyName, propertyType, null);
    JSType result = builder.build();
    RecordType recordType = (RecordType)result;
    assertTrue(recordType.hasProperty(propertyName));
    assertEquals(propertyType, recordType.getPropertyType(propertyName));
  }

}