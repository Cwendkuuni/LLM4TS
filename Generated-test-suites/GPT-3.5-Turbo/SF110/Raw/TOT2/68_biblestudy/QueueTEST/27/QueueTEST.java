import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class QueueTest {

    private Queue testQueue;

    @Before
    public void setUp() {
        testQueue = new Queue(5);
    }

    @Test
    public void testIsEmpty() {
        assertTrue(testQueue.isEmpty());
        testQueue.enqueue("A");
        assertFalse(testQueue.isEmpty());
    }

    @Test
    public void testEnqueue() {
        testQueue.enqueue("A");
        assertEquals(1, testQueue.getNumberItems());
        testQueue.enqueue("B");
        assertEquals(2, testQueue.getNumberItems());
    }

    @Test
    public void testDequeue() {
        testQueue.enqueue("A");
        testQueue.enqueue("B");
        assertEquals("A", testQueue.dequeue());
        assertEquals(1, testQueue.getNumberItems());
    }

    @Test
    public void testRemove() {
        testQueue.enqueue("A");
        testQueue.enqueue("B");
        assertEquals(2, testQueue.remove("A"));
        assertEquals(1, testQueue.getNumberItems());
    }

    @Test
    public void testRefreshElement() {
        testQueue.enqueue("A");
        testQueue.refreshElement("A");
        assertEquals(1, testQueue.getNumberItems());
    }

    @Test
    public void testGetObjects() {
        testQueue.enqueue("A");
        testQueue.enqueue("B");
        assertEquals(2, testQueue.getObjects().size());
    }

    @Test
    public void testMaxCapacityExceeded() {
        assertFalse(testQueue.maxCapacityExceeded());
        testQueue.enqueue("A");
        testQueue.enqueue("B");
        testQueue.enqueue("C");
        testQueue.enqueue("D");
        testQueue.enqueue("E");
        assertTrue(testQueue.maxCapacityExceeded());
    }

    // Add more test cases for other methods, edge cases, and error scenarios
}