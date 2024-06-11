import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        EvalContext context = new EvalContext();
        Expression left = new Expression();
        Expression right = new Expression();
        boolean result = coreOperationCompare.equal(context, left, right);
        assertEquals(true, result);
    }

    @Test
    public void testContains() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        Iterator it = new Iterator();
        Object value = new Object();
        boolean result = coreOperationCompare.contains(it, value);
        assertEquals(false, result);
    }

    @Test
    public void testFindMatch() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        Iterator lit = new Iterator();
        Iterator rit = new Iterator();
        boolean result = coreOperationCompare.findMatch(lit, rit);
        assertEquals(false, result);
    }

    @Test
    public void testEqual() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        Object l = new Object();
        Object r = new Object();
        boolean result = coreOperationCompare.equal(l, r);
        assertEquals(true, result);
    }

}