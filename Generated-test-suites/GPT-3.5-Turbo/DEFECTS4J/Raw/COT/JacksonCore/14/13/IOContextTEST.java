
import org.junit.Test;
import static org.junit.Assert.*;

public class IOContextTest {

    @Test
    public void testSetEncoding() {
        IOContext ioContext = new IOContext(null, null, false);
        JsonEncoding encoding = JsonEncoding.UTF8;
        ioContext.setEncoding(encoding);
        assertEquals(encoding, ioContext.getEncoding());
    }

    @Test
    public void testWithEncoding() {
        IOContext ioContext = new IOContext(null, null, false);
        JsonEncoding encoding = JsonEncoding.UTF8;
        IOContext newContext = ioContext.withEncoding(encoding);
        assertNotSame(ioContext, newContext);
        assertEquals(encoding, newContext.getEncoding());
    }

    @Test
    public void testGetSourceReference() {
        IOContext ioContext = new IOContext(null, "sourceRef", false);
        assertEquals("sourceRef", ioContext.getSourceReference());
    }

    @Test
    public void testIsResourceManaged() {
        IOContext ioContext = new IOContext(null, null, true);
        assertTrue(ioContext.isResourceManaged());

        ioContext = new IOContext(null, null, false);
        assertFalse(ioContext.isResourceManaged());
    }

    @Test
    public void testConstructTextBuffer() {
        IOContext ioContext = new IOContext(null, null, false);
        TextBuffer textBuffer = ioContext.constructTextBuffer();
        assertNotNull(textBuffer);
    }

    @Test
    public void testAllocReadIOBuffer() {
        IOContext ioContext = new IOContext(null, null, false);
        byte[] buffer = ioContext.allocReadIOBuffer();
        assertNotNull(buffer);
    }

    @Test
    public void testAllocReadIOBufferWithMinSize() {
        IOContext ioContext = new IOContext(null, null, false);
        byte[] buffer = ioContext.allocReadIOBuffer(100);
        assertNotNull(buffer);
        assertTrue(buffer.length >= 100);
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        IOContext ioContext = new IOContext(null, null, false);
        byte[] buffer = ioContext.allocWriteEncodingBuffer();
        assertNotNull(buffer);
    }

    @Test
    public void testAllocWriteEncodingBufferWithMinSize() {
        IOContext ioContext = new IOContext(null, null, false);
        byte[] buffer = ioContext.allocWriteEncodingBuffer(100);
        assertNotNull(buffer);
        assertTrue(buffer.length >= 100);
    }

    @Test
    public void testAllocBase64Buffer() {
        IOContext ioContext = new IOContext(null, null, false);
        byte[] buffer = ioContext.allocBase64Buffer();
        assertNotNull(buffer);
    }

    @Test
    public void testAllocTokenBuffer() {
        IOContext ioContext = new IOContext(null, null, false);
        char[] buffer = ioContext.allocTokenBuffer();
        assertNotNull(buffer);
    }

    @Test
    public void testAllocTokenBufferWithMinSize() {
        IOContext ioContext = new IOContext(null, null, false);
        char[] buffer = ioContext.allocTokenBuffer(100);
        assertNotNull(buffer);
        assertTrue(buffer.length >= 100);
    }

    @Test
    public void testAllocConcatBuffer() {
        IOContext ioContext = new IOContext(null, null, false);
        char[] buffer = ioContext.allocConcatBuffer();
        assertNotNull(buffer);
    }

    @Test
    public void testAllocNameCopyBuffer() {
        IOContext ioContext = new IOContext(null, null, false);
        char[] buffer = ioContext.allocNameCopyBuffer(100);
        assertNotNull(buffer);
        assertTrue(buffer.length >= 100);
    }

    @Test
    public void testReleaseReadIOBuffer() {
        IOContext ioContext = new IOContext(null, null, false);
        byte[] buffer = ioContext.allocReadIOBuffer();
        ioContext.releaseReadIOBuffer(buffer);
    }

    @Test
    public void testReleaseWriteEncodingBuffer() {
        IOContext ioContext = new IOContext(null, null, false);
        byte[] buffer = ioContext.allocWriteEncodingBuffer();
        ioContext.releaseWriteEncodingBuffer(buffer);
    }

    @Test
    public void testReleaseBase64Buffer() {
        IOContext ioContext = new IOContext(null, null, false);
        byte[] buffer = ioContext.allocBase64Buffer();
        ioContext.releaseBase64Buffer(buffer);
    }

    @Test
    public void testReleaseTokenBuffer() {
        IOContext ioContext = new IOContext(null, null, false);
        char[] buffer = ioContext.allocTokenBuffer();
        ioContext.releaseTokenBuffer(buffer);
    }

    @Test
    public void testReleaseConcatBuffer() {
        IOContext ioContext = new IOContext(null, null, false);
        char[] buffer = ioContext.allocConcatBuffer();
        ioContext.releaseConcatBuffer(buffer);
    }

    @Test
    public void testReleaseNameCopyBuffer() {
        IOContext ioContext = new IOContext(null, null, false);
        char[] buffer = ioContext.allocNameCopyBuffer(100);
        ioContext.releaseNameCopyBuffer(buffer);
    }
}

