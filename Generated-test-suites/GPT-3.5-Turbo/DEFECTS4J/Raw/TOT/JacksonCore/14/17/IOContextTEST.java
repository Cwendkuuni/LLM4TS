import org.junit.Test;
import static org.junit.Assert.*;

public class IOContextTest {

    @Test
    public void testSetEncoding() {
        IOContext context = new IOContext(new BufferRecycler(), "source", true);
        JsonEncoding encoding = JsonEncoding.UTF8;
        context.setEncoding(encoding);
        assertEquals(encoding, context.getEncoding());
    }

    @Test
    public void testWithEncoding() {
        IOContext context = new IOContext(new BufferRecycler(), "source", true);
        JsonEncoding encoding = JsonEncoding.UTF8;
        IOContext newContext = context.withEncoding(encoding);
        assertEquals(encoding, newContext.getEncoding());
    }

    @Test
    public void testGettersAndSetters() {
        IOContext context = new IOContext(new BufferRecycler(), "source", true);
        Object sourceRef = "source";
        boolean managedResource = true;

        assertEquals(sourceRef, context.getSourceReference());
        assertEquals(managedResource, context.isResourceManaged());
    }

    @Test
    public void testConstructTextBuffer() {
        IOContext context = new IOContext(new BufferRecycler(), "source", true);
        TextBuffer buffer = context.constructTextBuffer();
        assertNotNull(buffer);
    }

    @Test
    public void testAllocReadIOBuffer() {
        IOContext context = new IOContext(new BufferRecycler(), "source", true);
        byte[] buf = context.allocReadIOBuffer();
        assertNotNull(buf);
    }

    @Test
    public void testAllocReadIOBufferWithMinSize() {
        IOContext context = new IOContext(new BufferRecycler(), "source", true);
        int minSize = 16;
        byte[] buf = context.allocReadIOBuffer(minSize);
        assertNotNull(buf);
        assertTrue(buf.length >= minSize);
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        IOContext context = new IOContext(new BufferRecycler(), "source", true);
        byte[] buf = context.allocWriteEncodingBuffer();
        assertNotNull(buf);
    }

    @Test
    public void testAllocWriteEncodingBufferWithMinSize() {
        IOContext context = new IOContext(new BufferRecycler(), "source", true);
        int minSize = 16;
        byte[] buf = context.allocWriteEncodingBuffer(minSize);
        assertNotNull(buf);
        assertTrue(buf.length >= minSize);
    }

    @Test
    public void testAllocBase64Buffer() {
        IOContext context = new IOContext(new BufferRecycler(), "source", true);
        byte[] buf = context.allocBase64Buffer();
        assertNotNull(buf);
    }

    @Test
    public void testAllocTokenBuffer() {
        IOContext context = new IOContext(new BufferRecycler(), "source", true);
        char[] buf = context.allocTokenBuffer();
        assertNotNull(buf);
    }

    @Test
    public void testAllocTokenBufferWithMinSize() {
        IOContext context = new IOContext(new BufferRecycler(), "source", true);
        int minSize = 16;
        char[] buf = context.allocTokenBuffer(minSize);
        assertNotNull(buf);
        assertTrue(buf.length >= minSize);
    }

    @Test
    public void testAllocConcatBuffer() {
        IOContext context = new IOContext(new BufferRecycler(), "source", true);
        char[] buf = context.allocConcatBuffer();
        assertNotNull(buf);
    }

    @Test
    public void testAllocNameCopyBuffer() {
        IOContext context = new IOContext(new BufferRecycler(), "source", true);
        int minSize = 16;
        char[] buf = context.allocNameCopyBuffer(minSize);
        assertNotNull(buf);
        assertTrue(buf.length >= minSize);
    }

    @Test
    public void testReleaseReadIOBuffer() {
        IOContext context = new IOContext(new BufferRecycler(), "source", true);
        byte[] buf = context.allocReadIOBuffer();
        context.releaseReadIOBuffer(buf);
        assertNull(context._readIOBuffer);
    }

    @Test
    public void testReleaseWriteEncodingBuffer() {
        IOContext context = new IOContext(new BufferRecycler(), "source", true);
        byte[] buf = context.allocWriteEncodingBuffer();
        context.releaseWriteEncodingBuffer(buf);
        assertNull(context._writeEncodingBuffer);
    }

    @Test
    public void testReleaseBase64Buffer() {
        IOContext context = new IOContext(new BufferRecycler(), "source", true);
        byte[] buf = context.allocBase64Buffer();
        context.releaseBase64Buffer(buf);
        assertNull(context._base64Buffer);
    }

    @Test
    public void testReleaseTokenBuffer() {
        IOContext context = new IOContext(new BufferRecycler(), "source", true);
        char[] buf = context.allocTokenBuffer();
        context.releaseTokenBuffer(buf);
        assertNull(context._tokenCBuffer);
    }

    @Test
    public void testReleaseConcatBuffer() {
        IOContext context = new IOContext(new BufferRecycler(), "source", true);
        char[] buf = context.allocConcatBuffer();
        context.releaseConcatBuffer(buf);
        assertNull(context._concatCBuffer);
    }

    @Test
    public void testReleaseNameCopyBuffer() {
        IOContext context = new IOContext(new BufferRecycler(), "source", true);
        char[] buf = context.allocNameCopyBuffer(16);
        context.releaseNameCopyBuffer(buf);
        assertNull(context._nameCopyBuffer);
    }
}