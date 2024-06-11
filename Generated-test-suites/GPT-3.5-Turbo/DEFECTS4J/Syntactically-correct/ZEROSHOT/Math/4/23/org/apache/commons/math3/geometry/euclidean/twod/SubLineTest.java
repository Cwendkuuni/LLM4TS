import static org.junit.Assert.*;
import org.junit.Test;

public class SubLineTest {

	@Test
	public void testGetSegments() {
		Vector2D start = new Vector2D(0, 0);
		Vector2D end = new Vector2D(1, 1);
		SubLine subLine = new SubLine(start, end);
		List<Segment> segments = subLine.getSegments();
		assertEquals(1, segments.size());
		assertEquals(start, segments.get(0).getStart());
		assertEquals(end, segments.get(0).getEnd());
	}

	@Test
	public void testIntersection_includeEndPoints() {
		Vector2D start1 = new Vector2D(0, 0);
		Vector2D end1 = new Vector2D(1, 1);
		SubLine subLine1 = new SubLine(start1, end1);

		Vector2D start2 = new Vector2D(0, 1);
		Vector2D end2 = new Vector2D(1, 0);
		SubLine subLine2 = new SubLine(start2, end2);

		Vector2D intersection = subLine1.intersection(subLine2, true);

		assertNotNull(intersection);
		assertEquals(0.5, intersection.getX(), 0.0001);
		assertEquals(0.5, intersection.getY(), 0.0001);
	}

	@Test
	public void testIntersection_excludeEndPoints() {
		Vector2D start1 = new Vector2D(0, 0);
		Vector2D end1 = new Vector2D(1, 1);
		SubLine subLine1 = new SubLine(start1, end1);

		Vector2D start2 = new Vector2D(0, 1);
		Vector2D end2 = new Vector2D(1, 0);
		SubLine subLine2 = new SubLine(start2, end2);

		Vector2D intersection = subLine1.intersection(subLine2, false);

		assertNull(intersection);
	}

	@Test
	public void testBuildIntervalSet() {
		Vector2D start = new Vector2D(0, 0);
		Vector2D end = new Vector2D(1, 1);
		IntervalsSet intervalSet = SubLine.buildIntervalSet(start, end);
		assertFalse(intervalSet.isEmpty());
		assertEquals(0.0, intervalSet.getInf(), 0.0001);
		assertEquals(1.0, intervalSet.getSup(), 0.0001);
	}

	@Test
	public void testSide() {
		Vector2D start1 = new Vector2D(0, 0);
		Vector2D end1 = new Vector2D(1, 1);
		SubLine subLine1 = new SubLine(start1, end1);

		Vector2D start2 = new Vector2D(0, 1);
		Vector2D end2 = new Vector2D(1, 0);
		SubLine subLine2 = new SubLine(start2, end2);

		assertEquals(Side.HYPER, subLine1.side(subLine2.getHyperplane()));
	}

	@Test
	public void testSplit() {
		Vector2D start1 = new Vector2D(0, 0);
		Vector2D end1 = new Vector2D(1, 1);
		SubLine subLine1 = new SubLine(start1, end1);

		Vector2D start2 = new Vector2D(0, 1);
		Vector2D end2 = new Vector2D(1, 0);
		SubLine subLine2 = new SubLine(start2, end2);

		SplitSubHyperplane<Euclidean2D> split = subLine1.split(subLine2.getHyperplane());

		assertEquals(subLine1, split.getPlus());
		assertEquals(null, split.getMinus());
	}

	@Test
	public void testBuildNew() {
		Vector2D start = new Vector2D(0, 0);
		Vector2D end = new Vector2D(1, 1);
		SubLine subLine = new SubLine(start, end);

		SubLine newSubLine = (SubLine) subLine.buildNew(new Line(new Vector2D(1, 1), new Vector2D(2, 2)),
				new IntervalsSet(0, 1));

		assertEquals(new Line(new Vector2D(1, 1), new Vector2D(2, 2)), newSubLine.getHyperplane());
		assertEquals(new IntervalsSet(0, 1), newSubLine.getRemainingRegion());
	}

}