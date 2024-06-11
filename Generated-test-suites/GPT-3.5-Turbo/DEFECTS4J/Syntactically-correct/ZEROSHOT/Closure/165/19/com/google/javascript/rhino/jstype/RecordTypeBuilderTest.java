import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeBuilderTest {
  
  private RecordTypeBuilder recordTypeBuilder;
  
  @Before
  public void setUp() {
    recordTypeBuilder = new RecordTypeBuilder();
  }
  
  @Test
  public void testAddProperty() {
    String name = "test";
    JSType type = new JSType("String");
    Node propertyNode = new Node();
    
    RecordTypeBuilder result = recordTypeBuilder.addProperty(name, type, propertyNode);
    
    assertNotNull(result);
    assertEquals(1, recordTypeBuilder.getProperties().size());
    assertEquals(type, recordTypeBuilder.getProperties().get(name).getType());
    assertEquals(propertyNode, recordTypeBuilder.getProperties().get(name).getPropertyNode());
  }
  
  @Test
  public void testBuild_emptyRecord() {
    JSType result = recordTypeBuilder.build();
    
    assertNotNull(result);
    assertEquals(JSTypeNative.OBJECT_TYPE, result.getNativeObjectType());
  }
  
  @Test
  public void testBuild_nonEmptyRecord() {
    String name = "test";
    JSType type = new JSType("String");
    Node propertyNode = new Node();
    
    recordTypeBuilder.addProperty(name, type, propertyNode);
    
    JSType result = recordTypeBuilder.build();
    
    assertNotNull(result);
    assertEquals(RecordType.class, result.getClass());
    assertEquals(1, ((RecordType)result).getProperties().size());
    assertEquals(type, ((RecordType)result).getProperties().get(name).getType());
    assertEquals(propertyNode, ((RecordType)result).getProperties().get(name).getPropertyNode());
  }
}