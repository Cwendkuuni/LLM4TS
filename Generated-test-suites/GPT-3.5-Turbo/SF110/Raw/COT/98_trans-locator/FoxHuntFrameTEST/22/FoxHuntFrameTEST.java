import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Vector;
import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import java.io.*;

public class FoxHuntFrameTest {
    
    private FoxHuntFrame frame;
    
    @Before
    public void setUp() {
        frame = new FoxHuntFrame();
    }
    
    @Test
    public void testAddPoint() {
        frame.theta.setText("90");
        frame.lat.setText("37.7749");
        frame.lon.setText("-122.4194");
        frame.addPoint();
        
        Vector<TrigPoint> expectedPoints = new Vector<>();
        expectedPoints.add(new TrigPoint(37.7749, -122.4194, 90));
        
        Vector<TrigPoint> actualPoints = frame.points;
        
        assertEquals(expectedPoints, actualPoints);
    }
    
    @Test
    public void testLoadLandmarks() {
        frame.loadLandmarks();
        
        // Check if landmarks are correctly loaded
    }
}