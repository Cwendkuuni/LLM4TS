import static org.junit.Assert.*;
import org.junit.Test;

public class ArrowTypeTest {
  
  @Test
  public void testIsSubtype() {
    ArrowType arrowType1 = new ArrowType();
    ArrowType arrowType2 = new ArrowType();
    arrowType1.returnType = new JSType();
    arrowType2.returnType = new JSType();
    
    assertTrue(arrowType1.isSubtype(arrowType2));
    assertTrue(arrowType2.isSubtype(arrowType1));
  }
  
  @Test
  public void testIsSubtype_missingReturnType() {
    ArrowType arrowType1 = new ArrowType();
    ArrowType arrowType2 = new ArrowType();
    arrowType1.returnType = null;
    arrowType2.returnType = new JSType();
    
    assertFalse(arrowType1.isSubtype(arrowType2));
    assertTrue(arrowType2.isSubtype(arrowType1));
  }
  
  @Test
  public void testIsSubtype_missingParameterType() {
    ArrowType arrowType1 = new ArrowType();
    ArrowType arrowType2 = new ArrowType();
    arrowType1.parameters = new Node();
    arrowType2.parameters = new Node();
    
    assertTrue(arrowType1.isSubtype(arrowType2));
    assertTrue(arrowType2.isSubtype(arrowType1));
  }
  
  @Test
  public void testIsEquivalentTo() {
    ArrowType arrowType1 = new ArrowType();
    ArrowType arrowType2 = new ArrowType();
    arrowType1.returnType = new JSType();
    arrowType2.returnType = new JSType();
    
    assertTrue(arrowType1.isEquivalentTo(arrowType2));
    assertTrue(arrowType2.isEquivalentTo(arrowType1));
  }
  
  @Test
  public void testIsEquivalentTo_mismatchReturnType() {
    ArrowType arrowType1 = new ArrowType();
    ArrowType arrowType2 = new ArrowType();
    arrowType1.returnType = new JSType();
    arrowType2.returnType = new JSType();
    
    assertFalse(arrowType1.isEquivalentTo(arrowType2));
    assertFalse(arrowType2.isEquivalentTo(arrowType1));
  }
  
  @Test
  public void testIsEquivalentTo_mismatchParameterType() {
    ArrowType arrowType1 = new ArrowType();
    ArrowType arrowType2 = new ArrowType();
    arrowType1.parameters = new Node();
    arrowType2.parameters = new Node();
    
    assertFalse(arrowType1.isEquivalentTo(arrowType2));
    assertFalse(arrowType2.isEquivalentTo(arrowType1));
  }
  
  @Test
  public void testHasEqualParameters() {
    ArrowType arrowType1 = new ArrowType();
    ArrowType arrowType2 = new ArrowType();
    arrowType1.parameters = new Node();
    arrowType2.parameters = new Node();
    
    assertTrue(arrowType1.hasEqualParameters(arrowType2));
    assertTrue(arrowType2.hasEqualParameters(arrowType1));
  }
  
  @Test
  public void testHasEqualParameters_mismatchParameterType() {
    ArrowType arrowType1 = new ArrowType();
    ArrowType arrowType2 = new ArrowType();
    arrowType1.parameters = new Node();
    arrowType2.parameters = new Node();
    
    assertFalse(arrowType1.hasEqualParameters(arrowType2));
    assertFalse(arrowType2.hasEqualParameters(arrowType1));
  }
  
  @Test
  public void testHashCode() {
    ArrowType arrowType = new ArrowType();
    arrowType.returnType = new JSType();
    arrowType.returnTypeInferred = true;
    
    int expectedHashCode = arrowType.returnType.hashCode() + 1;
    assertEquals(expectedHashCode, arrowType.hashCode());
  }
  
  @Test
  public void testGetPossibleToBooleanOutcomes() {
    ArrowType arrowType = new ArrowType();
    
    BooleanLiteralSet expectedOutcome = BooleanLiteralSet.TRUE;
    assertEquals(expectedOutcome, arrowType.getPossibleToBooleanOutcomes());
  }
  
}