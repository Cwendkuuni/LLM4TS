import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare() {
            @Override
            public Object compute(EvalContext evalContext) {
                return null;
            }
        };
        
        EvalContext context = new EvalContext();
        Expression left = new Expression();
        Expression right = new Expression();
        
        Object l = left.compute(context);
        Object r = right.compute(context);
        
        assertTrue(coreOperationCompare.equal(context, left, right));
    }

    @Test
    public void testContains() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare() {
            @Override
            public Object compute(EvalContext evalContext) {
                return null;
            }
        };
        
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
        Object value = null;
        
        assertFalse(coreOperationCompare.contains(it, value));
    }

    @Test
    public void testFindMatch() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare() {
            @Override
            public Object compute(EvalContext evalContext) {
                return null;
            }
        };
        
        Iterator lit = new Iterator() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                return null;
            }
        };
        Iterator rit = new Iterator() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                return null;
            }
        };
        
        assertFalse(coreOperationCompare.findMatch(lit, rit));
    }

    @Test
    public void testEqualObjects() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare() {
            @Override
            public Object compute(EvalContext evalContext) {
                return null;
            }
        };
        
        Object l = null;
        Object r = null;
        
        assertTrue(coreOperationCompare.equal(l, r));
    }
}