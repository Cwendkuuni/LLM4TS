package org.apache.commons.math3.geometry.euclidean.oned;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.util.*;
import org.junit.Test;
import java.util.*;
import org.apache.commons.math3.geometry.partitioning.*;

public class IntervalsSetTest {
    @Test
    public void testConstructor() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertNull(intervalsSet.buildNew(null));
        assertNull(intervalsSet.getTree());
        
        intervalsSet = new IntervalsSet(1.0, 10.0);
        assertEquals(1.0, intervalsSet.getInf(), 0.0);
        assertEquals(10.0, intervalsSet.getSup(), 0.0);
        assertNotNull(intervalsSet.asLits());
        
        BSPTree<Euclidean1D> tree = new BSPTree<>(Boolean.TRUE);
        intervalsSet = new IntervalsSet(tree);
        assertEquals(tree, intervalsSet.getTree());
        
        Collection<SubHyperplane<Euclidean1D>> boundary = new ArrayList<>();
        intervalsSet = new IntervalsSet(boundary);
        assertEquals(boundary, intervalsSet.getBoundary());
    }
    
    @Test
    public void testBuildTree() {
        IntervalsSet intervalsSet = new IntervalsSet();
        double lower = 1.0;
        double upper = 10.0;
        BSPTree<Euclidean1D> tree = intervalsSet.buildTree(lower, upper);
        assertNotNull(tree);
        
        lower = Double.POSITIVE_INFINITY;
        upper = 0.0;
        tree = intervalsSet.buildTree(lower, upper);
        assertNotNull(tree);
        
        lower = Double.NEGATIVE_INFINITY;
        upper = 0.0;
        tree = intervalsSet.buildTree(lower, upper);
        assertNotNull(tree);
        
        lower = 1.0;
        upper = Double.POSITIVE_INFINITY;
        tree = intervalsSet.buildTree(lower, upper);
        assertNotNull(tree);
        
        lower = Double.NEGATIVE_INFINITY;
        upper = Double.POSITIVE_INFINITY;
        tree = intervalsSet.buildTree(lower, upper);
        assertNotNull(tree);
    }
    
    @Test
    public void testComputeGeometricalProperties() {
        IntervalsSet intervalsSet = new IntervalsSet();
        intervalsSet.computeGeometricalProperties();
        
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>();
        tree.setAttribute(Boolean.TRUE);
        OrientedPoint cut = new OrientedPoint(new Vector1D(1.0), true);
        tree.setCut(cut);
        tree.setPlus(new BSPTree<Euclidean1D>());
        tree.setMinus(new BSPTree<Euclidean1D>());
        intervalsSet.setTree(tree);
        intervalsSet.computeGeometricalProperties();
        
        tree = new BSPTree<Euclidean1D>();
        cut = new OrientedPoint(new Vector1D(1.0), false);
        tree.setCut(cut);
        tree.setPlus(new BSPTree<Euclidean1D>());
        tree.setMinus(new BSPTree<Euclidean1D>());
        intervalsSet.setTree(tree);
        intervalsSet.computeGeometricalProperties();
        
        tree = new BSPTree<Euclidean1D>();
        intervalsSet.setTree(tree);
        intervalsSet.computeGeometricalProperties();
        
        tree = new BSPTree<Euclidean1D>();
        cut = new OrientedPoint(new Vector1D(1.0), true);
        tree.setCut(cut);
        tree.setPlus(new BSPTree<Euclidean1D>());
        tree.setMinus(new BSPTree<Euclidean1D>());
        tree.getMinus().setAttribute(Boolean.TRUE);
        intervalsSet.setTree(tree);
        intervalsSet.computeGeometricalProperties();
        
        tree = new BSPTree<Euclidean1D>();
        cut = new OrientedPoint(new Vector1D(1.0), true);
        tree.setCut(cut);
        tree.setPlus(new BSPTree<Euclidean1D>());
        tree.setMinus(new BSPTree<Euclidean1D>());
        tree.getPlus().setAttribute(Boolean.TRUE);
        intervalsSet.setTree(tree);
        intervalsSet.computeGeometricalProperties();
    }
    
    @Test
    public void testGetInf() {
        IntervalsSet intervalsSet = new IntervalsSet();
        double inf = intervalsSet.getInf();
        assertEquals(Double.POSITIVE_INFINITY, inf, 0.0);
        
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>();
        OrientedPoint cut = new OrientedPoint(new Vector1D(1.0), true);
        tree.setCut(cut);
        tree.setPlus(new BSPTree<Euclidean1D>());
        tree.setMinus(new BSPTree<Euclidean1D>());
        tree.getMinus().setAttribute(Boolean.TRUE);
        intervalsSet.setTree(tree);
        inf = intervalsSet.getInf();
        assertEquals(Double.NEGATIVE_INFINITY, inf, 0.0);
    }
    
    @Test
    public void testGetSup() {
        IntervalsSet intervalsSet = new IntervalsSet();
        double sup = intervalsSet.getSup();
        assertEquals(Double.NEGATIVE_INFINITY, sup, 0.0);
        
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>();
        OrientedPoint cut = new OrientedPoint(new Vector1D(1.0), true);
        tree.setCut(cut);
        tree.setPlus(new BSPTree<Euclidean1D>());
        tree.setMinus(new BSPTree<Euclidean1D>());
        tree.getPlus().setAttribute(Boolean.TRUE);
        intervalsSet.setTree(tree);
        sup = intervalsSet.getSup();
        assertEquals(Double.POSITIVE_INFINITY, sup, 0.0);
    }
    
    @Test
    public void testAsList() {
        IntervalsSet intervalsSet = new IntervalsSet();
        List<Interval> list = intervalsSet.asList();
        assertNotNull(list);
        
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>();
        intervalsSet.setTree(tree);
        list = intervalsSet.asList();
        assertNotNull(list);
        
        tree = new BSPTree<Euclidean1D>();
        OrientedPoint cut = new OrientedPoint(new Vector1D(1.0), true);
        tree.setCut(cut);
        tree.setPlus(new BSPTree<Euclidean1D>());
        tree.setMinus(new BSPTree<Euclidean1D>());
        intervalsSet.setTree(tree);
        list = intervalsSet.asList();
        assertNotNull(list);
    }
}