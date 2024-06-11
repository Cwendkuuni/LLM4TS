import org.junit.Test;
import static org.junit.Assert.*;

public class ArrowTypeTest {

    @Test
    public void testArrowTypeConstructor() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = new Node(Token.EMPTY);
        JSType returnType = registry.getNativeType(UNKNOWN_TYPE);

        ArrowType arrowType = new ArrowType(registry, parameters, returnType);

        assertNotNull(arrowType);
        assertEquals(parameters, arrowType.parameters);
        assertEquals(returnType, arrowType.returnType);
        assertFalse(arrowType.returnTypeInferred);
    }

    @Test
    public void testArrowTypeIsSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters1 = new Node(Token.EMPTY);
        JSType returnType1 = registry.getNativeType(UNKNOWN_TYPE);

        ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1);

        Node parameters2 = new Node(Token.EMPTY);
        JSType returnType2 = registry.getNativeType(UNKNOWN_TYPE);

        ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType2);

        assertTrue(arrowType1.isSubtype(arrowType2));
    }

    @Test
    public void testArrowTypeHasEqualParameters() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters1 = new Node(Token.EMPTY);
        JSType returnType1 = registry.getNativeType(UNKNOWN_TYPE);

        ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1);

        Node parameters2 = new Node(Token.EMPTY);
        JSType returnType2 = registry.getNativeType(UNKNOWN_TYPE);

        ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType2);

        assertTrue(arrowType1.hasEqualParameters(arrowType2));
    }

    @Test
    public void testArrowTypeIsEquivalentTo() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters1 = new Node(Token.EMPTY);
        JSType returnType1 = registry.getNativeType(UNKNOWN_TYPE);

        ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1);

        Node parameters2 = new Node(Token.EMPTY);
        JSType returnType2 = registry.getNativeType(UNKNOWN_TYPE);

        ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType2);

        assertTrue(arrowType1.isEquivalentTo(arrowType2));
    }

    @Test
    public void testArrowTypeHashCode() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = new Node(Token.EMPTY);
        JSType returnType = registry.getNativeType(UNKNOWN_TYPE);

        ArrowType arrowType = new ArrowType(registry, parameters, returnType);

        assertNotNull(arrowType.hashCode());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testArrowTypeResolveInternal() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = new Node(Token.EMPTY);
        JSType returnType = registry.getNativeType(UNKNOWN_TYPE);

        ArrowType arrowType = new ArrowType(registry, parameters, returnType);

        arrowType.resolveInternal(new ErrorReporter(), new StaticScope<JSType>());
    }
}