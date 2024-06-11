import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;

public class RecordTypeBuilderTest {

  @Test
  public void testAddProperty() {
    // Typical use case
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    String name = "property";
    JSType type = new JSType();
    Node propertyNode = new Node();
    RecordTypeBuilder result = builder.addProperty(name, type, propertyNode);
    assertNotNull(result);
    
    // Edge case: Adding duplicate property
    result = builder.addProperty(name, type, propertyNode);
    assertNull(result);
  }

  @Test
  public void testBuild() {
    // Typical use case: Building non-empty record
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    builder.addProperty("prop1", new JSType(), new Node());
    builder.addProperty("prop2", new JSType(), new Node());
    JSType result = builder.build();
    assertNotNull(result);
    
    // Typical use case: Building empty record
    builder = new RecordTypeBuilder(registry);
    result = builder.build();
    assertNotNull(result);
    
    // Error scenario: Building record with duplicate property
    builder = new RecordTypeBuilder(registry);
    builder.addProperty("prop1", new JSType(), new Node());
    builder.addProperty("prop1", new JSType(), new Node());
    result = builder.build();
    assertNotNull(result);
  }

  @Test
  public void testRecordProperty() {
    // Typical use case
    JSType type = new JSType();
    Node propertyNode = new Node();
    RecordTypeBuilder.RecordProperty prop = new RecordTypeBuilder.RecordProperty(type, propertyNode);
    assertEquals(type, prop.getType());
    assertEquals(propertyNode, prop.getPropertyNode());
  }
}