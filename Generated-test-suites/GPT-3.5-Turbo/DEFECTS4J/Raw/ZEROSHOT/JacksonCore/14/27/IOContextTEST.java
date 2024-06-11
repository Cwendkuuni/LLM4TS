import org.junit.Test;
import static org.junit.Assert.*;

public class IOContextTest {

    @Test
    public void testSetEncoding() {
        BufferRecycler br = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = false;
        IOContext context = new IOContext(br, sourceRef, managedResource);

        JsonEncoding encoding = JsonEncoding.UTF8;
        context.setEncoding(encoding);

        assertEquals(encoding, context.getEncoding());
    }

    @Test
    public void testWithEncoding() {
        BufferRecycler br = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = false;
        IOContext context = new IOContext(br, sourceRef, managedResource);

        JsonEncoding encoding = JsonEncoding.UTF8;
        IOContext updatedContext = context.withEncoding(encoding);

        assertEquals(encoding, updatedContext.getEncoding());
    }

    @Test
    public void testGetSourceReference() {
        BufferRecycler br = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = false;
        IOContext context = new IOContext(br, sourceRef, managedResource);

        assertEquals(sourceRef, context.getSourceReference());
    }

    @Test
    public void testIsResourceManaged() {
        BufferRecycler br = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        IOContext context = new IOContext(br, sourceRef, managedResource);

        assertTrue(context.isResourceManaged());
    }

    @Test
    public void testConstructTextBuffer() {
        BufferRecycler br = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = false;
        IOContext context = new IOContext(br, sourceRef, managedResource);

        TextBuffer textBuffer = context.constructTextBuffer();

        assertNotNull(textBuffer);
    }

    @Test
    public void testAllocReadIOBuffer() {
        BufferRecycler br = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = false;
        IOContext context = new IOContext(br, sourceRef, managedResource);

        byte[] buffer = context.allocReadIOBuffer();

        assertNotNull(buffer);
    }

    @Test
    public void testAllocReadIOBufferWithMinSize() {
        BufferRecycler br = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = false;
        IOContext context = new IOContext(br, sourceRef, managedResource);
        int minSize = 1024;

        byte[] buffer = context.allocReadIOBuffer(minSize);

        assertNotNull(buffer);
        assertTrue(buffer.length >= minSize);
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        BufferRecycler br = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = false;
        IOContext context = new IOContext(br, sourceRef, managedResource);

        byte[] buffer = context.allocWriteEncodingBuffer();

        assertNotNull(buffer);
    }

    @Test
    public void testAllocWriteEncodingBufferWithMinSize() {
        BufferRecycler br = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = false;
        IOContext context = new IOContext(br, sourceRef, managedResource);
        int minSize = 1024;

        byte[] buffer = context.allocWriteEncodingBuffer(minSize);

        assertNotNull(buffer);
        assertTrue(buffer.length >= minSize);
    }

    @Test
    public void testAllocBase64Buffer() {
        BufferRecycler br = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = false;
        IOContext context = new IOContext(br, sourceRef, managedResource);

        byte[] buffer = context.allocBase64Buffer();

        assertNotNull(buffer);
    }

    @Test
    public void testAllocTokenBuffer() {
        BufferRecycler br = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = false;
        IOContext context = new IOContext(br, sourceRef, managedResource);

        char[] buffer = context.allocTokenBuffer();

        assertNotNull(buffer);
    }

    @Test
    public void testAllocTokenBufferWithMinSize() {
        BufferRecycler br = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = false;
        IOContext context = new IOContext(br, sourceRef, managedResource);
        int minSize = 1024;

        char[] buffer = context.allocTokenBuffer(minSize);

        assertNotNull(buffer);
        assertTrue(buffer.length >= minSize);
    }

    @Test
    public void testAllocConcatBuffer() {
        BufferRecycler br = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = false;
        IOContext context = new IOContext(br, sourceRef, managedResource);

        char[] buffer = context.allocConcatBuffer();

        assertNotNull(buffer);
    }

    @Test
    public void testAllocNameCopyBufferWithMinSize() {
        BufferRecycler br = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = false;
        IOContext context = new IOContext(br, sourceRef, managedResource);
        int minSize = 1024;

        char[] buffer = context.allocNameCopyBuffer(minSize);

        assertNotNull(buffer);
        assertTrue(buffer.length >= minSize);
    }

    @Test
    public void testReleaseReadIOBuffer() {
        BufferRecycler br = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = false;
        IOContext context = new IOContext(br, sourceRef, managedResource);
        byte[] buffer = context.allocReadIOBuffer();

        context.releaseReadIOBuffer(buffer);

        assertNull(context._readIOBuffer);
    }

    @Test
    public void testReleaseWriteEncodingBuffer() {
        BufferRecycler br = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = false;
        IOContext context = new IOContext(br, sourceRef, managedResource);
        byte[] buffer = context.allocWriteEncodingBuffer();

        context.releaseWriteEncodingBuffer(buffer);

        assertNull(context._writeEncodingBuffer);
    }

    @Test
    public void testReleaseBase64Buffer() {
        BufferRecycler br = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = false;
        IOContext context = new IOContext(br, sourceRef, managedResource);
        byte[] buffer = context.allocBase64Buffer();

        context.releaseBase64Buffer(buffer);

        assertNull(context._base64Buffer);
    }

    @Test
    public void testReleaseTokenBuffer() {
        BufferRecycler br = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = false;
        IOContext context = new IOContext(br, sourceRef, managedResource);
        char[] buffer = context.allocTokenBuffer();

        context.releaseTokenBuffer(buffer);

        assertNull(context._tokenCBuffer);
    }

    @Test
    public void testReleaseConcatBuffer() {
        BufferRecycler br = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = false;
        IOContext context = new IOContext(br, sourceRef, managedResource);
        char[] buffer = context.allocConcatBuffer();

        context.releaseConcatBuffer(buffer);

        assertNull(context._concatCBuffer);
    }

    @Test
    public void testReleaseNameCopyBuffer() {
        BufferRecycler br = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = false;
        IOContext context = new IOContext(br, sourceRef, managedResource);
        char[] buffer = context.allocNameCopyBuffer(1024);

        context.releaseNameCopyBuffer(buffer);

        assertNull(context._nameCopyBuffer);
    }

}