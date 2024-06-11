package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.cli.Utils;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.util.ValueUtils;
import org.junit.Test;
import java.util.Collections;
import java.util.Iterator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class ExpressionTest {

    @Test
    public void testAbstractExpression_isContextDependent_defaultsToFalse() {
        AbstractExpression expression = new AbstractExpression() {
            @Override
            public boolean computeContextDependent() {
                return false;
            }
        };
        assertFalse(expression.isContextDependent());
    }

    @Test
    public void testAbstractExpression_contextDependencyIsKnownAfterFirstCall() {
        AbstractExpression expression = new AbstractExpression() {
            private boolean contextDependent;

            @Override
            public boolean computeContextDependent() {
                if (!this.contextDependent) {
                    this.contextDependent = true;
                }
                return this.contextDependent;
            }
        };
        assertFalse(expression.isContextDependent());
        assertTrue(expression.isContextDependent());
    }

    @Test
    public void testAbstractExpression_contextDependencyIsComputedOnlyOnce() {
        AbstractExpression expression = new AbstractExpression() {
            private boolean contextDependent = false;

            @Override
            public boolean computeContextDependent() {
                if (!this.contextDependent) {
                    this.contextDependent = true;
                }
                return this.contextDependent;
            }
        };
        assertTrue(expression.isContextDependent());
        assertTrue(expression.isContextDependent());
    }

    @Test
    public void testExpression_computeValue_returnsNullForEmptyNodeSet() {
        Expression expression = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return Collections.emptyList();
            }
        };
        assertNull(expression.computeValue(new EvalContext(new NodePointer(new QName(null, "foo"), null, null))));
    }

    @Test
    public void testExpression_computeValue_returnsFirstElementOfNodeSet() {
        Expression expression = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return new EvalContext(new NodePointer(new QName(null, "foo"), null, null), null);
            }
        };
        assertEquals(new EvalContext(new NodePointer(new QName(null, "foo"), null, null), null), expression.computeValue(new EvalContext(new NodePointer(new QName(null, "bar"), null, null))));
    }

    @Test
    public void testExpression_compute_returnsNullForEmptyNodeSet() {
        Expression expression = new Expression() {
            @Override
            public Object compute(EvalContext context) {
                return Collections.emptyList();
            }
        };
        assertNull(expression.compute(new EvalContext(new NodePointer(new QName(null, "foo"), null, null))));
    }

    @Test
    public void testExpression_compute_returnsFirstElementOfNodeSet() {
        Expression expression = new Expression() {
            @Override
            public Object compute(EvalContext context) {
                return new EvalContext(new NodePointer(new QName(null, "foo"), null, null), null);
            }
        };
        assertEquals(new EvalContext(new NodePointer(new QName(null, "foo"), null, null), null), expression.compute(new EvalContext(new NodePointer(new QName(null, "bar"), null, null))));
    }

    @Test
    public void testExpression_iterate_returnsIteratorOverNodeSet() {
        Expression expression = new Expression() {
            @Override
            public Iterator iterate(EvalContext context) {
                return new Iterator() {
                    @Override
                    public boolean hasNext() {
                        return true;
                    }

                    @Override
                    public Object next() {
                        return new NodePointer(new QName(null, "foo"), null, null);
                    }

                    @Override
                    public void remove() {
                        // not supported
                    }
                };
            }
        };
        Iterator iterator = expression.iterate(new EvalContext(new NodePointer(new QName(null, "bar"), null, null)));
        assertTrue(iterator.hasNext());
        assertEquals(new NodePointer(new QName(null, "foo"), null, null), iterator.next());
    }

    @Test
    public void testExpression_iteratePointers_returnsIteratorOverNodePointers() {
        Expression expression = new Expression() {
            @Override
            public Iterator iteratePointers(EvalContext context) {
                return new Iterator() {
                    @Override
                    public boolean hasNext() {
                        return true;
                    }

                    @Override
                    public Object next() {
                        return new NodePointer(new QName(null, "foo"), null, null);
                    }

                    @Override
                    public void remove() {
                        // not supported
                    }
                };
            }
        };
        Iterator iterator = expression.iteratePointers(new EvalContext(new NodePointer(new QName(null, "bar"), null, null)));
        assertTrue(iterator.hasNext());
        assertEquals(new NodePointer(new QName(null, "foo"), null, null), iterator.next());
    }
}

class AbstractExpression {
}