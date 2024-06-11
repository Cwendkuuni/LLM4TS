package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {
    private CoreOperationGreaterThanOrEqual operation;
    private EvalContext context;

    @Before
    public void setup() {
        operation = new CoreOperationGreaterThanOrEqual(new DummyExpression(), new DummyExpression());
        context = new EvalContext();
    }

    @Test
    public void testComputeValue() {
        context.setValue("a", 10);
        context.setValue("b", 5);
        assertEquals(Boolean.TRUE, operation.computeValue(context));

        context.setValue("a", 5);
        context.setValue("b", 10);
        assertEquals(Boolean.FALSE, operation.computeValue(context));

        context.setValue("a", 5);
        context.setValue("b", 5);
        assertEquals(Boolean.TRUE, operation.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        assertEquals(">=", operation.getSymbol());
    }
}

class DummyExpression extends Expression {
    @Override
    public Object computeValue(EvalContext context) {
        return null;
    }
}