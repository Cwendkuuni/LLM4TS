import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    private CoreOperationRelationalExpression expression;

    @Before
    public void setUp() {
        expression = new CoreOperationRelationalExpression() {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
    }

    @Test
    public void testComputeValue_ValidInput_ReturnsBoolean() {
        EvalContext context = new EvalContext();
        context.setVariable("var1", 5);
        context.setVariable("var2", 10);
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
    }

    @Test
    public void testComputeValue_BothOperandsNumeric_ReturnsTrue() {
        EvalContext context = new EvalContext();
        context.setVariable("var1", 5);
        context.setVariable("var2", 10);
        Object result = expression.computeValue(context);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_LeftOperandNaN_ReturnsFalse() {
        EvalContext context = new EvalContext();
        context.setVariable("var1", Double.NaN);
        context.setVariable("var2", 10);
        Object result = expression.computeValue(context);
        assertFalse((Boolean) result);
    }

    @Test
    public void testComputeValue_RightOperandNaN_ReturnsFalse() {
        EvalContext context = new EvalContext();
        context.setVariable("var1", 5);
        context.setVariable("var2", Double.NaN);
        Object result = expression.computeValue(context);
        assertFalse((Boolean) result);
    }

    @Test
    public void testComputeValue_BothOperandsNaN_ReturnsFalse() {
        EvalContext context = new EvalContext();
        context.setVariable("var1", Double.NaN);
        context.setVariable("var2", Double.NaN);
        Object result = expression.computeValue(context);
        assertFalse((Boolean) result);
    }

    @Test
    public void testComputeValue_LeftOperandLessThanRightOperand_ReturnsTrue() {
        EvalContext context = new EvalContext();
        context.setVariable("var1", 5);
        context.setVariable("var2", 10);
        Object result = expression.computeValue(context);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_LeftOperandGreaterThanRightOperand_ReturnsFalse() {
        EvalContext context = new EvalContext();
        context.setVariable("var1", 15);
        context.setVariable("var2", 10);
        Object result = expression.computeValue(context);
        assertFalse((Boolean) result);
    }

    @Test
    public void testComputeValue_LeftOperandEqualToRightOperand_ReturnsFalse() {
        EvalContext context = new EvalContext();
        context.setVariable("var1", 10);
        context.setVariable("var2", 10);
        Object result = expression.computeValue(context);
        assertFalse((Boolean) result);
    }

    @Test
    public void testComputeValue_LeftOperandCollectionRightOperandValueInCollection_ReturnsTrue() {
        EvalContext context = new EvalContext();
        context.setVariable("var1", new int[]{1, 2, 3, 4, 5});
        context.setVariable("var2", 3);
        Object result = expression.computeValue(context);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_LeftOperandCollectionRightOperandValueNotInCollection_ReturnsFalse() {
        EvalContext context = new EvalContext();
        context.setVariable("var1", new int[]{1, 2, 3, 4, 5});
        context.setVariable("var2", 6);
        Object result = expression.computeValue(context);
        assertFalse((Boolean) result);
    }

    @Test
    public void testComputeValue_LeftOperandCollectionRightOperandCollectionIntersect_ReturnsTrue() {
        EvalContext context = new EvalContext();
        context.setVariable("var1", new int[]{1, 2, 3, 4, 5});
        context.setVariable("var2", new int[]{3, 4, 5, 6, 7});
        Object result = expression.computeValue(context);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_LeftOperandCollectionRightOperandCollectionNoIntersect_ReturnsFalse() {
        EvalContext context = new EvalContext();
        context.setVariable("var1", new int[]{1, 2, 3, 4, 5});
        context.setVariable("var2", new int[]{6, 7, 8, 9, 10});
        Object result = expression.computeValue(context);
        assertFalse((Boolean) result);
    }

}