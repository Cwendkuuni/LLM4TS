import static org.junit.Assert.*;
import org.junit.Test;

public class HuntDisplayTest {

   @Test
   public void testAddPoint() {
       TrigPoint point = new TrigPoint(30, 60, 45, true);
       HuntDisplay display = new HuntDisplay();
       display.addPoint(point);
       // Test if the point is added to the display
       assertEquals(1, display.points.size());
   }

   @Test
   public void testAddLandMark() {
       Landmark landmark = new Landmark(40, 80, new ImageIcon("icon.png"));
       HuntDisplay display = new HuntDisplay();
       display.addLandMark(landmark);
       // Test if the landmark is added to the display
       assertEquals(1, display.landmarks.size());
   }

   @Test
   public void testPaintComponent() {
       HuntDisplay display = new HuntDisplay();
       Graphics g = null; // Mock Graphics object
       // Test if the paintComponent method runs without any exceptions
       assertDoesNotThrow(() -> display.paintComponent(g));
   }

   @Test
   public void testGetPreferredSize() {
       HuntDisplay display = new HuntDisplay();
       // Test if the preferred size is as expected
       assertEquals(new Dimension(200, 200), display.getPreferredSize());
   }

   @Test
   public void testGetMinimumSize() {
       HuntDisplay display = new HuntDisplay();
       // Test if the minimum size is as expected
       assertEquals(new Dimension(50, 50), display.getMinimumSize());
   }

   // Add more test cases for edge cases and error scenarios if needed

}