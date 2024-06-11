import org.junit.Test;
import static org.junit.Assert.*;

public class QueueTest {

    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());
        
        queue.enqueue("Test");
        assertFalse(queue.isEmpty());
    }
    
    @Test
    public void testEnqueue() {
        Queue queue = new Queue();
        queue.enqueue("Test");
        assertEquals(1, queue.getNumberItems());
        assertEquals(1, queue.getPeakNumberItems());
        
        queue.enqueue("Test2");
        assertEquals(2, queue.getNumberItems());
    }
    
    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        queue.enqueue("Test");
        queue.enqueue("Test2");
        
        assertEquals("Test", queue.dequeue());
        assertEquals(1, queue.getNumberItems());
        assertEquals(2, queue.getPeakNumberItems());
    }
    
    @Test
    public void testRemove() {
        Queue queue = new Queue();
        queue.enqueue("Test");
        queue.enqueue("Test2");
        
        assertEquals(1, queue.remove("Test"));
        assertEquals(1, queue.getNumberItems());
    }
    
    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("Test");
        queue.refreshElement("Test");
        
        assertEquals(1, queue.getNumberItems());
        assertEquals(2, queue.getPeakNumberItems());
    }
    
    @Test
    public void testGetObjects() {
        Queue queue = new Queue();
        queue.enqueue("Test");
        queue.enqueue("Test2");
        
        Vector objects = queue.getObjects();
        
        assertEquals(2, objects.size());
        assertTrue(objects.contains("Test"));
        assertTrue(objects.contains("Test2"));
    }
    
    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(2);
        queue.enqueue("Test");
        queue.enqueue("Test2");
        
        assertTrue(queue.maxCapacityExceeded());
        
        queue.dequeue();
        assertFalse(queue.maxCapacityExceeded());
    }
    
    @Test
    public void testToString() {
        Queue queue = new Queue();
        queue.enqueue("Test");
        
        assertEquals("bible.util.Queue:[numItems=1, maxNumItems=1, maxCapacity=-1, getObjects()=[Test]\r\n]", queue.toString());
    }
}