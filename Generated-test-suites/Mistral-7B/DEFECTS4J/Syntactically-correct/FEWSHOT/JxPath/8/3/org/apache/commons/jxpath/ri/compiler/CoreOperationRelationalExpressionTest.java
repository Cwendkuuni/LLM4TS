package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testCoreOperationRelationalExpression() {
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(new Expression[0]) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        assertNotNull(coreOperationRelationalExpression);
    }

    @Test
    public void testComputeValue() {
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(new Expression[] {
            new ConstantExpression(new Integer(1)),
            new ConstantExpression(new Integer(2))
        }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        EvalContext evalContext = new EvalContext();
        assertEquals(Boolean.TRUE, coreOperationRelationalExpression.computeValue(evalContext));

        evalContext = new EvalContext();
        evalContext.setSingleNodePointer(new Integer(1));
        assertEquals(Boolean.TRUE, coreOperationRelationalExpression.computeValue(evalContext));

        evalContext = new EvalContext();
        evalContext.setSingleNodePointer(new Integer(2));
        assertEquals(Boolean.FALSE, coreOperationRelationalExpression.computeValue(evalContext));

        evalContext = new EvalContext();
        evalContext.setSingleNodePointer(new Double(1.5));
        evalContext.setSingleNodePointer(new Double(2.5));
        assertEquals(Boolean.FALSE, coreOperationRelationalExpression.computeValue(evalContext));

        evalContext = new EvalContext();
        evalContext.setSingleNodePointer(new Double(1.5));
        evalContext.setSingleNodePointer(new Double(1.5));
        assertEquals(Boolean.TRUE, coreOperationRelationalExpression.computeValue(evalContext));
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(new Expression[0]) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        assertEquals(3, coreOperationRelationalExpression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(new Expression[0]) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        assertFalse(coreOperationRelationalExpression.isSymmetric());
    }

    @Test
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(new Expression[0]) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        assertEquals(1, coreOperationRelationalExpression.evaluateCompare(1));
        assertEquals(-1, coreOperationRelationalExpression.evaluateCompare(-1));
        assertEquals(0, coreOperationRelationalExpression.evaluateCompare(0));
    }

    @Test
    public void testCompute() {
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(new Expression[] {
            new ConstantExpression(new Integer(1)),
            new ConstantExpression(new Integer(2))
        }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        Object left = new Integer(1);
        Object right = new Integer(2);

        assertFalse(coreOperationRelationalExpression.compute(left, right));

        left = new Integer(2);
        right = new Integer(1);

        assertFalse(coreOperationRelationalExpression.compute(left, right));

        left = new Double(1.5);
        right = new Double(2.5);

        assertFalse(coreOperationRelationalExpression.compute(left, right));

        left = new Double(1.5);
        right = new Double(1.5);

        assertTrue(coreOperationRelationalExpression.compute(left, right));

        left = new InitialContext();
        right = new InitialContext();

        assertFalse(coreOperationRelationalExpression.compute(left, right));

        left = new Iterator() {
            private int count = 0;

            @Override
            public boolean hasNext() {
                return count++ < 2;
            }

            @Override
            public Object next() {
                return new Integer(1);
            }
        };

        right = new Iterator() {
            private int count = 0;

            @Override
            public boolean hasNext() {
                return count++ < 2;
            }

            @Override
            public Object next() {
                return new Integer(2);
            }
        };

        assertFalse(coreOperationRelationalExpression.compute(left, right));

        left = new Iterator() {
            private List list = new ArrayList();

            {
                list.add(new Integer(1));
                list.add(new Integer(2));
            }

            @Override
            public boolean hasNext() {
                return list.iterator().hasNext();
            }

            @Override
            public Object next() {
                return list.iterator().next();
            }
        };

        right = new Integer(1);

        assertFalse(coreOperationRelationalExpression.compute(left, right));

        left = new Integer(1);
        right = new Iterator() {
            private List list = new ArrayList();

            {
                list.add(new Integer(1));
                list.add(new Integer(2));
            }

            @Override
            public boolean hasNext() {
                return list.iterator().hasNext();
            }

            @Override
            public Object next() {
                return list.iterator().next();
            }
        };

        assertFalse(coreOperationRelationalExpression.compute(left, right));

        left = new Iterator() {
            private List list = new ArrayList();

            {
                list.add(new Integer(1));
                list.add(new Integer(1));
            }

            @Override
            public boolean hasNext() {
                return list.iterator().hasNext();
            }

            @Override
            public Object next() {
                return list.iterator().next();
            }
        };

        right = new Iterator() {
            private List list = new ArrayList();

            {
                list.add(new Integer(1));
                list.add(new Integer(1));
            }

            @Override
            public boolean hasNext() {
                return list.iterator().hasNext();
            }

            @Override
            public Object next() {
                return list.iterator().next();
            }
        };

        assertTrue(coreOperationRelationalExpression.compute(left, right));

        left = new Iterator() {
            private List list = new ArrayList();

            {
                list.add(new Integer(1));
                list.add(new Integer(2));
            }

            @Override
            public boolean hasNext() {
                return list.iterator().hasNext();
            }

            @Override
            public Object next() {
                return list.iterator().next();
            }
        };

        right = new HashSet() {
            {
                add(new Integer(1));
                add(new Integer(1));
            }
        };

        assertTrue(coreOperationRelationalExpression.compute(left, right));
    }

    @Test
    public void testReduce() {
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(new Expression[0]) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        Object o = new Integer(1);

        assertEquals(o, coreOperationRelationalExpression.reduce(o));

        o = new SelfContext();
        assertEquals(o, coreOperationRelationalExpression.reduce(o));

        o = new Collection() {
            @Override
            public boolean iterator() {
                return true;
            }

            @Override
            public void remove() {
                // not implemented
            }

            @Override
            public int size() {
                return 0;
            }
        };
        assertEquals(o.iterator(), coreOperationRelationalExpression.reduce(o));
    }

    @Test
    public void testContainsMatch() {
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(new Expression[0]) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        Iterator it = new Iterator() {
            private int count = 0;

            @Override
            public boolean hasNext() {
                return count++ < 2;
            }

            @Override
            public Object next() {
                return new Integer(1);
            }
        };

        Object value = new Integer(1);

        assertFalse(coreOperationRelationalExpression.containsMatch(it, value));

        it = new Iterator() {
            private int count = 0;

            @Override
            public boolean hasNext() {
                return count++ < 2;
            }

            @Override
            public Object next() {
                return new Integer(2);
            }
        };

        assertFalse(coreOperationRelationalExpression.containsMatch(it, value));

        it = new Iterator() {
            private List list = new ArrayList();

            {
                list.add(new Integer(1));
                list.add(new Integer(1));
            }

            @Override
            public boolean hasNext() {
                return list.iterator().hasNext();
            }

            @Override
            public Object next() {
                return list.iterator().next();
            }
        };

        assertTrue(coreOperationRelationalExpression.containsMatch(it, value));

        it = new Iterator() {
            private List list = new ArrayList();

            {
                list.add(new Integer(1));
                list.add(new Integer(2));
            }

            @Override
            public boolean hasNext() {
                return list.iterator().hasNext();
            }

            @Override
            public Object next() {
                return list.iterator().next();
            }
        };

        assertFalse(coreOperationRelationalExpression.containsMatch(it, value));
    }

    @Test
    public void testFindMatch() {
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(new Expression[0]) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        Iterator lit = new Iterator() {
            private List list = new ArrayList();

            {
                list.add(new Integer(1));
                list.add(new Integer(2));
            }

            @Override
            public boolean hasNext() {
                return list.iterator().hasNext();
            }

            @Override
            public Object next() {
                return list.iterator().next();
            }
        };

        Iterator rit = new Iterator() {
            private List list = new ArrayList();

            {
                list.add(new Integer(1));
                list.add(new Integer(1));
            }

            @Override
            public boolean hasNext() {
                return list.iterator().hasNext();
            }

            @Override
            public Object next() {
                return list.iterator().next();
            }
        };

        HashSet result = new HashSet();

        coreOperationRelationalExpression.findMatch(lit, rit, result);

        assertEquals(1, result.size());
        assertTrue(result.contains(new Integer(1)));

        lit = new Iterator() {
            private List list = new ArrayList();

            {
                list.add(new Integer(1));
                list.add(new Integer(2));
            }

            @Override
            public boolean hasNext() {
                return list.iterator().hasNext();
            }

            @Override
            public Object next() {
                return list.iterator().next();
            }
        };

        rit = new Iterator() {
            private List list = new ArrayList();

            {
                list.add(new Integer(2));
                list.add(new Integer(1));
            }

            @Override
            public boolean hasNext() {
                return list.iterator().hasNext();
            }

            @Override
            public Object next() {
                return list.iterator().next();
            }
        };

        result = new HashSet();

        coreOperationRelationalExpression.findMatch(lit, rit, result);

        assertEquals(0, result.size());

        lit = new Iterator() {
            private List list = new ArrayList();

            {
                list.add(new Integer(1));
                list.add(new Integer(2));
            }

            @Override
            public boolean hasNext() {
                return list.iterator().hasNext();
            }

            @Override
            public Object next() {
                return list.iterator().next();
            }
        };

        rit = new Iterator() {
            private List list = new ArrayList();

            {
                list.add(new Integer(1));
                list.add(new Integer(1));
            }

            @Override
            public boolean hasNext() {
                return list.iterator().hasNext();
            }

            @Override
            public Object next() {
                return list.iterator().next();
            }
        };

        result = new HashSet();

        coreOperationRelationalExpression.findMatch(lit, rit, result);

        assertEquals(1, result.size());
        assertTrue(result.contains(new Integer(1)));
    }

}