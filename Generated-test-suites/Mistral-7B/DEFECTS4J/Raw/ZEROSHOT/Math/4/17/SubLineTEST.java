import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.geometry.euclidean.twod.*;
import org.apache.commons.math3.geometry.partitioning.*;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SubLineTest {

 @Test
 public void testSubLineConstruction() {
 Vector2D start = new Vector2D(1.0, 1.0);
 Vector2D end = new Vector2D(2.0, 2.0);
 SubLine subLine = new SubLine(start, end);
 Line line = (Line) subLine.getHyperplane();
 IntervalsSet iset = subLine.getRemainingRegion();
 assertEquals(start, line.toSpace(iset.getInf()));
 assertEquals(end, line.toSpace(iset.getSup()));
 }

 @Test
 public void testSubLineSegmentConstruction() {
 Segment segment = new Segment(new Vector2D(1.0, 1.0), new Vector2D(2.0, 2.0));
 SubLine subLine = new SubLine(segment);
 Line line = (Line) subLine.getHyperplane();
 IntervalsSet iset = subLine.getRemainingRegion();
 assertEquals(segment.getStart(), line.toSpace(iset.getInf()));
 assertEquals(segment.getEnd(), line.toSpace(iset.getSup()));
 }

 @Test
 public void testGetSegments() {
 Vector2D start = new Vector2D(0.0, 0.0);
 Vector2D end = new Vector2D(5.0, 5.0);
 Line line = new Line(start, end);
 IntervalsSet iset = new IntervalsSet(new Vector1D(0.0), new Vector1D(1.0),
                                       new Vector1D(3.0), new Vector1D(5.0));
 SubLine subLine = new SubLine(line, iset);
 List<Segment> segments = subLine.getSegments();
 assertEquals(3, segments.size());
 Segment seg1 = segments.get(0);
 assertEquals(start, seg1.getStart());
 Vector2D seg1End = new Vector2D(1.0, 1.0 + (5.0 - 0.0) / 3.0);
 assertEquals(seg1End, seg1.getEnd());
 Segment seg2 = segments.get(1);
 assertEquals(seg1End, seg2.getStart());
 Vector2D seg2End = new Vector2D(3.0, 3.0 + (5.0 - 0.0) / 3.0);
 assertEquals(seg2End, seg2.getEnd());
 Segment seg3 = segments.get(2);
 assertEquals(seg2End, seg3.getStart());
 Vector2D seg3End = new Vector2D(5.0, 5.0);
 assertEquals(seg3End, seg3.getEnd());
 }

 @Test
 public void testIntersection() {
 // intersection of two lines with infinite intervals
 Vector2D start1 = new Vector2D(0.0, 0.0);
 Vector2D end1 = new Vector2D(Double.POSITIVE_INFINITY, 0.0);
 Vector2D start2 = new Vector2D(Double.NEGATIVE_INFINITY, 0.0);
 Vector2D end2 = new Vector2D(0.0, Double.POSITIVE_INFINITY);
 Line line1 = new Line(start1, end1);
 Line line2 = new Line(start2, end2);
 SubLine subLine1 = new SubLine(line1);
 SubLine subLine2 = new SubLine(line2);
 assertNull(subLine1.intersection(subLine2, false));
 // intersection of two lines with finite intervals
 start1 = new Vector2D(0.0, 0.0);
 end1 = new Vector2D(2.0, 2.0);
 start2 = new Vector2D(2.0, 2.0);
 end2 = new Vector2D(4.0, 4.0);
 line1 = new Line(start1, end1);
 line2 = new Line(start2, end2);
 subLine1 = new SubLine(line1);
 subLine2 = new SubLine(line2);
 Vector2D point = subLine1.intersection(subLine2, false);
 assertEquals(new Vector2D(2.0, 2.0), point);
 // intersection of two lines with finite intervals, include endpoints
 start1 = new Vector2D(0.0, 0.0);
 end1 = new Vector2D(2.0, 2.0);
 start2 = new Vector2D(2.0, 2.0);
 end2 = new Vector2D(4.0, 4.0);
 line1 = new Line(start1, end1);
 line2 = new Line(start2, end2);
 subLine1 = new SubLine(line1);
 subLine2 = new SubLine(line2);
 point = subLine1.intersection(subLine2, true);
 assertEquals(new Vector2D(2.0, 2.0), point);
 // intersection of infinite line and a line with finite interval
 start1 = new Vector2D(0.0, 0.0);
 end1 = new Vector2D(Double.POSITIVE_INFINITY, 0.0);
 start2 = new Vector2D(1.0, 2.0);
 end2 = new Vector2D(2.0, 2.0);
 line1 = new Line(start1, end1);
 line2 = new Line(start2, end2);
 subLine1 = new SubLine(line1);
 subLine2 = new SubLine(line2);
 point = subLine1.intersection(subLine2, false);
 assertNull(point);
 // intersection of a line with finite interval and infinite line
 start1 = new Vector2D(0.0, 0.0);
 start2 = new Vector2D(0.0, Double.NEGATIVE_INFINITY);
 end2 = new Vector2D(0.0, Double.POSITIVE_INFINITY);
 line1 = new Line(start1, end1);
 line2 = new Line(start2, end2);
 subLine1 = new SubLine(line1);
 subLine2 = new SubLine(line2);
 point = subLine1.intersection(subLine2, false);
 assertNull(point);
 }

}