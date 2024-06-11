import org.apache.commons.jxpath.ri.compiler.CoreOperationNotEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationNotEqual.Args;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.test.AbstractTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CoreOperationNotEqualTest extends AbstractTest {

    @Test
    public void testNotEqualBooleanTrueAndFalse() {
        Args args = new Args() {{
            arg1 = createBooleanExpression(true);
            arg2 = createBooleanExpression(false);
        }};
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(args.arg1, args.arg2);
        Object result = notEqual.computeValue(createEvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        args.arg1 = createBooleanExpression(false);
        args.arg2 = createBooleanExpression(true);
        notEqual = new CoreOperationNotEqual(args.arg1, args.arg2);
        result = notEqual.computeValue(createEvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testNotEqualNullAndNumber() {
        Args args = new Args() {{
            arg1 = createExpressionFromString("null");
            arg2 = createNumberExpression(1);
        }};
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(args.arg1, args.arg2);
        Object result = notEqual.computeValue(createEvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        args.arg1 = createNumberExpression(1);
        args.arg2 = createExpressionFromString("null");
        notEqual = new CoreOperationNotEqual(args.arg1, args.arg2);
        result = notEqual.computeValue(createEvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testNotEqualStringAndNumber() {
        Args args = new Args() {{
            arg1 = createStringExpression("test");
            arg2 = createNumberExpression(1);
        }};
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(args.arg1, args.arg2);
        Object result = notEqual.computeValue(createEvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        args.arg1 = createNumberExpression(1);
        args.arg2 = createStringExpression("test");
        notEqual = new CoreOperationNotEqual(args.arg1, args.arg2);
        result = notEqual.computeValue(createEvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEqualWithInvalidArgTypes() {
        Args args = new Args() {{
            arg1 = createNumberExpression(1);
            arg2 = createStringExpression("test");
        }};
        new CoreOperationNotEqual(args.arg1, args.arg2);
    }

    @Test
    public void testNotEqualWithIdenticalValues() {
        Args args = new Args() {{
            arg1 = createStringExpression("test");
            arg2 = createStringExpression("test");
        }};
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(args.arg1, args.arg2);
        Object result = notEqual.computeValue(createEvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testNotEqualWithNullArg1() {
        Args args = new Args() {{
            arg1 = createExpressionFromString("null");
            arg2 = createNumberExpression(1);
        }};
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(args.arg1, args.arg2);
        Object result = notEqual.computeValue(createEvalContext());
        assertNull(result);
    }

    @Test
    public void testNotEqualWithNullArg2() {
        Args args = new Args() {{
            arg1 = createNumberExpression(1);
            arg2 = createExpressionFromString("null");
        }};
        CoreOperationNotEqual notEqual = new CoreOperationNotEqual(args.arg1, args.arg2);
        Object result = notEqual.computeValue(createEvalContext());
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }
}