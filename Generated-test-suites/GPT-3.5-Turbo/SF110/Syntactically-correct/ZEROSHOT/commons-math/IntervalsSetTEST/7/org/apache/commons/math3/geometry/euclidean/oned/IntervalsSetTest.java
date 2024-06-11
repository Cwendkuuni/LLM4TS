import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.geometry.partitioning.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

public class IntervalsSetTest {

    private IntervalsSet intervalsSet;
    
    @Before
    public void setup() {
        intervalsSet = new IntervalsSet();
    }
    
    @Test
    public void testConstructor1() {
        intervalsSet = new IntervalsSet();
        assertNotNull(intervalsSet);
    }
    
    @Test
    public void testConstructor2() {
        intervalsSet = new IntervalsSet(1.0, 2.0);
        assertNotNull(intervalsSet);
    }
    
    @Test
    public void testConstructor3() {
        BSPTree<Euclidean1D> tree = new BSPTree<>(true);
        intervalsSet = new IntervalsSet(tree);
        assertNotNull(intervalsSet);
    }
    
    @Test
    public void testConstructor4() {
        Collection<SubHyperplane<Euclidean1D>> boundary = new ArrayList<>();
        intervalsSet = new IntervalsSet(boundary);
        assertNotNull(intervalsSet);
    }
    
    @Test
    public void testBuildNew() {
        BSPTree<Euclidean1D> tree = new BSPTree<>(true);
        intervalsSet = intervalsSet.buildNew(tree);
        assertNotNull(intervalsSet);
    }
    
    @Test
    public void testComputeGeometricalProperties() {
        intervalsSet.computeGeometricalProperties();
        // TODO: Add assertion statements
    }
    
    @Test
    public void testGetInf() {
        double inf = intervalsSet.getInf();
        // TODO: Add assertion statements
    }
    
    @Test
    public void testGetSup() {
        double sup = intervalsSet.getSup();
        // TODO: Add assertion statements
    }
    
    @Test
    public void testAsList() {
        List<Interval> list = intervalsSet.asList();
        // TODO: Add assertion statements
    }
    
    @Test
    public void testRecurseList() {
        BSPTree<Euclidean1D> node = new BSPTree<>(true);
        List<Interval> list = new ArrayList<>();
        double lower = 0.0;
        double upper = 1.0;
        
        intervalsSet.recurseList(node, list, lower, upper);
        // TODO: Add assertion statements
    }

}