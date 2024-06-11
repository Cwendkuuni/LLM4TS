import static org.junit.Assert.*;
import org.junit.*;

public class QueueTest {
  
    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());
    }
  
    @Test
    public void testEnqueue() {
        Queue queue = new Queue();
        Object obj = new Object();
        queue.enqueue(obj);
        assertFalse(queue.isEmpty());
    }
  
    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        Object obj = new Object();
        queue.enqueue(obj);
        assertEquals(obj, queue.dequeue());
        assertTrue(queue.isEmpty());
    }
  
    @Test
    public void testRemove() {
        Queue queue = new Queue();
        Object obj1 = new Object();
        Object obj2 = new Object();
        Object obj3 = new Object();
        
        // enqueue multiple objects
        queue.enqueue(obj1);
        queue.enqueue(obj2);
        queue.enqueue(obj3);
        
        // remove obj2
        int removedCount = queue.remove(obj2);
        
        // obj2 should be removed
        assertEquals(1, removedCount);
        assertEquals(2, queue.getNumberItems());
        assertFalse(queue.getObjects().contains(obj2));
    }
  
    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        Object obj1 = new Object();
        Object obj2 = new Object();
        Object obj3 = new Object();
        
        // enqueue multiple objects
        queue.enqueue(obj1);
        queue.enqueue(obj2);
        queue.enqueue(obj3);
        
        // refresh obj2
        queue.refreshElement(obj2);
        
        // obj2 should be moved to the end of the queue
        assertTrue(queue.getObjects().lastElement().equals(obj2));
    }
  
    @Test
    public void testGetNumberItems() {
        Queue queue = new Queue();
        
        // enqueue multiple objects
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        
        assertEquals(3, queue.getNumberItems());
    }
  
    @Test
    public void testGetPeakNumberItems() {
        Queue queue = new Queue();
        
        // enqueue multiple objects
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        
        // maximum number of items should be 3
        assertEquals(3, queue.getPeakNumberItems());
    }
  
    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(2);
        
        // enqueue multiple objects
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        
        // max capacity exceeded
        assertTrue(queue.maxCapacityExceeded());
    }
  
    @Test
    public void testToString() {
        Queue queue = new Queue();
        
        // enqueue multiple objects
        queue.enqueue("A");
        queue.enqueue("B");
        queue.enqueue("C");
        
        assertEquals("bible.util.Queue:[numItems=3, maxNumItems=3, maxCapacity=-1, getObjects()=[A, B, C]\r\n]", queue.toString());
    }
}