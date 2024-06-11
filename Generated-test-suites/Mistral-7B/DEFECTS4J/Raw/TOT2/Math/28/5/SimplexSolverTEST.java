import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.optim.nonlinear.simple.functions.Add;
import org.apache.commons.math3.optim.nonlinear.simple.functions.Constant;
import org.apache.commons.math3.optim.nonlinear.simple.functions.Product;
import org.apache.commons.math3.optim.nonlinear.simple.functions.UnaryMinus;
import org.apache.commons.math3.optim.points.PointValuePair;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;

public class SimplexSolverTest {

    @Test
    public void testSimplexSolverDefaultConstructor() throws UnboundedSolutionException, MaxCountExceededException, NoFeasibleSolutionException {
        LinearFunction function = new Add(new Constant(0), new Product(new Constant(-2), new UnaryMinus(new Variable("x"))));
        LinearConstraint[] constraints = new LinearConstraint[] {new LinearConstraint(new Variable("x"), 2, Relation.LEQ, 5), new LinearConstraint(new Variable("x"), 2, Relation.EQ, 3)};
        GoalType goalType = GoalType.MINIMIZE;
        LinearOptimizer optimizer = new SimplexSolver();
        PointValuePair solution = optimizer.optimize(function, constraints, goalType);
        assertEquals(3d, solution.getPoint().getComponent(0), 0.0);
        assertEquals(-3d, solution.getValue(), 0.0);
    }

    @Test
    public void testSimplexSolverCustomConstructor() throws UnboundedSolutionException, MaxCountExceededException, NoFeasibleSolutionException {
        LinearFunction function = new Add(new Constant(0), new Product(new Constant(-2), new UnaryMinus(new Variable("x"))));
        LinearConstraint[] constraints = new LinearConstraint[] {new LinearConstraint(new Variable("x"), 2, Relation.LEQ, 5), new LinearConstraint(new Variable("x"), 2, Relation.EQ, 3)};
        GoalType goalType = GoalType.MINIMIZE;
        double epsilon = 1e-8;
        int maxUlps = 10;
        LinearOptimizer optimizer = new SimplexSolver(epsilon, maxUlps);
        PointValuePair solution = optimizer.optimize(function, constraints, goalType);
        assertEquals(3d, solution.getPoint().getComponent(0), 0.0);
        assertEquals(-3d, solution.getValue(), 0.0);
    }

    @Test
    public void testUnboundedSolution() throws UnboundedSolutionException, MaxCountExceededException, NoFeasibleSolutionException {
        LinearFunction function = new Add(new Constant(0), new Product(new Constant(-2), new UnaryMinus(new Variable("x"))));
        LinearConstraint[] constraints = new LinearConstraint[0];
        GoalType goalType = GoalType.MINIMIZE;
        try {
            new SimplexSolver().optimize(function, constraints, goalType);
            fail("Expected UnboundedSolutionException");
        } catch (UnboundedSolutionException e) {
            // expected
        }
    }

    @Test
    public void testMaxCountExceededException() throws UnboundedSolutionException, MaxCountExceededException, NoFeasibleSolutionException {
        LinearFunction function = new Add(new Constant(0), new Product(new Constant(-2), new UnaryMinus(new Variable("x"))));
        LinearConstraint[] constraints = new LinearConstraint[0];
        GoalType goalType = GoalType.MINIMIZE;
        SimplexSolver optimizer = new SimplexSolver();
        optimizer.setMaxIterations(0);
        try {
            optimizer.optimize(function, constraints, goalType);
            fail("Expected MaxCountExceededException");
        } catch (MaxCountExceededException e) {
            // expected
        }
    }

    @Test
    public void testNoFeasibleSolution() throws UnboundedSolutionException, MaxCountExceededException, NoFeasibleSolutionException {
        LinearFunction function = new Add(new Constant(0), new Product(new Constant(-2), new UnaryMinus(new Variable("x"))));
        LinearConstraint[] constraints = new LinearConstraint[] {new LinearConstraint(new Variable("x"), 2, Relation.LEQ, 5), new LinearConstraint(new Variable("x"), 2, Relation.EQ, 4)};
        GoalType goalType = GoalType.MINIMIZE;
        try {
            new SimplexSolver().optimize(function, constraints, goalType);
            fail("Expected NoFeasibleSolutionException");
        } catch (NoFeasibleSolutionException e) {
            // expected
        }
    }

}