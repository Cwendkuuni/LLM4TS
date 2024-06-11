
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.util.BufferRecycler;
import com.fasterxml.jackson.core.util.TextBuffer;

public class IOContextTest {

    private IOContext ioContext;

    @Before
    public void setup() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        ioContext = new IOContext(bufferRecycler, sourceRef, managedResource);
    }

    @Test
    public void testSetAndGetEncoding() {
        // Test setEncoding()
        JsonEncoding encoding = JsonEncoding.UTF8;
        ioContext.setEncoding(encoding);
        assertSame(encoding, ioContext.getEncoding());

        // Test withEncoding()
        JsonEncoding newEncoding = JsonEncoding.UTF16_BE;
        IOContext newIoContext = ioContext.withEncoding(newEncoding);
        assertSame(newEncoding, newIoContext.getEncoding());
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
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        assertNotNull(readIOBuffer);
        assertEquals(BufferRecycler.BYTE_READ_IO_BUFFER, readIOBuffer.length);
    }

    @Test
    public void testAllocReadIOBufferWithMinSize() {
        int minSize = 1024;
        byte[] readIOBuffer = ioContext.allocReadIOBuffer(minSize);
        assertNotNull(readIOBuffer);
        assertEquals(minSize, readIOBuffer.length);
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        assertNotNull(writeEncodingBuffer);
        assertEquals(BufferRecycler.BYTE_WRITE_ENCODING_BUFFER, writeEncodingBuffer.length);
    }

    @Test
    public void testAllocWriteEncodingBufferWithMinSize() {
        int minSize = 2048;
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer(minSize);
        assertNotNull(writeEncodingBuffer);
        assertEquals(minSize, writeEncodingBuffer.length);
    }

    @Test
    public void testAllocBase64Buffer() {
        byte[] base64Buffer = ioContext.allocBase64Buffer();
        assertNotNull(base64Buffer);
        assertEquals(BufferRecycler.BYTE_BASE64_CODEC_BUFFER, base64Buffer.length);
    }

    @Test
    public void testAllocTokenBuffer() {
        char[] tokenCBuffer = ioContext.allocTokenBuffer();
        assertNotNull(tokenCBuffer);
        assertEquals(BufferRecycler.CHAR_TOKEN_BUFFER, tokenCBuffer.length);
    }

    @Test
    public void testAllocTokenBufferWithMinSize() {
        int minSize = 512;
        char[] tokenCBuffer = ioContext.allocTokenBuffer(minSize);
        assertNotNull(tokenCBuffer);
        assertEquals(minSize, tokenCBuffer.length);
    }

    @Test
    public void testAllocConcatBuffer() {
        char[] concatCBuffer = ioContext.allocConcatBuffer();
        assertNotNull(concatCBuffer);
        assertEquals(BufferRecycler.CHAR_CONCAT_BUFFER, concatCBuffer.length);
    }

    @Test
    public void testAllocNameCopyBuffer() {
        int minSize = 32;
        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(minSize);
        assertNotNull(nameCopyBuffer);
        assertEquals(minSize, nameCopyBuffer.length);
    }

    @Test
    public void testReleaseReadIOBuffer() {
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        ioContext.releaseReadIOBuffer(readIOBuffer);
        assertNull(ioContext._readIOBuffer);
    }

    @Test
    public void testReleaseWriteEncodingBuffer() {
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
        assertNull(ioContext._writeEncodingBuffer);
    }

    @Test
    public void testReleaseBase64Buffer() {
        byte[] base64Buffer = ioContext.allocBase64Buffer();
        ioContext.releaseBase64Buffer(base64Buffer);
        assertNull(ioContext._base64Buffer);
    }

    @Test
    public void testReleaseTokenBuffer() {
        char[] tokenCBuffer = ioContext.allocTokenBuffer();
        ioContext.releaseTokenBuffer(tokenCBuffer);
        assertNull(ioContext._tokenCBuffer);
    }

    @Test
    public void testReleaseConcatBuffer() {
        char[] concatCBuffer = ioContext.allocConcatBuffer();
        ioContext.releaseConcatBuffer(concatCBuffer);
        assertNull(ioContext._concatCBuffer);
    }

    @Test
    public void testReleaseNameCopyBuffer() {
        int minSize = 32;
        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(minSize);
        ioContext.releaseNameCopyBuffer(nameCopyBuffer);
        assertNull(ioContext._nameCopyBuffer);
    }
}
