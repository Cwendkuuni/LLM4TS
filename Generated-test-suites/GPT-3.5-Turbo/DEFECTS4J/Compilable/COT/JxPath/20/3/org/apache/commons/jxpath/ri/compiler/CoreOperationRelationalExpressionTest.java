package org.apache.commons.jxpath.ri.compiler;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class CoreOperationRelationalExpressionTest {

    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(CoreOperationRelationalExpressionTest.class);

        if (result.wasSuccessful()) {
            System.out.println("All tests passed!");
        } else {
            System.out.println("Tests failed:");
            for (Failure failure : result.getFailures()) {
                System.out.println(failure.toString());
            }
        }
    }
}
