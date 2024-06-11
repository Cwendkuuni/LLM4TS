import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.util.BufferRecycler;
import com.fasterxml.jackson.core.util.TextBuffer;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;



public class IOContextTest {

    private IOContext ioContext;

    @Before
    public void setUp() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        Object sourceRef = new Object();
        boolean managedResource = true;
        ioContext = new IOContext(bufferRecycler, sourceRef, managedResource);
    }

    @Test
    public void testSetEncoding() {
        JsonEncoding encoding = JsonEncoding.UTF8;
        ioContext.setEncoding(encoding);
        assertEquals(encoding, ioContext.getEncoding());
    }

    @Test
    public void testWithEncoding() {
        JsonEncoding encoding = JsonEncoding.UTF8;
        IOContext modifiedContext = ioContext.withEncoding(encoding);
        assertEquals(encoding, modifiedContext.getEncoding());
    }

    @Test
    public void testGetSourceReference() {
        Object sourceRef = new Object();
        ioContext = new IOContext(null, sourceRef, true);
        assertEquals(sourceRef, ioContext.getSourceReference());
    }

    @Test
    public void testIsResourceManaged() {
        assertTrue(ioContext.isResourceManaged());
    }

    @Test
    public void testConstructTextBuffer() {
        TextBuffer textBuffer = ioContext.constructTextBuffer();
        assertNotNull(textBuffer);
    }

    @Test
    public void testAllocReadIOBuffer() {
        byte[] buffer = ioContext.allocReadIOBuffer();
        assertNotNull(buffer);
    }

    @Test
    public void testAllocReadIOBufferWithMinSize() {
        int minSize = 128;
        byte[] buffer = ioContext.allocReadIOBuffer(minSize);
        assertNotNull(buffer);
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        byte[] buffer = ioContext.allocWriteEncodingBuffer();
        assertNotNull(buffer);
    }

    @Test
    public void testAllocWriteEncodingBufferWithMinSize() {
        int minSize = 128;
        byte[] buffer = ioContext.allocWriteEncodingBuffer(minSize);
        assertNotNull(buffer);
    }

    @Test
    public void testAllocBase64Buffer() {
        byte[] buffer = ioContext.allocBase64Buffer();
        assertNotNull(buffer);
    }

    @Test
    public void testAllocTokenBuffer() {
        char[] buffer = ioContext.allocTokenBuffer();
        assertNotNull(buffer);
    }

    @Test
    public void testAllocTokenBufferWithMinSize() {
        int minSize = 128;
        char[] buffer = ioContext.allocTokenBuffer(minSize);
        assertNotNull(buffer);
    }

    @Test
    public void testAllocConcatBuffer() {
        char[] buffer = ioContext.allocConcatBuffer();
        assertNotNull(buffer);
    }

    @Test
    public void testAllocNameCopyBufferWithMinSize() {
        int minSize = 128;
        char[] buffer = ioContext.allocNameCopyBuffer(minSize);
        assertNotNull(buffer);
    }

    @Test
    public void testReleaseReadIOBuffer() {
        byte[] buffer = ioContext.allocReadIOBuffer();
        ioContext.releaseReadIOBuffer(buffer);
    }

    @Test
    public void testReleaseWriteEncodingBuffer() {
        byte[] buffer = ioContext.allocWriteEncodingBuffer();
        ioContext.releaseWriteEncodingBuffer(buffer);
    }

    @Test
    public void testReleaseBase64Buffer() {
        byte[] buffer = ioContext.allocBase64Buffer();
        ioContext.releaseBase64Buffer(buffer);
    }

    @Test
    public void testReleaseTokenBuffer() {
        char[] buffer = ioContext.allocTokenBuffer();
        ioContext.releaseTokenBuffer(buffer);
    }

    @Test
    public void testReleaseConcatBuffer() {
        char[] buffer = ioContext.allocConcatBuffer();
        ioContext.releaseConcatBuffer(buffer);
    }

    @Test
    public void testReleaseNameCopyBuffer() {
        char[] buffer = ioContext.allocNameCopyBuffer(128);
        ioContext.releaseNameCopyBuffer(buffer);
    }

}

