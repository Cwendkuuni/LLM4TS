
import org.junit.Assert;
import org.junit.Test;

public class DistanceTest {

	@Test
	public void testBuildDistance() {
		Distance distance = Distance.buildDistance(10, 20, 15.0f, 2.0f);
		Assert.assertEquals(10, distance.getMin());
		Assert.assertEquals(20, distance.getMax());
		Assert.assertEquals(15.0f, distance.getMean(), 0.001f);
		Assert.assertEquals(2.0f, distance.getStdDev(), 0.001f);
	}

	@Test
	public void testBuildDistanceWithMeanAndStdDev() {
		Distance distance = Distance.buildDistance(15.0f, 2.0f);
		Assert.assertEquals(13.0f, distance.getMin(), 0.001f);
		Assert.assertEquals(17.0f, distance.getMax(), 0.001f);
		Assert.assertEquals(15.0f, distance.getMean(), 0.001f);
		Assert.assertEquals(2.0f, distance.getStdDev(), 0.001f);
	}

	@Test
	public void testBuildDistanceWithMinAndMax() {
		Distance distance = Distance.buildDistance(10, 20);
		Assert.assertEquals(10, distance.getMin());
		Assert.assertEquals(20, distance.getMax());
		Assert.assertEquals(15.0f, distance.getMean(), 0.001f);
		Assert.assertEquals(1.666f, distance.getStdDev(), 0.001f);
	}

	@Test
	public void testTransformIntoCeleraAssemblerDistance() {
		Distance distance = Distance.buildDistance(15.0f, 2.0f);
		Distance transformedDistance = Distance.transformIntoCeleraAssemblerDistance(distance);
		Assert.assertEquals(15.0f, transformedDistance.getMin(), 0.001f);
		Assert.assertEquals(15.0f, transformedDistance.getMax(), 0.001f);
		Assert.assertEquals(15.0f, transformedDistance.getMean(), 0.001f);
		Assert.assertEquals(4.328f, transformedDistance.getStdDev(), 0.001f);
	}
}
