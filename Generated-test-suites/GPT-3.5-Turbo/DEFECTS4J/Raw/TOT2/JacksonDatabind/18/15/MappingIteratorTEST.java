import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

public class MappingIteratorTest {

    private MappingIterator<String> mappingIterator;

    @Before
    public void setUp() {
        mappingIterator = mock(MappingIterator.class);
    }

    @Test
    public void testHasNextValue() throws IOException {
        when(mappingIterator.hasNextValue()).thenReturn(true, false);
        assertTrue(mappingIterator.hasNext());
        assertFalse(mappingIterator.hasNext());
    }

    @Test
    public void testNextValue() throws IOException {
        when(mappingIterator.nextValue()).thenReturn("test");
        assertEquals("test", mappingIterator.next());
    }

    @Test(expected = NoSuchElementException.class)
    public void testRemove() {
        mappingIterator.remove();
    }

    @Test
    public void testClose() throws IOException {
        mappingIterator.close();
        verify(mappingIterator).close();
    }

    @Test
    public void testReadAll() throws IOException {
        List<String> resultList = new ArrayList<>();
        when(mappingIterator.readAll(resultList)).thenReturn(resultList);
        assertEquals(resultList, mappingIterator.readAll());
    }

    @Test
    public void testReadAllWithCollection() throws IOException {
        List<String> results = new ArrayList<>();
        when(mappingIterator.readAll(results)).thenReturn(results);
        assertEquals(results, mappingIterator.readAll(results));
    }

    @Test
    public void testGetParser() {
        assertNotNull(mappingIterator.getParser());
    }

    @Test
    public void testGetParserSchema() {
        assertNotNull(mappingIterator.getParserSchema());
    }

    @Test
    public void testGetCurrentLocation() {
        assertNotNull(mappingIterator.getCurrentLocation());
    }

    @Test(expected = NoSuchElementException.class)
    public void testThrowNoSuchElement() {
        mappingIterator._throwNoSuchElement();
    }

    @Test(expected = RuntimeException.class)
    public void testHandleMappingException() {
        mappingIterator._handleMappingException(new JsonMappingException("error"));
    }

    @Test(expected = RuntimeException.class)
    public void testHandleIOException() {
        mappingIterator._handleIOException(new IOException("error"));
    }
}