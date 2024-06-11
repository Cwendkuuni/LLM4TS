
import static org.junit.Assert.*;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import org.junit.Before;
import org.junit.Test;

public class ArrowTypeTest {

    private ArrowType arrowType;

    @Before
    public void setUp() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = registry.createParametersWithVarArgs(
                registry.getNativeType(JSTypeNative.UNKNOWN_TYPE)
        );
        JSType returnType = registry.getNativeType(JSTypeNative.UNKNOWN_TYPE);
        arrowType = new ArrowType(registry, parameters, returnType);
    }

    @Test
    public void testIsSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = registry.createParametersWithVarArgs(
                registry.getNativeType(JSTypeNative.NUMBER_TYPE)
        );
        JSType returnType = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
        ArrowType subtype = new ArrowType(registry, parameters, returnType);

        assertTrue(subtype.isSubtype(arrowType));
        assertFalse(arrowType.isSubtype(subtype));
    }

    @Test
    public void testHasEqualParameters() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = registry.createParametersWithVarArgs(
                registry.getNativeType(JSTypeNative.NUMBER_TYPE)
        );
        JSType returnType = registry.getNativeType(JSTypeNative.UNKNOWN_TYPE);
        ArrowType otherType = new ArrowType(registry, parameters, returnType);

        assertTrue(arrowType.hasEqualParameters(otherType, true));
    }

    @Test
    public void testCheckArrowEquivalenceHelper() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = registry.createParametersWithVarArgs(
                registry.getNativeType(JSTypeNative.NUMBER_TYPE)
        );
        JSType returnType = registry.getNativeType(JSTypeNative.STRING_TYPE);
        ArrowType otherType = new ArrowType(registry, parameters, returnType);

        assertFalse(arrowType.checkArrowEquivalenceHelper(otherType, false));
        assertTrue(arrowType.checkArrowEquivalenceHelper(otherType, true));
    }

    @Test
    public void testHashCode() {
        int expectedHashCode = 0;
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = registry.createParametersWithVarArgs(
                registry.getNativeType(JSTypeNative.UNKNOWN_TYPE)
        );
        JSType returnType = registry.getNativeType(JSTypeNative.UNKNOWN_TYPE);
        ArrowType testType = new ArrowType(registry, parameters, returnType);

        assertEquals(expectedHashCode, testType.hashCode());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetLeastSupertype() {
        arrowType.getLeastSupertype(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetGreatestSubtype() {
        arrowType.getGreatestSubtype(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testTestForEquality() {
        arrowType.testForEquality(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testVisit() {
        arrowType.visit(null);
    }

    @Test
    public void testGetPossibleToBooleanOutcomes() {
        assertEquals(BooleanLiteralSet.TRUE, arrowType.getPossibleToBooleanOutcomes());
    }

    @Test
    public void testResolveInternal() {
        ErrorReporter errorReporter = null;
        StaticScope<JSType> scope = null;
        arrowType.resolveInternal(errorReporter, scope);
    }

    @Test
    public void testHasUnknownParamsOrReturn() {
        assertFalse(arrowType.hasUnknownParamsOrReturn());
    }

    @Test
    public void testToStringHelper() {
        assertEquals("[ArrowType]", arrowType.toStringHelper(false));
    }

    @Test
    public void testHasAnyTemplateInternal() {
        assertFalse(arrowType.hasAnyTemplateInternal());
    }
}
