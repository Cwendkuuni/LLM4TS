import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class FoldTest {

    private Fold fold;
    private Dataset parent;
    private int[] indices = {1, 2, 3};

    @Before
    public void setUp() {
        parent = new DefaultDataset();
        parent.add(new Instance(new double[]{1.0, 2.0}));
        parent.add(new Instance(new double[]{3.0, 4.0}));
        parent.add(new Instance(new double[]{5.0, 6.0}));
        fold = new Fold(parent, indices);
    }

    @Test
    public void testInstance() {
        assertEquals(new Instance(new double[]{1.0, 2.0}), fold.instance(0));
        assertEquals(new Instance(new double[]{3.0, 4.0}), fold.instance(1));
        assertEquals(new Instance(new double[]{5.0, 6.0}), fold.instance(2));
    }

    @Test
    public void testKNearest() {
        try {
            fold.kNearest(3, new Instance(new double[]{0.0, 0.0}), new EuclideanDistance());
            fail("Expected UnsupportedOperationException for kNearest method");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testSize() {
        assertEquals(3, fold.size());
    }

    // Add more tests for other methods here

}