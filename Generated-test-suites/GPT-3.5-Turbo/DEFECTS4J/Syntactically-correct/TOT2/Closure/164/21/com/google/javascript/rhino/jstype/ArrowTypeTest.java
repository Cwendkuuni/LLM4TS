import static org.junit.Assert.*;
import org.junit.Test;

public class ArrowTypeTest {

    @Test
    public void testArrowTypeConstructor() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = new Node(Token.EMPTY);
        JSType returnType = new JSType(registry);

        ArrowType arrowType = new ArrowType(registry, parameters, returnType, false);

        assertNotNull(arrowType);
        assertEquals(parameters, arrowType.parameters);
        assertEquals(returnType, arrowType.returnType);
        assertFalse(arrowType.returnTypeInferred);
    }

    @Test
    public void testIsSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters1 = new Node(Token.EMPTY);
        JSType returnType1 = new JSType(registry);

        ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1, false);

        Node parameters2 = new Node(Token.EMPTY);
        JSType returnType2 = new JSType(registry);

        ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType2, false);

        assertTrue(arrowType1.isSubtype(arrowType2));
    }

    @Test
    public void testHashCode() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = new Node(Token.EMPTY);
        JSType returnType = new JSType(registry);

        ArrowType arrowType = new ArrowType(registry, parameters, returnType, false);

        int expectedHashCode = returnType.hashCode();

        assertEquals(expectedHashCode, arrowType.hashCode());
    }

    @Test
    public void testResolveInternal() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = new Node(Token.EMPTY);
        JSType returnType = new JSType(registry);

        ArrowType arrowType = new ArrowType(registry, parameters, returnType, false);

        ErrorReporter errorReporter = new ErrorReporter();
        StaticScope<JSType> scope = new StaticScope<>();

        JSType resolvedType = arrowType.resolveInternal(errorReporter, scope);

        assertEquals(arrowType, resolvedType);
    }

    @Test
    public void testToStringHelper() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = new Node(Token.EMPTY);
        JSType returnType = new JSType(registry);

        ArrowType arrowType = new ArrowType(registry, parameters, returnType, false);

        String expectedResult = arrowType.super.toString();

        assertEquals(expectedResult, arrowType.toStringHelper(false));
    }
}