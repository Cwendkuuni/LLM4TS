import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.jsr223.JXPathContext;
import org.apache.commons.jxpath.ri.parser.JXPathParser;
import org.apache.commons.jxpath.ri.util.ExpressionUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class CoreOperationLessThanTest {

    @Test
    public void testLessThanWithNumbers() {
        // Test with valid numbers
        InfoSet infoSet = InfoSetUtil.createInfoSet(new Object[]{5.0, 3.0});
        CoreOperationLessThan lessThan = new CoreOperationLessThan(
                ExpressionUtil.createConstantExpression(5.0),
                ExpressionUtil.createConstantExpression(3.0)
        );
        Assert.assertEquals(Boolean.TRUE, lessThan.computeValue(new EvalContext(new JXPathContext(infoSet))));

        infoSet = InfoSetUtil.createInfoSet(new Object[]{10.0, 3.0});
        Assert.assertEquals(Boolean.TRUE, lessThan.computeValue(new EvalContext(new JXPathContext(infoSet))));

        infoSet = InfoSetUtil.createInfoSet(new Object[]{1.0, 1.0});
        Assert.assertEquals(Boolean.FALSE, lessThan.computeValue(new EvalContext(new JXPathContext(infoSet))));
    }

    @Test
    public void testLessThanWithNullAndNumber() {
        // Test with null and number
        InfoSet infoSet = InfoSetUtil.createInfoSet(new Object[]{5.0, null});
        CoreOperationLessThan lessThan = new CoreOperationLessThan(
                ExpressionUtil.createConstantExpression(5.0),
                ExpressionUtil.createVariableExpression("x")
        );
        lessThan.setVariable("x", null);
        Assert.assertEquals(Boolean.FALSE, lessThan.computeValue(new EvalContext(new JXPathContext(infoSet))));
    }

    @Test
    public void testLessThanWithStrings() {
        // Test with strings
        InfoSet infoSet = InfoSetUtil.createInfoSet(new Object[]{"5", "3"});
        CoreOperationLessThan lessThan = new CoreOperationLessThan(
                ExpressionUtil.createConstantExpression("5"),
                ExpressionUtil.createConstantExpression("3")
        );
        Assert.assertFalse(lessThan.computeValue(new EvalContext(new JXPathContext(infoSet))));

        infoSet = InfoSetUtil.createInfoSet(new Object[]{"apple", "banana"});
        Assert.assertFalse(lessThan.computeValue(new EvalContext(new JXPathContext(infoSet))));
    }

    @Test
    public void testLessThanWithMixedTypes() {
        // Test with mixed types
        InfoSet infoSet = InfoSetUtil.createInfoSet(new Object[]{5.0, "3"});
        CoreOperationLessThan lessThan = new CoreOperationLessThan(
                ExpressionUtil.createConstantExpression(5.0),
                ExpressionUtil.createConstantExpression("3")
        );
        Assert.assertFalse(lessThan.computeValue(new EvalContext(new JXPathContext(infoSet))));

        infoSet = InfoSetUtil.createInfoSet(new Object[]{5.0, new Integer(3)});
        Assert.assertEquals(Boolean.TRUE, lessThan.computeValue(new EvalContext(new JXPathContext(infoSet))));
    }

    @Test
    public void testLessThanWithNaN() {
        // Test with NaN
        InfoSet infoSet = InfoSetUtil.createInfoSet(new Object[]{Double.NaN, Double.NaN});
        CoreOperationLessThan lessThan = new CoreOperationLessThan(
                ExpressionUtil.createConstantExpression(Double.NaN),
                ExpressionUtil.createConstantExpression(Double.NaN)
        );
        Assert.assertFalse(lessThan.computeValue(new EvalContext(new JXPathContext(infoSet))));

        infoSet = InfoSetUtil.createInfoSet(new Object[]{Double.NaN, 3.0});
        Assert.assertFalse(lessThan.computeValue(new EvalContext(new JXPathContext(infoSet))));
    }

    @Test
    public void testLessThanWithInfiniteNumbers() {
        // Test with infinite numbers
        InfoSet infoSet = InfoSetUtil.createInfoSet(new Object[]{Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY});
        CoreOperationLessThan lessThan = new CoreOperationLessThan(
                ExpressionUtil.createConstantExpression(Double.POSITIVE_INFINITY),
                ExpressionUtil.createConstantExpression(Double.NEGATIVE_INFINITY)
        );
        Assert.assertEquals(Boolean.FALSE, lessThan.computeValue(new EvalContext(new JXPathContext(infoSet))));

        infoSet = InfoSetUtil.createInfoSet(new Object[]{Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY});
        Assert.assertEquals(Boolean.TRUE, lessThan.computeValue(new EvalContext(new JXPathContext(infoSet))));
    }

    @Test
    public void testLessThanWithEmptyInfoSet() {
        // Test with empty InfoSet
        InfoSet infoSet = InfoSetUtil.createInfoSet(new Object[0]);
        CoreOperationLessThan lessThan = new CoreOperationLessThan(
                ExpressionUtil.createConstantExpression(5.0),
                ExpressionUtil.createConstantExpression(3.0)
        );
        Assert.assertThrows(RuntimeException.class, () -> lessThan.computeValue(new EvalContext(new JXPathContext(infoSet))));
    }
}