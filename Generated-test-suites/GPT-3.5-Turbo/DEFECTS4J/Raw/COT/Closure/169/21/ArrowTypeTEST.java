
import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;

public class ArrowTypeTest {

  @Test
  public void testIsSubtype() {
    ArrowType arrowType1 = new ArrowType(new JSTypeRegistry(), new Node(), new JSType());
    ArrowType arrowType2 = new ArrowType(new JSTypeRegistry(), new Node(), new JSType());
    
    assertTrue(arrowType1.isSubtype(arrowType2));
  }
  
  @Test
  public void testHasEqualParameters() {
    ArrowType arrowType1 = new ArrowType(new JSTypeRegistry(), new Node(), new JSType());
    ArrowType arrowType2 = new ArrowType(new JSTypeRegistry(), new Node(), new JSType());
    
    assertTrue(arrowType1.hasEqualParameters(arrowType2, false));
  }
  
  @Test
  public void testCheckArrowEquivalenceHelper() {
    ArrowType arrowType1 = new ArrowType(new JSTypeRegistry(), new Node(), new JSType());
    ArrowType arrowType2 = new ArrowType(new JSTypeRegistry(), new Node(), new JSType());
    
    assertTrue(arrowType1.checkArrowEquivalenceHelper(arrowType2, false));
  }
  
  @Test
  public void testHashCode() {
    ArrowType arrowType1 = new ArrowType(new JSTypeRegistry(), new Node(), new JSType());
    ArrowType arrowType2 = new ArrowType(new JSTypeRegistry(), new Node(), new JSType());
    
    assertEquals(arrowType1.hashCode(), arrowType2.hashCode());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetLeastSupertype() {
    ArrowType arrowType1 = new ArrowType(new JSTypeRegistry(), new Node(), new JSType());
    ArrowType arrowType2 = new ArrowType(new JSTypeRegistry(), new Node(), new JSType());
    
    arrowType1.getLeastSupertype(arrowType2);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetGreatestSubtype() {
    ArrowType arrowType1 = new ArrowType(new JSTypeRegistry(), new Node(), new JSType());
    ArrowType arrowType2 = new ArrowType(new JSTypeRegistry(), new Node(), new JSType());
    
    arrowType1.getGreatestSubtype(arrowType2);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testTestForEquality() {
    ArrowType arrowType1 = new ArrowType(new JSTypeRegistry(), new Node(), new JSType());
    ArrowType arrowType2 = new ArrowType(new JSTypeRegistry(), new Node(), new JSType());
    
    arrowType1.testForEquality(arrowType2);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testVisit() {
    ArrowType arrowType1 = new ArrowType(new JSTypeRegistry(), new Node(), new JSType());
    ArrowType arrowType2 = new ArrowType(new JSTypeRegistry(), new Node(), new JSType());
    
    arrowType1.visit(null);
  }

  @Test
  public void testGetPossibleToBooleanOutcomes() {
    ArrowType arrowType1 = new ArrowType(new JSTypeRegistry(), new Node(), new JSType());

    
    assertEquals(BooleanLiteralSet.TRUE, arrowType1.getPossibleToBooleanOutcomes());
  }

  @Test
  public void testResolveInternal() {
    ErrorReporter errorReporter = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<>();
    
    ArrowType arrowType1 = new ArrowType(new JSTypeRegistry(), new Node(), new JSType());
    
    assertNotNull(arrowType1.resolveInternal(errorReporter, scope));
  }

  @Test
  public void testHasUnknownParamsOrReturn() {
    ArrowType arrowType1 = new ArrowType(new JSTypeRegistry(), new Node(), new JSType());
    
    assertFalse(arrowType1.hasUnknownParamsOrReturn());
  }

  @Test
  public void testToStringHelper() {
    ArrowType arrowType1 = new ArrowType(new JSTypeRegistry(), new Node(), new JSType());
    
    assertNotNull(arrowType1.toStringHelper(true));
  }

  @Test
  public void testHasAnyTemplateInternal() {
    ArrowType arrowType1 = new ArrowType(new JSTypeRegistry(), new Node(), new JSType());
    
    assertFalse(arrowType1.hasAnyTemplateInternal());
  }
}
