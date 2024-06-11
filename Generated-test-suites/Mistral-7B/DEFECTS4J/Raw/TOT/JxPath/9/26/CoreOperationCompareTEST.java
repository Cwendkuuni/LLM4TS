import org.apache.commons.jxpath.EvaluationException;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.AXISException;
import org.apache.commons.jxpath.ri.CompiledExpression;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.IterateIterators;
import org.apache.commons.jxpath.ri.Iterators;
import org.apache.commons.jxpath.ri.NodeIterators;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class CoreOperationCompareTest {

    @Test
    public void testEquals() throws AXISException, EvaluationException {
        Expression left = new Const("1");
        Expression right = new Const("1");
        CoreOperationCompare compare = new Eq(left, right);
        JXPathContext ctx = new JXPathContext();
        CompiledExpression compiledExpression = ctx.compile(compare);
        List result = (List) compiledExpression.evaluate(new Object[]{"1"}, new Object[]{0}, null);
        Assert.assertTrue(result.isEmpty());

        left = new Const(null);
        right = new Const(null);
        compare = new Eq(left, right);
        compiledExpression = ctx.compile(compare);
        result = (List) compiledExpression.evaluate(new Object[]{null}, new Object[]{0}, null);
        Assert.assertTrue(result.isEmpty());

        left = new Const("1");
        right = new Const("2");
        compare = new Eq(left, right);
        compiledExpression = ctx.compile(compare);
        result = (List) compiledExpression.evaluate(new Object[]{"1"}, new Object[]{0}, null);
        Assert.assertFalse(result.isEmpty());
        Assert.assertEquals(0, result.size());

        left = new Const("1");
        right = new Const("1.0");
        compare = new Eq(left, right);
        compiledExpression = ctx.compile(compare);
        result = (List) compiledExpression.evaluate(new Object[]{"1"}, new Object[]{0}, null);
        Assert.assertTrue(result.isEmpty());

        left = new Const("1.0");
        right = new Const("1");
        compare = new Eq(left, right);
        compiledExpression = ctx.compile(compare);
        result = (List) compiledExpression.evaluate(new Object[]{"1.0"}, new Object[]{0}, null);
        Assert.assertFalse(result.isEmpty());
        Assert.assertEquals(0, result.size());

        left = new Const("true");
        right = new Const("true");
        compare = new Eq(left, right);
        compiledExpression = ctx.compile(compare);
        result = (List) compiledExpression.evaluate(new Object[]{"true"}, new Object[]{0}, null);
        Assert.assertTrue(result.isEmpty());

        left = new Const("true");
        right = new Const("false");
        compare = new Eq(left, right);
        compiledExpression = ctx.compile(compare);
        result = (List) compiledExpression.evaluate(new Object[]{"true"}, new Object[]{0}, null);
        Assert.assertFalse(result.isEmpty());
        Assert.assertEquals(0, result.size());

        left = new Const("false");
        right = new Const("true");
        compare = new Eq(left, right);
        compiledExpression = ctx.compile(compare);
        result = (List) compiledExpression.evaluate(new Object[]{"false"}, new Object[]{0}, null);
        Assert.assertFalse(result.isEmpty());
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void testNotEquals() throws AXISException, EvaluationException {
        Expression left = new Const("1");
        Expression right = new Const("1");
        CoreOperationCompare compare = new Ne(left, right);
        JXPathContext ctx = new JXPathContext();
        CompiledExpression compiledExpression = ctx.compile(compare);
        List result = (List) compiledExpression.evaluate(new Object[]{"1"}, new Object[]{0}, null);
        Assert.assertTrue(result.isEmpty());

        left = new Const("1");
        right = new Const("2");
        compare = new Ne(left, right);
        compiledExpression = ctx.compile(compare);
        result = (List) compiledExpression.evaluate(new Object[]{"1"}, new Object[]{0}, null);
        Assert.assertFalse(result.isEmpty());
        Assert.assertEquals(1, result.size());

        left = new Const("1");
        right = new Const("1.0");
        compare = new Ne(left, right);
        compiledExpression = ctx.compile(compare);
        result = (List) compiledExpression.evaluate(new Object[]{"1"}, new Object[]{0}, null);
        Assert.assertFalse(result.isEmpty());
        Assert.assertEquals(1, result.size());

        left = new Const("1.0");
        right = new Const("1");
        compare = new Ne(left, right);
        compiledExpression = ctx.compile(compare);
        result = (List) compiledExpression.evaluate(new Object[]{"1.0"}, new Object[]{0}, null);
        Assert.assertFalse(result.isEmpty());
        Assert.assertEquals(1, result.size());

        left = new Const("true");
        right = new Const("true");
        compare = new Ne(left, right);
        compiledExpression = ctx.compile(compare);
        result = (List) compiledExpression.evaluate(new Object[]{"true"}, new Object[]{0}, null);
        Assert.assertTrue(result.isEmpty());

        left = new Const("true");
        right = new Const("false");
        compare = new Ne(left, right);
        compiledExpression = ctx.compile(compare);
        result = (List) compiledExpression.evaluate(new Object[]{"true"}, new Object[]{0}, null);
        Assert.assertFalse(result.isEmpty());
        Assert.assertEquals(1, result.size());

        left = new Const("false");
        right = new Const("true");
        compare = new Ne(left, right);
        compiledExpression = ctx.compile(compare);
        result = (List) compiledExpression.evaluate(new Object[]{"false"}, new Object[]{0}, null);
        Assert.assertFalse(result.isEmpty());
        Assert.assertEquals(1, result.size());
    }

    @Test
    public void testContains() throws AXISException, EvaluationException {
        Expression left = new NodeSet("(1)//*[contains(.,'a')]");
        Expression right = new StringConst("a");
        CoreOperationCompare compare = new Contains(left, right);
        JXPathContext ctx = new JXPathContext();
        CompiledExpression compiledExpression = ctx.compile(compare);
        List result = (List) compiledExpression.evaluate(new Object[]{null}, new Object[]{0}, null);
        Assert.assertTrue(result.isEmpty());

        Object[] objects = new Object[]{"<root><a>1</a><b>2</b></root>"};
        result = (List) compiledExpression.evaluate(objects, new Object[]{0}, null);
        Assert.assertEquals(1, result.size());

        Object[] objects1 = new Object[]{"<root><a>1</a><b>2</b></root>"};
        Expression left1 = new NodeSet("(1)//*[contains(.,'b')]");
        result = (List) compiledExpression.evaluate(objects1, new Object[]{left1}, null);
        Assert.assertFalse(result.isEmpty());
        Assert.assertEquals(0, result.size());

        Object[] objects2 = new Object[]{"<root><a>1</a><b>2</b></root>"};
        Expression right1 = new StringConst("c");
        result = (List) compiledExpression.evaluate(objects2, new Object[]{left1, right1}, null);
        Assert.assertFalse(result.isEmpty());
        Assert.assertEquals(1, result.size());

        Object[] objects3 = new Object[]{"<root><a>1</a><b>2</b></root>"};
        Iterator it = ((IterateIterators) NodeIterators.iterate(objects3, "(1)//*[contains(.,'a')]")).iterator();
        result = (List) compiledExpression.evaluate(new Object[]{it}, new Object[]{right}, null);
        Assert.assertFalse(result.isEmpty());
        Assert.assertEquals(1, result.size());
    }

    @Test
    public void testNotContains() throws AXISException, EvaluationException {
        Expression left = new NodeSet("(1)//*[contains(.,'a')]");
        Expression right = new StringConst("a");
        CoreOperationCompare compare = new NotContains(left, right);
        JXPathContext ctx = new JXPathContext();
        CompiledExpression compiledExpression = ctx.compile(compare);
        List result = (List) compiledExpression.evaluate(new Object[]{null}, new Object[]{0}, null);
        Assert.assertTrue(result.isEmpty());

        Object[] objects = new Object[]{"<root><a>1</a><b>2</b></root>"};
        result = (List) compiledExpression.evaluate(objects, new Object[]{0}, null);
        Assert.assertEquals(1, result.size());

        Object[] objects1 = new Object[]{"<root><a>1</a><b>2</b></root>"};
        Expression left1 = new NodeSet("(1)//*[contains(.,'b')]");
        result = (List) compiledExpression.evaluate(objects1, new Object[]{left1}, null);
        Assert.assertFalse(result.isEmpty());
        Assert.assertEquals(0, result.size());

        Object[] objects2 = new Object[]{"<root><a>1</a><b>2</b></root>"};
        Iterator it = ((IterateIterators) NodeIterators.iterate(objects2, "(1)//*[contains(.,'a')]")).iterator();
        result = (List) compiledExpression.evaluate(new Object[]{it}, new Object[]{right}, null);
        Assert.assertFalse(result.isEmpty());
        Assert.assertEquals(1, result.size());

        Object[] objects3 = new Object[]{"<root><a>1</a><b>2</b></root>"};
        Expression right1 = new StringConst("c");
        result = (List) compiledExpression.evaluate(objects3, new Object[]{left1, right1}, null);
        Assert.assertFalse(result.isEmpty());
        Assert.assertEquals(1, result.size());
    }

    @Test(expected = EvaluationException.class)
    public void testError1() throws AXISException, EvaluationException {
        Expression left = new Const("1");
        Expression right = new Const("2");
        CoreOperationCompare compare = new Eq(left, right);
        JXPathContext ctx = new JXPathContext();
        CompiledExpression compiledExpression = ctx.compile(compare);
        List result = (List) compiledExpression.evaluate(new Object[]{"1", "2"}, new Object[]{0, 1}, null);
        Assert.fail();
    }

    @Test(expected = EvaluationException.class)
    public void testError2() throws AXISException, EvaluationException {
        Expression left = new Const("1");
        Expression right = new Const("1.0");
        CoreOperationCompare compare = new Eq(left, right);
        JXPathContext ctx = new JXPathContext();
        CompiledExpression compiledExpression = ctx.compile(compare);
        List result = (List) compiledExpression.evaluate(new Object[]{"1"}, new Object[]{0}, null);
        Assert.fail();
    }

    @Test(expected = EvaluationException.class)
    public void testError3() throws AXISException, EvaluationException {
        Expression left = new Const("true");
        Expression right = new Const("false");
        CoreOperationCompare compare = new Eq(left, right);
        JXPathContext ctx = new JXPathContext();
        CompiledExpression compiledExpression = ctx.compile(compare);
        List result = (List) compiledExpression.evaluate(new Object[]{"true"}, new Object[]{0}, null);
        Assert.fail();
    }
}