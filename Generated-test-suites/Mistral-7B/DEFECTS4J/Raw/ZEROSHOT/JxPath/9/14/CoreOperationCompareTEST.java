package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.ExpressionContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.CoreOperationConstant;
import org.apache.commons.jxpath.ri.compiler.CoreOperationNumber;
import org.apache.commons.jxpath.ri.compiler.CoreOperationString;
import org.apache.commons.jxpath.ri.compiler.ExpressionFactory;
import org.apache.commons.jxpath.ri.compiler.ExpressionParser;
import org.apache.commons.jxpath.ri.compiler.StaticContext;
import org.apache.commons.jxpath.ri.compiler.StaticVariable;
import org.apache.commons.jxpath.ri.iter.Iterator;
import org.apache.commons.jxpath.ri.iter.SingletonIterator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * CoreOperationCompareTest class file to test all methods in CoreOperationCompare class
 */
public class CoreOperationCompareTest {

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testCoreOperationCompare() {
        // Given
        ExpressionFactory expressionFactory = new ExpressionFactory();
        ExpressionParser expressionParser = new ExpressionParser();
        ExpressionContext expressionContext = new ExpressionContext();
        EvalContext evalContext = new EvalContext();
        StaticContext staticContext = new StaticContext();

        // When - test equal method
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new CoreOperationConstant(1),
                new CoreOperationConstant(1)
        );
        assertTrue(coreOperationCompare.equal(evalContext, new CoreOperationNumber(1), new CoreOperationNumber(1)));

        coreOperationCompare = new CoreOperationCompare(
                new CoreOperationConstant("test"),
                new CoreOperationConstant("test")
        );
        assertTrue(coreOperationCompare.equal(evalContext, new CoreOperationString("test"), new CoreOperationString("test")));

        coreOperationCompare = new CoreOperationCompare(
                new CoreOperationConstant(true),
                new CoreOperationConstant(true)
        );
        assertTrue(coreOperationCompare.equal(evalContext, new CoreOperationBoolean(true), new CoreOperationBoolean(true)));

        coreOperationCompare = new CoreOperationCompare(
                new CoreOperationNumber(1.0),
                new CoreOperationNumber(1.0)
        );
        assertTrue(coreOperationCompare.equal(evalContext, new CoreOperationNumber(1.0), new CoreOperationNumber(1.0)));

        coreOperationCompare = new CoreOperationCompare(
                new CoreOperationString("test"),
                new CoreOperationString("test")
        );
        assertTrue(coreOperationCompare.equal(evalContext, new CoreOperationString("test"), new CoreOperationString("test")));

        coreOperationCompare = new CoreOperationCompare(
                new CoreOperationConstant(null),
                new CoreOperationConstant(null)
        );
        assertTrue(coreOperationCompare.equal(evalContext, new CoreOperationConstant(null), new CoreOperationConstant(null)));

        coreOperationCompare = new CoreOperationCompare(
                new CoreOperationConstant(1),
                new CoreOperationConstant(2)
        );
        assertFalse(coreOperationCompare.equal(evalContext, new CoreOperationNumber(1), new CoreOperationNumber(2)));

        coreOperationCompare = new CoreOperationCompare(
                new CoreOperationConstant("test"),
                new CoreOperationConstant("test2")
        );
        assertFalse(coreOperationCompare.equal(evalContext, new CoreOperationString("test"), new CoreOperationString("test2")));

        coreOperationCompare = new CoreOperationCompare(
                new CoreOperationConstant(true),
                new CoreOperationConstant(false)
        );
        assertFalse(coreOperationCompare.equal(evalContext, new CoreOperationBoolean(true), new CoreOperationBoolean(false)));

        coreOperationCompare = new CoreOperationCompare(
                new CoreOperationNumber(1.0),
                new CoreOperationNumber(2.0)
        );
        assertFalse(coreOperationCompare.equal(evalContext, new CoreOperationNumber(1.0), new CoreOperationNumber(2.0)));

        coreOperationCompare = new CoreOperationCompare(
                new CoreOperationString("test"),
                new CoreOperationString("test2")
        );
        assertFalse(coreOperationCompare.equal(evalContext, new CoreOperationString("test"), new CoreOperationString("test2")));

        coreOperationCompare = new CoreOperationCompare(
                new CoreOperationConstant(null),
                new CoreOperationConstant("test")
        );
        assertFalse(coreOperationCompare.equal(evalContext, new CoreOperationConstant(null), new CoreOperationString("test")));

        // When - test contains method
        coreOperationCompare = new CoreOperationCompare(
                new CoreOperationConstant(Collections.singletonList(1)),
                new CoreOperationConstant(1)
        );
        assertTrue(coreOperationCompare.contains(evalContext, new CoreOperationConstant(Collections.singletonList(1)).iterator(), new CoreOperationNumber(1)));

        coreOperationCompare = new CoreOperationCompare(
                new CoreOperationConstant(Arrays.asList(1, 2)),
                new CoreOperationConstant(1)
        );
        assertTrue(coreOperationCompare.contains(evalContext, new CoreOperationConstant(Arrays.asList(1, 2)).iterator(), new CoreOperationNumber(1)));

