
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParseException;

public class JsonMappingExceptionTest {

    @Test
    public void testJsonMappingException() {
        JsonMappingException exception = new JsonMappingException("Test Exception");
        String expectedMessage = "Test Exception (through reference chain: UNKNOWN)";
        Assert.assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void testWrapWithPath() {
        JsonParseException parseException = new JsonParseException(null, "");
        JsonMappingException exception = JsonMappingException.wrapWithPath(parseException, "Test Object", "Test Field");
        String expectedMessage = "(through reference chain: Test Object[\"Test Field\"])";
        Assert.assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void testPrependPath() {
        JsonMappingException exception = new JsonMappingException("Test Exception");
        exception.prependPath("Object", "Field");
        String expectedMessage = "Test Exception (through reference chain: Object[\"Field\"])";
        Assert.assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void testGetPath() {
        JsonMappingException exception = new JsonMappingException("Test Exception");
        exception.prependPath("Object", "Field");
        Assert.assertEquals("Object[\"Field\"]", exception.getPath().get(0).getDescription());
    }
}
