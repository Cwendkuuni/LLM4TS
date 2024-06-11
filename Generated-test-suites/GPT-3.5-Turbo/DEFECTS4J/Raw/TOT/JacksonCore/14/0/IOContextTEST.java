import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.util.BufferRecycler;
import com.fasterxml.jackson.core.util.TextBuffer;
import com.fasterxml.jackson.core.io.IOContext;

public class IOContextTest {

    private BufferRecycler bufferRecycler;
    private Object sourceRef;
    private boolean managedResource;

    @Before
    public void setUp() {
        bufferRecycler = new BufferRecycler();
        sourceRef = new Object();
        managedResource = false;
    }

    @Test
    public void testSetEncoding() {
        IOContext ioContext = new IOContext(bufferRecycler, sourceRef, managedResource);
        JsonEncoding encoding = JsonEncoding.UTF8;
        ioContext.setEncoding(encoding);
        assertEquals(encoding, ioContext.getEncoding());
    }

    @Test
    public void testWithEncoding() {
        IOContext ioContext = new IOContext(bufferRecycler, sourceRef, managedResource);
        JsonEncoding encoding = JsonEncoding.UTF8;
        IOContext newContext = ioContext.withEncoding(encoding);
        assertNotSame(ioContext, newContext);
        assertEquals(encoding, newContext.getEncoding());
    }

    @Test
    public void testConstructTextBuffer() {
        IOContext ioContext = new IOContext(bufferRecycler, sourceRef, managedResource);
        TextBuffer textBuffer = ioContext.constructTextBuffer();
        assertNotNull(textBuffer);
    }

    @Test
    public void testAllocReadIOBuffer() {
        IOContext ioContext = new IOContext(bufferRecycler, sourceRef, managedResource);
        byte[] buffer = ioContext.allocReadIOBuffer();
        assertNotNull(buffer);
    }

    @Test
    public void testAllocReadIOBufferWithMinSize() {
        IOContext ioContext = new IOContext(bufferRecycler, sourceRef, managedResource);
        int minSize = 1024;
        byte[] buffer = ioContext.allocReadIOBuffer(minSize);
        assertNotNull(buffer);
        assertTrue(buffer.length >= minSize);
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        IOContext ioContext = new IOContext(bufferRecycler, sourceRef, managedResource);
        byte[] buffer = ioContext.allocWriteEncodingBuffer();
        assertNotNull(buffer);
    }

    @Test
    public void testAllocWriteEncodingBufferWithMinSize() {
        IOContext ioContext = new IOContext(bufferRecycler, sourceRef, managedResource);
        int minSize = 1024;
        byte[] buffer = ioContext.allocWriteEncodingBuffer(minSize);
        assertNotNull(buffer);
        assertTrue(buffer.length >= minSize);
    }

    @Test
    public void testAllocBase64Buffer() {
        IOContext ioContext = new IOContext(bufferRecycler, sourceRef, managedResource);
        byte[] buffer = ioContext.allocBase64Buffer();
        assertNotNull(buffer);
    }

    @Test
    public void testAllocTokenBuffer() {
        IOContext ioContext = new IOContext(bufferRecycler, sourceRef, managedResource);
        char[] buffer = ioContext.allocTokenBuffer();
        assertNotNull(buffer);
    }

    @Test
    public void testAllocTokenBufferWithMinSize() {
        IOContext ioContext = new IOContext(bufferRecycler, sourceRef, managedResource);
        int minSize = 1024;
        char[] buffer = ioContext.allocTokenBuffer(minSize);
        assertNotNull(buffer);
        assertTrue(buffer.length >= minSize);
    }

    @Test
    public void testAllocConcatBuffer() {
        IOContext ioContext = new IOContext(bufferRecycler, sourceRef, managedResource);
        char[] buffer = ioContext.allocConcatBuffer();
        assertNotNull(buffer);
    }
    
