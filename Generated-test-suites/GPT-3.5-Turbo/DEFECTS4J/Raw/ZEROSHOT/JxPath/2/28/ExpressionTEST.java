package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.util.ValueUtils;

import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExpressionTest {

    private Expression expression;

    @Before
    public void setUp() {
        expression = new Expression() {
            @Override
            public boolean computeContextDependent() {
                return false;
            }

            @Override
            public Object computeValue(EvalContext context) {
                return null;
            }

            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        };
    }

    @Test
    public void testIsContextDependent() {
        assertFalse(expression.isContextDependent());
    }

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        assertNull(expression.computeValue(context));
    }

    @Test
    public void testCompute() {
        EvalContext context = new EvalContext();
        assertNull(expression.compute(context));
    }

    @Test
    public void testIterate() {
        EvalContext context = new EvalContext();
        Iterator iterator = expression.iterate(context);
        assertNotNull(iterator);
    }

    @Test
    public void testIteratePointers() {
        EvalContext context = new EvalContext();
        Iterator iterator = expression.iteratePointers(context);
        assertNotNull(iterator);
    }

    @Test
    public void testPointerIterator() {
        Iterator iterator = expression.new PointerIterator(Collections.emptyList().iterator(), new QName(null, "value"), Locale.ENGLISH);
        assertNotNull(iterator);
    }

    @Test
    public void testValueIterator() {
        Iterator iterator = expression.new ValueIterator(Collections.emptyList().iterator());
        assertNotNull(iterator);
    }
}