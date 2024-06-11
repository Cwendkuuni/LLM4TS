import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JsonMappingExceptionTest {

    @Test
    public void testDefaultConstructor() {
        JsonMappingException exception = new JsonMappingException("Default constructor test");
        assertEquals("Default constructor test", exception.getMessage());
        assertNotNull(exception.getPath());
    }

    @Test
    public void testConstructorWithMessage() {
        Closeable processor = new ByteArrayInputStream(new byte[0]);
        JsonMappingException exception = new JsonMappingException(processor, "Constructor with message test");
        assertEquals("Constructor with message test", exception.getMessage());
        assertNotNull(exception.getPath());
    }

    @Test
    public void testConstructorWithMessageAndLocation() {
        Closeable processor = new ByteArrayInputStream(new byte[0]);
        JsonMappingException exception = new JsonMappingException(processor, "Constructor with message and location test", null);
        assertEquals("Constructor with message and location test", exception.getMessage());
        assertNotNull(exception.getPath());
        assertNotNull(exception.getLocation());
    }

    @Test
    public void testConstructorWithMessageAndLocationAndRootCause() {
        Closeable processor = new ByteArrayInputStream(new byte[0]);
        JsonMappingException exception = new JsonMappingException(processor, "Constructor with message, location and root cause test", new IllegalArgumentException("Root cause test"));
        assertEquals("Constructor with message, location and root cause test", exception.getMessage());
        assertNotNull(exception.getPath());
        assertNotNull(exception.getLocation());
        assertEquals("Root cause test", exception.getCause().getMessage());
    }

    @Test(expected = JsonMappingException.class)
    public void testThrowJsonMappingExceptionFromParser() {
        Closeable processor = new ByteArrayInputStream(new byte[0]);
        JsonParser parser = new ObjectMapper().readerForValueType(JsonMappingException.class).readValue(processor);
        JsonMappingException exception = JsonMappingException.from(parser, "Thrown from parser test");
        assertEquals("Thrown from parser test", exception.getMessage());
        assertNotNull(exception.getPath());
    }

    @Test(expected = JsonMappingException.class)
    public void testThrowJsonMappingExceptionFromParserWithRootCause() {
        Closeable processor = new ByteArrayInputStream(new byte[0]);
        JsonParser parser = new ObjectMapper().readerForValueType(JsonMappingException.class).readValue(processor);
        JsonMappingException exception = JsonMappingException.from(parser, "Thrown from parser with root cause test", new IllegalArgumentException("Root cause test"));
        assertEquals("Thrown from parser with root cause test", exception.getMessage());
        assertNotNull(exception.getPath());
        assertEquals("Root cause test", exception.getCause().getMessage());
    }

    @Test(expected = JsonMappingException.class)
    public void testThrowJsonMappingExceptionFromGenerator() {
        Closeable generator = new ByteArrayOutputStream();
        JsonMappingException exception = JsonMappingException.from((JsonGenerator) generator, "Thrown from generator test");
        assertEquals("Thrown from generator test", exception.getMessage());
        assertNotNull(exception.getPath());
    }

    @Test
    public void testWrapWithPath() {
        Object ref = new Object();
        Throwable cause = new IllegalArgumentException("Cause test");
        JsonMappingException exception = JsonMappingException.wrapWithPath(cause, ref, "Field test");
        assertEquals("Cause test (through reference chain: Object(Field test))", exception.getMessage());
        assertNotNull(exception.getPath());
        Reference ref0 = exception.getPath().get(0);
        assertEquals(ref, ref0.getFrom());
        assertEquals("Field test", ref0.getFieldName());
    }

    @Test
    public void testWrapWithPathWithIndex() {
        Object[] refs = new Object[2];
        refs[0] = new Object();
        refs[1] = new Object[2];
        Throwable cause = new IllegalArgumentException("Cause test");
        JsonMappingException exception = JsonMappingException.wrapWithPath(cause, refs, 1);
        assertEquals("Cause test (through reference chain: Object[1](index: 1))", exception.getMessage());
        assertNotNull(exception.getPath());
        Reference ref0 = exception.getPath().get(0);
        assertEquals(refs[0], ref0.getFrom());
        assertEquals(0, ref0.getIndex());
        Reference ref1 = exception.getPath().get(1);
        assertEquals(refs[1], ref1.getFrom());
        assertEquals(1, ref1.getIndex());
    }
}