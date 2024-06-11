import org.junit.Test;
import static org.junit.Assert.*;

public class DistanceTest {
    
    @Test
    public void testBuildDistance_minMaxMeanStdDev() {
        Distance distance = Distance.buildDistance(5, 10, 7.5f, 1.5f);
        assertEquals(5, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(7.5f, distance.getMean(), 0);
        assertEquals(1.5f, distance.getStdDev(), 0);
    }
    
    @Test
    public void testBuildDistance_meanStdDev() {
        Distance distance = Distance.buildDistance(7.5f, 1.5f);
        assertEquals(4, distance.getMin());
        assertEquals(11, distance.getMax());
        assertEquals(7.5f, distance.getMean(), 0);
        assertEquals(1.5f, distance.getStdDev(), 0);
    }
    
    @Test
    public void testBuildDistance_minMax() {
        Distance distance = Distance.buildDistance(5, 10);
        assertEquals(5, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(7.5f, distance.getMean(), 0);
        assertEquals(1.666f, distance.getStdDev(), 0.001f);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance_meanLessThanDelta() {
        Distance distance = Distance.buildDistance(1, 2, 1.5f, 0.1f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(distance.getMin(), transformedDistance.getMin());
        assertEquals(distance.getMax(), transformedDistance.getMax());
        assertEquals(distance.getMean(), transformedDistance.getMean(), 0);
        assertEquals(0.165f, transformedDistance.getStdDev(), 0.001f);
    }
    
    @Test
    public void testTransformIntoCeleraAssemblerDistance_meanGreaterThanDelta() {
        Distance distance = Distance.buildDistance(4, 10, 7f, 1.5f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(distance.getMin(), transformedDistance.getMin());
        assertEquals(distance.getMax(), transformedDistance.getMax());
        assertEquals(distance.getMean(), transformedDistance.getMean(), 0);
        assertEquals(distance.getStdDev(), transformedDistance.getStdDev(), 0);
    }

    @Test
    public void testHashCode() {
        Distance distance = Distance.buildDistance(5, 10, 7.5f, 1.5f);
        int hashCode = distance.hashCode();
        int expectedHashCode = 547869861;
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testEquals_sameObject() {
        Distance distance = Distance.buildDistance(5, 10, 7.5f, 1.5f);
        assertTrue(distance.equals(distance));
    }

    @Test
    public void testEquals_nullObject() {
        Distance distance = Distance.buildDistance(5, 10, 7.5f, 1.5f);
        assertFalse(distance.equals(null));
    }

    @Test
    public void testEquals_differentClass() {
        Distance distance = Distance.buildDistance(5, 10, 7.5f, 1.5f);
        assertFalse(distance.equals("not a Distance object"));
    }

    @Test
    public void testEquals_differentMax() {
        Distance distance1 = Distance.buildDistance(5, 10, 7.5f, 1.5f);
        Distance distance2 = Distance.buildDistance(6, 10, 7.5f, 1.5f);
        assertFalse(distance1.equals(distance2));
    }

    @Test
    public void testEquals_differentMean() {
        Distance distance1 = Distance.buildDistance(5, 10, 7.5f, 1.5f);
        Distance distance2 = Distance.buildDistance(5, 10, 7.6f, 1.5f);
        assertFalse(distance1.equals(distance2));
    }

    @Test
    public void testEquals_differentMin() {
        Distance distance1 = Distance.buildDistance(5, 10, 7.5f, 1.5f);
        Distance distance2 = Distance.buildDistance(5, 9, 7.5f, 1.5f);
        assertFalse(distance1.equals(distance2));
    }

    @Test
    public void testEquals_differentStdDev() {
        Distance distance1 = Distance.buildDistance(5, 10, 7.5f, 1.5f);
        Distance distance2 = Distance.buildDistance(5, 10, 7.5f, 1.6f);
        assertFalse(distance1.equals(distance2));
    }

    @Test
    public void testToString() {
        Distance distance = Distance.buildDistance(5, 10, 7.5f, 1.5f);
        String expectedString = "7.500, std: 1.500";
        assertEquals(expectedString, distance.toString());
    }
}