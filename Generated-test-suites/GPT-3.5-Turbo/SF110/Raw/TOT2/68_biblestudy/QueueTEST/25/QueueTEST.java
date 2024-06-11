import org.junit.Test;
import static org.junit.Assert.*;

public class QueueTest {

    @Test
    public void testEmptyQueue() {
        Queue q = new Queue();
        assertTrue(q.isEmpty());
    }

    @Test
    public void testEnqueue() {
        Queue q = new Queue();
        q.enqueue("Test");
        assertFalse(q.isEmpty());
    }

    @Test
    public void testDequeue() {
        Queue q = new Queue();
        q.enqueue("Test");
        assertEquals("Test", q.dequeue());
        assertTrue(q.isEmpty());
    }

    @Test
    public void testRemove() {
        Queue q = new Queue();
        q.enqueue("Test");
        assertEquals(1, q.remove("Test"));
        assertTrue(q.isEmpty());
    }

    @Test
    public void testRefreshElement() {
        Queue q = new Queue();
        q.enqueue("Test");
        q.refreshElement("Test");
        assertEquals(1, q.getNumberItems());
    }

    @Test
    public void testMaxCapacityExceeded() {
        Queue q = new Queue(1);
        q.enqueue("Item 1");
        assertTrue(q.maxCapacityExceeded());
    }

    // Add more test cases for other methods

}