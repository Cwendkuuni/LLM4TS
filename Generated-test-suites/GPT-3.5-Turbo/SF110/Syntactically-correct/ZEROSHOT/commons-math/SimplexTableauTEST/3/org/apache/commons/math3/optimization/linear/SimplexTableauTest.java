package org.apache.commons.math3.optimization.linear;

import org.apache.commons.math3.optimization.*;
import java.util.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.linear.*;
import java.io.*;

import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexTableauTest {
    
    @Test
    public void testInitializeColumnLabels() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{1, 2}, 0);
        Collection<LinearConstraint> constraints = new ArrayList<>();
        GoalType goalType = GoalType.MINIMIZE;
        boolean restrictToNonNegative = false;
        double epsilon = 1e-6;
        
        SimplexTableau simplexTableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);
        simplexTableau.initializeColumnLabels();
        
        List<String> expectedColumnLabels = Arrays.asList("x0", "x1", "x-", "s0", "s1", "a0", "a1", "RHS");
        
        assertEquals(expectedColumnLabels, simplexTableau.columnLabels);
    }
    
    @Test
    public void testCreateTableau() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{1, 2}, 0);
        Collection<LinearConstraint> constraints = new ArrayList<>();
        GoalType goalType = GoalType.MINIMIZE;
        boolean restrictToNonNegative = false;
        double epsilon = 1e-6;
        
        SimplexTableau simplexTableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);
        simplexTableau.initializeColumnLabels();
        RealMatrix actualTableau = simplexTableau.createTableau(goalType == GoalType.MAXIMIZE);
        
        double[][] expectedTableauData = {
            {-1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
            {0.0, -1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
            {1.0, 2.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
        };
        
        Array2DRowRealMatrix expectedTableau = new Array2DRowRealMatrix(expectedTableauData);
        
        assertArrayEquals(expectedTableau.getData(), actualTableau.getData());
    }

    //More test methods for other methods in SimplexTableau class can be added here
    
}