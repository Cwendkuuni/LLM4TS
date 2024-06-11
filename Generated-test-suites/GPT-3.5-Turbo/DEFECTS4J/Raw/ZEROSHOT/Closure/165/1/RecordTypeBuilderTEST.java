import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.rhino.Node;

public class RecordTypeBuilderTest {
  
  private RecordTypeBuilder recordTypeBuilder;
  
  @Before
  public void setUp() {
    recordTypeBuilder = new RecordTypeBuilder(new JSTypeRegistry());
  }
  
  @Test
  public void testAddProperty() {
    String name = "property1";
    JSType type = new JSType();
    Node propertyNode = new Node();
    
    RecordTypeBuilder result = recordTypeBuilder.addProperty(name, type, propertyNode);
    
    assertNotNull(result);
    assertFalse(recordTypeBuilder.isEmpty);
    assertNotNull(recordTypeBuilder.properties.get(name));
    assertEquals(type, recordTypeBuilder.properties.get(name).getType());
    assertEquals(propertyNode, recordTypeBuilder.properties.get(name).getPropertyNode());
  }
  
  @Test
  public void testAddDuplicateProperty() {
    String name = "property1";
    JSType type = new JSType();
    Node propertyNode = new Node();
    
    recordTypeBuilder.addProperty(name, type, propertyNode);
    RecordTypeBuilder result = recordTypeBuilder.addProperty(name, type, propertyNode);
    
    assertNull(result);
    assertNotNull(recordTypeBuilder.properties.get(name));
  }
  
  @Test
  public void testBuildWithEmptyRecord() {
    JSType result = recordTypeBuilder.build();
    
    assertNotNull(result);
    assertEquals(JSTypeNative.OBJECT_TYPE, result.getNativeObjectType());
  }
  
  @Test
  public void testBuildWithNonEmptyRecord() {
    String name = "property1";
    JSType type = new JSType();
    Node propertyNode = new Node();
    
    recordTypeBuilder.addProperty(name, type, propertyNode);
    JSType result = recordTypeBuilder.build();
    
    assertNotNull(result);
    assertTrue(result instanceof RecordType);
    assertEquals(recordTypeBuilder.properties, ((RecordType) result).getProperties());
  }
  
  @Test
  public void testRecordProperty() {
    JSType type = new JSType();
    Node propertyNode = new Node();
    
    RecordTypeBuilder.RecordProperty recordProperty = new RecordTypeBuilder.RecordProperty(type, propertyNode);
    
    assertEquals(type, recordProperty.getType());
    assertEquals(propertyNode, recordProperty.getPropertyNode());
  }
}