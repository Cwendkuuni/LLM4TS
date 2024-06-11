import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class IOContextTest {

    private IOContext ioContext;

    @Before
    public void setup() {
        ioContext = new IOContext(new BufferRecycler(), new Object(), true);
    }

    // Test case for setEncoding()
    @Test
    public void testSetEncoding() {
        ioContext.setEncoding(JsonEncoding.UTF8);
        Assert.assertEquals(JsonEncoding.UTF8, ioContext.getEncoding());
    }

    // Test case for withEncoding()
    @Test
    public void testWithEncoding() {
        IOContext newIOContext = ioContext.withEncoding(JsonEncoding.UTF16);
        Assert.assertEquals(JsonEncoding.UTF16, newIOContext.getEncoding());
    }

    // Test case for constructTextBuffer()
    @Test
    public void testConstructTextBuffer() {
        TextBuffer textBuffer = ioContext.constructTextBuffer();
        Assert.assertNotNull(textBuffer);
    }

    // Test case for allocReadIOBuffer()
    @Test
    public void testAllocReadIOBuffer() {
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        Assert.assertNotNull(readIOBuffer);
        Assert.assertEquals(BufferRecycler.BYTE_READ_IO_BUFFER, readIOBuffer.length);
    }

    // Test case for allocReadIOBuffer(int minSize)
    @Test
    public void testAllocReadIOBufferWithMinSize() {
        byte[] readIOBuffer = ioContext.allocReadIOBuffer(100);
        Assert.assertNotNull(readIOBuffer);
        Assert.assertEquals(100, readIOBuffer.length);
    }

    // Test case for allocWriteEncodingBuffer()
    @Test
    public void testAllocWriteEncodingBuffer() {
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        Assert.assertNotNull(writeEncodingBuffer);
        Assert.assertEquals(BufferRecycler.BYTE_WRITE_ENCODING_BUFFER, writeEncodingBuffer.length);
    }

    // Test case for allocWriteEncodingBuffer(int minSize)
    @Test
    public void testAllocWriteEncodingBufferWithMinSize() {
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer(100);
        Assert.assertNotNull(writeEncodingBuffer);
        Assert.assertEquals(100, writeEncodingBuffer.length);
    }

    // Test case for allocBase64Buffer()
    @Test
    public void testAllocBase64Buffer() {
        byte[] base64Buffer = ioContext.allocBase64Buffer();
        Assert.assertNotNull(base64Buffer);
        Assert.assertEquals(BufferRecycler.BYTE_BASE64_CODEC_BUFFER, base64Buffer.length);
    }

    // Test case for allocTokenBuffer()
    @Test
    public void testAllocTokenBuffer() {
        char[] tokenBuffer = ioContext.allocTokenBuffer();
        Assert.assertNotNull(tokenBuffer);
        Assert.assertEquals(BufferRecycler.CHAR_TOKEN_BUFFER, tokenBuffer.length);
    }

    // Test case for allocTokenBuffer(int minSize)
    @Test
    public void testAllocTokenBufferWithMinSize() {
        char[] tokenBuffer = ioContext.allocTokenBuffer(100);
        Assert.assertNotNull(tokenBuffer);
        Assert.assertEquals(100, tokenBuffer.length);
    }

    // Test case for allocConcatBuffer()
    @Test
    public void testAllocConcatBuffer() {
        char[] concatBuffer = ioContext.allocConcatBuffer();
        Assert.assertNotNull(concatBuffer);
        Assert.assertEquals(BufferRecycler.CHAR_CONCAT_BUFFER, concatBuffer.length);
    }

    // Test case for allocNameCopyBuffer(int minSize)
    @Test
    public void testAllocNameCopyBuffer() {
        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(100);
        Assert.assertNotNull(nameCopyBuffer);
        Assert.assertEquals(100, nameCopyBuffer.length);
    }

    // Test case for releaseReadIOBuffer()
    @Test
    public void testReleaseReadIOBuffer() {
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        ioContext.releaseReadIOBuffer(readIOBuffer);
        Assert.assertNull(ioContext._readIOBuffer);
    }

    // Test case for releaseWriteEncodingBuffer()
    @Test
    public void testReleaseWriteEncodingBuffer() {
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
        Assert.assertNull(ioContext._writeEncodingBuffer);
    }

    // Test case for releaseBase64Buffer()
    @Test
    public void testReleaseBase64Buffer() {
        byte[] base64Buffer = ioContext.allocBase64Buffer();
        ioContext.releaseBase64Buffer(base64Buffer);
        Assert.assertNull(ioContext._base64Buffer);
    }

    // Test case for releaseTokenBuffer()
    @Test
    public void testReleaseTokenBuffer() {
        char[] tokenBuffer = ioContext.allocTokenBuffer();
        ioContext.releaseTokenBuffer(tokenBuffer);
        Assert.assertNull(ioContext._tokenCBuffer);
    }

    // Test case for releaseConcatBuffer()
    @Test
    public void testReleaseConcatBuffer() {
        char[] concatBuffer = ioContext.allocConcatBuffer();
        ioContext.releaseConcatBuffer(concatBuffer);
        Assert.assertNull(ioContext._concatCBuffer);
    }

    // Test case for releaseNameCopyBuffer()
    @Test
    public void testReleaseNameCopyBuffer() {
        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(100);
        ioContext.releaseNameCopyBuffer(nameCopyBuffer);
        Assert.assertNull(ioContext._nameCopyBuffer);
    }

    // Test case for getSourceReference()
    @Test
    public void testGetSourceReference() {
        Object sourceRef = ioContext.getSourceReference();
        Assert.assertNotNull(sourceRef);
    }

    // Test case for isResourceManaged()
    @Test
    public void testIsResourceManaged() {
        boolean isManaged = ioContext.isResourceManaged();
        Assert.assertTrue(isManaged);
    }

}