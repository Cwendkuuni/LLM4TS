import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2DSpace;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.Side;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SubLineTest {

    @Test
    public void testSubLineCreation() {
        // Test creating a SubLine from a given Hyperplane and remaining Region
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0, 0), new Vector2D(1, 0)).getHyperplane();
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Vector1D(0), new Vector1D(2));
        SubLine subLine = new SubLine(hyperplane, remainingRegion);

        assertEquals(hyperplane, subLine.getHyperplane());
        assertEquals(remainingRegion, subLine.getRemainingRegion());

        // Test creating a SubLine from two endpoints
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine1 = new SubLine(start, end);

        assertEquals(new Line(start, end), subLine1.getHyperplane());
        assertEquals(new IntervalsSet(start, end), subLine1.getRemainingRegion());

        // Test creating a SubLine from a Segment
        Segment segment = new Segment(new Vector2D(0, 0), new Vector2D(1, 1), new Line(new Vector2D(0, 0), new Vector2D(1, 1)));
        SubLine subLine2 = new SubLine(segment);

        assertEquals(new Line(segment.getStart(), segment.getEnd()), subLine2.getHyperplane());
        assertEquals(new IntervalsSet(segment.getStart(), segment.getEnd()), subLine2.getRemainingRegion());
    }

    @Test
    public void testGetSegments() {
        // Test getting segments of a SubLine
        Line line = new Line(new Vector2D(0, 0), new Vector2D(2, 2));
        SubLine subLine = new SubLine(line, new IntervalsSet(new Vector2D(-1, -1), new Vector2D(3, 3)));

        List<Segment> segments = subLine.getSegments();

        assertEquals(3, segments.size());
        assertEquals(new Segment(new Vector2D(-1, -1), new Vector2D(0, 0), line), segments.get(0));
        assertEquals(new Segment(new Vector2D(0, 0), new Vector2D(1, 1), line), segments.get(1));
        assertEquals(new Segment(new Vector2D(1, 1), new Vector2D(3, 3), line), segments.get(2));
    }

    @Test
    public void testIntersection() {
        // Test intersection of two SubLines
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(1, 1), new Vector2D(2, 2));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Vector2D(0, 0), new Vector2D(1, 1)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Vector2D(1, 1), new Vector2D(2, 2)));

        // Case 1: SubLines do not intersect
        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);
        assertNull(intersectionPoint);

        // Case 2: SubLines intersect at a point
        SubLine subLine3 = new SubLine(line1, new IntervalsSet(new Vector2D(0, 0), new Vector2D(1, 1), new Vector2D(2, 2)));
        intersectionPoint = subLine1.intersection(subLine3, true);
        assertEquals(new Vector2D(1, 1), intersectionPoint);

        // Case 3: SubLines intersect at an endpoint
        SubLine subLine4 = new SubLine(line1, new IntervalsSet(new Vector2D(0, 0)));
        intersectionPoint = subLine1.intersection(subLine4, true);
        assertEquals(new Vector2D(0, 0), intersectionPoint);
    }

    @Test
    public void testIntersectionEdgeCases() {
        // Test intersection edge cases
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(1, 1), new Vector2D(2, 2));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Vector2D(0, 0), new Vector2D(1, 1)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Vector2D(1, 1), new Vector2D(2, 2)));

        // Case 1: SubLines intersect at the infinite point
        SubLine subLine3 = new SubLine(new Line(new Vector2D(0, 0), new Vector2D(3, 3)), new IntervalsSet(new Vector2D(0, 0)));
        Vector2D intersectionPoint = subLine1.intersection(subLine3, true);
        assertNull(intersectionPoint);

        // Case 2: SubLines intersect at a boundary point, but excludeEndPoints is false
        SubLine subLine4 = new SubLine(line1, new IntervalsSet(new Vector2D(0, 0), new Vector2D(1, 1), new Vector2D(2, 2)));
        intersectionPoint = subLine1.intersection(subLine4, false);
        assertNull(intersectionPoint);
    }

    @Test
    public void testIntersectionErrorScenarios() {
        // Test intersection error scenarios
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = null;
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Vector2D(0, 0), new Vector2D(1, 1)));

        try {
            subLine1.intersection(new SubLine(line2, new IntervalsSet(new Vector2D(0, 0), new Vector2D(1, 1))), true);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            subLine1.intersection(subLine1, true);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}