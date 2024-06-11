import org.junit.Test;
import static org.junit.Assert.*;

public class IOContextTest {

    @Test
    public void testConstructTextBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "test", true);
        TextBuffer textBuffer = ioContext.constructTextBuffer();

        assertNotNull(textBuffer);
        assertEquals(0, textBuffer.size());
        assertEquals(0, textBuffer.getTextBuffer().length);
    }

    @Test
    public void testAllocReadIOBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "test", true);
        byte[] buffer = ioContext.allocReadIOBuffer();

        assertNotNull(buffer);
        assertEquals(BufferRecycler.BYTE_READ_IO_BUFFER, buffer.length);
    }

    @Test
    public void testAllocReadIOBufferWithMinSize() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "test", true);
        int bufferSize = 100;
        byte[] buffer = ioContext.allocReadIOBuffer(bufferSize);

        assertNotNull(buffer);
        assertTrue(bufferSize <= buffer.length);
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "test", true);
        byte[] buffer = ioContext.allocWriteEncodingBuffer();

        assertNotNull(buffer);
        assertEquals(BufferRecycler.BYTE_WRITE_ENCODING_BUFFER, buffer.length);
    }

    @Test
    public void testAllocWriteEncodingBufferWithMinSize() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "test", true);
        int bufferSize = 100;
        byte[] buffer = ioContext.allocWriteEncodingBuffer(bufferSize);

        assertNotNull(buffer);
        assertTrue(bufferSize <= buffer.length);
    }

    @Test
    public void testAllocBase64Buffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "test", true);
        byte[] buffer = ioContext.allocBase64Buffer();

        assertNotNull(buffer);
        assertEquals(BufferRecycler.BYTE_BASE64_CODEC_BUFFER, buffer.length);
    }

    @Test
    public void testAllocTokenBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "test", true);
        char[] buffer = ioContext.allocTokenBuffer();

        assertNotNull(buffer);
        assertEquals(BufferRecycler.CHAR_TOKEN_BUFFER, buffer.length);
    }

    @Test
    public void testAllocTokenBufferWithMinSize() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "test", true);
        int bufferSize = 100;
        char[] buffer = ioContext.allocTokenBuffer(bufferSize);

        assertNotNull(buffer);
        assertTrue(bufferSize <= buffer.length);
    }

    @Test
    public void testAllocConcatBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "test", true);
        char[] buffer = ioContext.allocConcatBuffer();

        assertNotNull(buffer);
        assertEquals(BufferRecycler.CHAR_CONCAT_BUFFER, buffer.length);
    }

    @Test
    public void testAllocNameCopyBufferWithMinSize() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "test", true);
        int bufferSize = 100;
        char[] buffer = ioContext.allocNameCopyBuffer(bufferSize);

        assertNotNull(buffer);
        assertTrue(bufferSize <= buffer.length);
    }

    @Test
    public void testSetEncoding() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "test", true);
        JsonEncoding encoding = JsonEncoding.UTF8;

        ioContext.setEncoding(encoding);

        assertEquals(encoding, ioContext.getEncoding());
    }

    @Test
    public void testWithEncoding() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "test", true);
        JsonEncoding encoding = JsonEncoding.UTF8;

        ioContext = ioContext.withEncoding(encoding);

        assertEquals(encoding, ioContext.getEncoding());
    }

    @Test
    public void testGetSourceReference() {
        String sourceRef = "test";
        IOContext ioContext = new IOContext(new BufferRecycler(), sourceRef, true);

        assertEquals(sourceRef, ioContext.getSourceReference());
    }

    @Test
    public void testIsResourceManaged() {
        IOContext ioContextManaged = new IOContext(new BufferRecycler(), "test", true);
        IOContext ioContextNotManaged = new IOContext(new BufferRecycler(), "test", false);

        assertTrue(ioContextManaged.isResourceManaged());
        assertFalse(ioContextNotManaged.isResourceManaged());
    }

    @Test
    public void testReleaseReadIOBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "test", true);
        byte[] buffer = ioContext.allocReadIOBuffer();

        ioContext.releaseReadIOBuffer(buffer);

        assertNull(ioContext._readIOBuffer);
    }

    @Test
    public void testReleaseWriteEncodingBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "test", true);
        byte[] buffer = ioContext.allocWriteEncodingBuffer();

        ioContext.releaseWriteEncodingBuffer(buffer);

        assertNull(ioContext._writeEncodingBuffer);
    }

    @Test
    public void testReleaseBase64Buffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "test", true);
        byte[] buffer = ioContext.allocBase64Buffer();

        ioContext.releaseBase64Buffer(buffer);

        assertNull(ioContext._base64Buffer);
    }

    @Test
    public void testReleaseTokenBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "test", true);
        char[] buffer = ioContext.allocTokenBuffer();

        ioContext.releaseTokenBuffer(buffer);

        assertNull(ioContext._tokenCBuffer);
    }

    @Test
    public void testReleaseConcatBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "test", true);
        char[] buffer = ioContext.allocConcatBuffer();

        ioContext.releaseConcatBuffer(buffer);

        assertNull(ioContext._concatCBuffer);
    }

    @Test
    public void testReleaseNameCopyBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "test", true);
        char[] buffer = ioContext.allocNameCopyBuffer(BufferRecycler.CHAR_NAME_COPY_BUFFER);

        ioContext.releaseNameCopyBuffer(buffer);

        assertNull(ioContext._nameCopyBuffer);
    }

    @Test(expected = IllegalStateException.class)
    public void testVerifyAlloc() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "test", true);

        assertNull(ioContext._readIOBuffer);
        ioContext._verifyAlloc(ioContext._readIOBuffer);

        ioContext.allocReadIOBuffer();
        ioContext._verifyAlloc(ioContext._readIOBuffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifyReleaseWithWrongBuf() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "test", true);
        byte[] buffer = new byte[5];

        ioContext._verifyRelease(buffer, ioContext._readIOBuffer);
    }

    @Test
    public void testVerifyRelease() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "test", true);
        byte[] buffer = new byte[BufferRecycler.BYTE_READ_IO_BUFFER + 5];

        ioContext._verifyRelease(buffer, ioContext._readIOBuffer);
    }

}