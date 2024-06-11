import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.io.IOContext;

public class IOContextTest {
    @Test
    public void testIOContextConstructor() {
        IOContext ioContext = new IOContext(null, null, false);
        assertNull(ioContext.getSourceReference());
        assertNull(ioContext.getEncoding());
        assertFalse(ioContext.isResourceManaged());
    }

    @Test
    public void testAllocReadIOBuffer() {
        IOContext ioContext = new IOContext(null, null, false);
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        assertNotNull(readIOBuffer);
        ioContext.releaseReadIOBuffer(readIOBuffer);
    }

    @Test(expected = IllegalStateException.class)
    public void testAllocReadIOBufferTwice() {
        IOContext ioContext = new IOContext(null, null, false);
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        ioContext.allocReadIOBuffer();
    }

    // Add more test cases for other methods and edge cases here
}