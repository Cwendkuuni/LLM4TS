import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import org.junit.Assert;
import org.junit.Test;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        EvalContext context = new EvalContext();
        boolean result = coreOperationCompare.equal(context, arg1, arg2);
        Assert.assertEquals(true, result);
        Iterator it = new Iterator() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                return null;
            }
        };
        result = coreOperationCompare.contains(it, null);
        Assert.assertEquals(false, result);
        result = coreOperationCompare.findMatch(it, it);
        Assert.assertEquals(false, result);
        Object l = null;
        Object r = null;
        result = coreOperationCompare.equal(7, 7);
        Assert.assertEquals(true, result);
    }

}