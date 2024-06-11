package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThan;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.expression.Expression;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {

    @Test
    public void testCoreOperationGreaterThanBasic() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(
                new LiteralsTestHelper().createNumberLiteral(5.0),
                new LiteralsTestHelper().createNumberLiteral(3.0)
        );

        EvalContext context = new EvalContext(null, new InfoSet());
        Object result = operation.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testCoreOperationGreaterThanFalse() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(
                new LiteralsTestHelper().createNumberLiteral(3.0),
                new LiteralsTestHelper().createNumberLiteral(5.0)
        );

        EvalContext context = new EvalContext(null, new InfoSet());
        Object result = operation.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test(expected = ClassCastException.class)
    public void testCoreOperationGreaterThanMixedTypes() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(
                new LiteralsTestHelper().createNumberLiteral(5.0),
                new LiteralsTestHelper().createStringLiteral("3")
        );

        EvalContext context = new EvalContext(null, new InfoSet());
        operation.computeValue(context);
    }

    @Test
    public void testCoreOperationGreaterThanEqual() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(
                new LiteralsTestHelper().createNumberLiteral(5.0),
                new LiteralsTestHelper().createNumberLiteral(5.0)
        );

        EvalContext context = new EvalContext(null, new InfoSet());
        Object result = operation.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testCoreOperationGreaterThanNegative() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(
                new LiteralsTestHelper().createNumberLiteral(-3.0),
                new LiteralsTestHelper().createNumberLiteral(-2.0)
        );

        EvalContext context = new EvalContext(null, new InfoSet());
        Object result = operation.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationGreaterThanNull() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(
                null,
                new LiteralsTestHelper().createNumberLiteral(5.0)
        );

        EvalContext context = new EvalContext(null, new InfoSet());
        operation.computeValue(context);
    }

    @Test
    public void testCoreOperationGreaterThanZero() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(
                new LiteralsTestHelper().createNumberLiteral(0.0),
                new LiteralsTestHelper().createNumberLiteral(5.0)
        );

        EvalContext context = new EvalContext(null, new InfoSet());
        Object result = operation.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }
}