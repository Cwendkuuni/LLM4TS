import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class IOContextTest {

    @Test
    public void testSetEncoding() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "source", true);
        ioContext.setEncoding(JsonEncoding.UTF8);
        assertEquals(JsonEncoding.UTF8, ioContext.getEncoding());
    }

    @Test
    public void testWithEncoding() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "source", true);
        ioContext.withEncoding(JsonEncoding.UTF16);
        assertEquals(JsonEncoding.UTF16, ioContext.getEncoding());
    }

    @Test
    public void testSourceReference() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "source", true);
        assertEquals("source", ioContext.getSourceReference());
    }

    @Test
    public void testIsResourceManaged() {
        IOContext ioContextManaged = new IOContext(new BufferRecycler(), "source", true);
        IOContext ioContextNotManaged = new IOContext(new BufferRecycler(), "source", false);
        
        assertTrue(ioContextManaged.isResourceManaged());
        assertFalse(ioContextNotManaged.isResourceManaged());
    }

    // Add more test cases for other methods as needed

}