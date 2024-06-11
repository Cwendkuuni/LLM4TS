package org.apache.commons.math.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.MaxEvaluationsExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealFunction;
import org.apache.commons.math3.optim.SimpleValueFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.PointValuePairFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.RealVector;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.NelderMeadSimplex;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.NelderMeadSimplexSearch;
import org.apache.commons.math3.optim.point.PointValuePairComparator;
import org.junit.Test;

public class MultiDirectionalTest {

    @Test
    public void testMultiDirectional() throws Exception {
        final MultiDirectional optimizer = new MultiDirectional();
        final RealFunction function = new SimpleValueFunction("TestFunction", x -> Math.pow(Math.sin(x), 2));

        final ObjectiveFunction objectiveFunction = new PointValuePairFunction("ObjectiveFunction", new RealFunction("f", x -> function.value(x)) {
            @Override
            public PointValuePair value(RealVector x) {
                return new PointValuePair(x, this.f.value(x[0]));
            }
        });

        final NelderMeadSimplexSearch search = new NelderMeadSimplexSearch(objectiveFunction, GoalType.MINIMIZE, 5, new RealVector(1), null);
        search.setMaximumEvaluations(100);

        final NelderMeadSimplex simplex = new NelderMeadSimplex(5);
        for (int i = 0; i < simplex.getNumberOfPoints(); i++) {
            simplex.setPoint(i, new RealVector(1).setEntry(0, i * 0.1));
        }

        search.setInitialSimplex(simplex);
        search.setMaxIterationsWithoutImprovement(10);
        search.setCheckConvergence(new RealConvergenceChecker(1.0E-6, 1.0E-5, 1.0E-5));

        final PointValuePair result = search.optimize();

        assertEquals("TestFunction minimum not found", -1.0, result.getValue(), 1.0E-6);
        assertEquals("x value not correct", -1.0, result.getPoint()[0], 1.0E-6);

        assertArrayEquals("Simplex not updated", new double[]{-1.0}, simplex.getPoint(0).toArray(), 1.0E-6);
        assertArrayEquals("Simplex not updated", new double[]{0.0}, simplex.getPoint(1).toArray(), 1.0E-6);
        assertArrayEquals("Simplex not updated", new double[]{0.5}, simplex.getPoint(2).toArray(), 1.0E-6);
        assertArrayEquals("Simplex not updated", new double[]{1.0}, simplex.getPoint(3).toArray(), 1.0E-6);
        assertArrayEquals("Simplex not updated", new double[]{1.5}, simplex.getPoint(4).toArray(), 1.0E-6);

        // Test with an illegal argument
        assertThrows(IllegalArgumentException.class, () -> new MultiDirectional(1.0, -0.5));

        // Test with a function that never converges
        final RealFunction divergentFunction = new SimpleValueFunction("DivergentFunction", x -> 1.0 / Math.sqrt(-x));
        final ObjectiveFunction divergentObjectiveFunction = new PointValuePairFunction("DivergentObjectiveFunction", new RealFunction("f", x -> divergentFunction.value(x)) {
            @Override
            public PointValuePair value(RealVector x) {
                return new PointValuePair(x, this.f.value(x[0]));
            }
        });

        final NelderMeadSimplexSearch divergentSearch = new NelderMeadSimplexSearch(divergentObjectiveFunction, GoalType.MINIMIZE, 5, new RealVector(1), null);
        divergentSearch.setMaximumEvaluations(100);

        final NelderMeadSimplex divergentSimplex = new NelderMeadSimplex(5);
        for (int i = 0; i < divergentSimplex.getNumberOfPoints(); i++) {
            divergentSimplex.setPoint(i, new RealVector(1).setEntry(0, i * 0.1));
        }

        divergentSearch.setInitialSimplex(divergentSimplex);
        divergentSearch.setMaxIterationsWithoutImprovement(10);
        divergentSearch.setCheckConvergence(new RealConvergenceChecker(1.0E-6, 1.0E-5, 1.0E-5));

        assertThrows(MaxEvaluationsExceededException.class, () -> divergentSearch.optimize());

        // Test with a function that has no bracketing
        final RealFunction noBracketingFunction = new SimpleValueFunction("NoBracketingFunction", x -> Math.abs(Math.sin(x) - 0.5));
        final ObjectiveFunction noBracketingObjectiveFunction = new PointValuePairFunction("NoBracketingObjectiveFunction", new RealFunction("f", x -> noBracketingFunction.value(x)) {
            @Override
            public PointValuePair value(RealVector x) {
                return new PointValuePair(x, this.f.value(x[0]));
            }
        });

        final NelderMeadSimplexSearch noBracketingSearch = new NelderMeadSimplexSearch(noBracketingObjectiveFunction, GoalType.MINIMIZE, 5, new RealVector(1), null);
        noBracketingSearch.setMaximumEvaluations(100);

        final NelderMeadSimplex noBracketingSimplex = new NelderMeadSimplex(5);
        for (int i = 0; i < noBracketingSimplex.getNumberOfPoints(); i++) {
            noBracketingSimplex.setPoint(i, new RealVector(1).setEntry(0, i * 0.1));
        }

        noBracketingSearch.setInitialSimplex(noBracketingSimplex);
        noBracketingSearch.setMaxIterationsWithoutImprovement(10);
        noBracketingSearch.setCheckConvergence(new RealConvergenceChecker(1.0E-6, 1.0E-5, 1.0E-5));

        assertThrows(NoBracketingException.class, () -> noBracketingSearch.optimize());

        // Test with a function that throws an exception
        final RealFunction exceptionFunction = new SimpleValueFunction("ExceptionFunction", x -> {
            throw new NumberIsTooSmallException("Error message");
        });
        final ObjectiveFunction exceptionObjectiveFunction = new PointValuePairFunction("ExceptionObjectiveFunction", new RealFunction("f", x -> exceptionFunction.value(x)) {
            @Override
            public PointValuePair value(RealVector x) {
                return new PointValuePair(x, this.f.value(x[0]));
            }
        });

        final NelderMeadSimplexSearch exceptionSearch = new NelderMeadSimzieSearch(exceptionObjectiveFunction, GoalType.MINIMIZE, 5, new RealVector(1), null);
        exceptionSearch.setMaximumEvaluations(1);

        final NelderMeadSimplex exceptionSimplex = new NelderMeadSimplex(5);
        for (int i = 0; i < exceptionSimplex.getNumberOfPoints(); i++) {
            exceptionSimplex.setPoint(i, new RealVector(1).setEntry(0, i * 0.1));
        }

        exceptionSearch.setInitialSimplex(exceptionSimplex);
        exceptionSearch.setMaxIterationsWithoutImprovement(10);
        exceptionSearch.setCheckConvergence(new RealConvergenceChecker(1.0E-6, 1.0E-5, 1.0E-5));

        assertThrows(UnsupportedOperationException.class, () -> exceptionSearch.optimize());
    }
}