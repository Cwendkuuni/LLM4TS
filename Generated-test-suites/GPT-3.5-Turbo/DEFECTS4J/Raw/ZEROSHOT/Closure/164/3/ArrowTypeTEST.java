import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;

public class ArrowTypeTest {

    @Test
    public void testIsSubtype() {
        // Test case 1
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = new Node();
        JSType returnType = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
        ArrowType arrowType1 = new ArrowType(registry, parameters, returnType);
        boolean result1 = arrowType1.isSubtype(arrowType1);
        assertTrue(result1);
    }

    @Test
    public void testHasEqualParameters() {
        // Test case 1
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = new Node();
        JSType returnType = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
        ArrowType arrowType1 = new ArrowType(registry, parameters, returnType);
        boolean result1 = arrowType1.hasEqualParameters(arrowType1);
        assertTrue(result1);
    }

    @Test
    public void testIsEquivalentTo() {
        // Test case 1
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = new Node();
        JSType returnType = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
        ArrowType arrowType1 = new ArrowType(registry, parameters, returnType);
        boolean result1 = arrowType1.isEquivalentTo(arrowType1);
        assertTrue(result1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetLeastSupertype() {
        // Test case 1
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = new Node();
        JSType returnType = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
        ArrowType arrowType = new ArrowType(registry, parameters, returnType);
        arrowType.getLeastSupertype(arrowType);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetGreatestSubtype() {
        // Test case 1
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = new Node();
        JSType returnType = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
        ArrowType arrowType = new ArrowType(registry, parameters, returnType);
        arrowType.getGreatestSubtype(arrowType);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testTestForEquality() {
        // Test case 1
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = new Node();
        JSType returnType = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
        ArrowType arrowType = new ArrowType(registry, parameters, returnType);
        arrowType.testForEquality(arrowType);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testVisit() {
        // Test case 1
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = new Node();
        JSType returnType = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
        ArrowType arrowType = new ArrowType(registry, parameters, returnType);
        arrowType.visit(new Visitor() {});
    }

    @Test
    public void testToStringHelper() {
        // Test case 1
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = new Node();
        JSType returnType = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
        ArrowType arrowType1 = new ArrowType(registry, parameters, returnType);
        String result1 = arrowType1.toStringHelper(true);
        assertNotNull(result1);
    }
}