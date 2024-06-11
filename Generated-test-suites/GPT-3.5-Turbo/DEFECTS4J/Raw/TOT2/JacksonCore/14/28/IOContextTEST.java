import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.util.BufferRecycler;
import org.junit.Test;
import static org.junit.Assert.*;

public class IOContextTest {

    @Test
    public void testGetSourceReference() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "test", true);
        assertEquals("test", ioContext.getSourceReference());
    }

    @Test
    public void testGetEncoding() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "test", true);
        ioContext.setEncoding(JsonEncoding.UTF8);
        assertEquals(JsonEncoding.UTF8, ioContext.getEncoding());
    }

    @Test
    public void testIsResourceManaged() {
        IOContext ioContext = new IOContext(new BufferRecycler(), "test", true);
        assertTrue(ioContext.isResourceManaged());
    }

    // Add more test cases for other methods as needed

}