import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MappingIteratorTest {
    
    @Mock
    private JsonParser parser;
    
    @Mock
    private JavaType type;
    
    @Mock
    private DeserializationContext context;
    
    @Mock
    private JsonDeserializer<?> deserializer;
    
    private Object valueToUpdate;
    
    private boolean managedParser;
    
    private MappingIterator<Object> mappingIterator;
    
    @Before
    public void setUp() {
        mappingIterator = new MappingIterator<>(type, parser, context, deserializer, managedParser, valueToUpdate);
    }
    
    @Test
    public void testHasNext() throws IOException {
        when(parser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        assertEquals(true, mappingIterator.hasNext());
        verify(parser).getCurrentToken();
    }
    
    @Test
    public void testNext() throws IOException {
        when(parser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        when(deserializer.deserialize(parser, context)).thenReturn("Test");
        assertEquals("Test", mappingIterator.next());
        verify(parser).getCurrentToken();
        verify(deserializer).deserialize(parser, context);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() throws IOException {
        mappingIterator.remove();
    }
    
    @Test
    public void testClose() throws IOException {
        mappingIterator.close();
        verify(parser).close();
    }
    
    @Test
    public void testHasNextValue() throws IOException {
        when(parser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        assertEquals(true, mappingIterator.hasNextValue());
        verify(parser).getCurrentToken();
    }
    
    @Test
    public void testNextValue() throws IOException {
        when(parser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        when(deserializer.deserialize(parser, context)).thenReturn("Test");
        assertEquals("Test", mappingIterator.nextValue());
        verify(parser).getCurrentToken();
        verify(deserializer).deserialize(parser, context);
    }
    
    @Test
    public void testReadAll() throws IOException {
        when(parser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT, JsonToken.END_OBJECT);
        when(deserializer.deserialize(parser, context)).thenReturn("Test");
        assertEquals("Test", mappingIterator.readAll().get(0));
        verify(parser, times(2)).getCurrentToken();
        verify(deserializer).deserialize(parser, context);
    }
    
    @Test
    public void testReadAllWithList() throws IOException {
        when(parser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT, JsonToken.END_OBJECT);
        when(deserializer.deserialize(parser, context)).thenReturn("Test");
        assertEquals("Test", mappingIterator.readAll(new ArrayList<>()).get(0));
        verify(parser, times(2)).getCurrentToken();
        verify(deserializer).deserialize(parser, context);
    }
    
    @Test
    public void testReadAllWithCollection() throws IOException {
        when(parser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT, JsonToken.END_OBJECT);
        when(deserializer.deserialize(parser, context)).thenReturn("Test");
        assertEquals("Test", mappingIterator.readAll(new HashSet<>()).iterator().next());
        verify(parser, times(2)).getCurrentToken();
        verify(deserializer).deserialize(parser, context);
    }
    
    @Test
    public void testGetParser() {
        assertEquals(parser, mappingIterator.getParser());
    }
    
    @Test
    public void testGetParserSchema() {
        FormatSchema schema = mock(FormatSchema.class);
        when(parser.getSchema()).thenReturn(schema);
        assertEquals(schema, mappingIterator.getParserSchema());
    }
    
    @Test
    public void testGetCurrentLocation() {
        JsonLocation location = mock(JsonLocation.class);
        when(parser.getCurrentLocation()).thenReturn(location);
        assertEquals(location, mappingIterator.getCurrentLocation());
    }
    
    @Test(expected = NoSuchElementException.class)
    public void testThrowNoSuchElement() {
        mappingIterator._throwNoSuchElement();
    }
    
    @Test(expected = RuntimeJsonMappingException.class)
    public void testHandleMappingException() {
        JsonMappingException exception = mock(JsonMappingException.class);
        mappingIterator._handleMappingException(exception);
    }
    
    @Test(expected = RuntimeException.class)
    public void testHandleIOException() {
        IOException exception = mock(IOException.class);
        mappingIterator._handleIOException(exception);
    }
}