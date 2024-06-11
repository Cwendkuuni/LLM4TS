import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.awt.image.ImageObserver;
import java.util.Vector;

public class HuntDisplayTest {
    private HuntDisplay huntDisplay;

    @Before
    public void setup() {
        huntDisplay = new HuntDisplay();
    }

    @Test
    public void testAddPoint() {
        // Typical Use Case
        TrigPoint point = new TrigPoint(45, 90, 0, true);
        huntDisplay.addPoint(point);
        Vector points = huntDisplay.points;
        Assert.assertEquals(1, points.size());
        Assert.assertEquals(point, points.elementAt(0));

        // Edge Case
        point = new TrigPoint(0, 180, 270, false);
        huntDisplay.addPoint(point);
        points = huntDisplay.points;
        Assert.assertEquals(2, points.size());
        Assert.assertEquals(point, points.elementAt(1));

        // Error Scenario
        point = null;
        try {
            huntDisplay.addPoint(point);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NullPointerException);
        }
    }

    @Test
    public void testAddLandMark() {
        // Typical Use Case
        ImageIcon icon = new ImageIcon("landmark.png");
        Landmark landmark = new Landmark(45, 90, icon);
        huntDisplay.addLandMark(landmark);
        Vector landmarks = huntDisplay.landmarks;
        Assert.assertEquals(1, landmarks.size());
        Assert.assertEquals(landmark, landmarks.elementAt(0));

        // Edge Case
        landmark = new Landmark(0, 180, icon);
        huntDisplay.addLandMark(landmark);
        landmarks = huntDisplay.landmarks;
        Assert.assertEquals(2, landmarks.size());
        Assert.assertEquals(landmark, landmarks.elementAt(1));

        // Error Scenario
        landmark = null;
        try {
            huntDisplay.addLandMark(landmark);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NullPointerException);
        }
    }

    @Test
    public void testPaintComponent() {
        // Typical Use Case
        Graphics g = new Graphics() {
            @Override
            public Graphics create() {
                return null;
            }

            @Override
            public void translate(int x, int y) {

            }

            @Override
            public Color getColor() {
                return null;
            }

            @Override
            public void setColor(Color c) {

            }

            @Override
            public void setPaintMode() {

            }

            @Override
            public void setXORMode(Color c1) {

            }

            @Override
            public Font getFont() {
                return null;
            }

            @Override
            public void setFont(Font font) {

            }

            @Override
            public FontMetrics getFontMetrics(Font f) {
                return null;
            }

            @Override
            public Rectangle getClipBounds() {
                return null;
            }

            @Override
            public void clipRect(int x, int y, int width, int height) {

            }

            @Override
            public void setClip(int x, int y, int width, int height) {

            }

            @Override
            public Shape getClip() {
                return null;
            }

            @Override
            public void setClip(Shape clip) {

            }

            @Override
            public void copyArea(int x, int y, int width, int height, int dx, int dy) {

            }

            @Override
            public void drawLine(int x1, int y1, int x2, int y2) {

            }

            @Override
            public void fillRect(int x, int y, int width, int height) {

            }

            @Override
            public void clearRect(int x, int y, int width, int height) {

            }

            @Override
            public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {

            }

            @Override
            public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {

            }

            @Override
            public void drawOval(int x, int y, int width, int height) {

            }

            @Override
            public void fillOval(int x, int y, int width, int height) {

            }

            @Override
            public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {

            }

            @Override
            public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {

            }

            @Override
            public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints) {

            }

            @Override
            public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {

            }

            @Override
            public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {

            }

            @Override
            public void drawString(String str, int x, int y) {

            }

            @Override
            public void drawString(AttributedCharacterIterator iterator, int x, int y) {

            }

            @Override
            public boolean drawImage(Image img, int x, int y, ImageObserver observer) {
                return false;
            }

            @Override
            public boolean drawImage(Image img, int x, int y, int width, int height, ImageObserver observer) {
                return false;
            }

            @Override
            public boolean drawImage(Image img, int x, int y, Color bgcolor, ImageObserver observer) {
                return false;
            }

            @Override
            public boolean drawImage(Image img, int x, int y, int width, int height, Color bgcolor, ImageObserver observer) {
                return false;
            }

            @Override
            public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2,
                                     int sy2, ImageObserver observer) {
                return false;
            }

            @Override
            public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2,
                                     int sy2, Color bgcolor, ImageObserver observer) {
                return false;
            }

            @Override
            public void dispose() {

            }
        };
        // Test paintComponent without landmarks
        huntDisplay.paintComponent(g);
        // Test paintComponent with points and landmarks
        TrigPoint point = new TrigPoint(45, 90, 0, true);
        huntDisplay.addPoint(point);
        ImageIcon icon = new ImageIcon("landmark.png");
        Landmark landmark = new Landmark(45, 90, icon);
        huntDisplay.addLandMark(landmark);
        huntDisplay.paintComponent(g);

        // Error Scenario
        g = null;
        try {
            huntDisplay.paintComponent(g);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NullPointerException);
        }
    }
}