import static org.junit.Assert.*;
import org.junit.Test;

public class QueueTest {

    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testEnqueue() {
        Queue queue = new Queue();
        queue.enqueue("A");
        assertEquals(1, queue.getNumberItems());
    }

    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        queue.enqueue("A");
        Object obj = queue.dequeue();
        assertEquals("A", obj);
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testRemove() {
        Queue queue = new Queue();
        queue.enqueue("A");
        queue.enqueue("B");
        int removed = queue.remove("A");
        assertEquals(1, removed);
        assertEquals(1, queue.getNumberItems());
    }
    
    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("A");
        queue.enqueue("B");
        queue.refreshElement("A");
        assertEquals(2, queue.getNumberItems());
    }

    @Test
    public void testGetNumberItems() {
        Queue queue = new Queue();
        queue.enqueue("A");
        assertEquals(1, queue.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems() {
        Queue queue = new Queue();
        queue.enqueue("A");
        assertEquals(1, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        Queue queue = new Queue();
        queue.enqueue("A");
        queue.enqueue("B");
        queue.enqueue("C");
        Vector expected = new Vector();
        expected.addElement("A");
        expected.addElement("B");
        expected.addElement("C");
        assertEquals(expected, queue.getObjects());
    }

    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(2);
        queue.enqueue("A");
        queue.enqueue("B");
        assertTrue(queue.maxCapacityExceeded());
    }

    @Test
    public void testToString() {
        Queue queue = new Queue();
        queue.enqueue("A");
        queue.enqueue("B");
        String expected = "Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[A, B]\r\n]";
        assertEquals(expected, queue.toString());
    }

}