
package org.apache.commons.jxpath.ri.compiler;

import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationCompareTest {
    @Test
    public void testEqual() {
        Expression arg1 = new DummyExpression();
        Expression arg2 = new DummyExpression();
        CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);
        EvalContext context = new EvalContext();
        assertTrue(operation.equal(context, arg1, arg2));

        arg1 = new DummyExpression();
        arg2 = new DummyExpression();
        operation = new CoreOperationCompare(arg1, arg2);
        context = new EvalContext();
        assertFalse(operation.equal(context, arg1, arg2));
    }

    @Test
    public void testContains() {
        CoreOperationCompare operation = new CoreOperationCompare(new DummyExpression(), new DummyExpression());
        Iterator it = Arrays.asList(1, 2, 3).iterator();

        assertTrue(operation.contains(it, 2));
        assertFalse(operation.contains(it, 4));
    }

    @Test
    public void testFindMatch() {
        CoreOperationCompare operation = new CoreOperationCompare(new DummyExpression(), new DummyExpression());
        Iterator lit = Arrays.asList(1, 2, 3).iterator();
        Iterator rit = Arrays.asList(2, 4, 6).iterator();

        assertTrue(operation.findMatch(lit, rit));
        assertFalse(operation.findMatch(lit, rit));
    }

    @Test
    public void testEqualObjects() {
        CoreOperationCompare operation = new CoreOperationCompare(new DummyExpression(), new DummyExpression());
        assertTrue(operation.equal(1, 1));
        assertFalse(operation.equal(1, 2));
        assertFalse(operation.equal(null, null));
        assertFalse(operation.equal(null, 1));
        assertFalse(operation.equal(1, null));
        assertEquals(1.0, operation.equal(1.0, 1.0), 0.01);
        assertFalse(operation.equal(1.0, 2.0));
        assertTrue(operation.equal("a", "a"));
        assertFalse(operation.equal("a", "b"));
    }
}

class DummyExpression extends Expression {
    @Override
    public Object compute(EvalContext context) {
        return null;
    }
}
