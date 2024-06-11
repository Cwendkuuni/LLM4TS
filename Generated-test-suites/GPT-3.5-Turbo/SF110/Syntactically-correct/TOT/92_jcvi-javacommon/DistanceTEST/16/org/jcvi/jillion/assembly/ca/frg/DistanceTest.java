import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DistanceTest {

    private Distance distance;

    @Before
    public void setup() {
        distance = Distance.buildDistance(1, 10, 5.5f, 2.5f);
    }

    @Test
    public void testBuildDistance_withMinMaxMeanStdDev_returnsDistanceObjectWithCorrectValues() {
        Distance expected = new Distance(1, 10, 5.5f, 2.5f);
        Distance actual = Distance.buildDistance(1, 10, 5.5f, 2.5f);
        assertEquals(expected, actual);
    }

    @Test
    public void testBuildDistance_withMeanStdDev_returnsDistanceObjectWithCorrectValues() {
        Distance expected = new Distance(3, 8, 5.5f, 2.5f);
        Distance actual = Distance.buildDistance(5.5f, 2.5f);
        assertEquals(expected, actual);
    }

    @Test
    public void testBuildDistance_withMinMax_returnsDistanceObjectWithCorrectValues() {
        Distance expected = new Distance(1, 10, 5.5f, 2.5f);
        Distance actual = Distance.buildDistance(1, 10);
        assertEquals(expected, actual);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance_withDistanceLessThanDelta_returnsDistanceObjectWithCorrectValues() {
        Distance expected = new Distance(1, 10, 5.5f, -0.17167f);
        Distance actual = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertEquals(expected, actual);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance_withDistanceGreaterThanDelta_returnsSameDistanceObject() {
        Distance actual = Distance.transformIntoCeleraAssemblerDistance(distance);
        assertTrue(distance == actual);
    }

    @Test
    public void testGetMean_returnsCorrectMeanValue() {
        float expected = 5.5f;
        float actual = distance.getMean();
        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void testGetStdDev_returnsCorrectStdDevValue() {
        float expected = 2.5f;
        float actual = distance.getStdDev();
        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void testGetMin_returnsCorrectMinValue() {
        int expected = 1;
        int actual = distance.getMin();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetMax_returnsCorrectMaxValue() {
        int expected = 10;
        int actual = distance.getMax();
        assertEquals(expected, actual);
    }

    @Test
    public void testHashCode_withEqualObjects_returnsEqualHashCode() {
        Distance distance1 = new Distance(1, 10, 5.5f, 2.5f);
        Distance distance2 = new Distance(1, 10, 5.5f, 2.5f);
        assertEquals(distance1.hashCode(), distance2.hashCode());
    }

    @Test
    public void testEquals_withEqualObjects_returnsTrue() {
        Distance distance1 = new Distance(1, 10, 5.5f, 2.5f);
        Distance distance2 = new Distance(1, 10, 5.5f, 2.5f);
        assertEquals(distance1, distance2);
    }

    @Test
    public void testEquals_withDifferentObjects_returnsFalse() {
        Distance distance1 = new Distance(1, 10, 5.5f, 2.5f);
        Distance distance2 = new Distance(5, 20, 7.5f, 1.5f);
        assertEquals(false, distance1.equals(distance2));
    }

    @Test
    public void testToString_returnsCorrectStringValue() {
        String expected = "5.500, std: 2.500";
        String actual = distance.toString();
        assertEquals(expected, actual);
    }
}