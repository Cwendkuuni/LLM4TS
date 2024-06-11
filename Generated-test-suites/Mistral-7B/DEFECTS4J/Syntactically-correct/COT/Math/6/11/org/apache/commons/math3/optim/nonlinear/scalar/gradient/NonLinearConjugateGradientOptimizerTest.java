
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.analysis.function.Add;
import org.apache.commons.math3.analysis.function.ConstantFunction;
import org.apache.commons.math3.analysis.function.Divide;
import org.apache.commons.math3.analysis.function.Multiply;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.initializers.ArrayRealVectorInitializer;
import org.apache.commons.math3.optim.initializers.RealVectorValue;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.NonLinearConjugateGradientOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.Preconditioner;
import org.apache.commons.math3.optim.nonlinear.scalar.convergence.ConvergenceChecker;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.IdentityPreconditioner;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LUPreconditioner;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.CholeskyPreconditioner;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.QRPreconditioner;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.DiagonalPreconditioner;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.SparseLUPreconditioner;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.SparseCholeskyPreconditioner;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.SparseQRPreconditioner;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.SparseDiagonalPreconditioner;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.SparseLUSolver;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.SparseCholeskySolver;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.SparseQRSolver;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.SparseLUDecomposition;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.InitialGuess.RandomInitialGuess;

public class NonLinearConjugateGradientOptimizerTest {
    private NonLinearConjugateGradientOptimizer optimizer;
    private ConjugateGradientFormula updateFormula;
    private Preconditioner preconditioner;
    private UnivariateOptimizer optimizerWithoutPreconditioner;
    private ConvergenceChecker<PointValuePair> convergenceChecker;
    private UnivariateFunction function;

    @Before
    public void setUp() {
        updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        preconditioner = new IdentityPreconditioner();
        optimizer = new NonLinearConjugateGradientOptimizer(updateFormula,
                new SimpleValueChecker(),
                new BrentSolver(),
                preconditioner);
        optimizerWithoutPreconditioner = new NonLinearConjugateGradientOptimizer(updateFormula,
                new SimpleValueChecker(),
                new BrentSolver());
        convergenceChecker = new SimpleValueChecker(1e-6, 100);
        function = new Add(new Multiply(new Multiply(new ConstantFunction(1), new ConstantFunction(x -> x)), new ConstantFunction(2)),
                new Divide(new Multiply(new ConstantFunction(x -> Math.sin(x)), new ConstantFunction(x -> Math.cos(x))),
                        new ConstantFunction(2)));
    }

    @Test
    public void testEmptyConstructor() {
        assertThrows(IllegalArgumentException.class, () -> new NonLinearConjugateGradientOptimizer(null));
    }

