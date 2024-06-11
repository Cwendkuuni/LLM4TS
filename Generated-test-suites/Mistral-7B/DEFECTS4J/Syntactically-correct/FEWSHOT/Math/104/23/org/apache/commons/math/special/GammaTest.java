package org.apache.commons.cli;

import static org.junit.Assert.assertNaN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class GammaTest {

    @Test
    public void testLogGamma() {
        assertEquals(0.0, Gamma.logGamma(0.0), 0.0);
        assertEquals(-0.405465108369467, Gamma.logGamma(0.5), 1e-6);
        assertEquals(1.178097987447733, Gamma.logGamma(1.5), 1e-6);
        assertEquals(1.731315035257233, Gamma.logGamma(2.0), 1e-6);
        assertEquals(3.090163972147147, Gamma.logGamma(3.0), 1e-6);
        assertEquals(4.574874694747481, Gamma.logGamma(4.0), 1e-6);
        assertEquals(5.779349454379962, Gamma.logGamma(5.0), 1e-6);

        assertNaN(Gamma.logGamma(-1.0));
        assertNaN(Gamma.logGamma(0.5, Double.POSITIVE_INFINITY)); // too large x
        assertNaN(Gamma.logGamma(1.0, Double.NEGATIVE_INFINITY)); // too small x
        assertNaN(Gamma.logGamma(Integer.MAX_VALUE)); // too large a
        assertNaN(Gamma.logGamma(-Integer.MAX_VALUE)); // too small a
    }

    @Test
    public void testRegularizedGammaP() throws MathException {
        assertEquals(0.0, Gamma.regularizedGammaP(0.0), 0.0);
        assertEquals(0.5, Gamma.regularizedGammaP(0.5), 1e-6);
        assertEquals(0.874672997464032, Gamma.regularizedGammaP(1.5), 1e-6);
        assertEquals(0.918288334448321, Gamma.regularizedGammaP(2.0), 1e-6);
        assertEquals(0.953573446112464, Gamma.regularizedGammaP(3.0), 1e-6);
        assertEquals(0.992918355521923, Gamma.regularizedGammaP(4.0), 1e-6);
        assertEquals(0.999999999999998, Gamma.regularizedGammaP(5.0), 1e-6);

        double epsilon = 1e-9;
        double maxIterations = 1000;

        assertEquals(0.254000543911363, Gamma.regularizedGammaP(0.75, 0.5), epsilon);
        assertEquals(0.170141132338332, Gamma.regularizedGammaP(0.75, 1.5), epsilon);
        assertEquals(0.168228542172525, Gamma.regularizedGammaP(0.75, 2.0), epsilon);
        assertEquals(0.167944287183257, Gamma.regularizedGammaP(0.75, 3.0), epsilon);
        assertEquals(0.167933393453162, Gamma.regularizedGammaP(0.75, 4.0), epsilon);
        assertEquals(0.167933393453143, Gamma.regularizedGammaP(0.75, 5.0), epsilon);

        // test max iterations
        assertNaN(Gamma.regularizedGammaP(0.75, 1.0, 0, 1));
        assertNaN(Gamma.regularizedGammaP(0.75, 1.0, 0, 10));
        try {
            Gamma.regularizedGammaP(0.75, 1.0, epsilon, 0);
            fail("Expected MaxIterationsExceededException");
        } catch (MaxIterationsExceededException e) {
            // expected
        }
        try {
            Gamma.regularizedGammaP(0.75, 1.0, epsilon, Integer.MAX_VALUE);
            fail("Expected MathException");
        } catch (MathException e) {
            // expected
        }

        // test epsilon
        assertEquals(0.167933393453139, Gamma.regularizedGammaP(0.75, 1.0, 1e-10, 1000), epsilon);
        assertEquals(0.167933393453139, Gamma.regularizedGammaP(0.75, 1.0, 1e-15, 1000), epsilon);
        assertFalse(Double.isFinite(Gamma.regularizedGammaP(0.75, 1.0, 0, 1000)));
    }

    @Test
    public void testRegularizedGammaQ() throws MathException {
        assertEquals(1.0, Gamma.regularizedGammaQ(1.0), 0.0);
        assertEquals(0.612751261331633, Gamma.regularizedGammaQ(0.5), 1e-6);
        assertEquals(0.668646459131591, Gamma.regularizedGammaQ(1.5), 1e-6);
        assertEquals(0.712378192117173, Gamma.regularizedGammaQ(2.0), 1e-6);
        assertEquals(0.756802677123085, Gamma.regularizedGammaQ(3.0), 1e-6);
        assertEquals(0.795321267876882, Gamma.regularizedGammaQ(4.0), 1e-6);
        assertEquals(0.841755975238535, Gamma.regularizedGammaQ(5.0), 1e-6);

        double epsilon = 1e-9;
        double maxIterations = 1000;

        assertEquals(0.745964238955832, Gamma.regularizedGammaQ(0.75, 0.5), epsilon);
        assertEquals(0.831765861044169, Gamma.regularizedGammaQ(0.75, 1.5), epsilon);
        assertEquals(0.832113671084635, Gamma.regularizedGammaQ(0.75, 2.0), epsilon);
        assertEquals(0.832133635384378, Gamma.regularizedGammaQ(0.75, 3.0), epsilon);
        assertEquals(0.832133635384378, Gamma.regularizedGammaQ(0.75, 4.0), epsilon);
        assertEquals(0.832133635384378, Gamma.regularizedGammaQ(0.75, 5.0), epsilon);

        // test max iterations
        assertNaN(Gamma.regularizedGammaQ(0.75, 1.0, 0, 1));
        assertNaN(Gamma.regularizedGammaQ(0.75, 1.0, 0, 10));
        try {
            Gamma.regularizedGammaQ(0.75, 1.0, epsilon, 0);
            fail("Expected MaxIterationsExceededException");
        } catch (MaxIterationsExceededException e) {
            // expected
        }
        try {
            Gamma.regularizedGammaQ(0.75, 1.0, epsilon, Integer.MAX_VALUE);
            fail("Expected MathException");
        } catch (MathException e) {
            // expected
        }

        // test epsilon
        assertEquals(0.832133635384378, Gamma.regularizedGammaQ(0.75, 1.0, 1e-10, 1000), epsilon);
        assertEquals(0.832133635384378, Gamma.regularizedGammaQ(0.75, 1.0, 1e-15, 1000), epsilon);
        assertTrue(Double.isFinite(Gamma.regularizedGammaQ(0.75, 1.0, 0, 1000)));
    }
}