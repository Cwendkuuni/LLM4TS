package org.apache.commons.math3.geometry.euclidean.oned;
import static org.junit.Assert.*;
import org.junit.Test;

public class IntervalsSetTest {

    @Test
    public void testIntervalsSetConstructor() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertNotNull(intervalsSet);
    }

    @Test
    public void testBuildNew() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertNotNull(intervalsSet.buildNew(intervalsSet.getTree(false)));
    }

    @Test
    public void testGetInf() {
        IntervalsSet intervalsSet = new IntervalsSet(0.0, 1.0);
        assertEquals(0.0, intervalsSet.getInf(), 1e-5);
    }

    @Test
    public void testGetSup() {
        IntervalsSet intervalsSet = new IntervalsSet(0.0, 1.0);
        assertEquals(1.0, intervalsSet.getSup(), 1e-5);
    }

    @Test
    public void testAsList() {
        IntervalsSet intervalsSet = new IntervalsSet(0.0, 1.0);
        assertEquals(1, intervalsSet.asList().size());
    }

    // Add more test cases for edge cases and exceptions

}