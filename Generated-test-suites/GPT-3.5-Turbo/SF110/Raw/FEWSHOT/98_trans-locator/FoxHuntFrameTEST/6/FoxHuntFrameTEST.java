import org.junit.Test;
import static org.junit.Assert.*;

public class FoxHuntFrameTest {

    @Test
    public void testAddPoint() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.theta.setText("45");
        frame.lat.setText("10.5");
        frame.lon.setText("20.7");
        frame.addPoint();
        assertEquals(1, frame.points.size());
        assertEquals(1, frame.fox.size());
        assertEquals(new TrigPoint(10.5, 20.7, 45), frame.points.get(0));
        assertEquals(new TrigPoint(10.527, 20.724, 45), frame.fox.get(0));
        assertEquals(new TrigPoint(10.527, 20.724, 45), frame.display.points.get(0));
    }

    @Test
    public void testActionPerformed() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.theta.setText("45");
        frame.lat.setText("10.5");
        frame.lon.setText("20.7");
        frame.actionPerformed(null);
        assertEquals(1, frame.points.size());
        assertEquals(1, frame.fox.size());
        assertEquals(new TrigPoint(10.5, 20.7, 45), frame.points.get(0));
        assertEquals(new TrigPoint(10.527, 20.724, 45), frame.fox.get(0));
        assertEquals(new TrigPoint(10.527, 20.724, 45), frame.display.points.get(0));
    }

    @Test
    public void testFinalPrep() {
        FoxHuntFrame frame = new FoxHuntFrame();
        assertEquals("1", frame.zoom.getText());
        assertNotNull(frame.panel3);
        assertNotNull(frame.display);
        assertNotNull(frame.panel);
    }

    @Test
    public void testTextPrep() {
        FoxHuntFrame frame = new FoxHuntFrame();
        assertEquals("", frame.theta.getText());
        assertEquals("", frame.lat.getText());
        assertEquals("", frame.lon.getText());
    }

    @Test
    public void testTablePrep() {
        FoxHuntFrame frame = new FoxHuntFrame();
        assertNotNull(frame.posModel);
        assertNotNull(frame.pos);
    }

    @Test
    public void testDisplayPrep() {
        FoxHuntFrame frame = new FoxHuntFrame();
        assertNotNull(frame.display);
    }

    @Test
    public void testLoadLandmarks() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.loadLandmarks();
        assertEquals(0, frame.display.landmarks.size());
    }

    @Test
    public void testError() {
        FoxHuntFrame frame = new FoxHuntFrame();
        
        // Test user aborts
        System.setIn(new ByteArrayInputStream("0".getBytes()));
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        frame.error("Error message");
        String expectedOutput = "Error message\n";
        assertEquals(expectedOutput, outContent.toString());
        assertEquals(0, frame.getDefaultCloseOperation());
        System.setIn(System.in);
        
        // Test user continues
        System.setIn(new ByteArrayInputStream("1".getBytes()));
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        frame.error("Error message");
        expectedOutput = "Error message\n";
        assertEquals(expectedOutput, outContent.toString());
        assertEquals(1, frame.getDefaultCloseOperation());
        System.setIn(System.in);
    }

}