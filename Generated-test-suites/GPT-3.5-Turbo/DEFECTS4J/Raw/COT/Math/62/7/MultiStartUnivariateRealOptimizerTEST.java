
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    SetConvergenceCheckerTest.class,
    GetConvergenceCheckerTest.class,
    GetMaxEvaluationsTest.class,
    GetEvaluationsTest.class,
    SetMaxEvaluationsTest.class,
    OptimizeTest.class,
    OptimizeNoConvergenceTest.class,
    GetOptimaTest.class,
    GetOptimaNoOptimumTest.class
})
public class MultiStartUnivariateRealOptimizerTest {
}
