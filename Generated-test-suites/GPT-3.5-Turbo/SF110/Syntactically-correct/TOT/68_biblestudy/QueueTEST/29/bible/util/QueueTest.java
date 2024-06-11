import static org.junit.Assert.*;
import org.junit.*;

public class QueueTest {

    private Queue queue;

    @Before
    public void setUp() {
        queue = new Queue();
    }

    @Test
    public void testIsEmpty_whenQueueIsEmpty_shouldReturnTrue() {
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testIsEmpty_whenQueueIsNotEmpty_shouldReturnFalse() {
        queue.enqueue("item");
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testEnqueue_whenObjectIsNull_shouldNotAddToQueue() {
        queue.enqueue(null);
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testEnqueue_whenQueueIsEmpty_shouldAddObjectAsFirstAndLastNode() {
        queue.enqueue("item");
        assertEquals(1, queue.getNumberItems());
        assertEquals("item", queue.dequeue());
    }

    @Test
    public void testEnqueue_whenQueueIsNotEmpty_shouldAddObjectAsLastNode() {
        queue.enqueue("item1");
        queue.enqueue("item2");
        assertEquals(2, queue.getNumberItems());
        assertEquals("item1", queue.dequeue());
        assertEquals("item2", queue.dequeue());
    }

    @Test
    public void testDequeue_whenQueueIsEmpty_shouldPrintErrorMessage() {
        queue.dequeue(); // should print "Cannot remove when queue is empty"
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testDequeue_whenQueueHasOneItem_shouldRemoveAndReturnItem() {
        queue.enqueue("item");
        assertEquals("item", queue.dequeue());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testDequeue_whenQueueHasMultipleItems_shouldRemoveAndReturnFirstItem() {
        queue.enqueue("item1");
        queue.enqueue("item2");
        assertEquals("item1", queue.dequeue());
        assertEquals(1, queue.getNumberItems());
    }

    @Test
    public void testRemove_whenObjectIsInFirstNode_shouldRemoveAndReturnNumberOfItemsRemoved() {
        queue.enqueue("item1");
        queue.enqueue("item1");
        queue.enqueue("item2");

        int removedItems = queue.remove("item1");
        assertEquals(2, removedItems);
        assertEquals(1, queue.getNumberItems());
    }

    @Test
    public void testRemove_whenObjectIsInLastNode_shouldRemoveAndReturnNumberOfItemsRemoved() {
        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.enqueue("item1");

        int removedItems = queue.remove("item1");
        assertEquals(2, removedItems);
        assertEquals(1, queue.getNumberItems());
        assertEquals("item2", queue.dequeue());
    }

    @Test
    public void testRemove_whenObjectIsInMiddleNodes_shouldRemoveAndReturnNumberOfItemsRemoved() {
        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.enqueue("item1");
        queue.enqueue("item3");

        int removedItems = queue.remove("item1");
        assertEquals(2, removedItems);
        assertEquals(2, queue.getNumberItems());
    }

    @Test
    public void testRefreshElement_whenObjectIsInQueue_shouldRemoveAndEnqueueObject() {
        queue.enqueue("item1");
        queue.enqueue("item2");

        assertEquals(2, queue.getNumberItems());
        queue.refreshElement("item1");
        assertEquals(2, queue.getNumberItems());
        assertEquals("item2", queue.dequeue());
        assertEquals("item1", queue.dequeue());
    }

    @Test
    public void testGetNumberItems_whenQueueIsEmpty_shouldReturnZero() {
        assertEquals(0, queue.getNumberItems());
    }

    @Test
    public void testGetNumberItems_whenQueueHasItems_shouldReturnNumberOfItems() {
        queue.enqueue("item1");
        queue.enqueue("item2");
        assertEquals(2, queue.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems_whenQueueIsEmpty_shouldReturnZero() {
        assertEquals(0, queue.getPeakNumberItems());
    }

    @Test
    public void testGetPeakNumberItems_whenQueueHasItems_shouldReturnMaxNumberOfItems() {
        queue.enqueue("item1");
        queue.enqueue("item2");
        assertEquals(2, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects_whenQueueIsEmpty_shouldReturnEmptyVector() {
        Vector expected = new Vector();
        assertEquals(expected, queue.getObjects());
    }

    @Test
    public void testGetObjects_whenQueueHasItems_shouldReturnVectorWithAllItems() {
        queue.enqueue("item1");
        queue.enqueue("item2");
        Vector expected = new Vector();
        expected.add("item1");
        expected.add("item2");
        assertEquals(expected, queue.getObjects());
    }

    @Test
    public void testMaxCapacityExceeded_whenQueueHasNotExceededCapacity_shouldReturnFalse() {
        queue.enqueue("item1");
        queue.enqueue("item2");
        assertFalse(queue.maxCapacityExceeded());
    }

    @Test
    public void testMaxCapacityExceeded_whenQueueHasExceededCapacity_shouldReturnTrue() {
        queue = new Queue(2);
        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.enqueue("item3");
        assertTrue(queue.maxCapacityExceeded());
    }

    @Test
    public void testToString_shouldReturnStringRepresentationOfQueue() {
        queue.enqueue("item1");
        queue.enqueue("item2");
        String expected = "bible.util.Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[item1, item2]\r\n]";
        assertEquals(expected, queue.toString());
    }
}