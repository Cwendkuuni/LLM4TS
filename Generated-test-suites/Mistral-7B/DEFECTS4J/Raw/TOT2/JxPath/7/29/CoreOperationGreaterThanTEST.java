package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThan;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CoreOperationGreaterThanTest {

    @Test
    public void testCoreOperationGreaterThanBasic() {
        // Expert 1
    }

    @Test
    public void testCoreOperationGreaterThanEdgeCase() {
        // Expert 2
    }

    @Test
    public void testCoreOperationGreaterThanNegativeEdgeCase() {
        // Expert 2
    }

    @Test(expected = RuntimeException.class)
    public void testCoreOperationGreaterThanTypeMismatchException() {
        // Expert 2
    }

    @Test(expected = RuntimeException.class)
    public void testCoreOperationGreaterThanNullException() {
        // Expert 3
    }

    @Test(expected = RuntimeException.class)
    public void testCoreOperationGreaterThanNaNException() {
        // Expert 3
    }
}