import static org.junit.Assert.*;
import org.junit.Test;

public class QueueTest {
    
    private Queue queue;
    
    @Before
    public void setUp() {
        queue = new Queue();
    }
    
    @Test
    public void testIsEmpty() {
        assertTrue(queue.isEmpty());
        queue.enqueue("Test");
        assertFalse(queue.isEmpty());
    }
    
    @Test
    public void testEnqueue() {
        queue.enqueue("First");
        queue.enqueue("Second");
        assertEquals(queue.dequeue(), "First");
    }
    
    @Test
    public void testDequeue() {
        assertNull(queue.dequeue());
        queue.enqueue("Test");
        assertNotNull(queue.dequeue());
    }
    
    @Test
    public void testRemove() {
        queue.enqueue("Test");
        assertEquals(queue.remove("Test"), 1);
    }
    
    @Test
    public void testRefreshElement() {
        queue.enqueue("Test");
        queue.refreshElement("Test");
        assertEquals(queue.getNumberItems(), 1);
    }
    
    @Test
    public void testGetObjects() {
        queue.enqueue("First");
        queue.enqueue("Second");
        assertEquals(queue.getObjects().size(), 2);
    }
    
    @Test
    public void testMaxCapacityExceeded() {
        queue = new Queue(1);
        queue.enqueue("First");
        assertTrue(queue.maxCapacityExceeded());
    }
    
    @Test
    public void testToString() {
        assertNotNull(queue.toString());
    }
}