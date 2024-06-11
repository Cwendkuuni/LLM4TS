import static org.junit.Assert.*;
import org.junit.Test;

public class FunctionBuilderTest {
  
  @Test
  public void testWithName() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder builder = new FunctionBuilder(registry);
    builder.withName("testFunction");
    FunctionType function = builder.build();
    
    assertEquals("testFunction", function.getReferenceName());
  }
  
  @Test
  public void testWithSourceNode() {
    // Test setting the source node of the function type
  }
  
  @Test
  public void testWithParams() {
    // Test setting the parameters of the function type
  }
  
  @Test
  public void testWithReturnType() {
    // Test setting the return type of the function type
  }
  
  @Test
  public void testWithTypeOfThis() {
    // Test setting the "this" type of the function type
  }
  
  @Test
  public void testWithTemplateName() {
    // Test setting the template name of the function type
  }
  
  @Test
  public void testForConstructor() {
    // Test making the function type a constructor
  }
  
  @Test
  public void testForNativeType() {
    // Test making the function type a native type
  }
  
  @Test
  public void testCopyFromOtherFunction() {
    // Test copying information from another function type
  }
}