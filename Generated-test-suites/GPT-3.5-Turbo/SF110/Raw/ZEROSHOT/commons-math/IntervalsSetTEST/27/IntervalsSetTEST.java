import org.apache.commons.math3.geometry.euclidean.oned.IntervalsSet;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class IntervalsSetTest {

    private IntervalsSet intervalsSet;

    @Before
    public void setup() {
        intervalsSet = new IntervalsSet();
    }

    @Test
    public void testBuildNew() {
        IntervalsSet newIntervalsSet = intervalsSet.buildNew(null);
        assertNull(newIntervalsSet);
    }

    @Test
    public void testComputeGeometricalProperties() {
        intervalsSet.computeGeometricalProperties();
        // add assertions here
    }

    @Test
    public void testGetInf() {
        double inf = intervalsSet.getInf();
        // add assertions here
    }

    @Test
    public void testGetSup() {
        double sup = intervalsSet.getSup();
        // add assertions here
    }

    @Test
    public void testAsList() {
        List<Interval> list = intervalsSet.asList();
        // add assertions here
    }
}