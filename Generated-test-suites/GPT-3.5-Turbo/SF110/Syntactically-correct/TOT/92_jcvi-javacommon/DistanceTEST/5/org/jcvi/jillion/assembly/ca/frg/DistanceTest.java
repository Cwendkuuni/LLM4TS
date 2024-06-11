import org.junit.Assert;
import org.junit.Test;

public class DistanceTest {

    @Test
    public void testBuildDistanceInts() {
        // Typical use case
        Distance distance1 = Distance.buildDistance(1, 10);
        Assert.assertEquals(1, distance1.getMin());
        Assert.assertEquals(10, distance1.getMax());
        Assert.assertEquals(5.5, distance1.getMean(), 0.01);
        Assert.assertEquals(1.5, distance1.getStdDev(), 0.01);

        // Edge case
        Distance distance2 = Distance.buildDistance(0, 0);
        Assert.assertEquals(0, distance2.getMin());
        Assert.assertEquals(0, distance2.getMax());
        Assert.assertEquals(0, distance2.getMean(), 0.01);
        Assert.assertEquals(0, distance2.getStdDev(), 0.01);

        // Error scenario
        Distance distance3 = Distance.buildDistance(10, 1);
        Assert.assertEquals(1, distance3.getMin());
        Assert.assertEquals(10, distance3.getMax());
        Assert.assertEquals(5.5, distance3.getMean(), 0.01);
        Assert.assertEquals(1.5, distance3.getStdDev(), 0.01);
    }

    @Test
    public void testBuildDistanceFloats() {
        // Typical use case
        Distance distance1 = Distance.buildDistance(5.5f, 1.5f);
        Assert.assertEquals(3, distance1.getMin());
        Assert.assertEquals(8, distance1.getMax());
        Assert.assertEquals(5.5, distance1.getMean(), 0.01);
        Assert.assertEquals(1.5, distance1.getStdDev(), 0.01);
        
        // Edge case
        Distance distance2 = Distance.buildDistance(0.0f, 0.0f);
        Assert.assertEquals(0, distance2.getMin());
        Assert.assertEquals(0, distance2.getMax());
        Assert.assertEquals(0, distance2.getMean(), 0.01);
        Assert.assertEquals(0, distance2.getStdDev(), 0.01);
    }

    @Test
    public void testBuildDistanceMinMax() {
        // Typical use case
        Distance distance1 = Distance.buildDistance(3, 8);
        Assert.assertEquals(3, distance1.getMin());
        Assert.assertEquals(8, distance1.getMax());
        Assert.assertEquals(5.5, distance1.getMean(), 0.01);
        Assert.assertEquals(0.833, distance1.getStdDev(), 0.01);

        // Edge case
        Distance distance2 = Distance.buildDistance(Integer.MAX_VALUE, Integer.MAX_VALUE);
        Assert.assertEquals(Integer.MAX_VALUE, distance2.getMin());
        Assert.assertEquals(Integer.MAX_VALUE, distance2.getMax());
        Assert.assertEquals(Integer.MAX_VALUE, distance2.getMean(), 0.01);
        Assert.assertEquals(0, distance2.getStdDev(), 0.01);

        // Error scenario
        Distance distance3 = Distance.buildDistance(8, 3);
        Assert.assertEquals(3, distance3.getMin());
        Assert.assertEquals(8, distance3.getMax());
        Assert.assertEquals(5.5, distance3.getMean(), 0.01);
        Assert.assertEquals(0.833, distance3.getStdDev(), 0.01);
    }

    @Test
    public void testTransformIntoCeleraAssemblerDistance() {
        // Typical use case
        Distance distance1 = Distance.buildDistance(3, 8);
        Distance transformed1 = Distance.transformIntoCeleraAssemblerDistance(distance1);
        Assert.assertEquals(3, transformed1.getMin());
        Assert.assertEquals(8, transformed1.getMax());
        Assert.assertEquals(5.5, transformed1.getMean(), 0.01);
        Assert.assertEquals(1.495, transformed1.getStdDev(), 0.01);

        // Error scenario
        Distance distance2 = Distance.buildDistance(10, 1);
        Distance transformed2 = Distance.transformIntoCeleraAssemblerDistance(distance2);
        Assert.assertEquals(1, transformed2.getMin());
        Assert.assertEquals(10, transformed2.getMax());
        Assert.assertEquals(5.5, transformed2.getMean(), 0.01);
        Assert.assertEquals(1.495, transformed2.getStdDev(), 0.01);
    }

    @Test
    public void testHashCode() {
        Distance distance1 = Distance.buildDistance(1, 10);
        Distance distance2 = Distance.buildDistance(5.5f, 1.5f);

        Assert.assertEquals(distance1.hashCode(), distance1.hashCode());
        Assert.assertNotEquals(distance1.hashCode(), distance2.hashCode());
    }

    @Test
    public void testEquals() {
        Distance distance1 = Distance.buildDistance(1, 10);
        Distance distance2 = Distance.buildDistance(5.5f, 1.5f);
        Distance distance3 = Distance.buildDistance(1, 10);

        // Reflexive
        Assert.assertTrue(distance1.equals(distance1));

        // Symmetric
        Assert.assertEquals(distance1.equals(distance3), distance3.equals(distance1));

        // Transitive
        Assert.assertEquals(distance1.equals(distance3), distance1.equals(distance2) && distance2.equals(distance3));

        // Consistent
        Assert.assertTrue(distance1.equals(distance3));
        Assert.assertTrue(distance1.equals(distance3));

        // Null and different type
        Assert.assertFalse(distance1.equals(null));
        Assert.assertFalse(distance1.equals(new Object()));
    }

    @Test
    public void testToString() {
        Distance distance1 = Distance.buildDistance(5.5f, 1.5f);
        Assert.assertEquals("5.500, std: 1.500", distance1.toString());
    }
}