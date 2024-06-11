import org.apache.commons.math.analysis.Sin;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.MathIllegalStateException;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.random.RandomGenerator;
import org.apache.commons.math.random.Well1024a;
import org.apache.commons.math.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testGetMaxEvaluations() {
        BaseUnivariateRealOptimizer<Sin> optimizer = new UnivariateRealOptimizerMock();
        MultiStartUnivariateRealOptimizer<Sin> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(
                optimizer, 5, new Well1024a());

        assertEquals(0, multiStartOptimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        BaseUnivariateRealOptimizer<Sin> optimizer = new UnivariateRealOptimizerMock();
        MultiStartUnivariateRealOptimizer<Sin> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(
                optimizer, 5, new Well1024a());

        assertEquals(0, multiStartOptimizer.getEvaluations());
    }

    @Test
    public void testSetMaxEvaluations() {
        BaseUnivariateRealOptimizer<Sin> optimizer = new UnivariateRealOptimizerMock();
        MultiStartUnivariateRealOptimizer<Sin> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(
                optimizer, 5, new Well1024a());

        multiStartOptimizer.setMaxEvaluations(100);

        assertEquals(100, multiStartOptimizer.getMaxEvaluations());
    }

    @Test
    public void testOptimize() throws Exception {
        Sin sinFunction = new Sin();
        BaseUnivariateRealOptimizer<Sin> optimizer = new UnivariateRealOptimizerMock();
        MultiStartUnivariateRealOptimizer<Sin> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(
                optimizer, 5, new Well1024a());

        UnivariateRealPointValuePair result = multiStartOptimizer.optimize(sinFunction, GoalType.MINIMIZE, 0.0,
                FastMath.PI, 0.5);

        assertEquals(0.0, result.getPoint(), 1e-15);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testGetOptimaWithoutOptimize() {
        BaseUnivariateRealOptimizer<Sin> optimizer = new UnivariateRealOptimizerMock();
        MultiStartUnivariateRealOptimizer<Sin> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(
                optimizer, 5, new Well1024a());

        multiStartOptimizer.getOptima();
    }

    @Test(expected = ConvergenceException.class)
    public void testOptimizeWithNoConvergence() throws Exception {
        Sin sinFunction = new Sin();
        BaseUnivariateRealOptimizer<Sin> optimizer = new UnivariateRealOptimizerMock(true);
        MultiStartUnivariateRealOptimizer<Sin> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(
                optimizer, 5, new Well1024a());

        multiStartOptimizer.optimize(sinFunction, GoalType.MINIMIZE, 0.0, FastMath.PI, 0.5);
    }

}

class UnivariateRealOptimizerMock implements BaseUnivariateRealOptimizer<Sin> {

    private boolean throwConvergenceException;

    public UnivariateRealOptimizerMock() {
        this.throwConvergenceException = false;
    }

    public UnivariateRealOptimizerMock(boolean throwConvergenceException) {
        this.throwConvergenceException = throwConvergenceException;
    }

    @Override
    public void setConvergenceChecker(ConvergenceChecker<UnivariateRealPointValuePair> checker) {

    }

    @Override
    public ConvergenceChecker<UnivariateRealPointValuePair> getConvergenceChecker() {
        return null;
    }

    @Override
    public int getMaxEvaluations() {
        return 0;
    }

    @Override
    public int getEvaluations() {
        return 0;
    }

    @Override
    public void setMaxEvaluations(int maxEvaluations) {

    }

    @Override
    public UnivariateRealPointValuePair optimize(Sin f, GoalType goal, double min, double max)
            throws FunctionEvaluationException {
        if (throwConvergenceException) {
            throw new ConvergenceException();
        }
        return new UnivariateRealPointValuePair(0.0, f.value(0.0));
    }

}