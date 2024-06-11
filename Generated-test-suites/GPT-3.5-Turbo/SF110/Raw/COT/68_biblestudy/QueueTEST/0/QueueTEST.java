
import org.junit.*;
import java.util.*;

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
    Assert.assertFalse(queue.isEmpty());
    Assert.assertEquals(1, queue.getNumberItems());
    Assert.assertEquals(1, queue.getPeakNumberItems());
    
    queue.enqueue("Another Test");
    Assert.assertFalse(queue.isEmpty());
    Assert.assertEquals(2, queue.getNumberItems());
    Assert.assertEquals(2, queue.getPeakNumberItems());
  }
  
  @Test
  public void testDequeue() {
    Queue queue = new Queue();
    queue.enqueue("Test");
    queue.enqueue("Another Test");
    Assert.assertEquals("Test", queue.dequeue());
    Assert.assertEquals(1, queue.getNumberItems());
    
    Assert.assertEquals("Another Test", queue.dequeue());
    Assert.assertEquals(0, queue.getNumberItems());
    
    Assert.assertNull(queue.dequeue());
  }
  
  @Test
  public void testRemove() {
    Queue queue = new Queue();
    queue.enqueue("Test");
    queue.enqueue("Test");
    queue.enqueue("Another Test");
    
    Assert.assertEquals(2, queue.remove("Test"));
    Assert.assertEquals(1, queue.getNumberItems());
    
    Assert.assertEquals(1, queue.remove("Another Test"));
    Assert.assertEquals(0, queue.getNumberItems());
    
    Assert.assertEquals(0, queue.remove("Invalid Object"));
  }
  
  @Test
  public void testRefreshElement() {
    Queue queue = new Queue();
    queue.enqueue("Test");
    queue.enqueue("Another Test");
    
    queue.refreshElement("Test");
    Assert.assertEquals(1, queue.getNumberItems());
    Assert.assertEquals("Another Test", queue.dequeue());
    
    queue.enqueue("Test");
    queue.refreshElement("Another Test");
    Assert.assertEquals(1, queue.getNumberItems());
    Assert.assertEquals("Test", queue.dequeue());
  }
  
  @Test
  public void testGetNumberItems() {
    Queue queue = new Queue();
    Assert.assertEquals(0, queue.getNumberItems());
    
    queue.enqueue("Test");
    Assert.assertEquals(1, queue.getNumberItems());
    
    queue.enqueue("Another Test");
    Assert.assertEquals(2, queue.getNumberItems());
    
    queue.dequeue();
    Assert.assertEquals(1, queue.getNumberItems());
  }
  
  @Test
  public void testGetPeakNumberItems() {
    Queue queue = new Queue();
    Assert.assertEquals(0, queue.getPeakNumberItems());
    
    queue.enqueue("Test");
    Assert.assertEquals(1, queue.getPeakNumberItems());
    
    queue.enqueue("Another Test");
    Assert.assertEquals(2, queue.getPeakNumberItems());
    
    queue.dequeue();
    queue.enqueue("Another Test");
    Assert.assertEquals(2, queue.getPeakNumberItems());
  }
  
  @Test
  public void testGetObjects() {
    Queue queue = new Queue();
    Assert.assertEquals(new Vector(), queue.getObjects());
    
    queue.enqueue("Test");
    Vector expected = new Vector();
    expected.addElement("Test");
    Assert.assertEquals(expected, queue.getObjects());
    
    queue.enqueue("Another Test");
    expected.addElement("Another Test");
    Assert.assertEquals(expected, queue.getObjects());
    
    queue.dequeue();
    expected.removeElement("Test");
    Assert.assertEquals(expected, queue.getObjects());
  }
  
  @Test
  public void testMaxCapacityExceeded() {
    Queue queue = new Queue(2);
    Assert.assertFalse(queue.maxCapacityExceeded());
    
    queue.enqueue("Test");
    queue.enqueue("Another Test");
    Assert.assertTrue(queue.maxCapacityExceeded());
    
    queue.dequeue();
    Assert.assertFalse(queue.maxCapacityExceeded());
  }
  
  @Test
  public void testToString() {
    Queue queue = new Queue();
    Assert.assertEquals("bible.util.Queue:[numItems=0, maxNumItems=0, maxCapacity=-1, getObjects()=[]\r\n]", queue.toString());
    
    queue.enqueue("Test");
    Assert.assertEquals("bible.util.Queue:[numItems=1, maxNumItems=1, maxCapacity=-1, getObjects()=[Test]\r\n]", queue.toString());
    
    queue.enqueue("Another Test");
    Assert.assertEquals("bible.util.Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[Test, Another Test]\r\n]", queue.toString());
    
    queue.dequeue();
    Assert.assertEquals("bible.util.Queue:[numItems=1, maxNumItems=2, maxCapacity=-1, getObjects()=[Another Test]\r\n]", queue.toString());
  }
}
