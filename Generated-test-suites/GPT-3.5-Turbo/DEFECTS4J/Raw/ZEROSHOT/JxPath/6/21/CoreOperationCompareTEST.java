import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);

        EvalContext context = new EvalContext();
        Expression left = new Expression();
        Expression right = new Expression();

        boolean result = coreOperationCompare.equal(context, left, right);

        // Assert result
        assertTrue(result);
    }

    @Test
    public void testContains() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);

        Iterator it = Collections.singleton("value").iterator();
        Object value = "value";

        boolean result = coreOperationCompare.contains(it, value);

        // Assert result
        assertTrue(result);
    }

    @Test
    public void testFindMatch() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);

        Iterator lit = Collections.singleton("value").iterator();
        Iterator rit = Collections.singleton("value").iterator();

        boolean result = coreOperationCompare.findMatch(lit, rit);

        // Assert result
        assertTrue(result);
    }

    @Test
    public void testEqualValues() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);

        Object l = "value";
        Object r = "value";

        boolean result = coreOperationCompare.equal(l, r);

        // Assert result
        assertTrue(result);
    }
}