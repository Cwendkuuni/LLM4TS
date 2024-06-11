import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ArrowTypeTest {

  private ArrowType arrowType;

  @Before
  public void setUp() {
    arrowType = new ArrowType();
  }

  @After
  public void tearDown() {
    arrowType = null;
  }

  @Test
  public void testIsSubtype() {
    JSType otherType = new ArrowType();
    boolean result = arrowType.isSubtype(otherType);
    Assert.assertTrue(result);
  }

  @Test
  public void testHasEqualParameters() {
    ArrowType otherType = new ArrowType();
    boolean result = arrowType.hasEqualParameters(otherType, true);
    Assert.assertTrue(result);
  }

  @Test
  public void testCheckArrowEquivalenceHelper() {
    ArrowType otherType = new ArrowType();
    boolean result = arrowType.checkArrowEquivalenceHelper(otherType, true);
    Assert.assertTrue(result);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testHashCode() {
    arrowType.hashCode();
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetLeastSupertype() {
    arrowType.getLeastSupertype(null);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetGreatestSubtype() {
    arrowType.getGreatestSubtype(null);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testTestForEquality() {
    arrowType.testForEquality(null);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testVisit() {
    arrowType.visit(null);
  }

  @Test
  public void testGetPossibleToBooleanOutcomes() {
    BooleanLiteralSet result = arrowType.getPossibleToBooleanOutcomes();
    Assert.assertEquals(BooleanLiteralSet.TRUE, result);
  }

  @Test
  public void testResolveInternal() {
    ErrorReporter errorReporter = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<>();
    JSType result = arrowType.resolveInternal(errorReporter, scope);
    Assert.assertNotNull(result);
  }

  @Test
  public void testHasUnknownParamsOrReturn() {
    boolean result = arrowType.hasUnknownParamsOrReturn();
    Assert.assertFalse(result);
  }

  @Test
  public void testToStringHelper() {
    String result = arrowType.toStringHelper(false);
    Assert.assertNotNull(result);
  }

  @Test
  public void testHasAnyTemplateInternal() {
    boolean result = arrowType.hasAnyTemplateInternal();
    Assert.assertFalse(result);
  }

}