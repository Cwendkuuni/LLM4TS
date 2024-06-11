import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.io.IOContext;

public class IOContextTest {

    @Test
    public void testIOContextConstruction() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        assertNotNull(ioContext);
        assertSame(new Object(), ioContext.getSourceReference());
        assertTrue(ioContext.isResourceManaged());
    }

    @Test
    public void testAllocReadIOBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        assertNotNull(readIOBuffer);
        ioContext.releaseReadIOBuffer(readIOBuffer);
    }

    @Test(expected = IllegalStateException.class)
    public void testAllocReadIOBufferTwice() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        ioContext.allocReadIOBuffer(); // should throw an exception
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        assertNotNull(writeEncodingBuffer);
        ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
    }

    @Test(expected = IllegalStateException.class)
    public void testAllocWriteEncodingBufferTwice() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        ioContext.allocWriteEncodingBuffer(); // should throw an exception
    }

    // Add other test cases for methods: setEncoding, getEncoding, constructTextBuffer, etc.
    // Add test cases for edge cases and exceptions: null arguments, out-of-bounds sizes, etc.
}