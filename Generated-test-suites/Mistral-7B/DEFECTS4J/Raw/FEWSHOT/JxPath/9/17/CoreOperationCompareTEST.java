package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.InfoSet;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.compiler.Literal;
import org.apache.commons.jxpath.ri.compiler.Variable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class CoreOperationCompareTest {
    @Test
    public void testCoreOperationCompare() {
        CoreOperationCompare op = new CoreOperationCompare(
            new Literal(new Pointer("1")),
            new Literal(new Pointer("1"))
        );

        assertEquals(2, op.getPrecedence());
        assertTrue(op.isSymmetric());
    }

    @Test
    public void testEqual() {
        EvalContext context = new EvalContext(new InfoSet(), new HashSet<>());
        CoreOperationCompare op = new CoreOperationCompare(
            new Literal(new Pointer("1")),
            new Literal(new Pointer("1"))
        );

        Object left = op.compute(context);
        Object right = op.compute(context);

        assertTrue(op.equal(context, new Literal(new Pointer("1")), new Literal(new Pointer("1"))));
        assertEquals(left, right);
    }

    @Test
    public void testEqual_Boolean() {
        EvalContext context = new EvalContext(new InfoSet(), new HashSet<>());
        CoreOperationCompare op = new CoreOperationCompare(
            new Literal(Boolean.TRUE),
            new Literal(Boolean.TRUE)
        );

        Object left = op.compute(context);
        Object right = op.compute(context);

        assertTrue(op.equal(context, new Literal(Boolean.TRUE), new Literal(Boolean.TRUE)));
        assertTrue(InfoSetUtil.booleanValue(left) == InfoSetUtil.booleanValue(right));
    }

    @Test
    public void testEqual_Double() {
        EvalContext context = new EvalContext(new InfoSet(), new HashSet<>());
        CoreOperationCompare op = new CoreOperationCompare(
            new Literal(Double.doubleValue(1.0)),
            new Literal(Double.doubleValue(1.0))
        );

        Object left = op.compute(context);
        Object right = op.compute(context);

        assertTrue(op.equal(context, new Literal(Double.doubleValue(1.0)), new Literal(Double.doubleValue(1.0))));
        assertEquals(InfoSetUtil.doubleValue(left), InfoSetUtil.doubleValue(right));
    }

    @Test
    public void testEqual_String() {
        EvalContext context = new EvalContext(new InfoSet(), new HashSet<>());
        CoreOperationCompare op = new CoreOperationCompare(
            new Literal(new Pointer("str1")),
            new Literal(new Pointer("str1"))
        );

        Object left = op.compute(context);
        Object right = op.compute(context);

        assertTrue(op.equal(context, new Literal(new Pointer("str1")), new Literal(new Pointer("str1"))));
        assertEquals(InfoSetUtil.stringValue(left), InfoSetUtil.stringValue(right));
    }

    @Test
    public void testEqual_Null() {
        EvalContext context = new EvalContext(new InfoSet(), new HashSet<>());
        CoreOperationCompare op = new CoreOperationCompare(
            new Literal(null),
            new Literal(null)
        );

        Object left = op.compute(context);
        Object right = op.compute(context);

        assertTrue(op.equal(context, new Literal(null), new Literal(null)));
        assertNull(left);
        assertNull(right);
    }

    @Test
    public void testEqual_PointerToNull() {
        EvalContext context = new EvalContext(new InfoSet(), new HashSet<>());
        CoreOperationCompare op = new CoreOperationCompare(
            new Literal(null),
            new Literal(new Pointer(""))
        );

        Object left = op.compute(context);
        Object right = op.compute(context);

        assertFalse(op.equal(context, new Literal(null), new Literal(new Pointer(""))));
        assertNull(left);
        assertNotNull(right);
    }

    @Test
    public void testEqual_PointerToEmptyString() {
        EvalContext context = new EvalContext(new InfoSet(), new HashSet<>());
        CoreOperationCompare op = new CoreOperationCompare(
            new Literal(new Pointer("")),
            new Literal(new Pointer(""))
        );

        Object left = op.compute(context);
        Object right = op.compute(context);

        assertTrue(op.equal(context, new Literal(new Pointer("")), new Literal(new Pointer(""))));
        assertEquals(InfoSetUtil.stringValue(left), InfoSetUtil.stringValue(right));
    }

    @Test
    public void testEqual_IteratorAndObject() {
        EvalContext context = new EvalContext(new InfoSet(), new HashSet<>());
        Iterator it = new ArrayList(1).iterator();
        it.next();

        CoreOperationCompare op = new CoreOperationCompare(new Variable("it"), new Literal(1));

        Object left = op.compute(context);
        Object right = op.compute(context);

        assertFalse(op.equal(context, new Variable("it"), new Literal(1)));
        assertNotNull(left);
        assertNotNull(right);
    }

    @Test
    public void testEqual_ObjectAndIterator() {
        EvalContext context = new EvalContext(new InfoSet(), new HashSet<>());
        Iterator it = new ArrayList(1).iterator();
        it.next();

        CoreOperationCompare op = new CoreOperationCompare(new Literal(1), new Variable("it"));

        Object left = op.compute(context);
        Object right = op.compute(context);

        assertFalse(op.equal(context, new Literal(1), new Variable("it")));
        assertNotNull(left);
        assertNotNull(right);
    }

    @Test
    public void testEqual_CollectionAndCollection() {
        EvalContext context = new EvalContext(new InfoSet(), new HashSet<>());
        Collection collection1 = new ArrayList(1);
        collection1.add(new Pointer("1"));
        Collection collection2 = new ArrayList(1);
        collection2.add(new Pointer("1"));

        CoreOperationCompare op = new CoreOperationCompare(new Variable("collection1"), new Variable("collection2"));

        Object left = op.compute(context);
        Object right = op.compute(context);

        assertTrue(op.equal(context, new Variable("collection1"), new Variable("collection2")));
        assertEquals(left, right);
    }

    @Test
    public void testEqual_CollectionAndObject() {
        EvalContext context = new EvalContext(new InfoSet(), new HashSet<>());
        Collection collection1 = new ArrayList(1);
        collection1.add(new Pointer("1"));

        CoreOperationCompare op = new CoreOperationCompare(new Variable("collection1"), new Literal(new Pointer("1")));

        Object left = op.compute(context);
        Object right = op.compute(context);

        assertTrue(op.equal(context, new Variable("collection1"), new Literal(new Pointer("1"))));
        assertEquals(left, right);
    }

    @Test
    public void testEqual_ObjectAndCollection() {
        EvalContext context = new EvalContext(new InfoSet(), new HashSet<>());
        Collection collection1 = new ArrayList(1);
        collection1.add(new Pointer("1"));

        CoreOperationCompare op = new CoreOperationCompare(new Literal(new Pointer("1")), new Variable("collection1"));

        Object left = op.compute(context);
        Object right = op.compute(context);

        assertTrue(op.equal(context, new Literal(new Pointer("1")), new Variable("collection1")));
        assertEquals(left, right);
    }

    @Test
    public void testContains() {
        EvalContext context = new EvalContext(new InfoSet(), new HashSet<>());
        Iterator it = new ArrayList(1).iterator();
        it.next();

        CoreOperationCompare op = new CoreOperationCompare(new Variable("it"), new Literal(1));

        assertTrue(op.contains(it.iterator(), new Literal(1)));
    }

    @Test
    public void testContains_Null() {
        EvalContext context = new EvalContext(new InfoSet(), new HashSet<>());
        CoreOperationCompare op = new CoreOperationCompare(new Variable("it"), null);

        assertFalse(op.contains(null, new Literal(1)));
    }

    @Test
    public void testContains_NullIterator() {
        EvalContext context = new EvalContext(new InfoSet(), new HashSet<>());
        CoreOperationCompare op = new CoreOperationCompare(new Variable("it"), new Variable("it"));

        assertFalse(op.contains(null, new Variable("it")));
    }

    @Test
    public void testFindMatch() {
        EvalContext context = new EvalContext(new InfoSet(), new HashSet<>());
        Iterator it1 = new ArrayList(2).iterator();
        it1.next();
        it1.next();
        Iterator it2 = new ArrayList(1).iterator();
        it2.next();

        CoreOperationCompare op = new CoreOperationCompare(new Variable("it1"), new Variable("it2"));

        assertTrue(op.findMatch(it1, it2));
    }
}