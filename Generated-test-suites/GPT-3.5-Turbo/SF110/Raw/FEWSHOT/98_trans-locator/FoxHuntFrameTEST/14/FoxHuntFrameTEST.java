package Joshua.FoxHunt;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static org.junit.Assert.*;

public class FoxHuntFrameTest {

    private FoxHuntFrame foxHuntFrame;

    @Before
    public void setUp() {
        foxHuntFrame = new FoxHuntFrame();
    }

    @Test
    public void testAddPoint() {
        foxHuntFrame.theta.setText("45");
        foxHuntFrame.lat.setText("37.123");
        foxHuntFrame.lon.setText("-122.456");

        foxHuntFrame.addPoint();

        assertEquals(1, foxHuntFrame.points.size());
        assertEquals(1, foxHuntFrame.display.points.size());
        assertEquals(0, foxHuntFrame.fox.size());
        assertEquals(0, foxHuntFrame.posModel.getRowCount());

        TrigPoint point = (TrigPoint) foxHuntFrame.points.elementAt(0);

        assertEquals(45, point.angle);
        assertEquals(37.123, point.LAT, 0);
        assertEquals(-122.456, point.LON, 0);
    }

    @Test
    public void testActionPerformed() {
        foxHuntFrame.theta.setText("45");
        foxHuntFrame.lat.setText("37.123");
        foxHuntFrame.lon.setText("-122.456");

        foxHuntFrame.actionPerformed(new ActionEvent(new JButton(), 1, ""));

        assertEquals(1, foxHuntFrame.points.size());
        assertEquals(1, foxHuntFrame.display.points.size());
        assertEquals(0, foxHuntFrame.fox.size());
        assertEquals(0, foxHuntFrame.posModel.getRowCount());

        TrigPoint point = (TrigPoint) foxHuntFrame.points.elementAt(0);

        assertEquals(45, point.angle);
        assertEquals(37.123, point.LAT, 0);
        assertEquals(-122.456, point.LON, 0);
    }

    @Test
    public void testTablePrep() {
        foxHuntFrame.tablePrep();

        assertNotNull(foxHuntFrame.posModel);
        assertNotNull(foxHuntFrame.pos);
        assertNotNull(foxHuntFrame.panel2);

        JScrollPane scroll = (JScrollPane) foxHuntFrame.panel2.getComponent(0);
        assertEquals(foxHuntFrame.pos, scroll.getViewport().getView());
        assertEquals(500, foxHuntFrame.pos.getPreferredScrollableViewportSize().getWidth(), 0);
        assertEquals(70, foxHuntFrame.pos.getPreferredScrollableViewportSize().getHeight(), 0);
    }

    @Test
    public void testDisplayPrep() {
        foxHuntFrame.displayPrep();

        assertNotNull(foxHuntFrame.display);
        assertEquals(0, foxHuntFrame.display.landmarks.size());
        assertEquals(0, foxHuntFrame.display.points.size());
    }

    @Test
    public void testLoadLandmarks() {
        foxHuntFrame.loadLandmarks();

        assertEquals(0, foxHuntFrame.display.landmarks.size());
    }

    @Test
    public void testError() {
        foxHuntFrame.error("Can't Find the file");

        assertEquals(0, foxHuntFrame.display.landmarks.size());
        assertEquals(0, foxHuntFrame.display.points.size());
    }

    @Test
    public void testFoxTableModel() {
        FoxHuntFrame.FoxTableModel model = foxHuntFrame.new FoxTableModel();

        assertNotNull(model.colNames);
        assertEquals(2, model.colNames.length);
    }

    @Test
    public void testGetColumnCount() {
        FoxHuntFrame.FoxTableModel model = foxHuntFrame.new FoxTableModel();

        assertEquals(2, model.getColumnCount());
    }

    @Test
    public void testGetRowCount() {
        FoxHuntFrame.FoxTableModel model = foxHuntFrame.new FoxTableModel();

        assertEquals(0, model.getRowCount());

        foxHuntFrame.fox.add(new TrigPoint(37.123, -122.456, 45));
        assertEquals(1, model.getRowCount());
    }

    @Test
    public void testGetColumnName() {
        FoxHuntFrame.FoxTableModel model = foxHuntFrame.new FoxTableModel();

        assertEquals("Lattitude", model.getColumnName(0));
        assertEquals("Longitude", model.getColumnName(1));
    }

    @Test
    public void testGetValueAt() {
        FoxHuntFrame.FoxTableModel model = foxHuntFrame.new FoxTableModel();

        foxHuntFrame.fox.add(new TrigPoint(37.123, -122.456, 45));

        assertEquals("37.123", model.getValueAt(0, 0));
        assertEquals("-122.456", model.getValueAt(0, 1));
    }

    @Test
    public void testGetColumnClass() {
        FoxHuntFrame.FoxTableModel model = foxHuntFrame.new FoxTableModel();

        foxHuntFrame.fox.add(new TrigPoint(37.123, -122.456, 45));

        assertEquals(String.class, model.getColumnClass(0));
        assertEquals(String.class, model.getColumnClass(1));
    }

    @Test
    public void testIsCellEditable() {
        FoxHuntFrame.FoxTableModel model = foxHuntFrame.new FoxTableModel();

        assertFalse(model.isCellEditable(0, 0));
        assertFalse(model.isCellEditable(0, 1));
    }
}