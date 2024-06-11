import static org.junit.Assert.*;
import org.junit.Test;

public class RecordTypeBuilderTest {
  @Test
  public void testAddProperty() {
    // Test adding a property to an empty record type
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    builder.addProperty("name", registry.getNativeType(JSTypeNative.STRING_TYPE), null);
    JSType result = builder.build();
    assertTrue(result instanceof RecordType);
    
    // Test adding a duplicate property
    builder.addProperty("name", registry.getNativeType(JSTypeNative.STRING_TYPE), null);
    result = builder.build();
    assertNull(result);
    
    // Test adding a property to a non-empty record type
    builder = new RecordTypeBuilder(registry);
    builder.addProperty("name", registry.getNativeType(JSTypeNative.STRING_TYPE), null);
    builder.addProperty("age", registry.getNativeType(JSTypeNative.NUMBER_TYPE), null);
    result = builder.build();
    assertTrue(result instanceof RecordType);
  }
  
  @Test
  public void testBuild() {
    // Test building an empty record type
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    JSType result = builder.build();
    assertTrue(result instanceof ObjectType);
    
    // Test building a non-empty record type
    builder.addProperty("name", registry.getNativeType(JSTypeNative.STRING_TYPE), null);
    builder.addProperty("age", registry.getNativeType(JSTypeNative.NUMBER_TYPE), null);
    result = builder.build();
    assertTrue(result instanceof RecordType);
  }
}