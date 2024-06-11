import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class FoxHuntFrameTest {
    
    private FoxHuntFrame frame;
    
    @Before
    public void setUp() {
        frame = new FoxHuntFrame();
    }
    
    @Test
    public void testAddPoint() {
        frame.theta.setText("45");
        frame.lat.setText("32.1234");
        frame.lon.setText("45.6789");
        frame.addPoint();
        
        assertEquals(1, frame.points.size());
        assertEquals(1, frame.display.points.size());
        assertEquals(0, frame.fox.size());
        assertEquals(0, frame.posModel.getRowCount());
        assertEquals(1, frame.display.landmarks.size());
    }
    
    @Test
    public void testAddPointWithExistingPoints() {
        frame.theta.setText("45");
        frame.lat.setText("32.1234");
        frame.lon.setText("45.6789");
        frame.addPoint();
        frame.theta.setText("60");
        frame.lat.setText("23.4567");
        frame.lon.setText("56.7890");
        frame.addPoint();
        
        assertEquals(2, frame.points.size());
        assertEquals(2, frame.display.points.size());
        assertEquals(1, frame.fox.size());
        assertEquals(1, frame.posModel.getRowCount());
        assertEquals(2, frame.display.landmarks.size());
    }
    
    @Test
    public void testAddPointWithInvalidAngleInput() {
        frame.theta.setText("invalid");
        frame.lat.setText("32.1234");
        frame.lon.setText("45.6789");
        frame.addPoint();
        
        assertEquals(0, frame.points.size());
        assertEquals(0, frame.display.points.size());
        assertEquals(0, frame.fox.size());
        assertEquals(0, frame.posModel.getRowCount());
        assertEquals(0, frame.display.landmarks.size());
    }
    
    @Test
    public void testAddPointWithInvalidLatitudeInput() {
        frame.theta.setText("45");
        frame.lat.setText("invalid");
        frame.lon.setText("45.6789");
        frame.addPoint();
        
        assertEquals(0, frame.points.size());
        assertEquals(0, frame.display.points.size());
        assertEquals(0, frame.fox.size());
        assertEquals(0, frame.posModel.getRowCount());
        assertEquals(0, frame.display.landmarks.size());
    }
    
    @Test
    public void testAddPointWithInvalidLongitudeInput() {
        frame.theta.setText("45");
        frame.lat.setText("32.1234");
        frame.lon.setText("invalid");
        frame.addPoint();
        
        assertEquals(0, frame.points.size());
        assertEquals(0, frame.display.points.size());
        assertEquals(0, frame.fox.size());
        assertEquals(0, frame.posModel.getRowCount());
        assertEquals(0, frame.display.landmarks.size());
    }
    
    @Test
    public void testAddPointWithMultipleInvalidInputs() {
        frame.theta.setText("invalid");
        frame.lat.setText("invalid");
        frame.lon.setText("invalid");
        frame.addPoint();
        
        assertEquals(0, frame.points.size());
        assertEquals(0, frame.display.points.size());
        assertEquals(0, frame.fox.size());
        assertEquals(0, frame.posModel.getRowCount());
        assertEquals(0, frame.display.landmarks.size());
    }
    
    @Test
    public void testFinalPrep() {
        frame.finalPrep();
        
        assertNotNull(frame.zoom);
        assertNotNull(frame.zoom.getActionListeners()[0]);
        assertNotNull(frame.panel3);
        assertNotNull(frame.panel);
    }
    
    @Test
    public void testTextPrep() {
        frame.textPrep();
        
        assertNotNull(frame.theta);
        assertNotNull(frame.lat);
        assertNotNull(frame.lon);
        assertNotNull(frame.add);
        assertNotNull(frame.panel1);
        assertNotNull(frame.panel1.getComponents());
    }
    
    @Test
    public void testTablePrep() {
        frame.tablePrep();
        
        assertNotNull(frame.posModel);
        assertNotNull(frame.pos);
        assertNotNull(frame.panel2);
        assertNotNull(frame.panel2.getComponent(0));
    }
    
    @Test
    public void testDisplayPrep() {
        frame.displayPrep();
        
        assertNotNull(frame.display);
        assertNotNull(frame.display.landmarks);
        assertNotNull(frame.display.points);
    }
    
    @Test
    public void testLoadLandmarksWithValidFile() {
        frame.loadLandmarks();
        
        assertEquals(2, frame.display.landmarks.size());
    }
    
    @Test
    public void testLoadLandmarksWithInvalidFile() {
        assertNull(frame.loadLandmarks());
    }
    
    @Test
    public void testErrorAbortOption() {
        assertEquals(1, frame.error("Can't Find the file"));
    }
    
    @Test
    public void testErrorContinueOption() {
        assertEquals(0, frame.error("Can't Find the file"));
    }
    
}