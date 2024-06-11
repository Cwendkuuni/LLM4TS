import org.junit.*;
import java.util.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.swing.table.*;
import java.text.*;

public class FoxHuntFrameTest {
    private FoxHuntFrame frame;

    @Before
    public void setup() {
        frame = new FoxHuntFrame();
    }

    @After
    public void teardown() {
        frame.dispose();
    }

    @Test
    public void testAddPoint() {
        frame.theta.setText("30");
        frame.lat.setText("1.234");
        frame.lon.setText("5.678");

        frame.addPoint();

        assertEquals(1, frame.points.size());
        assertEquals(1, frame.fox.size());
        assertEquals(1, frame.posModel.getRowCount());
        assertEquals("1.234", frame.posModel.getValueAt(0, 0));
        assertEquals("5.678", frame.posModel.getValueAt(0, 1));
    }

    @Test
    public void testAddPointMultiple() {
        frame.theta.setText("30");
        frame.lat.setText("1.234");
        frame.lon.setText("5.678");

        frame.addPoint();

        frame.theta.setText("45");
        frame.lat.setText("2.345");
        frame.lon.setText("6.789");

        frame.addPoint();

        assertEquals(2, frame.points.size());
        assertEquals(2, frame.fox.size());
        assertEquals(2, frame.posModel.getRowCount());
        assertEquals("2.345", frame.posModel.getValueAt(1, 0));
        assertEquals("6.789", frame.posModel.getValueAt(1, 1));
    }

    @Test
    public void testZoomValueChanged() {
        frame.zoom.setText("2");

        // simulate action event
        ActionEvent event = new ActionEvent(frame.zoom, ActionEvent.ACTION_PERFORMED, "");
        frame.actionPerformed(event);

        // assert zoom value changed

    }
}