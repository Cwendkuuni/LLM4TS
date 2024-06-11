import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CombiningEvaluatorTest {
    @Test
    public void testCombiningEvaluatorConstruction() {
        CombiningEvaluator ce = new CombiningEvaluator();
        assertEquals(0, ce.evaluators.size());
    }
}