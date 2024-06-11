package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqualarg1NullTest;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqualarg1TypeMismatchTest;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqualarg2NullTest;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqualtrueResultTest;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqualfalseResultTest;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for CoreOperationEqual.
 */
public class CoreOperationEqualTest {

    @Test
    public void testCoreOperationEqual() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new LiteralExpression(1), new LiteralExpression(1));
        assertEquals(Boolean.TRUE, coreOperationEqual.computeValue(new EvalContext()));

        coreOperationEqual = new CoreOperationEqual(new LiteralExpression(1), new LiteralExpression(2));
        assertEquals(Boolean.FALSE, coreOperationEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testCoreOperationEqualarg1NullTest() {
        new CoreOperationEqualarg1NullTest().testNullArg1();
    }

    @Test
    public void testCoreOperationEqualarg1TypeMismatchTest() {
        new CoreOperationEqualarg1TypeMismatchTest().testTypeMismatchArg1();
    }

    @Test
    public void testCoreOperationEqualarg2NullTest() {
        new CoreOperationEqualarg2NullTest().testNullArg2();
    }

    @Test
    public void testCoreOperationEqualtrueResultTest() {
        new CoreOperationEqualtrueResultTest().testTrueResult();
    }

    @Test
    public void testCoreOperationEqualfalseResultTest() {
        new CoreOperationEqualfalseResultTest().testFalseResult();
    }
}