
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
    String name = "property1";
    JSType type = new JSType();
    Node propertyNode = new Node();
    
    RecordTypeBuilder result = recordTypeBuilder.addProperty(name, type, propertyNode);
    
    assertTrue(result != null);
    assertTrue(recordTypeBuilder.build().getProperties().containsKey(name));
  }
  
  @Test
  public void testBuild_emptyRecord() {
    JSType result = recordTypeBuilder.build();
    
    assertTrue(result != null);
    assertTrue(result instanceof RecordType);
    assertEquals(JSTypeNative.OBJECT_TYPE, result.getDisplayName());
  }
  
  @Test
  public void testBuild_nonEmptyRecord() {
    String name = "property1";
    JSType type = new JSType();
    Node propertyNode = new Node();
    
    recordTypeBuilder.addProperty(name, type, propertyNode);
    
    JSType result = recordTypeBuilder.build();
    
    assertTrue(result != null);
    assertTrue(result instanceof RecordType);
    assertTrue(((RecordType) result).getProperties().containsKey(name));
  }
}
