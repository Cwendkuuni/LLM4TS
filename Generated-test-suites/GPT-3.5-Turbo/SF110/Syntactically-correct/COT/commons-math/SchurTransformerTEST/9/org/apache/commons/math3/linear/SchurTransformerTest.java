import org.junit.Test;
import static org.junit.Assert.*;

public class SchurTransformerTest {

    @Test
    public void testGetP() {
        RealMatrix m = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SchurTransformer st = new SchurTransformer(m);
        RealMatrix p = st.getP();
        assertEquals(1.0, p.getEntry(0, 0), 0.0001);
        assertEquals(2.0, p.getEntry(0, 1), 0.0001);
        assertEquals(3.0, p.getEntry(1, 0), 0.0001);
        assertEquals(4.0, p.getEntry(1, 1), 0.0001);
    }

    @Test
    public void testGetPT() {
        RealMatrix m = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SchurTransformer st = new SchurTransformer(m);
        RealMatrix pt = st.getPT();
        assertEquals(1.0, pt.getEntry(0, 0), 0.0001);
        assertEquals(3.0, pt.getEntry(0, 1), 0.0001);
        assertEquals(2.0, pt.getEntry(1, 0), 0.0001);
        assertEquals(4.0, pt.getEntry(1, 1), 0.0001);
    }

    @Test
    public void testGetT() {
        RealMatrix m = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SchurTransformer st = new SchurTransformer(m);
        RealMatrix t = st.getT();
        assertEquals(1.0, t.getEntry(0, 0), 0.0001);
        assertEquals(2.0, t.getEntry(0, 1), 0.0001);
        assertEquals(0.0, t.getEntry(1, 0), 0.0001);
        assertEquals(3.6158, t.getEntry(1, 1), 0.0001);
    }
}