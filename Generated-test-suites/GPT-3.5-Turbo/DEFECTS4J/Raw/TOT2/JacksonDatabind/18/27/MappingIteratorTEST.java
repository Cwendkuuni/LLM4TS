import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

@RunWith(MockitoJUnitRunner.class)
public class MappingIteratorTest {

    @Mock
    private MappingIterator<Object> mappingIteratorMock;

    @Before
    public void setup() {
        // Initialization if needed
    }

    @Test
    public void hasNextValue_WhenHasNextElement_ReturnsTrue() throws IOException {
        when(mappingIteratorMock.hasNextValue()).thenReturn(true);

        assertTrue(mappingIteratorMock.hasNext());
    }

    @Test
    public void hasNextValue_WhenNoNextElement_ReturnsFalse() throws IOException {
        when(mappingIteratorMock.hasNextValue()).thenReturn(false);

        assertFalse(mappingIteratorMock.hasNext());
    }

    @Test(expected = NoSuchElementException.class)
    public void nextValue_WhenNoNextElement_ThrowsNoSuchElementException() throws IOException {
        when(mappingIteratorMock.hasNextValue()).thenReturn(false);

        mappingIteratorMock.next();
    }

    @Test
    public void nextValue_WhenHasNextElement_ReturnsElement() throws IOException {
        Object expected = new Object();
        when(mappingIteratorMock.hasNextValue()).thenReturn(true);
        when(mappingIteratorMock.nextValue()).thenReturn(expected);

        Object actual = mappingIteratorMock.next();

        assertEquals(expected, actual);
    }

    @Test
    public void readAll_WhenMultipleElements_ReturnsAllElementsInList() throws IOException {
        List<Object> elements = new ArrayList<>();
        elements.add(new Object());
        elements.add(new Object());
        when(mappingIteratorMock.hasNextValue()).thenReturn(true, true, false);
        when(mappingIteratorMock.nextValue()).thenReturn(elements.get(0), elements.get(1));

        List<Object> resultList = mappingIteratorMock.readAll();

        assertEquals(elements, resultList);
    }

    @Test
    public void readAll_WhenMultipleElements_ReturnsAllElementsInCustomList() throws IOException {
        List<Object> elements = new ArrayList<>();
        elements.add(new Object());
        elements.add(new Object());
        when(mappingIteratorMock.hasNextValue()).thenReturn(true, true, false);
        when(mappingIteratorMock.nextValue()).thenReturn(elements.get(0), elements.get(1));

        List<Object> resultList = mappingIteratorMock.readAll(new ArrayList<>());

        assertEquals(elements, resultList);
    }

    @Test
    public void readAll_WhenMultipleElements_ReturnsAllElementsInCustomCollection() throws IOException {
        List<Object> elements = new ArrayList<>();
        elements.add(new Object());
        elements.add(new Object());
        when(mappingIteratorMock.hasNextValue()).thenReturn(true, true, false);
        when(mappingIteratorMock.nextValue()).thenReturn(elements.get(0), elements.get(1));

        List<Object> resultList = mappingIteratorMock.readAll((List<Object>) new ArrayList<>());

        assertEquals(elements, resultList);
    }

    @Test(expected = RuntimeException.class)
    public void close_WhenIOExceptionOccurs_ThrowsRuntimeException() throws IOException {
        doThrow(new IOException()).when(mappingIteratorMock).close();

        mappingIteratorMock.close();
    }

    @Test
    public void close_WhenNoException_ClosesParser() throws IOException {
        mappingIteratorMock.close();

        verify(mappingIteratorMock, times(1)).close();
    }
}