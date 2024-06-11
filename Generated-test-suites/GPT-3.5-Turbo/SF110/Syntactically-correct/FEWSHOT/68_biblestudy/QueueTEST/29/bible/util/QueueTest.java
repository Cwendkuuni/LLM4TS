
package bible.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.Vector;
import org.junit.Before;
import org.junit.Test;

public class QueueTest {

    private Queue queue;

    @Before
    public void setUp() {
        queue = new Queue();
    }

    @Test
    public void testIsEmptyWhenQueueEmpty() {
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testIsEmptyWhenQueueNotEmpty() {
        queue.enqueue("item");
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testEnqueue() {
        queue.enqueue("item1");
        queue.enqueue("item2");
        assertEquals(2, queue.getNumberItems());
    }

    @Test
    public void testDequeueWhenQueueEmpty() {
        Object item = queue.dequeue();
        assertEquals(null, item);
    }

    @Test
    public void testDequeueWhenQueueNotEmpty() {
        queue.enqueue("item1");
        queue.enqueue("item2");
        Object item = queue.dequeue();
        assertEquals("item1", item);
    }

    @Test
    public void testRemoveWhenQueueEmpty() {
        int removedItems = queue.remove("item");
        assertEquals(0, removedItems);
    }

    @Test
    public void testRemoveWhenQueueNotEmptyAndMatchingItemsExist() {
        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.enqueue("item1");
        int removedItems = queue.remove("item1");
        assertEquals(2, removedItems);
    }

    @Test
    public void testRemoveWhenQueueNotEmptyAndNoMatchingItemsExist() {
        queue.enqueue("item1");
        queue.enqueue("item2");
        int removedItems = queue.remove("item3");
        assertEquals(0, removedItems);
    }

    @Test
    public void testRefreshElementWhenQueueEmpty() {
        queue.refreshElement("item");
        assertEquals(0, queue.getNumberItems());
    }

    @Test
    public void testRefreshElementWhenQueueNotEmpty() {
        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.refreshElement("item1");
        assertEquals(2, queue.getNumberItems());
    }

    @Test
    public void testGetNumberItemsWhenQueueEmpty() {
        assertEquals(0, queue.getNumberItems());
    }

    @Test
    public void testGetNumberItemsWhenQueueNotEmpty() {
        queue.enqueue("item1");
        queue.enqueue("item2");
        assertEquals(2, queue.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItemsWhenQueueEmpty() {
        assertEquals(0, queue.getPeakNumberItems());
    }

    @Test
    public void testGetPeakNumberItemsWhenQueueNotEmpty() {
        queue.enqueue("item1");
        queue.enqueue("item2");
        assertEquals(2, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjectsWhenQueueEmpty() {
        Vector objects = queue.getObjects();
        assertEquals(0, objects.size());
    }

    @Test
    public void testGetObjectsWhenQueueNotEmpty() {
        queue.enqueue("item1");
        queue.enqueue("item2");
        Vector objects = queue.getObjects();
        assertEquals(2, objects.size());
        assertTrue(objects.contains("item1"));
        assertTrue(objects.contains("item2"));
    }

    @Test
    public void testMaxCapacityExceededWhenQueueNotExceeded() {
        boolean exceeded = queue.maxCapacityExceeded();
        assertFalse(exceeded);
    }

    @Test
    public void testMaxCapacityExceededWhenQueueExceeded() {
        Queue queueWithCapacity = new Queue(2);
        queueWithCapacity.enqueue("item1");
        queueWithCapacity.enqueue("item2");
        boolean exceeded = queueWithCapacity.maxCapacityExceeded();
        assertTrue(exceeded);
    }

    @Test
    public void testToStringWhenQueueEmpty() {
        String expected = "bible.util.Queue:[numItems=0, maxNumItems=0, maxCapacity=-1, getObjects()=[]\r\n]";
        assertEquals(expected, queue.toString());
    }

    @Test
    public void testToStringWhenQueueNotEmpty() {
        queue.enqueue("item1");
        queue.enqueue("item2");
        String expected = "bible.util.Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[item1, item2]\r\n]";
        assertEquals(expected, queue.toString());
    }
}
