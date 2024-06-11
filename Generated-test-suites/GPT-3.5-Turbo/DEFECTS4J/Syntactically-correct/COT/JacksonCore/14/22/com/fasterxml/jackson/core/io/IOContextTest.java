
import org.junit.Assert;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.util.BufferRecycler;
import com.fasterxml.jackson.core.util.TextBuffer;

public class IOContextTest {

    @Test
    public void testConstructTextBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), null, true);
        TextBuffer textBuffer = ioContext.constructTextBuffer();
        Assert.assertNotNull(textBuffer);
    }

    @Test
    public void testAllocReadIOBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), null, true);
        byte[] buffer = ioContext.allocReadIOBuffer();
        Assert.assertNotNull(buffer);
    }

    @Test
    public void testAllocReadIOBufferWithMinSize() {
        IOContext ioContext = new IOContext(new BufferRecycler(), null, true);
        byte[] buffer = ioContext.allocReadIOBuffer(1024);
        Assert.assertNotNull(buffer);
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), null, true);
        byte[] buffer = ioContext.allocWriteEncodingBuffer();
        Assert.assertNotNull(buffer);
    }

    @Test
    public void testAllocWriteEncodingBufferWithMinSize() {
        IOContext ioContext = new IOContext(new BufferRecycler(), null, true);
        byte[] buffer = ioContext.allocWriteEncodingBuffer(1024);
        Assert.assertNotNull(buffer);
    }

    @Test
    public void testAllocBase64Buffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), null, true);
        byte[] buffer = ioContext.allocBase64Buffer();
        Assert.assertNotNull(buffer);
    }

    @Test
    public void testAllocTokenBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), null, true);
        char[] buffer = ioContext.allocTokenBuffer();
        Assert.assertNotNull(buffer);
    }

    @Test
    public void testAllocTokenBufferWithMinSize() {
        IOContext ioContext = new IOContext(new BufferRecycler(), null, true);
        char[] buffer = ioContext.allocTokenBuffer(1024);
        Assert.assertNotNull(buffer);
    }

    @Test
    public void testAllocConcatBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), null, true);
        char[] buffer = ioContext.allocConcatBuffer();
        Assert.assertNotNull(buffer);
    }

    @Test
    public void testAllocNameCopyBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), null, true);
        char[] buffer = ioContext.allocNameCopyBuffer(1024);
        Assert.assertNotNull(buffer);
    }

    @Test
    public void testSetEncoding() {
        IOContext ioContext = new IOContext(new BufferRecycler(), null, true);
        ioContext.setEncoding(JsonEncoding.UTF8);
        Assert.assertEquals(JsonEncoding.UTF8, ioContext.getEncoding());
    }

    @Test
    public void testWithEncoding() {
        IOContext ioContext = new IOContext(new BufferRecycler(), null, true);
        ioContext.withEncoding(JsonEncoding.UTF8);
        Assert.assertEquals(JsonEncoding.UTF8, ioContext.getEncoding());
    }

    @Test
    public void testGetSourceReference() {
        Object sourceRef = "Source";
        IOContext ioContext = new IOContext(new BufferRecycler(), sourceRef, true);
        Assert.assertEquals(sourceRef, ioContext.getSourceReference());
    }

    @Test
    public void testIsResourceManaged() {
        IOContext ioContext = new IOContext(new BufferRecycler(), null, true);
        Assert.assertTrue(ioContext.isResourceManaged());
    }

    @Test
    public void testReleaseReadIOBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), null, true);
        byte[] buffer = ioContext.allocReadIOBuffer();
        ioContext.releaseReadIOBuffer(buffer);
        Assert.assertNull(ioContext._readIOBuffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseReadIOBufferWithWrongBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), null, true);
        byte[] buffer = new byte[1024];
        ioContext.releaseReadIOBuffer(buffer);
    }

    @Test
    public void testReleaseWriteEncodingBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), null, true);
        byte[] buffer = ioContext.allocWriteEncodingBuffer();
        ioContext.releaseWriteEncodingBuffer(buffer);
        Assert.assertNull(ioContext._writeEncodingBuffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseWriteEncodingBufferWithWrongBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), null, true);
        byte[] buffer = new byte[1024];
        ioContext.releaseWriteEncodingBuffer(buffer);
    }

    @Test
    public void testReleaseBase64Buffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), null, true);
        byte[] buffer = ioContext.allocBase64Buffer();
        ioContext.releaseBase64Buffer(buffer);
        Assert.assertNull(ioContext._base64Buffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseBase64BufferWithWrongBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), null, true);
        byte[] buffer = new byte[1024];
        ioContext.releaseBase64Buffer(buffer);
    }

    @Test
    public void testReleaseTokenBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), null, true);
        char[] buffer = ioContext.allocTokenBuffer();
        ioContext.releaseTokenBuffer(buffer);
        Assert.assertNull(ioContext._tokenCBuffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseTokenBufferWithWrongBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), null, true);
        char[] buffer = new char[1024];
        ioContext.releaseTokenBuffer(buffer);
    }

    @Test
    public void testReleaseConcatBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), null, true);
        char[] buffer = ioContext.allocConcatBuffer();
        ioContext.releaseConcatBuffer(buffer);
        Assert.assertNull(ioContext._concatCBuffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseConcatBufferWithWrongBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), null, true);
        char[] buffer = new char[1024];
        ioContext.releaseConcatBuffer(buffer);
    }

    @Test
    public void testReleaseNameCopyBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), null, true);
        char[] buffer = ioContext.allocNameCopyBuffer(1024);
        ioContext.releaseNameCopyBuffer(buffer);
        Assert.assertNull(ioContext._nameCopyBuffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReleaseNameCopyBufferWithWrongBuffer() {
        IOContext ioContext = new IOContext(new BufferRecycler(), null, true);
        char[] buffer = new char[1024];
        ioContext.releaseNameCopyBuffer(buffer);
    }
}
