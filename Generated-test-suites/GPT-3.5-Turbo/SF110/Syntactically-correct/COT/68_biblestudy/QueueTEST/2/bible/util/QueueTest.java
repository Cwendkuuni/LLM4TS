
package bible.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class QueueTest {

  @Test
  public void testIsEmpty() {
    Queue queue = new Queue();
    assertTrue(queue.isEmpty());
    
    queue.enqueue("test");
    assertFalse(queue.isEmpty());
    
    queue.dequeue();
    assertTrue(queue.isEmpty());
  }

  @Test
  public void testEnqueue() {
    Queue queue = new Queue();
    queue.enqueue("test");

    assertFalse(queue.isEmpty());
    assertEquals(1, queue.getNumberItems());
    
    queue.enqueue("test2");
    assertEquals(2, queue.getNumberItems());
    assertEquals("test", queue.getObjects().get(0));
    assertEquals("test2", queue.getObjects().get(1));
  }

  @Test
  public void testDequeue() {
    Queue queue = new Queue();
    queue.enqueue("test");

    String dequeued = (String) queue.dequeue();

    assertEquals("test", dequeued);
    assertTrue(queue.isEmpty());
    assertEquals(0, queue.getNumberItems());
  }

  @Test
  public void testRemove() {
    Queue queue = new Queue();
    queue.enqueue("test");
    queue.enqueue("test2");
    queue.enqueue("test");
    queue.enqueue("test3");

    int removedItems = queue.remove("test");

    assertEquals(2, removedItems);
    assertFalse(queue.getObjects().contains("test"));
    assertEquals(2, queue.getNumberItems());
  }

  @Test
  public void testRefreshElement() {
    Queue queue = new Queue();
    queue.enqueue("test");
    queue.enqueue("test2");
    queue.enqueue("test3");

    queue.refreshElement("test");

    assertEquals(3, queue.getNumberItems());
    assertEquals("test", queue.getObjects().get(2));
  }

  @Test
  public void testGetNumberItems() {
    Queue queue = new Queue();
    assertEquals(0, queue.getNumberItems());

    queue.enqueue("test");
    assertEquals(1, queue.getNumberItems());

    queue.dequeue();
    assertEquals(0, queue.getNumberItems());
  }

  @Test
  public void testGetPeakNumberItems() {
    Queue queue = new Queue();
    assertEquals(0, queue.getPeakNumberItems());

    queue.enqueue("test");
    assertEquals(1, queue.getPeakNumberItems());

    queue.enqueue("test2");
    assertEquals(2, queue.getPeakNumberItems());

    queue.dequeue();
    assertEquals(2, queue.getPeakNumberItems());
  }

  @Test
  public void testGetObjects() {
    Queue queue = new Queue();
    queue.enqueue("test");
    queue.enqueue("test2");
    queue.enqueue("test3");

    assertEquals(3, queue.getObjects().size());
    assertTrue(queue.getObjects().contains("test"));
    assertTrue(queue.getObjects().contains("test2"));
    assertTrue(queue.getObjects().contains("test3"));
  }

  @Test
  public void testMaxCapacityExceeded() {
    Queue queue = new Queue(3);
    assertFalse(queue.maxCapacityExceeded());

    queue.enqueue("test");
    assertFalse(queue.maxCapacityExceeded());

    queue.enqueue("test2");
    assertFalse(queue.maxCapacityExceeded());

    queue.enqueue("test3");
    assertFalse(queue.maxCapacityExceeded());

    queue.enqueue("test4");
    assertTrue(queue.maxCapacityExceeded());
  }

  @Test
  public void testToString() {
    Queue queue = new Queue(3);
    queue.enqueue("test");
    queue.enqueue("test2");
    queue.enqueue("test3");
    queue.dequeue();

    assertEquals("bible.util.Queue:[numItems=2, maxNumItems=3, maxCapacity=3, getObjects()=[test2, test3]\r\n]", queue.toString());
  }
}
