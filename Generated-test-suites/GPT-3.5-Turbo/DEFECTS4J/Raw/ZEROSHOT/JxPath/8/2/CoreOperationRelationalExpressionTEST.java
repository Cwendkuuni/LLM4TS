import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

public class CoreOperationRelationalExpressionTest {

  private CoreOperationRelationalExpression instance;
  private EvalContext context;

  @Before
  public void setUp() {
    Expression[] args = new Expression[2];
    args[0] = new TestExpression();
    args[1] = new TestExpression();
    instance = new TestCoreOperationRelationalExpression(args);
    context = new EvalContext();
  }

  @Test
  public void testComputeValue() {
    Object result = instance.computeValue(context);
    assertNotNull(result);
    assertTrue(result instanceof Boolean);
  }

  @Test
  public void testGetPrecedence() {
    int result = instance.getPrecedence();
    assertEquals(3, result);
  }

  @Test
  public void testIsSymmetric() {
    boolean result = instance.isSymmetric();
    assertFalse(result);
  }

  @Test
  public void testEvaluateCompare() {
    boolean result = instance.evaluateCompare(0);
    assertTrue(result);
  }

  @Test
  public void testCompute() {
    Object left = new Object();
    Object right = new Object();
    boolean result = instance.compute(left, right);
    assertFalse(result);
  }

  @Test
  public void testReduceWithSelfContext() {
    Object o = new SelfContext(context);
    Object result = instance.reduce(o);
    assertNotNull(result);
    assertTrue(result instanceof InitialContext);
  }

  @Test
  public void testReduceWithCollection() {
    Object o = new ArrayList<Object>();
    Object result = instance.reduce(o);
    assertNotNull(result);
    assertTrue(result instanceof Iterator);
  }

  @Test
  public void testReduceWithOtherObject() {
    Object o = new Object();
    Object result = instance.reduce(o);
    assertNotNull(result);
    assertEquals(o, result);
  }

  @Test
  public void testContainsMatch() {
    Iterator it = new ArrayList<Object>().iterator();
    Object value = new Object();
    boolean result = instance.containsMatch(it, value);
    assertFalse(result);
  }

  @Test
  public void testFindMatch() {
    Iterator lit = new ArrayList<Object>().iterator();
    Iterator rit = new ArrayList<Object>().iterator();
    boolean result = instance.findMatch(lit, rit);
    assertFalse(result);
  }

  class TestExpression extends Expression {
    public Object computeValue(EvalContext context) {
      return null;
    }
  }

  class TestCoreOperationRelationalExpression extends CoreOperationRelationalExpression {
    public TestCoreOperationRelationalExpression(Expression[] args) {
      super(args);
    }
    protected boolean evaluateCompare(int compare) {
      return true;
    }
  }
}