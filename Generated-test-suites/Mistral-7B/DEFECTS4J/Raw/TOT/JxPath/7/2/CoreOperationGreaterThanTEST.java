package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.NodeInfoConst;
import org.apache.commons.jxpath.ri.Path;
import org.apache.commons.jxpath.ri.expr.Expression;
import org.apache.commons.jxpath.ri.expr.Variable;
import org.apache.commons.jxpath.ri.util.ExpressionBuilder;
import org.junit.Test;

public class CoreOperationGreaterThanTest {

    @Test
    public void testGreaterThanPositive() {
        Expression arg1 = new Variable("x");
        Expression arg2 = new Variable("y");
        Expression exp = new CoreOperationGreaterThan(arg1, arg2);

        EvalContext context = new EvalContext();
        context.setCurrentNode(new NodeInfoConst("5.5"));
        InfoSet infoSet = new InfoSet();
        infoSet.setCurrentNode(context.getCurrentNode());
        context.setInfoSet(infoSet);

        context.setVariable("x", new Double(7.0));
        context.setVariable("y", new Double(5.0));

        Object result = exp.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testGreaterThanZero() {
        Expression arg1 = new Variable("x");
        Expression arg2 = new ExpressionBuilder().number(0.0).build();
        Expression exp = new CoreOperationGreaterThan(arg1, arg2);

        EvalContext context = new EvalContext();
        context.setCurrentNode(new NodeInfoConst("5.5"));
        InfoSet infoSet = new InfoSet();
        context.setInfoSet(infoSet);

        context.setVariable("x", new Double(5.0));

        Object result = exp.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testGreaterThanNegative() {
        Expression arg1 = new Variable("x");
        Expression arg2 = new Variable("y");
        Expression exp = new CoreOperationGreaterThan(arg1, arg2);

        EvalContext context = new EvalContext();
        context.setCurrentNode(new NodeInfoConst("-5.5"));
        InfoSet infoSet = new InfoSet();
        context.setInfoSet(infoSet);

        context.setVariable("x", new Double(-3.0));
        context.setVariable("y", new Double(-7.0));

        Object result = exp.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test(expected = RuntimeException.class)
    public void testGreaterThanTypeMismatch() {
        Expression arg1 = new Variable("x");
        Expression arg2 = new Variable("y");
        Expression exp = new CoreOperationGreaterThan(arg1, arg2);

        EvalContext context = new EvalContext();
        context.setCurrentNode(new NodeInfoConst("5.5"));
        InfoSet infoSet = new InfoSet();
        context.setInfoSet(infoSet);

        context.setVariable("x", new NodeInfoConst("5.5"));
        context.setVariable("y", new Double(5.0));

        exp.computeValue(context);
    }

    @Test(expected = RuntimeException.class)
    public void testGreaterThanNull() {
        Expression arg1 = new Variable("x");
        Expression arg2 = new Variable("y");
        Expression exp = new CoreOperationGreaterThan(arg1, arg2);

        EvalContext context = new EvalContext();
        context.setCurrentNode(new NodeInfoConst("5.5"));
        InfoSet infoSet = new InfoSet();
        context.setInfoSet(infoSet);

        context.setVariable("x", new Double(5.0));
        context.setVariable("y", null);

        exp.computeValue(context);
    }

    @Test(expected = RuntimeException.class)
    public void testGreaterThanNaN() {
        Expression arg1 = new Variable("x");
        Expression arg2 = new Variable("y");
        Expression exp = new CoreOperationGreaterThan(arg1, arg2);

        EvalContext context = new EvalContext();
        context.setCurrentNode(new NodeInfoConst("5.5"));
        InfoSet infoSet = new InfoSet();
        context.setInfoSet(infoSet);

        context.setVariable("x", new Double(Double.NaN));
        context.setVariable("y", new Double(5.0));

        exp.computeValue(context);
    }
}