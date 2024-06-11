package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ExpressionContext;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.Node;
import org.apache.commons.jxpath.ri.CompilerTest;
import org.apache.commons.jxpath.ri.Evaluator;
import org.apache.commons.jxpath.ri.ITERATE_NO_MORE;
import org.apache.commons.jxpath.ri.Iterate;
import org.apache.commons.jxpath.ri.Iterates;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test for {@link CoreOperationCompare}
 */
public class CoreOperationCompareTest extends CompilerTest {

    /**
     * Test method for {@link CoreOperationCompare#CoreOperationCompare(Expression, Expression)}
     */
    @Test
    public void testCoreOperationCompare() {
        evaluateCoreOperationCompare("true = true", true, true);
        evaluateCoreOperationCompare("true = false", false, false);
        evaluateCoreOperationCompare("false = true", false, false);
        evaluateCoreOperationCompare("false = false", true, true);

        evaluateCoreOperationCompare("1 = 1", true, true);
        evaluateCoreOperationCompare("1 = 2", false, false);
        evaluateCoreOperationCompare("2 = 1", false, false);
        evaluateCoreOperationCompare("2 = 2", true, true);

        evaluateCoreOperationCompare("'abc' = 'abc'", true, true);
        evaluateCoreOperationCompare("'abc' = 'def'", false, false);
        evaluateCoreOperationCompare("'abc' = null", false, false);
        evaluateCoreOperationCompare("null = null", true, true);

        evaluateCoreOperationCompare("1 = [1, 2]", false, false);
        evaluateCoreOperationCompare("[1, 2] = [1, 2]", true, true);
        evaluateCoreOperationCompare("[1, 2] = [1, 3]", false, false);

        evaluateCoreOperationCompare("(1, 1) = (1, 1)", true, true);
        evaluateCoreOperationCompare("(1, 1) = (1, 2)", false, false);
        evaluateCoreOperationCompare("(1, 1) = (2, 1)", false, false);
        evaluateCoreOperationCompare("(1, 1) = (2, 2)", false, false);

        evaluateCoreOperationCompare("(1, 1) = (1, null)", false, false);
        evaluateCoreOperationCompare("(1, null) = (1, 1)", false, false);
        evaluateCoreOperationCompare("(1, null) = (null, 1)", false, false);
        evaluateCoreOperationCompare("(null, 1) = (1, 1)", false, false);

        evaluateCoreOperationCompare("(1, 1) = null", false, false);
        evaluateCoreOperationCompare("null = (1, 1)", false, false);

        evaluateCoreOperationCompare("true = null", false, false);
        evaluateCoreOperationCompare("null = true", false, false);

        evaluateCoreOperationCompare("(1, 1) = (1, 1)[1]", true, true);
        evaluateCoreOperationCompare("(1, 1)[1] = (1, 1)", true, true);

        evaluateCoreOperationCompare("(1, 1) = (1, 1)[new Integer(1)]", true, true);
        evaluateCoreOperationCompare("(1, 1)[new Integer(1)] = (1, 1)", true, true);

        // Error scenarios
        evaluateCoreOperationCompare("true =", CompilerError.class);
        evaluateCoreOperationCompare("true = false", CompilerError.class);
        evaluateCoreOperationCompare("false =", CompilerError.class);
        evaluateCoreOperationCompare("false = true", CompilerError.class);

        evaluateCoreOperationCompare("1 =", CompilerError.class);
        evaluateCoreOperationCompare("1 = 2", CompilerError.class);
        evaluateCoreOperationCompare("2 = 1", CompilerError.class);
        evaluateCoreOperationCompare("2 = 2[", CompilerError.class);

        evaluateCoreOperationCompare("'abc' =", CompilerError.class);
        evaluateCoreOperationCompare("'abc' = def", CompilerError.class);
        evaluateCoreOperationCompare("'abc' = null", CompilerError.class);
        evaluateCoreOperationCompare("null = null[", CompilerError.class);

        evaluateCoreOperationCompare("(1, 1) =", CompilerError.class);
        evaluateCoreOperationCompare("(1, 1) = (1, 2)[", CompilerError.class);
        evaluateCoreOperationCompare("(1, 1)[ = (1, 1)", CompilerError.class);
        evaluateCoreOperationCompare("(1, 1)[ = (1, 1)[", CompilerError.class);
        evaluateCoreOperationCompare("(1, 1) = (1, 1)[new Integer(", CompilerError.class);

        evaluateCoreOperationCompare("[1, 2] =", CompilerError.class);
        evaluateCoreOperationCompare("[1, 2] = [1, 2][", CompilerError.class);

        evaluateCoreOperationCompare("(1, 1) = null", CompilerError.class);
        evaluateCoreOperationCompare("null = (1, 1)", CompilerError.class);

        evaluateCoreOperationCompare("true = null", CompilerError.class);
        evaluateCoreOperationCompare("null = true", CompilerError.class);

        evaluateCoreOperationCompare("(1, 1) = (1, 1)[1 = 1]", Iterate.class);
        evaluateCoreOperationCompare("(1, 1)[1 = 1] = (1, 1)", Iterate.class);

        evaluateCoreOperationCompare("(1, 1) = (1, 1)[new Integer(1) = 1]", Iterate.class);
        evaluateCoreOperationCompare("(1, 1)[new Integer(1) = 1] = (1, 1)", Iterate.class);

        evaluateCoreOperationCompare("(1, 1) = (1, 1)[new Integer(1)] = 1", CompilerError.class);

        evaluateCoreOperationCompare("(1, 1) = (1, 1)[new Iterator() = 1]", Iterator.class);
        evaluateCoreOperationCompare("(1, 1)[new Iterator() = 1] = (1, 1)", Iterator.class);

        evaluateCoreOperationCompare("(1, 1) = (1, 1)[null = 1]", Iterator.class);
        evaluateCoreOperationCompare("(1, 1)[null = 1] = (1, 1)", Iterator.class);

        evaluateCoreOperationCompare("(1, 1) = (1, 1)[new Object() = 1]", CompilerError.class);

        evaluateCoreOperationCompare("(1, 1) = (1, 1)[1 = null]", Iterate.class);
        evaluateCoreOperationCompare("(1, 1)[1 = null] = (1, 1)", Iterate.class);

        evaluateCoreOperationCompare("(1, 1) = (1, 1)[new Integer(1) = null]", Iterate.class);
        evaluateCoreOperationCompare("(1, 1)[new Integer(1) = null] = (1, 1)", Iterate.class);

        evaluateCoreOperationCompare("(1, 1) = (1, 1)[new Iterator() = null]", Iterator.class);
        evaluateCoreOperationCompare("(1, 1)[new Iterator() = null] = (1, 1)", Iterator.class);

        evaluateCoreOperationCompare("(1, 1) = (1, 1)[new Object() = null]", CompilerError.class);

        evaluateCoreOperationCompare("(1, 1) = (1, 1)[new Iterator() = new Iterator()]", Iterator.class);
        evaluateCoreOperationCompare("(1, 1)[new Iterator() = new Iterator()] = (1, 1)", Iterator.class);

        evaluateCoreOperationCompare("(1, 1) = (1, 1)[new Object() = new Object()]", CompilerError.class);

        evaluateCoreOperationCompare("(1, 1) = (1, 1)[1 = new Iterator()]", Iterate.class);
        evaluateCoreOperationCompare("(1, 1)[1 = new Iterator()] = (1, 1)", Iterate.class);

        evaluateCoreOperationCompare("(1, 1) = (1, 1)[new Integer(1) = new Iterator()]", CompilerError.class);

        evaluateCoreOperationCompare("(1, 1) = (1, 1)[1 = new Object()]", Iterate.class);
        evaluateCoreOperationCompare("(1, 1)[1 = new Object()] = (1, 1)", Iterate.class);

        evaluateCoreOperationCompare("(1, 1) = (1, 1)[new Integer(1) = new Object()]", CompilerError.class);
    }

