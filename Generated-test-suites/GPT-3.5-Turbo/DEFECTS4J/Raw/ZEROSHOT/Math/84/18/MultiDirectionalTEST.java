import org.junit.Test;
import static org.junit.Assert.*;

public class MultiDirectionalTest {

    @Test
    public void testIterateSimplex() {
        MultiDirectional multiDirectional = new MultiDirectional();
        try {
            multiDirectional.iterateSimplex(null);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testEvaluateNewSimplex() {
        MultiDirectional multiDirectional = new MultiDirectional();
        try {
            multiDirectional.evaluateNewSimplex(null, 0.0, null);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

}