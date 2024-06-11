import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class IOContextTest {

    private IOContext ioContext;

    @Before
    public void setUp() {
        ioContext = new IOContext(null, null, false);
    }

    @Test
    public void testSetEncoding() {
        ioContext.setEncoding(JsonEncoding.UTF8);
        assertEquals(JsonEncoding.UTF8, ioContext.getEncoding());
    }

    @Test
    public void testWithEncoding() {
        ioContext.withEncoding(JsonEncoding.UTF16_BE);
        assertEquals(JsonEncoding.UTF16_BE, ioContext.getEncoding());
    }

    @Test
    public void testGetSourceReference() {
        assertNull(ioContext.getSourceReference());
    }

    @Test
    public void testIsResourceManaged() {
        assertFalse(ioContext.isResourceManaged());
    }

    @Test
    public void testConstructTextBuffer() {
        TextBuffer textBuffer = ioContext.constructTextBuffer();
        assertNotNull(textBuffer);
        assertTrue(textBuffer.isEmpty());
    }

    @Test
    public void testAllocReadIOBuffer() {
        byte[] readIOBuffer = ioContext.allocReadIOBuffer();
        assertNotNull(readIOBuffer);
        assertEquals(0, readIOBuffer.length);
    }

    @Test
    public void testAllocReadIOBufferWithMinSize() {
        byte[] readIOBuffer = ioContext.allocReadIOBuffer(1024);
        assertNotNull(readIOBuffer);
        assertEquals(1024, readIOBuffer.length);
    }

    @Test
    public void testAllocWriteEncodingBuffer() {
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer();
        assertNotNull(writeEncodingBuffer);
        assertEquals(0, writeEncodingBuffer.length);
    }

    @Test
    public void testAllocWriteEncodingBufferWithMinSize() {
        byte[] writeEncodingBuffer = ioContext.allocWriteEncodingBuffer(512);
        assertNotNull(writeEncodingBuffer);
        assertEquals(512, writeEncodingBuffer.length);
    }

    @Test
    public void testAllocBase64Buffer() {
        byte[] base64Buffer = ioContext.allocBase64Buffer();
        assertNotNull(base64Buffer);
        assertEquals(0, base64Buffer.length);
    }

    @Test
    public void testAllocTokenBuffer() {
        char[] tokenBuffer = ioContext.allocTokenBuffer();
        assertNotNull(tokenBuffer);
        assertEquals(0, tokenBuffer.length);
    }

    @Test
    public void testAllocTokenBufferWithMinSize() {
        char[] tokenBuffer = ioContext.allocTokenBuffer(256);
        assertNotNull(tokenBuffer);
        assertEquals(256, tokenBuffer.length);
    }

    @Test
    public void testAllocConcatBuffer() {
        char[] concatBuffer = ioContext.allocConcatBuffer();
        assertNotNull(concatBuffer);
        assertEquals(0, concatBuffer.length);
    }

    @Test
    public void testAllocNameCopyBuffer() {
        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(128);
        assertNotNull(nameCopyBuffer);
        assertEquals(128, nameCopyBuffer.length);
    }

    @Test(expected = IllegalStateException.class)
    public void testVerifyAllocWithNotNullBuffer() {
        ioContext.allocReadIOBuffer();
        ioContext._verifyAlloc(ioContext._readIOBuffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifyReleaseWithDifferentBuffer() {
        byte[] toRelease = new byte[1024];
        byte[] src = new byte[2048];
        ioContext._verifyRelease(toRelease, src);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifyReleaseWithSmallerBuffer() {
        byte[] toRelease = new byte[1024];
        byte[] src = new byte[512];
        ioContext._verifyRelease(toRelease, src);
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
        char[] nameCopyBuffer = ioContext.allocNameCopyBuffer(128);
        ioContext.releaseNameCopyBuffer(nameCopyBuffer);
        assertNull(ioContext._nameCopyBuffer);
    }

}