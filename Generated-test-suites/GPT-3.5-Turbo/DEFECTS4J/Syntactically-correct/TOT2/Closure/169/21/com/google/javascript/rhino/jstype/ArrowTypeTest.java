import static org.junit.Assert.*;
import org.junit.Test;

public class ArrowTypeTest {
  
  @Test
  public void testArrowTypeConstructor() {
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    assertNotNull(arrowType);
  }
  
  @Test
  public void testIsSubtype() {
    ArrowType arrowType1 = new ArrowType(registry, parameters, returnType);
    ArrowType arrowType2 = new ArrowType(registry, parameters, returnType);
    
    assertTrue(arrowType1.isSubtype(arrowType2));
  }
  
  @Test
  public void testEqualParameters() {
    ArrowType arrowType1 = new ArrowType(registry, parameters, returnType);
    ArrowType arrowType2 = new ArrowType(registry, parameters, returnType);
    
    assertTrue(arrowType1.hasEqualParameters(arrowType2, false));
  }
  
  @Test
  public void testCheckArrowEquivalenceHelper() {
    ArrowType arrowType1 = new ArrowType(registry, parameters, returnType);
    ArrowType arrowType2 = new ArrowType(registry, parameters, returnType);
    
    assertTrue(arrowType1.checkArrowEquivalenceHelper(arrowType2, false));
  }
  
  // Add more test cases for edge cases and exceptions
  
}