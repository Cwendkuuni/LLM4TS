import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.json.*;
import com.fasterxml.jackson.databind.node.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;

public class JsonMappingExceptionTest {

    private ObjectMapper objectMapper;
    private JsonParser jsonParser;
    private JsonGenerator jsonGenerator;

    @SuppressWarnings("unchecked")
    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        jsonParser = objectMapper.getFactory().createParser("{}");
        jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDefaultConstructor() {
        JsonMappingException exception = new JsonMappingException();
        assertNull(exception.getFrom());
        assertNull(exception.getPath());
        assertNotNull(exception.getMessage());
    }

    @Test
    public void testJsonMappingExceptionWithMessage() {
        String message = "Test message";
        JsonMappingException exception = new JsonMappingException(message);
        assertEquals(message, exception.getMessage());
        assertNull(exception.getFrom());
        assertNull(exception.getPath());
    }

    @Test
    public void testJsonMappingExceptionWithMessageAndThrowable() {
        String message = "Test message";
        Throwable rootCause = new IOException("Root cause");
        JsonMappingException exception = new JsonMappingException(message, rootCause);
        assertEquals(message, exception.getMessage());
        assertEquals(rootCause, exception.getRootCause());
        assertNull(exception.getFrom());
        assertNull(exception.getPath());
    }

    @Test
    public void testJsonMappingExceptionWithJsonLocation() {
        JsonLocation jsonLocation = new JsonLocation(1, 1, "source");
        String message = "Test message";
        JsonMappingException exception = new JsonMappingException(jsonLocation, message);
        assertEquals(message, exception.getMessage());
        assertEquals(jsonLocation, exception.getLocation());
        assertNull(exception.getFrom());
        assertNull(exception.getPath());
    }

    @Test
    public void testJsonMappingExceptionWithJsonLocationAndThrowable() {
        JsonLocation jsonLocation = new JsonLocation(1, 1, "source");
        String message = "Test message";
        Throwable rootCause = new IOException("Root cause");
        JsonMappingException exception = new JsonMappingException(jsonLocation, message, rootCause);
        assertEquals(message, exception.getMessage());
        assertEquals(jsonLocation, exception.getLocation());
        assertEquals(rootCause, exception.getRootCause());
        assertNull(exception.getFrom());
        assertNull(exception.getPath());
    }

    @Test
    public void testJsonMappingExceptionWithJsonParser() {
        String message = "Test message";
        JsonMappingException exception = JsonMappingException.from(jsonParser, message);
        assertEquals(message, exception.getMessage());
        assertNotNull(exception.getFrom());
        assertNull(exception.getPath());
        verify(jsonParser, times(1)).close();
    }

    @Test
    public void testJsonMappingExceptionWithJsonParserAndThrowable() {
        String message = "Test message";
        Throwable rootCause = new IOException("Root cause");
        JsonMappingException exception = JsonMappingException.from(jsonParser, message, rootCause);
        assertEquals(message, exception.getMessage());
        assertEquals(rootCause, exception.getRootCause());
        assertNotNull(exception.getFrom());
        assertNull(exception.getPath());
        verify(jsonParser, times(1)).close();
    }

    @Test
    public void testJsonMappingExceptionWithJsonGenerator() {
        String message = "Test message";
        JsonMappingException exception = JsonMappingException.from(jsonGenerator, message);
        assertEquals(message, exception.getMessage());
        assertNotNull(exception.getFrom());
        assertNull(exception.getPath());
        verify(jsonGenerator, times(1)).close();
    }

    @Test
    public void testJsonMappingExceptionWithJsonGeneratorAndThrowable() {
        String message = "Test message";
        Throwable rootCause = new IOException("Root cause");
        JsonMappingException exception = JsonMappingException.from(jsonGenerator, message, rootCause);
        assertEquals(message, exception.getMessage());
        assertEquals(rootCause, exception.getRootCause());
        assertNotNull(exception.getFrom());
        assertNull(exception.getPath());
        verify(jsonGenerator, times(1)).close();
    }

    @Test
    public void testJsonMappingExceptionWithDeserializationContext() {
        String message = "Test message";
        JsonMappingException exception = JsonMappingException.from(mock(DeserializationContext.class), message);
        assertEquals(message, exception.getMessage());
        assertNull(exception.getFrom());
        assertNull(exception.getPath());
    }

