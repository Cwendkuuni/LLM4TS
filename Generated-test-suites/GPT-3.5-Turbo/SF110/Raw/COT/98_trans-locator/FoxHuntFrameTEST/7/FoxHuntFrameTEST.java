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

    FoxHuntFrame frame;

    @Before
    public void setUp() {
        frame = new FoxHuntFrame();
    }

    @Test
    public void testAddPoint() {
        // Test adding a point
        frame.theta.setText("0");
        frame.lat.setText("0");
        frame.lon.setText("0");
        frame.addPoint();
        assertEquals(1, frame.points.size());
    }

    @Test
    public void testActionPerformed() {
        // Test actionPerformed method
        ActionEvent event = new ActionEvent(new JButton(), 1, null);
        frame.actionPerformed(event);
        assertEquals(1, frame.points.size());
    }

    @Test
    public void testLoadLandmarks() {
        // Test loading landmarks from file
        frame.loadLandmarks();
        assertEquals(0, frame.display.getLandmarks().size());
    }

    @Test
    public void testError() {
        // Test error handling
        frame.error("Test Error");
        assertEquals(JFrame.EXIT_ON_CLOSE, frame.getDefaultCloseOperation());
    }

    @After
    public void tearDown() {
        frame.dispose();
    }
}