import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;

public class MappingIteratorTest {

    private MappingIterator<Object> mappingIterator;
    private JsonParser mockParser;
    private DeserializationContext mockContext;
    private JavaType mockType;
    private JsonDeserializer<Object> mockDeserializer;
    
    @Before
    public void setUp() {
        mockParser = mock(JsonParser.class);
        mockContext = mock(DeserializationContext.class);
        mockType = mock(JavaType.class);
        mockDeserializer = mock(JsonDeserializer.class);
        mappingIterator = new MappingIterator<Object>(mockType, mockParser, mockContext, mockDeserializer, true, null);
    }

    @Test
    public void testHasNext() throws IOException {
        when(mockParser.getCurrentToken()).thenReturn(null).thenReturn(JsonToken.END_ARRAY);

        assertFalse(mappingIterator.hasNext());

        verify(mockParser, times(2)).getCurrentToken();
        verify(mockParser).nextToken();
        verify(mockParser).close();
    }

    @Test(expected = RuntimeException.class)
    public void testHasNextWithIOException() throws IOException {
        when(mockParser.getCurrentToken()).thenThrow(new IOException());

        mappingIterator.hasNext();
    }

    @Test
    public void testNext() throws IOException {
        when(mockParser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        when(mockParser.nextToken()).thenReturn(JsonToken.START_OBJECT, JsonToken.END_OBJECT);
        when(mockDeserializer.deserialize(mockParser, mockContext)).thenReturn("test");

        assertEquals("test", mappingIterator.next());

        verify(mockParser).getCurrentToken();
        verify(mockParser, times(2)).nextToken();
        verify(mockDeserializer).deserialize(mockParser, mockContext);
    }

    @Test(expected = RuntimeJsonMappingException.class)
    public void testNextWithJsonMappingException() throws IOException {
        when(mockParser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        when(mockDeserializer.deserialize(mockParser, mockContext)).thenThrow(new JsonMappingException("Mapping Exception"));
        mappingIterator.next();
    }

    @Test(expected = RuntimeException.class)
    public void testNextWithIOException() throws IOException {
        when(mockParser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        when(mockDeserializer.deserialize(mockParser, mockContext)).thenThrow(new IOException("IO Exception"));
        mappingIterator.next();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        mappingIterator.remove();
    }

    @Test
    public void testClose() throws IOException {
        mappingIterator.close();
        verify(mockParser).close();
    }

    @Test
    public void testReadAll() throws IOException {
        List<Object> resultList = new ArrayList<>();
        when(mockParser.getCurrentToken())
            .thenReturn(JsonToken.START_OBJECT)
            .thenReturn(JsonToken.START_OBJECT)
            .thenReturn(JsonToken.END_OBJECT)
            .thenReturn(null);

        when(mockParser.nextToken())
            .thenReturn(JsonToken.START_OBJECT)
            .thenReturn(JsonToken.END_OBJECT)
            .thenReturn(null);

        when(mockDeserializer.deserialize(mockParser, mockContext)).thenReturn("test");

        mappingIterator.readAll(resultList);

        assertEquals(1, resultList.size());
        assertEquals("test", resultList.get(0));

        verify(mockParser, times(4)).getCurrentToken();
        verify(mockParser, times(3)).nextToken();
        verify(mockDeserializer, times(2)).deserialize(mockParser, mockContext);
    }

    @Test
    public void testGetParser() {
        assertEquals(mockParser, mappingIterator.getParser());
    }

    @Test
    public void testGetParserSchema() {
        assertNotNull(mappingIterator.getParserSchema());
    }

    @Test
    public void testGetCurrentLocation() {
        JsonLocation mockLocation = mock(JsonLocation.class);
        when(mockParser.getCurrentLocation()).thenReturn(mockLocation);

        assertEquals(mockLocation, mappingIterator.getCurrentLocation());
        verify(mockParser).getCurrentLocation();
    }
}