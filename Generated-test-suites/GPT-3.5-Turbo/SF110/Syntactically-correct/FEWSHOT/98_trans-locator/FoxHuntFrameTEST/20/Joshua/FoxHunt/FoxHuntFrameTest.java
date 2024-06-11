package Joshua.FoxHunt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import org.junit.Before;
import org.junit.Test;

public class FoxHuntFrameTest {
    
    private FoxHuntFrame foxHuntFrame;
    
    @Before
    public void setUp() {
        foxHuntFrame = new FoxHuntFrame();
    }
    
    @Test
    public void testFoxHuntFrameConstructor() {
        assertNotNull(foxHuntFrame);
    }
    
    @Test
    public void testActionPerformed() {
        ActionEvent e = new ActionEvent(new JButton(), 0, "command");
        foxHuntFrame.actionPerformed(e);
    }
    
    @Test
    public void testAddPoint() {
        foxHuntFrame.addPoint();
    }
    
    @Test
    public void testFinalPrep() {
        foxHuntFrame.finalPrep();
    }
    
    @Test
    public void testTextPrep() {
        foxHuntFrame.textPrep();
    }
    
    @Test
    public void testTablePrep() {
        foxHuntFrame.tablePrep();
    }
    
    @Test
    public void testDisplayPrep() {
        foxHuntFrame.displayPrep();
    }
    
    @Test
    public void testLoadLandmarks() {
        foxHuntFrame.loadLandmarks();
    }
    
    @Test
    public void testError() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        foxHuntFrame.error("Test error message");
        assertEquals("Test error message", outContent.toString());
        System.setOut(System.out);
    }
    
    @Test
    public void testFoxTableModelConstructor() {
        FoxHuntFrame.FoxTableModel foxTableModel = foxHuntFrame.new FoxTableModel();
        assertNotNull(foxTableModel);
    }
    
    @Test
    public void testGetColumnCount() {
        FoxHuntFrame.FoxTableModel foxTableModel = foxHuntFrame.new FoxTableModel();
        assertEquals(2, foxTableModel.getColumnCount());
    }
    
    @Test
    public void testGetRowCount() {
        FoxHuntFrame.FoxTableModel foxTableModel = foxHuntFrame.new FoxTableModel();
        assertEquals(0, foxTableModel.getRowCount());
    }
    
    @Test
    public void testGetColumnName() {
        FoxHuntFrame.FoxTableModel foxTableModel = foxHuntFrame.new FoxTableModel();
        assertEquals("Lattitude", foxTableModel.getColumnName(0));
        assertEquals("Longitude", foxTableModel.getColumnName(1));
    }
    
    @Test
    public void testGetValueAt() {
        double LAT = 37.7749;
        double LON = -122.4194;
        int angle = 45;
        TrigPoint point = new TrigPoint(LAT, LON, angle);
        foxHuntFrame.points.add(point);
        
        FoxHuntFrame.FoxTableModel foxTableModel = foxHuntFrame.new FoxTableModel();
        Object valueAtZeroZero = foxTableModel.getValueAt(0, 0);
        Object valueAtZeroOne = foxTableModel.getValueAt(0, 1);
        
        DecimalFormat formatter = new DecimalFormat("###.##############");
        String expectedLattitude = formatter.format(LAT);
        String expectedLongitude = formatter.format(LON);
        
        assertEquals(expectedLattitude, valueAtZeroZero);
        assertEquals(expectedLongitude, valueAtZeroOne);
    }
    
    @Test
    public void testGetColumnClass() {
        FoxHuntFrame.FoxTableModel foxTableModel = foxHuntFrame.new FoxTableModel();
        assertEquals(String.class, foxTableModel.getColumnClass(0));
        assertEquals(String.class, foxTableModel.getColumnClass(1));
    }
    
    @Test
    public void testIsCellEditable() {
        FoxHuntFrame.FoxTableModel foxTableModel = foxHuntFrame.new FoxTableModel();
        assertFalse(foxTableModel.isCellEditable(0, 0));
        assertFalse(foxTableModel.isCellEditable(0, 1));
    }
}