    @Test
    public void testJsonMappingExceptionWithDeserializationContextAndThrowable() {
        String message = "Test message";
        Throwable rootCause = new IOException("Root cause");
        JsonMappingException exception =
                JsonMappingException.from(mock(DeserializationContext.class), message, rootCause);
        assertEquals(message, exception.getMessage());
        assertEquals(rootCause, exception.getRootCause());
        assertNull(exception.getFrom());
        assertNull(exception.getPath());
    }

    @Test
    public void testJsonMappingExceptionWithSerializerProvider() {
        String message = "Test message";
        JsonMappingException exception = JsonMappingException.from(mock(SerializerProvider.class), message);
        assertEquals(message, exception.getMessage());
        assertNotNull(exception.getFrom());
        assertNull(exception.getPath());
        verifyNoMoreInteractions(mock(SerializerProvider.class));
    }

    @Test
    public void testJsonMappingExceptionWithSerializerProviderAndThrowable() {
        String message = "Test message";
        Throwable rootCause = new IOException("Root cause");
        JsonMappingException exception =
                JsonMappingException.from(mock(SerializerProvider.class), message, rootCause);
        assertEquals(message, exception.getMessage());
        assertEquals(rootCause, exception.getRootCause());
        assertNotNull(exception.getFrom());
        assertNull(exception.getPath());
        verifyNoMoreInteractions(mock(SerializerProvider.class));
    }

    @Test
    public void testFromUnexpectedIOE() {
        IOException ioException = mock(IOException.class);
        JsonMappingException jsonMappingException = JsonMappingException.fromUnexpectedIOE(ioException);
        String expectedMessage =
                String.format("Unexpected IOException (of type %s): %s", ioException.getClass().getName(),
                        ioException.getMessage());
        assertEquals(expectedMessage, jsonMappingException.getMessage());
        assertNull(jsonMappingException.getFrom());
        assertNull(jsonMappingException.getPath());
        verify(ioException, times(1)).printStackTrace();
    }

    @Test
    public void testWrapWithPath() {
        Object referrer = new Object();
        String fieldName = "fieldName";
        int index = 0;
        Object reference = new Object();
        JsonMappingException jsonMappingException = new JsonMappingException();

        // Test with non-indexed object
        Reference ref1 = new Reference(referrer, fieldName);
        JsonMappingException ref1Wrapped = JsonMappingException.wrapWithPath(jsonMappingException, ref1);
        List<Reference> path = ref1Wrapped.getPath();
        assertEquals(1, path.size());
        assertEquals(ref1, path.get(0));

        // Test with indexed object
        Reference ref2 = new Reference(referrer, index);
        JsonMappingException ref2Wrapped = JsonMappingException.wrapWithPath(jsonMappingException, ref2);
        List<Reference> ref2Path = ref2Wrapped.getPath();
        assertEquals(1, ref2Path.size());
        assertEquals(ref2, ref2Path.get(0));

        // Test with the same reference but different methods
        Reference ref3 = new Reference(reference, fieldName);
        JsonMappingException ref3Wrapped = JsonMappingException.wrapWithPath(jsonMappingException, ref3);
        List<Reference> ref3Path = ref3Wrapped.getPath();
        assertEquals(1, ref3Path.size());
        assertEquals(ref3, ref3Path.get(0));

        Reference ref4 = new Reference(reference, index);
        JsonMappingException ref4Wrapped = JsonMappingException.wrapWithPath(jsonMappingException, ref4);
        List<Reference> ref4Path = ref4Wrapped.getPath();
        assertEquals(1, ref4Path.size());
        assertEquals(ref4, ref4Path.get(0));

        // Test with an empty path (no reference)
        JsonMappingException emptyPathWrapped = JsonMappingException.wrapWithPath(jsonMappingException);
        List<Reference> emptyPath = jsonMappingException.getPath();
        assertEquals(0, emptyPath.size());

        // Test with a non-empty path that contains multiple references
        LinkedList<Reference> pathToTest = new LinkedList<>();
        pathToTest.add(new Reference(referrer, fieldName));
        pathToTest.add(new Reference(reference, "innerFieldName"));
        JsonMappingException multipleRefsWrapped = JsonMappingException.wrapWithPath(jsonMappingException, pathToTest);
        List<Reference> multipleRefsPath = multipleRefsWrapped.getPath();
        assertEquals(2, multipleRefsPath.size());
        assertEquals(ref1, multipleRefsPath.get(0));
        assertEquals(ref2, multipleRefsPath.get(1));
    }
}