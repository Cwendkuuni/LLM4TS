package org.junit.tests.coreoperationlessthan;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThan;
import org.junit.Test;

public class CoreOperationLessThanTest {
    @Test
    public void testConstructor() {
        // Test constructor
        ConstructorTest.testCoreOperationLessThan();
    }

    @Test
    public void testComputeValue() {
        // Test computeValue method
        ComputeValueTest.testComputeValue();
    }
}