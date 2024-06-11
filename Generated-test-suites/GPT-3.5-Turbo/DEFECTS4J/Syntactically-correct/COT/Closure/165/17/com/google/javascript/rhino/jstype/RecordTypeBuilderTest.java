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
    String name = "test";
    JSType type = new JSType();
    Node propertyNode = new Node();
    
    RecordTypeBuilder result = recordTypeBuilder.addProperty(name, type, propertyNode);
    
    assertNotNull(result);
  }
  
  @Test
  public void testBuildEmptyRecord() {
    JSType result = recordTypeBuilder.build();
    
    assertTrue(result instanceof ObjectType);
    assertEquals(JSTypeNative.OBJECT_TYPE, result.getDisplayName());
  }
  
  @Test
  public void testBuildNonEmptyRecord() {
    String name = "test";
    JSType type = new JSType();
    Node propertyNode = new Node();
    recordTypeBuilder.addProperty(name, type, propertyNode);
    
    JSType result = recordTypeBuilder.build();
    
    assertTrue(result instanceof RecordType);
    assertEquals(Collections.singletonMap(name, new RecordProperty(type, propertyNode)), result.getProperties());
  }
}