    private void evaluateCoreOperationCompare(String expression, Object expectedResult, Object... operands) {
        JXPathContext context = createContext("/test/core-operation-compare");
        Expression left = compile(context, operands[0]);
        Expression right = compile(context, operands[1]);
        CoreOperationCompare operation = (CoreOperationCompare) compile(context, expression);

        EvaluationResult result = operation.evaluate(context, new EvaluationContext(context, Arrays.asList(left, right)));

        if (expectedResult instanceof Boolean) {
            assertTrue(result.getValue() instanceof Boolean);
            assertTrue((Boolean) result.getValue() == (Boolean) expectedResult);
        } else if (expectedResult instanceof Iterator) {
            assertTrue(result.getValue() instanceof Iterator);
            Iterator actual = (Iterator) result.getValue();
            Iterator expected = (Iterator) expectedResult;
            Iterates iterates = new Iterates() {
                @Override
                public Iterator iterator() {
                    return expected;
                }
            };
            assertTrue(Iterators.areEqual(actual, iterates));
        } else if (expectedResult instanceof CompilerError) {
            assertTrue(result.getValue() instanceof CompilerError);
            assertTrue(expectedResult.getClass().isInstance(result.getValue()));
        } else if (expectedResult instanceof Iterate) {
            assertTrue(result.getValue() instanceof Iterate);
            Iterate actual = (Iterate) result.getValue();
            Iterate expected = (Iterate) expectedResult;
            assertTrue(Iterators.areEqual(actual.getIterator(), expected.getIterator()));
            assertTrue(actual.getLastNode() == expected.getLastNode());
        } else {
            throw new IllegalStateException("Unsupported expected result type: " + expectedResult.getClass());
        }
    }

    private Node compile(JXPathContext context, Object object) {
        Node node = compile(context, String.valueOf(object));
        return node;
    }

    private Node compile(JXPathContext context, String expression) {
        return compile(context, expression, null);
    }

    private Node compile(JXPathContext context, String expression, List<Object> operands) {
        Expression compiledExpression = compileExpression(context, expression);
        if (compiledExpression instanceof CoreOperationCompare) {
            if (operands != null && operands.size() < 2) {
                throw new IllegalArgumentException("Expression " + expression + " requires 2 operands");
            }
            CoreOperationCompare coreOperationCompare = (CoreOperationCompare) compiledExpression;
            coreOperationCompare.setOperands(operands.toArray(new Object[0]));
            return compiledExpression;
        }
        return (Node) compiledExpression;
    }
}