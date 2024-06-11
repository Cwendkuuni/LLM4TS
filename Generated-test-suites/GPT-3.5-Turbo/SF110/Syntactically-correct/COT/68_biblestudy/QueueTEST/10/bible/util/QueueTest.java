
package bible.util;

import org.junit.Assert;
import org.junit.Test;
import java.util.Vector;

public class QueueTest {

    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        Assert.assertTrue(queue.isEmpty());
    }

    @Test
    public void testEnqueue() {
        Queue queue = new Queue();
        Object obj = new Object();
        queue.enqueue(obj);
        Assert.assertFalse(queue.isEmpty());
        Assert.assertEquals(1, queue.getNumberItems());
        Assert.assertEquals(1, queue.getPeakNumberItems());
        Vector objVector = new Vector();
        objVector.addElement(obj);
        Assert.assertEquals(objVector, queue.getObjects());
    }

    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        Object obj = new Object();
        queue.enqueue(obj);
        Object dequeuedObj = queue.dequeue();
        Assert.assertEquals(obj, dequeuedObj);
        Assert.assertTrue(queue.isEmpty());
        Assert.assertEquals(0, queue.getNumberItems());
    }

    @Test
    public void testRemove() {
        Queue queue = new Queue();
        Object obj1 = new Object();
        Object obj2 = new Object();
        queue.enqueue(obj1);
        queue.enqueue(obj2);
        int removedItems = queue.remove(obj1);
        Assert.assertEquals(1, removedItems);
        Assert.assertFalse(queue.isEmpty());
        Assert.assertEquals(1, queue.getNumberItems());
        Vector objVector = new Vector();
        objVector.addElement(obj2);
        Assert.assertEquals(objVector, queue.getObjects());
    }

    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        Object obj1 = new Object();
        Object obj2 = new Object();
        queue.enqueue(obj1);
        queue.enqueue(obj2);
        queue.refreshElement(obj1);
        Assert.assertFalse(queue.isEmpty());
        Assert.assertEquals(2, queue.getNumberItems());
        Vector objVector = new Vector();
        objVector.addElement(obj2);
        objVector.addElement(obj1);
        Assert.assertEquals(objVector, queue.getObjects());
    }

    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(1);
        Object obj1 = new Object();
        Object obj2 = new Object();
        queue.enqueue(obj1);
        queue.enqueue(obj2);
        Assert.assertTrue(queue.maxCapacityExceeded());
    }

    @Test
    public void testToString() {
        Queue queue = new Queue();
        Object obj = new Object();
        queue.enqueue(obj);
        String expectedString = "bible.util.Queue:[numItems=1, maxNumItems=1, " +
                "maxCapacity=-1, getObjects()=[java.lang.Object@<hashcode>]]\r\n]";
        Assert.assertEquals(expectedString, queue.toString());
    }
}
