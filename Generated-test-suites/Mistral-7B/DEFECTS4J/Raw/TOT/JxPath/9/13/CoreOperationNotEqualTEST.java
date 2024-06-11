package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.core.Context;
import org.apache.commons.jxpath.ri.core.CoreConstants;
import org.apache.commons.jxpath.ri.core.CoreFunctionCall;
import org.apache.commons.jxpath.ri.core.CoreFunctionCallExpression;
import org.apache.commons.jxpath.ri.core.CoreVariable;
import org.apache.commons.jxpath.ri.expressions.Constant;
import org.apache.commons.jxpath.ri.expressions.Variable;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class CoreOperationNotEqualTest {

    @Test
    public void testNotEqual_Boolean_Boolean() {
        Expression arg1 = new Constant(Boolean.TRUE);
        Expression arg2 = new Constant(Boolean.FALSE);
        CoreOperationNotEqual expression = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext(new Context(new Variable("root", new Constant(new Object() {}))));
        Object result = expression.computeValue(context);

        // typical use case
        assertThat(result, is(Boolean.TRUE));

        // edge case: both arguments are the same instance of Boolean
        arg1 = new CoreVariable("boolean1");
        arg2 = arg1;
        expression = new CoreOperationNotEqual(arg1, arg2);
        result = expression.computeValue(context);
        context.setVariable("boolean1", Boolean.TRUE);
        assertThat(result, is(Boolean.FALSE));

        // error case: arguments have different types
        arg1 = new CoreFunctionCallExpression(new CoreFunctionCall(CoreConstants.STRING_CONCAT, new Expression[]{arg1, new Constant("")}));
        arg2 = arg1;
        expression = new CoreOperationNotEqual(arg1, arg2);
        assertThat(expression, org.junit.Assert.throwsException(ClassCastException.class));

        // error case: context is null
        context = null;
        expression = new CoreOperationNotEqual(arg1, arg2);
        assertThat(expression, org.junit.Assert.throwsException(IllegalArgumentException.class));
    }

    // Add more test cases for different data types and combinations
}