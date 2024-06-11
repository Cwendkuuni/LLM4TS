package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationCompareTest {

    private CoreOperationCompare operation;

    @Before
    public void setUp() {
        operation = new CoreOperationCompare(null, null) {
            @Override
            protected boolean equal(
                EvalContext context,
                Expression left,
                Expression right) 
            {
                return false;
            }
        };
    }

    @Test
    public void testGetPrecedence() {
        assertEquals(2, operation.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        assertTrue(operation.isSymmetric());
    }

    @Test
    public void testEqual() {
        // Implement your test cases for the equal() method here
    }
}