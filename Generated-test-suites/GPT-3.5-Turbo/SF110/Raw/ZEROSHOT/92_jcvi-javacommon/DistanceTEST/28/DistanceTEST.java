import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DistanceTest {
    
    private Distance distance;

    @Before
    public void setUp() {
        distance = new Distance(1, 10, 5.5f, 2.0f);
    }

    @Test
    public void testBuildDistanceWithMinMaxMeanStdDev() {
        Distance result = Distance.buildDistance(1, 10, 5.5f, 2.0f);
        assertEquals(distance, result);
    }

    @Test
    public void testBuildDistanceWithMeanStdDev() {
        Distance result = Distance.buildDistance(5.5f, 2.0f);
        assertEquals(distance, result);
    }

    @Test
    public void testBuildDistanceWithMinMax() {
        Distance result = Distance.buildDistance(1, 10);
        assertEquals(distance, result);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance transformed = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(distance, transformed);
    }

    @Test
    public void testComputeStandardDeviation() {
        assertEquals(1.5f, Distance.computeStandardDeviation(1, 10), 0.001);
    }

    @Test
    public void testComputeMean() {
        assertEquals(5.5f, Distance.computeMean(1, 10), 0.001);
    }

    @Test
    public void testGetMean() {
        assertEquals(5.5f, distance.getMean(), 0.001);
    }

    @Test
    public void testGetStdDev() {
        assertEquals(2.0f, distance.getStdDev(), 0.001);
    }

    @Test
    public void testGetMin() {
        assertEquals(1, distance.getMin());
    }

    @Test
    public void testGetMax() {
        assertEquals(10, distance.getMax());
    }

    @Test
    public void testHashCode() {
        Distance other = new Distance(1, 10, 5.5f, 2.0f);
        assertEquals(distance.hashCode(), other.hashCode());
    }

    @Test
    public void testEquals() {
        Distance other = new Distance(1, 10, 5.5f, 2.0f);
        assertTrue(distance.equals(other));
    }

    @Test
    public void testToString() {
        String expected = "5.500, std: 2.000";
        assertEquals(expected, distance.toString());
    }
}