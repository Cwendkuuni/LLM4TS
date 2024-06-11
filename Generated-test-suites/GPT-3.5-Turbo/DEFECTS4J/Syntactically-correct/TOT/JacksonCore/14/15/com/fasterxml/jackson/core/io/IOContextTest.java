import org.junit.Assert;
import org.junit.Test;

public class IOContextTest {

    @Test
    public void testSetEncoding() {
        IOContext context = new IOContext(null, null, false);
        context.setEncoding(JsonEncoding.UTF8);
        
        Assert.assertEquals(JsonEncoding.UTF8, context.getEncoding());
    }
    
    @Test
    public void testWithEncoding() {
        IOContext context = new IOContext(null, null, false);
        context.setEncoding(JsonEncoding.UTF8);
        
        IOContext newContext = context.withEncoding(JsonEncoding.UTF16_BE);
        
        Assert.assertEquals(JsonEncoding.UTF16_BE, newContext.getEncoding());
    }
    
    @Test
    public void testGetSourceReference() {
        Object sourceRef = new Object();
        IOContext context = new IOContext(null, sourceRef, false);
        
        Assert.assertEquals(sourceRef, context.getSourceReference());
    }
    
    @Test
    public void testIsResourceManaged() {
        IOContext context1 = new IOContext(null, null, true);
        IOContext context2 = new IOContext(null, null, false);
        
        Assert.assertTrue(context1.isResourceManaged());
        Assert.assertFalse(context2.isResourceManaged());
    }
    
    @Test
    public void testConstructTextBuffer() {
        IOContext context = new IOContext(null, null, false);
        TextBuffer textBuffer = context.constructTextBuffer();
        
        Assert.assertNotNull(textBuffer);
    }
    
    @Test
    public void testAllocReadIOBuffer() {
        IOContext context = new IOContext(new BufferRecycler(), null, false);
        byte[] buffer = context.allocReadIOBuffer();
        
        Assert.assertNotNull(buffer);
        Assert.assertEquals(BufferRecycler.BYTE_READ_IO_BUFFER, buffer.length);
    }
    
    @Test
    public void testAllocReadIOBufferWithMinSize() {
        IOContext context = new IOContext(new BufferRecycler(), null, false);
        int minSize = 32;
        
        byte[] buffer = context.allocReadIOBuffer(minSize);
        
        Assert.assertNotNull(buffer);
        Assert.assertEquals(minSize, buffer.length);
    }
    
    @Test
    public void testAllocWriteEncodingBuffer() {
        IOContext context = new IOContext(new BufferRecycler(), null, false);
        byte[] buffer = context.allocWriteEncodingBuffer();
        
        Assert.assertNotNull(buffer);
        Assert.assertEquals(BufferRecycler.BYTE_WRITE_ENCODING_BUFFER, buffer.length);
    }
    
    @Test
    public void testAllocWriteEncodingBufferWithMinSize() {
        IOContext context = new IOContext(new BufferRecycler(), null, false);
        int minSize = 64;
        
        byte[] buffer = context.allocWriteEncodingBuffer(minSize);
        
        Assert.assertNotNull(buffer);
        Assert.assertEquals(minSize, buffer.length);
    }
    
    @Test
    public void testAllocBase64Buffer() {
        IOContext context = new IOContext(new BufferRecycler(), null, false);
        byte[] buffer = context.allocBase64Buffer();
        
        Assert.assertNotNull(buffer);
        Assert.assertEquals(BufferRecycler.BYTE_BASE64_CODEC_BUFFER, buffer.length);
    }
    
    @Test
    public void testAllocTokenBuffer() {
        IOContext context = new IOContext(new BufferRecycler(), null, false);
        char[] buffer = context.allocTokenBuffer();
        
        Assert.assertNotNull(buffer);
        Assert.assertEquals(BufferRecycler.CHAR_TOKEN_BUFFER, buffer.length);
    }
    
    @Test
    public void testAllocTokenBufferWithMinSize() {
        IOContext context = new IOContext(new BufferRecycler(), null, false);
        int minSize = 16;
        
        char[] buffer = context.allocTokenBuffer(minSize);
        
        Assert.assertNotNull(buffer);
        Assert.assertEquals(minSize, buffer.length);
    }
    
