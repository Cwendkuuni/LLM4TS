package org.apache.commons.jxpath.ri.compiler.test;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.apache.commons.jxpath.ArrayValues;
import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.JxPathContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqualTest;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.ExpressionFactory;
import org.apache.commons.jxpath.ri.compiler.SimpleNode;
import org.apache.commons.jxpath.ri.compiler.ast.AndExpr;
import org.apache.commons.jxpath.ri.compiler.ast.BinaryOperator;
import org.apache.commons.jxpath.ri.compiler.ast.ExpressionNode;
import org.apache.commons.jxpath.ri.compiler.ast.OrExpr;
import org.apache.commons.jxpath.ri.compiler.ast.UnaryOperator;

public class CoreOperationEqualTest extends TestCase {

    public void testCoreOperationEqual() {
        // Test with primitive types
        ExpressionNode arg1 = ExpressionFactory.createNumber(1);
        ExpressionNode arg2 = ExpressionFactory.createNumber(1);
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        Object result = operation.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, result);

        arg1 = ExpressionFactory.createNumber(1);
        arg2 = ExpressionFactory.createNumber(2);
        operation = new CoreOperationEqual(arg1, arg2);
        result = operation.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.FALSE, result);

        // Test with strings
        arg1 = ExpressionFactory.createString("Hello");
        arg2 = ExpressionFactory.createString("Hello");
        operation = new CoreOperationEqual(arg1, arg2);
        result = operation.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, result);

        arg1 = ExpressionFactory.createString("Hello");
        arg2 = ExpressionFactory.createString("World");
        operation = new CoreOperationEqual(arg1, arg2);
        result = operation.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.FALSE, result);

        // Test with arrays
        ExpressionNode arg1Array = ExpressionFactory.createArray(new Object[]{1, 2, 3, 4});
        ExpressionNode arg2Array = ExpressionFactory.createArray(new Object[]{1, 2, 3, 4});
        operation = new CoreOperationEqual(arg1Array, arg2Array);
        result = operation.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, result);

        arg1Array = ExpressionFactory.createArray(new Object[]{1, 2, 3, 4});
        arg2Array = ExpressionFactory.createArray(new Object[]{1, 2, 3, 5});
        operation = new CoreOperationEqual(arg1Array, arg2Array);
        result = operation.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.FALSE, result);

        // Test with complex types
        SimpleNode arg1Complex = new SimpleNode();
        arg1Complex.setValue("Hello");
        SimpleNode arg2Complex = new SimpleNode();
        arg2Complex.setValue("Hello");
        ExpressionNode arg1 = ExpressionFactory.createNode(arg1Complex);
        ExpressionNode arg2 = ExpressionFactory.createNode(arg2Complex);
        operation = new CoreOperationEqual(arg1, arg2);
        result = operation.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, result);

        arg1Complex.setValue("Hello");
        arg2Complex.setValue("World");
        operation = new CoreOperationEqual(arg1, arg2);
        result = operation.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.FALSE, result);

        // Test with boolean values
        ExpressionNode arg1Boolean = ExpressionFactory.createBoolean(true);
        ExpressionNode arg2Boolean = ExpressionFactory.createBoolean(true);
        operation = new CoreOperationEqual(arg1Boolean, arg2Boolean);
        result = operation.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, result);

        arg1Boolean = ExpressionFactory.createBoolean(true);
        arg2Boolean = ExpressionFactory.createBoolean(false);
        operation = new CoreOperationEqual(arg1Boolean, arg2Boolean);
        result = operation.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.FALSE, result);

        // Test with null values
        ExpressionNode arg1Null = ExpressionFactory.createNull();
        ExpressionNode arg2Null = ExpressionFactory.createNull();
        operation = new CoreOperationEqual(arg1Null, arg2Null);
        result = operation.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, result);

        arg1Null = ExpressionFactory.createNull();
        arg2 = ExpressionFactory.createNumber(1);
        operation = new CoreOperationEqual(arg1Null, arg2);
        result = operation.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.FALSE, result);

        // Test with different types
        arg1 = ExpressionFactory.createNumber(1);
        arg2 = ExpressionFactory.createString("Hello");
        operation = new CoreOperationEqual(arg1, arg2);
        result = operation.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.FALSE, result);

        // Test with complex expressions
        ExpressionNode arg1ComplexExpression = ExpressionFactory.createAdd(ExpressionFactory.createNumber(1), ExpressionFactory.createNumber(1));
        ExpressionNode arg2ComplexExpression = ExpressionFactory.createAdd(ExpressionFactory.createNumber(2), ExpressionFactory.createNumber(2));
        operation = new CoreOperationEqual(arg1ComplexExpression, arg2ComplexExpression);
        result = operation.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.FALSE, result);

        arg1ComplexExpression = ExpressionFactory.createAdd(ExpressionFactory.createNumber(1), ExpressionFactory.createNumber(1));
        arg2ComplexExpression = ExpressionFactory.createAdd(ExpressionFactory.createNumber(1), ExpressionFactory.createNumber(1));
        operation = new CoreOperationEqual(arg1ComplexExpression, arg2ComplexExpression);
        result = operation.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, result);

        // Test with parentheses
        ExpressionNode arg1Parentheses = ExpressionFactory.createParenthesis(arg1ComplexExpression);
        ExpressionNode arg2Parentheses = ExpressionFactory.createParenthesis(arg2ComplexExpression);
        operation = new CoreOperationEqual(arg1Parentheses, arg2Parentheses);
        result = operation.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, result);

        // Test with logical operators
        ExpressionNode left1 = ExpressionFactory.createNumber(1);
        ExpressionNode right1 = ExpressionFactory.createNumber(1);
        ExpressionNode left2 = ExpressionFactory.createNumber(1);
        ExpressionNode right2 = ExpressionFactory.createNumber(2);
        ExpressionNode left3 = ExpressionFactory.createNumber(1);
        ExpressionNode right3 = ExpressionFactory.createNumber(2);
        ExpressionNode expression1 = ExpressionFactory.createAnd(ExpressionFactory.createAnd(left1, right1), left3);
        ExpressionNode expression2 = ExpressionFactory.createAnd(left1, ExpressionFactory.createOr(left3, right2));
        operation = new CoreOperationEqual(expression1, expression2);
        result = operation.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.FALSE, result);

        // Test with unary operators
        ExpressionNode arg1Unary = ExpressionFactory.createUnaryMinus(ExpressionFactory.createNumber(1));
        ExpressionNode arg2 = ExpressionFactory.createNumber(1);
        operation = new CoreOperationEqual(arg1Unary, arg2);
        result = operation.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.FALSE, result);

        arg1Unary = ExpressionFactory.createUnaryMinus(ExpressionFactory.createNumber(1));
        operation = new CoreOperationEqual(arg1Unary, ExpressionFactory.createUnaryMinus(arg2));
        result = operation.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, result);

        // Test with array values
        ExpressionNode arg1ArrayValues = ExpressionFactory.createArrayValue(new Object[]{ExpressionFactory.createNumber(1), ExpressionFactory.createNumber(2)});
        ExpressionNode arg2ArrayValues = ExpressionFactory.createArrayValue(new Object[]{ExpressionFactory.createNumber(1), ExpressionFactory.createNumber(2)});
        operation = new CoreOperationEqual(arg1ArrayValues, arg2ArrayValues);
        result = operation.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, result);

        arg1ArrayValues = ExpressionFactory.createArrayValue(new Object[]{ExpressionFactory.createNumber(1), ExpressionFactory.createNumber(2)});
        ExpressionNode arg2ArrayValuesSingle = ExpressionFactory.createArrayValue(new Object[]{ExpressionFactory.createNumber(1)});
        operation = new CoreOperationEqual(arg1ArrayValues, arg2ArrayValuesSingle);
        result = operation.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.FALSE, result);

        // Test with multiple arguments
        ExpressionNode arg1Multiple = ExpressionFactory.createNumber(1);
        ExpressionNode arg2Multiple = ExpressionFactory.createNumber(2);
        ExpressionNode arg3Multiple = ExpressionFactory.createNumber(3);
        ExpressionNode arg4Multiple = ExpressionFactory.createNumber(3);
        ExpressionNode expressionMultiple = ExpressionFactory.createOr(ExpressionFactory.createAnd(arg1Multiple, arg2Multiple), ExpressionFactory.createAnd(arg3Multiple, arg4Multiple));
        operation = new CoreOperationEqual(arg1Multiple, expressionMultiple);
        result = operation.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.FALSE, result);

        // Test with subclassing
        ExpressionNode arg1Subclass = new SubClassExpressionNode();
        ExpressionNode arg2Subclass = new SubClassExpressionNode();
        operation = new CoreOperationEqual(arg1Subclass, arg2Subclass);
        result = operation.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, result);

        // Test with JXPath Context
        JxPathContext jxPathContext = new JxPathContext();
        ExpressionNode arg1JxPath = ExpressionFactory.createNumber(1);
        ExpressionNode arg2JxPath = ExpressionFactory.createNumber(1);
        operation = new CoreOperationEqual(arg1Jxpath, arg2Jxpath);
        result = operation.computeValue(jxPathContext);
        assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, result);

        arg1JxPath = ExpressionFactory.createNumber(1);
        arg2JxPath = ExpressionFactory.createNumber(2);
        operation = new CoreOperationEqual(arg1JxPath, arg2JxPath);
        result = operation.computeValue(jxPathContext);
        assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.FALSE, result);

        arg1JxPath = ExpressionFactory.createNode(new SimpleNode());
        arg2JxPath = ExpressionFactory.createNode(new SimpleNode());
        operation = new CoreOperationEqual(arg1JxPath, arg2JxPath);
        result = operation.computeValue(jxPathContext);
        assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, result);

        arg1JxPath = ExpressionFactory.createNode(new SimpleNode());
        arg2JxPath = ExpressionFactory.createNumber(1);
        operation = new CoreOperationEqual(arg1JxPath, arg2JxPath);
        result = operation.computeValue(jxPathContext);
        assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    private static class SubClassExpressionNode extends SimpleNode {
        @Override
        public String getSymbol() {
            return "SubClass";
        }

        @Override
        public Object computeValue(EvalContext context) {
            return null;
        }
    }
}