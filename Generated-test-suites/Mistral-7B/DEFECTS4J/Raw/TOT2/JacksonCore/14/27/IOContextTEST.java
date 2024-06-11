import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.BufferRecycler;

public class IOContextTest {
    IOContext context;
    BufferRecycler bufferRecycler;
    Object sourceRef;

    @Before
    public void setUp() {
        bufferRecycler = new BufferRecycler();
        sourceRef = new Object();
        context = new IOContext(bufferRecycler, sourceRef, false);
    }

    @Test
    public void testGetSourceReference() {
        assertEquals(sourceRef, context.getSourceReference());
    }

    @Test
    public void testSetEncoding() {
        JsonEncoding encoding = JsonEncoding.UTF8;
        context.setEncoding(encoding);
        assertEquals(encoding, context.getEncoding());
    }

    @Test
    public void testAllocReadIOBuffer() {
        byte[] readIOBuffer = context.allocReadIOBuffer();
        assertNotNull(readIOBuffer);
        context.releaseReadIOBuffer(readIOBuffer);
    }

    @Test(expected = IllegalStateException.class)
    public void testAllocReadIOBufferTwice() {
        byte[] readIOBuffer = context.allocReadIOBuffer();
        context.allocReadIOBuffer();
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        byte[] writeEncodingBuffer = context.allocWriteEncodingBuffer();
        assertNotNull(writeEncodingBuffer);
        context.releaseWriteEncodingBuffer(writeEncodingBuffer);
    }

    @Test(expected = IllegalStateException.class)
    public void testAllocWriteEncodingBufferTwice() {
        byte[] writeEncodingBuffer = context.allocWriteEncodingBuffer();
        context.allocWriteEncodingBuffer();
    }
}