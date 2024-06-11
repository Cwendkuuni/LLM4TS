import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    private CoreOperationRelationalExpression expression;

    @Before
    public void setup() {
        expression = new CoreOperationRelationalExpression(new Expression[2]);
    }

    @Test
    public void testComputeValue_LeftIsLessThanRight_ReturnsTrue() {
        EvalContext context = new EvalContext();

        Object left = 1;
        Object right = 2;
        
        context.setSingleNodePointer(left);

        boolean result = expression.computeValue(context);

        assertTrue(result);
    }

    @Test
    public void testComputeValue_LeftIsEqualToRight_ReturnsFalse() {
        EvalContext context = new EvalContext();
        
        Object left = 2;
        Object right = 2;
        
        context.setSingleNodePointer(left);

        boolean result = expression.computeValue(context);

        assertFalse(result);
    }

    @Test
    public void testComputeValue_LeftIsGreaterThanRight_ReturnsFalse() {
        EvalContext context = new EvalContext();
        
        Object left = 2;
        Object right = 1;
        
        context.setSingleNodePointer(left);

        boolean result = expression.computeValue(context);

        assertFalse(result);
    }

    @Test
    public void testComputeValue_LeftIsNull_ReturnsFalse() {
        EvalContext context = new EvalContext();
        
        Object left = null;
        Object right = 1;
        
        context.setSingleNodePointer(left);

        boolean result = expression.computeValue(context);

        assertFalse(result);
    }

    @Test
    public void testComputeValue_RightIsNull_ReturnsFalse() {
        EvalContext context = new EvalContext();
        
        Object left = 1;
        Object right = null;
        
        context.setSingleNodePointer(left);

        boolean result = expression.computeValue(context);

        assertFalse(result);
    }

    @Test
    public void testComputeValue_BothAreNull_ReturnsFalse() {
        EvalContext context = new EvalContext();
        
        Object left = null;
        Object right = null;
        
        context.setSingleNodePointer(left);

        boolean result = expression.computeValue(context);

        assertFalse(result);
    }

    @Test
    public void testComputeValue_LeftIsCollectionAndContainsMatch_ReturnsTrue() {
        EvalContext context = new EvalContext();
        
        Object left = new HashSet<>();
        ((HashSet) left).add(1);
        ((HashSet) left).add(2);
        ((HashSet) left).add(3);
        
        Object right = 2;
        
        context.setSingleNodePointer(left);

        boolean result = expression.computeValue(context);

        assertTrue(result);
    }

    @Test
    public void testComputeValue_LeftIsCollectionAndDoesNotContainMatch_ReturnsFalse() {
        EvalContext context = new EvalContext();
        
        Object left = new HashSet<>();
        ((HashSet) left).add(1);
        ((HashSet) left).add(3);
        ((HashSet) left).add(5);
        
        Object right = 2;
        
        context.setSingleNodePointer(left);

        boolean result = expression.computeValue(context);

        assertFalse(result);
    }

    @Test
    public void testComputeValue_RightIsCollectionAndContainsMatch_ReturnsTrue() {
        EvalContext context = new EvalContext();
        
        Object left = 2;
        
        Object right = new HashSet<>();
        ((HashSet) right).add(1);
        ((HashSet) right).add(2);
        ((HashSet) right).add(3);
        
        context.setSingleNodePointer(left);

        boolean result = expression.computeValue(context);

        assertTrue(result);
    }

    @Test
    public void testComputeValue_RightIsCollectionAndDoesNotContainMatch_ReturnsFalse() {
        EvalContext context = new EvalContext();
        
        Object left = 2;
        
        Object right = new HashSet<>();
        ((HashSet) right).add(1);
        ((HashSet) right).add(3);
        ((HashSet) right).add(5);
        
        context.setSingleNodePointer(left);

        boolean result = expression.computeValue(context);

        assertFalse(result);
    }

    @Test
    public void testComputeValue_LeftAndRightAreCollectionsAndNoMatchOccurs_ReturnsFalse() {
        EvalContext context = new EvalContext();
        
        Object left = new HashSet<>();
        ((HashSet) left).add(1);
        ((HashSet) left).add(3);
        ((HashSet) left).add(5);

        Object right = new HashSet<>();
        ((HashSet) right).add(2);
        ((HashSet) right).add(4);
        ((HashSet) right).add(6);
        
        context.setSingleNodePointer(left);

        boolean result = expression.computeValue(context);

        assertFalse(result);
    }

    @Test
    public void testComputeValue_LeftAndRightAreCollectionsAndMatchOccurs_ReturnsTrue() {
        EvalContext context = new EvalContext();
        
        Object left = new HashSet<>();
        ((HashSet) left).add(1);
        ((HashSet) left).add(3);
        ((HashSet) left).add(5);

        Object right = new HashSet<>();
        ((HashSet) right).add(2);
        ((HashSet) right).add(3);
        ((HashSet) right).add(4);
        
        context.setSingleNodePointer(left);

        boolean result = expression.computeValue(context);

        assertTrue(result);
    }

}