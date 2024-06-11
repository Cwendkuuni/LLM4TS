import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.rhino.Node;

public class RecordTypeBuilderTest {
  private RecordTypeBuilder builder;
  private JSTypeRegistry registry;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    builder = new RecordTypeBuilder(registry);
  }

  @Test
  public void testAddProperty() {
    String name = "testProperty";
    JSType type = registry.getNativeType(JSTypeNative.STRING_TYPE);
    Node propertyNode = new Node();
    
    RecordTypeBuilder result = builder.addProperty(name, type, propertyNode);
    
    assertNotNull(result);
    assertTrue(builder.getProperties().containsKey(name));
    assertEquals(result, builder);
  }

  @Test
  public void testBuildEmptyRecord() {
    JSType expected = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    JSType result = builder.build();
    
    assertEquals(expected, result);
  }

  @Test
  public void testBuildNonEmptyRecord() {
    String name = "testProperty";
    JSType type = registry.getNativeType(JSTypeNative.STRING_TYPE);
    Node propertyNode = new Node();
    
    builder.addProperty(name, type, propertyNode);
    JSType expected = new RecordType(registry, builder.getProperties());
    JSType result = builder.build();
    
    assertEquals(expected, result);
  }
}