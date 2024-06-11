
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.geometry.euclidean.oned.IntervalsSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class IntervalsSetTest {
    
    private IntervalsSet intervalsSet;
    
    @Before
    public void setUp() {
        intervalsSet = new IntervalsSet();
    }
    
    @Test
    public void testDefaultConstructor() {
        assertNotNull(intervalsSet);
    }
    
    @Test
    public void testConstructorWithLowerAndUpper() {
        double lower = 1.0;
        double upper = 5.0;
        intervalsSet = new IntervalsSet(lower, upper);
        
        assertEquals(lower, intervalsSet.getInf(), 0.0);
        assertEquals(upper, intervalsSet.getSup(), 0.0);
    }
    
    @Test
    public void testConstructorWithTree() {
        IntervalsSet intervalsSet2 = new IntervalsSet();
        intervalsSet = new IntervalsSet(intervalsSet2.getTree());
        
        assertNotNull(intervalsSet);
    }
    
    @Test
    public void testConstructorWithBoundary() {
        List<SubHyperplane<Euclidean1D>> boundary = new ArrayList<>();
        IntervalsSet intervalsSet2 = new IntervalsSet(boundary);
        intervalsSet = new IntervalsSet(boundary);
        
        assertNotNull(intervalsSet);
        assertEquals(intervalsSet2, intervalsSet);
    }
    
    @Test
    public void testGetInf() {
        double inf = intervalsSet.getInf();
        
        assertEquals(Double.NEGATIVE_INFINITY, inf, 0.0);
    }
    
    @Test
    public void testGetSup() {
        double sup = intervalsSet.getSup();
        
        assertEquals(Double.POSITIVE_INFINITY, sup, 0.0);
    }
    
    @Test
    public void testAsList() {
        List<Interval> intervalsList = intervalsSet.asList();
        
        assertTrue(intervalsList.isEmpty());
    }
}
