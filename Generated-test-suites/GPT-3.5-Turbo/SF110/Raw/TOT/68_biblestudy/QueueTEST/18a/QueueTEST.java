import org.junit.*;
import static org.junit.Assert.*;

public class QueueTest {
    
    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());
        queue.enqueue("Hello");
        assertFalse(queue.isEmpty());
        queue.dequeue();
        assertTrue(queue.isEmpty());
    }
    
    @Test
    public void testEnqueue() {
        Queue queue = new Queue();
        queue.enqueue("Hello");
        assertEquals("Hello", queue.dequeue());
        queue.enqueue("Test");
        assertEquals("Test", queue.dequeue());
    }
    
    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        assertNull(queue.dequeue());
        
        queue.enqueue("Hello");
        queue.enqueue("Test");
        assertEquals("Hello", queue.dequeue());
        assertEquals("Test", queue.dequeue());
        assertNull(queue.dequeue());
    }
    
    @Test
    public void testRemove() {
        Queue queue = new Queue();
        assertEquals(0, queue.remove("Test"));
        
        queue.enqueue("Test");
        queue.enqueue("Hello");
        queue.enqueue("Test");
        assertEquals(2, queue.remove("Test"));
    }
    
    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("Test");
        queue.enqueue("Hello");
        queue.refreshElement("Test");
        assertEquals("Hello", queue.dequeue());
        assertEquals("Test", queue.dequeue());
        assertTrue(queue.isEmpty());
    }
    
    @Test
    public void testGetNumberItems() {
        Queue queue = new Queue();
        assertEquals(0, queue.getNumberItems());
        
        queue.enqueue("Test");
        queue.enqueue("Hello");
        assertEquals(2, queue.getNumberItems());
        
        queue.dequeue();
        assertEquals(1, queue.getNumberItems());
    }
    
    @Test
    public void testGetPeakNumberItems() {
        Queue queue = new Queue();
        assertEquals(0, queue.getPeakNumberItems());
        
        queue.enqueue("Test");
        assertEquals(1, queue.getPeakNumberItems());
        
        queue.enqueue("Hello");
        assertEquals(2, queue.getPeakNumberItems());
        
        queue.dequeue();
        assertEquals(2, queue.getPeakNumberItems());
    }
    
    @Test
    public void testGetObjects() {
        Queue queue = new Queue();
        assertEquals(0, queue.getObjects().size());
        
        queue.enqueue("Test");
        queue.enqueue("Hello");
        
        assertEquals("Test", queue.getObjects().get(0));
        assertEquals("Hello", queue.getObjects().get(1));
    }
    
    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(2);
        assertFalse(queue.maxCapacityExceeded());
        
        queue.enqueue("Test");
        queue.enqueue("Hello");
        assertTrue(queue.maxCapacityExceeded());
        
        queue.dequeue();
        assertFalse(queue.maxCapacityExceeded());
    }
    
    @Override
    public String toString() {
        return "QueueTest";
    }
}