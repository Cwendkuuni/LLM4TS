import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class RecordTypeBuilderTest {
  
  private RecordTypeBuilder builder;
  
  @Before
  public void setUp() {
    builder = new RecordTypeBuilder(new JSTypeRegistry());
  }
  
  @Test
  public void testAddProperty() {
    JSType type = new JSType();
    Node propertyNode = new Node();
    
    // Adding a property for the first time should return the builder itself
    RecordTypeBuilder returnedBuilder = builder.addProperty("name", type, propertyNode);
    assertNotNull(returnedBuilder);
    
    // Adding the same property again should return null
    assertEquals(null, builder.addProperty("name", type, propertyNode));
  }
  
  @Test
  public void testBuildEmptyRecord() {
    JSType recordType = builder.build();
    // An empty record should return the object type
    assertNotNull(recordType);
    assertEquals("object", recordType.getTypeName());
  }
  
  @Test
  public void testBuildNonEmptyRecord() {
    JSType type1 = new JSType();
    JSType type2 = new JSType();
    Node propertyNode1 = new Node();
    Node propertyNode2 = new Node();
    
    builder.addProperty("name1", type1, propertyNode1);
    builder.addProperty("name2", type2, propertyNode2);
    
    JSType recordType = builder.build();
    
    // A non-empty record should return a RecordType with the added properties
    assertNotNull(recordType);
    assertEquals("record", recordType.getTypeName());
    
    // Check that the properties are present in the RecordType
    assertEquals(2, recordType.getProperties().size());
    assertNotNull(recordType.getProperty("name1"));
    assertNotNull(recordType.getProperty("name2"));
  }
}