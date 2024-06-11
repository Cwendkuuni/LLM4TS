import org.junit.Test;
import org.junit.Assert;

public class PearsonsCorrelationTest {

	@Test
	public void testComputeCorrelationMatrix() {
		double[][] data = {{1, 2, 3}, {2, 4, 6}, {3, 6, 9}};
		RealMatrix expectedMatrix = new BlockRealMatrix(new double[][]{{1, 1, 1}, {1, 1, 1}, {1, 1, 1}});
		PearsonsCorrelation correlation = new PearsonsCorrelation(data);
		RealMatrix actualMatrix = correlation.getCorrelationMatrix();
		Assert.assertEquals(expectedMatrix, actualMatrix);
	}

	@Test
	public void testComputeCorrelationMatrix_SingleColumn() {
		double[][] data = {{1}, {2}, {3}};
		RealMatrix expectedMatrix = new BlockRealMatrix(new double[][]{{1}});
		PearsonsCorrelation correlation = new PearsonsCorrelation(data);
		RealMatrix actualMatrix = correlation.getCorrelationMatrix();
		Assert.assertEquals(expectedMatrix, actualMatrix);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testComputeCorrelationMatrix_InsufficientData() {
		double[][] data = {{1}};
		PearsonsCorrelation correlation = new PearsonsCorrelation(data);
	}

	@Test
	public void testCorrelation() {
		double[] xArray = {1, 2, 3};
		double[] yArray = {2, 4, 6};
		double expectedCorrelation = 1.0;
		PearsonsCorrelation correlation = new PearsonsCorrelation();
		double actualCorrelation = correlation.correlation(xArray, yArray);
		Assert.assertEquals(expectedCorrelation, actualCorrelation, 0.0001);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCorrelation_DifferentLength() {
		double[] xArray = {1, 2, 3};
		double[] yArray = {2, 4};
		PearsonsCorrelation correlation = new PearsonsCorrelation();
		correlation.correlation(xArray, yArray);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCorrelation_InsufficientData() {
		double[] xArray = {1};
		double[] yArray = {2};
		PearsonsCorrelation correlation = new PearsonsCorrelation();
		correlation.correlation(xArray, yArray);
	}

	@Test
	public void testCovarianceToCorrelation() {
		double[][] covarianceData = {{9, 2}, {2, 4}};
		RealMatrix covarianceMatrix = new BlockRealMatrix(covarianceData);
		double[][] expectedData = {{1, 0.4472135954999579}, {0.4472135954999579, 1}};
		RealMatrix expectedMatrix = new BlockRealMatrix(expectedData);
		PearsonsCorrelation correlation = new PearsonsCorrelation();
		RealMatrix actualMatrix = correlation.covarianceToCorrelation(covarianceMatrix);
		Assert.assertEquals(expectedMatrix, actualMatrix);
	}

	@Test
	public void testGetCorrelationStandardErrors() {
		double[][] data = {{1, 2, 3}, {2, 4, 6}, {3, 6, 9}};
		RealMatrix expectedMatrix = new BlockRealMatrix(new double[][]{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}});
		PearsonsCorrelation correlation = new PearsonsCorrelation(data);
		RealMatrix actualMatrix = correlation.getCorrelationStandardErrors();
		Assert.assertEquals(expectedMatrix, actualMatrix);
	}

	@Test
	public void testGetCorrelationPValues() {
		double[][] data = {{1, 2, 3}, {2, 4, 6}, {3, 6, 9}};
		RealMatrix expectedMatrix = new BlockRealMatrix(new double[][]{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}});
		PearsonsCorrelation correlation = new PearsonsCorrelation(data);
		try {
			RealMatrix actualMatrix = correlation.getCorrelationPValues();
			Assert.assertEquals(expectedMatrix, actualMatrix);
		} catch (MathException e) {
			Assert.fail("Exception thrown: " + e.getMessage());
		}
	}

}