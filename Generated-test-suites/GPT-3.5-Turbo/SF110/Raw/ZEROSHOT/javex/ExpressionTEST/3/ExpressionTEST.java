package org.javex;

import org.javex.utils.*;
import org.javex.functions.logical.*;
import org.javex.functions.math.*;
import org.javex.functions.date.*;
import org.javex.functions.string.*;
import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

public class ExpressionTest {

    @Test
    public void testGetExternalFunctions() {
        Expression expr = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };

        List<FunctionExpression> result = expr.getExternalFunctions();

        assertNotNull(result);
    }

    @Test
    public void testExpressionConstructor() {
        Expression expr = new Expression("1 + 1");

        assertNotNull(expr);
    }

    @Test
    public void testPrepare() {
        Expression expr = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };

        expr.prepare();

        assertTrue(expr.isPrepared());
    }

    @Test
    public void testGetStackCopy() {
        Expression expr = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };

        ExpressionStack result = expr.getStackCopy();

        assertNotNull(result);
    }

    @Test
    public void testEvaluate() throws Exception {
        Expression expr = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };

        Object result = expr.evaluate();

        assertNull(result);
    }

    @Test
    public void testIsReservedFunction() {
        Expression expr = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };

        boolean result = expr.isReservedFunction("and");

        assertTrue(result);
    }

    @Test
    public void testIsLogicalFunction() {
        Expression expr = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };

        boolean result = expr.isLogicalFunction("and");

        assertTrue(result);
    }

    @Test
    public void testIsMathFunction() {
        Expression expr = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };

        boolean result = expr.isMathFunction("abs");

        assertTrue(result);
    }

    @Test
    public void testIsDateFunction() {
        Expression expr = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };

        boolean result = expr.isDateFunction("dateadd");

        assertTrue(result);
    }

    @Test
    public void testIsStringFunction() {
        Expression expr = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };

        boolean result = expr.isStringFunction("length");

        assertTrue(result);
    }

    @Test
    public void testGetExternalFunction() {
        Expression expr = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };

        FunctionExpression result = expr.getExternalFunction("extFn");

        assertNull(result);
    }

    @Test
    public void testIsExternalFunction() {
        Expression expr = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };

        boolean result = expr.isExternalFunction("extFn");

        assertFalse(result);
    }

    @Test
    public void testGetFunction() {
        Expression expr = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };

        FunctionExpression result = expr.getFunction("and");

        assertNotNull(result);
    }

    @Test
    public void testIsFunction() {
        Expression expr = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };

        boolean result = expr.isFunction("and");

        assertTrue(result);
    }

    @Test
    public void testExpressionDefaultConstructor() {
        Expression expr = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };

        assertNotNull(expr);
    }

    @Test
    public void testPrepareMethod() {
        Expression expr = new Expression() {
            @Override
            public Object evaluate() throws Exception {
                return null;
            }
        };

        expr.prepare();

        assertTrue(expr.isPrepared());
    }
}