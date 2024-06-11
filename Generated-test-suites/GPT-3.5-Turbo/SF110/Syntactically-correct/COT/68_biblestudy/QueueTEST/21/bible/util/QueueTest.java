
import org.junit.*;
import static org.junit.Assert.*;

public class QueueTest {

    @Test
    public void testisEmpty() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testenqueue() {
        Queue queue = new Queue();
        queue.enqueue("Item 1");
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testdequeue() {
        Queue queue = new Queue();
        queue.enqueue("Item 1");
        String item = (String) queue.dequeue();
        assertEquals("Item 1", item);
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testremove() {
        Queue queue = new Queue();
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        queue.enqueue("Item 3");

        int numRemoved = queue.remove("Item 2");
        assertEquals(1, numRemoved);

        numRemoved = queue.remove("Item 1");
        assertEquals(1, numRemoved);

        assertFalse(queue.isEmpty());
    }

    @Test
    public void testrefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        queue.enqueue("Item 3");

        queue.refreshElement("Item 2");

        assertFalse(queue.isEmpty());
    }

    @Test
    public void testgetNumberItems() {
        Queue queue = new Queue();
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        queue.enqueue("Item 3");

        int numItems = queue.getNumberItems();
        assertEquals(3, numItems);
    }

    @Test
    public void testgetPeakNumberItems() {
        Queue queue = new Queue();
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        queue.enqueue("Item 3");

        int peakNumItems = queue.getPeakNumberItems();
        assertEquals(3, peakNumItems);
    }

    @Test
    public void testgetObjects() {
        Queue queue = new Queue();
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        queue.enqueue("Item 3");

        Vector objects = queue.getObjects();
        assertEquals(3, objects.size());
        assertTrue(objects.contains("Item 1"));
        assertTrue(objects.contains("Item 2"));
        assertTrue(objects.contains("Item 3"));
    }

    @Test
    public void testmaxCapacityExceeded() {
        Queue queue = new Queue(2);
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");

        assertFalse(queue.maxCapacityExceeded());

        queue.enqueue("Item 3");
        assertTrue(queue.maxCapacityExceeded());
    }

    @Test
    public void testtoString() {
        Queue queue = new Queue();
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        queue.enqueue("Item 3");

        String stringResult = queue.toString();
        assertTrue(stringResult.contains("[numItems=3"));
        assertTrue(stringResult.contains("maxNumItems=3"));
        assertTrue(stringResult.contains("maxCapacity=-1"));
        assertTrue(stringResult.contains("getObjects()=[Item 1, Item 2, Item 3]"));
    }
}
