package Joshua.FoxHunt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import org.junit.Before;
import org.junit.Test;

public class FoxHuntFrameTest {
    private FoxHuntFrame foxHuntFrame;

    @Before
    public void setUp() {
        foxHuntFrame = new FoxHuntFrame();
    }

    @Test
    public void testAddPoint() {
        foxHuntFrame.theta.setText("45");
        foxHuntFrame.lat.setText("35.123");
        foxHuntFrame.lon.setText("139.456");
        
        foxHuntFrame.addPoint();
        
        Vector points = (Vector) ReflectionTestUtils.getField(foxHuntFrame, "points");
        assertEquals(1, points.size());
        TrigPoint point = (TrigPoint) points.get(0);
        assertEquals(45, point.getAngle());
        assertEquals(35.123, point.getLatitude(), 0.001);
        assertEquals(139.456, point.getLongitude(), 0.001);
        
        Vector fox = (Vector) ReflectionTestUtils.getField(foxHuntFrame, "fox");
        assertEquals(0, fox.size());
        
        JTextField theta = (JTextField) ReflectionTestUtils.getField(foxHuntFrame, "theta");
        JTextField lat = (JTextField) ReflectionTestUtils.getField(foxHuntFrame, "lat");
        JTextField lon = (JTextField) ReflectionTestUtils.getField(foxHuntFrame, "lon");
        assertEquals("", theta.getText());
        assertEquals("", lat.getText());
        assertEquals("", lon.getText());
    }

    @Test
    public void testActionPerformed() {
        JButton add = (JButton) ReflectionTestUtils.getField(foxHuntFrame, "add");
        add.doClick();
        
        // Verify that addPoint() method is called
        Vector points = (Vector) ReflectionTestUtils.getField(foxHuntFrame, "points");
        assertEquals(1, points.size());
    }

    @Test
    public void testFinalPrep() {
        JPanel panel = (JPanel) ReflectionTestUtils.getField(foxHuntFrame, "panel");
        JSplitPane map = (JSplitPane) panel.getComponent(0);
        JSplitPane controls = (JSplitPane) map.getComponent(0);
        JPanel zoomP = (JPanel) controls.getLeftComponent();

        JTextField zoom = (JTextField) ReflectionTestUtils.getField(foxHuntFrame, "zoom");
        JLabel zoomL = (JLabel) zoomP.getComponent(0);
        assertEquals("Zoom", zoomL.getText());
        assertEquals(zoom, zoomP.getComponent(1));

        JPanel panel3 = (JPanel) ReflectionTestUtils.getField(foxHuntFrame, "panel3");
        assertEquals(zoom, panel3.getComponent(0));

        HuntDisplay display = (HuntDisplay) ReflectionTestUtils.getField(foxHuntFrame, "display");
        assertEquals(display, panel3.getComponent(1));

        JPanel panel = (JPanel) ReflectionTestUtils.getField(foxHuntFrame, "panel");
        assertEquals(map, panel.getComponent(0));
    }

    @Test
    public void testTextPrep() {
        JTextField theta = (JTextField) ReflectionTestUtils.getField(foxHuntFrame, "theta");
        JTextField lat = (JTextField) ReflectionTestUtils.getField(foxHuntFrame, "lat");
        JTextField lon = (JTextField) ReflectionTestUtils.getField(foxHuntFrame, "lon");
        JButton add = (JButton) ReflectionTestUtils.getField(foxHuntFrame, "add");
        JPanel panel1 = (JPanel) ReflectionTestUtils.getField(foxHuntFrame, "panel1");

        JLabel thetaL = (JLabel) panel1.getComponent(0);
        assertEquals("Theta in Degrees", thetaL.getText());
        assertEquals(theta, panel1.getComponent(1));
        JLabel latL = (JLabel) panel1.getComponent(2);
        assertEquals("Lat. dd*mm.mmm", latL.getText());
        assertEquals(lat, panel1.getComponent(3));
        JLabel lonL = (JLabel) panel1.getComponent(4);
        assertEquals("lon. ddd*mm.mmm", lonL.getText());
        assertEquals(lon, panel1.getComponent(5));
        assertEquals(add, panel1.getComponent(6));
    }

    @Test
    public void testTablePrep() {
        FoxTableModel posModel = (FoxTableModel) ReflectionTestUtils.getField(foxHuntFrame, "posModel");
        JTable pos = (JTable) ReflectionTestUtils.getField(foxHuntFrame, "pos");
        JScrollPane scroll = (JScrollPane) ReflectionTestUtils.getField(foxHuntFrame, "scroll");
        JPanel panel2 = (JPanel) ReflectionTestUtils.getField(foxHuntFrame, "panel2");

        assertEquals(posModel, pos.getModel());
        assertEquals(pos, scroll.getViewport().getView());
        assertEquals(scroll, panel2.getComponent(0));
    }

