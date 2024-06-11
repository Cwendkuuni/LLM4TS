import org.junit.Test;
import com.fasterxml.jackson.core.io.IOContext;

public class IOContextTest {
    @Test
    public void testIOContextConstruction() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        assertNotNull(ioContext);
        assertEquals(true, ioContext.isResourceManaged());
    }
}