package org.apache.commons.cli.test;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.RootFinderException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.MathUtils;

public class BaseSecantSolverTest {

    @Test
    public void testRegulaFalsiMethod() {
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6);

        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };

        try {
            double result = solver.solve(10, f, -2.5, 3.5, 0.0);
            assertEquals(-1.0, result, Precision.EPSILON);
        } catch (RootFinderException e) {
            fail("Root finding failed: " + e.getMessage());
        } catch (MaxEvalExceededException e) {
            fail("Maximum number of evaluations exceeded: " + e.getMessage());
        }

        UnivariateRealFunction g = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - x;
            }
        };

        try {
            double result = solver.solve(10, g, -10, 10, 0.0);
            assertEquals(0.0, result, 1e-5);
        } catch (RootFinderException e) {
            fail("Root finding failed: " + e.getMessage());
        } catch (MaxEvalExceededException e) {
            fail("Maximum number of evaluations exceeded: " + e.getMessage());
        }

        UnivariateRealFunction h = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * Math.exp(-x);
            }
        };

        try {
            double result = solver.solve(10, h, 0, 1, 0.0);
            fail("Root finding succeeded but the function does not have a root in [0, 1].");
        } catch (RootFinderException e) {
            // Expected
        } catch (MaxEvalExceededException e) {
            fail("Maximum number of evaluations exceeded: " + e.getMessage());
        }
    }

    @Test
    public void testIllinoisMethod() {
        BaseSecantSolver solver = new IllinoisSolver(1e-6);

        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };

        try {
            double result = solver.solve(10, f, -2.5, 3.5, 0.0);
            assertEquals(-1.0, result, Precision.EPSILON);
        } catch (RootFinderException e) {
            fail("Root finding failed: " + e.getMessage());
        } catch (MaxEvalExceededException e) {
            fail("Maximum number of evaluations exceeded: " + e.getMessage());
        }

        UnivariateRealFunction g = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - x;
            }
        };

        try {
            double result = solver.solve(10, g, -10, 10, 0.0);
            assertEquals(0.0, result, 1e-5);
        } catch (RootFinderException e) {
            fail("Root finding failed: " + e.getMessage());
        } catch (MaxEvalExceededException e) {
            fail("Maximum number of evaluations exceeded: " + e.getMessage());
        }

        UnivariateRealFunction h = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * Math.exp(-x);
            }
        };

        try {
            double result = solver.solve(10, h, 0, 1, 0.0);
            fail("Root finding succeeded but the function does not have a root in [0, 1].");
        } catch (RootFinderException e) {
            // Expected
        } catch (MaxEvalExceededException e) {
            fail("Maximum number of evaluations exceeded: " + e.getMessage());
        }
    }

    @Test
    public void testPegasusMethod() {
        BaseSecantSolver solver = new PegasusSolver(1e-6);

        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };

        try {
            double result = solver.solve(10, f, -2.5, 3.5, 0.0);
            assertEquals(-1.0, result, Precision.EPSILON);
        } catch (RootFinderException e) {
            fail("Root finding failed: " + e.getMessage());
        } catch (MaxEvalExceededException e) {
            fail("Maximum number of evaluations exceeded: " + e.getMessage());
        }

        UnivariateRealFunction g = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - x;
            }
        };

        try {
            double result = solver.solve(10, g, -10, 10, 0.0);
            assertEquals(0.0, result, 1e-5);
        } catch (RootFinderException e) {
            fail("Root finding failed: " + e.getMessage());
        } catch (MaxEvalExceededException e) {
            fail("Maximum number of evaluations exceeded: " + e.getMessage());
        }

        UnivariateRealFunction h = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * Math.exp(-x);
            }
        };

        try {
            double result = solver.solve(10, h, 0, 1, 0.0);
            fail("Root finding succeeded but the function does not have a root in [0, 1].");
        } catch (RootFinderException e) {
            // Expected
        } catch (MaxEvalExceededException e) {
            fail("Maximum number of evaluations exceeded: " + e.getMessage());
        }
    }

    @Test
    public void testAllowedSolution() {
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6);

        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };

        try {
            double result = solver.solve(10, f, -2.5, 3.5, 0.0, AllowedSolution.RIGHT_SIDE);
            assertEquals(-1.0, result, Precision.EPSILON);
        } catch (RootFinderException e) {
            fail("Root finding failed: " + e.getMessage());
        } catch (MaxEvalExceededException e) {
            fail("Maximum number of evaluations exceeded: " + e.getMessage());
        }

        UnivariateRealFunction g = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - x;
            }
        };

        try {
            double result = solver.solve(10, g, -10, 10, 0.0, AllowedSolution.LEFT_SIDE);
            fail("Root finding succeeded but the function does not have a root on the left side of the interval.");
        } catch (RootFinderException e) {
            // Expected
        } catch (MaxEvalExceededException e) {
            fail("Maximum number of evaluations exceeded: " + e.getMessage());
        }

        UnivariateRealFunction h = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * Math.exp(-x);
            }
        };

        try {
            double result = solver.solve(10, h, 0, 1, 0.0, AllowedSolution.BELOW_SIDE);
            fail("Root finding succeeded but the function does not have a root below x = 0.");
        } catch (RootFinderException e) {
            // Expected
        } catch (MaxEvalExceededException e) {
            fail("Maximum number of evaluations exceeded: " + e.getMessage());
        }

        UnivariateRealFunction i = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 2;
            }
        };

        try {
            double result = solver.solve(10, i, -2, 2, 0.0, AllowedSolution.ABOVE_SIDE);
            assertEquals(Math.sqrt(2), result, 1e-2);
        } catch (RootFinderException e) {
            fail("Root finding failed: " + e.getMessage());
        } catch (MaxEvalExceededException e) {
            fail("Maximum number of evaluations exceeded: " + e.getMessage());
        }
    }

    @Test
    public void testMaxEvalExceededException() {
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6);

        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                for (int i = 0; i < 100000; i++) {
                    x += 0.0001;
                }
                return 0;
            }
        };

        try {
            double result = solver.solve(1, f, -1, 1, 0.0);
            fail("Maximum number of evaluations did not exceed the given limit.");
        } catch (MaxEvalExceededException e) {
            // Expected
        } catch (RootFinderException e) {
            fail("Root finding succeeded: " + e.getMessage());
        }
    }
}

class AllowedSolution {
    public static final int ANY_SIDE = 0;
    public static final int LEFT_SIDE = 1;
    public static final int RIGHT_SIDE = 2;
    public static final int BELOW_SIDE = 3;
    public static final int ABOVE_SIDE = 4;
}