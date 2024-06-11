import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2DSpace;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.geometry.partitioning.SplitSubHyperplane;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.test.OpenMathAssert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class SubLineTest {

    @Test
    public void testSubLineCreation() {
        Euclidean2DSpace space = new Euclidean2DSpace();
        Vector2D start = new Vector2D(1.0, 2.0);
        Vector2D end   = new Vector2D(3.0, 4.0);
        SubLine subLine = new SubLine(start, end);
        Line line = (Line) subLine.getHyperplane();
        assertEquals(start, line.getStartVector());
        assertEquals(end, line.getEndVector());

        List<Segment> segments = subLine.getSegments();
        assertEquals(1, segments.size());
        Segment segment = segments.get(0);
        assertEquals(start, segment.getStart());
        assertEquals(end, segment.getEnd());
        assertEquals(line, segment.getLine());

        Interval interval = ((IntervalsSet) subLine.getRemainingRegion()).get(0);
        assertEquals(start.getX(), interval.getInf(), Precision.SAFE_EPSILON);
        assertEquals(end.getX(), interval.getSup(), Precision.SAFE_EPSILON);

        Hyperplane<Euclidean2D> hyperplane = subLine.getHyperplane();
        Region<Euclidean1D> remainingRegion = subLine.getRemainingRegion();
        assertFalse(remainingRegion.isEmpty());
        assertEquals(1, remainingRegion.getNbIntervals());
        assertEquals(1, ((IntervalsSet) remainingRegion).size());
        Hyperplane<Euclidean1D> hyperplane1D = new Hyperplane<Euclidean1D>(new Line(interval.getInf(), interval.getSup()));
        assertEquals(hyperplane, new SubHyperplane<Euclidean2D, Euclidean1D>(hyperplane1D, remainingRegion));

        Line lineFromHyperplane = new Line(start, end);
        SubLine subLineFromHyperplane = new SubLine(lineFromHyperplane, subLine.getRemainingRegion());
        assertEquals(subLine, subLineFromHyperplane);

        Segment segmentFromHyperplane = new Segment(start, end, lineFromHyperplane);
        SubLine subLineFromSegment = new SubLine(segmentFromHyperplane);
        assertEquals(subLine, subLineFromSegment);
    }

    @Test
    public void testSubLineIntersection() {
        Euclidean2DSpace space = new Euclidean2DSpace();
        Vector2D start1 = new Vector2D(1.0, 2.0);
        Vector2D end1   = new Vector2D(3.0, 4.0);
        Vector2D start2 = new Vector2D(4.0, 2.0);
        Vector2D end2   = new Vector2D(5.0, 4.0);
        SubLine subLine1 = new SubLine(start1, end1);
        SubLine subLine2 = new SubLine(start2, end2);
        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertNull(intersection);

        Vector2D start3 = new Vector2D(1.0, 2.0);
        Vector2D end3   = new Vector2D(3.0, 4.0);
        Vector2D start4 = new Vector2D(3.0, 2.0);
        Vector2D end4   = new Vector2D(5.0, 4.0);
        SubLine subLine3 = new SubLine(start3, end3);
        SubLine subLine4 = new SubLine(start4, end4);
        Vector2D intersection1 = subLine1.intersection(subLine3, true);
        Vector2D expectedIntersection1 = new Vector2D(2.0, 2.0);
        OpenMathAssert.assertEquals(expectedIntersection1, intersection1, Precision.SAFE_EPSILON);

        Vector2D intersection2 = subLine3.intersection(subLine1, true);
        OpenMathAssert.assertEquals(expectedIntersection1, intersection2, Precision.SAFE_EPSILON);

        Vector2D start5 = new Vector2D(1.0, 2.0);
        Vector2D end5   = new Vector2D(3.0, 4.0);
        Vector2D start6 = new Vector2D(1.0, 3.0);
        Vector2D end6   = new Vector2D(3.0, 3.0);
        SubLine subLine5 = new SubLine(start5, end5);
        SubLine subLine6 = new SubLine(start6, end6);
        Vector2D intersection3 = subLine1.intersection(subLine6, true);
        assertNull(intersection3);
    }
}