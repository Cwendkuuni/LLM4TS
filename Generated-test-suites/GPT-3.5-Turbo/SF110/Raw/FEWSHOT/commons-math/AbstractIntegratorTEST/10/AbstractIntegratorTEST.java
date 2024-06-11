package org.apache.commons.math3.ode;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class AbstractIntegratorTest {

    private AbstractIntegrator integrator;

    @Before
    public void setUp() {
        integrator = new TestIntegrator();
    }

    private class TestIntegrator extends AbstractIntegrator {
        public void integrate(ExpandableStatefulODE p0, double p1) {
            // TODO: Implement integration method for testing
        }
    }

    @Test
    public void testGetName() {
        assertEquals("TestIntegrator", integrator.getName());
    }

    @Test
    public void testAddStepHandler() {
        // TODO: Implement test
    }

    @Test
    public void testGetStepHandlers() {
        // TODO: Implement test
    }

    @Test
    public void testClearStepHandlers() {
        // TODO: Implement test
    }

    @Test
    public void testAddEventHandler() {
        // TODO: Implement test
    }

    @Test
    public void testGetEventHandlers() {
        // TODO: Implement test
    }

    @Test
    public void testClearEventHandlers() {
        // TODO: Implement test
    }

    @Test
    public void testGetCurrentStepStart() {
        // TODO: Implement test
    }

    @Test
    public void testGetCurrentSignedStepsize() {
        // TODO: Implement test
    }

    @Test
    public void testSetMaxEvaluations() {
        // TODO: Implement test
    }

    @Test
    public void testGetMaxEvaluations() {
        // TODO: Implement test
    }

    @Test
    public void testGetEvaluations() {
        // TODO: Implement test
    }

    @Test
    public void testIntegrate() {
        // TODO: Implement test
    }

    @Test
    public void testComputeDerivatives() {
        // TODO: Implement test
    }

    @Test
    public void testSetStateInitialized() {
        // TODO: Implement test
    }

    @Test
    public void testAcceptStep() {
        // TODO: Implement test
    }

    @Test
    public void testSanityChecks() {
        // TODO: Implement test
    }
}