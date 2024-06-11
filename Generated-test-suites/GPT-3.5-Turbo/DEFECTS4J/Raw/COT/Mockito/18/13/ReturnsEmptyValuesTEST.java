
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswer_shouldReturnDefaultValuesForPrimitivesAndWrappers() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();

        assertEquals(0, answer.answer(mock(InvocationOnMock.class)));
        assertEquals(0, answer.answer(mock(InvocationOnMock.class)));
        assertEquals(0L, answer.answer(mock(InvocationOnMock.class)));
        assertEquals(0L, answer.answer(mock(InvocationOnMock.class)));
        assertEquals(0f, answer.answer(mock(InvocationOnMock.class)));
        assertEquals(0f, answer.answer(mock(InvocationOnMock.class)));
        assertEquals(0d, answer.answer(mock(InvocationOnMock.class)));
        assertEquals(0d, answer.answer(mock(InvocationOnMock.class)));
        assertEquals('\u0000', answer.answer(mock(InvocationOnMock.class)));
        assertEquals('\u0000', answer.answer(mock(InvocationOnMock.class)));
        assertEquals(false, answer.answer(mock(InvocationOnMock.class)));
        assertEquals(false, answer.answer(mock(InvocationOnMock.class)));
        assertEquals((byte) 0, answer.answer(mock(InvocationOnMock.class)));
        assertEquals((byte) 0, answer.answer(mock(InvocationOnMock.class)));
        assertEquals((short) 0, answer.answer(mock(InvocationOnMock.class)));
        assertEquals((short) 0, answer.answer(mock(InvocationOnMock.class)));
        assertEquals((char) 0, answer.answer(mock(InvocationOnMock.class)));
        assertEquals((char) 0, answer.answer(mock(InvocationOnMock.class)));
        assertEquals(0b0, answer.answer(mock(InvocationOnMock.class)));
        assertEquals(0b0, answer.answer(mock(InvocationOnMock.class)));
        assertEquals(0x0, answer.answer(mock(InvocationOnMock.class)));
        assertEquals(0x0, answer.answer(mock(InvocationOnMock.class)));

        assertNull(answer.answer(mock(InvocationOnMock.class)));
    }

    @Test
    public void testAnswer_shouldReturnEmptyCollectionsForCollectionTypes() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();

        assertTrue(answer.answer(mock(InvocationOnMock.class)) instanceof LinkedList);
        assertTrue(answer.answer(mock(InvocationOnMock.class)) instanceof HashSet);
        assertTrue(answer.answer(mock(InvocationOnMock.class)) instanceof TreeSet);
        assertTrue(answer.answer(mock(InvocationOnMock.class)) instanceof LinkedHashSet);
        assertTrue(answer.answer(mock(InvocationOnMock.class)) instanceof LinkedList);
        assertTrue(answer.answer(mock(InvocationOnMock.class)) instanceof ArrayList);
        assertTrue(answer.answer(mock(InvocationOnMock.class)) instanceof HashMap);
        assertTrue(answer.answer(mock(InvocationOnMock.class)) instanceof TreeMap);
        assertTrue(answer.answer(mock(InvocationOnMock.class)) instanceof LinkedHashMap);
    }

    @Test
    public void testAnswer_shouldReturnNullForUnsupportedTypes() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();

        assertNull(answer.answer(mock(InvocationOnMock.class)));
        assertNull(answer.answer(mock(InvocationOnMock.class)));
    }
}