    @Test
    public void testDisplayPrep() {
        HuntDisplay display = (HuntDisplay) ReflectionTestUtils.getField(foxHuntFrame, "display");
        Field landmarksField = ReflectionUtils.findField(HuntDisplay.class, "landmarks");
        landmarksField.setAccessible(true);
        Vector landmarks = (Vector) ReflectionUtils.getField(landmarksField, display);

        Landmark landmark1 = new Landmark(35.648, 139.736, new ImageIcon("icon1.gif"));
        Landmark landmark2 = new Landmark(35.653, 139.748, new ImageIcon("icon2.gif"));

        landmarks.add(landmark1);
        landmarks.add(landmark2);

        assertTrue(landmarks.contains(landmark1));
        assertTrue(landmarks.contains(landmark2));
    }

    @Test
    public void testLoadLandmarks() throws IOException {
        final JFrame frame = new JFrame();
        FrameMockUtil util = new FrameMockUtil(frame);

        MockClassUtil.setFieldValue(foxHuntFrame, "frame", frame);

        File file = new File("./landmarks.cfg");
        PrintWriter writer = new PrintWriter(file);
        writer.println("NUM=2");
        writer.println("FILEicon1.gif");
        writer.println("LAT35.648");
        writer.println("LON139.736");
        writer.println("FILEicon2.gif");
        writer.println("LAT35.653");
        writer.println("LON139.748");
        writer.close();

        FoxHuntFrame frame = new FoxHuntFrame();
        displayMock = mock(HuntDisplay.class);
        final Vector landmarks = new Vector();
        landmarks.add(new Landmark(35.648, 139.736, new ImageIcon("icon1.gif")));
        landmarks.add(new Landmark(35.653, 139.748, new ImageIcon("icon2.gif")));
        ReflectionTestUtils.setField(frame.display, "landmarks", landmarks);

        FileInputStream fileInputStream = new FileInputStream("./landmarks.cfg");
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        whenNew(FileInputStream.class).withArguments("./landmarks.cfg").thenReturn(fileInputStream);
        whenNew(InputStreamReader.class).withArguments(fileInputStream).thenReturn(inputStreamReader);
        whenNew(BufferedReader.class).withArguments(inputStreamReader).thenReturn(bufferedReader);

        frame.loadLandmarks();

        verifyNew(FileInputStream.class).withArguments("./landmarks.cfg");
        verifyNew(InputStreamReader.class).withArguments(fileInputStream);
        verifyNew(BufferedReader.class).withArguments(inputStreamReader);

        writer.close();

        verify(displayMock).addLandMark(new Landmark(35.648, 139.736, new ImageIcon("icon1.gif")));
        verify(displayMock).addLandMark(new Landmark(35.653, 139.748, new ImageIcon("icon2.gif")));

        file.delete();
    }

    @Test
    public void testError() {
        final JFrame frame = new JFrame();
        FrameMockUtil util = new FrameMockUtil(frame);

        MockClassUtil.setFieldValue(foxHuntFrame, "frame", frame);

        String text = "Can't Find the file";

        FoxHuntFrame frame = new FoxHuntFrame();
        displayMock = mock(HuntDisplay.class);
        final JPanel panel = mock(JPanel.class);
        final Object[] options = { "Abort", "Continue" };
        when(JOptionPane.showOptionDialog(panel, text, "Error", JOptionPane.DEFAULT_OPTION,
                JOptionPane.ERROR_MESSAGE, null, options, options[0])).thenReturn(0);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));
        frame.error(text);
        String output = baos.toString();

        assertEquals("Can't Find the file", output);
    }

    @Test
    public void testFoxTableModel() {
        FoxTableModel posModel = new FoxTableModel();
        assertNotNull(posModel.colNames);
        assertEquals(2, posModel.getColumnCount());
        assertTrue(posModel.getColumnName(0).contains("Lattitude"));
        assertTrue(posModel.getColumnName(1).contains("Longitude"));
        assertEquals(FoxHuntFrame.this.fox.size(), posModel.getRowCount());
        assertEquals(String.class, posModel.getColumnClass(0));
        assertEquals(String.class, posModel.getColumnClass(1));
        assertFalse(posModel.isCellEditable(0, 0));
        assertFalse(posModel.isCellEditable(0, 1));
    }
}