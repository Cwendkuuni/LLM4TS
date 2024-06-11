import static org.junit.Assert.*;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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
    public void testActionPerformedd() {
        // Test adding a point to the frame
        JTextField theta = foxHuntFrame.theta;
        JTextField lat = foxHuntFrame.lat;
        JTextField lon = foxHuntFrame.lon;
        
        theta.setText("45");
        lat.setText("12.3456");
        lon.setText("23.4567");
        
        JButton button = foxHuntFrame.add;
        button.doClick();
        
        assertEquals(1, foxHuntFrame.points.size());
        assertEquals(12.3456, ((TrigPoint)foxHuntFrame.points.get(0)).getLatitude(), 0.0001);
        assertEquals(23.4567, ((TrigPoint)foxHuntFrame.points.get(0)).getLongitude(), 0.0001);
        
        // Test adding multiple points to the frame
        theta.setText("90");
        lat.setText("98.7654");
        lon.setText("87.6543");
        button.doClick();
        
        assertEquals(2, foxHuntFrame.points.size());
        assertEquals(98.7654, ((TrigPoint)foxHuntFrame.points.get(1)).getLatitude(), 0.0001);
        assertEquals(87.6543, ((TrigPoint)foxHuntFrame.points.get(1)).getLongitude(), 0.0001);
    }
    
    @Test
    public void testAddPoint() {
        foxHuntFrame.theta.setText("0");
        foxHuntFrame.lat.setText("0.0000");
        foxHuntFrame.lon.setText("0.0000");
        
        JButton button = foxHuntFrame.add;
        button.doClick();
        
        assertEquals(1, foxHuntFrame.points.size());
        
        // Ensure point is added correctly
        assertEquals(0, ((TrigPoint)foxHuntFrame.points.get(0)).getAngle());
        assertEquals(0.0000, ((TrigPoint)foxHuntFrame.points.get(0)).getLatitude(), 0.0001);
        assertEquals(0.0000, ((TrigPoint)foxHuntFrame.points.get(0)).getLongitude(), 0.0001);
        
        // Add another point
        foxHuntFrame.theta.setText("45");
        foxHuntFrame.lat.setText("12.3456");
        foxHuntFrame.lon.setText("23.4567");
        button.doClick();
        
        assertEquals(2, foxHuntFrame.points.size());
        
        // Ensure new point is added correctly
        assertEquals(45, ((TrigPoint)foxHuntFrame.points.get(1)).getAngle());
        assertEquals(12.3456, ((TrigPoint)foxHuntFrame.points.get(1)).getLatitude(), 0.0001);
        assertEquals(23.4567, ((TrigPoint)foxHuntFrame.points.get(1)).getLongitude(), 0.0001);
        
        // Ensure calculation of fox point is correct
        TrigPoint C = new TrigPoint(0.0000, 0.0000, 0);
        TrigPoint B = new TrigPoint(12.3456, 23.4567, 45);
        assertEquals(B, foxHuntFrame.fox.get(0));
    }
    
    @Test
    public void testFinalPrep() {
        foxHuntFrame.finalPrep();
        JTextField zoom = foxHuntFrame.zoom;
        JPanel panel3 = foxHuntFrame.panel3;
        HuntDisplay display = foxHuntFrame.display;
        JPanel panel = foxHuntFrame.panel;
        
        // Ensure zoom field is initialized correctly
        assertEquals("1", zoom.getText());
        
        // Ensure panel3 is initialized correctly
        assertEquals(zoom, panel3.getComponent(0));
        assertEquals(display, panel3.getComponent(1));
        
        // Ensure panel is initialized correctly
        assertEquals(panel3, panel.getComponent(0));
    }
    
    @Test
    public void testTextPrep() {
        foxHuntFrame.textPrep();
        JTextField theta = foxHuntFrame.theta;
        JTextField lat = foxHuntFrame.lat;
        JTextField lon = foxHuntFrame.lon;
        JButton add = foxHuntFrame.add;
        JPanel panel1 = foxHuntFrame.panel1;
        
        // Ensure text fields are created and initialized correctly
        assertEquals(3, theta.getColumns());
        assertEquals(9, lat.getColumns());
        assertEquals(10, lon.getColumns());
        
        // Ensure button is created correctly
        assertEquals("Add It!!!", add.getText());
        
        // Ensure panel1 is created correctly
        assertEquals(8, panel1.getComponentCount());
        assertEquals(new JLabel("Theta in Degrees"), panel1.getComponent(0));
        assertEquals(theta, panel1.getComponent(1));
        assertEquals(new JLabel("Lat. dd*mm.mmm"), panel1.getComponent(2));
        assertEquals(lat, panel1.getComponent(3));
        assertEquals(new JLabel("lon. ddd*mm.mmm"), panel1.getComponent(4));
        assertEquals(lon, panel1.getComponent(5));
        assertEquals(add, panel1.getComponent(6));
    }
    
    @Test
    public void testTablePrep() {
        foxHuntFrame.tablePrep();
        JTable pos = foxHuntFrame.pos;
        JPanel panel2 = foxHuntFrame.panel2;
        
        // Ensure table is created and initialized correctly
        assertEquals("Lattitude", pos.getColumnName(0));
        assertEquals("Longitude", pos.getColumnName(1));
        
        jScrollPane scroll = (JScrollPane) panel2.getComponent(0);
        assertEquals(pos, scroll.getViewport().getView());
        
        assertEquals(new Dimension(500, 70), pos.getPreferredScrollableViewportSize());
    }
    
    @Test
    public void testDisplayPrep() {
        foxHuntFrame.displayPrep();
        HuntDisplay display = foxHuntFrame.display;
        
        // Ensure display is created correctly
        assertNotNull(display);
    }
    
    @Test
    public void testLoadLandmarks() {
        foxHuntFrame.loadLandmarks();
        
        // Ensure landmarks are correctly added to the display
        assertEquals(4, foxHuntFrame.display.landmarks.size());
        assertEquals(39.1234, ((Landmark)foxHuntFrame.display.landmarks.get(2)).getLatitude(), 0.0001);
        assertEquals(56.7890, ((Landmark)foxHuntFrame.display.landmarks.get(2)).getLongitude(), 0.0001);
    }
    
    @Test
    public void testError() {
        String errorMessage = "This is an error message";
        
        // Mock a JOptionPane.showOptionDialog with first option chosen
        JOptionPane.showOptionDialog(null, errorMessage, "Error", 0, 0, null, null, null);
        
        // Ensure program exits when first option is chosen
        assertEquals(1, System.exit(1));
    }
}