import org.junit.*;
import static org.junit.Assert.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.swing.table.*;
import java.text.*;

public class FoxHuntFrameTest {

  @Test
  public void testAddPoint() {
    FoxHuntFrame foxHuntFrame = new FoxHuntFrame();

    foxHuntFrame.theta.setText("45");
    foxHuntFrame.lat.setText("1.2345");
    foxHuntFrame.lon.setText("54.321");
    foxHuntFrame.addPoint();

    // Check if the point is added correctly
    Vector points = foxHuntFrame.points;
    assertEquals(1, points.size());
    TrigPoint point = (TrigPoint)points.get(0);
    assertEquals(45, point.getAngle());
    assertEquals(1.2345, point.getLatitude(), 0.001);
    assertEquals(54.321, point.getLongitude(), 0.001);

  }

  @Test
  public void testAddPointWithExistingPoints() {
    FoxHuntFrame foxHuntFrame = new FoxHuntFrame();

    foxHuntFrame.theta.setText("45");
    foxHuntFrame.lat.setText("1.2345");
    foxHuntFrame.lon.setText("54.321");
    foxHuntFrame.addPoint();

    foxHuntFrame.theta.setText("90");
    foxHuntFrame.lat.setText("2.3456");
    foxHuntFrame.lon.setText("65.432");
    foxHuntFrame.addPoint();

    // Check if the second point is added correctly
    Vector points = foxHuntFrame.points;
    assertEquals(2, points.size());
    TrigPoint point = (TrigPoint)points.get(1);
    assertEquals(90, point.getAngle());
    assertEquals(2.3456, point.getLatitude(), 0.001);
    assertEquals(65.432, point.getLongitude(), 0.001);

    // Check if the fox point is calculated correctly for the second point
    Vector fox = foxHuntFrame.fox;
    assertEquals(1, fox.size());
    TrigPoint foxPoint = (TrigPoint)fox.get(0);
    assertEquals(1.1432, foxPoint.getLatitude(), 0.001);
    assertEquals(40.123, foxPoint.getLongitude(), 0.001);
  }

  @Test
  public void testLoadLandmarks() {
    FoxHuntFrame foxHuntFrame = new FoxHuntFrame();

    // Check if the landmarks are loaded correctly
    assertEquals(3, foxHuntFrame.display.landmarks.size());
    Landmark landmark1 = (Landmark)foxHuntFrame.display.landmarks.get(0);
    assertEquals(12.345, landmark1.getLatitude(), 0.001);
    assertEquals(34.567, landmark1.getLongitude(), 0.001);
    Landmark landmark2 = (Landmark)foxHuntFrame.display.landmarks.get(1);
    assertEquals(23.456, landmark2.getLatitude(), 0.001);
    assertEquals(45.678, landmark2.getLongitude(), 0.001);
    Landmark landmark3 = (Landmark)foxHuntFrame.display.landmarks.get(2);
    assertEquals(34.567, landmark3.getLatitude(), 0.001);
    assertEquals(56.789, landmark3.getLongitude(), 0.001);

  }
}