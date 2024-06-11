
import org.junit.Test;
import java.awt.Color;
import javax.swing.JComponent;
import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.TrigPoint;
import Joshua.FoxHunt.Landmark;

public class HuntDisplayTest {

    @Test
    public void testHuntDisplay() {
        HuntDisplay display = new HuntDisplay();
        assert(display.points.size() == 0);
        assert(display.landmarks.size() == 0);
        assert(display.getBackground() == Color.WHITE);
        assert(display.getPreferredSize().equals(new Dimension(200, 200)));
        assert(display.getMinimumSize().equals(new Dimension(50, 50)));
    }
    
    @Test
    public void testAddPoint() {
        HuntDisplay display = new HuntDisplay();
        TrigPoint point = new TrigPoint(1, 1, 0, false);
        display.addPoint(point);
        assert(display.points.size() == 1);
        assert(display.points.get(0).equals(point));
    }
    
    @Test
    public void testAddLandmark() {
        HuntDisplay display = new HuntDisplay();
        Landmark landmark = new Landmark(0, 0, new ImageIcon());
        display.addLandMark(landmark);
        assert(display.landmarks.size() == 1);
        assert(display.landmarks.get(0).equals(landmark));
    }

}
