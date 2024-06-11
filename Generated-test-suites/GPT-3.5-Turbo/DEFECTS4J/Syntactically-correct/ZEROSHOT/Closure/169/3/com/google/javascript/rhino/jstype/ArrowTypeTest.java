import static org.junit.Assert.*;
import org.junit.Test;

public class ArrowTypeTest {

    @Test
    public void testIsSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType type1 = new ArrowType(registry, null, null);
        ArrowType type2 = new ArrowType(registry, null, null);
        assertTrue(type1.isSubtype(type2));
    }

    @Test
    public void testIsSubtype2() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType type1 = new ArrowType(registry, null, null);
        ArrowType type2 = new ArrowType(registry, null, null);
        assertFalse(type1.isSubtype(null));
    }

    @Test
    public void testHasEqualParameters() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType type1 = new ArrowType(registry, null, null);
        ArrowType type2 = new ArrowType(registry, null, null);
        assertTrue(type1.hasEqualParameters(type2, true));
    }

    @Test
    public void testCheckArrowEquivalenceHelper() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType type1 = new ArrowType(registry, null, null);
        ArrowType type2 = new ArrowType(registry, null, null);
        assertTrue(type1.checkArrowEquivalenceHelper(type2, true));
    }

    @Test
    public void testHashCode() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType type = new ArrowType(registry, null, null);
        assertNotNull(type.hashCode());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetLeastSupertype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType type1 = new ArrowType(registry, null, null);
        ArrowType type2 = new ArrowType(registry, null, null);
        type1.getLeastSupertype(type2);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetGreatestSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType type1 = new ArrowType(registry, null, null);
        ArrowType type2 = new ArrowType(registry, null, null);
        type1.getGreatestSubtype(type2);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testTestForEquality() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType type1 = new ArrowType(registry, null, null);
        ArrowType type2 = new ArrowType(registry, null, null);
        type1.testForEquality(type2);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testVisit() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType type = new ArrowType(registry, null, null);
        type.visit(null);
    }
    
    @Test
    public void testGetPossibleToBooleanOutcomes() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType type = new ArrowType(registry, null, null);
        assertNotNull(type.getPossibleToBooleanOutcomes());
    }
    
    @Test
    public void testResolveInternal() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType type = new ArrowType(registry, null, null);
        assertNotNull(type.resolveInternal(null, null));
    }
    
    @Test
    public void testHasUnknownParamsOrReturn() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType type = new ArrowType(registry, null, null);
        assertFalse(type.hasUnknownParamsOrReturn());
    }
    
    @Test
    public void testToStringHelper() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType type = new ArrowType(registry, null, null);
        assertNotNull(type.toStringHelper(true));
    }
    
    @Test
    public void testHasAnyTemplateInternal() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType type = new ArrowType(registry, null, null);
        assertFalse(type.hasAnyTemplateInternal());
    }
}