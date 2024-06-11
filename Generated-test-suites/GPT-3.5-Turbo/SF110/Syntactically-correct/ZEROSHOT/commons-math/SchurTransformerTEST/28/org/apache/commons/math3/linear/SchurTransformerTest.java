package org.apache.commons.math3.linear;

import org.junit.Test;
import org.junit.Assert;

public class SchurTransformerTest {

	@Test
	public void testGetP() {
		double[][] data = {{1, 2}, {3, 4}};
		RealMatrix matrix = MatrixUtils.createRealMatrix(data);
		SchurTransformer transformer = new SchurTransformer(matrix);
		RealMatrix P = transformer.getP();
		double[][] expectedData = {{0.4159735563815015, -0.9093767094526092}, {-0.9093767094526093, -0.41597355638150107}};
		RealMatrix expectedP = MatrixUtils.createRealMatrix(expectedData);
		Assert.assertEquals(expectedP, P);
	}

	@Test
	public void testGetPT() {
		double[][] data = {{1, 2}, {3, 4}};
		RealMatrix matrix = MatrixUtils.createRealMatrix(data);
		SchurTransformer transformer = new SchurTransformer(matrix);
		RealMatrix PT = transformer.getPT();
		double[][] expectedData = {{0.4159735563815015, -0.9093767094526093}, {-0.9093767094526092, -0.41597355638150107}};
		RealMatrix expectedPT = MatrixUtils.createRealMatrix(expectedData);
		Assert.assertEquals(expectedPT, PT);
	}

	@Test
	public void testGetT() {
		double[][] data = {{1, 2}, {3, 4}};
		RealMatrix matrix = MatrixUtils.createRealMatrix(data);
		SchurTransformer transformer = new SchurTransformer(matrix);
		RealMatrix T = transformer.getT();
		double[][] expectedData = {{10.257447742087774, -6.077454878968029}, {-6.07745487896819, -0.25744774208777065}};
		RealMatrix expectedT = MatrixUtils.createRealMatrix(expectedData);
		Assert.assertEquals(expectedT, T);
	}
}