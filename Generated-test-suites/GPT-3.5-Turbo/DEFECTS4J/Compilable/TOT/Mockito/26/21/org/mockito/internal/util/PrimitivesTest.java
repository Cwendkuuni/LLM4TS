package org.mockito.internal.util;
import org.junit.Test;
import static org.junit.Assert.*;

public class PrimitivesTest {
  
  @Test
  public void testPrimitiveTypeOf() {
    // Test typical use case
    assertEquals(Boolean.TYPE, Primitives.primitiveTypeOf(boolean.class));
    
    // Test edge case
    assertEquals(Character.TYPE, Primitives.primitiveTypeOf(char.class));
    
    // Test error scenario
    assertNull(Primitives.primitiveTypeOf(Object.class));
  }
  
  @Test
  public void testIsPrimitiveWrapper() {
    // Test typical use case
    assertTrue(Primitives.isPrimitiveWrapper(Integer.class));
    
    // Test edge case
    assertFalse(Primitives.isPrimitiveWrapper(Object.class));
    
    // Test error scenario
    assertFalse(Primitives.isPrimitiveWrapper(null));
  }
  
  @Test
  public void testPrimitiveWrapperOf() {
    // Test typical use case
    assertEquals(Boolean.FALSE, Primitives.primitiveWrapperOf(boolean.class));

    // Test edge case
    assertEquals(Character.valueOf('\u0000'), Primitives.primitiveWrapperOf(char.class));
    
    // Test error scenario
    assertNull(Primitives.primitiveWrapperOf(Object.class));
  }
  
  @Test
  public void testPrimitiveValueOrNullFor() {
    // Test typical use case
    assertEquals(false, (boolean) Primitives.primitiveValueOrNullFor(Boolean.TYPE));
    
    // Test edge case
    assertEquals('\u0000', (char) Primitives.primitiveValueOrNullFor(Character.TYPE));
    
    // Test error scenario
    assertNull(Primitives.primitiveValueOrNullFor(Object.class));
  }
  
}