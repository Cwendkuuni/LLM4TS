import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class IOContextTest {

    // Test cases for public methods

    @Test
    public void constructTextBufferTest() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        TextBuffer textBuffer = ioContext.constructTextBuffer();
        assertNotNull(textBuffer);
    }

    @Test
    public void allocReadIOBufferTest() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        byte[] buffer = ioContext.allocReadIOBuffer();
        assertNotNull(buffer);
    }

    @Test
    public void allocReadIOBufferWithSizeTest() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        byte[] buffer = ioContext.allocReadIOBuffer(1024);
        assertNotNull(buffer);
        assertEquals(1024, buffer.length);
    }

    // Additional test cases for edge cases and exceptions
    @Test(expected = IllegalStateException.class)
    public void allocReadIOBufferTwiceTest() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        ioContext.allocReadIOBuffer();
        ioContext.allocReadIOBuffer();
    }

    @Test(expected = IllegalArgumentException.class)
    public void releaseWrongReadIOBufferTest() {
        IOContext ioContext = new IOContext(new BufferRecycler(), new Object(), true);
        byte[] buffer1 = ioContext.allocReadIOBuffer();
        byte[] buffer2 = ioContext.allocReadIOBuffer(1024);
        ioContext.releaseReadIOBuffer(buffer2);
    }

    // Add more test cases for other methods as needed

}