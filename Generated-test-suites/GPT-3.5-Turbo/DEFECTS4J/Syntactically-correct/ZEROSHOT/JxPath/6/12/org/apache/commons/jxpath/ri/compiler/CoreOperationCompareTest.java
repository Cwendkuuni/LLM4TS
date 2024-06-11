import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class CoreOperationCompareTest {

    private class MockExpression extends Expression {
        public MockExpression() {
            super(null);
        }
        @Override
        public Object compute(EvalContext arg0) {}
    }

    private class MockCoreOperationCompare extends CoreOperationCompare {
        public MockCoreOperationCompare(Expression arg1, Expression arg2) {
            super(arg1, arg2);
        }
        @Override
        protected boolean equal(EvalContext arg0, Expression arg1, Expression arg2) {}
        @Override
        protected boolean contains(Iterator arg0, Object arg1) {}
        @Override
        protected boolean findMatch(Iterator arg0, Iterator arg1) {}
        @Override
        protected boolean equal(Object arg0, Object arg1) {}
    }

    @Test
    public void testEqual() {
        Expression left = new MockExpression();
        Expression right = new MockExpression();
        CoreOperationCompare coreOperationCompare = new MockCoreOperationCompare(left, right);
        EvalContext context = new EvalContext();
        boolean result = coreOperationCompare.equal(context, left, right);
        Assert.assertEquals(true, result);
    }

    @Test
    public void testContains() {
        CoreOperationCompare coreOperationCompare = new MockCoreOperationCompare(new MockExpression(), new MockExpression());
        Iterator it = new Iterator() {
            public boolean hasNext() {}
            public Object next() {}
        };
        Object value = new Object();
        boolean result = coreOperationCompare.contains(it, value);
        Assert.assertEquals(false, result);
    }

    @Test
    public void testFindMatch() {
        CoreOperationCompare coreOperationCompare = new MockCoreOperationCompare(new MockExpression(), new MockExpression());
        Iterator lit = new Iterator() {
            public boolean hasNext() {}
            public Object next() {}
        };
        Iterator rit = new Iterator() {
            public boolean hasNext() {}
            public Object next() {}
        };
        boolean result = coreOperationCompare.findMatch(lit, rit);
        Assert.assertEquals(false, result);
    }

    @Test
    public void testEqualValues() {
        CoreOperationCompare coreOperationCompare = new MockCoreOperationCompare(new MockExpression(), new MockExpression());
        Object l = new Object();
        Object r = new Object();
        boolean result = coreOperationCompare.equal(l, r);
        Assert.assertEquals(false, result);
    }

}