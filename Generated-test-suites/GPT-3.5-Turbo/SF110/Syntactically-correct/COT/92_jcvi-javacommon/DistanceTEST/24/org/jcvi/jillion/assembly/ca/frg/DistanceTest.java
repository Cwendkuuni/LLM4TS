
import org.junit.Test;

public class DistanceTest {

    @Test
    public void testBuildDistance_withMinMaxMeanStdDev() {
        Distance distance = Distance.buildDistance(1, 10, 5.0f, 2.0f);
        assert(distance.getMin() == 1);
        assert(distance.getMax() == 10);
        assert(distance.getMean() == 5.0f);
        assert(distance.getStdDev() == 2.0f);
    }

    @Test
    public void testBuildDistance_withMeanStdDev() {
        Distance distance = Distance.buildDistance(5.0f, 2.0f);
        assert(distance.getMin() == 3);
        assert(distance.getMax() == 7);
        assert(distance.getMean() == 5.0f);
        assert(distance.getStdDev() == 2.0f);
    }

    @Test
    public void testBuildDistance_withMinMax() {
        Distance distance = Distance.buildDistance(1, 10);
        assert(distance.getMin() == 1);
        assert(distance.getMax() == 10);
        assert(distance.getMean() == 5.5f);
        assert(distance.getStdDev() == 1.5f);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance_withLowMean() {
        Distance distance = new Distance(1, 10, 1.0f, 0.5f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assert(transformedDistance.getMin() == 1);
        assert(transformedDistance.getMax() == 10);
        assert(transformedDistance.getMean() == 1.0f);
        assert(transformedDistance.getStdDev() == 0.32833332f);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance_withHighMean() {
        Distance distance = new Distance(1, 10, 10.0f, 1.0f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assert(transformedDistance.getMin() == 1);
        assert(transformedDistance.getMax() == 10);
        assert(transformedDistance.getMean() == 10.0f);
        assert(transformedDistance.getStdDev() == 1.0f);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance_withBoundaryMean() {
        Distance distance = new Distance(1, 10, 4.0f, 1.0f);
        Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
        assert(transformedDistance.getMin() == 1);
        assert(transformedDistance.getMax() == 10);
        assert(transformedDistance.getMean() == 4.0f);
        assert(transformedDistance.getStdDev() == 1.0f);
    }

    @Test
    public void testHashCode() {
        Distance distance1 = new Distance(1, 10, 4.0f, 1.0f);
        Distance distance2 = new Distance(1, 10, 4.0f, 1.0f);
        assert(distance1.hashCode() == distance2.hashCode());
    }

    @Test
    public void testEquals_withEqualObjects() {
        Distance distance1 = new Distance(1, 10, 4.0f, 1.0f);
        Distance distance2 = new Distance(1, 10, 4.0f, 1.0f);
        assert(distance1.equals(distance2));
    }

    @Test
    public void testEquals_withDifferentObjects() {
        Distance distance1 = new Distance(1, 10, 4.0f, 1.0f);
        Distance distance2 = new Distance(2, 11, 4.0f, 1.0f);
        assert(!distance1.equals(distance2));
    }

    @Test
    public void testToString() {
        Distance distance = new Distance(1, 10, 4.0f, 1.0f);
        assert(distance.toString().equals("4.000, std: 1.000"));
    }
}
