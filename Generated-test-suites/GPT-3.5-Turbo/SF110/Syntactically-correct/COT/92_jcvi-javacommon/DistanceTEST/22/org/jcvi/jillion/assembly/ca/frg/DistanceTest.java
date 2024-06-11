import org.junit.Test;
import static org.junit.Assert.*;

public class DistanceTest {
  
    // Tests for method buildDistance(final int min, final int max, final float mean, final float stdDev)
  
    @Test
    public void testBuildDistance() {
        Distance distance = Distance.buildDistance(2, 5, 3.5f, 0.5f);
        assertEquals(2, distance.getMin());
        assertEquals(5, distance.getMax());
        assertEquals(3.5f, distance.getMean(), 0.00001);
        assertEquals(0.5f, distance.getStdDev(), 0.00001);
    }
  
    // Tests for method buildDistance(final float mean, final float stdDev)
  
    @Test
    public void testBuildDistance_meanStdDev() {
        Distance distance = Distance.buildDistance(3.5f, 0.5f);
        assertEquals(2, distance.getMin());
        assertEquals(5, distance.getMax());
        assertEquals(3.5f, distance.getMean(), 0.00001);
        assertEquals(0.5f, distance.getStdDev(), 0.00001);
    }
  
    // Tests for method calculateDelta(final float stdDev)
  
    @Test
    public void testCalculateDelta() {
        float delta = Distance.calculateDelta(0.5f);
        assertEquals(1.5f, delta, 0.00001);
    }
  
    // Tests for method buildDistance(final int min, final int max)
  
    @Test
    public void testBuildDistance_minMax() {
        Distance distance = Distance.buildDistance(2, 5);
        assertEquals(2, distance.getMin());
        assertEquals(5, distance.getMax());
        assertEquals(3.5f, distance.getMean(), 0.00001);
        assertEquals(0.5f, distance.getStdDev(), 0.00001);
    }
  
    // Tests for method computeStandardDeviation(final int min, final int max)
  
    @Test
    public void testComputeStandardDeviation() {
        float stdDev = Distance.computeStandardDeviation(2, 5);
        assertEquals(0.5f, stdDev, 0.00001);
    }
  
    // Tests for method computeMean(final int min, final int max)
  
    @Test
    public void testComputeMean() {
        float mean = Distance.computeMean(2, 5);
        assertEquals(3.5f, mean, 0.00001);
    }
  
    // Tests for method transformIntoCeleraAssemblerDistance(final Distance distance)
  
    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = new Distance(2, 5, 3.5f, 0.5f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(2, transformedDistance.getMin());
        assertEquals(5, transformedDistance.getMax());
        assertEquals(3.5f, transformedDistance.getMean(), 0.00001);
        assertEquals(0.5f, transformedDistance.getStdDev(), 0.00001);
    }
  
    // Tests for getters
  
    @Test
    public void testGetMean() {
        Distance distance = new Distance(2, 5, 3.5f, 0.5f);
        assertEquals(3.5f, distance.getMean(), 0.00001);
    }
  
    @Test
    public void testGetStdDev() {
        Distance distance = new Distance(2, 5, 3.5f, 0.5f);
        assertEquals(0.5f, distance.getStdDev(), 0.00001);
    }
  
    @Test
    public void testGetMin() {
        Distance distance = new Distance(2, 5, 3.5f, 0.5f);
        assertEquals(2, distance.getMin());
    }
  
    @Test
    public void testGetMax() {
        Distance distance = new Distance(2, 5, 3.5f, 0.5f);
        assertEquals(5, distance.getMax());
    }
  
    // Tests for overridden methods
  
    @Test
    public void testHashCode() {
        Distance distance1 = new Distance(2, 5, 3.5f, 0.5f);
        Distance distance2 = new Distance(2, 5, 3.5f, 0.5f);
        assertEquals(distance1.hashCode(), distance2.hashCode());
    }
  
    @Test
    public void testEquals_sameObject() {
        Distance distance = new Distance(2, 5, 3.5f, 0.5f);
        assertTrue(distance.equals(distance));
    }
  
    @Test
    public void testEquals_nullObject() {
        Distance distance = new Distance(2, 5, 3.5f, 0.5f);
        assertFalse(distance.equals(null));
    }
  
    @Test
    public void testEquals_differentClass() {
        Distance distance = new Distance(2, 5, 3.5f, 0.5f);
        assertFalse(distance.equals("some string"));
    }
  
    @Test
    public void testEquals_equalObjects() {
        Distance distance1 = new Distance(2, 5, 3.5f, 0.5f);
        Distance distance2 = new Distance(2, 5, 3.5f, 0.5f);
        assertTrue(distance1.equals(distance2));
    }
  
    @Test
    public void testEquals_unequalObjects() {
        Distance distance1 = new Distance(2, 5, 3.5f, 0.5f);
        Distance distance2 = new Distance(3, 6, 4.5f, 0.6f);
        assertFalse(distance1.equals(distance2));
    }
  
    @Test
    public void testToString() {
        Distance distance = new Distance(2, 5, 3.5f, 0.5f);
        assertEquals("3.500, std: 0.500", distance.toString());
    }
}