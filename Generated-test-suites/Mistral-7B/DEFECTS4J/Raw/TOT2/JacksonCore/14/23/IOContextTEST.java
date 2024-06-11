import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class IOContextTest {

    @Test
    public void testGetSourceReference() {
        IOContext ioContext = new IOContext(new BufferRecycler(), null, false);
        assertNotNull(ioContext.getSourceReference());
    }
}