package org.apache.commons.math3.geometry.euclidean.oned;

import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.util.*;
import java.util.*;
import org.apache.commons.math3.geometry.partitioning.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class IntervalsSetTest {
    
    @Test
    public void testConstructor() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertNotNull(intervalsSet);
        
        double lower = 0.0;
        double upper = 10.0;
        intervalsSet = new IntervalsSet(lower, upper);
        assertNotNull(intervalsSet);
        
        BSPTree<Euclidean1D> bspTree = new BSPTree<Euclidean1D>(Boolean.TRUE);
        intervalsSet = new IntervalsSet(bspTree);
        assertNotNull(intervalsSet);
        
        List<SubHyperplane<Euclidean1D>> boundary = new ArrayList<SubHyperplane<Euclidean1D>>();
        intervalsSet = new IntervalsSet(boundary);
        assertNotNull(intervalsSet);
    }
    
    @Test
    public void testBuildTree() {
        double lower = 0.0;
        double upper = 10.0;
        BSPTree<Euclidean1D> bspTree = IntervalsSet.buildTree(lower, upper);
        assertNotNull(bspTree);
        
        lower = Double.NEGATIVE_INFINITY;
        upper = 0.0;
        bspTree = IntervalsSet.buildTree(lower, upper);
        assertNotNull(bspTree);
        
        lower = Double.POSITIVE_INFINITY;
        upper = 0.0;
        bspTree = IntervalsSet.buildTree(lower, upper);
        assertNotNull(bspTree);
    }
    
    @Test
    public void testBuildNew() {
        BSPTree<Euclidean1D> bspTree = new BSPTree<Euclidean1D>(Boolean.TRUE);
        IntervalsSet intervalsSet = new IntervalsSet().buildNew(bspTree);
        assertNotNull(intervalsSet);
    }
    
    @Test
    public void testComputeGeometricalProperties() {
        IntervalsSet intervalsSet = new IntervalsSet();
        intervalsSet.computeGeometricalProperties();
        
        double lower = 0.0;
        double upper = 10.0;
        BSPTree<Euclidean1D> bspTree = IntervalsSet.buildTree(lower, upper);
        intervalsSet = new IntervalsSet(bspTree);
        intervalsSet.computeGeometricalProperties();
    }
    
    @Test
    public void testGetInf() {
        IntervalsSet intervalsSet = new IntervalsSet();
        double inf = intervalsSet.getInf();
        
        double lower = 0.0;
        double upper = 10.0;
        BSPTree<Euclidean1D> bspTree = IntervalsSet.buildTree(lower, upper);
        intervalsSet = new IntervalsSet(bspTree);
        inf = intervalsSet.getInf();
    }
    
    @Test
    public void testGetSup() {
        IntervalsSet intervalsSet = new IntervalsSet();
        double sup = intervalsSet.getSup();
        
        double lower = 0.0;
        double upper = 10.0;
        BSPTree<Euclidean1D> bspTree = IntervalsSet.buildTree(lower, upper);
        intervalsSet = new IntervalsSet(bspTree);
        sup = intervalsSet.getSup();
    }
    
    @Test
    public void testAsList() {
        IntervalsSet intervalsSet = new IntervalsSet();
        List<Interval> list = intervalsSet.asList();
        
        double lower = 0.0;
        double upper = 10.0;
        BSPTree<Euclidean1D> bspTree = IntervalsSet.buildTree(lower, upper);
        intervalsSet = new IntervalsSet(bspTree);
        list = intervalsSet.asList();
    }
}