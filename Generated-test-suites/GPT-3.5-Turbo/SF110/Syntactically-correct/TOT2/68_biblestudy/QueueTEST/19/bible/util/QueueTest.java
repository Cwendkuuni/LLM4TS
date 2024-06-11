package bible.util;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class QueueTest {

    private Queue queue;

    @Before
    public void setUp() {
        queue = new Queue();
    }

    @Test
    public void testIsEmpty() {
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testEnqueue() {
        Object obj = new Object();
        queue.enqueue(obj);
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testDequeue() {
        Object obj = new Object();
        queue.enqueue(obj);
        assertEquals(obj, queue.dequeue());
    }

    // Add more test cases for other methods as needed

}