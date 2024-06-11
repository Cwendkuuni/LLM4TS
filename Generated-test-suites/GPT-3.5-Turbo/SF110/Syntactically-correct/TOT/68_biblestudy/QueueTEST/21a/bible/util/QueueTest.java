
package bible.util;

import org.junit.Assert;
import org.junit.Test;
import java.util.Vector;

public class QueueTest {

    // Test isEmpty() method
    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        Assert.assertTrue(queue.isEmpty());

        queue.enqueue("Item 1");
        Assert.assertFalse(queue.isEmpty());
    }

    // Test enqueue() method
    @Test
    public void testEnqueue() {
        Queue queue = new Queue();
        queue.enqueue("Item 1");

        Assert.assertEquals(1, queue.getNumberItems());
        Assert.assertEquals(1, queue.getPeakNumberItems());
        Assert.assertFalse(queue.maxCapacityExceeded());
    }

    // Test dequeue() method
    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        queue.enqueue("Item 3");

        Assert.assertEquals("Item 1", queue.dequeue());
        Assert.assertEquals(2, queue.getNumberItems());
        Assert.assertFalse(queue.isEmpty());
    }

    // Test remove() method
    @Test
    public void testRemove() {
        Queue queue = new Queue();
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        queue.enqueue("Item 1");

        Assert.assertEquals(2, queue.remove("Item 1"));
        Assert.assertEquals(1, queue.getNumberItems());
        Assert.assertFalse(queue.getObjects().contains("Item 1"));
    }

    // Test refreshElement() method
    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        queue.enqueue("Item 3");

        queue.refreshElement("Item 2");
        Assert.assertEquals(3, queue.getNumberItems());
        Assert.assertEquals("Item 2", queue.dequeue());
        Assert.assertEquals(2, queue.getNumberItems());
    }

    // Test getNumberItems() method
    @Test
    public void testGetNumberItems() {
        Queue queue = new Queue();
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        queue.enqueue("Item 3");

        Assert.assertEquals(3, queue.getNumberItems());
    }

    // Test getPeakNumberItems() method
    @Test
    public void testGetPeakNumberItems() {
        Queue queue = new Queue();
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        queue.enqueue("Item 3");

        Assert.assertEquals(3, queue.getPeakNumberItems());

        queue.enqueue("Item 4");
        queue.enqueue("Item 5");
        Assert.assertEquals(5, queue.getPeakNumberItems());
    }

    // Test getObjects() method
    @Test
    public void testGetObjects() {
        Queue queue = new Queue();
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        queue.enqueue("Item 3");

        Vector expectedVector = new Vector();
        expectedVector.add("Item 1");
        expectedVector.add("Item 2");
        expectedVector.add("Item 3");

        Assert.assertEquals(expectedVector, queue.getObjects());
    }

    // Test maxCapacityExceeded() method
    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(2);
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");

        Assert.assertTrue(queue.maxCapacityExceeded());

        queue.dequeue();
        Assert.assertFalse(queue.maxCapacityExceeded());
    }

    // Test toString() method
    @Test
    public void testToString() {
        Queue queue = new Queue();
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");

        String expectedString = "bible.util.Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[Item 1, Item 2]\r\n]";
        Assert.assertEquals(expectedString, queue.toString());
    }
}
