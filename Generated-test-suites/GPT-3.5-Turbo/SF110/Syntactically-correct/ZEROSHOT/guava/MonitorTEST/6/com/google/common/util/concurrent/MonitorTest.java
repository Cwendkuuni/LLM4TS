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
  public void testEnterWithTime() {
    assertTrue(monitor.enter(1, TimeUnit.SECONDS));
    assertTrue(monitor.isOccupiedByCurrentThread());
  }
  
  @Test
  public void testEnterInterruptiblyWithTime() throws InterruptedException {
    assertTrue(monitor.enterInterruptibly(1, TimeUnit.SECONDS));
    assertTrue(monitor.isOccupiedByCurrentThread());
  }
  
  @Test
  public void testTryEnter() {
    assertTrue(monitor.tryEnter());
    assertTrue(monitor.isOccupiedByCurrentThread());
  }
  
  // Other test methods for the rest of the Monitor class methods
  
}