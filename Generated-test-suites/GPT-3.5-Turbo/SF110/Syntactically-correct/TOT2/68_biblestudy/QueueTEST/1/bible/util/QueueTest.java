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
        assertEquals(1, queue.getNumberItems());
    }

    @Test
    public void testDequeue() {
        Object obj = new Object();
        queue.enqueue(obj);
        Object dequeuedObj = queue.dequeue();
        assertEquals(obj, dequeuedObj);
        assertTrue(queue.isEmpty());
    }

    // Additional test cases for other methods should be added here

}