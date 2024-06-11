import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.concurrent.TimeUnit;

public class MonitorTest {
  
  private Monitor monitor;
  
  @Before
  public void setup() {
    monitor = new Monitor();
  }
  
  // Testing enter() method
  
  @Test
  public void enter_lockIsAcquired() {
    monitor.enter();
    assertTrue(monitor.isOccupiedByCurrentThread());
  }
  
  // Testing enterInterruptibly() method
  
  @Test
  public void enterInterruptibly_lockIsAcquired() throws InterruptedException {
    monitor.enterInterruptibly();
    assertTrue(monitor.isOccupiedByCurrentThread());
  }
  
  // Testing enter(long time, TimeUnit unit) method
  
  @Test
  public void enter_withTimeout_lockIsAcquired() {
    boolean entered = monitor.enter(1000, TimeUnit.MILLISECONDS);
    assertTrue(entered);
    assertTrue(monitor.isOccupiedByCurrentThread());
  }
  
  @Test
  public void enter_timeoutExpired_lockIsNotAcquired() {
    boolean entered = monitor.enter(100, TimeUnit.MILLISECONDS);
    assertFalse(entered);
    assertFalse(monitor.isOccupiedByCurrentThread());
  }
  
  // Testing enterInterruptibly(long time, TimeUnit unit) method
  
  @Test
  public void enterInterruptibly_withTimeout_lockIsAcquired() throws InterruptedException {
    boolean entered = monitor.enterInterruptibly(1000, TimeUnit.MILLISECONDS);
    assertTrue(entered);
    assertTrue(monitor.isOccupiedByCurrentThread());
  }
  
  @Test
  public void enterInterruptibly_timeoutExpired_lockIsNotAcquired() throws InterruptedException {
    boolean entered = monitor.enterInterruptibly(100, TimeUnit.MILLISECONDS);
    assertFalse(entered);
    assertFalse(monitor.isOccupiedByCurrentThread());
  }
  
  // Add more test cases for the rest of the methods
  
  // ...
  
}