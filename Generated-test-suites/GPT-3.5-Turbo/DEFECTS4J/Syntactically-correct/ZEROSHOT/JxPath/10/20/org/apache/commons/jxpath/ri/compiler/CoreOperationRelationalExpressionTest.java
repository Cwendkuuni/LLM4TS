import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {
  
  private CoreOperationRelationalExpression expression;
  
  @Before
  public void setUp() {
    expression = new CoreOperationRelationalExpression(new Expression[]{new MockExpression(), new MockExpression()});
  }
  
  @Test
  public void testComputeValue() {
    EvalContext context = new EvalContext();
    context.setNodeSet(new MockNodeSet());
    Object result = expression.computeValue(context);
    assertNotNull(result);
    assertTrue(result instanceof Boolean);
  }
  
  @Test
  public void testGetPrecedence() {
    int precedence = expression.getPrecedence();
    assertTrue(precedence == 3);
  }
  
  @Test
  public void testIsSymmetric() {
    boolean symmetric = expression.isSymmetric();
    assertFalse(symmetric);
  }
  
  @Test
  public void testEvaluateCompare() {
    boolean compare = expression.evaluateCompare(3);
    assertFalse(compare);
  }
  
  @Test
  public void testCompute() {
    Object left = 3;
    Object right = 4;
    boolean result = expression.compute(left, right);
    assertFalse(result);
  }
  
  @Test
  public void testReduce() {
    Object o = new MockExpression();
    Object result = expression.reduce(o);
    assertNotNull(result);
  }
  
  @Test
  public void testContainsMatch() {
    Iterator it = new MockIterator();
    Object value = new MockExpression();
    boolean match = expression.containsMatch(it, value);
    assertFalse(match);
  }
  
  @Test
  public void testFindMatch() {
    Iterator lit = new MockIterator();
    Iterator rit = new MockIterator();
    boolean match = expression.findMatch(lit, rit);
    assertFalse(match);
  }
}