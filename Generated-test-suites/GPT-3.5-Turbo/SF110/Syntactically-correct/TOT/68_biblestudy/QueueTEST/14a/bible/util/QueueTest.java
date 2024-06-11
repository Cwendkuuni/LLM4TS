import static org.junit.Assert.*;
import org.junit.*;

public class QueueTest {

    Queue queue;
    
    @Before
    public void setUp() {
        queue = new Queue();
    }
    
    @After
    public void tearDown() {
        queue = null;
    }
    
    // Test isEmpty() method
    @Test
    public void testIsEmpty() {
        assertTrue(queue.isEmpty());
        queue.enqueue("Test");
        assertFalse(queue.isEmpty());
        queue.dequeue();
        assertTrue(queue.isEmpty());
    }
    
    // Test enqueue() method
    @Test
    public void testEnqueue() {
        assertEquals(0, queue.getNumberItems());
        queue.enqueue("Test");
        assertEquals(1, queue.getNumberItems());
        queue.enqueue(123);
        assertEquals(2, queue.getNumberItems());
    }
    
    // Test dequeue() method
    @Test
    public void testDequeue() {
        assertNull(queue.dequeue());
        queue.enqueue("Test1");
        queue.enqueue("Test2");
        assertEquals("Test1", queue.dequeue());
        assertEquals(1, queue.getNumberItems());
        assertEquals("Test2", queue.dequeue());
        assertTrue(queue.isEmpty());
    }
    
    // Test remove() method
    @Test
    public void testRemove() {
        assertEquals(0, queue.remove("Test"));
        queue.enqueue("Test1");
        queue.enqueue("Test2");
        assertEquals(1, queue.remove("Test1"));
        assertEquals("Test2", queue.dequeue());
        assertTrue(queue.isEmpty());
    }
    
    // Test refreshElement() method
    @Test
    public void testRefreshElement() {
        queue.enqueue("Test1");
        queue.enqueue("Test2");
        queue.enqueue("Test3");
        assertEquals(3, queue.getNumberItems());
        queue.refreshElement("Test2");
        assertEquals(3, queue.getNumberItems());
        assertEquals("Test1", queue.dequeue());
        assertEquals("Test3", queue.dequeue());
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.getNumberItems());
    }
    
    // Test getNumberItems() method
    @Test
    public void testGetNumberItems() {
        assertEquals(0, queue.getNumberItems());
        queue.enqueue("Test1");
        queue.enqueue("Test2");
        assertEquals(2, queue.getNumberItems());
        queue.dequeue();
        assertEquals(1, queue.getNumberItems());
    }
    
    // Test getPeakNumberItems() method
    @Test
    public void testGetPeakNumberItems() {
        assertEquals(0, queue.getPeakNumberItems());
        queue.enqueue("Test1");
        assertEquals(1, queue.getPeakNumberItems());
        queue.enqueue("Test2");
        assertEquals(2, queue.getPeakNumberItems());
        queue.dequeue();
        assertEquals(2, queue.getPeakNumberItems());
    }
    
    // Test getObjects() method
    @Test
    public void testGetObjects() {
        queue.enqueue("Test1");
        queue.enqueue("Test2");
        queue.enqueue("Test3");
        assertEquals(3, queue.getNumberItems());
        Vector objects = queue.getObjects();
        assertEquals(3, objects.size());
        assertEquals("Test1", objects.get(0));
        assertEquals("Test2", objects.get(1));
        assertEquals("Test3", objects.get(2));
    }
    
    // Test maxCapacityExceeded() method
    @Test
    public void testMaxCapacityExceeded() {
        assertFalse(queue.maxCapacityExceeded());
        queue = new Queue(3);
        queue.enqueue("Test1");
        queue.enqueue("Test2");
        queue.enqueue("Test3");
        assertFalse(queue.maxCapacityExceeded());
        queue.enqueue("Test4");
        assertTrue(queue.maxCapacityExceeded());
    }
    
    // Test toString() method
    @Test
    public void testToString() {
        assertEquals("bible.util.Queue:[numItems=0, maxNumItems=0, maxCapacity=-1, getObjects()=[]\r\n]", queue.toString());
        queue.enqueue("Test1");
        queue.enqueue("Test2");
        assertEquals("bible.util.Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[Test1, Test2]\r\n]", queue.toString());
    }
    
}