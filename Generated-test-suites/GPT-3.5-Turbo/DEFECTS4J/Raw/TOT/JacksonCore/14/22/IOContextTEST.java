
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class IOContextTest {

    private IOContext ioContext;

    @Before
    public void setUp() {
        ioContext = new IOContext(null, null, false);
    }

    // Test cases for setEncoding() method

    @Test
    public void testSetEncoding() {
        JsonEncoding enc = JsonEncoding.UTF8;
        ioContext.setEncoding(enc);
        assertEquals(enc, ioContext.getEncoding());
    }

    @Test
    public void testSetEncoding_Null() {
        ioContext.setEncoding(null);
        assertNull(ioContext.getEncoding());
    }

    // Test cases for withEncoding() method

    @Test
    public void testWithEncoding() {
        JsonEncoding enc = JsonEncoding.UTF16_BE;
        IOContext newIoContext = ioContext.withEncoding(enc);
        assertEquals(enc, newIoContext.getEncoding());
    }

    // Test cases for getSourceReference() method

    @Test
    public void testGetSourceReference() {
        Object sourceRef = new Object();
        ioContext = new IOContext(null, sourceRef, false);
        assertEquals(sourceRef, ioContext.getSourceReference());
    }

    // Test cases for getEncoding() method

    @Test
    public void testGetEncoding() {
        JsonEncoding enc = JsonEncoding.UTF8;
        ioContext.setEncoding(enc);
        assertEquals(enc, ioContext.getEncoding());
    }

    // Test cases for isResourceManaged() method

    @Test
    public void testIsResourceManaged_True() {
        ioContext = new IOContext(null, null, true);
        assertTrue(ioContext.isResourceManaged());
    }

    @Test
    public void testIsResourceManaged_False() {
        ioContext = new IOContext(null, null, false);
        assertFalse(ioContext.isResourceManaged());
    }

    // Test cases for constructTextBuffer() method

    @Test
    public void testConstructTextBuffer() {
        TextBuffer textBuffer = ioContext.constructTextBuffer();
        assertNotNull(textBuffer);
    }

    // Test cases for allocReadIOBuffer() method

    @Test
    public void testAllocReadIOBuffer() {
        byte[] buffer = ioContext.allocReadIOBuffer();
        assertNotNull(buffer);
    }

    @Test
    public void testAllocReadIOBuffer_MinSize() {
        int minSize = 1024;
        byte[] buffer = ioContext.allocReadIOBuffer(minSize);
        assertNotNull(buffer);
        assertTrue(buffer.length >= minSize);
    }

    // Test cases for allocWriteEncodingBuffer() method

    @Test
    public void testAllocWriteEncodingBuffer() {
        byte[] buffer = ioContext.allocWriteEncodingBuffer();
        assertNotNull(buffer);
    }

    @Test
    public void testAllocWriteEncodingBuffer_MinSize() {
        int minSize = 1024;
        byte[] buffer = ioContext.allocWriteEncodingBuffer(minSize);
        assertNotNull(buffer);
        assertTrue(buffer.length >= minSize);
    }

    // Test cases for allocBase64Buffer() method

    @Test
    public void testAllocBase64Buffer() {
        byte[] buffer = ioContext.allocBase64Buffer();
        assertNotNull(buffer);
    }

    // Test cases for allocTokenBuffer() method

    @Test
    public void testAllocTokenBuffer() {
        char[] buffer = ioContext.allocTokenBuffer();
        assertNotNull(buffer);
    }

    @Test
    public void testAllocTokenBuffer_MinSize() {
        int minSize = 1024;
        char[] buffer = ioContext.allocTokenBuffer(minSize);
        assertNotNull(buffer);
        assertTrue(buffer.length >= minSize);
    }

    // Test cases for allocConcatBuffer() method

    @Test
    public void testAllocConcatBuffer() {
        char[] buffer = ioContext.allocConcatBuffer();
        assertNotNull(buffer);
    }

    // Test cases for allocNameCopyBuffer() method

    @Test
    public void testAllocNameCopyBuffer() {
        int minSize = 1024;
        char[] buffer = ioContext.allocNameCopyBuffer(minSize);
        assertNotNull(buffer);
        assertTrue(buffer.length >= minSize);
    }

    // Test cases for releaseReadIOBuffer() method

    @Test
    public void testReleaseReadIOBuffer_Null() {
        ioContext.releaseReadIOBuffer(null);
        assertNull(ioContext._readIOBuffer);
    }

    @Test
    public void testReleaseReadIOBuffer_NotOwned() {
        byte[] buf = new byte[1024];
        ioContext._readIOBuffer = buf;
        try {
            ioContext.releaseReadIOBuffer(new byte[512]);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
            assertEquals("Trying to release buffer not owned by the context", e.getMessage());
        }
    }

    // Test cases for releaseWriteEncodingBuffer() method

    @Test
    public void testReleaseWriteEncodingBuffer_Null() {
        ioContext.releaseWriteEncodingBuffer(null);
        assertNull(ioContext._writeEncodingBuffer);
    }

    @Test
    public void testReleaseWriteEncodingBuffer_NotOwned() {
        byte[] buf = new byte[1024];
        ioContext._writeEncodingBuffer = buf;
        try {
            ioContext.releaseWriteEncodingBuffer(new byte[512]);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
            assertEquals("Trying to release buffer not owned by the context", e.getMessage());
        }
    }

    // Test cases for releaseBase64Buffer() method

    @Test
    public void testReleaseBase64Buffer_Null() {
        ioContext.releaseBase64Buffer(null);
        assertNull(ioContext._base64Buffer);
    }

    @Test
    public void testReleaseBase64Buffer_NotOwned() {
        byte[] buf = new byte[1024];
        ioContext._base64Buffer = buf;
        try {
            ioContext.releaseBase64Buffer(new byte[512]);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
            assertEquals("Trying to release buffer not owned by the context", e.getMessage());
        }
    }

    // Test cases for releaseTokenBuffer() method

    @Test
    public void testReleaseTokenBuffer_Null() {
        ioContext.releaseTokenBuffer(null);
        assertNull(ioContext._tokenCBuffer);
    }

    @Test
    public void testReleaseTokenBuffer_NotOwned() {
        char[] buf = new char[1024];
        ioContext._tokenCBuffer = buf;
        try {
            ioContext.releaseTokenBuffer(new char[512]);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
            assertEquals("Trying to release buffer not owned by the context", e.getMessage());
        }
    }

    // Test cases for releaseConcatBuffer() method

    @Test
    public void testReleaseConcatBuffer_Null() {
        ioContext.releaseConcatBuffer(null);
        assertNull(ioContext._concatCBuffer);
    }

    @Test
    public void testReleaseConcatBuffer_NotOwned() {
        char[] buf = new char[1024];
        ioContext._concatCBuffer = buf;
        try {
            ioContext.releaseConcatBuffer(new char[512]);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
            assertEquals("Trying to release buffer not owned by the context", e.getMessage());
        }
    }

    // Test cases for releaseNameCopyBuffer() method

    @Test
    public void testReleaseNameCopyBuffer_Null() {
        ioContext.releaseNameCopyBuffer(null);
        assertNull(ioContext._nameCopyBuffer);
    }

    @Test
    public void testReleaseNameCopyBuffer_NotOwned() {
        char[] buf = new char[1024];
        ioContext._nameCopyBuffer = buf;
        try {
            ioContext.releaseNameCopyBuffer(new char[512]);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
            assertEquals("Trying to release buffer not owned by the context", e.getMessage());
        }
    }
}
