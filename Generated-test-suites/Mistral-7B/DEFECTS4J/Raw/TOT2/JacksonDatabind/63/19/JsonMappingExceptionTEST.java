import org.junit.Assert;
import org.junit.Test;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonProcessingException;

public class JsonMappingExceptionTest {

    @Test
    public void testJsonMappingExceptionConstruction() {
        // Test default constructor
        JsonMappingException e = new JsonMappingException();
        Assert.assertEquals("", e.getMessage());
        Assert.assertNull(e.getCause());
        Assert.assertNull(e.getLocation());

        // Test constructor with message
        String msg = "Test message";
        JsonMappingException e1 = new JsonMappingException(msg);
        Assert.assertEquals(msg, e1.getMessage());
        Assert.assertNull(e1.getCause());
        Assert.assertNull(e1.getLocation());

        // Test constructor with message and cause
        Throwable cause = new Exception("Cause message");
        JsonMappingException e2 = new JsonMappingException(msg, cause);
        Assert.assertEquals(msg, e2.getMessage());
        Assert.assertEquals(cause, e2.getCause());
        Assert.assertNull(e2.getLocation());

        // Test constructor with message, cause and location
        JsonParser parser = new JsonTestParser();
        JsonMappingException e3 = new JsonMappingException(parser, msg, cause);
        Assert.assertEquals(msg, e3.getMessage());
        Assert.assertEquals(cause, e3.getCause());
        Assert.assertEquals(parser.getCurrentLocation(), e3.getLocation());
    }

    @Test
    public void testJsonMappingExceptionPath() {
        Object referrer = new Object();
        JsonMappingException e = new JsonMappingException(referrer, "Test message");
        e.prependPath(referrer, "fieldName");
        List<Reference> path = e.getPath();
        Assert.assertEquals(1, path.size());
        Assert.assertEquals(referrer, path.get(0).getFrom());
        Assert.assertEquals("fieldName", path.get(0).getFieldName());
    }

    @Test
    public void testJsonMappingExceptionFrom() {
        IOException ioException = new IOException("Test IO exception message");
        JsonMappingException e = JsonMappingException.fromUnexpectedIOE(ioException);
        Assert.assertEquals("Unexpected IOException (of type java.io.IOException): Test IO exception message", e.getMessage());
        Assert.assertEquals(ioException, e.getCause());
        Assert.assertNull(e.getLocation());
    }
}