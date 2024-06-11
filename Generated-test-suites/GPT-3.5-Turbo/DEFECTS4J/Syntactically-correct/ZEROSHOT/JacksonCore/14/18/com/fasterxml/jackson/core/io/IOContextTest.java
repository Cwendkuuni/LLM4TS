import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.util.BufferRecycler;
import com.fasterxml.jackson.core.util.TextBuffer;

public class IOContextTest {

    private IOContext ioContext;
    
    @Before
    public void setUp() {
        ioContext = new IOContext(new BufferRecycler(), "sourceRef", true);
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
        assertEquals(ioContext, ioContext.withEncoding(encoding));
        assertEquals(encoding, ioContext.getEncoding());
    }

    @Test
    public void testGetSourceReference() {
        assertEquals("sourceRef", ioContext.getSourceReference());
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
        assertNull(ioContext._readIOBuffer);
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        assertNotNull(readIOBuffer);
        assertSame(readIOBuffer, ioContext._readIOBuffer);
    }

    @Test
    public void testAllocReadIOBufferWithMinSize() {
        assertNull(ioContext._readIOBuffer);
        int minSize = 1024;
        byte[] readIOBuffer = ioContext.allocReadIOBuffer(minSize);
        assertNotNull(readIOBuffer);
        assertSame(readIOBuffer, ioContext._readIOBuffer);
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        assertNull(ioContext._writeEncodingBuffer);
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        assertNotNull(writeEncodingBuffer);
        assertSame(writeEncodingBuffer, ioContext._writeEncodingBuffer);
    }

    @Test
    public void testAllocWriteEncodingBufferWithMinSize() {
        assertNull(ioContext._writeEncodingBuffer);
        int minSize = 1024;
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer(minSize);
        assertNotNull(writeEncodingBuffer);
        assertSame(writeEncodingBuffer, ioContext._writeEncodingBuffer);
    }

    @Test
    public void testAllocBase64Buffer() {
        assertNull(ioContext._base64Buffer);
        byte[] base64Buffer = ioContext.allocBase64Buffer();
        assertNotNull(base64Buffer);
        assertSame(base64Buffer, ioContext._base64Buffer);
    }

    @Test
    public void testAllocTokenBuffer() {
        assertNull(ioContext._tokenCBuffer);
        char[] tokenBuffer = ioContext.allocTokenBuffer();
        assertNotNull(tokenBuffer);
        assertSame(tokenBuffer, ioContext._tokenCBuffer);
    }

    @Test
    public void testAllocTokenBufferWithMinSize() {
        assertNull(ioContext._tokenCBuffer);
        int minSize = 1024;
        char[] tokenBuffer = ioContext.allocTokenBuffer(minSize);
        assertNotNull(tokenBuffer);
        assertSame(tokenBuffer, ioContext._tokenCBuffer);
    }

    @Test
    public void testAllocConcatBuffer() {
        assertNull(ioContext._concatCBuffer);
        char[] concatBuffer = ioContext.allocConcatBuffer();
        assertNotNull(concatBuffer);
        assertSame(concatBuffer, ioContext._concatCBuffer);
    }

    @Test
    public void testAllocNameCopyBuffer() {
        assertNull(ioContext._nameCopyBuffer);
        int minSize = 1024;
        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(minSize);
        assertNotNull(nameCopyBuffer);
        assertSame(nameCopyBuffer, ioContext._nameCopyBuffer);
    }

    @Test
    public void testReleaseReadIOBuffer() {
        byte[] buf = new byte[1024];
        ioContext._readIOBuffer = buf;
        ioContext.releaseReadIOBuffer(buf);
        assertNull(ioContext._readIOBuffer);
    }

    @Test
    public void testReleaseWriteEncodingBuffer() {
        byte[] buf = new byte[1024];
        ioContext._writeEncodingBuffer = buf;
        ioContext.releaseWriteEncodingBuffer(buf);
        assertNull(ioContext._writeEncodingBuffer);
    }

    @Test
    public void testReleaseBase64Buffer() {
        byte[] buf = new byte[1024];
        ioContext._base64Buffer = buf;
        ioContext.releaseBase64Buffer(buf);
        assertNull(ioContext._base64Buffer);
    }

    @Test
    public void testReleaseTokenBuffer() {
        char[] buf = new char[1024];
        ioContext._tokenCBuffer = buf;
        ioContext.releaseTokenBuffer(buf);
        assertNull(ioContext._tokenCBuffer);
    }

    @Test
    public void testReleaseConcatBuffer() {
        char[] buf = new char[1024];
        ioContext._concatCBuffer = buf;
        ioContext.releaseConcatBuffer(buf);
        assertNull(ioContext._concatCBuffer);
    }

    @Test
    public void testReleaseNameCopyBuffer() {
        char[] buf = new char[1024];
        ioContext._nameCopyBuffer = buf;
        ioContext.releaseNameCopyBuffer(buf);
        assertNull(ioContext._nameCopyBuffer);
    }

}