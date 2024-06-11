
package bible.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class QueueTest {

    /**
     * Test of isEmpty method, typical use case.
     */
    @Test
    public void testIsEmptyTypical() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());
    }
    
    /**
     * Test of isEmpty method, when the queue is not empty.
     */
    @Test
    public void testIsEmptyNotEmpty() {
        Queue queue = new Queue();
        queue.enqueue("Test");
        assertFalse(queue.isEmpty());
    }
    
    /**
     * Test of enqueue method, typical use case.
     */
    @Test
    public void testEnqueueTypical() {
        Queue queue = new Queue();
        queue.enqueue("Test");
        
        assertEquals("Test", queue.dequeue());
    }
    
    /**
     * Test of enqueue method, multiple enqueues.
     */
    @Test
    public void testEnqueueMultiple() {
        Queue queue = new Queue();
        queue.enqueue("Test1");
        queue.enqueue("Test2");
        queue.enqueue("Test3");
        
        assertEquals("Test1", queue.dequeue());
        assertEquals("Test2", queue.dequeue());
        assertEquals("Test3", queue.dequeue());
    }
    
    /**
     * Test of dequeue method, when the queue is empty.
     */
    @Test
    public void testDequeueEmpty() {
        Queue queue = new Queue();
        
        assertNull(queue.dequeue());
    }
    
    /**
     * Test of dequeue method, typical use case.
     */
    @Test
    public void testDequeueTypical() {
        Queue queue = new Queue();
        queue.enqueue("Test");
        
        assertEquals("Test", queue.dequeue());
        assertTrue(queue.isEmpty());
    }
    
    /**
     * Test of remove method, when the object to remove is not in the queue.
     */
    @Test
    public void testRemoveNotInQueue() {
        Queue queue = new Queue();
        queue.enqueue("Test1");
        queue.enqueue("Test2");
        
        int result = queue.remove("Test3");
        
        assertEquals(0, result);
    }
    
    /**
     * Test of remove method, typical use case.
     */
    @Test
    public void testRemoveTypical() {
        Queue queue = new Queue();
        queue.enqueue("Test1");
        queue.enqueue("Test2");
        queue.enqueue("Test3");
        
        int result = queue.remove("Test2");
        
        assertEquals(1, result);
        assertEquals("Test1", queue.dequeue());
        assertEquals("Test3", queue.dequeue());
        assertTrue(queue.isEmpty());
    }
    
    /**
     * Test of refreshElement method, typical use case.
     */
    @Test
    public void testRefreshElementTypical() {
        Queue queue = new Queue();
        queue.enqueue("Test1");
        queue.enqueue("Test2");
        
        queue.refreshElement("Test1");
        
        assertEquals("Test2", queue.dequeue());
        assertEquals("Test1", queue.dequeue());
        assertTrue(queue.isEmpty());
    }
    
    /**
     * Test of getNumberItems method, typical use case.
     */
    @Test
    public void testGetNumberItemsTypical() {
        Queue queue = new Queue();
        queue.enqueue("Test1");
        queue.enqueue("Test2");
        
        int result = queue.getNumberItems();
        
        assertEquals(2, result);
    }
    
    /**
     * Test of getPeakNumberItems method, typical use case.
     */
    @Test
    public void testGetPeakNumberItemsTypical() {
        Queue queue = new Queue();
        queue.enqueue("Test1");
        queue.enqueue("Test2");
        
        int result = queue.getPeakNumberItems();
        
        assertEquals(2, result);
    }
    
    /**
     * Test of getObjects method, typical use case.
     */
    @Test
    public void testGetObjectsTypical() {
        Queue queue = new Queue();
        queue.enqueue("Test1");
        queue.enqueue("Test2");
        
        Vector result = queue.getObjects();
        
        assertEquals(2, result.size());
        assertEquals("Test1", result.get(0));
        assertEquals("Test2", result.get(1));
    }
    
    /**
     * Test of maxCapacityExceeded method, when the queue is not full.
     */
    @Test
    public void testMaxCapacityNotExceeded() {
        Queue queue = new Queue(10);
        queue.enqueue("Test1");
        queue.enqueue("Test2");
        
        boolean result = queue.maxCapacityExceeded();
        
        assertFalse(result);
    }
    
    /**
     * Test of maxCapacityExceeded method, when the queue is full.
     */
    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(2);
        queue.enqueue("Test1");
        queue.enqueue("Test2");
        queue.enqueue("Test3");
        
        boolean result = queue.maxCapacityExceeded();
        
        assertTrue(result);
    }
    
    /**
     * Test of toString method, typical use case.
     */
    @Test
    public void testToStringTypical() {
        Queue queue = new Queue();
        queue.enqueue("Test1");
        queue.enqueue("Test2");
        
        String result = queue.toString();
        
        assertEquals("bible.util.Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[Test1, Test2]\r\n]", result);
    }
    
}
