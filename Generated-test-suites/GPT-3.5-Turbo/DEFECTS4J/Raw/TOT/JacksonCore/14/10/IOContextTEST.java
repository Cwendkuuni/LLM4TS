import org.junit.Test;
import org.junit.Assert;

public class IOContextTest {
    
    @Test
    public void testConstructTextBuffer() {
        IOContext context = new IOContext(null, null, true);
        TextBuffer buffer = context.constructTextBuffer();
        Assert.assertNotNull(buffer);
    }
    
    @Test
    public void testWithEncoding() {
        IOContext context = new IOContext(null, null, true);
        JsonEncoding encoding = JsonEncoding.UTF8;
        context.setEncoding(encoding);
        IOContext newContext = context.withEncoding(JsonEncoding.UTF16_BE);
        Assert.assertEquals(JsonEncoding.UTF16_BE, newContext.getEncoding());
    }
    
    @Test
    public void testAllocReadIOBuffer() {
        IOContext context = new IOContext(null, null, true);
        byte[] buffer = context.allocReadIOBuffer();
        Assert.assertNotNull(buffer);
    }
    
    @Test
    public void testAllocReadIOBufferWithMinSize() {
        IOContext context = new IOContext(null, null, true);
        byte[] buffer = context.allocReadIOBuffer(1024);
        Assert.assertNotNull(buffer);
        Assert.assertTrue(buffer.length >= 1024);
    }
    
    @Test
    public void testAllocWriteEncodingBuffer() {
        IOContext context = new IOContext(null, null, true);
        byte[] buffer = context.allocWriteEncodingBuffer();
        Assert.assertNotNull(buffer);
    }
    
    @Test
    public void testAllocWriteEncodingBufferWithMinSize() {
        IOContext context = new IOContext(null, null, true);
        byte[] buffer = context.allocWriteEncodingBuffer(1024);
        Assert.assertNotNull(buffer);
        Assert.assertTrue(buffer.length >= 1024);
    }
    
    @Test
    public void testAllocBase64Buffer() {
        IOContext context = new IOContext(null, null, true);
        byte[] buffer = context.allocBase64Buffer();
        Assert.assertNotNull(buffer);
    }
    
    @Test
    public void testAllocTokenBuffer() {
        IOContext context = new IOContext(null, null, true);
        char[] buffer = context.allocTokenBuffer();
        Assert.assertNotNull(buffer);
    }
    
    @Test
    public void testAllocTokenBufferWithMinSize() {
        IOContext context = new IOContext(null, null, true);
        char[] buffer = context.allocTokenBuffer(1024);
        Assert.assertNotNull(buffer);
        Assert.assertTrue(buffer.length >= 1024);
    }
    
    @Test
    public void testAllocConcatBuffer() {
        IOContext context = new IOContext(null, null, true);
        char[] buffer = context.allocConcatBuffer();
        Assert.assertNotNull(buffer);
    }
    
    @Test
    public void testAllocNameCopyBuffer() {
        IOContext context = new IOContext(null, null, true);
        char[] buffer = context.allocNameCopyBuffer(1024);
        Assert.assertNotNull(buffer);
        Assert.assertTrue(buffer.length >= 1024);
    }
    
    @Test
    public void testReleaseReadIOBuffer() {
        IOContext context = new IOContext(null, null, true);
        byte[] buffer = context.allocReadIOBuffer();
        context.releaseReadIOBuffer(buffer);
        Assert.assertNull(context._readIOBuffer);
    }
    
    @Test
    public void testReleaseWriteEncodingBuffer() {
        IOContext context = new IOContext(null, null, true);
        byte[] buffer = context.allocWriteEncodingBuffer();
        context.releaseWriteEncodingBuffer(buffer);
        Assert.assertNull(context._writeEncodingBuffer);
    }
    
    @Test
    public void testReleaseBase64Buffer() {
        IOContext context = new IOContext(null, null, true);
        byte[] buffer = context.allocBase64Buffer();
        context.releaseBase64Buffer(buffer);
        Assert.assertNull(context._base64Buffer);
    }
    
    @Test
    public void testReleaseTokenBuffer() {
        IOContext context = new IOContext(null, null, true);
        char[] buffer = context.allocTokenBuffer();
        context.releaseTokenBuffer(buffer);
        Assert.assertNull(context._tokenCBuffer);
    }
    
    @Test
    public void testReleaseConcatBuffer() {
        IOContext context = new IOContext(null, null, true);
        char[] buffer = context.allocConcatBuffer();
        context.releaseConcatBuffer(buffer);
        Assert.assertNull(context._concatCBuffer);
    }
    
    @Test
    public void testReleaseNameCopyBuffer() {
        IOContext context = new IOContext(null, null, true);
        char[] buffer = context.allocNameCopyBuffer(1024);
        context.releaseNameCopyBuffer(buffer);
        Assert.assertNull(context._nameCopyBuffer);
    }
}