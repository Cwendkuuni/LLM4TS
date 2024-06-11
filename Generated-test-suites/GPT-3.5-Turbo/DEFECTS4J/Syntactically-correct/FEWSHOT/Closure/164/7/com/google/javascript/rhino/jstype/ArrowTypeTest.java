import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ArrowTypeTest {
    
    @Test
    public void testIsSubtype() {
        ArrowType arrowType1 = new ArrowType(null, null, null);
        assertTrue(arrowType1.isSubtype(arrowType1));
        
        ArrowType arrowType2 = new ArrowType(null, null, null);
        assertFalse(arrowType1.isSubtype(arrowType2));
        
        ArrowType arrowType3 = new ArrowType(null, null, null);
        arrowType3.returnType = null;
        assertTrue(arrowType1.isSubtype(arrowType3));
        
        ArrowType arrowType4 = new ArrowType(null, null, null);
        arrowType4.returnType = null;
        assertFalse(arrowType3.isSubtype(arrowType4));
        
        ArrowType arrowType5 = new ArrowType(null, null, null);
        arrowType5.parameters = null;
        assertTrue(arrowType1.isSubtype(arrowType5));
        
        ArrowType arrowType6 = new ArrowType(null, null, null);
        arrowType6.parameters = null;
        assertFalse(arrowType5.isSubtype(arrowType6));
    }
    
    @Test
    public void testIsEquivalentTo() {
        ArrowType arrowType1 = new ArrowType(null, null, null);
        assertTrue(arrowType1.isEquivalentTo(arrowType1));
        
        ArrowType arrowType2 = new ArrowType(null, null, null);
        assertFalse(arrowType1.isEquivalentTo(arrowType2));
        
        ArrowType arrowType3 = new ArrowType(null, null, null);
        arrowType3.returnType = null;
        assertTrue(arrowType1.isEquivalentTo(arrowType3));
        
        ArrowType arrowType4 = new ArrowType(null, null, null);
        arrowType4.returnType = null;
        assertFalse(arrowType3.isEquivalentTo(arrowType4));
        
        ArrowType arrowType5 = new ArrowType(null, null, null);
        arrowType5.parameters = null;
        assertTrue(arrowType1.isEquivalentTo(arrowType5));
        
        ArrowType arrowType6 = new ArrowType(null, null, null);
        arrowType6.parameters = null;
        assertFalse(arrowType5.isEquivalentTo(arrowType6));
    }
    
    @Test
    public void testHashCode() {
        ArrowType arrowType = new ArrowType(null, null, null);
        assertEquals(arrowType.hashCode(), arrowType.hashCode());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetLeastSupertype() {
        ArrowType arrowType = new ArrowType(null, null, null);
        arrowType.getLeastSupertype(null);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetGreatestSubtype() {
        ArrowType arrowType = new ArrowType(null, null, null);
        arrowType.getGreatestSubtype(null);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testTestForEquality() {
        ArrowType arrowType = new ArrowType(null, null, null);
        arrowType.testForEquality(null);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testVisit() {
        ArrowType arrowType = new ArrowType(null, null, null);
        arrowType.visit(null);
    }
    
    @Test
    public void testToStringHelper() {
        ArrowType arrowType = new ArrowType(null, null, null);
        assertEquals("toString", arrowType.toStringHelper(true));
    }
}