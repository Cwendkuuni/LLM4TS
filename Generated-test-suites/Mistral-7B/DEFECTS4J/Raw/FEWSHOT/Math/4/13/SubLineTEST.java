package org.apache.commons.cli;

import static org.junit.Assert.*;
import static org.apache.commons.math3.geometry.euclidean.oned.Interval.interval;
import static org.apache.commons.math3.geometry.euclidean.twod.Vector2D.ZERO;
import static org.apache.commons.math3.geometry.euclidean.twod.Vector2D.VECTOR_1;
import static org.apache.commons.math3.geometry.euclidean.twod.Vector2D.VECTOR_2;
import static org.apache.commons.math3.geometry.euclidean.twod.Vector2D.VECTOR_3;
import static org.apache.commons.math3.geometry.euclidean.twod.Vector2D.VECTOR_4;
import static org.apache.commons.math3.geometry.partitioning.Hyperplane.hyperplane;
import static org.apache.commons.math3.geometry.partitioning.Region.Location.INSIDE;
import static org.apache.commons.math3.geometry.partitioning.Region.Location.OUTSIDE;

import org.apache.commons.math3.geometry.euclidean.oned.IntervalSet;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
import org.apache.commons.math3.geometry.euclidean.twod.EuclideanSpace2D;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.OrientedPoint;
import org.apache.commons.math3.geometry.euclidean.twod.Point2D;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.SubHyperplane;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.SplitSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.SubHyperplaneFactory;
import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;

public class SubLineTest {

    private Line line1, line2;
    private SubLine subLine1, subLine2, subLine3, subLine4, subLine5;

    @Before
    public void setUp() {
        line1 = new Line(new Vector2D(1.0, 2.0), new Vector2D(3.0, 4.0));
        line2 = new Line(new Vector2D(5.0, 6.0), new Vector2D(7.0, 8.0));

        subLine1 = new SubLine(line1, interval(0.0, 2.0));
        subLine2 = new SubLine(line1, interval(3.0, 5.0));
        subLine3 = new SubLine(line1, interval(2.0, Double.POSITIVE_INFINITY));
        subLine4 = new SubLine(line1, interval(Double.NEGATIVE_INFINITY, 1.0));
        subLine5 = new SubLine(line2);
    }

    @Test
    public void testGetSegments() {
        List<Segment> segments1 = subLine1.getSegments();
        List<Segment> segments2 = subLine2.getSegments();
        List<Segment> segments3 = subLine3.getSegments();
        List<Segment> segments4 = subLine4.getSegments();
        List<Segment> segments5 = subLine5.getSegments();

        assertEquals(1, segments1.size());
        assertEquals(1, segments2.size());
        assertEquals(0, segments3.size());
        assertEquals(1, segments4.size());
        assertEquals(0, segments5.size());

        assertEquals(VECTOR_1, segments1.get(0).getStart());
        assertEquals(VECTOR_2, segments1.get(0).getEnd());
        assertEquals(VECTOR_2, segments2.get(0).getStart());
        assertEquals(VECTOR_3, segments2.get(0).getEnd());
        assertNull(segments3.get(0));
        assertEquals(VECTOR_4, segments4.get(0).getStart());
        assertNull(segments4.get(0).getEnd());
    }

