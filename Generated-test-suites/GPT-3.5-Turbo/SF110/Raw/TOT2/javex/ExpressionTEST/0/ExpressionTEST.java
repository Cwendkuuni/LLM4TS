package org.javex;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class ExpressionTest {

    @Test
    public void testGetExternalFunctions() {
        Expression expr = new Expression("testing");
        List<String> externalFunctions = new ArrayList<>();
        externalFunctions.add("function1");
        externalFunctions.add("function2");
        externalFunctions.add("function3");
        expr.externalFunctions = externalFunctions;

        assertEquals(externalFunctions, expr.getExternalFunctions());
    }

    @Test
    public void testPrepare() {
        Expression expr = new Expression("5 + 3");
        expr.prepare();

        assertNotNull(expr.stack);
        assertTrue(expr.prepared);
    }

    @Test
    public void testEvaluate() {
        //Test for successful evaluation
        Expression expr = new Expression("5 + 3");
        expr.prepare();

        try {
            assertEquals(8, expr.evaluate());
        } catch (Exception e) {
            fail("Evaluation should not throw an exception");
        }

        //Test for exception handling
        Expression expr2 = new Expression("5 +");
        expr2.prepare();

        try {
            expr2.evaluate();
            fail("Evaluation should throw an exception for invalid expression");
        } catch (Exception e) {
            //Expected exception
        }
    }

    //Add more test cases for other methods if needed

}