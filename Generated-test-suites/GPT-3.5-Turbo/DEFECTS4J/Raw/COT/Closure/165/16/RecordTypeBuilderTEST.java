
import org.junit.Test;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.Node;

public class RecordTypeBuilderTest {

  @Test
  public void testAddProperty_SingleProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    JSType type = registry.getNativeType(JSTypeNative.STRING_TYPE);
    Node propertyNode = new Node("propertyNode");
    RecordTypeBuilder result = builder.addProperty("name", type, propertyNode);
    assertNotNull(result);
  }
  
  @Test
  public void testAddProperty_DuplicateProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    JSType type = registry.getNativeType(JSTypeNative.STRING_TYPE);
    Node propertyNode = new Node("propertyNode");
    builder.addProperty("name", type, propertyNode);
    RecordTypeBuilder result = builder.addProperty("name", type, propertyNode);
    assertNull(result);
  }
  
  @Test
  public void testAddProperty_EmptyRecord() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    JSType result = builder.build();
    assertNotNull(result);
  }
  
  @Test
  public void testAddProperty_NonEmptyRecord() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    JSType type = registry.getNativeType(JSTypeNative.STRING_TYPE);
    Node propertyNode = new Node("propertyNode");
    builder.addProperty("name", type, propertyNode);
    JSType result = builder.build();
    assertNotNull(result);
  }
}
