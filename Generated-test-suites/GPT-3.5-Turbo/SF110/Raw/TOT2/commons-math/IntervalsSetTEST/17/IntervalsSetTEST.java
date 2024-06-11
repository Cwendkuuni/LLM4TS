import static org.junit.Assert.*;
import org.junit.Test;

public class IntervalsSetTest
{
    @Test
    public void testIntervalsSetConstructor()
    {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertNotNull(intervalsSet);
    }
    
    @Test
    public void testBuildNew()
    {
        IntervalsSet intervalsSet = new IntervalsSet();
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(Boolean.TRUE);
        IntervalsSet newIntervalsSet = intervalsSet.buildNew(tree);
        assertNotNull(newIntervalsSet);
    }
    
    @Test
    public void testGetInf()
    {
        IntervalsSet intervalsSet = new IntervalsSet(0.0, 1.0);
        assertEquals(0.0, intervalsSet.getInf(), 0.001);
    }
    
    @Test
    public void testGetSup()
    {
        IntervalsSet intervalsSet = new IntervalsSet(0.0, 1.0);
        assertEquals(1.0, intervalsSet.getSup(), 0.001);
    }
    
    @Test
    public void testAsList()
    {
        IntervalsSet intervalsSet = new IntervalsSet(0.0, 1.0);
        assertEquals(1, intervalsSet.asList().size());
    }
}