    @Test
    public void testConstructorWithUpdateFormula() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula);
        assertSame(updateFormula, optimizer.getUpdateFormula());
        assertNull(optimizer.getPreconditioner());
        assertNull(optimizer.getSolver());
        assertNull(optimizer.getInitialStep());
        assertNull(optimizer.getPoint());
    }

    @Test
    public void testConstructorWithUpdateFormulaCheckerSolverPreconditioner() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula,
                new SimpleValueChecker(),
                new BrentSolver(),
                preconditioner);
        assertSame(updateFormula, optimizer.getUpdateFormula());
        assertSame(preconditioner, optimizer.getPreconditioner());
        assertSame(new BrentSolver(), optimizer.getSolver());
        assertNull(optimizer.getInitialStep());
        assertNull(optimizer.getPoint());
    }

    @Test
    public void testSetInitialStep() {
        optimizer.setInitialStep(0.5);
        assertEquals(0.5, optimizer.getInitialStep(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetInitialStepNegative() {
        optimizer.setInitialStep(-1.0);
    }

    @Test
    public void testDoOptimizeWithIdentityPreconditioner() {
        double[] startPoint = {1.0, 2.0};
        PointValuePair result = optimizer.optimize(function, startPoint);
        assertEquals(0.0, result.getValue(), 1e-6);
        assertArrayEquals(new double[]{0.0, 0.0}, result.getPoint(), 1e-6);
    }

    @Test
    public void testDoOptimizeWithEmptyPreconditioner() {
        preconditioner = new DiagonalPreconditioner();
        double[] startPoint = {1.0, 2.0};
        PointValuePair result = optimizer.optimize(function, startPoint);
        assertArrayEquals(new double[]{0.0, 0.0}, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test
    public void testDoOptimizeWithLUPreconditioner() {
        preconditioner = new LUPreconditioner();
        double[] startPoint = {1.0, 2.0};
        PointValuePair result = optimizer.optimize(function, startPoint);
        assertArrayEquals(new double[]{0.0, 0.0}, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test
    public void testDoOptimizeWithCholeskyPreconditioner() {
        preconditioner = new CholeskyPreconditioner();
        double[] startPoint = {1.0, 2.0};
        PointValuePair result = optimizer.optimize(function, startPoint);
        assertArrayEquals(new double[]{0.0, 0.0}, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test
    public void testDoOptimizeWithQRPreconditioner() {
        preconditioner = new QRPreconditioner();
        double[] startPoint = {1.0, 2.0};
        PointValuePair result = optimizer.optimize(function, startPoint);
        assertArrayEquals(new double[]{0.0, 0.0}, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test
    public void testDoOptimizeWithSparseLUPreconditioner() {
        preconditioner = new SparseLUPreconditioner();
        double[] startPoint = {1.0, 2.0};
        PointValuePair result = optimizer.optimize(function, startPoint);
        assertArrayEquals(new double[]{0.0, 0.0}, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test
    public void testDoOptimizeWithSparseCholeskyPreconditioner() {
        preconditioner = new SparseCholeskyPreconditioner();
        double[] startPoint = {1.0, 2.0};
        PointValuePair result = optimizer.optimize(function, startPoint);
        assertArrayEquals(new double[]{0.0, 0.0}, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test
    public void testDoOptimizeWithSparseQRPreconditioner() {
        preconditioner = new SparseQRPreconditioner();
        double[] startPoint = {1.0, 2.0};
        PointValuePair result = optimizer.optimize(function, startPoint);
        assertArrayEquals(new double[]{0.0, 0.0}, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test
    public void testDoOptimizeWithSparseDiagonalPreconditioner() {
        preconditioner = new SparseDiagonalPreconditioner();
        double[] startPoint = {1.0, 2.0};
        PointValuePair result = optimizer.optimize(function, startPoint);
        assertArrayEquals(new double[]{0.0, 0.0}, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test
    public void testDoOptimizeWithoutPreconditioner() {
        optimizerWithoutPreconditioner.setInitialStep(0.5);
        double[] startPoint = {1.0, 2.0};
        PointValuePair result = optimizerWithoutPreconditioner.optimize(function, startPoint);
        assertArrayEquals(new double[]{0.0, 0.0}, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test
    public void testDoOptimizeWithDifferentGoalType() {
        optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, new SimpleValueChecker(), new BrentSolver(), preconditioner);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        double[] startPoint = {1.0, 2.0};
        PointValuePair result = optimizer.optimize(function.negate(), startPoint);
        assertArrayEquals(new double[]{0.0, 0.0}, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testDoOptimizeThrowsExceptionWhenNotBracketingOptimum() {
        optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, new SimpleValueChecker(), new BrentSolver(), preconditioner);
        double[] startPoint = {1.0, 1.0};
        PointValuePair result = optimizer.optimize(function, startPoint);
        fail("Exception expected");
    }

    @Test
    public void testOptimizeWithInitialGuess() {
        optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, new SimpleValueChecker(), new BrentSolver(), preconditioner);
        InitialGuess initialGuess = new ArrayRealVectorInitializer(2);
        double[] startPoint = initialGuess.getInitialVector();
        PointValuePair result = optimizer.optimize(function, startPoint);
        assertArrayEquals(new double[]{0.0, 0.0}, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test
    public void testOptimizeWithRandomInitialGuess() {
        optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, new SimpleValueChecker(), new BrentSolver(), preconditioner);
        InitialGuess initialGuess = new RandomInitialGuess(2, new RealVectorValue(0.0, 0.0));
        double[] startPoint = initialGuess.getInitialVector();
        PointValuePair result = optimizer.optimize(function, startPoint);
        assertArrayEquals(new double[]{0.0, 0.0}, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }
}
