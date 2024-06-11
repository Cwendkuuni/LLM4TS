import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class IOContextTest {

    private IOContext ioContext;

    @Before
    public void setUp() {
        ioContext = new IOContext(null, null, false);
    }

    @Test
    public void testConstructTextBuffer() {
        TextBuffer buffer = ioContext.constructTextBuffer();
        
        assertNotNull(buffer);
    }

    @Test
    public void testAllocReadIOBuffer() {
        byte[] buffer = ioContext.allocReadIOBuffer();

        assertNotNull(buffer);
    }

    @Test
    public void testAllocReadIOBufferWithSize() {
        byte[] buffer = ioContext.allocReadIOBuffer(10);

        assertNotNull(buffer);
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        byte[] buffer = ioContext.allocWriteEncodingBuffer();

        assertNotNull(buffer);
    }

    @Test
    public void testAllocWriteEncodingBufferWithSize() {
        byte[] buffer = ioContext.allocWriteEncodingBuffer(10);

        assertNotNull(buffer);
    }

    @Test
    public void testAllocBase64Buffer() {
        byte[] buffer = ioContext.allocBase64Buffer();

        assertNotNull(buffer);
    }

    @Test
    public void testAllocTokenBuffer() {
        char[] buffer = ioContext.allocTokenBuffer();

        assertNotNull(buffer);
    }

    @Test
    public void testAllocTokenBufferWithSize() {
        char[] buffer = ioContext.allocTokenBuffer(10);

        assertNotNull(buffer);
    }

    @Test
    public void testAllocConcatBuffer() {
        char[] buffer = ioContext.allocConcatBuffer();

        assertNotNull(buffer);
    }

    @Test
    public void testAllocNameCopyBuffer() {
        char[] buffer = ioContext.allocNameCopyBuffer(10);

        assertNotNull(buffer);
    }

    @Test
    public void testSetEncoding() {
        ioContext.setEncoding(JsonEncoding.UTF8);

        assertEquals(JsonEncoding.UTF8, ioContext.getEncoding());
    }

    @Test
    public void testWithEncoding() {
        IOContext newIOContext = ioContext.withEncoding(JsonEncoding.UTF16_LE);

        assertEquals(JsonEncoding.UTF16_LE, newIOContext.getEncoding());
    }

    @Test
    public void testGetSourceReference() {
        Object sourceRef = new Object();

        IOContext ioContext = new IOContext(null, sourceRef, false);

        assertEquals(sourceRef, ioContext.getSourceReference());
    }

    @Test
    public void testIsResourceManaged() {
        IOContext ioContext = new IOContext(null, null, true);

        assertTrue(ioContext.isResourceManaged());
    }

    @Test
    public void testReleaseReadIOBuffer() {
        byte[] buffer = ioContext.allocReadIOBuffer();

        ioContext.releaseReadIOBuffer(buffer);

        assertNull(ioContext._readIOBuffer);
    }

    @Test
    public void testReleaseWriteEncodingBuffer() {
        byte[] buffer = ioContext.allocWriteEncodingBuffer();

        ioContext.releaseWriteEncodingBuffer(buffer);

        assertNull(ioContext._writeEncodingBuffer);
    }

    @Test
    public void testReleaseBase64Buffer() {
        byte[] buffer = ioContext.allocBase64Buffer();

        ioContext.releaseBase64Buffer(buffer);

        assertNull(ioContext._base64Buffer);
    }

    @Test
    public void testReleaseTokenBuffer() {
        char[] buffer = ioContext.allocTokenBuffer();

        ioContext.releaseTokenBuffer(buffer);

        assertNull(ioContext._tokenCBuffer);
    }

    @Test
    public void testReleaseConcatBuffer() {
        char[] buffer = ioContext.allocConcatBuffer();

        ioContext.releaseConcatBuffer(buffer);

        assertNull(ioContext._concatCBuffer);
    }

    @Test
    public void testReleaseNameCopyBuffer() {
        char[] buffer = ioContext.allocNameCopyBuffer(10);

        ioContext.releaseNameCopyBuffer(buffer);

        assertNull(ioContext._nameCopyBuffer);
    }
}