package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testAllTestCases() {
        CoreOperationRelationalExpressionTest1 test1 = new CoreOperationRelationalExpressionTest1();
        CoreOperationRelationalExpressionTest2 test2 = new CoreOperationRelationalExpressionTest2();
        CoreOperationRelationalExpressionTest3 test3 = new CoreOperationRelationalExpressionTest3();

        test1.runTestCases();
        test2.runTestCases();
        test3.runTestCases();
    }

    private void runTestCases() {
        for (Method method : this.getClass().getMethods()) {
            if (method.isAnnotationPresent(Test.class)) {
                try {
                    method.invoke(this);
                } catch (Exception e) {
                    fail("Test case failed with unexpected error: " + e.getMessage());
                }
            }
        }
    }
}