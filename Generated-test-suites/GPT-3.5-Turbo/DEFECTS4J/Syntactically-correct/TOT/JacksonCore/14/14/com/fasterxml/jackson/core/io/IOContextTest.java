import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.BufferRecycler;
import com.fasterxml.jackson.core.util.TextBuffer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

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
        JsonEncoding expectedEncoding = JsonEncoding.UTF8;
        ioContext.setEncoding(expectedEncoding);
        JsonEncoding actualEncoding = ioContext.getEncoding();
        assertEquals(expectedEncoding, actualEncoding);
    }

    @Test
    public void testWithEncoding() {
        JsonEncoding expectedEncoding = JsonEncoding.UTF8;
        IOContext updatedIOContext = ioContext.withEncoding(expectedEncoding);
        JsonEncoding actualEncoding = updatedIOContext.getEncoding();
        assertEquals(expectedEncoding, actualEncoding);
    }

    @Test
    public void testGetSourceReference() {
        Object expectedSourceRef = new Object();
        ioContext = new IOContext(new BufferRecycler(), expectedSourceRef, true);
        Object actualSourceRef = ioContext.getSourceReference();
        assertEquals(expectedSourceRef, actualSourceRef);
    }

    @Test
    public void testIsResourceManaged() {
        boolean expectedManagedResource = false;
        ioContext = new IOContext(new BufferRecycler(), new Object(), expectedManagedResource);
        boolean actualManagedResource = ioContext.isResourceManaged();
        assertEquals(expectedManagedResource, actualManagedResource);
    }

    @Test
    public void testConstructTextBuffer() {
        TextBuffer textBuffer = ioContext.constructTextBuffer();
        assertNotNull(textBuffer);
    }

    @Test
    public void testAllocReadIOBuffer() {
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        assertNotNull(readIOBuffer);
    }

    @Test
    public void testAllocReadIOBufferWithMinSize() {
        int minSize = 1024;
        byte[] readIOBuffer = ioContext.allocReadIOBuffer(minSize);
        assertNotNull(readIOBuffer);
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        assertNotNull(writeEncodingBuffer);
    }

    @Test
    public void testAllocWriteEncodingBufferWithMinSize() {
        int minSize = 1024;
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer(minSize);
        assertNotNull(writeEncodingBuffer);
    }

    @Test
    public void testAllocBase64Buffer() {
        byte[] base64Buffer = ioContext.allocBase64Buffer();
        assertNotNull(base64Buffer);
    }

    @Test
    public void testAllocTokenBuffer() {
        char[] tokenBuffer = ioContext.allocTokenBuffer();
        assertNotNull(tokenBuffer);
    }

    @Test
    public void testAllocTokenBufferWithMinSize() {
        int minSize = 1024;
        char[] tokenBuffer = ioContext.allocTokenBuffer(minSize);
        assertNotNull(tokenBuffer);
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