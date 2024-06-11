import org.junit.*;
import static org.junit.Assert.*;
import java.awt.*;
import javax.swing.*;
import java.awt.image.*;

public class HuntDisplayTest {

	@Test
	public void testAddPoint() {
		HuntDisplay huntDisplay = new HuntDisplay();
		TrigPoint point = new TrigPoint(2, 4, 30, false);
		huntDisplay.addPoint(point);
		Vector<TrigPoint> points = huntDisplay.getPoints();
		assertEquals(1, points.size());
		assertEquals(2/90.0, points.elementAt(0).getY(), 0.001);
		assertEquals(4/180.0, points.elementAt(0).getX(), 0.001);
		assertEquals(30, points.elementAt(0).getTheta());
		assertFalse(points.elementAt(0).isFox());
	}
	
	@Test
	public void testAddPointMultipleTimes() {
		HuntDisplay huntDisplay = new HuntDisplay();
		TrigPoint point1 = new TrigPoint(2, 4, 30, false);
		TrigPoint point2 = new TrigPoint(3, 1, 45, true);
		huntDisplay.addPoint(point1);
		huntDisplay.addPoint(point2);
		Vector<TrigPoint> points = huntDisplay.getPoints();
		assertEquals(2, points.size());
		assertEquals(2/90.0, points.elementAt(0).getY(), 0.001);
		assertEquals(4/180.0, points.elementAt(0).getX(), 0.001);
		assertEquals(30, points.elementAt(0).getTheta());
		assertFalse(points.elementAt(0).isFox());
		assertEquals(3/90.0, points.elementAt(1).getY(), 0.001);
		assertEquals(1/180.0, points.elementAt(1).getX(), 0.001);
		assertEquals(45, points.elementAt(1).getTheta());
		assertTrue(points.elementAt(1).isFox());
	}

	@Test
	public void testAddLandMark() {
		HuntDisplay huntDisplay = new HuntDisplay();
		Landmark landmark = new Landmark(4, 3, new ImageIcon());
		huntDisplay.addLandMark(landmark);
		Vector<Landmark> landmarks = huntDisplay.getLandmarks();
		assertEquals(1, landmarks.size());
		assertEquals(4/90.0, landmarks.elementAt(0).getLat(), 0.001);
		assertEquals(3/180.0, landmarks.elementAt(0).getLon(), 0.001);
		assertNull(landmarks.elementAt(0).getIcon());
	}

	@Test
	public void testAddPointAndLandmark() {
		HuntDisplay huntDisplay = new HuntDisplay();
		TrigPoint point = new TrigPoint(2, 4, 30, false);
		Landmark landmark = new Landmark(4, 3, new ImageIcon());
		huntDisplay.addPoint(point);
		huntDisplay.addLandMark(landmark);
		Vector<TrigPoint> points = huntDisplay.getPoints();
		Vector<Landmark> landmarks = huntDisplay.getLandmarks();
		assertEquals(1, points.size());
		assertEquals(2/90.0, points.elementAt(0).getY(), 0.001);
		assertEquals(4/180.0, points.elementAt(0).getX(), 0.001);
		assertEquals(30, points.elementAt(0).getTheta());
		assertFalse(points.elementAt(0).isFox());
		assertEquals(1, landmarks.size());
		assertEquals(4/90.0, landmarks.elementAt(0).getLat(), 0.001);
		assertEquals(3/180.0, landmarks.elementAt(0).getLon(), 0.001);
		assertNull(landmarks.elementAt(0).getIcon());
	}

	@Test
	public void testPaintComponentWithPoints() {
		HuntDisplay huntDisplay = new HuntDisplay();
		TrigPoint point1 = new TrigPoint(2, 4, 30, false);
		TrigPoint point2 = new TrigPoint(3, 1, 45, true);
		huntDisplay.addPoint(point1);
		huntDisplay.addPoint(point2);
		Graphics g = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB).createGraphics();
		huntDisplay.paintComponent(g);
		// Verify paint operation visually
	}
	
	@Test
	public void testPaintComponentWithLandmarks() {
		HuntDisplay huntDisplay = new HuntDisplay();
		Landmark landmark1 = new Landmark(4, 3, new ImageIcon());
		Landmark landmark2 = new Landmark(1, 2, new ImageIcon());
		huntDisplay.addLandMark(landmark1);
		huntDisplay.addLandMark(landmark2);
		Graphics g = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB).createGraphics();
		huntDisplay.paintComponent(g);
		// Verify paint operation visually
	}
	
}