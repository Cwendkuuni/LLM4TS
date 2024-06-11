import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ArrowTypeTest {
    
    @Test
    public void testConstructorNoReturn() {
        ArrowType arrowType = new ArrowType(
            new JSTypeRegistry(),
            null,
            null
        );
        assertNull(arrowType.returnType);
        assertFalse(arrowType.returnTypeInferred);
        assertNull(arrowType.parameters);
    }

    @Test
    public void testConstructorWithReturn() {
        JSType returnType = new JSTypeRegistry().getNativeType("string");
        ArrowType arrowType = new ArrowType(
            new JSTypeRegistry(),
            null,
            returnType
        );
        assertEquals(returnType, arrowType.returnType);
        assertFalse(arrowType.returnTypeInferred);
        assertNull(arrowType.parameters);
    }

    @Test
    public void testConstructorWithParamsAndReturn() {
        JSTypeRegistry registry = new JSTypeRegistry();
        JSType returnType = registry.getNativeType("string");
        Node parameters = new Node();
        ArrowType arrowType = new ArrowType(
            registry,
            parameters,
            returnType
        );
        assertEquals(returnType, arrowType.returnType);
        assertFalse(arrowType.returnTypeInferred);
        assertEquals(parameters, arrowType.parameters);
    }

    @Test
    public void testConstructorWithParamsAndReturnInferred() {
        JSTypeRegistry registry = new JSTypeRegistry();
        JSType returnType = registry.getNativeType("string");
        Node parameters = new Node();
        ArrowType arrowType = new ArrowType(
            registry,
            parameters,
            returnType,
            true
        );
        assertEquals(returnType, arrowType.returnType);
        assertTrue(arrowType.returnTypeInferred);
        assertEquals(parameters, arrowType.parameters);
    }

    @Test
    public void testIsSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        JSType returnType1 = registry.getNativeType("string");
        JSType returnType2 = registry.getNativeType("number");
        JSType parameterType1 = registry.getNativeType("boolean");
        JSType parameterType2 = registry.getNativeType("string");
        Node parameters1 = new Node();
        parameters1.addChildrenToFront(new Node(null, parameterType1));
        Node parameters2 = new Node();
        parameters2.addChildrenToFront(new Node(null, parameterType2));

        ArrowType arrowType1 = new ArrowType(
            registry,
            parameters1,
            returnType1
        );
        ArrowType arrowType2 = new ArrowType(
            registry,
            parameters2,
            returnType2
        );

        assertTrue(arrowType1.isSubtype(arrowType1));
        assertFalse(arrowType1.isSubtype(arrowType2));
        assertFalse(arrowType2.isSubtype(arrowType1));
    }

    @Test
    public void testHasEqualParameters() {
        JSTypeRegistry registry = new JSTypeRegistry();
        JSType parameterType = registry.getNativeType("boolean");
        Node parameters1 = new Node();
        parameters1.addChildrenToFront(new Node(null, parameterType));
        Node parameters2 = new Node();
        parameters2.addChildrenToFront(new Node(null, parameterType));

        ArrowType arrowType1 = new ArrowType(
            registry,
            parameters1,
            registry.getNativeType("string")
        );
        ArrowType arrowType2 = new ArrowType(
            registry,
            parameters2,
            registry.getNativeType("number")
        );

        assertTrue(arrowType1.hasEqualParameters(arrowType1));
        assertTrue(arrowType1.hasEqualParameters(arrowType2));
        assertTrue(arrowType2.hasEqualParameters(arrowType1));
    }

    @Test
    public void testHashCode() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType arrowType = new ArrowType(
            registry,
            new Node(),
            registry.getNativeType("string")
        );
        assertEquals(arrowType.hashCode(), arrowType.hashCode());
    }
}