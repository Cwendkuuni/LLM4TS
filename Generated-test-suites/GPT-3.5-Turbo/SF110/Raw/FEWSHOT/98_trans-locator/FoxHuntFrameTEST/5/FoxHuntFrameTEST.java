package Joshua.FoxHunt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;

import org.junit.Before;
import org.junit.Test;

public class FoxHuntFrameTest {

    private FoxHuntFrame frame;

    @Before
    public void setUp() {
        frame = new FoxHuntFrame();
    }

    @Test
    public void testConstructor() {
        assertNotNull(frame);
        assertEquals("Triangulation", frame.getTitle());
        assertTrue(frame.getDefaultCloseOperation() == JFrame.EXIT_ON_CLOSE);
        assertNotNull(frame.getContentPane());
    }

    @Test
    public void testActionPerformed() {
        JTextField theta = frame.theta;
        JTextField lat = frame.lat;
        JTextField lon = frame.lon;

        theta.setText("45");
        lat.setText("37.2350");
        lon.setText("-115.8111");

        JButton add = frame.add;
        ActionListener[] actionListeners = add.getActionListeners();

        assertEquals(1, actionListeners.length);

        actionListeners[0].actionPerformed(new ActionEvent(add, ActionEvent.ACTION_PERFORMED, ""));

        assertEquals(1, frame.points.size());
        assertEquals(45, frame.points.get(0).getAngle());
        assertEquals(37.2350, frame.points.get(0).getLat(), 0);
        assertEquals(-115.8111, frame.points.get(0).getLon(), 0);

        assertEquals(1, frame.display.points.size());
        assertEquals(45, frame.display.points.get(0).getAngle());
        assertEquals(37.2350, frame.display.points.get(0).getLat(), 0);
        assertEquals(-115.8111, frame.display.points.get(0).getLon(), 0);
    }

    @Test
    public void testAddPoint() {
        frame.theta.setText("45");
        frame.lat.setText("37.2350");
        frame.lon.setText("-115.8111");

        frame.addPoint();

        assertEquals(1, frame.points.size());
        assertEquals(45, frame.points.get(0).getAngle());
        assertEquals(37.2350, frame.points.get(0).getLat(), 0);
        assertEquals(-115.8111, frame.points.get(0).getLon(), 0);

        assertEquals(1, frame.display.points.size());
        assertEquals(45, frame.display.points.get(0).getAngle());
        assertEquals(37.2350, frame.display.points.get(0).getLat(), 0);
        assertEquals(-115.8111, frame.display.points.get(0).getLon(), 0);
    }

    @Test
    public void testFinalPrep() {
        JTextField zoom = frame.zoom;

        assertNotNull(zoom);
        assertEquals("1", zoom.getText());

        JPanel panel3 = frame.panel3;

        assertNotNull(panel3);
        assertEquals(1, panel3.getComponentCount());
        assertTrue(panel3.getComponent(0) instanceof JTextField);

        JPanel panel = frame.panel;

        assertNotNull(panel);
        assertEquals(1, panel.getComponentCount());
        assertTrue(panel.getComponent(0) instanceof JSplitPane);
    }

    @Test
    public void testTextPrep() {
        JTextField theta = frame.theta;
        JTextField lat = frame.lat;
        JTextField lon = frame.lon;

        assertNotNull(theta);
        assertNotNull(lat);
        assertNotNull(lon);
        assertEquals(3, theta.getColumns());
        assertEquals(9, lat.getColumns());
        assertEquals(10, lon.getColumns());

        JButton add = frame.add;
        ActionListener[] actionListeners = add.getActionListeners();

        assertNotNull(add);
        assertEquals(1, actionListeners.length);
    }

    @Test
    public void testTablePrep() {
        JTable pos = frame.pos;

        assertNotNull(pos);
        assertTrue(pos.getModel() instanceof AbstractTableModel);
    }

    @Test
    public void testDisplayPrep() {
        HuntDisplay display = frame.display;

        assertNotNull(display);
        assertTrue(frame.panel3.getComponent(1) == display);
    }

    @Test
    public void testLoadLandmarks() {
        frame.loadLandmarks();

        assertEquals(4, frame.display.landmarks.size());

        Landmark landmark1 = frame.display.landmarks.get(0);
        assertEquals(37.2350, landmark1.getLat(), 0);
        assertEquals(-115.8111, landmark1.getLon(), 0);
        assertNotNull(landmark1.getIcon());

        Landmark landmark2 = frame.display.landmarks.get(1);
        assertEquals(38.2350, landmark2.getLat(), 0);
        assertEquals(-116.8111, landmark2.getLon(), 0);
        assertNotNull(landmark2.getIcon());

        Landmark landmark3 = frame.display.landmarks.get(2);
        assertEquals(39.2350, landmark3.getLat(), 0);
        assertEquals(-117.8111, landmark3.getLon(), 0);
        assertNotNull(landmark3.getIcon());

        Landmark landmark4 = frame.display.landmarks.get(3);
        assertEquals(40.2350, landmark4.getLat(), 0);
        assertEquals(-118.8111, landmark4.getLon(), 0);
        assertNotNull(landmark4.getIcon());
    }
}