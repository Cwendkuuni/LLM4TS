
package bible.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.Vector;

public class QueueTest {

  @Test
  public void testIsEmpty() {
    Queue queue = new Queue();
    Assert.assertTrue(queue.isEmpty());
    
    queue.enqueue("Test");
    Assert.assertFalse(queue.isEmpty());
  }
  
  @Test
  public void testEnqueue() {
    Queue queue = new Queue();
    queue.enqueue("Test");
    
    Vector objects = queue.getObjects();
    Assert.assertEquals(1, objects.size());
    Assert.assertEquals("Test", objects.get(0));
  }
  
  @Test
  public void testDequeue() {
    Queue queue = new Queue();
    queue.enqueue("Test");
    
    Object dequeuedObject = queue.dequeue();
    Assert.assertEquals("Test", dequeuedObject);
    Assert.assertTrue(queue.isEmpty());
  }
  
  @Test
  public void testRemove() {
    Queue queue = new Queue();
    queue.enqueue("Test1");
    queue.enqueue("Test2");
    queue.enqueue("Test3");
    
    int itemsRemoved = queue.remove("Test2");
    Assert.assertEquals(1, itemsRemoved);
    
    Vector objects = queue.getObjects();
    Assert.assertEquals(2, objects.size());
    Assert.assertEquals("Test1", objects.get(0));
    Assert.assertEquals("Test3", objects.get(1));
  }
  
  @Test
  public void testRefreshElement() {
    Queue queue = new Queue();
    queue.enqueue("Test1");
    queue.enqueue("Test2");
    queue.enqueue("Test3");
    
    queue.refreshElement("Test2");
    
    Vector objects = queue.getObjects();
    Assert.assertEquals(3, objects.size());
    Assert.assertEquals("Test1", objects.get(0));
    Assert.assertEquals("Test3", objects.get(1));
    Assert.assertEquals("Test2", objects.get(2));
  }
  
  @Test
  public void testGetNumberItems() {
    Queue queue = new Queue();
    queue.enqueue("Test1");
    
    Assert.assertEquals(1, queue.getNumberItems());
  }
  
  @Test
  public void testGetPeakNumberItems() {
    Queue queue = new Queue();
    queue.enqueue("Test1");
    queue.enqueue("Test2");
    
    Assert.assertEquals(2, queue.getPeakNumberItems());
  }
  
  @Test
  public void testGetObjects() {
    Queue queue = new Queue();
    queue.enqueue("Test1");
    queue.enqueue("Test2");
    queue.enqueue("Test3");
    
    Vector objects = queue.getObjects();
    Assert.assertEquals(3, objects.size());
    Assert.assertEquals("Test1", objects.get(0));
    Assert.assertEquals("Test2", objects.get(1));
    Assert.assertEquals("Test3", objects.get(2));
  }
  
  @Test
  public void testMaxCapacityExceeded() {
    Queue queue = new Queue(2);
    queue.enqueue("Test1");
    queue.enqueue("Test2");
    
    Assert.assertTrue(queue.maxCapacityExceeded());
    
    queue.dequeue();
    
    Assert.assertFalse(queue.maxCapacityExceeded());
  }
  
  @Test
  public void testToString() {
    Queue queue = new Queue();
    queue.enqueue("Test1");
    queue.enqueue("Test2");
    queue.enqueue("Test3");
    
    String expectedToString = "bible.util.Queue:[numItems=3, maxNumItems=3, maxCapacity=-1, getObjects()=[Test1, Test2, Test3]\r\n]";
    Assert.assertEquals(expectedToString, queue.toString());
  }
  
}
