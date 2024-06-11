import org.junit.*;
import static org.junit.Assert.*;

public class QueueTest {
    
    // Test case for the constructor without capacity
    @Test
    public void testConstructorWithoutCapacity() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.getNumberItems());
        assertEquals(0, queue.getPeakNumberItems());
        assertFalse(queue.maxCapacityExceeded());
    }
    
    // Test case for the constructor with capacity
    @Test
    public void testConstructorWithCapacity() {
        int capacity = 5;
        Queue queue = new Queue(capacity);
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.getNumberItems());
        assertEquals(0, queue.getPeakNumberItems());
        assertFalse(queue.maxCapacityExceeded());
        assertEquals(capacity, queue.maxCapacity);
    }
    
    // Test case for isEmpty when queue is empty
    @Test
    public void testIsEmptyWhenEmpty() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());
    }
    
    // Test case for isEmpty when queue is not empty
    @Test
    public void testIsEmptyWhenNotEmpty() {
        Queue queue = new Queue();
        queue.enqueue("Test");
        assertFalse(queue.isEmpty());
    }
    
    // Test case for enqueue when the object is not null
    @Test
    public void testEnqueueValidObject() {
        Queue queue = new Queue();
        queue.enqueue("Test");
        assertFalse(queue.isEmpty());
        assertEquals(1, queue.getNumberItems());
        assertEquals(1, queue.getPeakNumberItems());
        assertFalse(queue.maxCapacityExceeded());
    }
    
    // Test case for enqueue when the object is null
    @Test
    public void testEnqueueNullObject() {
        Queue queue = new Queue();
        queue.enqueue(null);
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.getNumberItems());
        assertEquals(0, queue.getPeakNumberItems());
        assertFalse(queue.maxCapacityExceeded());
    }
    
    // Test case for dequeue when the queue is empty
    @Test
    public void testDequeueWhenEmpty() {
        Queue queue = new Queue();
        assertNull(queue.dequeue());
    }
    
    // Test case for dequeue when the queue has one element
    @Test
    public void testDequeueWithOneElement() {
        Queue queue = new Queue();
        queue.enqueue("Test");
        assertEquals("Test", queue.dequeue());
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.getNumberItems());
        assertEquals(0, queue.getPeakNumberItems());
        assertFalse(queue.maxCapacityExceeded());
    }
    
    // Test case for dequeue when the queue has multiple elements
    @Test
    public void testDequeueWithMultipleElements() {
        Queue queue = new Queue();
        queue.enqueue("Test1");
        queue.enqueue("Test2");
        assertEquals("Test1", queue.dequeue());
        assertFalse(queue.isEmpty());
        assertEquals(1, queue.getNumberItems());
        assertEquals(2, queue.getPeakNumberItems());
        assertFalse(queue.maxCapacityExceeded());
    }
    
    // Test case for remove when the queue is empty
    @Test
    public void testRemoveWhenEmpty() {
        Queue queue = new Queue();
        assertEquals(0, queue.remove("Test"));
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.getNumberItems());
        assertEquals(0, queue.getPeakNumberItems());
        assertFalse(queue.maxCapacityExceeded());
    }
    
    // Test case for remove when the element is found at the first position
    @Test
    public void testRemoveFirstPosition() {
        Queue queue = new Queue();
        queue.enqueue("Test1");
        queue.enqueue("Test2");
        queue.enqueue("Test3");
        assertEquals(1, queue.remove("Test1"));
        assertFalse(queue.isEmpty());
        assertEquals(2, queue.getNumberItems());
        assertEquals(3, queue.getPeakNumberItems());
        assertFalse(queue.maxCapacityExceeded());
    }
    
    // Test case for remove when the element is found in the middle
    @Test
    public void testRemoveMiddle() {
        Queue queue = new Queue();
        queue.enqueue("Test1");
        queue.enqueue("Test2");
        queue.enqueue("Test3");
        assertEquals(1, queue.remove("Test2"));
        assertFalse(queue.isEmpty());
        assertEquals(2, queue.getNumberItems());
        assertEquals(3, queue.getPeakNumberItems());
        assertFalse(queue.maxCapacityExceeded());
    }
    
    // Test case for remove when the element is found at the last position
    @Test
    public void testRemoveLastPosition() {
        Queue queue = new Queue();
        queue.enqueue("Test1");
        queue.enqueue("Test2");
        queue.enqueue("Test3");
        assertEquals(1, queue.remove("Test3"));
        assertFalse(queue.isEmpty());
        assertEquals(2, queue.getNumberItems());
        assertEquals(3, queue.getPeakNumberItems());
        assertFalse(queue.maxCapacityExceeded());
    }
    
    // Test case for refreshElement when the queue is empty
    @Test
    public void testRefreshElementWhenEmpty() {
        Queue queue = new Queue();
        queue.refreshElement("Test");
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.getNumberItems());
        assertEquals(0, queue.getPeakNumberItems());
        assertFalse(queue.maxCapacityExceeded());
    }
    
    // Test case for refreshElement when the element is not found
    @Test
    public void testRefreshElementNotFound() {
        Queue queue = new Queue();
        queue.enqueue("Test1");
        queue.enqueue("Test2");
        queue.enqueue("Test3");
        queue.refreshElement("Test4");
        assertFalse(queue.isEmpty());
        assertEquals(3, queue.getNumberItems());
        assertEquals(3, queue.getPeakNumberItems());
        assertFalse(queue.maxCapacityExceeded());
    }
    
    // Test case for refreshElement when the element is found
    @Test
    public void testRefreshElementFound() {
        Queue queue = new Queue();
        queue.enqueue("Test1");
        queue.enqueue("Test2");
        queue.enqueue("Test3");
        queue.refreshElement("Test2");
        assertFalse(queue.isEmpty());
        assertEquals(3, queue.getNumberItems());
        assertEquals(3, queue.getPeakNumberItems());
        assertFalse(queue.maxCapacityExceeded());
    }
    
    // Test case for getNumberItems
    @Test
    public void testGetNumberItems() {
        Queue queue = new Queue();
        queue.enqueue("Test1");
        queue.enqueue("Test2");
        assertEquals(2, queue.getNumberItems());
    }
    
    // Test case for getPeakNumberItems
    @Test
    public void testGetPeakNumberItems() {
        Queue queue = new Queue();
        queue.enqueue("Test1");
        queue.enqueue("Test2");
        assertEquals(2, queue.getPeakNumberItems());
        queue.dequeue();
        assertEquals(1, queue.getPeakNumberItems());
    }
    
    // Test case for getObjects when queue is empty
    @Test
    public void testGetObjectsWhenEmpty() {
        Queue queue = new Queue();
        assertEquals(new Vector(), queue.getObjects());
    }
    
    // Test case for getObjects when queue is not empty
    @Test
    public void testGetObjectsWhenNotEmpty() {
        Queue queue = new Queue();
        queue.enqueue("Test1");
        queue.enqueue("Test2");
        Vector expected = new Vector();
        expected.addElement("Test1");
        expected.addElement("Test2");
        assertEquals(expected, queue.getObjects());
    }
    
    // Test case for maxCapacityExceeded when number of items does not exceed maximum capacity
    @Test
    public void testMaxCapacityExceededFalse() {
        Queue queue = new Queue();
        queue.enqueue("Test1");
        assertFalse(queue.maxCapacityExceeded());
    }
    
    // Test case for maxCapacityExceeded when number of items exceeds maximum capacity
    @Test
    public void testMaxCapacityExceededTrue() {
        int capacity = 2;
        Queue queue = new Queue(capacity);
        queue.enqueue("Test1");
        queue.enqueue("Test2");
        assertTrue(queue.maxCapacityExceeded());
    }
    
    // Test case for toString when queue is empty
    @Test
    public void testToStringWhenEmpty() {
        Queue queue = new Queue();
        assertEquals("bible.util.Queue:[numItems=0, maxNumItems=0, maxCapacity=-1, getObjects()=[]\r\n]", queue.toString());
    }
    
    // Test case for toString when queue is not empty
    @Test
    public void testToStringWhenNotEmpty() {
        Queue queue = new Queue();
        queue.enqueue("Test1");
        queue.enqueue("Test2");
        assertEquals("bible.util.Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[Test1, Test2]\r\n]", queue.toString());
    }
}