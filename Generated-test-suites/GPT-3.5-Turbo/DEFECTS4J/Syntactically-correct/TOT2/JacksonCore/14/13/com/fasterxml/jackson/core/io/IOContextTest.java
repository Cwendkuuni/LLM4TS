import static org.junit.Assert.*;
import org.junit.Test;

public class IOContextTest {

    @Test
    public void testConstructTextBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        TextBuffer textBuffer = ioContext.constructTextBuffer();
        assertNotNull(textBuffer);
    }

    @Test
    public void testAllocReadIOBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        byte[] buffer = ioContext.allocReadIOBuffer();
        assertNotNull(buffer);
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        byte[] buffer = ioContext.allocWriteEncodingBuffer();
        assertNotNull(buffer);
    }

    @Test
    public void testAllocBase64Buffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        byte[] buffer = ioContext.allocBase64Buffer();
        assertNotNull(buffer);
    }

    @Test
    public void testAllocTokenBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        char[] buffer = ioContext.allocTokenBuffer();
        assertNotNull(buffer);
    }

    @Test
    public void testAllocNameCopyBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        char[] buffer = ioContext.allocNameCopyBuffer(10);
        assertNotNull(buffer);
    }
}