import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    private CoreOperationRelationalExpression expression;
    private EvalContext dummyContext;

    @Before
    public void setUp() {
        expression = new CoreOperationRelationalExpressionStub(new Expression[2]);
        dummyContext = new EvalContext();
    }

    // Test computeValue() method

    @Test
    public void testComputeValue_ReturnsTrue() {
        dummyContext.setValues(5, 10);
        assertEquals(true, expression.computeValue(dummyContext));

        dummyContext.setValues(5.0, 5.0);
        assertEquals(true, expression.computeValue(dummyContext));

        dummyContext.setValues("abc", "xyz");
        assertEquals(true, expression.computeValue(dummyContext));
    }

    @Test
    public void testComputeValue_ReturnsFalse() {
        dummyContext.setValues(10, 5);
        assertEquals(false, expression.computeValue(dummyContext));

        dummyContext.setValues(5.0, 10.0);
        assertEquals(false, expression.computeValue(dummyContext));

        dummyContext.setValues("xyz", "abc");
        assertEquals(false, expression.computeValue(dummyContext));
    }

    // Test getPrecedence() method

    @Test
    public void testGetPrecedence_ReturnsRelationalExpressionPrecedence() {
        int expectedPrecedence = CoreOperation.RELATIONAL_EXPR_PRECEDENCE;
        assertEquals(expectedPrecedence, expression.getPrecedence());
    }

    // Test isSymmetric() method

    @Test
    public void testIsSymmetric_ReturnsFalse() {
        assertEquals(false, expression.isSymmetric());
    }

    // Test evaluateCompare() method

    @Test
    public void testEvaluateCompare_ReturnsTrue() {
        int compare = 0;
        assertEquals(true, expression.evaluateCompare(compare));

        compare = -1;
        assertEquals(true, expression.evaluateCompare(compare));

        compare = 1;
        assertEquals(true, expression.evaluateCompare(compare));
    }

    @Test
    public void testEvaluateCompare_ReturnsFalse() {
        int compare = 2;
        assertEquals(false, expression.evaluateCompare(compare));

        compare = -2;
        assertEquals(false, expression.evaluateCompare(compare));

        compare = 100;
        assertEquals(false, expression.evaluateCompare(compare));
    }

    // Test compute() method

    @Test
    public void testCompute_ReturnsTrue() {
        Object left = new CoreOperationRelationalExpressionStub(new Expression[2]);
        Object right = new CoreOperationRelationalExpressionStub(new Expression[2]);
        assertEquals(true, expression.compute(left, right));
    }

    @Test
    public void testCompute_ReturnsFalse() {
        Object left = 5;
        Object right = 10;
        assertEquals(false, expression.compute(left, right));
    }

    // Test reduce() method

    @Test
    public void testReduce_ReturnsReducedOperand() {
        Object operand = dummyContext;
        assertEquals(dummyContext, expression.reduce(operand));

        operand = new ArrayList<>();
        assertEquals(((ArrayList) operand).iterator(), expression.reduce(operand));

        operand = new CoreOperationRelationalExpressionStub(new Expression[2]);
        assertEquals(operand, expression.reduce(operand));
    }

    // Test containsMatch() method

    @Test
    public void testContainsMatch_ReturnsTrue() {
        Iterator iterator = Arrays.asList(1, 2, 3, 4, 5).iterator();
        Object value = 3;
        assertEquals(true, expression.containsMatch(iterator, value));
    }

    @Test
    public void testContainsMatch_ReturnsFalse() {
        Iterator iterator = Arrays.asList(1, 2, 3, 4, 5).iterator();
        Object value = 6;
        assertEquals(false, expression.containsMatch(iterator, value));
    }

    // Test findMatch() method

    @Test
    public void testFindMatch_ReturnsTrue() {
        Iterator leftIterator = Arrays.asList(1, 2, 3, 4, 5).iterator();
        Iterator rightIterator = Arrays.asList(5, 6, 7, 8, 9).iterator();
        assertEquals(true, expression.findMatch(leftIterator, rightIterator));
    }

    @Test
    public void testFindMatch_ReturnsFalse() {
        Iterator leftIterator = Arrays.asList(1, 2, 3, 4, 5).iterator();
        Iterator rightIterator = Arrays.asList(6, 7, 8, 9, 10).iterator();
        assertEquals(false, expression.findMatch(leftIterator, rightIterator));
    }

    // Test 

    @Test
    public void testCoreOperationRelationalExpressionTest() {
        assertNotNull(expression);
    }
}

class CoreOperationRelationalExpressionStub extends CoreOperationRelationalExpression {
    protected CoreOperationRelationalExpressionStub(Expression[] args) {
        super(args);
    }

    protected boolean evaluateCompare(int compare) {
        return true;
    }
}