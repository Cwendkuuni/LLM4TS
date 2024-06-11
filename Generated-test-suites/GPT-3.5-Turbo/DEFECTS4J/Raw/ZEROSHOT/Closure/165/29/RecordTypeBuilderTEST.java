import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.Node;
import java.util.Map;

public class RecordTypeBuilderTest {
  
  private JSTypeRegistry registry;
  private RecordTypeBuilder builder;
  
  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    builder = new RecordTypeBuilder(registry);
  }
  
  @Test
  public void testAddProperty() {
    String name = "property";
    JSType type = registry.getNativeType(JSTypeNative.STRING_TYPE);
    Node propertyNode = new Node();
    
    RecordTypeBuilder result = builder.addProperty(name, type, propertyNode);
    
    assertNotNull(result);
    assertTrue(result.getProperties().containsKey(name));
    assertEquals(type, result.getProperties().get(name).getType());
    assertEquals(propertyNode, result.getProperties().get(name).getPropertyNode());
  }
  
  @Test
  public void testBuild_emptyRecord() {
    JSType result = builder.build();
    
    assertNotNull(result);
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), result);
  }
  
  @Test
  public void testBuild_nonEmptyRecord() {
    String name = "property";
    JSType type = registry.getNativeType(JSTypeNative.STRING_TYPE);
    Node propertyNode = new Node();
    
    builder.addProperty(name, type, propertyNode);
    
    JSType result = builder.build();
    
    assertNotNull(result);
    assertTrue(result instanceof RecordType);
    assertEquals(1, ((RecordType)result).getProperties().size());
    assertTrue(((RecordType)result).getProperties().containsKey(name));
    assertEquals(type, ((RecordType)result).getProperties().get(name).getType());
    assertEquals(propertyNode, ((RecordType)result).getProperties().get(name).getPropertyNode());
  }
}