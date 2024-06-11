import org.junit.Test;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        EvalContext context = new EvalContext();
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);
        boolean result = operation.equal(context, arg1, arg2);
        assert(result);
    }

    @Test
    public void testContains() {
        Iterator it = new Iterator();
        Object value = new Object();
        CoreOperationCompare operation = new CoreOperationCompare();
        boolean result = operation.contains(it, value);
        assert(!result);
    }

    @Test
    public void testFindMatch() {
        Iterator lit = new Iterator();
        Iterator rit = new Iterator();
        CoreOperationCompare operation = new CoreOperationCompare();
        boolean result = operation.findMatch(lit, rit);
        assert(!result);
    }

    @Test
    public void testEqualObjectObject() {
        Object l = new Object();
        Object r = new Object();
        CoreOperationCompare operation = new CoreOperationCompare();
        boolean result = operation.equal(l, r);
        assert(!result);
    }
    
    @Test
    public void testEqualPointerPointer() {
        Pointer l = new Pointer();
        Pointer r = new Pointer();
        CoreOperationCompare operation = new CoreOperationCompare();
        boolean result = operation.equal(l, r);
        assert(!result);
    }

    @Test
    public void testEqualPointerObject() {
        Pointer l = new Pointer();
        Object r = new Object();
        CoreOperationCompare operation = new CoreOperationCompare();
        boolean result = operation.equal(l, r);
        assert(!result);
    }

    @Test
    public void testEqualObjectPointer() {
        Object l = new Object();
        Pointer r = new Pointer();
        CoreOperationCompare operation = new CoreOperationCompare();
        boolean result = operation.equal(l, r);
        assert(!result);
    }

    @Test
    public void testEqualBoolean() {
        Object l = true;
        Object r = false;
        CoreOperationCompare operation = new CoreOperationCompare();
        boolean result = operation.equal(l, r);
        assert(!result);
    }

    @Test
    public void testEqualNumber() {
        Object l = 1;
        Object r = 2;
        CoreOperationCompare operation = new CoreOperationCompare();
        boolean result = operation.equal(l, r);
        assert(!result);
    }

    @Test
    public void testEqualString() {
        Object l = "Hello";
        Object r = "World";
        CoreOperationCompare operation = new CoreOperationCompare();
        boolean result = operation.equal(l, r);
        assert(!result);
    }

    @Test
    public void testEqualNull() {
        Object l = null;
        Object r = null;
        CoreOperationCompare operation = new CoreOperationCompare();
        boolean result = operation.equal(l, r);
        assert(result);
    }

    @Test
    public void testEqualNonNull() {
        Object l = new Object();
        Object r = null;
        CoreOperationCompare operation = new CoreOperationCompare();
        boolean result = operation.equal(l, r);
        assert(!result);
    }

}