import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.util.BufferRecycler;
import com.fasterxml.jackson.core.util.TextBuffer;

public class IOContextTest {

    private IOContext ioContext;

    @Before
    public void setUp() {
        ioContext = new IOContext(new BufferRecycler(), "testSource", true);
    }

    @Test
    public void testSetAndGetEncoding() {
        assertNull(ioContext.getEncoding());
        ioContext.setEncoding(JsonEncoding.UTF8);
        assertEquals(JsonEncoding.UTF8, ioContext.getEncoding());
    }

    @Test
    public void testWithEncoding() {
        assertNull(ioContext.getEncoding());
        ioContext.withEncoding(JsonEncoding.UTF16);
        assertEquals(JsonEncoding.UTF16, ioContext.getEncoding());
    }

    @Test
    public void testGetSourceReference() {
        assertEquals("testSource", ioContext.getSourceReference());
    }

    @Test
    public void testIsResourceManaged() {
        assertTrue(ioContext.isResourceManaged());
    }

    @Test
    public void testAllocReadIOBuffer() {
        assertNotNull(ioContext.allocReadIOBuffer());
    }

    @Test
    public void testAllocReadIOBufferWithMinSize() {
        assertNotNull(ioContext.allocReadIOBuffer(1024));
    }

    // Additional test cases for other methods should be added here

    @Test
    public void testReleaseReadIOBuffer() {
        byte[] buffer = ioContext.allocReadIOBuffer();
        ioContext.releaseReadIOBuffer(buffer);
    }

    // Additional test cases for release methods should be added here

}