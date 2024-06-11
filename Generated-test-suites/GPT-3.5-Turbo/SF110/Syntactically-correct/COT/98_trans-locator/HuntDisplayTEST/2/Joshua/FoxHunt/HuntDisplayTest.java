
import javax.swing.*;
import java.util.*;
import java.awt.image.*;
import java.awt.*;

public class HuntDisplay extends JComponent
{
    Vector<TrigPoint> points;
    Vector<Landmark> landmarks;
    Dimension minSize;
    Dimension prefSize;
    
    public HuntDisplay() {
        this.points = new Vector<TrigPoint>();
        this.landmarks = new Vector<Landmark>();
        this.setBackground(Color.WHITE);
        this.minSize = new Dimension(50, 50);
        this.prefSize = new Dimension(200, 200);
    }
    
    @Override
    public Dimension getPreferredSize() {
        return this.prefSize;
    }
    
    @Override
    public Dimension getMinimumSize() {
        return this.minSize;
    }
    
    public void addPoint(final TrigPoint point) {
        this.points.add(new TrigPoint(point.getY() / 90.0, point.getX() / 180.0, point.getTheta(), point.isFox()));
        this.repaint();
    }
    
    public void addLandMark(final Landmark landmark) {
        this.landmarks.add(new Landmark(landmark.getLat() / 90.0, landmark.getLon() / 180.0, landmark.getIcon()));
        this.repaint();
    }
    
    public void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final Color point = Color.BLACK;
        final Color fox = Color.GREEN;
        final TrigPoint[] trigpoints = new TrigPoint[this.points.size()];
        for (int a = 0; a < this.points.size(); ++a) {
            trigpoints[a] = this.points.elementAt(a);
        }
        final Landmark[] landMarks = new Landmark[this.landmarks.size()];
        if (this.landmarks.size() != 0) {
            for (int z = 0; z < this.points.size(); ++z) {
                landMarks[z] = this.landmarks.elementAt(z);
            }
        }
        double trigPointAvgX = 0.0;
        for (int b = 0; b < trigpoints.length; ++b) {
            trigPointAvgX += trigpoints[b].getX();
        }
        trigPointAvgX /= trigpoints.length;
        double trigPointAvgY = 0.0;
        for (int b2 = 0; b2 < trigpoints.length; ++b2) {
            trigPointAvgY += trigpoints[b2].getY();
        }
        trigPointAvgY /= trigpoints.length;
        final double xAdd = 0.5 - trigPointAvgX;
        final double yAdd = 0.5 - trigPointAvgY;
        for (int t = 0; t < trigpoints.length; ++t) {
            if (trigpoints[t].isFox()) {
                g.setColor(fox);
            }
            else {
                g.setColor(point);
            }
            final int X = (int)((trigpoints[t].getX() + xAdd) * this.getWidth());
            final int Y = (int)((trigpoints[t].getY() + yAdd) * this.getHeight());
            g.fillOval(X, Y, 5, 5);
        }
        g.setColor(this.getForeground());
        if (this.landmarks.size() != 0) {
            for (int l = 0; l < landMarks.length; ++l) {
                final int X = (int)((landMarks[l].getLon() + xAdd) * this.getWidth());
                final int Y = (int)((landMarks[l].getLat() + yAdd) * this.getHeight());
                final Image image = landMarks[l].getIcon().getImage();
                g.drawImage(image, X, Y, 10, 10, this.getBackground(), this);
            }
        }
    }
}