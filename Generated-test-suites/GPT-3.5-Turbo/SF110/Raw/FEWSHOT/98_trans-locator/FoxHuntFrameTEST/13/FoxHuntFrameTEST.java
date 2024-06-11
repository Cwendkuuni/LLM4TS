package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class FoxHuntFrameTest {
    @Test
    public void testActionPerformed() {
        // Test Case 1
        FoxHuntFrame foxHuntFrame = new FoxHuntFrame();
        foxHuntFrame.theta.setText("45");
        foxHuntFrame.lat.setText("23.456");
        foxHuntFrame.lon.setText("34.567");
        foxHuntFrame.addPoint();
        assertEquals(1, foxHuntFrame.points.size());
        assertEquals(1, foxHuntFrame.display.points.size());
        assertEquals(0, foxHuntFrame.fox.size());

        // Test Case 2
        foxHuntFrame.theta.setText("90");
        foxHuntFrame.lat.setText("12.345");
        foxHuntFrame.lon.setText("45.678");
        foxHuntFrame.addPoint();
        assertEquals(2, foxHuntFrame.points.size());
        assertEquals(2, foxHuntFrame.display.points.size());
        assertEquals(1, foxHuntFrame.fox.size());
    }

    @Test
    public void testAddPoint() {
        // Test Case 1
        FoxHuntFrame foxHuntFrame = new FoxHuntFrame();
        foxHuntFrame.theta.setText("45");
        foxHuntFrame.lat.setText("23.456");
        foxHuntFrame.lon.setText("34.567");
        foxHuntFrame.addPoint();
        assertEquals(1, foxHuntFrame.points.size());
        assertEquals(1, foxHuntFrame.display.points.size());
        assertEquals(0, foxHuntFrame.fox.size());

        // Test Case 2
        foxHuntFrame.theta.setText("90");
        foxHuntFrame.lat.setText("12.345");
        foxHuntFrame.lon.setText("45.678");
        foxHuntFrame.addPoint();
        assertEquals(2, foxHuntFrame.points.size());
        assertEquals(2, foxHuntFrame.display.points.size());
        assertEquals(1, foxHuntFrame.fox.size());
    }

    @Test
    public void testFinalPrep() {
        // Test Case 1
        FoxHuntFrame foxHuntFrame = new FoxHuntFrame();
        FoxHuntFrame.JPanel panel = foxHuntFrame.panel;
        assertEquals(1, panel.getComponentCount());
        FoxHuntFrame.JSplitPane controls = (FoxHuntFrame.JSplitPane) panel.getComponent(0);
        assertEquals(foxHuntFrame.panel1, controls.getLeftComponent());
        assertEquals(foxHuntFrame.panel2, controls.getRightComponent());
        FoxHuntFrame.JSplitPane map = (FoxHuntFrame.JSplitPane) controls.getLeftComponent();
        assertEquals(foxHuntFrame.panel1, map.getLeftComponent());
        assertEquals(foxHuntFrame.panel2, map.getRightComponent());
        assertEquals(foxHuntFrame.panel3, controls.getRightComponent());
        assertEquals(FoxHuntFrame.JSPLIT_PANE_VERTICAL_SPLIT, controls.getOrientation(), 0);
        assertEquals(FoxHuntFrame.JSPLIT_PANE_HORIZONTAL_SPLIT, map.getOrientation(), 0);
    }

    @Test
    public void testTextPrep() {
        // Test Case 1
        FoxHuntFrame foxHuntFrame = new FoxHuntFrame();
        assertEquals("", foxHuntFrame.theta.getText());
        assertEquals("", foxHuntFrame.lat.getText());
        assertEquals("", foxHuntFrame.lon.getText());
        
        // Test Case 2
        foxHuntFrame.theta.setText("45");
        foxHuntFrame.lat.setText("23.456");
        foxHuntFrame.lon.setText("34.567");
        assertEquals("45", foxHuntFrame.theta.getText());
        assertEquals("23.456", foxHuntFrame.lat.getText());
        assertEquals("34.567", foxHuntFrame.lon.getText());
    }

    @Test
    public void testTablePrep() {
        // Test Case 1
        FoxHuntFrame foxHuntFrame = new FoxHuntFrame();
        FoxHuntFrame.JTable pos = foxHuntFrame.pos;
        assertEquals(1, pos.getComponents().length);
        assertEquals(foxHuntFrame.posModel, pos.getModel());
        assertEquals(FoxHuntFrame.JTABLE_W_AUTOSIZE_OFF, pos.getAutoResizeMode(), 0);
        assertEquals(new Dimension(500, 70), pos.getPreferredScrollableViewportSize());
        assertEquals(0, pos.getColumnCount());
        assertEquals(0, pos.getRowCount());
    }

    @Test
    public void testDisplayPrep() {
        // Test Case 1
        FoxHuntFrame foxHuntFrame = new FoxHuntFrame();
        FoxHuntFrame.HuntDisplay display = foxHuntFrame.display;
        assertEquals(0, display.points.size());
        assertEquals(0, display.landmarks.size());
    }

    @Test
    public void testLoadLandmarks() {
        // Test Case 1
        FoxHuntFrame foxHuntFrame = new FoxHuntFrame();
        foxHuntFrame.loadLandmarks();
        assertEquals(0, foxHuntFrame.display.landmarks.size());
    }

    @Test
    public void testError() {
        // Test Case 1
        FoxHuntFrame foxHuntFrame = new FoxHuntFrame();
        foxHuntFrame.error("Can't Find the file");
        assertEquals(0, foxHuntFrame.display.landmarks.size());

        // Test Case 2
        foxHuntFrame.error("Error communicating with the file");
        assertEquals(0, foxHuntFrame.display.landmarks.size());
    }

    @Test
    public void testFoxTableModel() {
        // Test Case 1
        FoxHuntFrame foxHuntFrame = new FoxHuntFrame();
        FoxHuntFrame.FoxTableModel foxTableModel = foxHuntFrame.new FoxTableModel();
        assertEquals(2, foxTableModel.getColumnCount());
        assertEquals(0, foxTableModel.getRowCount());
        assertEquals("Lattitude", foxTableModel.getColumnName(0));
        assertEquals("Longitude", foxTableModel.getColumnName(1));
        assertNull(foxTableModel.getValueAt(0, 0));
        assertNull(foxTableModel.getValueAt(0, 1));
        assertEquals(Double.class, foxTableModel.getColumnClass(0));
        assertEquals(Double.class, foxTableModel.getColumnClass(1));
        assertEquals(false, foxTableModel.isCellEditable(0, 0));
    }
}