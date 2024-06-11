import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class RecordTypeBuilderTest {
  
  private RecordTypeBuilder recordTypeBuilder;
  
  @Before
  public void setUp() {
    recordTypeBuilder = new RecordTypeBuilder(new JSTypeRegistry());
  }
  
  @Test
  public void testAddProperty_uniqueName_success() {
    String propertyName = "property1";
    JSType propertyType = new JSType();
    Node propertyNode = new Node();
    
    RecordTypeBuilder result = recordTypeBuilder.addProperty(propertyName, propertyType, propertyNode);
    
    assertNotNull(result);
    assertTrue(recordTypeBuilder.properties.containsKey(propertyName));
    assertEquals(propertyType, recordTypeBuilder.properties.get(propertyName).getType());
    assertEquals(propertyNode, recordTypeBuilder.properties.get(propertyName).getPropertyNode());
  }
  
  @Test
  public void testAddProperty_duplicateName_failure() {
    String propertyName = "property1";
    JSType propertyType = new JSType();
    Node propertyNode = new Node();
    
    recordTypeBuilder.addProperty(propertyName, propertyType, propertyNode);
    RecordTypeBuilder result = recordTypeBuilder.addProperty(propertyName, propertyType, propertyNode);
    
    assertNull(result);
    assertTrue(recordTypeBuilder.properties.containsKey(propertyName));
    assertEquals(propertyType, recordTypeBuilder.properties.get(propertyName).getType());
    assertEquals(propertyNode, recordTypeBuilder.properties.get(propertyName).getPropertyNode());
  }
  
  @Test
  public void testBuild_emptyBuilder_returnObjectType() {
    JSType expected = new JSType();
    
    JSType result = recordTypeBuilder.build();
    
    assertEquals(expected, result);
  }
  
  @Test
  public void testBuild_nonEmptyBuilder_returnRecordType() {
    String propertyName = "property1";
    JSType propertyType = new JSType();
    Node propertyNode = new Node();
    
    recordTypeBuilder.addProperty(propertyName, propertyType, propertyNode);
    JSType result = recordTypeBuilder.build();
    
    assertTrue(result instanceof RecordType);
    assertEquals(recordTypeBuilder.registry, result.getRegistry());
    assertEquals(recordTypeBuilder.properties, ((RecordType) result).getProperties());
  }
  
  @Test
  public void testRecordProperty_getType_returnCorrectType() {
    JSType expected = new JSType();
    Node propertyNode = new Node();
    
    RecordTypeBuilder.RecordProperty recordProperty = recordTypeBuilder.new RecordProperty(expected, propertyNode);
    
    assertEquals(expected, recordProperty.getType());
  }
  
  @Test
  public void testRecordProperty_getPropertyNode_returnCorrectNode() {
    JSType propertyType = new JSType();
    Node expected = new Node();
    
    RecordTypeBuilder.RecordProperty recordProperty = recordTypeBuilder.new RecordProperty(propertyType, expected);
    
    assertEquals(expected, recordProperty.getPropertyNode());
  }
}