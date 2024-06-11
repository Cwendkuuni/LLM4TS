import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class IOContextTest {

    private IOContext ioContext;

    @Before
    public void setUp() {
        // Create a new instance of IOContext for each test case
        ioContext = new IOContext(new BufferRecycler(), new Object(), true);
    }

    @Test
    public void testSetEncoding() {
        JsonEncoding encoding = JsonEncoding.UTF8;
        ioContext.setEncoding(encoding);
        assertNotNull(ioContext.getEncoding());
        assertEquals(encoding, ioContext.getEncoding());
    }

    @Test
    public void testWithEncoding() {
        JsonEncoding encoding = JsonEncoding.UTF8;
        IOContext updatedContext = ioContext.withEncoding(encoding);
        assertNotNull(updatedContext);
        assertNotNull(updatedContext.getEncoding());
        assertEquals(encoding, updatedContext.getEncoding());
    }

    @Test
    public void testGetSourceReference() {
        Object sourceRef = ioContext.getSourceReference();
        assertNotNull(sourceRef);
    }

    @Test
    public void testIsResourceManaged() {
        boolean isManaged = ioContext.isResourceManaged();
        assertTrue(isManaged);
    }

    @Test
    public void testConstructTextBuffer() {
        TextBuffer textBuffer = ioContext.constructTextBuffer();
        assertNotNull(textBuffer);
    }

    @Test
    public void testAllocReadIOBuffer() {
        byte[] buffer = ioContext.allocReadIOBuffer();
        assertNotNull(buffer);
        assertEquals(BufferRecycler.BYTE_READ_IO_BUFFER, buffer.length);
    }

    @Test
    public void testAllocReadIOBufferWithSize() {
        int minSize = 1024;
        byte[] buffer = ioContext.allocReadIOBuffer(minSize);
        assertNotNull(buffer);
        assertEquals(minSize, buffer.length);
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        byte[] buffer = ioContext.allocWriteEncodingBuffer();
        assertNotNull(buffer);
        assertEquals(BufferRecycler.BYTE_WRITE_ENCODING_BUFFER, buffer.length);
    }

    @Test
    public void testAllocWriteEncodingBufferWithSize() {
        int minSize = 1024;
        byte[] buffer = ioContext.allocWriteEncodingBuffer(minSize);
        assertNotNull(buffer);
        assertEquals(minSize, buffer.length);
    }

    @Test
    public void testAllocBase64Buffer() {
        byte[] buffer = ioContext.allocBase64Buffer();
        assertNotNull(buffer);
        assertEquals(BufferRecycler.BYTE_BASE64_CODEC_BUFFER, buffer.length);
    }

    @Test
    public void testAllocTokenBuffer() {
        char[] buffer = ioContext.allocTokenBuffer();
        assertNotNull(buffer);
        assertEquals(BufferRecycler.CHAR_TOKEN_BUFFER, buffer.length);
    }

    @Test
    public void testAllocTokenBufferWithSize() {
        int minSize = 1024;
        char[] buffer = ioContext.allocTokenBuffer(minSize);
        assertNotNull(buffer);
        assertEquals(minSize, buffer.length);
    }

    @Test
    public void testAllocConcatBuffer() {
        char[] buffer = ioContext.allocConcatBuffer();
        assertNotNull(buffer);
        assertEquals(BufferRecycler.CHAR_CONCAT_BUFFER, buffer.length);
    }

    @Test
    public void testAllocNameCopyBuffer() {
        int minSize = 1024;
        char[] buffer = ioContext.allocNameCopyBuffer(minSize);
        assertNotNull(buffer);
        assertEquals(minSize, buffer.length);
    }

    @Test
    public void testReleaseReadIOBuffer() {
        byte[] buffer = new byte[BufferRecycler.BYTE_READ_IO_BUFFER];
        ioContext.releaseReadIOBuffer(buffer);
        assertNull(ioContext._readIOBuffer);
    }

    @Test
    public void testReleaseWriteEncodingBuffer() {
        byte[] buffer = new byte[BufferRecycler.BYTE_WRITE_ENCODING_BUFFER];
        ioContext.releaseWriteEncodingBuffer(buffer);
        assertNull(ioContext._writeEncodingBuffer);
    }

    @Test
    public void testReleaseBase64Buffer() {
        byte[] buffer = new byte[BufferRecycler.BYTE_BASE64_CODEC_BUFFER];
        ioContext.releaseBase64Buffer(buffer);
        assertNull(ioContext._base64Buffer);
    }

    @Test
    public void testReleaseTokenBuffer() {
        char[] buffer = new char[BufferRecycler.CHAR_TOKEN_BUFFER];
        ioContext.releaseTokenBuffer(buffer);
        assertNull(ioContext._tokenCBuffer);
    }

    @Test
    public void testReleaseConcatBuffer() {
        char[] buffer = new char[BufferRecycler.CHAR_CONCAT_BUFFER];
        ioContext.releaseConcatBuffer(buffer);
        assertNull(ioContext._concatCBuffer);
    }

    @Test
    public void testReleaseNameCopyBuffer() {
        char[] buffer = new char[BufferRecycler.CHAR_NAME_COPY_BUFFER];
        ioContext.releaseNameCopyBuffer(buffer);
        assertNull(ioContext._nameCopyBuffer);
    }
}