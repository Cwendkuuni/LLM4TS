import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import javax.swing.*;
import java.util.*;
import java.awt.image.*;
import java.awt.*;

public class HuntDisplayTest {

    private HuntDisplay huntDisplay;

    @Before
    public void setup() {
        huntDisplay = new HuntDisplay();
    }

    @Test
    public void testGetPreferredSize() {
        Dimension prefSize = huntDisplay.getPreferredSize();
        assertNotNull(prefSize);
        assertEquals(200, prefSize.getWidth(), 0);
        assertEquals(200, prefSize.getHeight(), 0);
    }

    @Test
    public void testGetMinimumSize() {
        Dimension minSize = huntDisplay.getMinimumSize();
        assertNotNull(minSize);
        assertEquals(50, minSize.getWidth(), 0);
        assertEquals(50, minSize.getHeight(), 0);
    }

    @Test
    public void testAddPoint() {
        TrigPoint point = new TrigPoint(45, 90, 0, true);
        huntDisplay.addPoint(point);
        // Assert any conditions after adding point
    }

    @Test
    public void testAddLandMark() {
        Landmark landmark = new Landmark(30, 60, new ImageIcon());
        huntDisplay.addLandMark(landmark);
        // Assert any conditions after adding landmark
    }

    @Test
    public void testPaintComponent() {
        HuntDisplay display = new HuntDisplay();
        Graphics graphics = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB).getGraphics();
        // Assert any conditions after painting component
    }
}