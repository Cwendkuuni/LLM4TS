import org.junit.*;
import static org.junit.Assert.*;
import java.util.Vector;

public class QueueTest {
    
    private Queue queue;
    
    @Before
    public void setUp() {
        queue = new Queue(5);
    }
    
    @Test
    public void testEnqueue() {
        assertEquals(0, queue.getNumberItems());
        queue.enqueue("Test");
        assertEquals(1, queue.getNumberItems());
    }
    
    @Test
    public void testDequeue() {
        queue.enqueue("Test");
        assertEquals(1, queue.getNumberItems());
        assertEquals("Test", queue.dequeue());
        assertEquals(0, queue.getNumberItems());
    }
    
    @Test
    public void testRemove() {
        queue.enqueue("Test");
        queue.enqueue("Hello");
        assertEquals(2, queue.getNumberItems());
        assertEquals(1, queue.remove("Test"));
        assertEquals(1, queue.getNumberItems());
    }
    
    @Test
    public void testRefreshElement() {
        queue.enqueue("Test");
        assertEquals(1, queue.getNumberItems());
        queue.refreshElement("Test");
        assertEquals(1, queue.getNumberItems());
    }
    
    @Test
    public void testMaxCapacityExceeded() {
        assertFalse(queue.maxCapacityExceeded());
        queue.enqueue("1");
        queue.enqueue("2");
        queue.enqueue("3");
        queue.enqueue("4");
        queue.enqueue("5");
        assertTrue(queue.maxCapacityExceeded());
    }
    
    @Test
    public void testGetObjects() {
        queue.enqueue("Test");
        Vector objects = queue.getObjects();
        assertEquals(1, objects.size());
        assertEquals("Test", objects.get(0));
    }
    
    @Test
    public void testToString() {
        assertEquals("bible.util.Queue:[numItems=0, maxNumItems=0, maxCapacity=5, getObjects()=[]\r\n]", queue.toString());
    }
    
}