import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{new Expression(), new Expression()}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        assertFalse((Boolean) expression.computeValue(new EvalContext()));
        assertTrue((Boolean) expression.computeValue(new EvalContext()));

        expression = new CoreOperationRelationalExpression(new Expression[]{new Expression(), new Expression()}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };

        assertFalse((Boolean) expression.computeValue(new EvalContext()));
        assertTrue((Boolean) expression.computeValue(new EvalContext()));
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{new Expression(), new Expression()}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        assertEquals(3, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{new Expression(), new Expression()}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testCompute() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{new Expression(), new Expression()}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        assertFalse(expression.compute(null, null));
        assertFalse(expression.compute(new InitialContext(), new InitialContext()));

        expression = new CoreOperationRelationalExpression(new Expression[]{new Expression(), new Expression()}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };

        assertFalse(expression.compute(null, null));
        assertFalse(expression.compute(new InitialContext(), new InitialContext()));

        expression = new CoreOperationRelationalExpression(new Expression[]{new Expression(), new Expression()}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };

        assertTrue(expression.compute(null, null));
        assertTrue(expression.compute(new InitialContext(), new InitialContext()));
    }

    @Test
    public void testReduce() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{new Expression(), new Expression()}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        SelfContext selfContext = new SelfContext();
        assertEquals(selfContext, expression.reduce(selfContext));

        Collection<String> collection = Arrays.asList("foo");
        Iterator<String> iterator = collection.iterator();
        assertEquals(iterator, expression.reduce(collection));
    }

    @Test
    public void testContainsMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{new Expression(), new Expression()}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        HashSet<String> set = new HashSet<>(Arrays.asList("foo", "bar"));
        Iterator<String> iterator = set.iterator();
        assertTrue(expression.containsMatch(iterator, "foo"));
        assertFalse(expression.containsMatch(iterator, "baz"));
    }

    @Test
    public void testFindMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{new Expression(), new Expression()}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };


        HashSet<String> set1 = new HashSet<>(Arrays.asList("foo", "bar"));
        HashSet<String> set2 = new HashSet<>(Arrays.asList("baz", "qux"));
        Iterator<String> iterator1 = set1.iterator();
        Iterator<String> iterator2 = set2.iterator();
        assertTrue(expression.findMatch(iterator1, iterator2));

        set1 = new HashSet<>(Arrays.asList("foo", "bar"));
        set2 = new HashSet<>(Arrays.asList("baz", "qux"));
        iterator1 = set1.iterator();
        iterator2 = set2.iterator();
        assertFalse(expression.findMatch(iterator1, iterator2));
    }
}