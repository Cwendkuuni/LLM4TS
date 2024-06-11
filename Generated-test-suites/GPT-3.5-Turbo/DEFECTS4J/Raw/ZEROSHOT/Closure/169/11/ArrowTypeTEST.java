import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ArrowTypeTest {

  private ArrowType arrowType;

  @Before
  public void setUp() {
    arrowType = new ArrowType(null, null, null);
  }

  @Test
  public void testIsSubtype() {
    JSType otherType = new ArrowType(null, null, null);
    boolean result = arrowType.isSubtype(otherType);
    assertEquals(true, result);
  }

  @Test
  public void testHasEqualParameters() {
    ArrowType that = new ArrowType(null, null, null);
    boolean tolerateUnknowns = false;
    boolean result = arrowType.hasEqualParameters(that, tolerateUnknowns);
    assertEquals(true, result);
  }

  @Test
  public void testCheckArrowEquivalenceHelper() {
    ArrowType that = new ArrowType(null, null, null);
    boolean tolerateUnknowns = false;
    boolean result = arrowType.checkArrowEquivalenceHelper(that, tolerateUnknowns);
    assertEquals(true, result);
  }

  @Test
  public void testHashCode() {
    int result = arrowType.hashCode();
    assertEquals(0, result);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetLeastSupertype() {
    JSType that = new ArrowType(null, null, null);
    arrowType.getLeastSupertype(that);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetGreatestSubtype() {
    JSType that = new ArrowType(null, null, null);
    arrowType.getGreatestSubtype(that);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testTestForEquality() {
    JSType that = new ArrowType(null, null, null);
    arrowType.testForEquality(that);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testVisit() {
    Visitor<T> visitor = null;
    arrowType.visit(visitor);
  }

  @Test
  public void testGetPossibleToBooleanOutcomes() {
    BooleanLiteralSet result = arrowType.getPossibleToBooleanOutcomes();
    assertEquals(BooleanLiteralSet.TRUE, result);
  }

  @Test
  public void testResolveInternal() {
    ErrorReporter t = null;
    StaticScope<JSType> scope = null;
    JSType result = arrowType.resolveInternal(t, scope);
    assertEquals(arrowType, result);
  }

  @Test
  public void testHasUnknownParamsOrReturn() {
    boolean result = arrowType.hasUnknownParamsOrReturn();
    assertEquals(true, result);
  }

  @Test
  public void testToStringHelper() {
    boolean forAnnotations = false;
    String result = arrowType.toStringHelper(forAnnotations);
    assertEquals("[ArrowType]", result);
  }

  @Test
  public void testHasAnyTemplateInternal() {
    boolean result = arrowType.hasAnyTemplateInternal();
    assertEquals(false, result);
  }
}