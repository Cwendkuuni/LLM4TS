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
  public void testAddProperty() {
    String propertyName = "name";
    JSType propertyType = new JSType(JSTypeNative.STRING_TYPE);
    Node propertyNode = new Node();
    
    // Test adding a property when the record is empty
    assertNotNull(recordTypeBuilder.addProperty(propertyName, propertyType, propertyNode));
  }
  
  @Test
  public void testBuild() {
    // Test building an empty record
    JSType emptyRecord = recordTypeBuilder.build();
    assertNotNull(emptyRecord);
    
    // Test building a record with properties
    String propertyName1 = "name1";
    JSType propertyType1 = new JSType(JSTypeNative.STRING_TYPE);
    Node propertyNode1 = new Node();
    recordTypeBuilder.addProperty(propertyName1, propertyType1, propertyNode1);
    
    String propertyName2 = "name2";
    JSType propertyType2 = new JSType(JSTypeNative.NUMBER_TYPE);
    Node propertyNode2 = new Node();
    recordTypeBuilder.addProperty(propertyName2, propertyType2, propertyNode2);
    
    JSType recordWithProperties = recordTypeBuilder.build();
    assertNotNull(recordWithProperties);
  }
  
  @Test
  public void testAddDuplicateProperty() {
    String propertyName = "name";
    JSType propertyType = new JSType(JSTypeNative.STRING_TYPE);
    Node propertyNode = new Node();
    
    // Test adding a duplicate property
    recordTypeBuilder.addProperty(propertyName, propertyType, propertyNode);
    assertNull(recordTypeBuilder.addProperty(propertyName, propertyType, propertyNode));
  }
  
  @Test
  public void testGetPropertyNode() {
    String propertyName = "name";
    JSType propertyType = new JSType(JSTypeNative.STRING_TYPE);
    Node propertyNode = new Node();
    
    // Test getting the property node
    recordTypeBuilder.addProperty(propertyName, propertyType, propertyNode);
    assertEquals(propertyNode, recordTypeBuilder.getPropertyNode(propertyName));
  }
  
  @Test
  public void testGetPropertyNodeNonExistentProperty() {
    // Test getting the property node for a non-existent property
    assertNull(recordTypeBuilder.getPropertyNode("nonExistentProperty"));
  }
}