import org.junit.Assert;
import org.junit.Test;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValue_TypicalCase() {
        // Typical case: arg1 > arg2, expected result: true
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);

        EvalContext context = new EvalContext();
        arg1.computeValue(context);
        arg2.computeValue(context);

        Object result = operation.computeValue(context);

        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_EqualValues() {
        // Case where arg1 and arg2 are equal, expected result: false
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);

        EvalContext context = new EvalContext();
        arg1.setExpressionValue(5); // Set arg1 value to 5
        arg2.setExpressionValue(5); // Set arg2 value to 5

        Object result = operation.computeValue(context);

        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_Arg1GreaterThanArg2() {
        // Case where arg1 is greater than arg2, expected result: true
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);

        EvalContext context = new EvalContext();
        arg1.setExpressionValue(10); // Set arg1 value to 10
        arg2.setExpressionValue(5); // Set arg2 value to 5

        Object result = operation.computeValue(context);

        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_Arg2GreaterThanArg1() {
        // Case where arg2 is greater than arg1, expected result: false
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);

        EvalContext context = new EvalContext();
        arg1.setExpressionValue(5); // Set arg1 value to 5
        arg2.setExpressionValue(10); // Set arg2 value to 10

        Object result = operation.computeValue(context);

        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_Arg1StringArg2Int() {
        // Case where arg1 is a String and arg2 is an int, expected result: error
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);

        EvalContext context = new EvalContext();
        arg1.setExpressionValue("5"); // Set arg1 value as String "5"
        arg2.setExpressionValue(5); // Set arg2 value as int 5

        // Since arg1 and arg2 are of different types, an error is expected
        try {
            operation.computeValue(context);
            Assert.fail("Expected ClassCastException to be thrown");
        } catch (ClassCastException e) {
            // Success
        }
    }

    @Test
    public void testComputeValue_Arg1NullArg2NotNull() {
        // Case where arg1 is null and arg2 is not null, expected result: error
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);

        EvalContext context = new EvalContext();
        arg1.setExpressionValue(null); // Set arg1 value as null
        arg2.setExpressionValue(5); // Set arg2 value to 5

        // Since arg1 is null, an error is expected
        try {
            operation.computeValue(context);
            Assert.fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Success
        }
    }

    @Test
    public void testComputeValue_Arg1NotNullArg2Null() {
        // Case where arg1 is not null and arg2 is null, expected result: error
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);

        EvalContext context = new EvalContext();
        arg1.setExpressionValue(5); // Set arg1 value to 5
        arg2.setExpressionValue(null); // Set arg2 value as null

        // Since arg2 is null, an error is expected
        try {
            operation.computeValue(context);
            Assert.fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Success
        }
    }

}