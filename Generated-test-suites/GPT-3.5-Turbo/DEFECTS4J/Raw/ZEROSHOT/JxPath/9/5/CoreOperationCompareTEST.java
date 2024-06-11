import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationCompareTest {

  @Test
  public void testEqual() {
    CoreOperationCompare op = new CoreOperationCompare(expression1, expression2);
    EvalContext context = createContext();
    boolean result = op.equal(context, expression1, expression2);
    assertEquals(expectedResult, result);
  }

  @Test
  public void testContains() {
    CoreOperationCompare op = new CoreOperationCompare(expression1, expression2);
    Iterator it = createIterator();
    Object value = createValue();
    boolean result = op.contains(it, value);
    assertEquals(expectedResult, result);
  }

  @Test
  public void testFindMatch() {
    CoreOperationCompare op = new CoreOperationCompare(expression1, expression2);
    Iterator lit = createIterator();
    Iterator rit = createIterator();
    boolean result = op.findMatch(lit, rit);
    assertEquals(expectedResult, result);
  }

  @Test
  public void testEqualObjectObject() {
    CoreOperationCompare op = new CoreOperationCompare(expression1, expression2);
    Object l = createObject();
    Object r = createObject();
    boolean result = op.equal(l, r);
    assertEquals(expectedResult, result);
  }
}