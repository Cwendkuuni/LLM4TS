import org.junit.Test;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.Region.Location;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.geometry.euclidean.oned.Euclidean1D;
import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.IntervalsSet;
import org.apache.commons.math3.geometry.euclidean.oned.OrientedPoint;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import java.util.ArrayList;
import java.util.List;


public class SubLineTest {
  
  @Test
  public void testConstructor1() {
    Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(1, 2), new Vector2D(3, 4));
    Region<Euclidean1D> remainingRegion = new IntervalsSet(1, 2);
    SubLine line = new SubLine(hyperplane, remainingRegion);
    assert line.getHyperplane() == hyperplane;
    assert line.getRemainingRegion() == remainingRegion;
  }
  
  @Test
  public void testConstructor2() {
    Vector2D start = new Vector2D(1, 2);
    Vector2D end = new Vector2D(3, 4);
    SubLine line = new SubLine(start, end);
    assert line.getHyperplane() instanceof Line;
    assert line.getRemainingRegion() instanceof IntervalsSet;
    assert ((Line)line.getHyperplane()).getStart() == start;
    assert ((Line)line.getHyperplane()).getEnd() == end;
    assert ((IntervalsSet)line.getRemainingRegion()).getInf() != Double.NEGATIVE_INFINITY;
    assert ((IntervalsSet)line.getRemainingRegion()).getSup() != Double.POSITIVE_INFINITY;
  }
  
  @Test
  public void testConstructor3() {
    Vector2D start = new Vector2D(1, 2);
    Vector2D end = new Vector2D(3, 4);
    Segment segment = new Segment(start, end, new Line(start, end));
    SubLine line = new SubLine(segment);
    assert line.getHyperplane() instanceof Line;
    assert line.getRemainingRegion() instanceof IntervalsSet;
    assert ((Line)line.getHyperplane()).getStart() == start;
    assert ((Line)line.getHyperplane()).getEnd() == end;
    assert ((IntervalsSet)line.getRemainingRegion()).getInf() != Double.NEGATIVE_INFINITY;
    assert ((IntervalsSet)line.getRemainingRegion()).getSup() != Double.POSITIVE_INFINITY;
  }
  
  @Test
  public void testGetSegments() {
    Vector2D start = new Vector2D(1, 2);
    Vector2D end = new Vector2D(3, 4);
    SubLine line = new SubLine(start, end);
    List<Segment> segments = line.getSegments();
    assert segments.size() > 0;
    assert segments.get(0).getStart() == start;
    assert segments.get(0).getEnd() == end;
  }
  
  @Test
  public void testIntersection() {
    Vector2D start1 = new Vector2D(1, 2);
    Vector2D end1 = new Vector2D(3, 4);
    Vector2D start2 = new Vector2D(5, 6);
    Vector2D end2 = new Vector2D(7, 8);
    SubLine line1 = new SubLine(start1, end1);
    SubLine line2 = new SubLine(start2, end2);
    Vector2D intersection = line1.intersection(line2, true);
    assert intersection != null;
    assert intersection.getX() != Double.POSITIVE_INFINITY;
    assert intersection.getX() != Double.NEGATIVE_INFINITY;
  }
  
  @Test
  public void testBuildIntervalSet() {
    Vector2D start = new Vector2D(1, 2);
    Vector2D end = new Vector2D(3, 4);
    IntervalsSet intervalSet = buildIntervalSet(start, end);
    assert intervalSet.getInf() != Double.POSITIVE_INFINITY;
    assert intervalSet.getSup() != Double.NEGATIVE_INFINITY;
  }
  
  @Test
  public void testBuildNew() {
    Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(1, 2), new Vector2D(3, 4));
    Region<Euclidean1D> remainingRegion = new IntervalsSet(1, 2);
    SubLine line = new SubLine(hyperplane, remainingRegion);
    AbstractSubHyperplane<Euclidean2D, Euclidean1D> newLine = line.buildNew(hyperplane, remainingRegion);
    assert newLine.getHyperplane() == hyperplane;
    assert newLine.getRemainingRegion() == remainingRegion;
  }
  
  @Test
  public void testSide() {
    Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(1, 2), new Vector2D(3, 4));
    Region<Euclidean1D> remainingRegion = new IntervalsSet(1, 2);
    SubLine line = new SubLine(hyperplane, remainingRegion);
    Hyperplane<Euclidean2D> otherLine = new Line(new Vector2D(5, 6), new Vector2D(7, 8));
    Side side = line.side(otherLine);
    assert side == Side.PLUS || side == Side.HYPER;
  }
  
  @Test
  public void testSplit() {
    Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(1, 2), new Vector2D(3, 4));
    Region<Euclidean1D> remainingRegion = new IntervalsSet(1, 2);
    SubLine line = new SubLine(hyperplane, remainingRegion);
    Hyperplane<Euclidean2D> otherLine = new Line(new Vector2D(5, 6), new Vector2D(7, 8));
    SplitSubHyperplane<Euclidean2D> split = line.split(otherLine);
    assert split.getPlus() == null || split.getMinus() == null;
  }
}