    @Test
    public void testAllocNameCopyBufferWithMinSize() {
        IOContext ioContext = new IOContext(bufferRecycler, sourceRef, managedResource);
        int minSize = 1024;
        char[] buffer = ioContext.allocNameCopyBuffer(minSize);
        assertNotNull(buffer);
        assertTrue(buffer.length >= minSize);
    }

    @Test
    public void testReleaseReadIOBuffer() {
        IOContext ioContext = new IOContext(bufferRecycler, sourceRef, managedResource);
        byte[] buffer = ioContext.allocReadIOBuffer();
        ioContext.releaseReadIOBuffer(buffer);
        assertNull(ioContext._readIOBuffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseReadIOBufferWithInvalidBuffer() {
        IOContext ioContext = new IOContext(bufferRecycler, sourceRef, managedResource);
        byte[] invalidateBuffer = new byte[1024];
        ioContext.releaseReadIOBuffer(invalidateBuffer);
    }

    @Test
    public void testReleaseWriteEncodingBuffer() {
        IOContext ioContext = new IOContext(bufferRecycler, sourceRef, managedResource);
        byte[] buffer = ioContext.allocWriteEncodingBuffer();
        ioContext.releaseWriteEncodingBuffer(buffer);
        assertNull(ioContext._writeEncodingBuffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseWriteEncodingBufferWithInvalidBuffer() {
        IOContext ioContext = new IOContext(bufferRecycler, sourceRef, managedResource);
        byte[] invalidateBuffer = new byte[1024];
        ioContext.releaseWriteEncodingBuffer(invalidateBuffer);
    }

    @Test
    public void testReleaseBase64Buffer() {
        IOContext ioContext = new IOContext(bufferRecycler, sourceRef, managedResource);
        byte[] buffer = ioContext.allocBase64Buffer();
        ioContext.releaseBase64Buffer(buffer);
        assertNull(ioContext._base64Buffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseBase64BufferWithInvalidBuffer() {
        IOContext ioContext = new IOContext(bufferRecycler, sourceRef, managedResource);
        byte[] invalidateBuffer = new byte[1024];
        ioContext.releaseBase64Buffer(invalidateBuffer);
    }

    @Test
    public void testReleaseTokenBuffer() {
        IOContext ioContext = new IOContext(bufferRecycler, sourceRef, managedResource);
        char[] buffer = ioContext.allocTokenBuffer();
        ioContext.releaseTokenBuffer(buffer);
        assertNull(ioContext._tokenCBuffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseTokenBufferWithInvalidBuffer() {
        IOContext ioContext = new IOContext(bufferRecycler, sourceRef, managedResource);
        char[] invalidateBuffer = new char[1024];
        ioContext.releaseTokenBuffer(invalidateBuffer);
    }

    @Test
    public void testReleaseConcatBuffer() {
        IOContext ioContext = new IOContext(bufferRecycler, sourceRef, managedResource);
        char[] buffer = ioContext.allocConcatBuffer();
        ioContext.releaseConcatBuffer(buffer);
        assertNull(ioContext._concatCBuffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseConcatBufferWithInvalidBuffer() {
        IOContext ioContext = new IOContext(bufferRecycler, sourceRef, managedResource);
        char[] invalidateBuffer = new char[1024];
        ioContext.releaseConcatBuffer(invalidateBuffer);
    }

    @Test
    public void testReleaseNameCopyBuffer() {
        IOContext ioContext = new IOContext(bufferRecycler, sourceRef, managedResource);
        char[] buffer = ioContext.allocNameCopyBuffer(1024);
        ioContext.releaseNameCopyBuffer(buffer);
        assertNull(ioContext._nameCopyBuffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseNameCopyBufferWithInvalidBuffer() {
        IOContext ioContext = new IOContext(bufferRecycler, sourceRef, managedResource);
        char[] invalidateBuffer = new char[1024];
        ioContext.releaseNameCopyBuffer(invalidateBuffer);
    }

}