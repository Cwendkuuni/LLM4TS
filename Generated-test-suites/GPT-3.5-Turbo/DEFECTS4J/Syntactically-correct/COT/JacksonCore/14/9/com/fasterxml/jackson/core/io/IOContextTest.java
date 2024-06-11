
import org.junit.*;
import static org.junit.Assert.*;

public class IOContextTest {

    @Test
    public void testSetAndGetEncoding() {
        IOContext io = new IOContext(null, null, false);
        JsonEncoding encoding = JsonEncoding.UTF8;
        io.setEncoding(encoding);
        assertEquals(encoding, io.getEncoding());
    }

    @Test
    public void testWithEncoding() {
        IOContext io = new IOContext(null, null, false);
        JsonEncoding encoding = JsonEncoding.UTF8;
        IOContext newContext = io.withEncoding(encoding);
        assertEquals(encoding, newContext.getEncoding());
    }

    @Test
    public void testConstructTextBuffer() {
        IOContext io = new IOContext(null, null, false);
        TextBuffer buffer = io.constructTextBuffer();
        assertNotNull(buffer);
    }


    @Test
    public void testAllocReadIOBuffer() {
        IOContext io = new IOContext(null, null, false);
        byte[] buffer = io.allocReadIOBuffer();
        assertNotNull(buffer);
    }

    @Test
    public void testAllocReadIOBufferWithSize() {
        IOContext io = new IOContext(null, null, false);
        int minSize = 1024;
        byte[] buffer = io.allocReadIOBuffer(minSize);
        assertNotNull(buffer);
        assertTrue(buffer.length >= minSize);
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        IOContext io = new IOContext(null, null, false);
        byte[] buffer = io.allocWriteEncodingBuffer();
        assertNotNull(buffer);
    }

    @Test
    public void testAllocWriteEncodingBufferWithSize() {
        IOContext io = new IOContext(null, null, false);
        int minSize = 1024;
        byte[] buffer = io.allocWriteEncodingBuffer(minSize);
        assertNotNull(buffer);
        assertTrue(buffer.length >= minSize);
    }

    @Test
    public void testAllocBase64Buffer() {
        IOContext io = new IOContext(null, null, false);
        byte[] buffer = io.allocBase64Buffer();
        assertNotNull(buffer);
    }

    @Test
    public void testAllocTokenBuffer() {
        IOContext io = new IOContext(null, null, false);
        char[] buffer = io.allocTokenBuffer();
        assertNotNull(buffer);
    }

    @Test
    public void testAllocTokenBufferWithSize() {
        IOContext io = new IOContext(null, null, false);
        int minSize = 1024;
        char[] buffer = io.allocTokenBuffer(minSize);
        assertNotNull(buffer);
        assertTrue(buffer.length >= minSize);
    }

    @Test
    public void testAllocConcatBuffer() {
        IOContext io = new IOContext(null, null, false);
        char[] buffer = io.allocConcatBuffer();
        assertNotNull(buffer);
    }

    @Test
    public void testAllocNameCopyBuffer() {
        IOContext io = new IOContext(null, null, false);
        int minSize = 1024;
        char[] buffer = io.allocNameCopyBuffer(minSize);
        assertNotNull(buffer);
        assertTrue(buffer.length >= minSize);
    }    
    
    @Test
    public void testReleaseReadIOBuffer() {
        IOContext io = new IOContext(null, null, false);
        byte[] buffer = io.allocReadIOBuffer();
        io.releaseReadIOBuffer(buffer);
        assertNull(io._readIOBuffer);
    }

    @Test
    public void testReleaseWriteEncodingBuffer() {
        IOContext io = new IOContext(null, null, false);
        byte[] buffer = io.allocWriteEncodingBuffer();
        io.releaseWriteEncodingBuffer(buffer);
        assertNull(io._writeEncodingBuffer);
    }

    @Test
    public void testReleaseBase64Buffer() {
        IOContext io = new IOContext(null, null, false);
        byte[] buffer = io.allocBase64Buffer();
        io.releaseBase64Buffer(buffer);
        assertNull(io._base64Buffer);
    }

    @Test
    public void testReleaseTokenBuffer() {
        IOContext io = new IOContext(null, null, false);
        char[] buffer = io.allocTokenBuffer();
        io.releaseTokenBuffer(buffer);
        assertNull(io._tokenCBuffer);
    }

    @Test
    public void testReleaseConcatBuffer() {
        IOContext io = new IOContext(null, null, false);
        char[] buffer = io.allocConcatBuffer();
        io.releaseConcatBuffer(buffer);
        assertNull(io._concatCBuffer);
    }

    @Test
    public void testReleaseNameCopyBuffer() {
        IOContext io = new IOContext(null, null, false);
        char[] buffer = io.allocNameCopyBuffer(1024);
        io.releaseNameCopyBuffer(buffer);
        assertNull(io._nameCopyBuffer);
    }
}


