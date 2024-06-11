import org.junit.*;
import static org.junit.Assert.*;
import org.junit.runners.MethodSorters;
import java.util.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class QueueTest {

    private Queue queue;
    
    @Before
    public void setUp() {
        queue = new Queue();
    }
    
    // Test cases for isEmpty() method
    
    @Test
    public void test1IsEmpty() {
        assertTrue(queue.isEmpty());
    }
    
    @Test
    public void test2IsEmpty() {
        queue.enqueue("Test");
        assertFalse(queue.isEmpty());
    }
    
    
    // Test cases for enqueue() method
    
    @Test
    public void test1Enqueue() {
        queue.enqueue("Test");
        assertEquals(1, queue.getNumberItems());
    }
    
    @Test
    public void test2Enqueue() {
        queue.enqueue("Test1");
        queue.enqueue("Test2");
        assertEquals(2, queue.getNumberItems());
    }
    
    @Test
    public void test3Enqueue() {
        queue.enqueue(null);
        assertEquals(0, queue.getNumberItems());
    }
    
    
    // Test cases for dequeue() method
    
    @Test
    public void test1Dequeue() {
        assertNull(queue.dequeue());
    }
    
    @Test
    public void test2Dequeue() {
        queue.enqueue("Test");
        assertEquals("Test", queue.dequeue());
        assertEquals(0, queue.getNumberItems());
    }
    
    @Test
    public void test3Dequeue() {
        queue.enqueue("Test1");
        queue.enqueue("Test2");
        assertEquals("Test1", queue.dequeue());
        assertEquals(1, queue.getNumberItems());
    }
    
    
    // Test cases for remove() method
    
    @Test
    public void test1Remove() {
        assertEquals(0, queue.remove(null));
    }
    
    @Test
    public void test2Remove() {
        queue.enqueue("Test1");
        queue.enqueue("Test2");
        assertEquals(0, queue.remove("Test3"));
    }
    
    @Test
    public void test3Remove() {
        queue.enqueue("Test");
        assertEquals(1, queue.remove("Test"));
        assertEquals(0, queue.getNumberItems());
    }
    
    
    // Test cases for refreshElement() method
    
    @Test
    public void test1RefreshElement() {
        queue.refreshElement("Test");
        assertEquals(0, queue.getNumberItems());
    }
    
    @Test
    public void test2RefreshElement() {
        queue.enqueue("Test1");
        queue.enqueue("Test2");
        queue.refreshElement("Test1");
        assertEquals(2, queue.getNumberItems());
    }
    
    
    // Test cases for getNumberItems() method
    
    @Test
    public void test1GetNumberItems() {
        assertEquals(0, queue.getNumberItems());
    }
    
    @Test
    public void test2GetNumberItems() {
        queue.enqueue("Test1");
        queue.enqueue("Test2");
        assertEquals(2, queue.getNumberItems());
    }
    
    
    // Test cases for getPeakNumberItems() method
    
    @Test
    public void test1GetPeakNumberItems() {
        assertEquals(0, queue.getPeakNumberItems());
    }
    
    @Test
    public void test2GetPeakNumberItems() {
        queue.enqueue("Test1");
        queue.enqueue("Test2");
        assertEquals(2, queue.getPeakNumberItems());
    }
    
    
    // Test cases for getObjects() method
    
    @Test
    public void test1GetObjects() {
        Vector testVector = new Vector();
        assertTrue(queue.getObjects().equals(testVector));
    }
    
    @Test
    public void test2GetObjects() {
        queue.enqueue("Test1");
        queue.enqueue("Test2");
        Vector testVector = new Vector();
        testVector.add("Test1");
        testVector.add("Test2");
        assertTrue(queue.getObjects().equals(testVector));
    }
    
    
    // Test cases for maxCapacityExceeded() method
    
    @Test
    public void test1MaxCapacityExceeded() {
        assertFalse(queue.maxCapacityExceeded());
    }
    
    @Test
    public void test2MaxCapacityExceeded() {
        Queue queue = new Queue(1);
        queue.enqueue("Test1");
        assertTrue(queue.maxCapacityExceeded());
    }
    
    
    // Test case for toString() method
    
    @Test
    public void testToString() {
        queue.enqueue("Test");
        String expected = "bible.util.Queue:[numItems=1, maxNumItems=1, maxCapacity=-1, getObjects()=[Test]]\r\n";
        assertEquals(expected, queue.toString());
    }
}