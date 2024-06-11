package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class ArrowTypeTest {

  private JSTypeRegistry registry;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
  }
  
  @Test
  public void testIsSubtype_differentReturnType() {
    // ARRANGE
    JSType returnType1 = registry.createTypeBuilder().setPrimitiveType(JSTypePrimitive.BOOLEAN).build();
    ArrowType arrowType1 = new ArrowType(registry, null, returnType1);
    
    JSType returnType2 = registry.createTypeBuilder().setPrimitiveType(JSTypePrimitive.NUMBER).build();
    ArrowType arrowType2 = new ArrowType(registry, null, returnType2);
    
    // ACT
    boolean result = arrowType1.isSubtype(arrowType2);
    
    // ASSERT
    assertEquals(false, result);
  }
  
  @Test
  public void testIsSubtype_sameParameterTypeAndReturnType() {
    // ARRANGE
    JSType returnType = registry.createTypeBuilder().setPrimitiveType(JSTypePrimitive.BOOLEAN).build();
    ArrowType arrowType1 = new ArrowType(registry, null, returnType);
    ArrowType arrowType2 = new ArrowType(registry, null, returnType);
    
    // ACT
    boolean result = arrowType1.isSubtype(arrowType2);
    
    // ASSERT
    assertEquals(true, result);
  }
  
  @Test
  public void testIsEquivalentTo_equalTypes() {
    // ARRANGE
    JSType returnType = registry.createTypeBuilder().setPrimitiveType(JSTypePrimitive.BOOLEAN).build();
    ArrowType arrowType1 = new ArrowType(registry, null, returnType);
    ArrowType arrowType2 = new ArrowType(registry, null, returnType);
    
    // ACT
    boolean result = arrowType1.isEquivalentTo(arrowType2);
    
    // ASSERT
    assertEquals(true, result);
  }
  
  @Test
  public void testHashCode_equalTypes() {
    // ARRANGE
    JSType returnType = registry.createTypeBuilder().setPrimitiveType(JSTypePrimitive.BOOLEAN).build();
    ArrowType arrowType1 = new ArrowType(registry, null, returnType);
    ArrowType arrowType2 = new ArrowType(registry, null, returnType);
    
    // ACT
    int hashCode1 = arrowType1.hashCode();
    int hashCode2 = arrowType2.hashCode();
    
    // ASSERT
    assertEquals(hashCode1, hashCode2);
  }
  
  @Test
  public void testHasUnknownParamsOrReturn_returnTypeIsNull() {
    // ARRANGE
    ArrowType arrowType = new ArrowType(registry, null, null);
    
    // ACT
    boolean result = arrowType.hasUnknownParamsOrReturn();
    
    // ASSERT
    assertEquals(true, result);
  }
  
  @Test
  public void testHasUnknownParamsOrReturn_paramTypeIsNull() {
    // ARRANGE
    Node parameters = new Node(Token.PARAM_LIST);
    parameters.addChildToFront(new Node(Token.NAME).setJSType(null));
    ArrowType arrowType = new ArrowType(registry, parameters, null);
    
    // ACT
    boolean result = arrowType.hasUnknownParamsOrReturn();
    
    // ASSERT
    assertEquals(true, result);
  }
  
  @Test
  public void testToStringHelper_forAnnotations() {
    // ARRANGE
    JSType returnType = registry.createTypeBuilder().setPrimitiveType(JSTypePrimitive.BOOLEAN).build();
    ArrowType arrowType = new ArrowType(registry, null, returnType);
    
    // ACT
    String result = arrowType.toStringHelper(true);
    
    // ASSERT
    assertEquals("JSType", result);
  }
}