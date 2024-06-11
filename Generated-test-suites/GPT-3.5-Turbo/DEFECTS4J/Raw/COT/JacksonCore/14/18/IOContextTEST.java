import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.util.BufferRecycler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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
    public void testSetEncoding() {
        ioContext.setEncoding(JsonEncoding.UTF8);
        Assert.assertEquals(JsonEncoding.UTF8, ioContext.getEncoding());
    }

    @Test
    public void testWithEncoding() {
        IOContext updatedContext = ioContext.withEncoding(JsonEncoding.UTF16_LE);
        Assert.assertEquals(JsonEncoding.UTF16_LE, updatedContext.getEncoding());
    }

    @Test
    public void testGetSourceReference() {
        Object sourceRef = ioContext.getSourceReference();
        Assert.assertNotNull(sourceRef);
    }

    @Test
    public void testIsResourceManaged() {
        boolean isManaged = ioContext.isResourceManaged();
        Assert.assertTrue(isManaged);
    }

    @Test
    public void testConstructTextBuffer() {
        TextBuffer textBuffer = ioContext.constructTextBuffer();
        Assert.assertNotNull(textBuffer);
    }

    @Test
    public void testAllocReadIOBuffer() {
        byte[] buf = ioContext.allocReadIOBuffer();
        Assert.assertNotNull(buf);
    }

    @Test
    public void testAllocReadIOBufferWithSize() {
        int minSize = 1024;
        byte[] buf = ioContext.allocReadIOBuffer(minSize);
        Assert.assertNotNull(buf);
        Assert.assertTrue(buf.length >= minSize);
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        byte[] buf = ioContext.allocWriteEncodingBuffer();
        Assert.assertNotNull(buf);
    }

    @Test
    public void testAllocWriteEncodingBufferWithSize() {
        int minSize = 1024;
        byte[] buf = ioContext.allocWriteEncodingBuffer(minSize);
        Assert.assertNotNull(buf);
        Assert.assertTrue(buf.length >= minSize);
    }

    @Test
    public void testAllocBase64Buffer() {
        byte[] buf = ioContext.allocBase64Buffer();
        Assert.assertNotNull(buf);
    }

    @Test
    public void testAllocTokenBuffer() {
        char[] buf = ioContext.allocTokenBuffer();
        Assert.assertNotNull(buf);
    }

    @Test
    public void testAllocTokenBufferWithSize() {
        int minSize = 1024;
        char[] buf = ioContext.allocTokenBuffer(minSize);
        Assert.assertNotNull(buf);
        Assert.assertTrue(buf.length >= minSize);
    }

    @Test
    public void testAllocConcatBuffer() {
        char[] buf = ioContext.allocConcatBuffer();
        Assert.assertNotNull(buf);
    }

    @Test
    public void testAllocNameCopyBuffer() {
        int minSize = 1024;
        char[] buf = ioContext.allocNameCopyBuffer(minSize);
        Assert.assertNotNull(buf);
        Assert.assertTrue(buf.length >= minSize);
    }

    @Test
    public void testReleaseReadIOBuffer() {
        byte[] buf = ioContext.allocReadIOBuffer();
        ioContext.releaseReadIOBuffer(buf);
        Assert.assertNull(ioContext._readIOBuffer);
    }

    @Test
    public void testReleaseWriteEncodingBuffer() {
        byte[] buf = ioContext.allocWriteEncodingBuffer();
        ioContext.releaseWriteEncodingBuffer(buf);
        Assert.assertNull(ioContext._writeEncodingBuffer);
    }

    @Test
    public void testReleaseBase64Buffer() {
        byte[] buf = ioContext.allocBase64Buffer();
        ioContext.releaseBase64Buffer(buf);
        Assert.assertNull(ioContext._base64Buffer);
    }

    @Test
    public void testReleaseTokenBuffer() {
        char[] buf = ioContext.allocTokenBuffer();
        ioContext.releaseTokenBuffer(buf);
        Assert.assertNull(ioContext._tokenCBuffer);
    }

    @Test
    public void testReleaseConcatBuffer() {
        char[] buf = ioContext.allocConcatBuffer();
        ioContext.releaseConcatBuffer(buf);
        Assert.assertNull(ioContext._concatCBuffer);
    }

    @Test
    public void testReleaseNameCopyBuffer() {
        int minSize = 1024;
        char[] buf = ioContext.allocNameCopyBuffer(minSize);
        ioContext.releaseNameCopyBuffer(buf);
        Assert.assertNull(ioContext._nameCopyBuffer);
    }
}