package Joshua.FoxHunt;

import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

import static org.junit.Assert.assertEquals;

public class HuntDisplayTest {

    @Test
    public void testAddPoint() {
        HuntDisplay huntDisplay = new HuntDisplay();
        TrigPoint point = new TrigPoint(45.0, 60.0, 90.0, true);
        huntDisplay.addPoint(point);

        Vector<TrigPoint> expectedPoints = new Vector<>();
        expectedPoints.add(new TrigPoint(0.5, 0.5, 90.0, true));

        assertEquals(expectedPoints, huntDisplay.points);
    }

    @Test
    public void testAddLandMark() {
        HuntDisplay huntDisplay = new HuntDisplay();
        ImageIcon icon = new ImageIcon();
        Landmark landmark = new Landmark(45.0, 60.0, icon);
        huntDisplay.addLandMark(landmark);

        Vector<Landmark> expectedLandmarks = new Vector<>();
        expectedLandmarks.add(new Landmark(0.5, 0.5, icon));

        assertEquals(expectedLandmarks, huntDisplay.landmarks);
    }
    
    // Additional test cases can be written to further test the methods in the HuntDisplay class.

}