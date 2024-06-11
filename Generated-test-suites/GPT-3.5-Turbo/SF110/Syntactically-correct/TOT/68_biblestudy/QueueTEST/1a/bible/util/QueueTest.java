import org.junit.Test;
import static org.junit.Assert.*;

public class QueueTest {
  
  @Test
  public void testIsEmpty() {
    Queue q = new Queue();
    assertTrue(q.isEmpty());
    
    q.enqueue("element");
    assertFalse(q.isEmpty());
  }
  
  @Test
  public void testEnqueue() {
    Queue q = new Queue();
    q.enqueue("element");
    
    assertFalse(q.isEmpty());
    assertEquals(1, q.getNumberItems());
    assertEquals(1, q.getPeakNumberItems());
  }
  
  @Test
  public void testDequeue() {
    Queue q = new Queue();
    q.enqueue("element1");
    q.enqueue("element2");
    
    assertEquals("element1", q.dequeue());
    assertEquals(1, q.getNumberItems());
    
    assertEquals("element2", q.dequeue());
    assertEquals(0, q.getNumberItems());
    
    assertNull(q.dequeue());
    
    System.out.println("Cannot remove when queue is empty");
  }
  
  @Test
  public void testRemove() {
    Queue q = new Queue();
    q.enqueue("element1");
    q.enqueue("element2");
    q.enqueue("element1");
    q.enqueue("element3");
    q.enqueue("element1");
    
    assertEquals(3, q.remove("element1"));
    assertEquals(2, q.getNumberItems());
    
    assertEquals(1, q.remove("element2"));
    assertEquals(1, q.getNumberItems());
    
    assertEquals(1, q.remove("element3"));
    assertEquals(0, q.getNumberItems());
    
    q.enqueue("element4");
    q.enqueue("element5");
    
    assertEquals(4, q.remove("element1"));
    assertEquals(2, q.getNumberItems());
    
    assertEquals(4, q.remove("nonexistent"));
    assertEquals(0, q.getNumberItems());
  }
  
  @Test
  public void testRefreshElement() {
    Queue q = new Queue();
    q.enqueue("element1");
    q.enqueue("element2");
    
    assertEquals(2, q.getNumberItems());
    assertEquals(2, q.getPeakNumberItems());
    
    q.refreshElement("element1");
    
    assertEquals(1, q.getNumberItems());
    assertEquals(2, q.getPeakNumberItems());
    
    assertEquals("element1", q.dequeue());
    assertEquals("element2", q.dequeue());
    assertEquals(0, q.getNumberItems());
    assertTrue(q.isEmpty());
  }
  
  @Test
  public void testGetObjects() {
    Queue q = new Queue();
    q.enqueue("element1");
    q.enqueue("element2");
    q.enqueue("element3");
    
    assertEquals(3, q.getObjects().size());
    assertTrue(q.getObjects().contains("element1"));
    assertTrue(q.getObjects().contains("element2"));
    assertTrue(q.getObjects().contains("element3"));
    
    q.dequeue();
    
    assertEquals(2, q.getObjects().size());
    assertFalse(q.getObjects().contains("element1"));
  }
  
  @Test
  public void testMaxCapacityExceeded() {
    Queue q = new Queue(3);
    q.enqueue("element1");
    q.enqueue("element2");
    q.enqueue("element3");
    
    assertFalse(q.maxCapacityExceeded());
    
    q.enqueue("element4");
    
    assertTrue(q.maxCapacityExceeded());
  }
  
  @Test
  public void testToString() {
    Queue q = new Queue();
    q.enqueue("element1");
    q.enqueue("element2");
    q.enqueue("element3");
    
    String expected = "bible.util.Queue:[numItems=3, maxNumItems=3, maxCapacity=-1, getObjects()=[element1, element2, element3]\r\n]";
    assertEquals(expected, q.toString());
  }
}