import org.junit.Test;

public class IOContextTest {

    @Test
    public void testSetEncoding() {
        // Create an instance of IOContext
        IOContext ioContext = new IOContext(null, this, false);

        // Set the encoding
        ioContext.setEncoding(JsonEncoding.UTF8);

        // Verify that the encoding is set correctly
        assertEquals(JsonEncoding.UTF8, ioContext.getEncoding());
    }
}