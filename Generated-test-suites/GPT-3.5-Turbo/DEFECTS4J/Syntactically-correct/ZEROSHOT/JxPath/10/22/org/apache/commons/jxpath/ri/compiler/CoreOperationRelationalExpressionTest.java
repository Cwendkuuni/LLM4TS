import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class CoreOperationRelationalExpressionTest {

  private class TestCoreOperationRelationalExpression extends CoreOperationRelationalExpression {
    public TestCoreOperationRelationalExpression(Expression[] args) {
      super(args);
    }
    protected boolean evaluateCompare(int compare) {
      // TODO: Implement this method
      return false;
    }
  }

  @Test
  public void testComputeValue() {
    Expression[] args = new Expression[] {null, null};
    TestCoreOperationRelationalExpression expr = new TestCoreOperationRelationalExpression(args);
    EvalContext context = null;
    Object result = expr.computeValue(context);
    Assert.assertEquals(Boolean.FALSE, result);
  }

  @Test
  public void testGetPrecedence() {
    Expression[] args = new Expression[] {null, null};
    TestCoreOperationRelationalExpression expr = new TestCoreOperationRelationalExpression(args);
    int result = expr.getPrecedence();
    Assert.assertEquals(3, result);
  }

  @Test
  public void testIsSymmetric() {
    Expression[] args = new Expression[] {null, null};
    TestCoreOperationRelationalExpression expr = new TestCoreOperationRelationalExpression(args);
    boolean result = expr.isSymmetric();
    Assert.assertEquals(false, result);
  }

  @Test
  public void testCompute() {
    Expression[] args = new Expression[] {null, null};
    TestCoreOperationRelationalExpression expr = new TestCoreOperationRelationalExpression(args);
    Object left = null;
    Object right = null;
    boolean result = expr.compute(left, right);
    Assert.assertEquals(false, result);
  }

  @Test
  public void testReduce() {
    Expression[] args = new Expression[] {null, null};
    TestCoreOperationRelationalExpression expr = new TestCoreOperationRelationalExpression(args);
    Object o = null;
    Object result = expr.reduce(o);
    Assert.assertEquals(null, result);
  }

  @Test
  public void testContainsMatch() {
    Expression[] args = new Expression[] {null, null};
    TestCoreOperationRelationalExpression expr = new TestCoreOperationRelationalExpression(args);
    Iterator it = null;
    Object value = null;
    boolean result = expr.containsMatch(it, value);
    Assert.assertEquals(false, result);
  }

  @Test
  public void testFindMatch() {
    Expression[] args = new Expression[] {null, null};
    TestCoreOperationRelationalExpression expr = new TestCoreOperationRelationalExpression(args);
    Iterator lit = null;
    Iterator rit = null;
    boolean result = expr.findMatch(lit, rit);
    Assert.assertEquals(false, result);
  }

}