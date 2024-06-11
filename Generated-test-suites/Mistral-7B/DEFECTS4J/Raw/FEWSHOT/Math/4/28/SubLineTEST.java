package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
import org.apache.commons.math3.geometry.euclidean.twod.EuclideanSpace2D;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Point2D;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.Side;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.geometry.partitioning.SplitSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.SubHyperplaneType;
import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane.AbstractSplit;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

import org.apache.commons.commons.math3.geometry.euclidean.twod.SubLine;

public class SubLineTest {

    @Test
    public void testSubLineIntersection() {
        EuclideanSpace2D e2D = new EuclideanSpace2D(2);
        Line line1 = new Line(new Point2D(0, 0), new Point2D(1, 1));
        Line line2 = new Line(new Point2D(1, 1), new Point2D(2, 1));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1), new Interval(1.5, 2)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1, 2)));

        assertEquals(new Point2D(1, 1), subLine1.intersection(subLine2, true));
        assertNull(subLine1.intersection(subLine2, false));

        line1 = new Line(new Point2D(0, 0), new Point2D(1, 1));
        line2 = new Line(new Point2D(0, 1), new Point2D(1, 2));
        subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1), new Interval(1.5, 2)));
        subLine2 = new SubLine(line2, new IntervalsSet(new Interval(0, 1), new Interval(1, 2)));

        assertEquals(new Point2D(1, 1), subLine1.intersection(subLine2, true));
        assertNull(subLine1.intersection(subLine2, false));

        line1 = new Line(new Point2D(0, 0), new Point2D(1, 1));
        line2 = new Line(new Point2D(1, 1), new Point2D(2, 2));
        subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1), new Interval(1.5, 2)));
        subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1, 2)));

        assertNull(subLine1.intersection(subLine2, true));
        assertNull(subLine1.intersection(subLine2, false));

        line1 = new Line(new Point2D(0, 0), new Point2D(1, 1));
        line2 = new Line(new Point2D(1, 1), new Point2D(2, 1));
        subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));
        subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1, 2)));

        assertNull(subLine1.intersection(subLine2, true));
        assertNull(subLine1.intersection(subLine2, false));

        line1 = new Line(new Point2D(0, 0), new Point2D(1, 1));
        line2 = new Line(new Point2D(1, 1), new Point2D(2, 1));
        subLine1 = new SubLine(line1, new IntervalsSet(new Interval(1.5, 2)));
        subLine2 = new SubLine(line2, new IntervalsSet(new Interval(0, 1)));

        assertNull(subLine1.intersection(subLine2, true));
        assertNull(subLine1.intersection(subLine2, false));
    }

    @Test
    public void testSubLineSide() {
        Line line1 = new Line(new Point2D(0, 0), new Point2D(1, 1));
        Line line2 = new Line(new Point2D(1, 1), new Point2D(2, 2));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1), new Interval(1.5, 2)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1, 2)));

        assertEquals(Side.MINUS, subLine1.side(line2));
        assertEquals(Side.PLUS, subLine2.side(line1));

        line1 = new Line(new Point2D(0, 0), new Point2D(1, 1));
        line2 = new Line(new Point2D(1, 1), new Point2D(2, 1));
        subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));
        subLine2 = new SubLine(line2, new IntervalsSet(new Interval(0, 1)));

        assertEquals(Side.MINUS, subLine1.side(line2));
        assertEquals(Side.MINUS, subLine2.side(line1));

        line1 = new Line(new Point2D(0, 0), new Point2D(1, 1));
        line2 = new Line(new Point2D(1, 1), new Point2D(2, 2));
        subLine1 = new SubLine(line1, new IntervalsSet(new Interval(1.5, 2)));
        subLine2 = new SubLine(line2, new IntervalsSet(new Interval(0, 1)));

        assertEquals(Side.PLUS, subLine1.side(line2));
        assertEquals(Side.MINUS, subLine2.side(line1));

        line1 = new Line(new Point2D(0, 0), new Point2D(1, 1));
        line2 = new Line(new Point2D(1, 1), new Point2D(2, 1));
        subLine1 = new SubLine(line1, new IntervalsSet(new Interval(1, 2)));
        subLine2 = new SubLine(line2, new IntervalsSet(new Interval(0, 1)));

        assertEquals(Side.MINUS, subLine1.side(line2));
        assertEquals(Side.MINUS, subLine2.side(line1));

        line1 = new Line(new Point2D(0, 0), new Point2D(1, 1));
        line2 = new Line(new Point2D(1, 1), new Point2D(2, 2));
        subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));
        subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1.5, 2)));

        assertEquals(Side.PLUS, subLine1.side(line2));
        assertEquals(Side.MINUS, subLine2.side(line1));
    }

    @Test
    public void testSubLineSplit() {
        Line line1 = new Line(new Point2D(0, 0), new Point2D(1, 1));
        Line line2 = new Line(new Point2D(1, 1), new Point2D(2, 2));
        Hyperplane<Euclidean2D> hyperplane = new Hyperplane<Euclidean2D>(new Point2D(1, 1), new Vector2D(1, 1));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1), new Interval(1.5, 2)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1, 2)));

        SplitSubHyperplane<Euclidean2D> split = subLine1.split(hyperplane);
        assertEquals(SubHyperplaneType.LINE, split.getType());
        assertEquals(subLine1, split.getLeft());
        assertEquals(subLine2, split.getRight());

        split = subLine2.split(hyperplane);
        assertEquals(SubHyperplaneType.LINE, split.getType());
        assertEquals(subLine2, split.getLeft());
        assertEquals(subLine1, split.getRight());

        split = subLine1.split(line2);
        assertEquals(SubHyperplaneType.PLANE, split.getType());
        assertEquals(null, split.getLeft());
        assertEquals(null, split.getRight());

        split = subLine2.split(line1);
        assertEquals(SubHyperplaneType.PLANE, split.getType());
        assertEquals(null, split.getLeft());
        assertEquals(null, split.getRight());

        split = subLine1.split(line2);
        split = split.getLeft().split(line1);
        assertEquals(SubHyperplaneType.LINE, split.getType());
        assertEquals(subLine1, split.getLeft());
        assertEquals(subLine2, split.getRight());

        split = subLine2.split(line1);
        split = split.getLeft().split(line2);
        assertEquals(SubHyperplaneType.LINE, split.getType());
        assertEquals(subLine2, split.getLeft());
        assertEquals(subLine1, split.getRight());

        split = subLine1.split(line1);
        split = split.getLeft().split(line1);
        split = split.getLeft().split(line1);
        assertEquals(SubHyperplaneType.POINT, split.getType());
        assertEquals(null, split.getLeft());
        assertEquals(null, split.getRight());

        split = subLine2.split(line2);
        split = split.getLeft().split(line2);
        split = split.getLeft().split(line2);
        assertEquals(SubHyperplaneType.POINT, split.getType());
        assertEquals(null, split.getLeft());
        assertEquals(null, split.getRight());
    }

    @Test
    public void testSubLineBuildNew() {
        Line line1 = new Line(new Point2D(0, 0), new Point2D(1, 1));
        Line line2 = new Line(new Point2D(1, 1), new Point2D(2, 2));
        Hyperplane<Euclidean2D> hyperplane1 = new Hyperplane<Euclidean2D>(new Point2D(1, 1), new Vector2D(1, 1));
        Hyperplane<Euclidean2D> hyperplane2 = new Hyperplane<Euclidean2D>(new Point2D(1, 2), new Vector2D(1, 1));
        Region<Euclidean1D> region1 = new IntervalsSet(new Interval(0, 1), new Interval(1.5, 2));
        Region<Euclidean1D> region2 = new IntervalsSet(new Interval(1, 2));

        AbstractSubHyperplane<Euclidean2D, Euclidean1D> subLine1 = SubLine.buildNew(hyperplane1, region1);
        AbstractSubHyperplane<Euclidean2D, Euclidean1D> subLine2 = SubLine.buildNew(hyperplane2, region2);

        assertEquals(SubLine.class, subLine1.getClass());
        assertEquals(SubLine.class, subLine2.getClass());
        assertEquals(line1, ((SubLine) subLine1).getHyperplane());
        assertEquals(region1, ((SubLine) subLine1).getRemainingRegion());
        assertEquals(line2, ((SubLine) subLine2).getHyperplane());
        assertEquals(region2, ((SubLine) subLine2).getRemainingRegion());

        assertNull(((SubLine) subLine1).getSegments().get(0).getStart());
        assertNull(((SubLine) subLine1).getSegments().get(0).getEnd());
        assertEquals(2, ((SubLine) subLine1).getSegments().size());
        assertEquals(new Segment(new Point2D(0, 0), new Point2D(1, 1), line1), ((SubLine) subLine1).getSegments().get(0));
        assertEquals(new Segment(new Point2D(1.5, 1.5), new Point2D(2, 2), line1), ((SubLine) subLine1).getSegments().get(1));

        assertNull(((SubLine) subLine2).getSegments().get(0).getStart());
        assertNull(((SubLine) subLine2).getSegments().get(0).getEnd());
        assertEquals(2, ((SubLine) subLine2).getSegments().size());
        assertEquals(new Segment(new Point2D(1, 1), new Point2D(2, 2), line2), ((SubLine) subLine2).getSegments().get(0));
        assertEquals(new Segment(new Point2D(1, 1), new Point2D(1, 2), line2), ((SubLine) subLine2).getSegments().get(1));
    }
}