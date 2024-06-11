import org.junit.Test;
import static org.junit.Assert.*;

public class DistanceTest {

    @Test
    public void testBuildDistanceWithMinMaxMeanStdDev() {
        Distance distance = Distance.buildDistance(1, 10, 5.5f, 1.0f);
        assertEquals(1, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(5.5f, distance.getMean(), 0.01);
        assertEquals(1.0f, distance.getStdDev(), 0.01);
    }

    @Test
    public void testBuildDistanceWithMeanStdDev() {
        Distance distance = Distance.buildDistance(7.5f, 2.5f);
        assertEquals(5, distance.getMin());
        assertEquals(10, distance.getMax());
        assertEquals(7.5f, distance.getMean(), 0.01);
        assertEquals(2.5f, distance.getStdDev(), 0.01);
    }

    @Test
    public void testBuildDistanceWithMinMax() {
        Distance distance = Distance.buildDistance(2, 8);
        assertEquals(2, distance.getMin());
        assertEquals(8, distance.getMax());
        assertEquals(5.0f, distance.getMean(), 0.01);
        assertEquals(1.0f, distance.getStdDev(), 0.01);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        Distance distance = new Distance(2, 8, 5.5f, 1.0f);
        distance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(2, distance.getMin());
        assertEquals(8, distance.getMax());
        assertEquals(5.5f, distance.getMean(), 0.01);
        assertEquals(1.0f, distance.getStdDev(), 0.01);
    }

    @Test
    public void testEquals() {
        Distance distance1 = new Distance(2, 8, 5.5f, 1.0f);
        Distance distance2 = new Distance(2, 8, 5.5f, 1.0f);
        assertTrue(distance1.equals(distance2));
    }

    @Test
    public void testHashCode() {
        Distance distance = new Distance(2, 8, 5.5f, 1.0f);
        int expectedHashCode = 1761178165;
        assertEquals(expectedHashCode, distance.hashCode());
    }

    @Test
    public void testToString() {
        Distance distance = new Distance(2, 8, 5.5f, 1.0f);
        String expectedString = "5.500, std: 1.000";
        assertEquals(expectedString, distance.toString());
    }  
}