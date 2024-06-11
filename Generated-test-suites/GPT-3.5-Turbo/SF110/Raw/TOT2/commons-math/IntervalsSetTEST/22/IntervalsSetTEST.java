import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.geometry.euclidean.oned.IntervalsSet;
import org.apache.commons.math3.geometry.Partitioning;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.geometry.partitioning.OrientedPoint;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;

import java.util.ArrayList;
import java.util.List;

public class IntervalsSetTest {

    @Test
    public void testIntervalsSetConstructor() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertNotNull(intervalsSet);
    }

    @Test
    public void testBuildNew() {
        BSPTree<Euclidean1D> tree = new BSPTree<>();
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        assertNotNull(intervalsSet);
    }

    @Test
    public void testComputeGeometricalProperties() {
        // Test cases for computeGeometricalProperties method can be added here
    }

    @Test
    public void testGetInf() {
        // Test cases for getInf method can be added here
    }

    @Test
    public void testGetSup() {
        // Test cases for getSup method can be added here
    }

    @Test
    public void testAsList() {
        // Test cases for asList method can be added here
    }

    // Additional test cases for edge cases and exceptions can be added here

}