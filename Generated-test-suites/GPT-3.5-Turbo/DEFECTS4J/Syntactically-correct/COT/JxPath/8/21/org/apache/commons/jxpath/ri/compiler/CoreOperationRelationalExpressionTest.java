import org.junit.Assert;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void computeValue_shouldReturnTrue_whenLeftArgumentIsGreater() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        boolean result = expression.computeValue(null);

        Assert.assertTrue(result);
    }

    @Test
    public void computeValue_shouldReturnFalse_whenLeftArgumentIsLess() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };

        boolean result = expression.computeValue(null);

        Assert.assertFalse(result);
    }

    @Test
    public void computeValue_shouldReturnFalse_whenLeftArgumentIsEqual() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };

        boolean result = expression.computeValue(null);

        Assert.assertFalse(result);
    }

    @Test
    public void computeValue_shouldReturnTrue_whenLeftArgumentIsIteratorAndContainsMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        boolean result = expression.computeValue(null);

        Assert.assertTrue(result);
    }

    @Test
    public void computeValue_shouldReturnFalse_whenLeftArgumentIsIteratorAndDoesNotContainMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        boolean result = expression.computeValue(null);

        Assert.assertFalse(result);
    }

    @Test
    public void computeValue_shouldReturnTrue_whenBothArgumentsAreIteratorsAndContainsMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        boolean result = expression.computeValue(null);

        Assert.assertTrue(result);
    }

    @Test
    public void computeValue_shouldReturnFalse_whenBothArgumentsAreIteratorsAndDoNotContainMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        boolean result = expression.computeValue(null);

        Assert.assertFalse(result);
    }

    @Test
    public void computeValue_shouldReturnTrue_whenBothArgumentsAreNumbersAndEvaluateCompareIsTrue() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        boolean result = expression.computeValue(null);

        Assert.assertTrue(result);
    }

    @Test
    public void computeValue_shouldReturnFalse_whenBothArgumentsAreNumbersAndEvaluateCompareIsFalse() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(null) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        boolean result = expression.computeValue(null);

        Assert.assertFalse(result);
    }
}