
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
        boolean managedResource = false;
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
        JsonEncoding encoding = JsonEncoding.UTF16_BE;
        IOContext updated = ioContext.withEncoding(encoding);
        assertEquals(encoding, updated.getEncoding());
    }

    @Test
    public void testGetSourceReference() {
        Object sourceRef = ioContext.getSourceReference();
        assertNotNull(sourceRef);
    }

    @Test
    public void testIsResourceManaged() {
        boolean managed = ioContext.isResourceManaged();
        assertFalse(managed);
    }

    @Test
    public void testConstructTextBuffer() {
        TextBuffer textBuffer = ioContext.constructTextBuffer();
        assertNotNull(textBuffer);
    }

    @Test
    public void testAllocReadIOBuffer() {
        int minSize = 1024;
        byte[] readIOBuffer = ioContext.allocReadIOBuffer(minSize);
        assertNotNull(readIOBuffer);
        assertEquals(minSize, readIOBuffer.length);
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        int minSize = 1024;
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer(minSize);
        assertNotNull(writeEncodingBuffer);
        assertEquals(minSize, writeEncodingBuffer.length);
    }

    @Test
    public void testAllocBase64Buffer() {
        byte[] base64Buffer = ioContext.allocBase64Buffer();
        assertNotNull(base64Buffer);
    }

    @Test
    public void testAllocTokenBuffer() {
        int minSize = 1024;
        char[] tokenBuffer = ioContext.allocTokenBuffer(minSize);
        assertNotNull(tokenBuffer);
        assertEquals(minSize, tokenBuffer.length);
    }

    @Test
    public void testAllocConcatBuffer() {
        char[] concatBuffer = ioContext.allocConcatBuffer();
        assertNotNull(concatBuffer);
    }

    @Test
    public void testAllocNameCopyBuffer() {
        int minSize = 1024;
        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(minSize);
        assertNotNull(nameCopyBuffer);
        assertEquals(minSize, nameCopyBuffer.length);
    }

    @Test
    public void testReleaseReadIOBuffer() {
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        ioContext.releaseReadIOBuffer(readIOBuffer);
        assertNull(ioContext._readIOBuffer);
    }

    @Test
    public void testReleaseWriteEncodingBuffer() {
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        ioContext.releaseWriteEncodingBuffer(writeEncodingBuffer);
        assertNull(ioContext._writeEncodingBuffer);
    }

    @Test
    public void testReleaseBase64Buffer() {
        byte[] base64Buffer = ioContext.allocBase64Buffer();
        ioContext.releaseBase64Buffer(base64Buffer);
        assertNull(ioContext._base64Buffer);
    }

    @Test
    public void testReleaseTokenBuffer() {
        char[] tokenBuffer = ioContext.allocTokenBuffer();
        ioContext.releaseTokenBuffer(tokenBuffer);
        assertNull(ioContext._tokenCBuffer);
    }

    @Test
    public void testReleaseConcatBuffer() {
        char[] concatBuffer = ioContext.allocConcatBuffer();
        ioContext.releaseConcatBuffer(concatBuffer);
        assertNull(ioContext._concatCBuffer);
    }

    @Test
    public void testReleaseNameCopyBuffer() {
        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(1024);
        ioContext.releaseNameCopyBuffer(nameCopyBuffer);
        assertNull(ioContext._nameCopyBuffer);
    }
}
