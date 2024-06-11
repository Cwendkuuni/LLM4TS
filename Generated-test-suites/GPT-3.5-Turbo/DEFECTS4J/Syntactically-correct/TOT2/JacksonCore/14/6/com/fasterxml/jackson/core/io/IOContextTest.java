import static org.junit.Assert.*;
import org.junit.Test;

public class IOContextTest {

    @Test
    public void testSetEncoding() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        ioContext.setEncoding(JsonEncoding.UTF8);
        assertEquals(JsonEncoding.UTF8, ioContext.getEncoding());
    }

    @Test
    public void testWithEncoding() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        IOContext updatedContext = ioContext.withEncoding(JsonEncoding.UTF16);
        assertEquals(JsonEncoding.UTF16, updatedContext.getEncoding());
    }

    // Add more test cases for other methods in the IOContext class

}