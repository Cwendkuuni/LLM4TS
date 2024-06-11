package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.expression.Expression;
import org.apache.commons.jxpath.ri.expression.Variable;

public class CoreOperationEqualTest {

    @Test
    public void testCoreOperationEqual() {
        Expression expr1 = new Variable("x");
        Expression expr2 = new Variable("y");
        CoreOperationEqual operation = new CoreOperationEqual(expr1, expr2);

        Object result = operation.computeValue(new EvalContext());
        assertEquals(Boolean.FALSE, result);

        expr1 = new Variable("5");
        expr2 = new Variable("5");
        result = operation.computeValue(new EvalContext());
        assertEquals(Boolean.TRUE, result);

        expr1 = new Variable("5");
        expr2 = new Variable("6");
        result = operation.computeValue(new EvalContext());
        assertEquals(Boolean.FALSE, result);

        expr1 = new Variable("5.0");
        expr2 = new Variable("5.0");
        result = operation.computeValue(new EvalContext());
        assertEquals(Boolean.TRUE, result);

        expr1 = new Variable("5.0");
        expr2 = new Variable("6.0");
        result = operation.computeValue(new EvalContext());
        assertEquals(Boolean.FALSE, result);

        expr1 = new Variable("\"foo\"");
        expr2 = new Variable("\"foo\"");
        result = operation.computeValue(new EvalContext());
        assertEquals(Boolean.TRUE, result);

        expr1 = new Variable("\"foo\"");
        expr2 = new Variable("\"bar\"");
        result = operation.computeValue(new EvalContext());
        assertEquals(Boolean.FALSE, result);
    }
}