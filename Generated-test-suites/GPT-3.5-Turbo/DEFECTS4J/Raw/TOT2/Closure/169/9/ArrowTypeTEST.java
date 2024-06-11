import org.junit.Test;
import static org.junit.Assert.*;

public class ArrowTypeTest {
  
  @Test
  public void testArrowTypeConstructor() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = new Node(Token.EMPTY);
    JSType returnType = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    
    assertNotNull(arrowType);
    assertEquals(parameters, arrowType.parameters);
    assertEquals(returnType, arrowType.returnType);
    assertFalse(arrowType.returnTypeInferred);
  }
  
  @Test
  public void testIsSubtypeReturnsFalseForNonArrowType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType = new ArrowType(registry, null, null);
    JSType nonArrowType = new JSType(registry);
    
    assertFalse(arrowType.isSubtype(nonArrowType));
  }
  
  // Add more test cases for other methods in ArrowType class
  
  @Test
  public void testHashCode() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = new Node(Token.EMPTY);
    JSType returnType = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
    ArrowType arrowType1 = new ArrowType(registry, parameters, returnType);
    ArrowType arrowType2 = new ArrowType(registry, parameters, returnType);
    
    assertEquals(arrowType1.hashCode(), arrowType2.hashCode());
  }
  
  @Test
  public void testGetLeastSupertypeThrowsUnsupportedOperationException() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ArrowType arrowType = new ArrowType(registry, null, null);
    
    try {
      arrowType.getLeastSupertype(null);
      fail("Expected UnsupportedOperationException was not thrown");
    } catch (UnsupportedOperationException e) {
      // Expected, test passed
    }
  }
  
}