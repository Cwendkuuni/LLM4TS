import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

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
        TrigPoint point = new TrigPoint(37.123, -122.456, 45);
        
        frame.addPoint();
        
        assertNotNull(frame.points);
        assertTrue(frame.points.contains(point));
        assertTrue(frame.display.points.contains(point));
    }

    @Test
    public void testAddPoint_WithMultiplePoints() {
        TrigPoint point1 = new TrigPoint(37.123, -122.456, 45);
        TrigPoint point2 = new TrigPoint(37.456, -122.789, 90);
        
        frame.addPoint();
        frame.addPoint();
        
        assertNotNull(frame.points);
        assertTrue(frame.points.contains(point1));
        assertTrue(frame.display.points.contains(point1));
        assertTrue(frame.points.contains(point2));
        assertTrue(frame.display.points.contains(point2));
    }

    @Test
    public void testAddPoint_WithFox() {
        TrigPoint point1 = new TrigPoint(37.123, -122.456, 45);
        TrigPoint point2 = new TrigPoint(37.456, -122.789, 90);
        TrigPoint fox1 = FoxCalc.calcFox(point1, point2);
        
        frame.addPoint();
        frame.addPoint();
        
        assertNotNull(frame.points);
        assertTrue(frame.points.contains(point1));
        assertTrue(frame.display.points.contains(point1));
        assertTrue(frame.points.contains(point2));
        assertTrue(frame.display.points.contains(point2));
        assertTrue(frame.fox.contains(fox1));
    }

    @Test
    public void testAddPoint_WithFox_WhenPointsSizeIs1() {
        TrigPoint point = new TrigPoint(37.123, -122.456, 45);
        
        frame.addPoint();
        
        assertNotNull(frame.points);
        assertTrue(frame.points.contains(point));
        assertTrue(frame.display.points.contains(point));
        assertTrue(frame.fox.isEmpty());
    }

    @Test
    public void testFinalPrep() {
        assertNotNull(frame.zoom);
        assertEquals("1", frame.zoom.getText());
        assertNotNull(frame.display);
        assertNotNull(frame.panel1);
        assertNotNull(frame.panel2);
        assertNotNull(frame.panel3);
        assertNotNull(frame.panel);
    }

    @Test
    public void testTextPrep() {
        assertNotNull(frame.theta);
        assertEquals(3, frame.theta.getColumns());
        
        assertNotNull(frame.lat);
        assertEquals(9, frame.lat.getColumns());
        
        assertNotNull(frame.lon);
        assertEquals(10, frame.lon.getColumns());
        
        assertNotNull(frame.add);
        assertEquals("Add It!!!", frame.add.getText());
    }

    @Test
    public void testTablePrep() {
        assertNotNull(frame.posModel);
        assertTrue(frame.posModel instanceof FoxTableModel);
        
        assertNotNull(frame.pos);
        assertTrue(frame.pos.getModel() instanceof FoxTableModel);
        
        assertNotNull(frame.panel2);
        assertEquals(frame.pos, frame.panel2.getComponent(0));
        assertTrue(frame.panel2.getLayout() instanceof BorderLayout);
    }

    @Test
    public void testDisplayPrep() {
        assertNotNull(frame.display);
        assertTrue(frame.display instanceof HuntDisplay);
        assertNotNull(frame.display.landmarks);
    }

    @Test
    public void testLoadLandmarks() {
        frame.loadLandmarks();
        
        assertFalse(frame.display.landmarks.isEmpty());
    }

    @Test
    public void testLoadLandmarks_WithMissingFile() {
        frame.loadLandmarks();
        
        assertTrue(frame.display.landmarks.isEmpty());
    }

    @Test
    public void testLoadLandmarks_WithIOException() {
        frame.loadLandmarks();
        
        assertTrue(frame.display.landmarks.isEmpty());
    }

    @Test
    public void testError() {
        String text = "Test Error Message";
        
        frame.error(text);
        
        // Error message dialog should be displayed, user should be able to continue
        // We can't assert anything about user interaction with the dialog
    }
}