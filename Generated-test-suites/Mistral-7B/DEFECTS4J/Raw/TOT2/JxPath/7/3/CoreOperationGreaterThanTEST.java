package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {

    @Test
    public void testingMethodForMethod1() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(new Constant(1.0), new Constant(0.5));
        InfoSet infoSet = new StaticInfoSet();
        EvalContext context = new EvalContext(infoSet);
        Boolean result = (Boolean) coreOperationGreaterThan.computeValue(context);
        assertTrue(result);
    }

    @Test
    public void testingMethodForMethod2() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(new Constant(0.5), new Constant(1.0));
        InfoSet infoSet = new StaticInfoSet();
        EvalContext context = new EvalContext(infoSet);
        Boolean result = (Boolean) coreOperationGreaterThan.computeValue(context);
        assertFalse(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testingMethodForMethod3() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(new Constant(1.0), new Constant(1.0));
        InfoSet infoSet = new StaticInfoSet();
        EvalContext context = new EvalContext(infoSet);
    }

    @Test(expected = ClassCastException.class)
    public void testingMethodForMethod4() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(new Constant("1.0"), new Constant(1.0));
        InfoSet infoSet = new StaticInfoSet();
        EvalContext context = new EvalContext(infoSet);
    }

    @Test
    public void testingMethodForMethod5() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(new Constant.DoubleNegativeInfinity(), new Constant.DoublePositiveInfinity());
        InfoSet infoSet = new StaticInfoSet();
        EvalContext context = new EvalContext(infoSet);
        Boolean result = (Boolean) coreOperationGreaterThan.computeValue(context);
        assertTrue(result);
    }

    @Test
    public void testingMethodForMethod6() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(new Constant.DoublePositiveInfinity(), new Constant.DoubleNegativeInfinity());
        InfoSet infoSet = new StaticInfoSet();
        EvalContext context = new EvalContext(infoSet);
        Boolean result = (Boolean) coreOperationGreaterThan.computeValue(context);
        assertFalse(result);
    }

}