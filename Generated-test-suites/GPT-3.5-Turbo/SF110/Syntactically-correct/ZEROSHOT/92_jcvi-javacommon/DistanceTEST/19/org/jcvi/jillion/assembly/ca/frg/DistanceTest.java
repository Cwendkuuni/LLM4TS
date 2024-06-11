import org.junit.Test;
import static org.junit.Assert.*;

public class DistanceTest {

    @Test
    public void testBuildDistance_WithMinAndMaxAndMeanAndStdDev_ReturnsDistanceObject() {
        Distance distance = Distance.buildDistance(10, 20, 15.0f, 3.0f);
        assertNotNull(distance);
    }

    @Test
    public void testBuildDistance_WithMeanAndStdDev_ReturnsDistanceObject() {
        Distance distance = Distance.buildDistance(15.0f, 3.0f);
        assertNotNull(distance);
    }

    @Test
    public void testBuildDistance_WithMinAndMax_ReturnsDistanceObject() {
        Distance distance = Distance.buildDistance(10, 20);
        assertNotNull(distance);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance_WithValidDistance_ReturnsTransformedDistance() {
        Distance distance = Distance.buildDistance(10, 20, 15.0f, 3.0f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertNotNull(transformedDistance);
        assertEquals(distance.getMin(), transformedDistance.getMin());
        assertEquals(distance.getMax(), transformedDistance.getMax());
        assertEquals(distance.getMean(), transformedDistance.getMean(), 1.0e-5f);
        assertEquals(distance.getStdDev(), transformedDistance.getStdDev(), 1.0e-5f);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance_WithInvalidDistance_ReturnsSameDistance() {
        Distance distance = Distance.buildDistance(30, 40, 35.0f, 1.0f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertNotNull(transformedDistance);
        assertEquals(distance.getMin(), transformedDistance.getMin());
        assertEquals(distance.getMax(), transformedDistance.getMax());
        assertEquals(distance.getMean(), transformedDistance.getMean(), 1.0e-5f);
        assertEquals(distance.getStdDev(), transformedDistance.getStdDev(), 1.0e-5f);
    }

    @Test
    public void testGetMean_WithDistance_ReturnsMean() {
        Distance distance = Distance.buildDistance(10, 20, 15.0f, 3.0f);
        float mean = distance.getMean();
        assertEquals(15.0f, mean, 1.0e-5f);
    }

    @Test
    public void testGetStdDev_WithDistance_ReturnsStdDev() {
        Distance distance = Distance.buildDistance(10, 20, 15.0f, 3.0f);
        float stdDev = distance.getStdDev();
        assertEquals(3.0f, stdDev, 1.0e-5f);
    }

    @Test
    public void testGetMin_WithDistance_ReturnsMin() {
        Distance distance = Distance.buildDistance(10, 20, 15.0f, 3.0f);
        int min = distance.getMin();
        assertEquals(10, min);
    }

    @Test
    public void testGetMax_WithDistance_ReturnsMax() {
        Distance distance = Distance.buildDistance(10, 20, 15.0f, 3.0f);
        int max = distance.getMax();
        assertEquals(20, max);
    }

    @Test
    public void testHashCode_WithSameObject_ReturnsSameHashCode() {
        Distance distance = Distance.buildDistance(10, 20, 15.0f, 3.0f);
        int hashCode = distance.hashCode();
        assertEquals(distance.hashCode(), hashCode);
    }

    @Test
    public void testHashCode_WithEqualObjects_ReturnsEqualHashCodes() {
        Distance distance1 = Distance.buildDistance(10, 20, 15.0f, 3.0f);
        Distance distance2 = Distance.buildDistance(10, 20, 15.0f, 3.0f);
        assertEquals(distance1.hashCode(), distance2.hashCode());
    }

    @Test
    public void testEquals_WithSameObjects_ReturnsTrue() {
        Distance distance = Distance.buildDistance(10, 20, 15.0f, 3.0f);
        assertEquals(distance, distance);
    }

    @Test
    public void testEquals_WithEqualObjects_ReturnsTrue() {
        Distance distance1 = Distance.buildDistance(10, 20, 15.0f, 3.0f);
        Distance distance2 = Distance.buildDistance(10, 20, 15.0f, 3.0f);
        assertEquals(distance1, distance2);
    }

    @Test
    public void testEquals_WithDifferentObjects_ReturnsFalse() {
        Distance distance1 = Distance.buildDistance(10, 20, 15.0f, 3.0f);
        Distance distance2 = Distance.buildDistance(20, 30, 15.0f, 3.0f);
        assertNotEquals(distance1, distance2);
    }

    @Test
    public void testToString_WithDistance_ReturnsFormattedString() {
        Distance distance = Distance.buildDistance(10, 20, 15.0f, 3.0f);
        String toString = distance.toString();
        String expected = "15.000, std: 3.000";
        assertEquals(expected, toString);
    }
}