import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.MathIllegalStateException;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.univariate.*;
import org.apache.commons.math.random.RandomGenerator;
import org.junit.Before;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {

    private BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer;
    private RandomGenerator generator;

    @Before
    public void setUp() {
        optimizer = mock(BaseUnivariateRealOptimizer.class);
        generator = mock(RandomGenerator.class);
    }

    @Test
    public void testOptimize_Success() throws Exception {
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(this.optimizer, 5, generator);

        UnivariateRealFunction f = mock(UnivariateRealFunction.class);
        GoalType goal = GoalType.MAXIMIZE;
        double min = 0.0;
        double max = 1.0;

        UnivariateRealPointValuePair pointValuePair1 = new UnivariateRealPointValuePair(0.5, 10.0);
        UnivariateRealPointValuePair pointValuePair2 = new UnivariateRealPointValuePair(0.4, 15.0);
        UnivariateRealPointValuePair pointValuePair3 = new UnivariateRealPointValuePair(0.6, 12.0);

        when(this.optimizer.optimize(f, goal, min, max)).thenReturn(pointValuePair1);
        when(this.optimizer.optimize(f, goal, min, max)).thenReturn(pointValuePair2);
        when(this.optimizer.optimize(f, goal, min, max)).thenReturn(pointValuePair3);

        UnivariateRealPointValuePair result = optimizer.optimize(f, goal, min, max);

        assertEquals(pointValuePair1, result);
        assertEquals(pointValuePair1, optimizer.getOptima()[0]);
        assertEquals(pointValuePair2, optimizer.getOptima()[1]);
        assertEquals(pointValuePair3, optimizer.getOptima()[2]);
    }

    @Test(expected = ConvergenceException.class)
    public void testOptimize_ConvergenceException() throws Exception {
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(this.optimizer, 5, generator);

        UnivariateRealFunction f = mock(UnivariateRealFunction.class);
        GoalType goal = GoalType.MAXIMIZE;
        double min = 0.0;
        double max = 1.0;

        when(this.optimizer.optimize(f, goal, min, max)).thenThrow(new ConvergenceException());

        optimizer.optimize(f, goal, min, max);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testGetOptima_NoOptimumComputed() {
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(this.optimizer, 5, generator);
        optimizer.getOptima();
    }
}