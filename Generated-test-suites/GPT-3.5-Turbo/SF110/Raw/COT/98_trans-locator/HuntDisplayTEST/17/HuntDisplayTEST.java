
import org.junit.Test;
import static org.junit.Assert.*;

public class HuntDisplayTest {

    @Test
    public void testAddPoint() {
        TrigPoint point = new TrigPoint(45, 60, 90, true);
        HuntDisplay display = new HuntDisplay();
        
        display.addPoint(point);
        Vector expectedPoints = new Vector();
        expectedPoints.add(new TrigPoint(point.getY() / 90.0, point.getX() / 180.0, point.getTheta(), point.isFox()));
        Vector actualPoints = display.points;
        
        assertEquals(expectedPoints, actualPoints);
    }
    
    @Test
    public void testAddLandMark() {
        Landmark landmark = new Landmark(30, 45, new ImageIcon());
        HuntDisplay display = new HuntDisplay();
        
        display.addLandMark(landmark);
        Vector expectedLandmarks = new Vector();
        expectedLandmarks.add(new Landmark(landmark.getLat() / 90.0, landmark.getLon() / 180.0, landmark.getIcon()));
        Vector actualLandmarks = display.landmarks;
        
        assertEquals(expectedLandmarks, actualLandmarks);
    }
}
