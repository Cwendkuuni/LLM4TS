
package bible.util.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bible.util.Queue;

public class QueueTest {

    private Queue queue;
    
    @Before
    public void setUp() {
        queue = new Queue();
    }
    
    @After
    public void tearDown() {
        queue = null;
    }
    
  	// Typical use case: enqueue an object and dequeue it
  	@Test
    public void testEnqueueAndDequeue() {
        String obj = "test";
        queue.enqueue(obj);
        Object dequeuedObj = queue.dequeue();
        assertEquals("Dequeued object should be equal to the enqueued object", obj, dequeuedObj);
    }
  
  	// Typical use case: enqueue multiple objects and verify number of items
  	@Test
    public void testEnqueueMultipleObjectsAndGetNumberItems() {
        queue.enqueue("object1");
        queue.enqueue("object2");
        queue.enqueue("object3");
        assertEquals("Number of items should be equal to the number of enqueued objects", 3, queue.getNumberItems());
    }
  
  	// Edge case: enqueue object beyond max capacity and verify if max capacity exceeded
  	@Test
    public void testEnqueueObjectsBeyondMaxCapacityAndVerifyMaxCapacityExceeded() {
        queue = new Queue(5);
        queue.enqueue("object1");
        queue.enqueue("object2");
        queue.enqueue("object3");
        queue.enqueue("object4");
        queue.enqueue("object5");
        queue.enqueue("object6");
        assertTrue("Max capacity should be exceeded", queue.maxCapacityExceeded());
    }
  
  	// Error scenario: dequeue from empty queue and verify if null is returned
  	@Test
    public void testDequeueFromEmptyQueue() {
        Object dequeuedObj = queue.dequeue();
        assertNull("Dequeued object from empty queue should be null", dequeuedObj);
    }
  
  	// Error scenario: remove an object that is not in the queue and verify if no items are removed
  	@Test
    public void testRemoveNonExistentObject() {
        queue.enqueue("object1");
        queue.enqueue("object2");
        queue.enqueue("object3");
        int removedItems = queue.remove("object4");
        assertEquals("No items should be removed from the queue", 0, removedItems);
    }
  
  	// Error scenario: remove objects from queue and verify number of removed items
  	@Test
    public void testRemoveObjectsAndGetNumberRemovedItems() {
        queue.enqueue("object1");
        queue.enqueue("object2");
        queue.enqueue("object3");
        queue.enqueue("object2");
        int removedItems = queue.remove("object2");
        assertEquals("Number of removed items should be equal to the number of occurrences of the object in the queue", 2, removedItems);
    }
  
  	// Error scenario: refresh an object that does not exist in the queue and verify number of removed items
  	@Test
    public void testRefreshNonExistentObject() {
        queue.enqueue("object1");
        queue.enqueue("object2");
        queue.enqueue("object3");
        queue.refreshElement("object4");
        assertEquals("No items should be removed from the queue", 0, queue.getNumberItems());
    }
  
  	// Error scenario: refresh an object in the queue and verify if object is added again to the queue
  	@Test
    public void testRefreshObjectAndVerifyAddedToQueue() {
        queue.enqueue("object1");
        queue.enqueue("object2");
        queue.enqueue("object3");
        queue.refreshElement("object2");
        assertEquals("Number of items should be equal to the number of enqueued objects", 3, queue.getNumberItems());
    }
  
  	// Typical use case: get peak number of items after enqueueing multiple objects
  	@Test
    public void testGetPeakNumberItems() {
        queue.enqueue("object1");
        queue.enqueue("object2");
        queue.enqueue("object3");
        assertEquals("Peak number of items should be equal to the number of enqueued objects", 3, queue.getPeakNumberItems());
    }
  
  	// Typical use case: get all objects in the queue in a vector
	  @Test
    public void testGetObjects() {
        queue.enqueue("object1");
        queue.enqueue("object2");
        queue.enqueue("object3");
        assertEquals("Vector should contain all the objects in the queue", 3, queue.getObjects().size());
    }
}
