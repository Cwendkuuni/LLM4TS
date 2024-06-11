package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * Test class for CoreOperationCompare.
 */
public class CoreOperationCompareTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        // Given
        Expression arg1 = new Expression(null);
        Expression arg2 = new Expression(null);
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);

        // Then
        assertInstanceOf(CoreOperationCompare.class, coreOperationCompare);
        assertEquals(new Expression[] { arg1, arg2 }, coreOperationCompare.getExpressions());
    }

    /**
     * Test equals method.
     */
    @Test
    public void testEquals() {
        // Given
        CoreOperationCompare coreOperationCompare1 = new CoreOperationCompare(null, null);
        CoreOperationCompare coreOperationCompare2 = new CoreOperationCompare(null, null);

        // When
        boolean result = coreOperationCompare1.equal(
            new EvalContext(),
            new Expression(null),
            new Expression(null)
        );

        // Then
        assertTrue(result);
        result = coreOperationCompare1.equal(coreOperationCompare2, new EvalContext(), new Expression(null), new Expression(null));
        assertTrue(result);

        // Given different objects
        Object l = new Object();
        Object r = new Object();
        result = coreOperationCompare1.equal(l, r);
        assertFalse(result);

        // Given null pointers
        Pointer l1 = null;
        Pointer r1 = null;
        result = coreOperationCompare1.equal(l1, r1);
        assertTrue(result);

        // Given non-null pointers with the same value
        Pointer l2 = new Pointer(new Object());
        Pointer r2 = new Pointer(new Object());
        result = coreOperationCompare1.equal(l2, r2);
        assertTrue(result);

        // Given non-null pointers with different values
        Pointer l3 = new Pointer(new Object());
        Pointer r3 = new Pointer(new Object());
        result = coreOperationCompare1.equal(l3, r3);
        assertFalse(result);

        // Given non-pointers with the same value
        Object l4 = new Object();
        Object r4 = new Object();
        result = coreOperationCompare1.equal(l4, r4);
        assertTrue(result);

        // Given non-pointers with different values
        Object l5 = new Object();
        Object r5 = new Object();
        result = coreOperationCompare1.equal(l5, r5);
        assertFalse(result);

        // Given one pointer and one number
        Pointer l6 = new Pointer(new Object());
        Number r6 = 1;
        result = coreOperationCompare1.equal(l6, r6);
        assertFalse(result);

        // Given one pointer and one string
        Pointer l7 = new Pointer(new Object());
        String r7 = "test";
        result = coreOperationCompare1.equal(l7, r7);
        assertFalse(result);

        // Given one number and one string
        Number l8 = 1;
        String r8 = "test";
        result = coreOperationCompare1.equal(l8, r8);
        assertFalse(result);

        // Given one collection and one number
        Iterator it = new ArrayList(Collections.singletonList(1)).iterator();
        Number r9 = 1;
        result = coreOperationCompare1.contains(it, r9);
        assertFalse(result);

        // Given one collection and one string
        Iterator it1 = new ArrayList(Collections.singletonList("test")).iterator();
        String r10 = "test";
        result = coreOperationCompare1.contains(it1, r10);
        assertFalse(result);

        // Given one number and one collection
        Number l11 = 1;
        Iterator it2 = new ArrayList(Collections.singletonList("test")).iterator();
        result = coreOperationCompare1.contains(it2, l11);
        assertFalse(result);

        // Given one string and one collection
        String l12 = "test";
        Iterator it3 = new ArrayList(Collections.singletonList(1)).iterator();
        result = coreOperationCompare1.contains(it3, l12);
        assertFalse(result);

        // Given two collections with the same elements
        Iterator lit = new ArrayList(Collections.singletonList(1)).iterator();
        Iterator rit = new ArrayList(Collections.singletonList(1)).iterator();
        result = coreOperationCompare1.findMatch(lit, rit);
        assertTrue(result);

        // Given two collections with different elements
        Iterator lit1 = new ArrayList(Collections.singletonList(1)).iterator();
        Iterator rit1 = new ArrayList(Collections.singletonList("test")).iterator();
        result = coreOperationCompare1.findMatch(lit1, rit1);
        assertFalse(result);

        // Given one collection and one empty collection
        Iterator it4 = new ArrayList().iterator();
        Iterator r11 = new ArrayList().iterator();
        result = coreOperationCompare1.findMatch(it4, r11);
        assertFalse(result);

        // Given one empty collection and one collection
        Iterator it5 = new ArrayList().iterator();
        Iterator r12 = new ArrayList(Collections.singletonList(1)).iterator();
        result = coreOperationCompare1.findMatch(it5, r12);
        assertFalse(result);
    }

}