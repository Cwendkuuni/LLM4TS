import org.junit.*;

public class HuntDisplayTest {

    @Test
    public void testAddPoint() {
        HuntDisplay display = new HuntDisplay();
        TrigPoint point = new TrigPoint(0, 0, 0, false);
        display.addPoint(point);
        // TODO: Add assertion to check if the point was added correctly
    }
    
    @Test
    public void testAddLandmark() {
        HuntDisplay display = new HuntDisplay();
        Landmark landmark = new Landmark(0, 0, new ImageIcon());
        display.addLandMark(landmark);
        // TODO: Add assertion to check if the landmark was added correctly
    }
    
    @Test
    public void testPaintComponent() {
        // TODO: Add test case to check the functionality of paintComponent method
    }

}