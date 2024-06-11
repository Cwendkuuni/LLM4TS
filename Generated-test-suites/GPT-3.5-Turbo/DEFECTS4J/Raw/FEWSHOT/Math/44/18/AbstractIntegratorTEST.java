package org.apache.commons.math.ode;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AbstractIntegratorTest {

    @Test
    public void testIntegrate() {

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                // TODO: Implement this method
            }

            @Override
            public int getDimension() {
                // TODO: Implement this method
                return 0;
            }
        };

        double t0 = 0;
        double[] y0 = new double[0];
        double t = 10;
        double[] y = new double[0];

        // Create instance of AbstractIntegrator
        AbstractIntegrator integrator = new AbstractIntegrator() {

            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws MathIllegalStateException, MathIllegalArgumentException {
                // TODO: Implement this method
            }
        };

        try {
            double finalT = integrator.integrate(equations, t0, y0, t, y);
            assertEquals(t, finalT, 0);
        } catch (MathIllegalStateException e) {
            // TODO: Handle exception
        } catch (MathIllegalArgumentException e) {
            // TODO: Handle exception
        }
    }
}