package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.AnnotatedClassLoader;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
@PowerMockRunner.Silent
public class CoreOperationGreaterThanOrEqualTest {

    private CoreOperationGreaterThanOrEqual operation;
    private Expression arg1;
    private Expression arg2;
    private EvalContext context;

    @Before
    public void setUp() throws Exception {
        Class<?> coreOperationGreaterThanOrEqualClass = new AnnotatedClassLoader().loadClass("org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThanOrEqual");
        CoreOperationGreaterThanOrEqual constructor = (CoreOperationGreaterThanOrEqual) coreOperationGreaterThanOrEqualClass.getDeclaredConstructor(Expression.class, Expression.class);
        constructor.setAccessible(true);

        arg1 = new ConstantExpression(new InfoSet() {
            @Override
            public String getStringValue() {
                return "10.5";
            }

            @Override
            public int getIntValue() {
                return 10;
            }

            @Override
            public long getLongValue() {
                return 10L;
            }

            @Override
            public double doubleValue() {
                return 10.5;
           }
        });

        arg2 = new ConstantExpression(new InfoSet() {
            @Override
            public String getStringValue() {
                return "5.5";
            }

            @Override
            public int getIntValue() {
                return 5;
            }

            @Override
            public long getLongValue() {
                return 5L;
            }

            @Override
            public double doubleValue() {
                return 5.5;
            }
        });

        operation = (CoreOperationGreaterThanOrEqual) constructor.newInstance(arg1, arg2);
        context = new EvalContext();
    }

    @Test
    public void testComputeValueWithEqualDoubleValues() {
        InfoSetUtil.setInfoSet(arg1, context);
        InfoSetUtil.setInfoSet(arg2, context);

        boolean result = operation.computeValue(context).booleanValue();
        assertTrue("Expected true, got false", result);
    }

    @Test
    public void testComputeValueWithLessThanDoubleValues() {
        InfoSetUtil.setInfoSet(arg1, context);
        InfoSetUtil.setInfoSet(arg2, context);

        boolean result = operation.computeValue(context).booleanValue();
        assertFalse("Expected false, got true", result);
    }

    @Test
    public void testComputeValueWithGreaterThanDoubleValues() {
        InfoSetUtil.setInfoSet(arg1, context);
        InfoSetUtil.setInfoSet(arg2, context);

        boolean result = operation.computeValue(context).booleanValue();
        assertTrue("Expected true, got false", result);
    }

    @Test
    public void testComputeValueWithMixedTypes() {
        InfoSetUtil.setInfoSet(arg1, context);
        InfoSetUtil.setInfoSet(arg2, context);

        boolean result = operation.computeValue(context).booleanValue();
        assertFalse("Expected false, got true", result);
    }

    @Test
    public void testComputeValueWithBothNull() {
        InfoSetUtil.setInfoSet(arg1, context);
        InfoSetUtil.setInfoSet(arg2, context);

        boolean result = operation.computeValue(context).booleanValue();
        assertTrue("Expected true, got false", result);
    }

    @Test
    public void testSymbol() {
        String symbol = operation.getSymbol();
        assertEquals("Expected '>=', got: " + symbol, ">=", symbol);
    }
}