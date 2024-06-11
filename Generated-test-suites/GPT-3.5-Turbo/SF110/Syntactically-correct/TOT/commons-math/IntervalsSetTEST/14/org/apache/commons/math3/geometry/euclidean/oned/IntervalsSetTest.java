import org.junit.Test;
import static org.junit.Assert.*;

public class IntervalsSetTest {

    @Test
    public void testIntervalsSetConstructor1() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertNotNull(intervalsSet);
        // Add more assertions here...
    }

    @Test
    public void testIntervalsSetConstructor2() {
        IntervalsSet intervalsSet = new IntervalsSet(1.0, 5.0);
        assertNotNull(intervalsSet);
        // Add more assertions here...
    }

    @Test
    public void testIntervalsSetConstructor3() {
        BSPTree<Euclidean1D> tree = new BSPTree<>();
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        assertNotNull(intervalsSet);
        // Add more assertions here...
    }

    @Test
    public void testIntervalsSetConstructor4() {
        Collection<SubHyperplane<Euclidean1D>> boundary = new ArrayList<>();
        IntervalsSet intervalsSet = new IntervalsSet(boundary);
        assertNotNull(intervalsSet);
        // Add more assertions here...
    }

    @Test
    public void testBuildNew() {
        BSPTree<Euclidean1D> tree = new BSPTree<>();
        IntervalsSet intervalsSet = new IntervalsSet();
        IntervalsSet newIntervalsSet = intervalsSet.buildNew(tree);
        assertNotNull(newIntervalsSet);
        // Add more assertions here...
    }

    @Test
    public void testComputeGeometricalProperties() {
        IntervalsSet intervalsSet = new IntervalsSet();
        intervalsSet.computeGeometricalProperties();
        // Add assertions related to computed geometrical properties here...
    }

    @Test
    public void testGetInf() {
        IntervalsSet intervalsSet = new IntervalsSet();
        double inf = intervalsSet.getInf();
        // Add assertions related to the expected return value of getInf() here...
    }

    @Test
    public void testGetSup() {
        IntervalsSet intervalsSet = new IntervalsSet();
        double sup = intervalsSet.getSup();
        // Add assertions related to the expected return value of getSup() here...
    }

    @Test
    public void testAsList() {
        IntervalsSet intervalsSet = new IntervalsSet();
        List<Interval> intervalList = intervalsSet.asList();
        assertNotNull(intervalList);
        // Add more assertions related to intervalList here...
    }

    // Add more test cases for other methods...

}