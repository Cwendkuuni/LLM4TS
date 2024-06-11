package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math.ode.ExpandableStatefulODE;
import org.apache.commons.math.ode.FirstOrderIntegrator;
import org.apache.commons.math.ode.TestProblem1;
import org.apache.commons.math.util.FastMath;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testIntegrate() {

        TestProblem1 pb = new TestProblem1();
        double initialStepSize = 0.01;
        double minStepSize = 1.0e-10;
        double maxStepSize = 1.0;
        double scalAbsoluteTolerance = 1.0e-6;
        double scalRelativeTolerance = 1.0e-6;
        double[] vecAbsoluteTolerance = {1.0e-6, 1.0e-6};
        double[] vecRelativeTolerance = {1.0e-6, 1.0e-6};
        double t0 = pb.getInitialTime();
        double[] y0 = pb.getInitialState();
        double t = pb.getFinalTime();

        EmbeddedRungeKuttaIntegrator integrator =
                new DormandPrince853Integrator(initialStepSize, minStepSize, maxStepSize, scalAbsoluteTolerance, scalRelativeTolerance);
        FirstOrderIntegrator integ = integrator;
        
        ExpandableStatefulODE expandable = new ExpandableStatefulODE(pb);
        integ.integrate(expandable, t);
        
        double[] y = expandable.getCompleteState();
        
        assertTrue(FastMath.abs(y[0]-3.7302)<1.0e-4);
        assertTrue(FastMath.abs(y[1]-5.5639)<1.0e-4);
    }
}