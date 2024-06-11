import org.junit.jupiter.api.Assertions;
import org.junit.Test;

public class HuntDisplayTest {

    @Test
    public void testAddPoint() {
        TrigPoint point = new TrigPoint(90, 180, 0, false);
        HuntDisplay display = new HuntDisplay();
        display.addPoint(point);
        // Assert that the point is added to the list
        Assertions.assertEquals(1, display.points.size());
        // Assert that repaint is called
        // (Unfortunately, Swing does not provide a way to directly test this)
    }

    @Test
    public void testAddLandmark() {
        Landmark landmark = new Landmark(90, 180, new ImageIcon());
        HuntDisplay display = new HuntDisplay();
        display.addLandMark(landmark);
        // Assert that the landmark is added to the list
        Assertions.assertEquals(1, display.landmarks.size());
        // Assert that repaint is called
        // (Unfortunately, Swing does not provide a way to directly test this)
    }

    @Test
    public void testPaintComponent() {
        HuntDisplay display = new HuntDisplay();
        Graphics g = new MockGraphics();
        // Invoke the paintComponent method
        display.paintComponent(g);
        // Assert that the method completes without any error
    }
    
    private class MockGraphics extends Graphics {
        // Mock implementation of the abstract Graphics class
    }
}