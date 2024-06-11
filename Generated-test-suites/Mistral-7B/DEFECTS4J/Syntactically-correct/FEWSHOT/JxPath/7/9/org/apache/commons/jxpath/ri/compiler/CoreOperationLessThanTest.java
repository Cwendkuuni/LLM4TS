package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.eval.ConstantEval;
import org.apache.commons.jxpath.ri.eval.EvalContext;
import org.apache.commons.jxpath.ri.eval.NumberEval;
import org.junit.Test;

public class CoreOperationLessThanTest {

    @Test
    public void testCoreOperationLessThan() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(new ConstantEval(1.5), new ConstantEval(2.0));
        EvalContext context = new EvalContext(new InfoSet(), null);
        assertTrue(coreOperationLessThan.computeValue(context).booleanValue());

        coreOperationLessThan = new CoreOperationLessThan(new ConstantEval(1.5), new ConstantEval(1.5));
        assertFalse(coreOperationLessThan.computeValue(context).booleanValue());

        coreOperationLessThan = new CoreOperationLessThan(new ConstantEval(1.5), new ConstantEval(1.4));
        assertTrue(coreOperationLessThan.computeValue(context).booleanValue());

        coreOperationLessThan = new CoreOperationLessThan(new NumberEval(1.5), new NumberEval(2.0));
        assertTrue(coreOperationLessThan.computeValue(context).booleanValue());
    }

    @Test
    public void testCoreOperationLessThanWithNegativeNumbers() {
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(new ConstantEval(-1.5), new ConstantEval(-2.0));
        EvalContext context = new EvalContext(new InfoSet(), null);
        assertTrue(coreOperationLessThan.computeValue(context).booleanValue());

        coreOperationLessThan = new CoreOperationLessThan(new ConstantEval(-1.5), new ConstantEval(-1.5));
        assertFalse(coreOperationLessThan.computeValue(context).booleanValue());

        coreOperationLessThan = new CoreOperationLessThan(new ConstantEval(-1.5), new ConstantEval(-1.6));
        assertTrue(coreOperationLessThan.computeValue(context).booleanValue());

        coreOperationLessThan = new CoreOperationLessThan(new NumberEval(-1.5), new NumberEval(-2.0));
        assertTrue(coreOperationLessThan.computeValue(context).booleanValue());
    }
}