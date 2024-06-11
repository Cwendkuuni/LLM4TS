package org.apache.commons.math.analysis.solvers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;

public class BracketingNthOrderBrentSolverTest {

    @Mock
    private BracketingNthOrderBrentSolver solver;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testGetMaximalOrder() {
        assertEquals(5, solver.getMaximalOrder());
    }
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorInvalidMaximalOrder() {
        new BracketingNthOrderBrentSolver(1e-6, 1);
    }
    
    @Test
    public void testConstructorValidMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 2);
        assertEquals(2, solver.getMaximalOrder());
    }
    
    @Test
    public void testSolve() {
        // TODO: Add test cases for the solve method
    }
}