import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.geometry.partitioning.*;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class IntervalsSetTest {
    
    private static final double EPSILON = 1e-10;
    
    private static IntervalsSet emptySet;
    private static IntervalsSet infiniteSet;
    private static IntervalsSet singleIntervalSet;
    private static IntervalsSet multipleIntervalSet;
    
    @BeforeClass
    public static void setUp() {
        
        emptySet = new IntervalsSet();
        
        infiniteSet = new IntervalsSet(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        
        singleIntervalSet = new IntervalsSet(0.0, 1.0);
        
        multipleIntervalSet = new IntervalsSet();
        multipleIntervalSet.union(new IntervalsSet(-1.0, 0.0));
        multipleIntervalSet.union(new IntervalsSet(1.0, 2.0));
        multipleIntervalSet.union(new IntervalsSet(3.0, 4.0));
    }
    
    @Test
    public void testEmptySet() {
        assertTrue(emptySet.isEmpty());
        assertFalse(emptySet.isFull());
        assertEquals(Double.POSITIVE_INFINITY, emptySet.getSize(), EPSILON);
        assertEquals(Double.NEGATIVE_INFINITY, emptySet.getInf(), EPSILON);
        assertEquals(Double.POSITIVE_INFINITY, emptySet.getSup(), EPSILON);
        assertTrue(emptySet.asList().isEmpty());
    }
    
    @Test
    public void testInfiniteSet() {
        assertFalse(infiniteSet.isEmpty());
        assertTrue(infiniteSet.isFull());
        assertEquals(Double.POSITIVE_INFINITY, infiniteSet.getSize(), EPSILON);
        assertEquals(Double.NEGATIVE_INFINITY, infiniteSet.getInf(), EPSILON);
        assertEquals(Double.POSITIVE_INFINITY, infiniteSet.getSup(), EPSILON);
        assertTrue(infiniteSet.asList().isEmpty());
    }
    
    @Test
    public void testSingleIntervalSet() {
        assertFalse(singleIntervalSet.isEmpty());
        assertFalse(singleIntervalSet.isFull());
        assertEquals(1.0, singleIntervalSet.getSize(), EPSILON);
        assertEquals(0.0, singleIntervalSet.getInf(), EPSILON);
        assertEquals(1.0, singleIntervalSet.getSup(), EPSILON);
        assertEquals(1, singleIntervalSet.asList().size());
        
        Interval interval = singleIntervalSet.asList().get(0);
        assertEquals(0.0, interval.getInf(), EPSILON);
        assertEquals(1.0, interval.getSup(), EPSILON);
    }
    
    @Test
    public void testMultipleIntervalSet() {
        assertFalse(multipleIntervalSet.isEmpty());
        assertFalse(multipleIntervalSet.isFull());
        assertEquals(4.0, multipleIntervalSet.getSize(), EPSILON);
        assertEquals(-1.0, multipleIntervalSet.getInf(), EPSILON);
        assertEquals(4.0, multipleIntervalSet.getSup(), EPSILON);
        assertEquals(3, multipleIntervalSet.asList().size());
        
        Interval interval1 = multipleIntervalSet.asList().get(0);
        assertEquals(-1.0, interval1.getInf(), EPSILON);
        assertEquals(0.0, interval1.getSup(), EPSILON);
        
        Interval interval2 = multipleIntervalSet.asList().get(1);
        assertEquals(1.0, interval2.getInf(), EPSILON);
        assertEquals(2.0, interval2.getSup(), EPSILON);
        
        Interval interval3 = multipleIntervalSet.asList().get(2);
        assertEquals(3.0, interval3.getInf(), EPSILON);
        assertEquals(4.0, interval3.getSup(), EPSILON);
    }
}