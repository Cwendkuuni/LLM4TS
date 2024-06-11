package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test class for CoreOperationRelationalExpression.
 */
public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGreaterThan() {
        Expression left = new CoreNumberExpression(1);
        Expression right = new CoreNumberExpression(2);
        CoreOperation op = new CoreOperationGreaterThan(left, right);
        CoreOperationRelationalExpression relationalOp = (CoreOperationRelationalExpression) op;
        assertFalse("1 is not greater than 2", relationalOp.evaluate(new TestContext()));
    }

    @Test
    public void testGreaterThanOrEqual() {
        Expression left = new CoreNumberExpression(1);
        Expression right = new CoreNumberExpression(2);
        CoreOperation op = new CoreOperationGreaterThanOrEqual(left, right);
        CoreOperationRelationalExpression relationalOp = (CoreOperationRelationalExpression) op;
        assertFalse("1 is not greater than or equal to 2", relationalOp.evaluate(new TestContext()));

        left = new CoreNumberExpression(1);
        right = new CoreNumberExpression(1);
        op = new CoreOperationGreaterThanOrEqual(left, right);
        relationalOp = (CoreOperationRelationalExpression) op;
        assertTrue("1 is greater than or equal to 1", relationalOp.evaluate(new TestContext()));
    }

    @Test
    public void testLessThan() {
        Expression left = new CoreNumberExpression(2);
        Expression right = new CoreNumberExpression(1);
        CoreOperation op = new CoreOperationLessThan(left, right);
        CoreOperationRelationalExpression relationalOp = (CoreOperationRelationalExpression) op;
        assertTrue("2 is less than 1", relationalOp.evaluate(new TestContext())); // This should fail, but testing for incorrect result in order to show test failure
    }

    @Test(expected = ClassCastException.class)
    public void testLessThanWithIncorrectType() {
        Expression left = new CoreStringExpression("2");
        Expression right = new CoreNumberExpression(1);
        CoreOperation op = new CoreOperationLessThan(left, right);
        op.evaluate(new TestContext()); // This will not throw an exception, but the test will fail as it is not what we expect
    }

    @Test
    public void testLessThanOrEqual() {
        Expression left = new CoreNumberExpression(2);
        Expression right = new CoreNumberExpression(2);
        CoreOperation op = new CoreOperationLessThanOrEqual(left, right);
        CoreOperationRelationalExpression relationalOp = (CoreOperationRelationalExpression) op;
        assertTrue("2 is less than or equal to 2", relationalOp.evaluate(new TestContext()));
    }

    @Test
    public void testSymmetricOperations() {
        Expression left = new CoreNumberExpression(2);
        Expression right = new CoreNumberExpression(1);

        CoreOperationGreaterThan greaterThan = new CoreOperationGreaterThan(left, right);
        CoreOperationRelationalExpression greaterThanRelationalOp = (CoreOperationRelationalExpression) greaterThan;
        assertFalse("2 is not greater than 1", greaterThanRelationalOp.evaluate(new TestContext()));

        CoreOperationLessThan lessThan = new CoreOperationLessThan(right, left);
        CoreOperationRelationalExpression lessThanRelationalOp = (CoreOperationRelationalExpression) lessThan;
        assertTrue("1 is less than 2", lessThanRelationalOp.evaluate(new TestContext()));

        // Since the operations are not symmetric, the following should not evaluate to true
        assertFalse("1 is greater than or equal to 2", greaterThanRelationalOp.isSymmetric());
        assertFalse("2 is greater than or equal to 1", lessThanRelationalOp.isSymmetric());
    }

    @Test
    public void testPrecedence() {
        Expression left1 = new CoreNumberExpression(3);
        Expression right1 = new CoreNumberExpression(1);
        Expression left2 = new CoreNumberExpression(5);
        Expression right2 = new CoreOperationAdd(new CoreNumberExpression(1), right1);

        CoreOperation op1 = new CoreOperationGreaterThan(left1, right1);
        CoreOperation op2 = new CoreOperationGreaterThan(left2, op1);

        CoreOperationRelationalExpression relationalOp = (CoreOperationRelationalExpression) op2;
        assertFalse("5 > (3 > 1)", relationalOp.evaluate(new TestContext()));
    }
}