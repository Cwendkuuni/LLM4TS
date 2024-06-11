package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.iter.IteratorSupport;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationRelationalExpressionTest {

    private CoreOperationRelationalExpression expression;
    private EvalContext context;
    private InfoSet infoSet;

    @Before
    public void setup() {
        expression = new CoreOperationRelationalExpression(new CoreConstantExpression(5)) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        context = new EvalContext(new SelfContext(), new InitialContext());
        infoSet = new InfoSet() {
            @Override
            public Iterator iterator() {
                return new IteratorSupport(new ArrayList() {{
                    add(1.0);
                    add(2.0);
                    add(3.0);
                }}.iterator());
            }

            @Override
            public Object getSingleNodePointer() {
                return 1.0;
            }

            @Override
            public Collection values() {
                return new ArrayList() {{
                    add(1.0);
                    add(2.0);
                    add(3.0);
                }};
            }
        };
    }

    @Test
    public void testComputeValue() {
        assertTrue(expression.computeValue(context));
    }

    @Test
    public void testReduce() {
        Object left = new SelfContext();
        Object right = new ArrayList() {{
            add(1.0);
            add(2.0);
        }};

        left = expression.reduce(left);
        right = expression.reduce(right);

        assertTrue(left instanceof Double);
        assertTrue(right instanceof Iterator);

        assertEquals(1.0, left.doubleValue());
        assertTrue(((Iterator) right).hasNext());
    }

    @Test
    public void testContainsMatch() {
        Iterator iterator = new ArrayList() {{
            add(1.0);
            add(2.0);
            add(3.0);
        }}.iterator();

        assertTrue(expression.containsMatch(iterator, 2.0));
        assertFalse(expression.containsMatch(iterator, 4.0));
    }

    @Test
    public void testFindMatch() {
        Iterator lIter = new ArrayList() {{
            add(1.0);
            add(2.0);
        }}.iterator();
        Iterator rIter = new ArrayList() {{
            add(1.0);
            add(2.0);
            add(3.0);
        }}.iterator();

        assertTrue(expression.findMatch(lIter, rIter));
        assertFalse(expression.findMatch(lIter, new ArrayList() {{
            add(5.0);
        }}.iterator()));
    }
}