import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationCompareTest {

  @Test
  public void testEqual() {
    Expression arg1 = new Expression();
    Expression arg2 = new Expression();
    CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);

    EvalContext context = new EvalContext();
    boolean result = operation.equal(context, arg1, arg2);

    assertFalse(result);
  }

  @Test
  public void testContains() {
    Expression arg1 = new Expression();
    Expression arg2 = new Expression();
    CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);

    Iterator it = new Iterator();
    Object value = new Object();

    boolean result = operation.contains(it, value);

    assertFalse(result);
  }

  @Test
  public void testFindMatch() {
    Expression arg1 = new Expression();
    Expression arg2 = new Expression();
    CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);

    Iterator lit = new Iterator();
    Iterator rit = new Iterator();

    boolean result = operation.findMatch(lit, rit);

    assertFalse(result);
  }

  @Test
  public void testEqualObjectObject() {
    Expression arg1 = new Expression();
    Expression arg2 = new Expression();
    CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);

    Object l = new Object();
    Object r = new Object();

    boolean result = operation.equal(l, r);

    assertFalse(result);
  }

}