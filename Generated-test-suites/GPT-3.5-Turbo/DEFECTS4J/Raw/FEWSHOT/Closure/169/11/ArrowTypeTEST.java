import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ArrowTypeTest {
    @Test
    public void testIsSubtype() {
        ArrowType arrowType1 = new ArrowType(null, null, null);
        ArrowType arrowType2 = new ArrowType(null, null, null);
        
        assertTrue(arrowType1.isSubtype(arrowType2));
    }

    @Test
    public void testHasEqualParameters() {
        ArrowType arrowType1 = new ArrowType(null, null, null);
        ArrowType arrowType2 = new ArrowType(null, null, null);
        
        assertTrue(arrowType1.hasEqualParameters(arrowType2, true));
    }
    
    @Test
    public void testCheckArrowEquivalenceHelper() {
        ArrowType arrowType1 = new ArrowType(null, null, null);
        ArrowType arrowType2 = new ArrowType(null, null, null);
        
        assertTrue(arrowType1.checkArrowEquivalenceHelper(arrowType2, true));
    }
    
    @Test
    public void testHashCode() {
        ArrowType arrowType = new ArrowType(null, null, null);
        
        assertEquals(arrowType.hashCode(), 0);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetLeastSupertype() {
        ArrowType arrowType1 = new ArrowType(null, null, null);
        ArrowType arrowType2 = new ArrowType(null, null, null);
        
        arrowType1.getLeastSupertype(arrowType2);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetGreatestSubtype() {
        ArrowType arrowType1 = new ArrowType(null, null, null);
        ArrowType arrowType2 = new ArrowType(null, null, null);
        
        arrowType1.getGreatestSubtype(arrowType2);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testTestForEquality() {
        ArrowType arrowType1 = new ArrowType(null, null, null);
        ArrowType arrowType2 = new ArrowType(null, null, null);
        
        arrowType1.testForEquality(arrowType2);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testVisit() {
        ArrowType arrowType = new ArrowType(null, null, null);
        
        arrowType.visit(null);
    }
    
    @Test
    public void testGetPossibleToBooleanOutcomes() {
        ArrowType arrowType = new ArrowType(null, null, null);
        
        assertEquals(arrowType.getPossibleToBooleanOutcomes(), BooleanLiteralSet.TRUE);
    }
    
    @Test
    public void testResolveInternal() {
        ArrowType arrowType = new ArrowType(null, null, null);
        
        assertNull(arrowType.resolveInternal(null, null));
    }
    
    @Test
    public void testHasUnknownParamsOrReturn() {
        ArrowType arrowType = new ArrowType(null, null, null);
        
        assertFalse(arrowType.hasUnknownParamsOrReturn());
    }
    
    @Test
    public void testToStringHelper() {
        ArrowType arrowType = new ArrowType(null, null, null);
        
        assertEquals(arrowType.toStringHelper(true), "[ArrowType]");
    }
    
    @Test
    public void testHasAnyTemplateInternal() {
        ArrowType arrowType = new ArrowType(null, null, null);
        
        assertFalse(arrowType.hasAnyTemplateInternal());
    }
}