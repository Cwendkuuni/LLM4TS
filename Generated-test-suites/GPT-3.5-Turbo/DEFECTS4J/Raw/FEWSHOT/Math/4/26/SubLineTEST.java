import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.geometry.euclidean.oned.Euclidean1D;
import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.IntervalsSet;
import org.apache.commons.math3.geometry.euclidean.oned.OrientedPoint;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Region;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region.Location;
import org.apache.commons.math3.geometry.partitioning.Side;
import org.junit.Test;

public class SubLineTest {
	
	@Test
	public void testConstructor() {
		Line line = new Line(new Vector2D(1, 2), new Vector2D(3, 4));
		Region<Euclidean1D> region = new IntervalsSet(1, 2);
		SubLine subLine = new SubLine(line, region);
		
		assertEquals(line, subLine.getHyperplane());
		assertEquals(region, subLine.getRemainingRegion());
	}
	
	@Test
	public void testSubLine() {
		Vector2D start = new Vector2D(1, 2);
		Vector2D end = new Vector2D(3, 4);
		SubLine subLine = new SubLine(start, end);
		
		assertEquals(new Line(start, end), subLine.getHyperplane());
		assertEquals(new IntervalsSet(1, 3), subLine.getRemainingRegion());
	}
	
	@Test
	public void testGetSegments() {
		Vector2D start = new Vector2D(1, 2);
		Vector2D end = new Vector2D(3, 4);
		SubLine subLine = new SubLine(start, end);
		List<Segment> segments = subLine.getSegments();
		
		assertEquals(1, segments.size());
		assertEquals(new Segment(start, end, new Line(start, end)), segments.get(0));
	}
	
	@Test
	public void testIntersection() {
		Vector2D start1 = new Vector2D(1, 2);
		Vector2D end1 = new Vector2D(3, 4);
		SubLine subLine1 = new SubLine(start1, end1);
		
		Vector2D start2 = new Vector2D(2, 3);
		Vector2D end2 = new Vector2D(4, 5);
		SubLine subLine2 = new SubLine(start2, end2);
		
		Vector2D intersectionPoint = subLine1.intersection(subLine2, true);
		assertEquals(new Vector2D(3, 4), intersectionPoint);
		
		Vector2D nullIntersectionPoint = subLine1.intersection(subLine2, false);
		assertNull(nullIntersectionPoint);
	}
	
	@Test
	public void testBuildIntervalSet() {
		Vector2D start = new Vector2D(1, 2);
		Vector2D end = new Vector2D(3, 4);
		IntervalsSet intervalSet = SubLine.buildIntervalSet(start, end);
		
		IntervalsSet expectedIntervalSet = new IntervalsSet(1, 3);
		assertEquals(expectedIntervalSet, intervalSet);
	}
	
	@Test
	public void testBuildNew() {
		Line line = new Line(new Vector2D(1, 2), new Vector2D(3, 4));
		Region<Euclidean1D> region = new IntervalsSet(1, 2);
		SubLine subLine = new SubLine(line, region);
		
		AbstractSubHyperplane<Euclidean2D, Euclidean1D> newSubLine = subLine.buildNew(line, region);
		assertEquals(line, newSubLine.getHyperplane());
		assertEquals(region, newSubLine.getRemainingRegion());
	}
	
	@Test
	public void testSide() {
		Vector2D start1 = new Vector2D(1, 2);
		Vector2D end1 = new Vector2D(3, 4);
		SubLine subLine1 = new SubLine(start1, end1);
		
		Vector2D start2 = new Vector2D(2, 3);
		Vector2D end2 = new Vector2D(4, 5);
		SubLine subLine2 = new SubLine(start2, end2);
		
		Side side = subLine1.side(subLine2);
		assertEquals(Side.PLUS, side);
	}
	
	@Test
	public void testSplit() {
		Vector2D start1 = new Vector2D(1, 2);
		Vector2D end1 = new Vector2D(3, 4);
		SubLine subLine1 = new SubLine(start1, end1);
		
		Vector2D start2 = new Vector2D(2, 3);
		Vector2D end2 = new Vector2D(4, 5);
		SubLine subLine2 = new SubLine(start2, end2);
		
		SplitSubHyperplane<Euclidean2D> splitSubLine = subLine1.split(subLine2);
		
		assertEquals(new SubLine(subLine1.getHyperplane(), new IntervalsSet(1, 2)), splitSubLine.getPlus());
		assertEquals(new SubLine(subLine1.getHyperplane(), new IntervalsSet(2, 3)), splitSubLine.getMinus());
	}
}