import org.junit.Assert;
import org.junit.Test;

public class IOContextTest {

    @Test
    public void testSetEncoding() {
        IOContext ioContext = new IOContext(null, null, false);

        ioContext.setEncoding(JsonEncoding.UTF8);

        Assert.assertEquals(JsonEncoding.UTF8, ioContext.getEncoding());
    }

    @Test
    public void testWithEncoding() {
        IOContext ioContext = new IOContext(null, null, false);

        IOContext newContext = ioContext.withEncoding(JsonEncoding.UTF8);

        Assert.assertEquals(JsonEncoding.UTF8, newContext.getEncoding());
        Assert.assertNotSame(ioContext, newContext);
    }

    @Test
    public void testConstructTextBuffer() {
        IOContext ioContext = new IOContext(null, null, false);

        TextBuffer textBuffer = ioContext.constructTextBuffer();

        Assert.assertNotNull(textBuffer);
    }

    @Test
    public void testAllocReadIOBuffer() {
        IOContext ioContext = new IOContext(null, null, false);

        byte[] buffer = ioContext.allocReadIOBuffer();

        Assert.assertNotNull(buffer);
    }

    @Test
    public void testAllocReadIOBufferWithMinSize() {
        IOContext ioContext = new IOContext(null, null, false);

        byte[] buffer = ioContext.allocReadIOBuffer(1024);

        Assert.assertNotNull(buffer);
        Assert.assertTrue(buffer.length >= 1024);
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        IOContext ioContext = new IOContext(null, null, false);

        byte[] buffer = ioContext.allocWriteEncodingBuffer();

        Assert.assertNotNull(buffer);
    }

    @Test
    public void testAllocWriteEncodingBufferWithMinSize() {
        IOContext ioContext = new IOContext(null, null, false);

        byte[] buffer = ioContext.allocWriteEncodingBuffer(1024);

        Assert.assertNotNull(buffer);
        Assert.assertTrue(buffer.length >= 1024);
    }

    @Test
    public void testAllocBase64Buffer() {
        IOContext ioContext = new IOContext(null, null, false);

        byte[] buffer = ioContext.allocBase64Buffer();

        Assert.assertNotNull(buffer);
    }

    @Test
    public void testAllocTokenBuffer() {
        IOContext ioContext = new IOContext(null, null, false);

        char[] buffer = ioContext.allocTokenBuffer();

        Assert.assertNotNull(buffer);
    }

    @Test
    public void testAllocTokenBufferWithMinSize() {
        IOContext ioContext = new IOContext(null, null, false);

        char[] buffer = ioContext.allocTokenBuffer(1024);

        Assert.assertNotNull(buffer);
        Assert.assertTrue(buffer.length >= 1024);
    }

    @Test
    public void testAllocConcatBuffer() {
        IOContext ioContext = new IOContext(null, null, false);

        char[] buffer = ioContext.allocConcatBuffer();

        Assert.assertNotNull(buffer);
    }

    @Test
    public void testAllocNameCopyBuffer() {
        IOContext ioContext = new IOContext(null, null, false);

        char[] buffer = ioContext.allocNameCopyBuffer(1024);

        Assert.assertNotNull(buffer);
        Assert.assertTrue(buffer.length >= 1024);
    }

    @Test
    public void testReleaseReadIOBuffer() {
        IOContext ioContext = new IOContext(null, null, false);
        byte[] buffer = ioContext.allocReadIOBuffer();

        ioContext.releaseReadIOBuffer(buffer);

        Assert.assertNull(ioContext._readIOBuffer);
    }

    @Test
    public void testReleaseWriteEncodingBuffer() {
        IOContext ioContext = new IOContext(null, null, false);
        byte[] buffer = ioContext.allocWriteEncodingBuffer();

        ioContext.releaseWriteEncodingBuffer(buffer);

        Assert.assertNull(ioContext._writeEncodingBuffer);
    }

    @Test
    public void testReleaseBase64Buffer() {
        IOContext ioContext = new IOContext(null, null, false);
        byte[] buffer = ioContext.allocBase64Buffer();

        ioContext.releaseBase64Buffer(buffer);

        Assert.assertNull(ioContext._base64Buffer);
    }

    @Test
    public void testReleaseTokenBuffer() {
        IOContext ioContext = new IOContext(null, null, false);
        char[] buffer = ioContext.allocTokenBuffer();

        ioContext.releaseTokenBuffer(buffer);

        Assert.assertNull(ioContext._tokenCBuffer);
    }

    @Test
    public void testReleaseConcatBuffer() {
        IOContext ioContext = new IOContext(null, null, false);
        char[] buffer = ioContext.allocConcatBuffer();

        ioContext.releaseConcatBuffer(buffer);

        Assert.assertNull(ioContext._concatCBuffer);
    }

    @Test
    public void testReleaseNameCopyBuffer() {
        IOContext ioContext = new IOContext(null, null, false);
        char[] buffer = ioContext.allocNameCopyBuffer(1024);

        ioContext.releaseNameCopyBuffer(buffer);

        Assert.assertNull(ioContext._nameCopyBuffer);
    }
}