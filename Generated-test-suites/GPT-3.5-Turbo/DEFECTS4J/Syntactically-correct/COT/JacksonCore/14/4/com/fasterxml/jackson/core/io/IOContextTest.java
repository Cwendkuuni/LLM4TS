
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
    public void testSetAndGetEncoding() {
        JsonEncoding encoding = JsonEncoding.UTF8;
        ioContext.setEncoding(encoding);
        assertEquals(encoding, ioContext.getEncoding());
    }

    @Test
    public void testSourceReference() {
        Object sourceRef = new Object();
        ioContext = new IOContext(null, sourceRef, false);
        assertEquals(sourceRef, ioContext.getSourceReference());
    }

    @Test
    public void testIsResourceManaged() {
        assertFalse(ioContext.isResourceManaged());
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
        assertEquals(BufferRecycler.BYTE_READ_IO_BUFFER, buffer.length);
    }

    @Test
    public void testAllocReadIOBufferWithMinSize() {
        int minSize = 100;
        byte[] buffer = ioContext.allocReadIOBuffer(minSize);
        assertNotNull(buffer);
        assertTrue(buffer.length >= minSize);
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        byte[] buffer = ioContext.allocWriteEncodingBuffer();
        assertNotNull(buffer);
        assertEquals(BufferRecycler.BYTE_WRITE_ENCODING_BUFFER, buffer.length);
    }

    @Test
    public void testAllocWriteEncodingBufferWithMinSize() {
        int minSize = 100;
        byte[] buffer = ioContext.allocWriteEncodingBuffer(minSize);
        assertNotNull(buffer);
        assertTrue(buffer.length >= minSize);
    }

    @Test
    public void testAllocBase64Buffer() {
        byte[] buffer = ioContext.allocBase64Buffer();
        assertNotNull(buffer);
        assertEquals(BufferRecycler.BYTE_BASE64_CODEC_BUFFER, buffer.length);
    }

    @Test
    public void testAllocTokenBuffer() {
        char[] buffer = ioContext.allocTokenBuffer();
        assertNotNull(buffer);
        assertEquals(BufferRecycler.CHAR_TOKEN_BUFFER, buffer.length);
    }

    @Test
    public void testAllocTokenBufferWithMinSize() {
        int minSize = 100;
        char[] buffer = ioContext.allocTokenBuffer(minSize);
        assertNotNull(buffer);
        assertTrue(buffer.length >= minSize);
    }

    @Test
    public void testAllocConcatBuffer() {
        char[] buffer = ioContext.allocConcatBuffer();
        assertNotNull(buffer);
        assertEquals(BufferRecycler.CHAR_CONCAT_BUFFER, buffer.length);
    }

    @Test
    public void testAllocNameCopyBuffer() {
        int minSize = 100;
        char[] buffer = ioContext.allocNameCopyBuffer(minSize);
        assertNotNull(buffer);
        assertTrue(buffer.length >= minSize);
    }

    @Test(expected = IllegalStateException.class)
    public void testVerifyAllocThrowsException() {
        byte[] buffer = new byte[10];
        ioContext._verifyAlloc(buffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifyReleaseThrowsException() {
        byte[] toRelease = new byte[5];
        byte[] src = new byte[10];
        ioContext._verifyRelease(toRelease, src);
    }
}