    @Test
    public void testIntersection() {
        assertNull(subLine1.intersection(subLine1, false));
        assertNull(subLine1.intersection(subLine2, false));
        assertNull(subLine1.intersection(subLine3, false));
        assertNull(subLine1.intersection(subLine4, false));
        assertNull(subLine1.intersection(subLine5, false));

        Vector2D intersection = subLine2.intersection(subLine1, false);
        assertEquals(VECTOR_2, intersection);

        intersection = subLine2.intersection(subLine2, false);
        assertNull(intersection);

        intersection = subLine2.intersection(subLine3, false);
        assertNull(intersection);

        intersection = subLine2.intersection(subLine4, false);
        assertNull(intersection);

        intersection = subLine2.intersection(subLine5, false);
        assertNull(intersection);

        intersection = subLine3.intersection(subLine1, false);
        assertNull(intersection);

        intersection = subLine3.intersection(subLine2, false);
        assertNull(intersection);

        intersection = subLine3.intersection(subLine3, false);
        assertNull(intersection);

        intersection = subLine3.intersection(subLine4, false);
        assertNull(intersection);

        intersection = subLine3.intersection(subLine5, false);
        assertNull(intersection);

        intersection = subLine4.intersection(subLine1, false);
        assertNull(intersection);

        intersection = subLine4.intersection(subLine2, false);
        assertNull(intersection);

        intersection = subLine4.intersection(subLine3, false);
        assertNull(intersection);

        intersection = subLine4.intersection(subLine4, false);
        assertNull(intersection);

        intersection = subLine4.intersection(subLine5, false);
        assertNull(intersection);

        intersection = subLine5.intersection(subLine1, false);
        assertNull(intersection);

        intersection = subLine5.intersection(subLine2, false);
        assertNull(intersection);

        intersection = subLine5.intersection(subLine3, false);
        assertNull(intersection);

        intersection = subLine5.intersection(subLine4, false);
        assertNull(intersection);

        intersection = subLine5.intersection(subLine5, false);
        assertNull(intersection);

        // test intersection with includeEndPoints = true

        intersection = subLine1.intersection(subLine1, true);
        assertNull(intersection);

        intersection = subLine1.intersection(subLine2, true);
        assertNull(intersection);

        intersection = subLine1.intersection(subLine3, true);
        assertNull(intersection);

        intersection = subLine1.intersection(subLine4, true);
        assertNull(intersection);

        intersection = subLine1.intersection(subLine5, true);
        assertNull(intersection);

        intersection = subLine2.intersection(subLine1, true);
        assertEquals(VECTOR_2, intersection);

        intersection = subLine2.intersection(subLine2, true);
        assertNull(intersection);

        intersection = subLine2.intersection(subLine3, true);
        assertNull(intersection);

        intersection = subLine2.intersection(subLine4, true);
        assertNull(intersection);

        intersection = subLine2.intersection(subLine5, true);
        assertNull(intersection);

        intersection = subLine3.intersection(subLine1, true);
        assertNull(intersection);

        intersection = subLine3.intersection(subLine2, true);
        assertNull(intersection);

        intersection = subLine3.intersection(subLine3, true);
        assertNull(intersection);

        intersection = subLine3.intersection(subLine4, true);
        assertNull(intersection);

        intersection = subLine3.intersection(subLine5, true);
        assertNull(intersection);

        intersection = subLine4.intersection(subLine1, true);
        assertNull(intersection);

        intersection = subLine4.intersection(subLine2, true);
        assertNull(intersection);

        intersection = subLine4.intersection(subLine3, true);
        assertNull(intersection);

        intersection = subLine4.intersection(subLine4, true);
        assertNull(intersection);

        intersection = subLine4.intersection(subLine5, true);
        assertNull(intersection);

        intersection = subLine5.intersection(subLine1, true);
        assertNull(intersection);

        intersection = subLine5.intersection(subLine2, true);
        assertNull(intersection);

        intersection = subLine5.intersection(subLine3, true);
        assertNull(intersection);

        intersection = subLine5.intersection(subLine4, true);
        assertNull(intersection);

        intersection = subLine5.intersection(subLine5, true);
        assertNull(intersection);
    }

    @Test
    public void testIntersectionWithParallelsLines() {
        Line line1P = new Line(new Vector2D(1.0, 2.0), new Vector2D(5.0, 6.0));
        Line line1M = new Line(new Vector2D(1.0, 2.0), new Vector2D(5.0, 6.0));
        line1M.setMirrored(true);
        SubLine subLine1P = new SubLine(line1P);
        SubLine subLine1M = new SubLine(line1M);

        assertNull(subLine1P.intersection(subLine1M, false));
        assertNull(subLine1P.intersection(subLine1M, true));
    }

    @Test
    public void testSide() {
        SubHyperplane<Euclidean2D> hyperplane1 = hyperplane(line1);
        SubHyperplane<Euclidean2D> hyperplane2 = hyperplane(line2);

        Side side1 = subLine1.side(hyperplane1);
        Side side2 = subLine1.side(hyperplane2);

        assertEquals(Side.MINUS, side1);
        assertEquals(Side.PLUS, side2);
    }

    @Test
    public void testSplit() {
        Hyperplane<Euclidean2D> newHyperplane1 = hyperplane(new Line(new Vector2D(1.0, 2.0), new Vector2D(3.0, 3.0)));
        Hyperplane<Euclidean2D> newHyperplane2 = hyperplane(new Line(new Vector2D(3.0, 3.0), new Vector2D(5.0, 3.0)));

        SplitSubHyperplane<Euclidean2D> splitSubHyperplane1 = subLine1.split(newHyperplane1);
        SplitSubHyperplane<Euclidean2D> splitSubHyperplane2 = subLine1.split(newHyperplane2);

        assertEquals(null, splitSubHyperplane1.getFirst());
        assertEquals(subLine2, splitSubHyperplane1.getSecond());

        assertEquals(subLine1, splitSubHyperplane2.getFirst());
        assertEquals(null, splitSubHyperplane2.getSecond());

        SubHyperplane<Euclidean1D> newSubHyperplane1 = subHyperplaneFactory.create(new Hyperplane<Euclidean1D>(0.0));
        SubHyperplane<Euclidean1D> newSubHyperplane2 = subHyperplaneFactory.create(new Hyperplane<Euclidean1D>(1.0));

        Region<Euclidean1D> region1 = new IntervalsSet(interval(0.0, 2.0), interval(3.0, 5.0));

        SplitSubHyperplane<Euclidean2D> splitSubHyperplane3 = subLine1.split(newHyperplane(new Line(ZERO, new Vector1D(1.0))));

        assertEquals(subLine3, splitSubHyperplane3.getFirst());
        assertEquals(subLine4, splitSubHyperplane3.getSecond());
    }
}