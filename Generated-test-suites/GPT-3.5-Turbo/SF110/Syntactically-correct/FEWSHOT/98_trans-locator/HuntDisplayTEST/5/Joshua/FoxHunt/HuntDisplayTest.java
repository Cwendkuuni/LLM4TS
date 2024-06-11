import static org.junit.Assert.*;
import org.junit.Test;

public class HuntDisplayTest {
  
  @Test
  public void testAddPoint() {
    HuntDisplay hunt = new HuntDisplay();
    TrigPoint point = new TrigPoint(1, 1, 0.2, true);
    
    hunt.addPoint(point);
    
    //Assert that the point is added to the points vector
    assertEquals(1, hunt.points.size());
    
    //Add more test cases for different points and verify if they are added to the points vector
  }
  
  @Test
  public void testAddLandMark() {
    HuntDisplay hunt = new HuntDisplay();
    Landmark landmark = new Landmark(1, 1, new ImageIcon());
    
    hunt.addLandMark(landmark);
    
    //Assert that the landmark is added to the landmarks vector
    assertEquals(1, hunt.landmarks.size());
    
    //Add more test cases for different landmarks and verify if they are added to the landmarks vector
  }
  
  @Test
  public void testPaintComponent() {
    HuntDisplay hunt = new HuntDisplay();
    TrigPoint point1 = new TrigPoint(0.5, 0.5, 0.2, true);
    TrigPoint point2 = new TrigPoint(0.3, 0.3, 0.4, false);
    hunt.addPoint(point1);
    hunt.addPoint(point2);
    
    Landmark landmark = new Landmark(0.2, 0.2, new ImageIcon());
    hunt.addLandMark(landmark);
    
    //Create a BufferedImage and use it to create Graphics object for testing
    BufferedImage image = new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB);
    Graphics g = image.getGraphics();
    
    hunt.paintComponent(g);
    
    //Perform assertions to verify that the points and landmarks are painted correctly on the graphics object
  }
}