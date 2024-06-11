import org.junit.Test;
import static org.junit.Assert.*;

public class PrimitivesTest {
  
  @Test
  public void testPrimitiveTypeOf() {
    // Test when input class is a primitive type
    assertEquals(boolean.class, Primitives.primitiveTypeOf(boolean.class));
    
    // Test when input class is a wrapper class
    assertEquals(boolean.class, Primitives.primitiveTypeOf(Boolean.class));
    
    // Test when input class is unknown
    assertNull(Primitives.primitiveTypeOf(String.class));
  }
  
  @Test
  public void testIsPrimitiveWrapper() {
    // Test when input class is a primitive wrapper class
    assertTrue(Primitives.isPrimitiveWrapper(Boolean.class));
    
    // Test when input class is a primitive type
    assertFalse(Primitives.isPrimitiveWrapper(boolean.class));
    
    // Test when input class is unknown
    assertFalse(Primitives.isPrimitiveWrapper(String.class));
  }
  
  @Test
  public void testPrimitiveWrapperOf() {
    // Test when input class is a primitive wrapper class
    assertEquals(Boolean.FALSE, Primitives.primitiveWrapperOf(Boolean.class));
    
    // Test when input class is a primitive type
    assertNull(Primitives.primitiveWrapperOf(boolean.class));
    
    // Test when input class is unknown
    assertNull(Primitives.primitiveWrapperOf(String.class));
  }
  
  @Test
  public void testPrimitiveValueOrNullFor() {
    // Test when input class is a primitive type
    assertEquals(false, Primitives.primitiveValueOrNullFor(boolean.class));
    
    // Test when input class is a wrapper class
    assertEquals(false, Primitives.primitiveValueOrNullFor(Boolean.class));
    
    // Test when input class is unknown
    assertNull(Primitives.primitiveValueOrNullFor(String.class));
  }
  
}