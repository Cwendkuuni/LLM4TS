
package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class ReturnsEmptyValuesTest {
    
    private ReturnsEmptyValues returnsEmptyValues;

    @Before
    public void setUp() {
        returnsEmptyValues = new ReturnsEmptyValues();
    }

    @Test
    public void testAnswerToString() {
        Object mock = new Object();
        assertEquals("Mock for Object, hashCode: " + mock.hashCode(), returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class, Mockito.withSettings().defaultAnswer(returnsEmptyValues))).toString());
    }

    @Test
    public void testAnswerCompareTo() {
        assertEquals(1, returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class, Mockito.withSettings().defaultAnswer(returnsEmptyValues))).compareTo(new Object()));
    }

    @Test
    public void testAnswerPrimitiveReturn() {
        assertEquals((byte) 0, returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class, Mockito.withSettings().defaultAnswer(returnsEmptyValues))));
        assertEquals((short) 0, returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class, Mockito.withSettings().defaultAnswer(returnsEmptyValues))));
        assertEquals(0, returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class, Mockito.withSettings().defaultAnswer(returnsEmptyValues))));
        assertEquals(0L, returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class, Mockito.withSettings().defaultAnswer(returnsEmptyValues))));
        assertEquals(0.0f, returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class, Mockito.withSettings().defaultAnswer(returnsEmptyValues))), 0);
        assertEquals(0.0, returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class, Mockito.withSettings().defaultAnswer(returnsEmptyValues))), 0);
        assertEquals(false, returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class, Mockito.withSettings().defaultAnswer(returnsEmptyValues))));
    }

    @Test
    public void testAnswerWrapperReturn() {
        assertEquals(Byte.valueOf((byte) 0), returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class, Mockito.withSettings().defaultAnswer(returnsEmptyValues))));
        assertEquals(Short.valueOf((short) 0), returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class, Mockito.withSettings().defaultAnswer(returnsEmptyValues))));
        assertEquals(Integer.valueOf(0), returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class, Mockito.withSettings().defaultAnswer(returnsEmptyValues))));
        assertEquals(Long.valueOf(0L), returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class, Mockito.withSettings().defaultAnswer(returnsEmptyValues))));
        assertEquals(Float.valueOf(0.0f), returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class, Mockito.withSettings().defaultAnswer(returnsEmptyValues))));
        assertEquals(Double.valueOf(0.0), returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class, Mockito.withSettings().defaultAnswer(returnsEmptyValues))));
        assertEquals(Boolean.valueOf(false), returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class, Mockito.withSettings().defaultAnswer(returnsEmptyValues))));
    }

    @Test
    public void testAnswerCollectionReturn() {
        assertEquals(new LinkedList<Object>(), returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class, Mockito.withSettings().defaultAnswer(returnsEmptyValues))));
        assertEquals(new HashSet<Object>(), returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class, Mockito.withSettings().defaultAnswer(returnsEmptyValues))));
        assertEquals(new TreeSet<Object>(), returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class, Mockito.withSettings().defaultAnswer(returnsEmptyValues))));
        assertEquals(new LinkedHashSet<Object>(), returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class, Mockito.withSettings().defaultAnswer(returnsEmptyValues))));
        assertEquals(new ArrayList<Object>(), returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class, Mockito.withSettings().defaultAnswer(returnsEmptyValues))));
    }

    @Test
    public void testAnswerMapReturn() {
        assertEquals(new HashMap<Object, Object>(), returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class, Mockito.withSettings().defaultAnswer(returnsEmptyValues))));
        assertEquals(new TreeMap<Object, Object>(), returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class, Mockito.withSettings().defaultAnswer(returnsEmptyValues))));
        assertEquals(new LinkedHashMap<Object, Object>(), returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class, Mockito.withSettings().defaultAnswer(returnsEmptyValues))));
    }

    @Test
    public void testAnswerOtherReturn() {
        assertNull(returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class, Mockito.withSettings().defaultAnswer(returnsEmptyValues))));
    }
}
