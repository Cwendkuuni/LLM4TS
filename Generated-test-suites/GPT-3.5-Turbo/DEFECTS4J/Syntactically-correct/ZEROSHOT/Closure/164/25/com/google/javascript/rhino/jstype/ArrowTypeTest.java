import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ArrowTypeTest {
  
  private ArrowType arrowType;
  
  @Before
  public void setUp() {
    arrowType = new ArrowType(new JSTypeRegistry(), null, null);
  }
  
  @Test
  public void testIsSubtype() {
    JSType jsType = new JSType(new JSTypeRegistry());
    assertFalse(arrowType.isSubtype(jsType));
  }
  
  @Test
  public void testHasEqualParameters() {
    ArrowType that = new ArrowType(new JSTypeRegistry(), null, null);
    boolean result = arrowType.hasEqualParameters(that);
    assertTrue(result);
  }
  
  @Test
  public void testHashCode() {
    int hashCode = arrowType.hashCode();
    assertNotNull(hashCode);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testGetLeastSupertype() {
    JSType jsType = new JSType(new JSTypeRegistry());
    arrowType.getLeastSupertype(jsType);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testGetGreatestSubtype() {
    JSType jsType = new JSType(new JSTypeRegistry());
    arrowType.getGreatestSubtype(jsType);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testTestForEquality() {
    JSType jsType = new JSType(new JSTypeRegistry());
    arrowType.testForEquality(jsType);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testVisit() {
    ArrowType.Visitor<String> visitor = new ArrowType.Visitor<String>() {
      public String visit() {
        return "visited";
      }
    };
    arrowType.visit(visitor);
  }
  
  @Test
  public void testGetPossibleToBooleanOutcomes() {
    BooleanLiteralSet booleanLiteralSet = arrowType.getPossibleToBooleanOutcomes();
    assertNotNull(booleanLiteralSet);
  }
  
  @Test
  public void testResolveInternal() {
    ErrorReporter errorReporter = new ErrorReporter();
    StaticScope<JSType> staticScope = new StaticScope<JSType>();
    JSType resolvedType = arrowType.resolveInternal(errorReporter, staticScope);
    assertNotNull(resolvedType);
  }
  
  @Test
  public void testHasUnknownParamsOrReturn() {
    boolean hasUnknownParamsOrReturn = arrowType.hasUnknownParamsOrReturn();
    assertFalse(hasUnknownParamsOrReturn);
  }
  
  @Test
  public void testToStringHelper() {
    String toString = arrowType.toStringHelper(true);
    assertNotNull(toString);
  }
  
}