// import necessary packages
import org.junit.Before;
import org.junit.Test;
import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class FoxHuntFrameTest {
    private FoxHuntFrame foxHuntFrame;

    @Before
    public void setUp() {
        // create an instance of the FoxHuntFrame class
        foxHuntFrame = new FoxHuntFrame();
    }

    @Test
    public void testAddPoint_noPoints() {
        // input test values
        String thetaText = "45";
        String latText = "40.1234";
        String lonText = "-80.5678";

        // set the test values to the text fields in the frame
        foxHuntFrame.theta.setText(thetaText);
        foxHuntFrame.lat.setText(latText);
        foxHuntFrame.lon.setText(lonText);

        // simulate the button click
        foxHuntFrame.addPoint();

        // get the added point
        TrigPoint point = (TrigPoint) foxHuntFrame.points.get(0);

        // assert the correctness of the added point
        assertEquals(Double.parseDouble(latText), point.getLat(), 0.0001);
        assertEquals(Double.parseDouble(lonText), point.getLon(), 0.0001);
        assertEquals(Integer.parseInt(thetaText), point.getTheta());
    }

    @Test
    public void testAddPoint_twoPoints() {
        // input test values for the first point
        String thetaText1 = "45";
        String latText1 = "40.1234";
        String lonText1 = "-80.5678";

        // set the test values to the text fields in the frame
        foxHuntFrame.theta.setText(thetaText1);
        foxHuntFrame.lat.setText(latText1);
        foxHuntFrame.lon.setText(lonText1);

        // simulate the button click
        foxHuntFrame.addPoint();

        // input test values for the second point
        String thetaText2 = "60";
        String latText2 = "41.9876";
        String lonText2 = "-79.5432";

        // set the test values to the text fields in the frame
        foxHuntFrame.theta.setText(thetaText2);
        foxHuntFrame.lat.setText(latText2);
        foxHuntFrame.lon.setText(lonText2);

        // simulate the button click
        foxHuntFrame.addPoint();

        // get the added points
        TrigPoint point1 = (TrigPoint) foxHuntFrame.points.get(0);
        TrigPoint point2 = (TrigPoint) foxHuntFrame.points.get(1);
        TrigPoint foxPoint = (TrigPoint) foxHuntFrame.fox.get(0);

        // assert the correctness of the added points
        assertEquals(Double.parseDouble(latText1), point1.getLat(), 0.0001);
        assertEquals(Double.parseDouble(lonText1), point1.getLon(), 0.0001);
        assertEquals(Integer.parseInt(thetaText1), point1.getTheta());

        assertEquals(Double.parseDouble(latText2), point2.getLat(), 0.0001);
        assertEquals(Double.parseDouble(lonText2), point2.getLon(), 0.0001);
        assertEquals(Integer.parseInt(thetaText2), point2.getTheta());

        // assert the correctness of the added fox point
        // using the formula: B = FoxCalc.calcFox(point, C)
        TrigPoint pointC = point1;
        TrigPoint pointB = FoxCalc.calcFox(point2, pointC);

        assertEquals(pointB, foxPoint);
    }

    @Test
    public void testLoadLandmarks() {
        // add test landmarks to the display
        Landmark landmark1 = new Landmark(39.9876, -79.5432, new ImageIcon("icon1.gif"));
        Landmark landmark2 = new Landmark(40.1234, -80.5678, new ImageIcon("icon2.gif"));

        foxHuntFrame.display.addLandMark(landmark1);
        foxHuntFrame.display.addLandMark(landmark2);

        // assert the correctness of the added landmarks
        Vector<Landmark> landmarks = foxHuntFrame.display.getLandmarks();
        assertEquals(2, landmarks.size());
        assertEquals(landmark1, landmarks.get(0));
        assertEquals(landmark2, landmarks.get(1));
    }
}