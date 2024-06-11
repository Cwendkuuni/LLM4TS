package Joshua.FoxHunt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class FoxHuntFrameTest {
    
    private FoxHuntFrame frame;
    
    @Before
    public void setup() {
        frame = new FoxHuntFrame();
    }
    
    @Test
    public void testAddPoint() {
        frame.theta.setText("45");
        frame.lat.setText("37.7749");
        frame.lon.setText("-122.4194");
        frame.addPoint();
        
        assertEquals(1, frame.points.size());
        assertEquals(1, frame.display.points.size());
        assertEquals(0, frame.fox.size());
        assertEquals(1, frame.posModel.getRowCount());
        assertEquals(37.7749, frame.display.points.get(0).getLatitude(), 0.0001);
        assertEquals(-122.4194, frame.display.points.get(0).getLongitude(), 0.0001);
        assertEquals("37.7749", frame.posModel.getValueAt(0, 0));
        assertEquals("-122.4194", frame.posModel.getValueAt(0, 1));
    }
    
    @Test
    public void testAddPoint_multiplePoints() {
        TrigPoint point1 = mock(TrigPoint.class);
        TrigPoint point2 = mock(TrigPoint.class);
        
        frame.points.add(point1);
        frame.display.points.add(point1);
        
        frame.theta.setText("45");
        frame.lat.setText("37.7749");
        frame.lon.setText("-122.4194");
        when(point1.getAngle()).thenReturn(30);
        when(point1.getLatitude()).thenReturn(37.7749);
        when(point1.getLongitude()).thenReturn(-122.4194);
        when(point2.getAngle()).thenReturn(60);
        when(point2.getLatitude()).thenReturn(36.7783);
        when(point2.getLongitude()).thenReturn(-119.4179);
        when(FoxCalc.calcFox(point1, point2)).thenReturn(point2);
        
        frame.addPoint();
        
        assertEquals(2, frame.points.size());
        assertEquals(2, frame.display.points.size());
        assertEquals(1, frame.fox.size());
        assertEquals(2, frame.posModel.getRowCount());
        assertEquals(37.7749, frame.display.points.get(0).getLatitude(), 0.0001);
        assertEquals(-122.4194, frame.display.points.get(0).getLongitude(), 0.0001);
        assertEquals(36.7783, frame.display.points.get(1).getLatitude(), 0.0001);
        assertEquals(-119.4179, frame.display.points.get(1).getLongitude(), 0.0001);
        assertEquals("37.7749", frame.posModel.getValueAt(0, 0));
        assertEquals("-122.4194", frame.posModel.getValueAt(0, 1));
        assertEquals("36.7783", frame.posModel.getValueAt(1, 0));
        assertEquals("-119.4179", frame.posModel.getValueAt(1, 1));
    }
    
    @Test
    public void testAddPoint_invalidInput() {
        frame.theta.setText("invalid");
        frame.lat.setText("invalid");
        frame.lon.setText("invalid");
        frame.addPoint();
        
        assertEquals(0, frame.points.size());
        assertEquals(0, frame.display.points.size());
        assertEquals(0, frame.fox.size());
        assertEquals(0, frame.posModel.getRowCount());
    }
    
    @Test
    public void testAddPoint_noPointsAdded() {
        frame.addPoint();
        
        assertEquals(0, frame.points.size());
        assertEquals(0, frame.display.points.size());
        assertEquals(0, frame.fox.size());
        assertEquals(0, frame.posModel.getRowCount());
        
        verifyZeroInteractions(FoxCalc.class);
    }
    
    @Test
    public void testActionPerformed() {
        frame.theta.setText("45");
        frame.lat.setText("37.7749");
        frame.lon.setText("-122.4194");
        frame.add.actionPerformed(null);
        
        assertEquals(1, frame.points.size());
        assertEquals(1, frame.display.points.size());
        assertEquals(0, frame.fox.size());
        assertEquals(1, frame.posModel.getRowCount());
        assertEquals(37.7749, frame.display.points.get(0).getLatitude(), 0.0001);
        assertEquals(-122.4194, frame.display.points.get(0).getLongitude(), 0.0001);
        assertEquals("37.7749", frame.posModel.getValueAt(0, 0));
        assertEquals("-122.4194", frame.posModel.getValueAt(0, 1));
    }
    
    @Test
    public void testFinalPrep() {
        // TODO: Test case for the finalPrep() method
    }
    
    @Test
    public void testTextPrep() {
        // TODO: Test case for the textPrep() method
    }
    
    @Test
    public void testTablePrep() {
        // TODO: Test case for the tablePrep() method
    }
    
    @Test
    public void testDisplayPrep() {
        // TODO: Test case for the displayPrep() method
    }
    
    @Test
    public void testLoadLandmarks() {
        // TODO: Test case for the loadLandmarks() method
    }
    
    @Test
    public void testError() {
        // TODO: Test case for the error() method
    }
}