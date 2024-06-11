package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.cli.Utils;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.util.ValueUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Collections;
import java.util.Iterator;

public class ExpressionTest {

    private EvalContext context;

    @Before
    public void setUp() {
        context = new EvalContext();
    }

    @Test
    public void testIsContextDependentDefault() {
        AbstractExpression exp = new AbstractExpression() {
            @Override
            public boolean computeContextDependent() {
                return false;
            }
        };
        assertTrue(exp.isContextDependent());
    }

    @Test
    public void testIsContextDependentFalse() {
        AbstractExpression exp = new AbstractExpression() {
            @Override
            public boolean computeContextDependent() {
                return false;
            }
        };
        assertFalse(exp.isContextDependent());
    }

    @Test
    public void testIsContextDependentTrue() {
        AbstractExpression exp = new AbstractExpression() {
            @Override
            public boolean computeContextDependent() {
                return true;
            }
        };
        assertTrue(exp.isContextDependent());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testComputeValueNullContext() {
        AbstractExpression exp = new AbstractExpression() {
            @Override
            public Object computeValue(EvalContext context) {
                return null;
            }
        };
        exp.computeValue(null);
    }

    @Test
    public void testComputeValue() {
        AbstractExpression exp = new AbstractExpression() {
            @Override
            public Object computeValue(EvalContext context) {
                return "result";
            }
        };
        assertEquals("result", exp.computeValue(context));
    }

    @Test
    public void testComputeValueIterable() {
        IterableExpression exp = new IterableExpression() {
            @Override
            public Iterator iterate(EvalContext context) {
                return Collections.singletonList("result").iterator();
            }

            @Override
            public Object compute(EvalContext context) {
                return "result";
            }
        };
        assertEquals("result", exp.computeValue(context));
        Iterator it = exp.iterate(context);
        assertTrue(it.hasNext());
        assertEquals("result", it.next());
    }

    @Test
    public void testComputeValueIterator() {
        IteratorExpression exp = new IteratorExpression() {
            @Override
            public Iterator iterate(EvalContext context) {
                return ValueUtils.iterate("result");
            }

            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        };
        Iterator it = exp.iterate(context);
        assertTrue(it.hasNext());
        assertEquals("result", it.next().toString());
        it.remove(); //unsupported operation
    }

    @Test
    public void testIterate() {
        AbstractExpression exp = new AbstractExpression() {
            @Override
            public Object computeValue(EvalContext context) {
                return new EvalContext();
            }

            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        };
        Iterator it = exp.iterate(context);
        assertNull(it.next());

        Iterator it2 = exp.iteratePointers(context);
        assertNull(it2.next());
    }

    @Test
    public void testIteratePointers() {
        AbstractExpression exp = new AbstractExpression() {
            @Override
            public Object computeValue(EvalContext context) {
                return new NodePointer(new QName("foo"), "value", Locale.ENGLISH);
            }

            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        };
        Iterator it = exp.iteratePointers(context);
        Pointer pointer = (Pointer) it.next();
        assertEquals("foo", pointer.getQName().getLocalPart());
        assertEquals("value", pointer.getValue());
    }
}

class AbstractExpression {
}

class IterableExpression extends Expression {
    @Override
    public Iterator iterate(EvalContext context) {
        return null;
    }

    @Override
    public Object compute(EvalContext context) {
        return null;
    }
}

class IteratorExpression extends Expression {
    @Override
    public Iterator iterate(EvalContext context) {
        return null;
    }

    @Override
    public Object compute(EvalContext context) {
        return null;
    }
}