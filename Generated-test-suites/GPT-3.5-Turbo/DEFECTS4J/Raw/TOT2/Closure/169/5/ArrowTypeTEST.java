import org.junit.Test;
import static org.junit.Assert.*;

public class ArrowTypeTest {

    @Test
    public void testArrowTypeSubtype() {
        // Test if one ArrowType is a subtype of another ArrowType
        ArrowType arrowType1 = new ArrowType(registry, parameters, returnType);
        ArrowType arrowType2 = new ArrowType(registry, parameters, returnType);

        assertTrue(arrowType1.isSubtype(arrowType2));
    }

    @Test
    public void testArrowTypeParameters() {
        // Test if ArrowType has equal parameters with another ArrowType
        ArrowType arrowType1 = new ArrowType(registry, parameters, returnType);
        ArrowType arrowType2 = new ArrowType(registry, parameters, returnType);

        assertTrue(arrowType1.hasEqualParameters(arrowType2, false));
    }

    @Test
    public void testArrowTypeEquivalence() {
        // Test if two ArrowTypes are equivalent in terms of return type and parameters
        ArrowType arrowType1 = new ArrowType(registry, parameters, returnType);
        ArrowType arrowType2 = new ArrowType(registry, parameters, returnType);

        assertTrue(arrowType1.checkArrowEquivalenceHelper(arrowType2, false));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testLeastSupertype() {
        // Test UnsupportedOperationException for getLeastSupertype method
        ArrowType arrowType = new ArrowType(registry, parameters, returnType);
        arrowType.getLeastSupertype(arrowType);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGreatestSubtype() {
        // Test UnsupportedOperationException for getGreatestSubtype method
        ArrowType arrowType = new ArrowType(registry, parameters, returnType);
        arrowType.getGreatestSubtype(arrowType);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEquality() {
        // Test UnsupportedOperationException for testForEquality method
        ArrowType arrowType = new ArrowType(registry, parameters, returnType);
        arrowType.testForEquality(arrowType);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testVisitor() {
        // Test UnsupportedOperationException for visit method
        ArrowType arrowType = new ArrowType(registry, parameters, returnType);
        arrowType.visit(visitor);
    }

}