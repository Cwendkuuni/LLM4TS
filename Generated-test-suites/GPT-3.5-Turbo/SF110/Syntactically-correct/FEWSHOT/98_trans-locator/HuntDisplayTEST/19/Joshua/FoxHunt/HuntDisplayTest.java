import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.TrigPoint;
import Joshua.FoxHunt.Landmark;

import java.awt.*;
import java.util.Vector;
import javax.swing.*;

import org.junit.Before;
import org.junit.Test;

public class HuntDisplayTest {

    private HuntDisplay huntDisplay;

    @Before
    public void setup() {
        huntDisplay = new HuntDisplay();
    }

    @Test
    public void testAddPoint() {
        assertEquals(0, huntDisplay.points.size());
        
        TrigPoint point = new TrigPoint(50.0, 50.0, 45.0, true);
        huntDisplay.addPoint(point);
        
        assertEquals(1, huntDisplay.points.size());
        assertEquals(point, huntDisplay.points.elementAt(0));
    }

    @Test
    public void testAddLandmark() {
        assertEquals(0, huntDisplay.landmarks.size());
        
        Landmark landmark = new Landmark(50.0, 50.0, new ImageIcon());
        huntDisplay.addLandMark(landmark);
        
        assertEquals(1, huntDisplay.landmarks.size());
        assertEquals(landmark, huntDisplay.landmarks.elementAt(0));
    }

    @Test
    public void testPaintComponent() {
        Graphics g = mock(Graphics.class);
        huntDisplay.paintComponent(g);
        
        verify(g).setColor(Color.BLACK);
        verify(g).setColor(Color.GREEN);
        verify(g, times(huntDisplay.points.size())).fillOval(anyInt(), anyInt(), anyInt(), anyInt());
        verify(g, times(huntDisplay.landmarks.size())).drawImage(any(Image.class), anyInt(), anyInt(), anyInt(), anyInt(), any(Color.class), any());

        // Test if points are correctly converted to TrigPoint array
        huntDisplay.points = new Vector();
        huntDisplay.points.add(new TrigPoint(50.0, 50.0, 45.0, true));
        TrigPoint[] trigpoints = new TrigPoint[huntDisplay.points.size()];
        trigpoints[0] = (TrigPoint) huntDisplay.points.elementAt(0);
        g = mock(Graphics.class);
        huntDisplay.paintComponent(g);
        verify(g, times(trigpoints.length)).fillOval(anyInt(), anyInt(), anyInt(), anyInt());
    }

    @Test
    public void testGetPreferredSize() {
        assertEquals(new Dimension(200, 200), huntDisplay.getPreferredSize());
    }

    @Test
    public void testGetMinimumSize() {
        assertEquals(new Dimension(50, 50), huntDisplay.getMinimumSize());
    }

}