
package bible.util;

import org.junit.*;
import static org.junit.Assert.*;

public class QueueTest {

    @Test
    public void testQueueConstructor1() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.getNumberItems());
        assertEquals(-1, queue.getPeakNumberItems());
        assertFalse(queue.maxCapacityExceeded());
    }

    @Test
    public void testQueueConstructor2() {
        Queue queue = new Queue(5);
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.getNumberItems());
        assertEquals(-1, queue.getPeakNumberItems());
        assertFalse(queue.maxCapacityExceeded());
    }

    @Test
    public void testEnqueue() {
        Queue queue = new Queue();
        queue.enqueue("item1");
        assertEquals(1, queue.getNumberItems());
        assertFalse(queue.isEmpty());
        assertFalse(queue.maxCapacityExceeded());
    }

    @Test
    public void testEnqueue_NullObject() {
        Queue queue = new Queue();
        queue.enqueue(null);
        assertEquals(0, queue.getNumberItems());
        assertTrue(queue.isEmpty());
        assertFalse(queue.maxCapacityExceeded());
    }

    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        queue.enqueue("item1");
        queue.enqueue("item2");
        assertEquals("item1", queue.dequeue());
        assertEquals(1, queue.getNumberItems());
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testDequeue_EmptyQueue() {
        Queue queue = new Queue();
        assertNull(queue.dequeue());
        assertEquals(0, queue.getNumberItems());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testRemove() {
        Queue queue = new Queue();
        queue.enqueue("item1");
        queue.enqueue("item1");
        queue.enqueue("item2");
        assertEquals(2, queue.remove("item1"));
        assertEquals(1, queue.getNumberItems());
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testRemove_ObjectNotInQueue() {
        Queue queue = new Queue();
        queue.enqueue("item1");
        queue.enqueue("item2");
        assertEquals(0, queue.remove("item3"));
        assertEquals(2, queue.getNumberItems());
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.refreshElement("item1");
        assertEquals(2, queue.getNumberItems());
        assertFalse(queue.isEmpty());
        assertEquals("item1", queue.last.value);
    }

    @Test
    public void testGetObjects() {
        Queue queue = new Queue();
        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.enqueue("item3");
        queue.enqueue("item4");
        assertEquals(4, queue.getObjects().size());
        assertTrue(queue.getObjects().contains("item1"));
        assertTrue(queue.getObjects().contains("item2"));
        assertTrue(queue.getObjects().contains("item3"));
        assertTrue(queue.getObjects().contains("item4"));
    }

    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(2);
        queue.enqueue("item1");
        queue.enqueue("item2");
        assertTrue(queue.maxCapacityExceeded());
        queue.dequeue();
        assertFalse(queue.maxCapacityExceeded());
    }

    @Test
    public void testToString() {
        Queue queue = new Queue();
        queue.enqueue("item1");
        queue.enqueue("item2");
        String expectedString = "bible.util.Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[item1, item2]\r\n]";
        assertEquals(expectedString, queue.toString());
    }
}
