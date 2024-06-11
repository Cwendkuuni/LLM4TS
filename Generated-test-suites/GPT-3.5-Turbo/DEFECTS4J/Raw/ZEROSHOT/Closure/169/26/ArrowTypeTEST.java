import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.JSType;

public class ArrowTypeTest {

  private ArrowType arrowType;

  @Before
  public void setUp() {
    arrowType = new ArrowType(null, null, null);
  }

  @Test
  public void testIsSubtype() {
    JSType other = new ArrowType(null, null, null);
    boolean result = arrowType.isSubtype(other);
    assertEquals(false, result);
  }

  @Test
  public void testHasEqualParameters() {
    ArrowType that = new ArrowType(null, null, null);
    boolean result = arrowType.hasEqualParameters(that, false);
    assertEquals(true, result);
  }

  @Test
  public void testCheckArrowEquivalenceHelper() {
    ArrowType that = new ArrowType(null, null, null);
    boolean result = arrowType.checkArrowEquivalenceHelper(that, false);
    assertEquals(true, result);
  }

  @Test(expected=UnsupportedOperationException.class)
  public void testGetLeastSupertype() {
    JSType that = new ArrowType(null, null, null);
    arrowType.getLeastSupertype(that);
  }

  @Test(expected=UnsupportedOperationException.class)
  public void testGetGreatestSubtype() {
    JSType that = new ArrowType(null, null, null);
    arrowType.getGreatestSubtype(that);
  }

  @Test(expected=UnsupportedOperationException.class)
  public void testTestForEquality() {
    JSType that = new ArrowType(null, null, null);
    arrowType.testForEquality(that);
  }

  @Test(expected=UnsupportedOperationException.class)
  public void testVisit() {
    arrowType.visit(null);
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
    assertNotNull(result);
  }

  @Test
  public void testHasUnknownParamsOrReturn() {
    boolean result = arrowType.hasUnknownParamsOrReturn();
    assertEquals(false, result);
  }

  @Test
  public void testToStringHelper() {
    String result = arrowType.toStringHelper(false);
    assertNotNull(result);
  }

  @Test
  public void testHasAnyTemplateInternal() {
    boolean result = arrowType.hasAnyTemplateInternal();
    assertEquals(false, result);
  }

}