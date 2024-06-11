package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpressionarg0;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpressionarg1;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testCoreOperationRelationalExpression() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperationRelationalExpressionarg0(), new CoreOperationRelationalExpressionarg1());
        assertEquals(3, expression.getPrecedence());
        assertFalse(expression.isSymmetric());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationRelationalExpressionNullArguments() {
        new CoreOperationRelationalExpression(null, new CoreOperationRelationalExpressionarg1());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationRelationalExpressionNullArg1() {
        new CoreOperationRelationalExpression(new CoreOperationRelationalExpressionarg0(), null);
    }
}

@Test
public class CoreOperationRelationalExpressionGtTest {
    CoreOperationRelationalExpression expression;

    @Before
    public void setUp() {
        expression = new CoreOperationGt(new CoreOperationRelationalExpressionarg0(), new CoreOperationRelationalExpressionarg1());
    }

    @Test
    public void testCoreOperationGt() {
        assertEquals(3, expression.getPrecedence());
        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testCoreOperationGtTrue() {
        Expression arg0 = new CoreOperationRelationalExpressionarg0();
        Expression arg1 = new CoreOperationRelationalExpressionarg1();
        assertTrue(expression.evaluate(arg0, arg1));
    }

    @Test
    public void testCoreOperationGtFalse() {
        Expression arg0 = new CoreOperationRelationalExpressionarg1();
        Expression arg1 = new CoreOperationRelationalExpressionarg0();
        assertFalse(expression.evaluate(arg0, arg1));
    }

    @Test
    public void testCoreOperationGtSameValue() {
        Expression arg0 = new CoreOperationRelationalExpressionarg0();
        Expression arg1 = arg0;
        assertFalse(expression.evaluate(arg0, arg1));
    }
}

@Test
public class CoreOperationRelationalExpressionGtEqTest {
    CoreOperationRelationalExpression expression;

    @Before
    public void setUp() {
        expression = new CoreOperationGtEq(new CoreOperationRelationalExpressionarg0(), new CoreOperationRelationalExpressionarg1());
    }

    @Test
    public void testCoreOperationGtEq() {
        assertEquals(3, expression.getPrecedence());
        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testCoreOperationGtEqTrue() {
        Expression arg0 = new CoreOperationRelationalExpressionarg0();
        Expression arg1 = new CoreOperationRelationalExpressionarg1();
        assertTrue(expression.evaluate(arg0, arg1));
        assertTrue(expression.evaluate(arg1, arg0));
    }

    @Test
    public void testCoreOperationGtEqFalse() {
        Expression arg0 = new CoreOperationRelationalExpressionarg1();
        Expression arg1 = new CoreOperationRelationalExpressionarg0();
        assertFalse(expression.evaluate(arg0, arg1));
        assertFalse(expression.evaluate(arg1, arg0));
    }

    @Test
    public void testCoreOperationGtEqSameValue() {
        Expression arg0 = new CoreOperationRelationalExpressionarg0();
        Expression arg1 = arg0;
        assertTrue(expression.evaluate(arg0, arg1));
    }
}

@Test
public class CoreOperationRelationalExpressionLtTest {
    CoreOperationRelationalExpression expression;

    @Before
    public void setUp() {
        expression = new CoreOperationLt(new CoreOperationRelationalExpressionarg0(), new CoreOperationRelationalExpressionarg1());
    }

    @Test
    public void testCoreOperationLt() {
        assertEquals(3, expression.getPrecedence());
        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testCoreOperationLtTrue() {
        Expression arg0 = new CoreOperationRelationalExpressionarg0();
        Expression arg1 = new CoreOperationRelationalExpressionarg1();
        assertFalse(expression.evaluate(arg0, arg1));
        assertTrue(expression.evaluate(arg1, arg0));
    }

    @Test
    public void testCoreOperationLtFalse() {
        Expression arg0 = new CoreOperationRelationalExpressionarg1();
        Expression arg1 = new CoreOperationRelationalExpressionarg0();
        assertFalse(expression.evaluate(arg0, arg1));
        assertFalse(expression.evaluate(arg1, arg0));
    }

    @Test
    public void testCoreOperationLtSameValue() {
        Expression arg0 = new CoreOperationRelationalExpressionarg0();
        Expression arg1 = arg0;
        assertFalse(expression.evaluate(arg0, arg1));
    }
}

@Test
public class CoreOperationRelationalExpressionLtEqTest {
    CoreOperationRelationalExpression expression;

    @Before
    public void setUp() {
        expression = new CoreOperationLtEq(new CoreOperationRelationalExpressionarg0(), new CoreOperationRelationalExpressionarg1());
    }

    @Test
    public void testCoreOperationLtEq() {
        assertEquals(3, expression.getPrecedence());
        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testCoreOperationLtEqTrue() {
        Expression arg0 = new CoreOperationRelationalExpressionarg0();
        Expression arg1 = new CoreOperationRelationalExpressionarg1();
        assertTrue(expression.evaluate(arg0, arg1));
        assertTrue(expression.evaluate(arg1, arg0));
    }

    @Test
    public void testCoreOperationLtEqFalse() {
        Expression arg0 = new CoreOperationRelationalExpressionarg1();
        Expression arg1 = new CoreOperationRelationalExpressionarg0();
        assertFalse(expression.evaluate(arg0, arg1));
        assertFalse(expression.evaluate(arg1, arg0));
    }

    @Test
    public void testCoreOperationLtEqSameValue() {
        Expression arg0 = new CoreOperationRelationalExpressionarg0();
        Expression arg1 = arg0;
        assertTrue(expression.evaluate(arg0, arg1));
    }
}