import org.junit.Test;
import static org.junit.Assert.*;

public class TimerTest {

   @Test
   public void testIsCountingTrue() {
      Timer timer = new Timer(5000);
      timer.start();
      assertTrue(timer.isCounting());
   }

   @Test
   public void testIsCountingFalse() {
      Timer timer = new Timer(5000);
      assertFalse(timer.isCounting());
   }

   @Test
   public void testStart() {
      Timer timer = new Timer(5000);
      timer.start();
      assertTrue(timer.isCounting());
   }

   @Test
   public void testNegativeDuration() {
      try {
         Timer timer = new Timer(-5000);
         fail("Expected IllegalArgumentException");
      } catch (IllegalArgumentException e) {
         assertEquals("Duration must be greater than or equal to 0", e.getMessage());
      }
   }

   @Test
   public void testZeroDuration() {
      Timer timer = new Timer(0);
      assertFalse(timer.isCounting());
   }

}