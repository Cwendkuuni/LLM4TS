package org.apache.commons.math3.geometry.euclidean.twod;

import org.apache.commons.math3.geometry.euclidean.oned.Euclidean1D;
import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.OrientedPoint;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Point2D;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.Side;
import org.apache.commons.math3.util.FastMath;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SubLineTest {

    @Test
    public void testSubLineConstruction() {

        // Test simple constructor
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0.0, 0.5));
        SubLine subLine1 = new SubLine(hyperplane, remainingRegion);

        // Test Line constructor with endpoints
        Vector2D start = new Vector2D(0.0, 0.0);
        Vector2D end = new Vector2D(1.0, 1.0);
        SubLine subLine2 = new SubLine(start, end);

        // Test Segment constructor
        Segment segment = new Segment(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0), new Line(start, end));
        SubLine subLine3 = new SubLine(segment);

        // Check if instances are equal
        Assert.assertEquals(subLine1.getHyperplane(), subLine2.getHyperplane());
        Assert.assertEquals(subLine2.getHyperplane(), subLine3.getHyperplane());

        // Test getSegments method
        List<Segment> segments = subLine3.getSegments();
        Assert.assertNotNull(segments);
        Assert.assertEquals(segments.size(), 1);
        Assert.assertEquals(segments.get(0).getStart(), new Vector2D(0.0, 0.0));
        Assert.assertEquals(segments.get(0).getEnd(), new Vector2D(1.0, 1.0));

        // Test intersection method
        Hyperplane<Euclidean2D> hyperplane1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(2.0, 2.0));
        SubLine subLine4 = new SubLine(new Line(new Vector2D(1.0, 1.0), new Vector2D(3.0, 3.0)), new IntervalsSet(new Interval(0.0, 1.0)));
        Vector2D intersectionPoint = subLine3.intersection(subLine4, true);
        Assert.assertNull(intersectionPoint);

        // Test intersection method with lines intersecting
        Hyperplane<Euclidean2D> hyperplane2 = new Line(new Vector2D(0.0, 1.0), new Vector2D(2.0, 1.0));
        SubLine subLine5 = new SubLine(new Line(new Vector2D(-1.0, 1.0), new Vector2D(3.0, 1.0)), new IntervalsSet(new Interval(-1.0, 1.0)));
        intersectionPoint = subLine3.intersection(subLine5, true);
        Assert.assertEquals(intersectionPoint, new Vector2D(0.0, 1.0));

        // Test intersection method with lines not intersecting
        Hyperplane<Euclidean2D> hyperplane3 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        SubLine subLine6 = new SubLine(new Line(new Vector2D(1.0, 1.0), new Vector2D(3.0, 3.0)), new IntervalsSet(new Interval(0.0, 1.0)));
        intersectionPoint = subLine3.intersection(subLine6, true);
        Assert.assertNull(intersectionPoint);

        // Test intersection method with lines parallel
        Hyperplane<Euclidean2D> hyperplane4 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 2.0));
        SubLine subLine7 = new SubLine(new Line(new Vector2D(1.0, 1.0), new Vector2D(3.0, 3.0)), new IntervalsSet(new Interval(0.0, 1.0)));
        Assert.assertNotNull(subLine3.side(hyperplane4));

        // Test split method
        Hyperplane<Euclidean2D> hyperplane5 = new Line(new Vector2D(1.0, 1.0), new Vector2D(3.0, 3.0));
        SubHyperplane<Euclidean1D> subPlus = new OrientedPoint(new Vector1D(0.5), false).wholeHyperplane();
        SubHyperplane<Euclidean1D> subMinus = new OrientedPoint(new Vector1D(-0.5), true).wholeHyperplane();
        BSPTree<Euclidean1D> splitTree = new BSPTree<>(Boolean.FALSE);

        if (subLine3.getRemainingRegion().isEmpty(splitTree.getPlus())) {
            splitTree = new BSPTree<>(Boolean.FALSE);
        }

        BSPTree<Euclidean1D> plusTree = new BSPTree<>(subPlus, new BSPTree<>(Boolean.FALSE), splitTree.getPlus(), null);
        BSPTree<Euclidean1D> minusTree = new BSPTree<>(subMinus, new BSPTree<>(Boolean.FALSE), splitTree.getMinus(), null);

        List<SubLine> splitSubLines = Arrays.asList(
                new SubLine(new Line(new Vector2D(0.0, 0.0), new Vector2D(0.5)), new IntervalsSet(new Interval(0.0, 0.5))),
                new SubLine(new Line(new Vector2D(0.5, 0.0), new Vector2D(1.0, 0.0)), new IntervalsSet(new Interval(0.5, 1.0))),
                new SubLine(new Line(new Vector2D(1.0, 0.0), new Vector2D(1.5, 0.5)), new IntervalsSet(new Interval(1.0, 1.5))),
                new SubLine(new Line(new Vector2D(1.5, 0.5), new Vector2D(2.0, 1.0)), new IntervalsSet(new Interval(1.5, 2.0))),
                new SubLine(new Line(new Vector2D(2.0, 1.0), new Vector2D(3.0, 3.0)), new IntervalsSet(new Interval(2.0, 3.0)))
        );

        SplitSubHyperplane<Euclidean2D> splitResult = subLine3.split(hyperplane5);
        SubLine splitSubLine1 = splitResult.getSubHyperplane1();
        SubLine splitSubLine2 = splitResult.getSubHyperplane2();

        for (int i = 0; i < splitSubLines.size(); i++) {
            if (splitSubLines.get(i) == splitSubLine1) {
                Assert.assertEquals(splitSubLines.get(i).getHyperplane(), splitSubLine1.getHyperplane());
                Assert.assertEquals(splitSubLines.get(i).getRemainingRegion(), splitSubLine1.getRemainingRegion());
                break;
            }
            if (splitSubLines.get(i) == splitSubLine2) {
                Assert.assertEquals(splitSubLines.get(i).getHyperplane(), splitSubLine2.getHyperplane());
                Assert.assertEquals(splitSubLines.get(i).getRemainingRegion(), splitSubLine2.getRemainingRegion());
                break;
            }
        }

        // Test angle calculation
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        Line line2 = new Line(new Vector2D(1.0, 1.0), new Vector2D(2.0, 2.0));
        double angle = Math.toDegrees(line1.getAngle() - line2.getAngle());
        Assert.assertEquals(angle, 90.0, 1.0);

        // Test Side method
        List<Vector2D> points = new ArrayList<>();
        points.add(new Vector2D(-1.0, -1.0));
        points.add(new Vector2D(0.0, 1.0));
        points.add(new Vector2D(1.0, -1.0));

        for (Vector2D point : points) {
            Side side = line1.side(point);
            if (FastMath.sin(Math.toRadians(angle)) < 0) {
                Assert.assertEquals(side, Side.MINUS);
            } else {
                Assert.assertEquals(side, Side.PLUS);
            }
        }

        // Test offset calculation
        Line line3 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 0.0));
        Line line4 = new Line(new Vector2D(1.0, 0.0), new Vector2D(1.0, 1.0));
        double offset = line1.getOffset(line3);
        Assert.assertEquals(offset, 0.0, 1.0e-10);

        offset = line1.getOffset(line4);
        Assert.assertEquals(offset, -1.0, 1.0e-10);

    }

}