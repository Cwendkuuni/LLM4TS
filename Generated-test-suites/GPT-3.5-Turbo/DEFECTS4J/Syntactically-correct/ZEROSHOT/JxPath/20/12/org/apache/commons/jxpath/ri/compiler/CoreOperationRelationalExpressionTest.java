import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

  @Test
  public void testComputeValue() {
    Object result = null;
    CoreOperationRelationalExpression relExpr = new StubCoreOperationRelationalExpression();
    EvalContext context = new EvalContext();
    result = relExpr.computeValue(context);
    assertNotNull(result);
  }

  @Test
  public void testGetPrecedence() {
    CoreOperationRelationalExpression relExpr = new StubCoreOperationRelationalExpression();
    int precedence = relExpr.getPrecedence();
    assertEquals(6, precedence);
  }

  @Test
  public void testIsSymmetric() {
    CoreOperationRelationalExpression relExpr = new StubCoreOperationRelationalExpression();
    boolean symmetric = relExpr.isSymmetric();
    assertFalse(symmetric);
  }

  @Test
  public void testEvaluateCompare() {
    CoreOperationRelationalExpression relExpr = new StubCoreOperationRelationalExpression();
    boolean result = relExpr.evaluateCompare(0);
    assertFalse(result);
  }

  @Test
  public void testCompute() {
    boolean result = false;
    Object left = null;
    Object right = null;
    CoreOperationRelationalExpression relExpr = new StubCoreOperationRelationalExpression();
    result = relExpr.compute(left, right);
    assertFalse(result);
  }

  @Test
  public void testReduce() {
    CoreOperationRelationalExpression relExpr = new StubCoreOperationRelationalExpression();
    Object result = relExpr.reduce(null);
    assertNull(result);
  }

  @Test
  public void testContainsMatch() {
    boolean result = false;
    Iterator it = null;
    Object value = null;
    CoreOperationRelationalExpression relExpr = new StubCoreOperationRelationalExpression();
    result = relExpr.containsMatch(it, value);
    assertFalse(result);
  }

  @Test
  public void testFindMatch() {
    boolean result = false;
    Iterator lit = null;
    Iterator rit = null;
    CoreOperationRelationalExpression relExpr = new StubCoreOperationRelationalExpression();
    result = relExpr.findMatch(lit, rit);
    assertFalse(result);
  }
}