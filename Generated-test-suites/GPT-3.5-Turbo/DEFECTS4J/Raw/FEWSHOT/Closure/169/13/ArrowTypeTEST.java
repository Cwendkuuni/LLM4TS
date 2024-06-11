import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ArrowTypeTest {
    @Test
    public void testIsSubtype() {
        ArrowType arrowType1 = new ArrowType(null, null, null);
        ArrowType arrowType2 = new ArrowType(null, null, null);
        assertEquals(true, arrowType1.isSubtype(arrowType2));
    }
    
    @Test
    public void testHasEqualParameters() {
        ArrowType arrowType1 = new ArrowType(null, null, null);
        ArrowType arrowType2 = new ArrowType(null, null, null);
        assertEquals(true, arrowType1.hasEqualParameters(arrowType2, false));
    }
    
    @Test
    public void testCheckArrowEquivalenceHelper() {
        ArrowType arrowType1 = new ArrowType(null, null, null);
        ArrowType arrowType2 = new ArrowType(null, null, null);
        assertEquals(true, arrowType1.checkArrowEquivalenceHelper(arrowType2, false));
    }
    
    @Test
    public void testHashCode() {
        ArrowType arrowType = new ArrowType(null, null, null);
        assertEquals(0, arrowType.hashCode());
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
    public void testGetPossibleToBooleanOutcomes() {
        ArrowType arrowType = new ArrowType(null, null, null);
        assertEquals(BooleanLiteralSet.TRUE, arrowType.getPossibleToBooleanOutcomes());
    }
    
    @Test
    public void testResolveInternal() {
        ArrowType arrowType = new ArrowType(null, null, null);
        JSType resolvedType = arrowType.resolveInternal(null, null);
        assertEquals(arrowType, resolvedType);
    }
    
    @Test
    public void testHasUnknownParamsOrReturn() {
        ArrowType arrowType = new ArrowType(null, null, null);
        assertEquals(false, arrowType.hasUnknownParamsOrReturn());
    }
    
    @Test
    public void testToStringHelper() {
        ArrowType arrowType = new ArrowType(null, null, null);
        assertEquals("[ArrowType]", arrowType.toStringHelper(false));
    }
    
    @Test
    public void testHasAnyTemplateInternal() {
        ArrowType arrowType = new ArrowType(null, null, null);
        assertEquals(false, arrowType.hasAnyTemplateInternal());
    }
}