package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.nodes.NumberNode;
import org.junit.Test;

public class CoreOperationLessThanTest {

    @Test
    public void testCoreOperationLessThan_TwoNumbers_ReturnsCorrectResult() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(
                new NumberNode(1.0), new NumberNode(2.0));
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext();
        context.setInfoSet(infoSet);

        Boolean result = (Boolean) coreOperationLessThan.computeValue(context);
        assertTrue(result);
    }

    @Test
    public void testCoreOperationLessThan_TwoNumbersWithSameValue_ReturnsFalse() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(
                new NumberNode(1.0), new NumberNode(1.0));
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext();
        context.setInfoSet(infoSet);

        Boolean result = (Boolean) coreOperationLessThan.computeValue(context);
        assertFalse(result);
    }

    @Test(expected = ClassCastException.class)
    public void testCoreOperationLessThan_ArgumentsAreNotNumbers_ThrowsClassCastException() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(
                new NodeInfo("test"), new NodeInfo("test"));
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext();
        context.setInfoSet(infoSet);

        coreOperationLessThan.computeValue(context);
    }

    @Test
    public void testCoreOperationLessThan_FirstArgumentIsNaN_ReturnsFalse() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(
                new NumberNode(Double.NaN), new NumberNode(1.0));
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext();
        context.setInfoSet(infoSet);

        Boolean result = (Boolean) coreOperationLessThan.computeValue(context);
        assertFalse(result);
    }

    @Test
    public void testCoreOperationLessThan_SecondArgumentIsNaN_ReturnsTrue() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(
                new NumberNode(1.0), new NumberNode(Double.NaN));
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext();
        context.setInfoSet(infoSet);

        Boolean result = (Boolean) coreOperationLessThan.computeValue(context);
        assertTrue(result);
    }

    @Test
    public void testCoreOperationLessThan_FirstArgumentIsZero_ReturnsFalseForPositiveSecondArgument() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(
                new NumberNode(0.0), new NumberNode(1.0));
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext();
        context.setInfoSet(infoSet);

        Boolean result = (Boolean) coreOperationLessThan.computeValue(context);
        assertFalse(result);
    }

    @Test
    public void testCoreOperationLessThan_FirstArgumentIsZero_ReturnsTrueForNegativeSecondArgument() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(
                new NumberNode(0.0), new NumberNode(-1.0));
        InfoSet infoSet = new InfoSet();
        EvalContext context = new EvalContext();
        context.setInfoSet(infoSet);

        Boolean result = (Boolean) coreOperationLessThan.computeValue(context);
        assertTrue(result);
    }
}