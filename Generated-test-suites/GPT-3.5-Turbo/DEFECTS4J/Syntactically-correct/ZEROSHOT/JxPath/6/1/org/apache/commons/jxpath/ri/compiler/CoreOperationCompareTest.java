import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationCompareTest {

  @Test
  public void testEqual() {
    CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(), new Expression());
    EvalContext context = new EvalContext();
    Expression left = new Expression();
    Expression right = new Expression();
    boolean result = coreOperationCompare.equal(context, left, right);
    assertFalse(result);
  }

  @Test
  public void testContains() {
    CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(), new Expression());
    Iterator it = new Iterator();
    Object value = new Object();
    boolean result = coreOperationCompare.contains(it, value);
    assertFalse(result);
  }

  @Test
  public void testFindMatch() {
    CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(), new Expression());
    Iterator lit = new Iterator();
    Iterator rit = new Iterator();
    boolean result = coreOperationCompare.findMatch(lit, rit);
    assertFalse(result);
  }

  @Test
  public void testEqualObjectObject() {
    CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(), new Expression());
    Object l = new Object();
    Object r = new Object();
    boolean result = coreOperationCompare.equal(l, r);
    assertFalse(result);
  }

}