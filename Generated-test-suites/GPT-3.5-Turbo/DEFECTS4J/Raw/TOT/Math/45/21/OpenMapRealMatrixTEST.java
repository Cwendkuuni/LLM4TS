import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.linear.OpenMapRealMatrix;
import org.apache.commons.math.linear.RealMatrix;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class OpenMapRealMatrixTest {
	
	private OpenMapRealMatrix matrix;
	private double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
	private double delta = 1e-15;
	
	@Before
	public void setUp() {
		matrix = new OpenMapRealMatrix(data.length, data[0].length);
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				matrix.setEntry(i, j, data[i][j]);
			}
		}
	}
	
	@Test
	public void testGetRowDimension() {
		Assert.assertEquals(data.length, matrix.getRowDimension());
	}
	
	@Test
	public void testGetColumnDimension() {
		Assert.assertEquals(data[0].length, matrix.getColumnDimension());
	}
	
	@Test
	public void testGetEntry() {
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				Assert.assertEquals(data[i][j], matrix.getEntry(i, j), delta);
			}
		}
	}
	
	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testGetEntryWithInvalidRow() {
		matrix.getEntry(data.length, 0);
	}
	
	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testGetEntryWithInvalidColumn() {
		matrix.getEntry(0, data[0].length);
	}
	
	@Test
	public void testSetEntry() {
		double value = 10;
		matrix.setEntry(0, 0, value);
		Assert.assertEquals(value, matrix.getEntry(0, 0), delta);
	}
	
	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testSetEntryWithInvalidRow() {
		matrix.setEntry(data.length, 0, 10);
	}
	
	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testSetEntryWithInvalidColumn() {
		matrix.setEntry(0, data[0].length, 10);
	}
	
	@Test
	public void testAdd() {
		OpenMapRealMatrix m = new OpenMapRealMatrix(data.length, data[0].length);
		double[][] m_data = {{1, 1, 1}, {1, 1, 1}, {1, 1, 1}};
		for (int i = 0; i < m_data.length; i++) {
			for (int j = 0; j < m_data[i].length; j++) {
				m.setEntry(i, j, m_data[i][j]);
			}
		}
		OpenMapRealMatrix result = matrix.add(m);
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				Assert.assertEquals(data[i][j] + m_data[i][j], result.getEntry(i, j), delta);
			}
		}
	}
	
	@Test(expected = DimensionMismatchException.class)
	public void testAddWithInvalidDimension() {
		OpenMapRealMatrix m = new OpenMapRealMatrix(data.length, data[0].length + 1);
		matrix.add(m);
	}
	
	@Test
	public void testSubtract() {
		OpenMapRealMatrix m = new OpenMapRealMatrix(data.length, data[0].length);
		double[][] m_data = {{1, 1, 1}, {1, 1, 1}, {1, 1, 1}};
		for (int i = 0; i < m_data.length; i++) {
			for (int j = 0; j < m_data[i].length; j++) {
				m.setEntry(i, j, m_data[i][j]);
			}
		}
		OpenMapRealMatrix result = matrix.subtract(m);
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				Assert.assertEquals(data[i][j] - m_data[i][j], result.getEntry(i, j), delta);
			}
		}
	}
	
	@Test(expected = DimensionMismatchException.class)
	public void testSubtractWithInvalidDimension() {
		OpenMapRealMatrix m = new OpenMapRealMatrix(data.length, data[0].length + 1);
		matrix.subtract(m);
	}
	
	@Test
	public void testMultiply() {
		RealMatrix m = new OpenMapRealMatrix(data[0].length, data.length);
		double[][] m_data = {{1, 1}, {1, 1}, {1, 1}};
		for (int i = 0; i < m_data.length; i++) {
			for (int j = 0; j < m_data[i].length; j++) {
				m.setEntry(i, j, m_data[i][j]);
			}
		}
		RealMatrix result = matrix.multiply(m);
		Assert.assertEquals(data.length, result.getRowDimension());
		Assert.assertEquals(m_data[0].length, result.getColumnDimension());
		double[][] expected = {{6, 6}, {15, 15}, {24, 24}};
		for (int i = 0; i < expected.length; i++) {
			for (int j = 0; j < expected[i].length; j++) {
				Assert.assertEquals(expected[i][j], result.getEntry(i, j), delta);
			}
		}
	}
	
	@Test(expected = DimensionMismatchException.class)
	public void testMultiplyWithInvalidDimension() {
		RealMatrix m = new OpenMapRealMatrix(data.length, data[0].length + 1);
		matrix.multiply(m);
	}
}