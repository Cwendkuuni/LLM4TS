import static org.junit.Assert.*;
import org.junit.Test;

public class QueueTest {

    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());
    }
    
    @Test
    public void testEnqueue() {
        Queue queue = new Queue();
        queue.enqueue("A");
        
        assertFalse(queue.isEmpty());
        assertEquals(queue.getNumberItems(), 1);
        assertFalse(queue.maxCapacityExceeded());
    }
    
    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        queue.enqueue("A");
        
        Object result = queue.dequeue();
        
        assertTrue(queue.isEmpty());
        assertEquals(result, "A");
    }
    
    @Test
    public void testRemove() {
        Queue queue = new Queue();
        queue.enqueue("A");
        queue.enqueue("B");
        queue.enqueue("A");
        
        int count = queue.remove("A");
        
        assertEquals(queue.getNumberItems(), 1);
        assertEquals(count, 2);
    }
    
    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("A");
        
        queue.refreshElement("A");
        
        assertEquals(queue.getNumberItems(), 1);
    }
    
    @Test
    public void testGetNumberItems() {
        Queue queue = new Queue();
        queue.enqueue("A");
        queue.enqueue("B");
        
        assertEquals(queue.getNumberItems(), 2);
    }
    
    @Test
    public void testGetPeakNumberItems() {
        Queue queue = new Queue();
        queue.enqueue("A");
        queue.enqueue("B");
        
        assertEquals(queue.getPeakNumberItems(), 2);
    }
    
    @Test
    public void testGetObjects() {
        Queue queue = new Queue();
        queue.enqueue("A");
        queue.enqueue("B");
        
        Vector expected = new Vector();
        expected.addElement("A");
        expected.addElement("B");
        
        assertEquals(queue.getObjects(), expected);
    }
    
    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(2);
        queue.enqueue("A");
        queue.enqueue("B");
        
        assertTrue(queue.maxCapacityExceeded());
    }
    
    @Test
    public void testToString() {
        Queue queue = new Queue();
        queue.enqueue("A");
        queue.enqueue("B");
        
        String expected = "Queue:[numItems=2, maxNumItems=0, maxCapacity=-1, getObjects()=[A, B]\r\n]";
        
        assertEquals(queue.toString(), expected);
    }
}