    @Test
    public void testAllocConcatBuffer() {
        IOContext context = new IOContext(new BufferRecycler(), null, false);
        char[] buffer = context.allocConcatBuffer();
        
        Assert.assertNotNull(buffer);
        Assert.assertEquals(BufferRecycler.CHAR_CONCAT_BUFFER, buffer.length);
    }
    
    @Test
    public void testAllocNameCopyBuffer() {
        IOContext context = new IOContext(new BufferRecycler(), null, false);
        int minSize = 24;
        
        char[] buffer = context.allocNameCopyBuffer(minSize);
        
        Assert.assertNotNull(buffer);
        Assert.assertEquals(minSize, buffer.length);
    }
    
    @Test
    public void testReleaseReadIOBuffer() {
        IOContext context = new IOContext(new BufferRecycler(), null, false);
        byte[] buffer = context.allocReadIOBuffer();
        
        context.releaseReadIOBuffer(buffer);
        
        Assert.assertNull(context._readIOBuffer);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReleaseReadIOBufferWithWrongBuffer() {
        IOContext context = new IOContext(new BufferRecycler(), null, false);
        byte[] buffer = new byte[BufferRecycler.BYTE_READ_IO_BUFFER / 2];
        
        context.releaseReadIOBuffer(buffer);
        
        // Exception should have been thrown
    }
    
    @Test
    public void testReleaseWriteEncodingBuffer() {
        IOContext context = new IOContext(new BufferRecycler(), null, false);
        byte[] buffer = context.allocWriteEncodingBuffer();
        
        context.releaseWriteEncodingBuffer(buffer);
        
        Assert.assertNull(context._writeEncodingBuffer);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReleaseWriteEncodingBufferWithWrongBuffer() {
        IOContext context = new IOContext(new BufferRecycler(), null, false);
        byte[] buffer = new byte[BufferRecycler.BYTE_WRITE_ENCODING_BUFFER / 2];
        
        context.releaseWriteEncodingBuffer(buffer);
        
        // Exception should have been thrown
    }
    
    @Test
    public void testReleaseBase64Buffer() {
        IOContext context = new IOContext(new BufferRecycler(), null, false);
        byte[] buffer = context.allocBase64Buffer();
        
        context.releaseBase64Buffer(buffer);
        
        Assert.assertNull(context._base64Buffer);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReleaseBase64BufferWithWrongBuffer() {
        IOContext context = new IOContext(new BufferRecycler(), null, false);
        byte[] buffer = new byte[BufferRecycler.BYTE_BASE64_CODEC_BUFFER / 2];
        
        context.releaseBase64Buffer(buffer);
        
        // Exception should have been thrown
    }
    
    @Test
    public void testReleaseTokenBuffer() {
        IOContext context = new IOContext(new BufferRecycler(), null, false);
        char[] buffer = context.allocTokenBuffer();
        
        context.releaseTokenBuffer(buffer);
        
        Assert.assertNull(context._tokenCBuffer);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReleaseTokenBufferWithWrongBuffer() {
        IOContext context = new IOContext(new BufferRecycler(), null, false);
        char[] buffer = new char[BufferRecycler.CHAR_TOKEN_BUFFER / 2];
        
        context.releaseTokenBuffer(buffer);
        
        // Exception should have been thrown
    }
    
    @Test
    public void testReleaseConcatBuffer() {
        IOContext context = new IOContext(new BufferRecycler(), null, false);
        char[] buffer = context.allocConcatBuffer();
        
        context.releaseConcatBuffer(buffer);
        
        Assert.assertNull(context._concatCBuffer);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReleaseConcatBufferWithWrongBuffer() {
        IOContext context = new IOContext(new BufferRecycler(), null, false);
        char[] buffer = new char[BufferRecycler.CHAR_CONCAT_BUFFER / 2];
        
        context.releaseConcatBuffer(buffer);
        
        // Exception should have been thrown
    }
    
    @Test
    public void testReleaseNameCopyBuffer() {
        IOContext context = new IOContext(new BufferRecycler(), null, false);
        char[] buffer = context.allocNameCopyBuffer(16);
        
        context.releaseNameCopyBuffer(buffer);
        
        Assert.assertNull(context._nameCopyBuffer);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReleaseNameCopyBufferWithWrongBuffer() {
        IOContext context = new IOContext(new BufferRecycler(), null, false);
        char[] buffer = new char[16];
        
        context.releaseNameCopyBuffer(buffer);
        
        // Exception should have been thrown
    }
}