package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.util.ValueUtils;
import org.junit.Test;

import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

public class ExpressionTest {

    @Test
    public void testAbstractExpression() {
        Expression expression = new AbstractExpression() {
            @Override
            public boolean computeContextDependent() {
                return false;
            }

            @Override
            public Object computeValue(EvalContext context) {
                return ZERO;
            }

            @Override
            public Object compute(EvalContext context) {
                return ZERO;
            }

            @Override
            public Iterator iterate(EvalContext context) {
                return ValueUtils.iterate(ZERO);
            }

            @Override
            public Iterator iteratePointers(EvalContext context) {
                return Collections.EMPTY_LIST.iterator();
            }
        };

        assertTrue(expression.isContextDependent());
        assertTrue(expression.computeValue(new EvalContext(new NodePointer(null, null, null, null), new QName(null, null, null), null)) instanceof Double);
        assertTrue(expression.compute(new EvalContext(new NodePointer(null, null, null, null), new QName(null, null, null), null)) instanceof Double);
        assertFalse(expression.iterate(new EvalContext(new NodePointer(null, null, null, null), new QName(null, null, null), null)).hasNext());
        assertFalse(expression.iteratePointers(new EvalContext(new NodePointer(null, null, null, null), new QName(null, null, null), null)).hasNext());
    }

    @Test
    public void testContextDependencyForSubclass() {
        Expression expression = new MyExpression() {
            @Override
            public boolean computeContextDependent() {
                return true;
            }
        };

        assertTrue(expression.isContextDependent());
    }

    @Test
    public void testContextDependencyForSubclassThatReturnsFalse() {
        Expression expression = new MyExpression() {
            @Override
            public boolean computeContextDependent() {
                return false;
            }
        };

        assertFalse(expression.isContextDependent());
    }
}

class AbstractExpression {
    // The same code as in the original class
}

class MyExpression extends AbstractExpression {
    // Add any custom code here
}