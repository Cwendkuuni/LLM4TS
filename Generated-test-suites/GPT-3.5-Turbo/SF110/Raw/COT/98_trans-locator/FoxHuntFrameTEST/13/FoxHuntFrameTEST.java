import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.swing.table.*;
import java.text.*;

public class FoxHuntFrameTest {

    private FoxHuntFrame foxHuntFrame;

    @Before
    public void setUp() {
        foxHuntFrame = new FoxHuntFrame();
    }

    @Test
    public void testAddPoint() {
        foxHuntFrame.theta.setText("45");
        foxHuntFrame.lat.setText("37.7749");
        foxHuntFrame.lon.setText("-122.4194");

        foxHuntFrame.addPoint();

        Vector points = foxHuntFrame.points;
        TrigPoint point = (TrigPoint) points.elementAt(0);

        assertEquals(1, points.size());
        assertEquals(45, point.getAngle());
        assertEquals(37.7749, point.getY(), 0.01);
        assertEquals(-122.4194, point.getX(), 0.01);
    }

    @Test
    public void testAddPointWithMultiplePoints() {
        foxHuntFrame.addPoint();

        foxHuntFrame.theta.setText("45");
        foxHuntFrame.lat.setText("37.7749");
        foxHuntFrame.lon.setText("-122.4194");

        foxHuntFrame.addPoint();

        Vector points = foxHuntFrame.points;
        TrigPoint point1 = (TrigPoint) points.elementAt(0);
        TrigPoint point2 = (TrigPoint) points.elementAt(1);

        assertEquals(2, points.size());
        assertEquals(45, point1.getAngle());
        assertEquals(37.7749, point1.getY(), 0.01);
        assertEquals(-122.4194, point1.getX(), 0.01);
        assertEquals(45, point2.getAngle());
        assertEquals(37.7749, point2.getY(), 0.01);
        assertEquals(-122.4194, point2.getX(), 0.01);

        Vector foxes = foxHuntFrame.fox;
        TrigPoint fox = (TrigPoint) foxes.elementAt(0);

        assertEquals(1, foxes.size());
        assertEquals(37.7749, fox.getY(), 0.01);
        assertEquals(-122.4194, fox.getX(), 0.01);
    }

    @Test
    public void testActionPerformed() {
        ActionEvent event = new ActionEvent(foxHuntFrame.add, ActionEvent.ACTION_PERFORMED, "Add");

        foxHuntFrame.actionPerformed(event);

        Vector points = foxHuntFrame.points;
        TrigPoint point = (TrigPoint) points.elementAt(0);

        assertEquals(1, points.size());
        assertEquals(0, point.getAngle());
        assertEquals(0.0, point.getY(), 0.01);
        assertEquals(0.0, point.getX(), 0.01);
    }

    @Test
    public void testLoadLandmarks() {
        foxHuntFrame.loadLandmarks();

        HuntDisplay display = foxHuntFrame.display;
        Landmark landmark = display.getLandmarks().elementAt(0);

        assertEquals(1, display.getLandmarks().size());
        assertEquals(37.7749, landmark.getLat(), 0.01);
        assertEquals(-122.4194, landmark.getLon(), 0.01);
    }

    @Test
    public void testError() {
        String text = "Error message";
        Object[] options = {"Abort", "Continue"};

        int ans = JOptionPane.showOptionDialog(foxHuntFrame, text, "Error", 0, 0, null, options, options[0]);
        if (ans == 0 | ans == -1) {
            System.exit(1);
        }

        assertEquals(0, ans);
    }
}

