package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqualArg;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqualArg2;
import org.apache.commons.jxpath.ri.tests.BaseTest;

public class CoreOperationEqualTest extends BaseTest {

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testCoreOperationEqual_whenBothArgsAreSameObject_thenReturnsTrue() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(
                new CoreOperationEqualArg(createValue(1)),
                new CoreOperationEqualArg2(createValue(1))
        );
        EvalContext context = new EvalContext();
        assertEquals(Boolean.TRUE, coreOperationEqual.computeValue(context));
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testCoreOperationEqual_whenBothArgsAreDifferentObjectsButHaveSameValue_thenReturnsTrue() {
        Object value1 = createValue(1);
        Object value2 = createValue(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(
                new CoreOperationEqualArg(value1),
                new CoreOperationEqualArg2(value2)
        );
        EvalContext context = new EvalContext();
        assertEquals(Boolean.TRUE, coreOperationEqual.computeValue(context));
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testCoreOperationEqual_whenBothArgsAreDifferentObjectsButHaveDifferentValues_thenReturnsFalse() {
        Object value1 = createValue(1);
        Object value2 = createValue(2);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(
                new CoreOperationEqualArg(value1),
                new CoreOperationEqualArg2(value2)
        );
        EvalContext context = new EvalContext();
        assertFalse(coreOperationEqual.computeValue(context).booleanValue());
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCoreOperationEqual_whenFirstArgIsNull_thenThrowsIllegalArgumentException() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(
                null,
                new CoreOperationEqualArg2(createValue(1))
        );
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCoreOperationEqual_whenSecondArgIsNull_thenThrowsIllegalArgumentException() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(
                new CoreOperationEqualArg(createValue(1)),
                null
        );
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testCoreOperationEqual_whenFirstArgIsANumberAndSecondArgIsString_thenThrowsRuntimeException() {
        Object value1 = createValue(1);
        String value2 = "one";
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(
                new CoreOperationEqualArg(value1),
                new CoreOperationEqualArg2(value2)
        );
        EvalContext context = new EvalContext();
        try {
            coreOperationEqual.computeValue(context);
            fail("Expected RuntimeException");
        } catch (RuntimeException e) {
            // expected
        }
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testCoreOperationEqual_whenFirstArgIsStringAndSecondArgIsANumber_thenThrowsRuntimeException() {
        String value1 = "one";
        Object value2 = createValue(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(
                new CoreOperationEqualArg(value1),
                new CoreOperationEqualArg2(value2)
        );
        EvalContext context = new EvalContext();
        try {
            coreOperationEqual.computeValue(context);
            fail("Expected RuntimeException");
        } catch (RuntimeException e) {
            // expected
        }
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testCoreOperationEqual_whenFirstArgIsANumberAndSecondArgIsANumberButWithDifferentValues_thenReturnsFalse() {
        Object value1 = createValue(1);
        Object value2 = createValue(2);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(
                new CoreOperationEqualArg(value1),
                new CoreOperationEqualArg2(value2)
        );
        EvalContext context = new EvalContext();
        assertFalse(coreOperationEqual.computeValue(context).booleanValue());
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testCoreOperationEqual_whenFirstArgIsANumberAndSecondArgIsANumberWithTheSameValue_thenReturnsTrue() {
        Object value1 = createValue(1);
        Object value2 = createValue(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(
                new CoreOperationEqualArg(value1),
                new CoreOperationEqualArg2(value2)
        );
        EvalContext context = new EvalContext();
        assertEquals(Boolean.TRUE, coreOperationEqual.computeValue(context));
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testCoreOperationEqual_whenFirstArgIsStringAndSecondArgIsStringWithTheSameValue_thenReturnsTrue() {
        String value1 = "one";
        String value2 = "one";
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(
                new CoreOperationEqualArg(value1),
                new CoreOperationEqualArg2(value2)
        );
        EvalContext context = new EvalContext();
        assertEquals(Boolean.TRUE, coreOperationEqual.computeValue(context));
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testCoreOperationEqual_whenFirstArgIsANumberAndSecondArgIsANull_thenReturnsFalse() {
        Object value1 = createValue(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(
                new CoreOperationEqualArg(value1),
                new CoreOperationEqualArg2(null)
        );
        EvalContext context = new EvalContext();
        assertFalse(coreOperationEqual.computeValue(context).booleanValue());
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testCoreOperationEqual_whenFirstArgIsANullAndSecondArgIsANumber_thenReturnsFalse() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(
                null,
                new CoreOperationEqualArg2(createValue(1))
        );
        EvalContext context = new EvalContext();
        assertFalse(coreOperationEqual.computeValue(context).booleanValue());
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testCoreOperationEqual_whenFirstArgIsANullAndSecondArgIsANull_thenReturnsTrue() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(
                null,
                null
        );
        EvalContext context = new EvalContext();
        assertEquals(Boolean.TRUE, coreOperationEqual.computeValue(context));
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testCoreOperationEqual_whenFirstArgIsANullAndSecondArgIsAList_thenReturnsFalse() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(
                null,
                new CoreOperationEqualArg2(createArray())
        );
        EvalContext context = new EvalContext();
        assertFalse(coreOperationEqual.computeValue(context).booleanValue());
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testCoreOperationEqual_whenFirstArgIsAListAndSecondArgIsANull_thenReturnsFalse() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(
                new CoreOperationEqualArg(createArray()),
                null
        );
        EvalContext context = new EvalContext();
        assertFalse(coreOperationEqual.computeValue(context).booleanValue());
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testCoreOperationEqual_whenFirstArgIsAListAndSecondArgIsAListWithTheSameElements_thenReturnsTrue() {
        Object[] list1 = {createValue(1), createValue(1)};
        Object[] list2 = {createValue(1), createValue(1)};
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(
                new CoreOperationEqualArg(createArray(list1)),
                new CoreOperationEqualArg2(createArray(list2))
        );
        EvalContext context = new EvalContext();
        assertEquals(Boolean.TRUE, coreOperationEqual.computeValue(context));
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testCoreOperationEqual_whenFirstArgIsAListAndSecondArgIsAListWithDifferentElements_thenReturnsFalse() {
        Object[] list1 = {createValue(1), createValue(1)};
        Object[] list2 = {createValue(1), createValue(2)};
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(
                new CoreOperationEqualArg(createArray(list1)),
                new CoreOperationEqualArg2(createArray(list2))
        );
        EvalContext context = new EvalContext();
        assertFalse(coreOperationEqual.computeValue(context).booleanValue());
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testCoreOperationEqual_whenFirstArgIsAListAndSecondArgIsAnEmptyList_thenReturnsFalse() {
        Object[] list1 = {createValue(1)};
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(
                new CoreOperationEqualArg(createArray(list1)),
                new CoreOperationEqualArg2(createEmptyArray())
        );
        EvalContext context = new EvalContext();
        assertFalse(coreOperationEqual.computeValue(context).booleanValue());
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testCoreOperationEqual_whenFirstArgIsAnEmptyListAndSecondArgIsAList_thenReturnsFalse() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(
                new CoreOperationEqualArg(createEmptyArray()),
                new CoreOperationEqualArg2(createArray(new Object[]{createValue(1)}))
        );
        EvalContext context = new EvalContext();
        assertFalse(coreOperationEqual.computeValue(context).booleanValue());
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testCoreOperationEqual_whenFirstArgIsAListAndSecondArgIsAnEmptyList_thenReturnsFalse() {
        Object[] list1 = {createValue(1)};
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(
                new CoreOperationEqualArg(createArray(list1)),
                new CoreOperationEqualArg2(createEmptyArray())
        );
        EvalContext context = new EvalContext();
        assertFalse(coreOperationEqual.computeValue(context).booleanValue());
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testCoreOperationEqual_whenFirstArgIsAnEmptyListAndSecondArgIsAnEmptyList_thenReturnsTrue() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(
                new CoreOperationEqualArg(createEmptyArray()),
                new CoreOperationEqualArg2(createEmptyArray())
        );
        EvalContext context = new EvalContext();
        assertEquals(Boolean.TRUE, coreOperationEqual.computeValue(context));
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testCoreOperationEqual_whenFirstArgIsADictionaryAndSecondArgIsADictionaryWithTheSameKeyValuePairs_thenReturnsTrue() {
        Object dictionary1 = createDictionary();
        Object dictionary2 = createDictionary();
        for (Object key : dictionary1.keySet()) {
            Object value = dictionary1.get(key);
            Object value2 = dictionary2.get(key);
            assertEquals(value, value2);
        }
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(
                new CoreOperationEqualArg(createDictionary(dictionary1)),
                new CoreOperationEqualArg2(createDictionary(dictionary2))
        );
        EvalContext context = new EvalContext();
        assertEquals(Boolean.TRUE, coreOperationEqual.computeValue(context));
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testCoreOperationEqual_whenFirstArgIsADictionaryAndSecondArgIsADictionaryWithDifferentKeyValuePairs_thenReturnsFalse() {
        Object dictionary1 = createDictionary();
        Object dictionary2 = createDictionary();
        for (Object key : dictionary1.keySet()) {
            Object value = dictionary1.get(key);
            Object value2 = dictionary2.get(key);
            if (!value.equals(value2)) {
                fail("Expected same key-value pairs in dictionaries but found different ones: " + key + " = " + value + " != " + value2);
            }
            dictionary2.put(key, createValue(2));
        }
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(
                new CoreOperationEqualArg(createDictionary(dictionary1)),
                new CoreOperationEqualArg2(createDictionary(dictionary2))
        );
        EvalContext context = new EvalContext();
        assertFalse(coreOperationEqual.computeValue(context).booleanValue());
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testCoreOperationEqual_whenFirstArgIsADictionaryAndSecondArgIsAnEmptyDictionary_thenReturnsFalse() {
        Object dictionary1 = createDictionary();
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(
                new CoreOperationEqualArg(createDictionary(dictionary1)),
                new CoreOperationEqualArg2(createEmptyDictionary())
        );
        EvalContext context = new EvalContext();
        assertFalse(coreOperationEqual.computeValue(context).booleanValue());
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testCoreOperationEqual_whenFirstArgIsAnEmptyDictionaryAndSecondArgIsADictionary_thenReturnsFalse() {
        Object dictionary1 = createEmptyDictionary();
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(
                new CoreOperationEqualArg(createEmptyDictionary()),
                new CoreOperationEqualArg2(createDictionary(new Object[][]{new Object[]{"key", createValue(1)}}))
        );
        EvalContext context = new EvalContext();
        assertFalse(coreOperationEqual.computeValue(context).booleanValue());
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testCoreOperationEqual_whenFirstArgIsADictionaryAndSecondArgIsAnEmptyDictionary_thenReturnsFalse() {
        Object dictionary1 = createDictionary();
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(
                new CoreOperationEqualArg(createDictionary(dictionary1)),
                new CoreOperationEqualArg2(createEmptyDictionary())
        );
        EvalContext context = new EvalContext();
        assertFalse(coreOperationEqual.computeValue(context).booleanValue());
    }

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testCoreOperationEqual_whenFirstArgIsAnEmptyDictionaryAndSecondArgIsAnEmptyDictionary_thenReturnsTrue() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(
                new CoreOperationEqualArg(createEmptyDictionary()),
                new CoreOperationEqualArg2(createEmptyDictionary())
        );
        EvalContext context = new EvalContext();
        assertEquals(Boolean.TRUE, coreOperationEqual.computeValue(context));
    }

}