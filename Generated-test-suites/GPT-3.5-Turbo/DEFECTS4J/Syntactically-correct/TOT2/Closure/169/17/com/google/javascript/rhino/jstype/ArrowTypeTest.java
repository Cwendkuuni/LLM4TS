import static org.junit.Assert.*;
import org.junit.Test;

public class ArrowTypeTest {

    @Test
    public void testArrowTypeConstructor() {
        ArrowType arrowType = new ArrowType(registry, parameters, returnType);
        assertNotNull(arrowType);
    }

    @Test
    public void testIsSubtypeReturnsFalseForNonArrowType() {
        ArrowType arrowType1 = new ArrowType(registry, parameters, returnType);
        JSType otherType = new JSType();
        assertFalse(arrowType1.isSubtype(otherType));
    }

    @Test
    public void testIsSubtypeForCompatibleArrowTypes() {
        ArrowType arrowType1 = new ArrowType(registry, parameters, returnType);
        ArrowType arrowType2 = new ArrowType(registry, parameters, returnType);
        assertTrue(arrowType1.isSubtype(arrowType2));
    }

    @Test
    public void testIsSubtypeForIncompatibleArrowTypes() {
        Node parameters1 = new Node();
        Node parameters2 = new Node();
        parameters2.addChildToBack(new Node(Token.NAME));
        ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType);
        ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType);
        assertFalse(arrowType1.isSubtype(arrowType2));
    }

    @Test
    public void testHasEqualParametersReturnsTrueForEqualParameters() {
        ArrowType arrowType1 = new ArrowType(registry, parameters, returnType);
        ArrowType arrowType2 = new ArrowType(registry, parameters, returnType);
        assertTrue(arrowType1.hasEqualParameters(arrowType2, true));
    }

    @Test
    public void testCheckArrowEquivalenceHelperReturnsTrueForEqualArrowTypes() {
        ArrowType arrowType1 = new ArrowType(registry, parameters, returnType);
        ArrowType arrowType2 = new ArrowType(registry, parameters, returnType);
        assertTrue(arrowType1.checkArrowEquivalenceHelper(arrowType2, true));
    }

    @Test
    public void testHashCode() {
        ArrowType arrowType = new ArrowType(registry, parameters, returnType);
        assertNotNull(arrowType.hashCode());
    }

    @Test
    public void testResolveInternal() {
        ErrorReporter reporter = new ErrorReporter();
        StaticScope<JSType> scope = new StaticScope<>();
        ArrowType arrowType = new ArrowType(registry, parameters, returnType);
        assertNotNull(arrowType.resolveInternal(reporter, scope));
    }

}