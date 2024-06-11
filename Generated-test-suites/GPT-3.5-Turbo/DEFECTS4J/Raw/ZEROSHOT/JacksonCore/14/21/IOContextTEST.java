import org.junit.*;
import static org.junit.Assert.*;

public class IOContextTest {

    @Test
    public void testSetEncoding() {
      BufferRecycler bufferRecycler = new BufferRecycler();
      IOContext ioContext = new IOContext(bufferRecycler, "source", true);
      ioContext.setEncoding(JsonEncoding.UTF8);
      assertEquals(JsonEncoding.UTF8, ioContext.getEncoding());
    }
    
    @Test
    public void testWithEncoding() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, "source", true);
        IOContext newContext = ioContext.withEncoding(JsonEncoding.UTF8);
        assertEquals(JsonEncoding.UTF8, newContext.getEncoding());
        assertNotSame(ioContext, newContext);
    }
    
    @Test
    public void testGetSourceReference() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        Object sourceRef = new Object();
        IOContext ioContext = new IOContext(bufferRecycler, sourceRef, true);
        assertEquals(sourceRef, ioContext.getSourceReference());
    }
    
    @Test
    public void testIsResourceManaged() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, "source", true);
        assertTrue(ioContext.isResourceManaged());
    }
    
    @Test
    public void testConstructTextBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, "source", true);
        TextBuffer textBuffer = ioContext.constructTextBuffer();
        assertNotNull(textBuffer);
        assertSame(bufferRecycler, textBuffer.getBufferRecycler());
    }
    
    @Test
    public void testAllocReadIOBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, "source", true);
        byte[] buffer = ioContext.allocReadIOBuffer();
        assertNotNull(buffer);
        assertSame(bufferRecycler, ioContext._bufferRecycler);
    }
    
    @Test
    public void testAllocReadIOBufferWithSize() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, "source", true);
        int minSize = 512;
        byte[] buffer = ioContext.allocReadIOBuffer(minSize);
        assertNotNull(buffer);
        assertEquals(minSize, buffer.length);
        assertSame(bufferRecycler, ioContext._bufferRecycler);
    }
    
    @Test
    public void testAllocWriteEncodingBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, "source", true);
        byte[] buffer = ioContext.allocWriteEncodingBuffer();
        assertNotNull(buffer);
        assertSame(bufferRecycler, ioContext._bufferRecycler);
    }
    
    @Test
    public void testAllocWriteEncodingBufferWithSize() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, "source", true);
        int minSize = 512;
        byte[] buffer = ioContext.allocWriteEncodingBuffer(minSize);
        assertNotNull(buffer);
        assertEquals(minSize, buffer.length);
        assertSame(bufferRecycler, ioContext._bufferRecycler);
    }
    
    @Test
    public void testAllocBase64Buffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, "source", true);
        byte[] buffer = ioContext.allocBase64Buffer();
        assertNotNull(buffer);
        assertSame(bufferRecycler, ioContext._bufferRecycler);
    }
    
    @Test
    public void testAllocTokenBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, "source", true);
        char[] buffer = ioContext.allocTokenBuffer();
        assertNotNull(buffer);
        assertSame(bufferRecycler, ioContext._bufferRecycler);
    }
    
    @Test
    public void testAllocTokenBufferWithSize() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, "source", true);
        int minSize = 512;
        char[] buffer = ioContext.allocTokenBuffer(minSize);
        assertNotNull(buffer);
        assertEquals(minSize, buffer.length);
        assertSame(bufferRecycler, ioContext._bufferRecycler);
    }
    
    @Test
    public void testAllocConcatBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, "source", true);
        char[] buffer = ioContext.allocConcatBuffer();
        assertNotNull(buffer);
        assertSame(bufferRecycler, ioContext._bufferRecycler);
    }
    
    @Test
    public void testAllocNameCopyBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, "source", true);
        int minSize = 512;
        char[] buffer = ioContext.allocNameCopyBuffer(minSize);
        assertNotNull(buffer);
        assertEquals(minSize, buffer.length);
        assertSame(bufferRecycler, ioContext._bufferRecycler);
    }
    
    @Test
    public void testReleaseReadIOBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, "source", true);
        byte[] buffer = ioContext.allocReadIOBuffer();
        ioContext.releaseReadIOBuffer(buffer);
        assertNull(ioContext._readIOBuffer);
    }
    
    @Test
    public void testReleaseWriteEncodingBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, "source", true);
        byte[] buffer = ioContext.allocWriteEncodingBuffer();
        ioContext.releaseWriteEncodingBuffer(buffer);
        assertNull(ioContext._writeEncodingBuffer);
    }
    
    @Test
    public void testReleaseBase64Buffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, "source", true);
        byte[] buffer = ioContext.allocBase64Buffer();
        ioContext.releaseBase64Buffer(buffer);
        assertNull(ioContext._base64Buffer);
    }
    
    @Test
    public void testReleaseTokenBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, "source", true);
        char[] buffer = ioContext.allocTokenBuffer();
        ioContext.releaseTokenBuffer(buffer);
        assertNull(ioContext._tokenCBuffer);
    }
    
    @Test
    public void testReleaseConcatBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, "source", true);
        char[] buffer = ioContext.allocConcatBuffer();
        ioContext.releaseConcatBuffer(buffer);
        assertNull(ioContext._concatCBuffer);
    }
    
    @Test
    public void testReleaseNameCopyBuffer() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(bufferRecycler, "source", true);
        int minSize = 512;
        char[] buffer = ioContext.allocNameCopyBuffer(minSize);
        ioContext.releaseNameCopyBuffer(buffer);
        assertNull(ioContext._nameCopyBuffer);
    }
}