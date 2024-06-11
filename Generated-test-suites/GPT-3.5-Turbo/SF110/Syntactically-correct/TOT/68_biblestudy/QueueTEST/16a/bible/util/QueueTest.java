import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Vector;

public class QueueTest {

    private Queue queue;

    @Before
    public void setUp() {
        queue = new Queue();
    }
    
    @Test
    public void testIsEmpty() {
        assertTrue(queue.isEmpty());

        queue.enqueue("Test Object");
        assertFalse(queue.isEmpty());
    }
    
    @Test
    public void testEnqueue() {
        String testObject = "Test Object";
        queue.enqueue(testObject);

        assertEquals(testObject, queue.getObjects().get(0));
        assertEquals(1, queue.getNumberItems());
        assertEquals(1, queue.getPeakNumberItems());
    }
    
    @Test
    public void testDequeue() {
        assertNull(queue.dequeue());

        String testObject = "Test Object";
        queue.enqueue(testObject);

        assertEquals(testObject, queue.dequeue());
        assertEquals(0, queue.getNumberItems());
        assertEquals(0, queue.getPeakNumberItems());
    }

    @Test
    public void testRemove() {
        assertEquals(0, queue.remove("Test Object"));

        String testObject = "Test Object";
        queue.enqueue(testObject);

        assertEquals(1, queue.remove(testObject));
        assertEquals(0, queue.getNumberItems());
        assertEquals(0, queue.getPeakNumberItems());
    }

    @Test
    public void testRefreshElement() {
        String testObject = "Test Object";
        queue.enqueue(testObject);

        queue.refreshElement(testObject);
        assertEquals(testObject, queue.getObjects().lastElement());
    }

    @Test
    public void testGetNumberItems() {
        assertEquals(0, queue.getNumberItems());

        queue.enqueue("Test Object 1");
        queue.enqueue("Test Object 2");

        assertEquals(2, queue.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems() {
        assertEquals(0, queue.getPeakNumberItems());

        queue.enqueue("Test Object 1");
        queue.enqueue("Test Object 2");

        assertEquals(2, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        Vector objects = new Vector();
        objects.addElement("Test Object 1");
        objects.addElement("Test Object 2");
        Vector queueObjects = queue.getObjects();

        assertEquals(objects.size(), queueObjects.size());
        assertEquals(objects.elementAt(0), queueObjects.elementAt(0));
        assertEquals(objects.elementAt(1), queueObjects.elementAt(1));
    }

    @Test
    public void testMaxCapacityExceeded() {
        assertFalse(queue.maxCapacityExceeded());

        queue = new Queue(1);
        queue.enqueue("Test Object 1");
        assertTrue(queue.maxCapacityExceeded());

        queue.dequeue();
        assertFalse(queue.maxCapacityExceeded());
    }

    @Test
    public void testToString() {
        String expectedString = "bible.util.Queue:[numItems=0, maxNumItems=0, maxCapacity=-1, getObjects()=\r\n[]\r\n]";

        assertEquals(expectedString, queue.toString());
    }
}