package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CoreOperationGreaterThanOrEqualTest {

    private CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual;
    private EvalContext evalContext;

    @Before
    public void setUp() {
        evalContext = new EvalContext();
        coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(
                new ExpressionStub("5"), new ExpressionStub("3"));
    }

    @Test
    public void testComputeValue() {
        Object result = coreOperationGreaterThanOrEqual.computeValue(evalContext);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGetSymbol() {
        String symbol = coreOperationGreaterThanOrEqual.getSymbol();
        assertEquals(">=", symbol);
    }
}

class ExpressionStub extends Expression {
    private final String value;

    ExpressionStub(String value) {
        this.value = value;
    }

    @Override
    public Object computeValue(EvalContext evalContext) {
        return value;
    }
}