        coreOperationCompare = new CoreOperationCompare(
                new CoreOperationConstant(Collections.singletonList("test")),
                new CoreOperationString("test")
        );
        assertTrue(coreOperationCompare.contains(evalContext, new CoreOperationConstant(Collections.singletonList("test")).iterator(), new CoreOperationString("test")));

        coreOperationCompare = new CoreOperationCompare(
                new CoreOperationConstant(Arrays.asList("test", "test2")),
                new CoreOperationString("test")
        );
        assertTrue(coreOperationCompare.contains(evalContext, new CoreOperationConstant(Arrays.asList("test", "test2")).iterator(), new CoreOperationString("test")));

        coreOperationCompare = new CoreOperationCompare(
                new CoreOperationConstant(Arrays.asList(1, 2)),
                new CoreOperationConstant(1)
        );
        assertFalse(coreOperationCompare.contains(evalContext, new CoreOperationConstant(Arrays.asList(1, 2)).iterator(), new CoreOperationNumber(3)));

        coreOperationCompare = new CoreOperationCompare(
                new CoreOperationConstant(Collections.singletonList("test")),
                new CoreOperationString("test2")
        );
        assertFalse(coreOperationCompare.contains(evalContext, new CoreOperationConstant(Collections.singletonList("test")).iterator(), new CoreOperationString("test2")));

        // When - test findMatch method
        List<Integer> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(2);
        List<Integer> list2 = new ArrayList<>();
        list2.add(1);
        list2.add(2);
        list2.add(3);

        CoreOperationCompare coreOperationCompareList = new CoreOperationCompare(
                new CoreOperationConstant(list1),
                new CoreOperationConstant(list2)
        );
        assertTrue(coreOperationCompareList.findMatch(evalContext, list1.iterator(), list2.iterator()));

        CoreOperationCompare coreOperationCompareString = new CoreOperationCompare(
                new CoreOperationConstant(Arrays.asList("test", "test2")),
                new CoreOperationConstant(Arrays.asList("test", "test3"))
        );
        assertFalse(coreOperationCompareString.findMatch(evalContext, Arrays.asList("test", "test2").iterator(), Arrays.asList("test", "test3").iterator()));

        // When - test findMatch method with single value
        CoreOperationCompare coreOperationCompareSingle = new CoreOperationCompare(
                new CoreOperationConstant(1),
                new CoreOperationConstant(Collections.singletonList(1))
        );
        assertTrue(coreOperationCompareSingle.findMatch(evalContext, new CoreOperationConstant(1).iterator(), new SingletonIterator(1).iterator()));

        CoreOperationCompare coreOperationCompareStringSingle = new CoreOperationCompare(
                new CoreOperationConstant("test"),
                new CoreOperationConstant(Collections.singletonList("test"))
        );
        assertTrue(coreOperationCompareStringSingle.findMatch(evalContext, new CoreOperationConstant("test").iterator(), new SingletonIterator("test").iterator()));

        CoreOperationCompare coreOperationCompareSingleList = new CoreOperationCompare(
                new CoreOperationConstant(Collections.singletonList(1)),
                new CoreOperationConstant(1)
        );
        assertFalse(coreOperationCompareSingleList.findMatch(evalContext, new SingletonIterator(1).iterator(), new CoreOperationConstant(1).iterator()));

        CoreOperationCompare coreOperationCompareStringSingleList = new CoreOperationCompare(
                new CoreOperationConstant(Collections.singletonList("test")),
                new CoreOperationConstant("test")
        );
        assertFalse(coreOperationCompareStringSingleList.findMatch(evalContext, new SingletonIterator("test").iterator(), new CoreOperationConstant("test").iterator()));

        // When - test isSymmetric method
        assertTrue(coreOperationCompare.isSymmetric());

        // When - test getPrecedence method
        assertEquals(2, coreOperationCompare.getPrecedence());

        // When - test equals method
        assertEquals(coreOperationCompare.getClass(), new CoreOperationCompare(
                new CoreOperationConstant(1),
                new CoreOperationConstant(1)
        ).getClass());
        assertNotEquals(coreOperationCompare.getClass(), new CoreOperationCompare(
                new CoreOperationConstant(1),
                new CoreOperationConstant(2)
        ).getClass());

        // When - test hashCode method
        int hashCode1 = coreOperationCompare.hashCode();
        int hashCode2 = coreOperationCompare.hashCode();
        assertEquals(hashCode1, hashCode2);

        CoreOperationCompare coreOperationCompareDiff = new CoreOperationCompare(
                new CoreOperationConstant(1),
                new CoreOperationConstant(2)
        );
        assertNotEquals(hashCode1, coreOperationCompareDiff.hashCode());

        // When - test toString method
        String toString1 = coreOperationCompare.toString(staticContext, new StaticVariable[0]);
        String toString2 = coreOperationCompare.toString(staticContext, new StaticVariable[0]);
        assertEquals(toString1, toString2);

        CoreOperationCompare coreOperationCompareToStringDiff = new CoreOperationCompare(
                new CoreOperationConstant(1),
                new CoreOperationConstant(2)
        );
        assertNotEquals(toString1, coreOperationCompareToStringDiff.toString(staticContext, new StaticVariable[0]));
    }

}