package bible.util;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Vector;

public class QueueTest {

    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());

        queue.enqueue("hello");
        assertFalse(queue.isEmpty());
    }
    
    @Test
    public void testEnqueueAndDequeue() {
        Queue queue = new Queue();
        queue.enqueue("hello");
        queue.enqueue("world");

        assertEquals("hello", queue.dequeue());
        assertEquals("world", queue.dequeue());
    }
    
    @Test
    public void testRemove() {
        Queue queue = new Queue();
        queue.enqueue("hello");
        queue.enqueue("world");
        queue.enqueue("hello");
        queue.enqueue("world");

        assertEquals(2, queue.remove("hello"));
        assertEquals(2, queue.remove("world"));
        assertEquals(0, queue.remove("nonexistent"));
    }

    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("hello");
        queue.enqueue("world");
        queue.enqueue("hello");

        queue.refreshElement("hello");

        assertEquals(3, queue.getNumberItems());
        assertEquals(1, queue.getPeakNumberItems());
        assertEquals(new Vector<>(List.of("world", "hello")), queue.getObjects());
    }

    @Test
    public void testGetNumberItems() {
        Queue queue = new Queue();
        assertEquals(0, queue.getNumberItems());

        queue.enqueue("hello");
        assertEquals(1, queue.getNumberItems());

        queue.enqueue("world");
        assertEquals(2, queue.getNumberItems());

        queue.dequeue();
        assertEquals(1, queue.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems() {
        Queue queue = new Queue();
        assertEquals(0, queue.getPeakNumberItems());

        queue.enqueue("hello");
        assertEquals(1, queue.getPeakNumberItems());

        queue.enqueue("world");
        assertEquals(2, queue.getPeakNumberItems());

        queue.dequeue();
        assertEquals(2, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        Queue queue = new Queue();
        assertEquals(new Vector<>(), queue.getObjects());

        queue.enqueue("hello");
        assertEquals(new Vector<>(List.of("hello")), queue.getObjects());

        queue.enqueue("world");
        assertEquals(new Vector<>(List.of("hello", "world")), queue.getObjects());

        queue.dequeue();
        assertEquals(new Vector<>(List.of("world")), queue.getObjects());
    }

    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(2);
        assertFalse(queue.maxCapacityExceeded());

        queue.enqueue("hello");
        assertFalse(queue.maxCapacityExceeded());

        queue.enqueue("world");
        assertTrue(queue.maxCapacityExceeded());

        queue.dequeue();
        assertFalse(queue.maxCapacityExceeded());
    }

    @Test
    public void testToString() {
        Queue queue = new Queue();
        assertEquals("bible.util.Queue:[numItems=0, maxNumItems=0, maxCapacity=-1, getObjects()=[]\r\n]", queue.toString());

        queue.enqueue("hello");
        assertEquals("bible.util.Queue:[numItems=1, maxNumItems=1, maxCapacity=-1, getObjects()=[hello]\r\n]", queue.toString());

        queue.enqueue("world");
        assertEquals("bible.util.Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[hello, world]\r\n]", queue.toString());

        queue.dequeue();
        assertEquals("bible.util.Queue:[numItems=1, maxNumItems=2, maxCapacity=-1, getObjects()=[world]\r\n]", queue.toString());
    }
}