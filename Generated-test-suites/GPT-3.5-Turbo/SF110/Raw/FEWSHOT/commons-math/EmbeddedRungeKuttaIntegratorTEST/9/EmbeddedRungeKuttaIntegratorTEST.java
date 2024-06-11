
package org.apache.commons.math3.ode.nonstiff;

import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.exception.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testEmbeddedRungeKuttaIntegrator() {

        // Test 1
        double[] c = {0.1, 0.2};
        double[][] a = { {0.11, 0.12, 0.13}, {0.21, 0.22, 0.23} };
        double[] b = {0.3, 0.4};

        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator();

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("TestIntegrator", true, c, a, b, prototype, 0.01, 1.0, 0.001, 0.001);

        assertNotNull(integrator);

        // Test 2
        double[] c2 = {0.2, 0.3};
        double[][] a2 = { {0.21, 0.22, 0.23}, {0.31, 0.32, 0.33} };
        double[] b2 = {0.4, 0.5};

        EmbeddedRungeKuttaIntegrator integrator2 = new EmbeddedRungeKuttaIntegrator("TestIntegrator2", true, c2, a2, b2, prototype, 0.01, 1.0, 0.001, 0.001);

        assertNotNull(integrator2);

        // Test 3
        double[] c3 = {0.3, 0.4};
        double[][] a3 = { {0.31, 0.32, 0.33}, {0.41, 0.42, 0.43} };
        double[] b3 = {0.5, 0.6};

        EmbeddedRungeKuttaIntegrator integrator3 = new EmbeddedRungeKuttaIntegrator("TestIntegrator3", true, c3, a3, b3, prototype, 0.01, 1.0, 0.001, 0.001);

        assertNotNull(integrator3);
    }
}
