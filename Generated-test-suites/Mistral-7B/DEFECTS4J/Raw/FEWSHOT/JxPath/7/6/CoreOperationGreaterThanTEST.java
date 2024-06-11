package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.JXPathConstants;
import org.apache.commons.jxpath.ri. JaneValue;

public class CoreOperationGreaterThanTest {

    @Test
    public void testCoreOperationGreaterThan() {
        CoreOperationGreaterThan cog = new CoreOperationGreaterThan(new ConstantExpression(5.0), new ConstantExpression(3.0));
        EvalContext context = new EvalContext(new InfoSet() {
            @Override
            public Object getItem(int index) {
                return JXPathConstants.X_NUMBER;
            }

            @Override
            public double doubleValue(Object value) {
                return ((Number) value).doubleValue();
            }
        });
        assertEquals(Boolean.TRUE, cog.computeValue(context));

        context = new EvalContext(new InfoSet() {
            @Override
            public Object getItem(int index) {
                return JXPathConstants.X_NUMBER;
            }

            @Override
            public double doubleValue(Object value) {
                return ((Number) value).doubleValue();
            }
        });
        ContextHelper.setContextItem(context, 0, new ConstantExpression(3.0));
        ContextHelper.setContextItem(context, 1, new ConstantExpression(5.0));
        assertEquals(Boolean.TRUE, cog.computeValue(context));

        context = new EvalContext(new InfoSet() {
            @Override
            public Object getItem(int index) {
                return JXPathConstants.X_NUMBER;
            }

            @Override
            public double doubleValue(Object value) {
                return ((Number) value).doubleValue();
            }
        });
        ContextHelper.setContextItem(context, 0, new ConstantExpression(3.0));
        ContextHelper.setContextItem(context, 1, new ConstantExpression(5.0));
        assertEquals(Boolean.TRUE, cog.computeValue(context));

        context = new EvalContext(new InfoSet() {
            @Override
            public Object getItem(int index) {
                return JXPathConstants.X_NUMBER;
            }

            @Override
            public double doubleValue(Object value) {
                return ((Number) value).doubleValue();
            }
        });
        ContextHelper.setContextItem(context, 0, new ConstantExpression(3.0));
        ContextHelper.setContextItem(context, 1, new ConstantExpression(2.5));
        assertEquals(Boolean.FALSE, cog.computeValue(context));
    }

    @Test
    public void testCoreOperationGreaterThanWithNaN() {
        CoreOperationGreaterThan cog = new CoreOperationGreaterThan(new ConstantExpression(Double.NaN), new ConstantExpression(Double.NaN));
        EvalContext context = new EvalContext(new InfoSet() {
            @Override
            public Object getItem(int index) {
                return JXPathConstants.X_NUMBER;
            }

            @Override
            public double doubleValue(Object value) {
                return ((Number) value).doubleValue();
            }
        });
        ContextHelper.setContextItem(context, 0, new ConstantExpression(Double.NaN));
        ContextHelper.setContextItem(context, 1, new ConstantExpression(Double.NaN));
        assertEquals(Boolean.UNKNOWN, cog.computeValue(context));
    }

    @Test
    public void testCoreOperationGreaterThanWithNull() {
        CoreOperationGreaterThan cog = new CoreOperationGreaterThan(new ConstantExpression(5.0), new ConstantExpression(null));
        EvalContext context = new EvalContext(new InfoSet() {
            @Override
            public Object getItem(int index) {
                return JXPathConstants.X_NUMBER;
            }

            @Override
            public double doubleValue(Object value) {
                return ((Number) value).doubleValue();
            }
        });
        ContextHelper.setContextItem(context, 0, new ConstantExpression(5.0));
        ContextHelper.setContextItem(context, 1, null);
        assertNull(cog.computeValue(context));

        cog = new CoreOperationGreaterThan(new ConstantExpression(null), new ConstantExpression(5.0));
        context = new EvalContext(new InfoSet() {
            @Override
            public Object getItem(int index) {
                return JXPathConstants.X_NUMBER;
            }

            @Override
            public double doubleValue(Object value) {
                return ((Number) value).doubleValue();
            }
        });
        ContextHelper.setContextItem(context, 0, null);
        ContextHelper.setContextItem(context, 1, new ConstantExpression(5.0));
        assertNull(cog.computeValue(context));
    }

    @Test
    public void testCoreOperationGreaterThanWithDifferentDataTypes() {
        CoreOperationGreaterThan cog = new CoreOperationGreaterThan(new ConstantExpression(5.0), new StringExpression("5"));
        EvalContext context = new EvalContext(new InfoSet() {
            @Override
            public Object getItem(int index) {
                return JXPathConstants.X_NUMBER;
            }

            @Override
            public double doubleValue(Object value) {
                return ((Number) value).doubleValue();
            }
        });
        ContextHelper.setContextItem(context, 0, new ConstantExpression(5.0));
        ContextHelper.setContextItem(context, 1, new StringExpression("5"));
        assertEquals(Boolean.TRUE, cog.computeValue(context));

        cog = new CoreOperationGreaterThan(new StringExpression("5"), new ConstantExpression(5.0));
        context = new EvalContext(new InfoSet() {
            @Override
            public Object getItem(int index) {
                return JXPathConstants.X_NUMBER;
            }

            @Override
            public double doubleValue(Object value) {
                return ((Number) value).doubleValue();
            }
        });
        ContextHelper.setContextItem(context, 0, new StringExpression("5"));
        ContextHelper.setContextItem(context, 1, new ConstantExpression(5.0));
        assertEquals(Boolean.TRUE, cog.computeValue(context));

        cog = new CoreOperationGreaterThan(new ConstantExpression(5.0), new NodeTestExpression("node()"));
        context = new EvalContext(new InfoSet() {
            @Override
            public Object getItem(int index) {
                return JXPathConstants.X_NUMBER;
            }

            @Override
            public double doubleValue(Object value) {
                return ((Number) value).doubleValue();
            }
        });
        ContextHelper.setContextItem(context, 0, new ConstantExpression(5.0));
        ContextHelper.setContextItem(context, 1, new NodeTestExpression("node()"));
        assertNull(cog.computeValue(context));

        cog = new CoreOperationGreaterThan(new NodeTestExpression("node()"), new ConstantExpression(5.0));
        context = new EvalContext(new InfoSet() {
            @Override
            public Object getItem(int index) {
                return JXPathConstants.X_NUMBER;
            }

            @Override
            public double doubleValue(Object value) {
                return ((Number) value).doubleValue();
            }
        });
        ContextHelper.setContextItem(context, 0, new NodeTestExpression("node()"));
        ContextHelper.setContextItem(context, 1, new ConstantExpression(5.0));
        assertNull(cog.computeValue(context));
    }
}