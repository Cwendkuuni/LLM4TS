import org.junit.Test;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.Node;

public class RecordTypeBuilderTest {
  
  @Test
  public void testAddProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    JSType type = new JSType();
    Node propertyNode = new Node();
    
    // Case: Adding a new property to an empty record type
    RecordTypeBuilder result1 = builder.addProperty("name", type, propertyNode);
    assertNotNull(result1);
    
    // Case: Adding a duplicate property
    RecordTypeBuilder result2 = builder.addProperty("name", type, propertyNode);
    assertNull(result2);
  }
  
  @Test
  public void testBuild() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    JSType type = new JSType();
    Node propertyNode = new Node();
    
    // Case: Building an empty record type
    JSType result1 = builder.build();
    assertEquals(result1, registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
    
    // Case: Building a record type with properties
    builder.addProperty("name", type, propertyNode);
    builder.addProperty("age", type, propertyNode);
    JSType result2 = builder.build();
    assertEquals(result2.getProperties().size(), 2);
  }
}