import static org.junit.Assert.*;
import org.junit.Test;

public class ArrowTypeTest {

    @Test
    public void testArrowTypeConstructor() {
        ArrowType arrowType = new ArrowType(registry, null, null);
        assertNotNull(arrowType);
    }

    @Test
    public void testIsSubtype() {
        ArrowType arrowType1 = new ArrowType(registry, null, null);
        ArrowType arrowType2 = new ArrowType(registry, null, null);
        assertTrue(arrowType1.isSubtype(arrowType2));
    }
    
    @Test
    public void testHasEqualParameters() {
        ArrowType arrowType1 = new ArrowType(registry, null, null);
        ArrowType arrowType2 = new ArrowType(registry, null, null);
        assertTrue(arrowType1.hasEqualParameters(arrowType2, false));
    }

    @Test
    public void testCheckArrowEquivalenceHelper() {
        ArrowType arrowType1 = new ArrowType(registry, null, null);
        ArrowType arrowType2 = new ArrowType(registry, null, null);
        assertTrue(arrowType1.checkArrowEquivalenceHelper(arrowType2, false));
    }

    @Test
    public void testHashCode() {
        ArrowType arrowType = new ArrowType(registry, null, null);
        assertNotNull(arrowType.hashCode());
    }

    @Test
    public void testResolveInternal() {
        ArrowType arrowType = new ArrowType(registry, null, null);
        assertNotNull(arrowType.resolveInternal(t, scope));
    }

    @Test
    public void testHasUnknownParamsOrReturn() {
        ArrowType arrowType = new ArrowType(registry, null, null);
        assertFalse(arrowType.hasUnknownParamsOrReturn());
    }

    @Test
    public void testToStringHelper() {
        ArrowType arrowType = new ArrowType(registry, null, null);
        assertNotNull(arrowType.toStringHelper(false));
    }

    @Test
    public void testHasAnyTemplateInternal() {
        ArrowType arrowType = new ArrowType(registry, null, null);
        assertFalse(arrowType.hasAnyTemplateInternal());
    }

}