
package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.Pointer;

public class CoreOperationCompareTest {
    @Test
    public void testEqual() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        EvalContext context = new EvalContext();
        
        assertFalse(coreOperationCompare.equal(context, arg1, arg2));

        context.reset();
        context.setSingleNodePointer(new Pointer());
        assertFalse(coreOperationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testContains() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        Iterator it = Arrays.asList(1, 2, 3).iterator();
        
        assertFalse(coreOperationCompare.contains(it, 4));
        assertTrue(coreOperationCompare.contains(it, 2));
    }

    @Test
    public void testFindMatch() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        Iterator lit = Arrays.asList(1, 2, 3).iterator();
        Iterator rit = Arrays.asList(4, 5, 6).iterator();
        
        assertFalse(coreOperationCompare.findMatch(lit, rit));

        lit = Arrays.asList(1, 2, 3).iterator();
        rit = Arrays.asList(2, 5, 6).iterator();
        assertTrue(coreOperationCompare.findMatch(lit, rit));
    }

    @Test
    public void testEqual() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare();
        
        assertFalse(coreOperationCompare.equal(null, null));
        assertTrue(coreOperationCompare.equal(1, 1));
    }
}
