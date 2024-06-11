import org.junit.Test;
import static org.junit.Assert.*;

public class IOContextTest {

    @Test
    public void testConstructTextBuffer() {
        BufferRecycler br = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        IOContext ioContext = new IOContext(br, sourceRef, managedResource);

        TextBuffer textBuffer = ioContext.constructTextBuffer();

        assertNotNull(textBuffer);
    }

    @Test
    public void testAllocReadIOBuffer() {
        BufferRecycler br = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        IOContext ioContext = new IOContext(br, sourceRef, managedResource);

        byte[] buffer = ioContext.allocReadIOBuffer();

        assertNotNull(buffer);
    }

    @Test
    public void testAllocReadIOBufferWithMinSize() {
        BufferRecycler br = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        IOContext ioContext = new IOContext(br, sourceRef, managedResource);
        int minSize = 10;

        byte[] buffer = ioContext.allocReadIOBuffer(minSize);

        assertNotNull(buffer);
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        BufferRecycler br = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        IOContext ioContext = new IOContext(br, sourceRef, managedResource);

        byte[] buffer = ioContext.allocWriteEncodingBuffer();

        assertNotNull(buffer);
    }

    @Test
    public void testAllocWriteEncodingBufferWithMinSize() {
        BufferRecycler br = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        IOContext ioContext = new IOContext(br, sourceRef, managedResource);
        int minSize = 10;

        byte[] buffer = ioContext.allocWriteEncodingBuffer(minSize);

        assertNotNull(buffer);
    }

    @Test
    public void testAllocBase64Buffer() {
        BufferRecycler br = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        IOContext ioContext = new IOContext(br, sourceRef, managedResource);

        byte[] buffer = ioContext.allocBase64Buffer();

        assertNotNull(buffer);
    }

    @Test
    public void testAllocTokenBuffer() {
        BufferRecycler br = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        IOContext ioContext = new IOContext(br, sourceRef, managedResource);

        char[] buffer = ioContext.allocTokenBuffer();

        assertNotNull(buffer);
    }

    @Test
    public void testAllocTokenBufferWithMinSize() {
        BufferRecycler br = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        IOContext ioContext = new IOContext(br, sourceRef, managedResource);
        int minSize = 10;

        char[] buffer = ioContext.allocTokenBuffer(minSize);

        assertNotNull(buffer);
    }

    @Test
    public void testAllocConcatBuffer() {
        BufferRecycler br = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        IOContext ioContext = new IOContext(br, sourceRef, managedResource);

        char[] buffer = ioContext.allocConcatBuffer();

        assertNotNull(buffer);
    }

    @Test
    public void testAllocNameCopyBuffer() {
        BufferRecycler br = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        IOContext ioContext = new IOContext(br, sourceRef, managedResource);
        int minSize = 10;

        char[] buffer = ioContext.allocNameCopyBuffer(minSize);

        assertNotNull(buffer);
    }

    @Test
    public void testReleaseReadIOBuffer() {
        BufferRecycler br = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        IOContext ioContext = new IOContext(br, sourceRef, managedResource);
        byte[] buffer = ioContext.allocReadIOBuffer();

        ioContext.releaseReadIOBuffer(buffer);

        assertNull(ioContext._readIOBuffer);
    }

    @Test
    public void testReleaseWriteEncodingBuffer() {
        BufferRecycler br = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        IOContext ioContext = new IOContext(br, sourceRef, managedResource);
        byte[] buffer = ioContext.allocWriteEncodingBuffer();

        ioContext.releaseWriteEncodingBuffer(buffer);

        assertNull(ioContext._writeEncodingBuffer);
    }

    @Test
    public void testReleaseBase64Buffer() {
        BufferRecycler br = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        IOContext ioContext = new IOContext(br, sourceRef, managedResource);
        byte[] buffer = ioContext.allocBase64Buffer();

        ioContext.releaseBase64Buffer(buffer);

        assertNull(ioContext._base64Buffer);
    }

    @Test
    public void testReleaseTokenBuffer() {
        BufferRecycler br = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        IOContext ioContext = new IOContext(br, sourceRef, managedResource);
        char[] buffer = ioContext.allocTokenBuffer();

        ioContext.releaseTokenBuffer(buffer);

        assertNull(ioContext._tokenCBuffer);
    }

    @Test
    public void testReleaseConcatBuffer() {
        BufferRecycler br = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        IOContext ioContext = new IOContext(br, sourceRef, managedResource);
        char[] buffer = ioContext.allocConcatBuffer();

        ioContext.releaseConcatBuffer(buffer);

        assertNull(ioContext._concatCBuffer);
    }

    @Test
    public void testReleaseNameCopyBuffer() {
        BufferRecycler br = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        IOContext ioContext = new IOContext(br, sourceRef, managedResource);
        char[] buffer = ioContext.allocNameCopyBuffer(10);

        ioContext.releaseNameCopyBuffer(buffer);

        assertNull(ioContext._nameCopyBuffer);
    }
}