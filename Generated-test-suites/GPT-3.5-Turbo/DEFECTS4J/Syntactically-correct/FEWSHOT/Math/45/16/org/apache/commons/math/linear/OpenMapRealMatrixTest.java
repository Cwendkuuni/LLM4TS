
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.linear.OpenMapRealMatrix;
import org.junit.Before;
import org.junit.Test;

public class OpenMapRealMatrixTest {
    private OpenMapRealMatrix A;
    private OpenMapRealMatrix B;
    private OpenMapRealMatrix C;
    
    @Before
    public void setUp() {
        A = new OpenMapRealMatrix(3, 3);
        B = new OpenMapRealMatrix(3, 3);
        C = new OpenMapRealMatrix(3, 3);
        
        A.setEntry(0, 0, 1.0);
        A.setEntry(0, 1, 2.0);
        A.setEntry(0, 2, 3.0);
        A.setEntry(1, 0, 4.0);
        A.setEntry(1, 1, 5.0);
        A.setEntry(1, 2, 6.0);
        A.setEntry(2, 0, 7.0);
        A.setEntry(2, 1, 8.0);
        A.setEntry(2, 2, 9.0);
        
        B.setEntry(0, 0, 1.0);
        B.setEntry(0, 1, 2.0);
        B.setEntry(0, 2, 3.0);
        B.setEntry(1, 0, 4.0);
        B.setEntry(1, 1, 5.0);
        B.setEntry(1, 2, 6.0);
        B.setEntry(2, 0, 7.0);
        B.setEntry(2, 1, 8.0);
        B.setEntry(2, 2, 9.0);
        
        C.setEntry(0, 0, 2.0);
        C.setEntry(0, 1, 4.0);
        C.setEntry(0, 2, 6.0);
        C.setEntry(1, 0, 8.0);
        C.setEntry(1, 1, 10.0);
        C.setEntry(1, 2, 12.0);
        C.setEntry(2, 0, 14.0);
        C.setEntry(2, 1, 16.0);
        C.setEntry(2, 2, 18.0);
    }
    
    @Test
    public void testAdd() {
        OpenMapRealMatrix result = A.add(B);
        
        assertEquals(C.getRowDimension(), result.getRowDimension());
        assertEquals(C.getColumnDimension(), result.getColumnDimension());
        
        for (int i = 0; i < C.getRowDimension(); i++) {
            for (int j = 0; j < C.getColumnDimension(); j++) {
                assertEquals(C.getEntry(i, j), result.getEntry(i, j), 0.0);
            }
        }
    }
    
    @Test
    public void testSubtract() {
        OpenMapRealMatrix result = C.subtract(B);
        
        assertEquals(A.getRowDimension(), result.getRowDimension());
        assertEquals(A.getColumnDimension(), result.getColumnDimension());
        
        for (int i = 0; i < A.getRowDimension(); i++) {
            for (int j = 0; j < A.getColumnDimension(); j++) {
                assertEquals(A.getEntry(i, j), result.getEntry(i, j), 0.0);
            }
        }
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testSubtractDimensionMismatch() {
        A.subtract(new OpenMapRealMatrix(2, 2));
    }
    
    @Test
    public void testMultiply() {
        OpenMapRealMatrix result = A.multiply(B);
        
        assertEquals(C.getRowDimension(), result.getRowDimension());
        assertEquals(C.getColumnDimension(), result.getColumnDimension());
        
        for (int i = 0; i < C.getRowDimension(); i++) {
            for (int j = 0; j < C.getColumnDimension(); j++) {
                assertEquals(C.getEntry(i, j), result.getEntry(i, j), 0.0);
            }
        }
    }
    
    @Test
    public void testGetEntry() {
        assertEquals(1.0, A.getEntry(0, 0), 0.0);
        assertEquals(5.0, A.getEntry(1, 1), 0.0);
        assertEquals(9.0, A.getEntry(2, 2), 0.0);
    }
    
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetEntryOutOfBounds() {
        A.getEntry(3, 3);
    }
    
    @Test
    public void testSetEntry() {
        A.setEntry(0, 0, 10.0);
        assertEquals(10.0, A.getEntry(0, 0), 0.0);
    }
    
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testSetEntryOutOfBounds() {
        A.setEntry(3, 3, 10.0);
    }
    
    @Test
    public void testAddToEntry() {
        A.addToEntry(0, 0, 5.0);
        assertEquals(6.0, A.getEntry(0, 0), 0.0);
    }
    
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testAddToEntryOutOfBounds() {
        A.addToEntry(3, 3, 5.0);
    }
    
    @Test
    public void testMultiplyEntry() {
        A.multiplyEntry(0, 0, 2.0);
        assertEquals(2.0, A.getEntry(0, 0), 0.0);
    }
    
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testMultiplyEntryOutOfBounds() {
        A.multiplyEntry(3, 3, 2.0);
    }
    
}
