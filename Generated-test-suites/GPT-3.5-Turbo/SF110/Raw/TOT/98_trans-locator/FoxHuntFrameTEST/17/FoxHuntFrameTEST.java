import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FoxHuntFrameTest {
    private FoxHuntFrame frame;
    
    @Before
    public void setUp() {
        frame = new FoxHuntFrame();
    }
    
    @Test
    public void testAddPoint_typical() {
        frame.theta.setText("45");
        frame.lat.setText("37.7749");
        frame.lon.setText("-122.4194");
        frame.addPoint();
        
        // Check if the point is added to the 'points' vector
        assertEquals(1, frame.points.size());
        TrigPoint point = frame.points.elementAt(0);
        assertEquals(45, point.getAngle());
        assertEquals(37.7749, point.getLat(), 0.001);
        assertEquals(-122.4194, point.getLon(), 0.001);
        
        // Check if the point is added to the 'display' object
        assertTrue(frame.display.getPoints().contains(point));
        
        // Check if the 'fox' vector and the 'posModel' are updated correctly
        assertEquals(0, frame.fox.size());
        assertEquals(0, frame.posModel.getRowCount());
    }
    
    @Test
    public void testAddPoint_multiplePoints() {
        // Add a point
        frame.theta.setText("45");
        frame.lat.setText("37.7749");
        frame.lon.setText("-122.4194");
        frame.addPoint();
        
        // Add another point
        frame.theta.setText("60");
        frame.lat.setText("34.0522");
        frame.lon.setText("-118.2437");
        frame.addPoint();
        
        // Check if the points are added to the 'points' vector
        assertEquals(2, frame.points.size());
        TrigPoint point1 = frame.points.elementAt(0);
        assertEquals(45, point1.getAngle());
        assertEquals(37.7749, point1.getLat(), 0.001);
        assertEquals(-122.4194, point1.getLon(), 0.001);
        TrigPoint point2 = frame.points.elementAt(1);
        assertEquals(60, point2.getAngle());
        assertEquals(34.0522, point2.getLat(), 0.001);
        assertEquals(-118.2437, point2.getLon(), 0.001);
        
        // Check if the points are added to the 'display' object
        assertTrue(frame.display.getPoints().contains(point1));
        assertTrue(frame.display.getPoints().contains(point2));
        
        // Check if the 'fox' vector and the 'posModel' are updated correctly
        assertEquals(1, frame.fox.size());
        assertEquals(1, frame.posModel.getRowCount());
        TrigPoint foxPoint = frame.fox.elementAt(0);
        assertEquals(point2.getX(), foxPoint.getX(), 0.001);
        assertEquals(point2.getY(), foxPoint.getY(), 0.001);
    }
    
    @Test
    public void testAddPoint_edge() {
        frame.theta.setText("0");
        frame.lat.setText("-90");
        frame.lon.setText("180");
        frame.addPoint();
        
        // Check if the point is added to the 'points' vector
        assertEquals(1, frame.points.size());
        TrigPoint point = frame.points.elementAt(0);
        assertEquals(0, point.getAngle());
        assertEquals(-90, point.getLat(), 0.001);
        assertEquals(180, point.getLon(), 0.001);
        
        // Check if the point is added to the 'display' object
        assertTrue(frame.display.getPoints().contains(point));
        
        // Check if the 'fox' vector and the 'posModel' are updated correctly
        assertEquals(0, frame.fox.size());
        assertEquals(0, frame.posModel.getRowCount());
    }
    
    @Test
    public void testAddPoint_error() {
        frame.theta.setText("45");
        frame.lat.setText("37.7749");
        frame.lon.setText("INVALID");
        frame.addPoint();
        
        // Check if the point is not added to the 'points' vector
        assertEquals(0, frame.points.size());
        
        // Check if the point is not added to the 'display' object
        assertTrue(frame.display.getPoints().isEmpty());
        
        // Check if the 'fox' vector and the 'posModel' are not updated
        assertEquals(0, frame.fox.size());
        assertEquals(0, frame.posModel.getRowCount());
    }
    
    @Test
    public void testLoadLandmarks_typical() {
        // Manually load landmarks
        frame.loadLandmarks();
        
        // Check if the landmarks are loaded correctly and added to the 'display' object
        assertEquals(2, frame.display.getLandmarks().size());
        Landmark landmark1 = frame.display.getLandmarks().get(0);
        assertEquals(37.7749, landmark1.getLat(), 0.001);
        assertEquals(-122.4194, landmark1.getLon(), 0.001);
        Landmark landmark2 = frame.display.getLandmarks().get(1);
        assertEquals(34.0522, landmark2.getLat(), 0.001);
        assertEquals(-118.2437, landmark2.getLon(), 0.001);
    }
    
    @Test
    public void testLoadLandmarks_error() {
        // Update the path of the landmarks file to an invalid path
        frame.loadLandmarks();
        
        // Check if the error dialog is displayed
        // TODO: Implement a way to test if the error dialog is displayed
    }
}