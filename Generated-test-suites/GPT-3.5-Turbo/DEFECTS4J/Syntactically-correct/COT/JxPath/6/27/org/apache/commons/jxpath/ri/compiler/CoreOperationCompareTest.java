import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;

public class CoreOperationCompareTest {

  @Test
  public void testEqual() {
    Expression left = new Expression();
    Expression right = new Expression();
    EvalContext context = new InitialContext();
    CoreOperationCompare compare = new CoreOperationCompare(left, right);

    boolean result = compare.equal(context, left, right);

    // Assert result
    assertTrue(result);
  }

  @Test
  public void testContains() {
    Iterator it = new Iterator();
    Object value = new Object();
    CoreOperationCompare compare = new CoreOperationCompare();

    boolean result = compare.contains(it, value);

    // Assert result
    assertFalse(result);
  }

  @Test
  public void testFindMatch() {
    Iterator lit = new Iterator();
    Iterator rit = new Iterator();
    CoreOperationCompare compare = new CoreOperationCompare();

    boolean result = compare.findMatch(lit, rit);

    // Assert result
    assertFalse(result);
  }

  @Test
  public void testEqual() {
    Object l = new Object();
    Object r = new Object();
    CoreOperationCompare compare = new CoreOperationCompare();

    boolean result = compare.equal(l, r);

    // Assert result
    assertFalse(result);
  }

  @Test
  public void testEqualWithPointers() {
    Pointer l = new Pointer();
    Pointer r = new Pointer();
    CoreOperationCompare compare = new CoreOperationCompare();

    boolean result = compare.equal(l, r);

    // Assert result
    assertFalse(result);
  }

}