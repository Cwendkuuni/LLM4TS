import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.EvaluationException;
import org.apache.commons.jxpath.ri.InfoSet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.parameterized.Parameters;

import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class CoreOperationCompareTest {

    private Expression left;
    private Expression right;
    private CoreOperationCompare testObject;

    @Parameters(name = "{index}: test({0}, {1})")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new LiteralExpression("1"), new LiteralExpression("1")},
                {new LiteralExpression("1"), new LiteralExpression("2")},
                {new LiteralExpression("true"), new LiteralExpression("true")},
                {new LiteralExpression("true"), new LiteralExpression("false")},
                {new LiteralExpression("1.0"), new LiteralExpression("1.0")},
                {new LiteralExpression("1.0"), new LiteralExpression("2.0")},
                {new VariableExpression("x"), new VariableExpression("x")},
                {new VariableExpression("x"), new LiteralExpression("1")},
                {new LiteralExpression("{}"), new LiteralExpression("{}")},
                {new LiteralExpression("{}"), new LiteralExpression(new Integer(1))},
                {new CollectionExpression(new ArrayList<Expression>(){{ add(new LiteralExpression("1")); add(new LiteralExpression("2")); }}), new CollectionExpression(new ArrayList<Expression>(){{ add(new LiteralExpression("1")); add(new LiteralExpression("1")); }})},
                {new CollectionExpression(new ArrayList<Expression>(){{ add(new LiteralExpression("1")); add(new LiteralExpression("2")); }}), new CollectionExpression(new ArrayList<Expression>(){{ add(new LiteralExpression("2")); add(new LiteralExpression("1")); }})}
        });
    }

    public CoreOperationCompareTest(Expression left, Expression right) {
        this.left = left;
        this.right = right;
        this.testObject = new CoreOperationCompare(left, right);
    }

    @Test
    public void testEqual() throws EvaluationException {
        InfoSet infoSet = new JXPathContext().setAttributes(null).setNodes(new Object[]{new Object[]{}}).createInfoSet();
        EvalContext context = new EvalContext(infoSet);
        assertTrue(testObject.equal(context, left, right));
    }

    @Test
    public void testNotEqual() throws EvaluationException {
        InfoSet infoSet = new JXPathContext().setAttributes(null).setNodes(new Object[]{new Object[]{}}).createInfoSet();
        EvalContext context = new EvalContext(infoSet);
        assertFalse(testObject.equal(context, left, right));
    }

    @Test(expected = EvaluationException.class)
    public void testNullLeft() throws EvaluationException {
        InfoSet infoSet = new JXPathContext().setAttributes(null).setNodes(new Object[]{new Object[]{}}).createInfoSet();
        EvalContext context = new EvalContext(infoSet);
        testObject.equal(context, new NullExpression(), right);
    }

    @Test(expected = EvaluationException.class)
    public void testNullRight() throws EvaluationException {
        InfoSet infoSet = new JXPathContext().setAttributes(null).setNodes(new Object[]{new Object[]{}}).createInfoSet();
        EvalContext context = new EvalContext(infoSet);
        testObject.equal(context, left, new NullExpression());
    }

    @Test(expected = EvaluationException.class)
    public void testIteratorsOfDifferentLength() throws EvaluationException {
        InfoSet leftInfoSet = new JXPathContext().setAttributes(null).setNodes(new Object[]{new Object[]{1, 2, 3}, new Object[]{}})
                .createInfoSet();
        InfoSet rightInfoSet = new JXPathContext().setAttributes(null).setNodes(new Object[]{new Object[]{1, 2}, new Object[]{}})
                .createInfoSet();
        EvalContext leftContext = new EvalContext(leftInfoSet);
        EvalContext rightContext = new EvalContext(rightInfoSet);
        testObject.equal(leftContext, new CollectionExpression(left.getExpressions()), new CollectionExpression(right.getExpressions()));
    }

    @Test(expected = EvaluationException.class)
    public void testIteratorsWithDifferentTypes() throws EvaluationException {
        InfoSet leftInfoSet = new JXPathContext().setAttributes(null).setNodes(new Object[]{new Object[]{1, 2, 3}, new Object[]{}})
                .createInfoSet();
        InfoSet rightInfoSet = new JXPathContext().setAttributes(null).setNodes(new Object[]{new Object[]{1.0, 2.0}, new Object[]{}})
                .createInfoSet();
        EvalContext leftContext = new EvalContext(leftInfoSet);
        EvalContext rightContext = new EvalContext(rightInfoSet);
        testObject.equal(leftContext, new CollectionExpression(left.getExpressions()), new CollectionExpression(right.getExpressions()));
    }
}