import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ArrowTypeTest {
    @Test
    public void testIsSubtype() {
        ArrowType arrowType1 = new ArrowType(new JSTypeRegistry(), null, null);
        ArrowType arrowType2 = new ArrowType(new JSTypeRegistry(), null, null);
        assertTrue(arrowType1.isSubtype(arrowType2));
        
        ArrowType arrowType3 = new ArrowType(new JSTypeRegistry(), null, null);
        ArrowType arrowType4 = new ArrowType(new JSTypeRegistry(), null, new JSType(new JSTypeRegistry()));
        assertFalse(arrowType3.isSubtype(arrowType4));
    }

    @Test
    public void testHasEqualParameters() {
        ArrowType arrowType1 = new ArrowType(new JSTypeRegistry(), null, null);
        ArrowType arrowType2 = new ArrowType(new JSTypeRegistry(), null, null);
        assertTrue(arrowType1.hasEqualParameters(arrowType2, false));
        
        ArrowType arrowType3 = new ArrowType(new JSTypeRegistry(), null, null);
        ArrowType arrowType4 = new ArrowType(new JSTypeRegistry(), null, new JSType(new JSTypeRegistry()));
        assertFalse(arrowType3.hasEqualParameters(arrowType4, false));
    }

    @Test
    public void testCheckArrowEquivalenceHelper() {
        ArrowType arrowType1 = new ArrowType(new JSTypeRegistry(), null, null);
        ArrowType arrowType2 = new ArrowType(new JSTypeRegistry(), null, null);
        assertTrue(arrowType1.checkArrowEquivalenceHelper(arrowType2, false));
        
        ArrowType arrowType3 = new ArrowType(new JSTypeRegistry(), null, null);
        ArrowType arrowType4 = new ArrowType(new JSTypeRegistry(), null, new JSType(new JSTypeRegistry()));
        assertFalse(arrowType3.checkArrowEquivalenceHelper(arrowType4, false));
    }

    @Test
    public void testHashCode() {
        ArrowType arrowType1 = new ArrowType(new JSTypeRegistry(), null, null);
        ArrowType arrowType2 = new ArrowType(new JSTypeRegistry(), null, null);
        assertEquals(arrowType1.hashCode(), arrowType2.hashCode());
        
        ArrowType arrowType3 = new ArrowType(new JSTypeRegistry(), null, null);
        ArrowType arrowType4 = new ArrowType(new JSTypeRegistry(), null, new JSType(new JSTypeRegistry()));
        assertFalse(arrowType3.hashCode() == arrowType4.hashCode());
    }

    @Test
    public void testGetLeastSupertype() {
        ArrowType arrowType1 = new ArrowType(new JSTypeRegistry(), null, null);
        AssertionError expectedException = null;
        try {
            arrowType1.getLeastSupertype(new JSType(new JSTypeRegistry()));
        } catch (AssertionError e) {
            expectedException = e;
        }
        assertNotNull(expectedException);
    }

    @Test
    public void testGetGreatestSubtype() {
        ArrowType arrowType1 = new ArrowType(new JSTypeRegistry(), null, null);
        AssertionError expectedException = null;
        try {
            arrowType1.getGreatestSubtype(new JSType(new JSTypeRegistry()));
        } catch (AssertionError e) {
            expectedException = e;
        }
        assertNotNull(expectedException);
    }

    @Test
    public void testTestForEquality() {
        ArrowType arrowType1 = new ArrowType(new JSTypeRegistry(), null, null);
        AssertionError expectedException = null;
        try {
            arrowType1.testForEquality(new JSType(new JSTypeRegistry()));
        } catch (AssertionError e) {
            expectedException = e;
        }
        assertNotNull(expectedException);
    }

    @Test
    public void testVisit() {
        ArrowType arrowType1 = new ArrowType(new JSTypeRegistry(), null, null);
        AssertionError expectedException = null;
        try {
            arrowType1.visit(new Visitor<T>() {});
        } catch (AssertionError e) {
            expectedException = e;
        }
        assertNotNull(expectedException);
    }

    @Test
    public void testHasAnyTemplateInternal() {
        ArrowType arrowType1 = new ArrowType(new JSTypeRegistry(), null, null);
        assertFalse(arrowType1.hasAnyTemplateInternal());
        
        ArrowType arrowType2 = new ArrowType(new JSTypeRegistry(), new Node(), null);
        assertTrue(arrowType2.hasAnyTemplateInternal());
        
        ArrowType arrowType3 = new ArrowType(new JSTypeRegistry(), null, new JSType(new JSTypeRegistry()));
        assertTrue(arrowType3.hasAnyTemplateInternal());
    }
}