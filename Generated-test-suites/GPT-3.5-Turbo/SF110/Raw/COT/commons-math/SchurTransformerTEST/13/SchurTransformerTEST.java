
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

public class SchurTransformerTest {

    @Test
    public void testGetP() {
        RealMatrix matrix = createTestMatrix();
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expected = transformer.getP();
        Assert.assertEquals(expected, transformer.getP());
    }

    @Test
    public void testGetPT() {
        RealMatrix matrix = createTestMatrix();
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expected = transformer.getPT();
        Assert.assertEquals(expected, transformer.getPT());
    }

    @Test
    public void testGetT() {
        RealMatrix matrix = createTestMatrix();
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expected = transformer.getT();
        Assert.assertEquals(expected, transformer.getT());
    }

    @Test
    public void testTransform() {
        RealMatrix matrix = createTestMatrix();
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedT = transformer.getT();
        transformer.transform();
        RealMatrix actualT = transformer.getT();
        Assert.assertEquals(expectedT, actualT);
    }

    private RealMatrix createTestMatrix() {
        // TODO: create a test matrix for testing each method
    }
}

