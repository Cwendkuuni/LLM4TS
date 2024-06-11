package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.test.TestSupport;

import java.util.Arrays;

public class CoreOperationLessThanTest {

    @Test
    public void testCoreOperationLessThan() {
        CoreOperationLessThan op = new CoreOperationLessThan(
                new CoreConstExpression(1.0),
                new CoreConstExpression(2.0));

        EvalContext context = new TestEvalContext();
        context.setCurrentInfoSet(TestSupport.createAndSetUpSimpleInfoSet());

        assertEquals(Boolean.FALSE, op.computeValue(context));

        context.setCurrentInfoSet(TestSupport.createAndSetUpSimpleInfoSet("1.5"));
        context.setVariable("x", new Double(1.5));

        assertEquals(Boolean.TRUE, op.computeValue(context));

        context.setCurrentInfoSet(TestSupport.createAndSetUpSimpleInfoSet("3.0"));
        context.setVariable("x", new Double(3.0));

        assertEquals(Boolean.FALSE, op.computeValue(context));
    }

    @Test
    public void testCoreOperationLessThanWithComplexExpressions() {
        CoreOperationLessThan op = new CoreOperationLessThan(
                new CoreOperationAdd(
                        new CoreOperationMultiply(
                                new CoreVariableExpression("x"),
                                new CoreConstExpression(2.0)
                        ),
                        new CoreVariableExpression("y")
                ),
                new CoreConstExpression(5.0)
        );

        EvalContext context = new TestEvalContext();
        context.setCurrentInfoSet(TestSupport.createAndSetUpSimpleInfoSet());

        context.setVariable("x", new Double(1.0));
        context.setVariable("y", new Double(3.0));

        assertEquals(Boolean.FALSE, op.computeValue(context));

        context.setVariable("x", new Double(1.5));
        context.setVariable("y", new Double(3.5));

        assertEquals(Boolean.TRUE, op.computeValue(context));

        context.setVariable("x", new Double(5.0));
        context.setVariable("y", new Double(5.0));

        assertEquals(Boolean.FALSE, op.computeValue(context));
    }

    @Test
    public void testCoreOperationLessThanWithNull() {
        CoreOperationLessThan op = new CoreOperationLessThan(
                new CoreConstExpression(1.0),
                new CoreVariableExpression(null)
        );

        EvalContext context = new TestEvalContext();
        context.setCurrentInfoSet(TestSupport.createAndSetUpSimpleInfoSet());

        assertNull(op.computeValue(context));
    }

    @Test
    public void testCoreOperationLessThanWithMixedTypes() {
        CoreOperationLessThan op = new CoreOperationLessThan(
                new CoreConstExpression(1.0),
                new CoreVariableExpression(new Integer(2))
        );

        EvalContext context = new TestEvalContext();
        context.setCurrentInfoSet(TestSupport.createAndSetUpSimpleInfoSet());

        assertFalse(op.computeValue(context) instanceof Boolean);
        assertNull(op.computeValue(context));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationLessThanWithInvalidArgumentTypes() {
        CoreOperationLessThan op = new CoreOperationLessThan(
                new CoreConstExpression("foo"),
                new CoreConstExpression(1.0)
        );
    }

    @Test
    public void testCoreOperationLessThanWithMultipleArguments() {
        try {
            new CoreOperationLessThan(Arrays.asList(
                    new CoreConstExpression(1.0),
                    new CoreConstExpression(2.0),
                    new CoreConstExpression(3.0)
            ).toArray(new Expression[0]));
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}