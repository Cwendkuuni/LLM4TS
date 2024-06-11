import org.junit.Test;


public class SubLineTest {

  @Test
  public void testConstructor1() {
    Vector2D start = new Vector2D(0, 0);
    Vector2D end = new Vector2D(1, 1);
    SubLine subLine = new SubLine(start, end);
    assertNotNull(subLine);
  }
  
  @Test
  public void testConstructor2() {
    Vector2D start = new Vector2D(0, 0);
    Vector2D end = new Vector2D(1, 1);
    Line line = new Line(start, end);
    IntervalsSet intervalSet = new IntervalsSet(0, 1);
    SubLine subLine = new SubLine(line, intervalSet);
    assertNotNull(subLine);
  }
   
  @Test
  public void testConstructor3() {
    Vector2D start = new Vector2D(0, 0);
    Vector2D end = new Vector2D(1, 1);
    Segment segment = new Segment(start, end, new Line(start, end));
    SubLine subLine = new SubLine(segment);
    assertNotNull(subLine);
  }

  @Test
  public void testGetSegments() {
    Vector2D start = new Vector2D(0, 0);
    Vector2D end = new Vector2D(1, 1);
    SubLine subLine = new SubLine(start, end);
    List<Segment> segments = subLine.getSegments();
    assertEquals(start, segments.get(0).getStart());
    assertEquals(end, segments.get(0).getEnd());
  }

  @Test
  public void testIntersection() {
    Vector2D start1 = new Vector2D(0, 0);
    Vector2D end1 = new Vector2D(1, 1);
    Vector2D start2 = new Vector2D(0, 1);
    Vector2D end2 = new Vector2D(1, 0);
    SubLine subLine1 = new SubLine(start1, end1);
    SubLine subLine2 = new SubLine(start2, end2);
    Vector2D intersectionPoint = subLine1.intersection(subLine2, true);
    assertNotNull(intersectionPoint);
    assertEquals(0.5, intersectionPoint.getX(), 1e-10);
    assertEquals(0.5, intersectionPoint.getY(), 1e-10);
  }

  @Test
  public void testSide() {
    Vector2D start1 = new Vector2D(0, 0);
    Vector2D end1 = new Vector2D(1, 1);
    Vector2D start2 = new Vector2D(0, 1);
    Vector2D end2 = new Vector2D(1, 0);
    SubLine subLine1 = new SubLine(start1, end1);
    SubLine subLine2 = new SubLine(start2, end2);
    assertEquals(Side.HYPER, subLine1.side(subLine2));
  }

  @Test
  public void testSplit() {
    Vector2D start1 = new Vector2D(0, 0);
    Vector2D end1 = new Vector2D(1, 1);
    Vector2D start2 = new Vector2D(0, 1);
    Vector2D end2 = new Vector2D(1, 0);
    SubLine subLine1 = new SubLine(start1, end1);
    SubLine subLine2 = new SubLine(start2, end2);
    SplitSubHyperplane<Euclidean2D> split = subLine1.split(subLine2);
    assertNotNull(split);
    assertNotNull(split.getMinus());
    assertNotNull(split.getPlus());
  }
  
}