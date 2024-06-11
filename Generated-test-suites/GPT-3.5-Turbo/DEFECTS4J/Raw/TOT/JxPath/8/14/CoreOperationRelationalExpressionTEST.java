import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue_GreaterThan() {
        // Typical use case
        // Expected: true
        Expression args[] = new Expression[2];
        args[0] = new Expression("5");
        args[1] = new Expression("3");
        CoreOperationRelationalExpression operation = new CoreOperationRelationalExpression(args);
        assertEqual(operation.computeValue(context), true);

        // Edge case
        // Expected: false
        args[0] = new Expression("3.0");
        args[1] = new Expression("3");
        operation = new CoreOperationRelationalExpression(args);
        assertEqual(operation.computeValue(context), false);

        // Error scenario: Negative numbers
        // Expected: true
        args[0] = new Expression("-5");
        args[1] = new Expression("-10");
        operation = new CoreOperationRelationalExpression(args);
        assertEqual(operation.computeValue(context), true);
    }

    @Test
    public void testComputeValue_GreaterThanOrEqualTo() {
        // Typical use case
        // Expected: true
        Expression args[] = new Expression[2];
        args[0] = new Expression("3");
        args[1] = new Expression("3");
        CoreOperationRelationalExpression operation = new CoreOperationRelationalExpression(args);
        assertEqual(operation.computeValue(context), true);

        // Edge case
        // Expected: false
        args[0] = new Expression("3.0");
        args[1] = new Expression("5");
        operation = new CoreOperationRelationalExpression(args);
        assertEqual(operation.computeValue(context), false);

        // Error scenario: Negative numbers
        // Expected: false
        args[0] = new Expression("-5");
        args[1] = new Expression("0");
        operation = new CoreOperationRelationalExpression(args);
        assertEqual(operation.computeValue(context), false);
    }

    @Test
    public void testComputeValue_LessThan() {
        // Typical use case
        // Expected: false
        Expression args[] = new Expression[2];
        args[0] = new Expression("5");
        args[1] = new Expression("3");
        CoreOperationRelationalExpression operation = new CoreOperationRelationalExpression(args);
        assertEqual(operation.computeValue(context), false);

        // Edge case
        // Expected: true
        args[0] = new Expression("3");
        args[1] = new Expression("3.0");
        operation = new CoreOperationRelationalExpression(args);
        assertEqual(operation.computeValue(context), true);

        // Error scenario: Negative numbers
        // Expected: false
        args[0] = new Expression("-5");
        args[1] = new Expression("-10");
        operation = new CoreOperationRelationalExpression(args);
        assertEqual(operation.computeValue(context), false);
    }

    @Test
    public void testComputeValue_LessThanOrEqualTo() {
        // Typical use case
        // Expected: true
        Expression args[] = new Expression[2];
        args[0] = new Expression("3");
        args[1] = new Expression("3");
        CoreOperationRelationalExpression operation = new CoreOperationRelationalExpression(args);
        assertEqual(operation.computeValue(context), true);

        // Edge case
        // Expected: true
        args[0] = new Expression("3");
        args[1] = new Expression("5");
        operation = new CoreOperationRelationalExpression(args);
        assertEqual(operation.computeValue(context), true);

        // Error scenario: Negative numbers
        // Expected: true
        args[0] = new Expression("-5");
        args[1] = new Expression("0");
        operation = new CoreOperationRelationalExpression(args);
        assertEqual(operation.computeValue(context), true);
    }
}