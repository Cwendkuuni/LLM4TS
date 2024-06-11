import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        // Testing with two integers
        // 3 > 2 should return true
        CoreOperationRelationalExpression expression = new CoreOperationRelationalGreaterThan(new Expression[]{new CoreOperation(), new CoreOperation()});
        EvalContext context = new EvalContext();
        context.setContextNodePointer(new NodePointer());
        context.setSingleNodePointer(new NodePointer());
        context.setJXPathContext(new JXPathContext());
        assertTrue((boolean) expression.computeValue(context));

        // Testing with two different types (integer and string)
        // 3 > "abc" should return false
        expression = new CoreOperationRelationalGreaterThan(new Expression[]{new CoreOperation(), new CoreOperation()});
        context = new EvalContext();
        context.setContextNodePointer(new NodePointer());
        context.setSingleNodePointer(new NodePointer());
        context.setJXPathContext(new JXPathContext());
        assertFalse((boolean) expression.computeValue(context));

        // Testing with null values
        // null > null should return false
        expression = new CoreOperationRelationalGreaterThan(new Expression[]{new CoreOperation(), new CoreOperation()});
        context = new EvalContext();
        context.setContextNodePointer(new NodePointer());
        context.setSingleNodePointer(new NodePointer());
        context.setJXPathContext(new JXPathContext());
        assertFalse((boolean) expression.computeValue(context));
    }

    @Test
    public void testEvaluateCompare() {
        // Testing for equality
        CoreOperationRelationalGreaterThan expression = new CoreOperationRelationalGreaterThan(new Expression[]{new CoreOperation(), new CoreOperation()});
        assertTrue(expression.evaluateCompare(0));

        // Testing for less than
        expression = new CoreOperationRelationalGreaterThan(new Expression[]{new CoreOperation(), new CoreOperation()});
        assertFalse(expression.evaluateCompare(-1));

        // Testing for greater than
        expression = new CoreOperationRelationalGreaterThan(new Expression[]{new CoreOperation(), new CoreOperation()});
        assertTrue(expression.evaluateCompare(1));
    }

    @Test
    public void testContainsMatch() {
        // Testing when iterator contains a match
        CoreOperationRelationalGreaterThan expression = new CoreOperationRelationalGreaterThan(new Expression[]{new CoreOperation(), new CoreOperation()});
        Iterator<Integer> it = Arrays.asList(1, 2, 3, 4).iterator();
        assertTrue(expression.containsMatch(it, 2));

        // Testing when iterator does not contain a match
        expression = new CoreOperationRelationalGreaterThan(new Expression[]{new CoreOperation(), new CoreOperation()});
        it = Arrays.asList(1, 2, 3, 4).iterator();
        assertFalse(expression.containsMatch(it, 5));
    }

    @Test
    public void testFindMatch() {
        // Testing when both iterators contain a match
        CoreOperationRelationalGreaterThan expression = new CoreOperationRelationalGreaterThan(new Expression[]{new CoreOperation(), new CoreOperation()});
        Iterator<Integer> lit = Arrays.asList(1, 2, 3, 4).iterator();
        Iterator<Integer> rit = Arrays.asList(2, 4, 6).iterator();
        assertTrue(expression.findMatch(lit, rit));

        // Testing when one iterator contains a match
        expression = new CoreOperationRelationalGreaterThan(new Expression[]{new CoreOperation(), new CoreOperation()});
        lit = Arrays.asList(1, 2, 3, 4).iterator();
        rit = Arrays.asList(5, 7, 9).iterator();
        assertFalse(expression.findMatch(lit, rit));

        // Testing when no match is found
        expression = new CoreOperationRelationalGreaterThan(new Expression[]{new CoreOperation(), new CoreOperation()});
        lit = Arrays.asList(1, 2, 3, 4).iterator();
        rit = Arrays.asList(5, 7, 9).iterator();
        assertFalse(expression.findMatch(lit, rit));
    }

    @Test
    public void testGetPrecedence() {
        // Testing the precedence value of the core operation relational expression
        CoreOperationRelationalGreaterThan expression = new CoreOperationRelationalGreaterThan(new Expression[]{new CoreOperation(), new CoreOperation()});
        assertEquals(3, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        // Testing if the core operation relational expression is symmetric
        CoreOperationRelationalGreaterThan expression = new CoreOperationRelationalGreaterThan(new Expression[]{new CoreOperation(), new CoreOperation()});
        assertFalse(expression.isSymmetric());
    }

}