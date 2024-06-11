import static org.junit.Assert.*;
import org.junit.Test;

public class RecordTypeBuilderTest {
  
  @Test
  public void testAddProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    RecordTypeBuilder.RecordProperty property = new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), new Node());
    builder.addProperty("age", property.getType(), property.getPropertyNode());
    assertNotNull(builder.build());
  }
  
  @Test
  public void testBuild() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    JSType result = builder.build();
    assertNotNull(result);
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), result);
  }
  
  @Test
  public void testBuildWithProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    RecordTypeBuilder.RecordProperty property1 = new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), new Node());
    RecordTypeBuilder.RecordProperty property2 = new RecordTypeBuilder.RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), new Node());
    builder.addProperty("age", property1.getType(), property1.getPropertyNode());
    builder.addProperty("name", property2.getType(), property2.getPropertyNode());
    JSType result = builder.build();
    assertNotNull(result);
    assertTrue(result instanceof RecordType);
    assertEquals(2, ((RecordType) result).getProperties().size());
  }
}