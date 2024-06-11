package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.Pointer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class CoreOperationCompareTest {

    @Mock
    private EvalContext context;

    private CoreOperationCompare coreOperationCompare;

    private Expression arg1;
    private Expression arg2;

    @Before
    public void setup() {
        arg1 = new StubExpression();
        arg2 = new StubExpression();

        coreOperationCompare = new CoreOperationCompare(arg1, arg2) {
            @Override
            protected boolean equal(EvalContext context, Expression left, Expression right) {
                return super.equal(context, left, right);
            }
        };
    }

    @Test
    public void testEqualObjects() {
        Object obj1 = new Object();
        Object obj2 = obj1;

        assertTrue(coreOperationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testEqualPrimitiveTypes() {
        byte byte1 = 1;
        byte byte2 = 1;
        short short1 = 1;
        short short2 = 1;
        int int1 = 1;
        int int2 = 1;
        long long1 = 1L;
        long long2 = 1L;
        float float1 = 1.0f;
        float float2 = 1.0f;
        double double1 = 1.0;
        double double2 = 1.0;
        boolean boolean1 = true;
        boolean boolean2 = true;

        assertTrue(coreOperationCompare.equal(context, arg1, arg2));

        setArguments(byte1, byte2);
        assertTrue(coreOperationCompare.equal(context, arg1, arg2));
        setArguments(short1, short2);
        assertTrue(coreOperationCompare.equal(context, arg1, arg2));
        setArguments(int1, int2);
        assertTrue(coreOperationCompare.equal(context, arg1, arg2));
        setArguments(long1, long2);
        assertTrue(coreOperationCompare.equal(context, arg1, arg2));
        setArguments(float1, float2);
        assertTrue(coreOperationCompare.equal(context, arg1, arg2));
        setArguments(double1, double2);
        assertTrue(coreOperationCompare.equal(context, arg1, arg2));
        setArguments(boolean1, boolean2);
        assertTrue(coreOperationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testEqualStrings() {
        String str1 = "test";
        String str2 = "test";

        assertTrue(coreOperationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testEqualCollections() {
        List<Integer> list1 = Arrays.asList(1, 2, 3);
        List<Integer> list2 = Arrays.asList(1, 2, 3);

        assertTrue(coreOperationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testEqualIterators() {
        Iterator<Integer> iterator1 = Arrays.asList(1, 2, 3).iterator();
        Iterator<Integer> iterator2 = Arrays.asList(1, 2, 3).iterator();

        assertTrue(coreOperationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testNotEqualObjects() {
        Object obj1 = new Object();
        Object obj2 = new Object();

        assertFalse(coreOperationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testNotEqualPrimitiveTypes() {
        byte byte1 = 1;
        byte byte2 = 2;
        short short1 = 1;
        short short2 = 2;
        int int1 = 1;
        int int2 = 2;
        long long1 = 1L;
        long long2 = 2L;
        float float1 = 1.0f;
        float float2 = 2.0f;
        double double1 = 1.0;
        double double2 = 2.0;
        boolean boolean1 = true;
        boolean boolean2 = false;

        assertFalse(coreOperationCompare.equal(context, arg1, arg2));

        setArguments(byte1, byte2);
        assertFalse(coreOperationCompare.equal(context, arg1, arg2));
        setArguments(short1, short2);
        assertFalse(coreOperationCompare.equal(context, arg1, arg2));
        setArguments(int1, int2);
        assertFalse(coreOperationCompare.equal(context, arg1, arg2));
        setArguments(long1, long2);
        assertFalse(coreOperationCompare.equal(context, arg1, arg2));
        setArguments(float1, float2);
        assertFalse(coreOperationCompare.equal(context, arg1, arg2));
        setArguments(double1, double2);
        assertFalse(coreOperationCompare.equal(context, arg1, arg2));
        setArguments(boolean1, boolean2);
        assertFalse(coreOperationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testNotEqualStrings() {
        String str1 = "test";
        String str2 = "another";

        assertFalse(coreOperationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testNotEqualCollections() {
        List<Integer> list1 = Arrays.asList(1, 2, 3);
        List<Integer> list2 = Arrays.asList(1, 2, 4);

        assertFalse(coreOperationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testNotEqualIterators() {
        Iterator<Integer> iterator1 = Arrays.asList(1, 2, 3).iterator();
        Iterator<Integer> iterator2 = Arrays.asList(1, 2, 4).iterator();

        assertFalse(coreOperationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testContains() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        Object obj = 2;

        assertTrue(coreOperationCompare.contains(context, list.iterator(), obj));

        list = Collections.emptyList();
        assertFalse(coreOperationCompare.contains(context, list.iterator(), obj));
    }

    @Test
    public void testFindMatch() {
        List<Integer> list1 = Arrays.asList(1, 2, 3);
        List<Integer> list2 = Arrays.asList(2, 3, 4);

        assertTrue(coreOperationCompare.findMatch(list1.iterator(), list2.iterator()));

        list1 = Collections.emptyList();
        assertFalse(coreOperationCompare.findMatch(list1.iterator(), list2.iterator()));
    }

    @Test
    public void testEqualPointers() {
        Pointer pointer1 = new Pointer() {
            @Override
            public Object getValue() {
                return 1;
            }
        };
        Pointer pointer2 = new Pointer() {
            @Override
            public Object getValue() {
                return 1;
            }
        };

        assertTrue(coreOperationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testNotEqualPointers() {
        Pointer pointer1 = new Pointer() {
            @Override
            public Object getValue() {
                return 1;
            }
        };
        Pointer pointer2 = new Pointer() {
            @Override
            public Object getValue() {
                return 2;
            }
        };

        assertFalse(coreOperationCompare.equal(context, arg1, arg2));
    }

    private void setArguments(Object... arguments) {
        when(arg1.compute(context)).thenReturn(arguments[0]);
        when(arg2.compute(context)).thenReturn(arguments[1]);
    }
}

class StubExpression implements Expression {

    @Override
    public Object compute(EvalContext context) {
        return null;
    }
}