import static org.junit.Assert.*;
import org.junit.*;

public class QueueTest {

    @Test
    public void testIsEmpty() {
        // Test when the queue is empty
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());

        // Test when the queue is not empty
        queue.enqueue("element");
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testEnqueue() {
        // Test enqueueing null object
        Queue queue = new Queue();
        int initialNumItems = queue.getNumberItems();
        queue.enqueue(null);
        int finalNumItems = queue.getNumberItems();
        assertEquals(initialNumItems, finalNumItems);

        // Test enqueueing non-null object
        queue.enqueue("element");
        assertEquals(initialNumItems + 1, queue.getNumberItems());
        assertTrue(queue.getObjects().contains("element"));
    }

    @Test
    public void testDequeue() {
        // Test dequeuing from an empty queue
        Queue queue = new Queue();
        assertNull(queue.dequeue());

        // Test dequeuing from a non-empty queue
        queue.enqueue("element1");
        queue.enqueue("element2");
        assertEquals("element1", queue.dequeue());
        assertEquals(1, queue.getNumberItems());
    }

     @Test
    public void testRemove() {
        // Test removing from an empty queue
        Queue queue = new Queue();
        int initialNumItems = queue.getNumberItems();
        assertEquals(0, queue.remove("element"));

        // Test removing from a non-empty queue
        queue.enqueue("element1");
        queue.enqueue("element2");
        queue.enqueue("element1");
        int removedItems = queue.remove("element1");
        assertEquals(2, removedItems);
        assertEquals(initialNumItems + removedItems, queue.getNumberItems());
    }

    @Test
    public void testRefreshElement() {
        // Test refreshing an element that does not exist in the queue
        Queue queue = new Queue();
        queue.enqueue("element1");
        int initialNumItems = queue.getNumberItems();
        queue.refreshElement("element2");
        assertEquals(initialNumItems, queue.getNumberItems());

        // Test refreshing an element that exists in the queue
        queue.refreshElement("element1");
        assertEquals(initialNumItems, queue.getNumberItems());
        assertTrue(queue.getObjects().contains("element1"));
    }

    @Test
    public void testGetNumberItems() {
        // Test getting number of items from an empty queue
        Queue queue = new Queue();
        assertEquals(0, queue.getNumberItems());

        // Test getting number of items from a non-empty queue
        queue.enqueue("element1");
        queue.enqueue("element2");
        assertEquals(2, queue.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems() {
        Queue queue = new Queue();

        // Test getting peak number of items from an empty queue
        assertEquals(0, queue.getPeakNumberItems());

        // Test getting peak number of items from a non-empty queue
        queue.enqueue("element1");
        queue.enqueue("element2");
        assertEquals(2, queue.getPeakNumberItems());

        // Test getting peak number of items after removing
        queue.dequeue();
        assertEquals(2, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        Queue queue = new Queue();

        // Test getting objects from an empty queue
        assertEquals(0, queue.getObjects().size());

        // Test getting objects from a non-empty queue
        queue.enqueue("element1");
        queue.enqueue("element2");
        assertTrue(queue.getObjects().contains("element1"));
        assertTrue(queue.getObjects().contains("element2"));
    }

    @Test
    public void testMaxCapacityExceeded() {
        // Test max capacity not exceeded
        Queue queue = new Queue(3);
        queue.enqueue("element1");
        queue.enqueue("element2");
        assertFalse(queue.maxCapacityExceeded());

        // Test max capacity exceeded
        queue.enqueue("element3");
        assertTrue(queue.maxCapacityExceeded());
    }

    @Test
    public void testToString() {
        Queue queue = new Queue();

        // Test toString when the queue is empty
        assertEquals("bible.util.Queue:[numItems=0, maxNumItems=0, maxCapacity=-1, getObjects()=[]\r\n]", queue.toString());

        // Test toString when the queue is not empty
        queue.enqueue("element1");
        queue.enqueue("element2");
        assertEquals("bible.util.Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[element1, element2]\r\n]", queue.toString());
    }

}