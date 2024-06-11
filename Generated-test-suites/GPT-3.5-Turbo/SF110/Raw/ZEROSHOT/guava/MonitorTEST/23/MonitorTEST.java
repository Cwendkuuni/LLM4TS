import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.concurrent.TimeUnit;

public class MonitorTest {

  private Monitor monitor;

  @Before
  public void setUp() {
    monitor = new Monitor();
  }

  @Test
  public void testEnter() {
    monitor.enter();
    assertTrue(monitor.isOccupiedByCurrentThread());
  }

  @Test
  public void testEnterInterruptibly() throws InterruptedException {
    monitor.enterInterruptibly();
    assertTrue(monitor.isOccupiedByCurrentThread());
  }

  @Test
  public void testEnterWithTimeout() {
    boolean entered = monitor.enter(1, TimeUnit.SECONDS);
    assertTrue(entered);
    assertTrue(monitor.isOccupiedByCurrentThread());
  }

  @Test
  public void testEnterInterruptiblyWithTimeout() throws InterruptedException {
    boolean entered = monitor.enterInterruptibly(1, TimeUnit.SECONDS);
    assertTrue(entered);
    assertTrue(monitor.isOccupiedByCurrentThread());
  }

  @Test
  public void testTryEnter() {
    boolean entered = monitor.tryEnter();
    assertTrue(entered);
    assertTrue(monitor.isOccupiedByCurrentThread());
  }

  // Add more test methods for the other methods in the Monitor class

}