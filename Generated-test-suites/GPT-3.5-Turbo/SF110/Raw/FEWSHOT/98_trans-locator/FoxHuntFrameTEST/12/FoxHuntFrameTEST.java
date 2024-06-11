package Joshua.FoxHunt;

import org.junit.Before;
import org.junit.Test;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Vector;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class FoxHuntFrameTest {
    
    private FoxHuntFrame foxHuntFrame;
    
    @Before
    public void setUp() {
        foxHuntFrame = new FoxHuntFrame();
    }
    
    @Test
    public void testAddPoint() {
        JTextField theta = mock(JTextField.class);
        JTextField lat = mock(JTextField.class);
        JTextField lon = mock(JTextField.class);
        foxHuntFrame.theta = theta;
        foxHuntFrame.lat = lat;
        foxHuntFrame.lon = lon;
        
        TrigPoint point = new TrigPoint(0.0, 0.0, 0);
        foxHuntFrame.addPoint();
        Vector<TrigPoint> points = (Vector<TrigPoint>) foxHuntFrame.points;
        assertEquals(1, points.size());
        assertEquals(point, points.get(0));
        
        foxHuntFrame.addPoint();
        points = (Vector<TrigPoint>) foxHuntFrame.points;
        assertEquals(2, points.size());
        
        TrigPoint B = FoxCalc.calcFox(points.get(1), points.get(0));
        Vector<TrigPoint> fox = (Vector<TrigPoint>) foxHuntFrame.fox;
        assertEquals(B, fox.get(0));
    }
    
    @Test
    public void testFinalPrep() {
        foxHuntFrame.finalPrep();
        JPanel expectedPanel = new JPanel(new BorderLayout());
        expectedPanel.add(foxHuntFrame.zoom, BorderLayout.NORTH);
        expectedPanel.add(foxHuntFrame.display, BorderLayout.CENTER);
        assertEquals(expectedPanel, foxHuntFrame.panel3);
        
        JPanel expectedPanel2 = new JPanel(new BorderLayout());
        JSplitPane expectedControls = new JSplitPane(1, foxHuntFrame.panel1, foxHuntFrame.panel2);
        JSplitPane expectedMap = new JSplitPane(0, expectedControls, foxHuntFrame.panel3);
        expectedMap.setDividerLocation(0.25);
        expectedPanel2.add(expectedMap, BorderLayout.CENTER);
        assertEquals(expectedPanel2, foxHuntFrame.panel);
    }
    
    @Test
    public void testTextPrep() {
        foxHuntFrame.textPrep();
        JTextField expectedTheta = new JTextField(3);
        JTextField expectedLat = new JTextField(9);
        JTextField expectedLon = new JTextField(10);
        JButton expectedAdd = new JButton("Add It!!!");
        JLabel expectedThetaL = new JLabel("Theta in Degrees");
        JLabel expectedLatL = new JLabel("Lat. dd*mm.mmm");
        JLabel expectedLonL = new JLabel("lon. ddd*mm.mmm");
        JPanel expectedPanel1 = new JPanel(new GridLayout(4, 2));
        expectedPanel1.add(expectedThetaL);
        expectedPanel1.add(expectedTheta);
        expectedPanel1.add(expectedLatL);
        expectedPanel1.add(expectedLat);
        expectedPanel1.add(expectedLonL);
        expectedPanel1.add(expectedLon);
        expectedPanel1.add(expectedAdd);
        
        assertEquals(expectedTheta, foxHuntFrame.theta);
        assertEquals(expectedLat, foxHuntFrame.lat);
        assertEquals(expectedLon, foxHuntFrame.lon);
        assertEquals(expectedAdd, foxHuntFrame.add);
        assertEquals(expectedPanel1, foxHuntFrame.panel1);
    }
    
    @Test
    public void testTablePrep() {
        foxHuntFrame.tablePrep();
        FoxHuntFrame.FoxTableModel expectedPosModel = new FoxHuntFrame.FoxTableModel();
        JTable expectedPos = new JTable(expectedPosModel);
        JScrollPane expectedScroll = new JScrollPane(expectedPos);
        expectedPos.setPreferredScrollableViewportSize(new Dimension(500, 70));
        JPanel expectedPanel2 = new JPanel(new BorderLayout());
        expectedPanel2.add(expectedScroll, BorderLayout.CENTER);
        
        assertEquals(expectedPosModel, foxHuntFrame.posModel);
        assertEquals(expectedPos, foxHuntFrame.pos);
        assertEquals(expectedPanel2, foxHuntFrame.panel2);
    }
    
    @Test
    public void testDisplayPrep() {
        foxHuntFrame.displayPrep();
        assertNotNull(foxHuntFrame.display);
    }
    
    @Test
    public void testLoadLandmarks() {
        foxHuntFrame.loadLandmarks();
        assertFalse(foxHuntFrame.display.landmarks.isEmpty());
    }
    
    @Test
    public void testError() {
        String text = "Error occurred";
        int ans = 1;
        foxHuntFrame.error(text);
        assertEquals(ans, JOptionPane.showOptionDialog(foxHuntFrame, text, "Error", 0, 0, null, null, null));
    }
    
    @Test
    public void testActionPerformed() {
        ActionEvent e = mock(ActionEvent.class);
        foxHuntFrame.actionPerformed(e);
        assertTrue(foxHuntFrame.points.size() > 0);
        assertNotNull(foxHuntFrame.points.elementAt(0));
    }
    
    @Test
    public void testFoxTableModel() {
        FoxHuntFrame.FoxTableModel foxTableModel = foxHuntFrame.new FoxTableModel();
        assertEquals(2, foxTableModel.getColumnCount());
        assertEquals(foxHuntFrame.fox.size(), foxTableModel.getRowCount());
        for(int i=0; i<foxHuntFrame.fox.size(); i++) {
            assertEquals(Double.class, foxTableModel.getColumnClass(i));
        }
        
        assertFalse(foxTableModel.isCellEditable(0, 0));
        assertFalse(foxTableModel.isCellEditable(0, 1));
        
        assertNotNull(foxTableModel.colNames);
        assertEquals(2, foxTableModel.colNames.length);
        assertEquals("Lattitude", foxTableModel.getColumnName(0));
        assertEquals("Longitude", foxTableModel.getColumnName(1));
        
        DecimalFormat formatter = new DecimalFormat("###.##############");
        TrigPoint point = foxHuntFrame.fox.get(0);
        String expectedOutput = formatter.format(point.getY());
        assertEquals(expectedOutput, foxTableModel.getValueAt(0, 0));
        
        point = foxHuntFrame.fox.get(0);
        expectedOutput = formatter.format(point.getX());
        assertEquals(expectedOutput, foxTableModel.getValueAt(0, 1));
    }
    
}