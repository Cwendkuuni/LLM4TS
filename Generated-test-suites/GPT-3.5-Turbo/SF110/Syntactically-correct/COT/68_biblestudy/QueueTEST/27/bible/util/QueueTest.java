
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Vector;

public class QueueTest {
    private Queue queue;

    @Before
    public void setUp() {
        queue = new Queue();
    }

    @Test
    public void testIsEmpty() {
        Assert.assertTrue(queue.isEmpty());
        queue.enqueue(1);
        Assert.assertFalse(queue.isEmpty());
    }

    @Test
    public void testEnqueue() {
        queue.enqueue(1);
        Assert.assertEquals(1, queue.getNumberItems());
        Assert.assertEquals(1, queue.getObjects().size());
        Assert.assertEquals(1, queue.getObjects().get(0));

        queue.enqueue(2);
        Assert.assertEquals(2, queue.getNumberItems());
        Assert.assertEquals(2, queue.getObjects().size());
        Assert.assertEquals(1, queue.getObjects().get(0));
        Assert.assertEquals(2, queue.getObjects().get(1));
    }

    @Test
    public void testDequeue() {
        Assert.assertNull(queue.dequeue());

        queue.enqueue(1);
        queue.enqueue(2);

        Assert.assertEquals(1, queue.dequeue());
        Assert.assertEquals(1, queue.getNumberItems());
        Assert.assertEquals(1, queue.getObjects().size());
        Assert.assertEquals(2, queue.getObjects().get(0));

        Assert.assertEquals(2, queue.dequeue());
        Assert.assertEquals(0, queue.getNumberItems());
        Assert.assertEquals(0, queue.getObjects().size());
    }

    @Test
    public void testRemove() {
        Assert.assertEquals(0, queue.remove(1));

        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(1);
        queue.enqueue(3);

        Assert.assertEquals(2, queue.remove(1));
        Assert.assertEquals(2, queue.getNumberItems());
        Assert.assertEquals(2, queue.getObjects().size());
        Assert.assertEquals(2, queue.getObjects().get(0));
        Assert.assertEquals(3, queue.getObjects().get(1));
    }

    @Test
    public void testRefreshElement() {
        queue.refreshElement(1);
        Assert.assertEquals(0, queue.getNumberItems());
        Assert.assertEquals(0, queue.getObjects().size());

        queue.enqueue(1);
        queue.enqueue(2);

        Assert.assertEquals(2, queue.refreshElement(1));
        Assert.assertEquals(2, queue.getNumberItems());
        Assert.assertEquals(2, queue.getObjects().size());
        Assert.assertEquals(2, queue.getObjects().get(0));
        Assert.assertEquals(1, queue.getObjects().get(1));
    }

    @Test
    public void testGetNumberItems() {
        Assert.assertEquals(0, queue.getNumberItems());

        queue.enqueue(1);
        Assert.assertEquals(1, queue.getNumberItems());

        queue.enqueue(2);
        Assert.assertEquals(2, queue.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems() {
        Assert.assertEquals(0, queue.getPeakNumberItems());

        queue.enqueue(1);
        Assert.assertEquals(1, queue.getPeakNumberItems());

        queue.enqueue(2);
        Assert.assertEquals(2, queue.getPeakNumberItems());

        queue.dequeue();
        Assert.assertEquals(2, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        Vector emptyVector = new Vector();
        Assert.assertEquals(emptyVector, queue.getObjects());

        queue.enqueue(1);
        Vector vector1 = new Vector();
        vector1.add(1);
        Assert.assertEquals(vector1, queue.getObjects());

        queue.enqueue(2);
        Vector vector2 = new Vector();
        vector2.add(1);
        vector2.add(2);
        Assert.assertEquals(vector2, queue.getObjects());
    }

    @Test
    public void testMaxCapacityExceeded() {
        queue = new Queue(2);
        Assert.assertFalse(queue.maxCapacityExceeded());

        queue.enqueue(1);
        queue.enqueue(2);
        Assert.assertTrue(queue.maxCapacityExceeded());

        queue.enqueue(3);
        Assert.assertTrue(queue.maxCapacityExceeded());

        queue.dequeue();
        Assert.assertFalse(queue.maxCapacityExceeded());
    }
}
