import org.junit.*;
import static org.junit.Assert.*;
import java.util.Vector;

public class QueueTest {
    
    @Test
    public void testIsEmpty() {
        Queue q = new Queue();
        
        assertTrue(q.isEmpty());
        
        q.enqueue("test");
        
        assertFalse(q.isEmpty());
    }
    
    @Test
    public void testEnqueue() {
        Queue q = new Queue();
        
        q.enqueue("test");
        
        assertEquals(1, q.getNumberItems());
    }
    
    @Test
    public void testDequeue() {
        Queue q = new Queue();
        
        q.enqueue("test1");
        q.enqueue("test2");
        
        assertEquals("test1", q.dequeue());
        assertEquals("test2", q.dequeue());
    }
    
    @Test
    public void testRemove() {
        Queue q = new Queue();
        
        q.enqueue("test1");
        q.enqueue("test2");
        q.enqueue("test1");
        
        assertEquals(2, q.remove("test1"));
        assertEquals(1, q.getNumberItems());
    }
    
    @Test
    public void testRefreshElement() {
        Queue q = new Queue();
        
        q.enqueue("test1");
        q.enqueue("test2");
        
        q.refreshElement("test1");
        
        assertEquals("test2", q.dequeue());
        assertEquals("test1", q.dequeue());
    }
    
    @Test
    public void testGetNumberItems() {
        Queue q = new Queue();
        
        q.enqueue("test1");
        q.enqueue("test2");
        
        assertEquals(2, q.getNumberItems());
    }
    
    @Test
    public void testGetPeakNumberItems() {
        Queue q = new Queue();
        
        q.enqueue("test1");
        q.enqueue("test2");
        q.enqueue("test3");
        
        assertEquals(3, q.getPeakNumberItems());
        
        q.enqueue("test4");
        
        assertEquals(4, q.getPeakNumberItems());
    }
    
    @Test
    public void testGetObjects() {
        Queue q = new Queue();
        
        q.enqueue("test1");
        q.enqueue("test2");
        
        Vector<Object> objects = q.getObjects();
        
        assertEquals("test1", objects.get(0));
        assertEquals("test2", objects.get(1));
    }
    
    @Test
    public void testMaxCapacityExceeded() {
        Queue q = new Queue(2);
        
        q.enqueue("test1");
        q.enqueue("test2");
        
        assertTrue(q.maxCapacityExceeded());
        
        q.dequeue();
        
        assertFalse(q.maxCapacityExceeded());
    }
    
    @Test
    public void testToString() {
        Queue q = new Queue();
        
        q.enqueue("test1");
        q.enqueue("test2");
        
        assertEquals("Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[test1, test2]\r\n]", q.toString());
    }
}