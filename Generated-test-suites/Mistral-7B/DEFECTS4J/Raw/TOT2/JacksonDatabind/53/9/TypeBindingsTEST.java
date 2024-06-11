import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertEquals("[]", bindings.toString());
        assertNull(bindings.findBoundType(""));
        assertNull(bindings.findBoundType("Unknown"));
        assertEquals(0, bindings.size());
        assertEquals("<>", bindings.getTypeParameters().get(0));
    }

    @Test
    public void testCreateBindingsForOneTypeParameter() {
        TypeBindings bindings = TypeBindings.create(String[].class, JavaType.constructCollectionType(ArrayList.class, String.class));
        assertEquals("[Ljava.lang.String;", bindings.toString());
        assertNotNull(bindings.findBoundType("T"));
        assertEquals(String.class, bindings.findBoundType("T").getRawClass());
        assertEquals(1, bindings.size());
        assertEquals("T", bindings.getBoundName(0));
        assertEquals(JavaType.constructCollectionType(ArrayList.class, String.class), bindings.getBoundType(0));
    }

    @Test
    public void testCreateBindingsForTwoTypeParameters() {
        TypeBindings bindings = TypeBindings.create(List.class, JavaType.constructTypeVariableType("K"), JavaType.constructTypeVariableType("V"));
        assertEquals("<K, V>", bindings.toString());
        assertNotNull(bindings.findBoundType("K"));
        assertEquals(JavaType.constructTypeVariableType("K"), bindings.findBoundType("K"));
        assertNotNull(bindings.findBoundType("V"));
        assertEquals(JavaType.constructTypeVariableType("V"), bindings.findBoundType("V"));
        assertEquals(2, bindings.size());
        assertEquals("K", bindings.getBoundName(0));
        assertEquals("V", bindings.getBoundName(1));
        assertEquals(JavaType.constructTypeVariableType("K"), bindings.getBoundType(0));
        assertEquals(JavaType.constructTypeVariableType("V"), bindings.getBoundType(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateBindingsWithWrongNumberOfTypeParameters() {
        TypeBindings bindings = TypeBindings.create(List.class);
    }

    @Test
    public void testCreateBindingsWithNullTypes() {
        TypeBindings bindings = TypeBindings.create(List.class, null);
        assertEquals("<>", bindings);
        assertEquals(0, bindings.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateBindingsWithWrongTypes() {
        TypeBindings bindings = TypeBindings.create(List.class, JavaType.constructTypeVariableType("K"), new TypeVariable<List<Integer>>("X") {}.getGenericComponentType());
    }

    @Test
    public void testCreateBindingsWithSubclassTypes() {
        // Test that binding creation does not create duplicate bindings for subclass types
        TypeBindings bindings1 = TypeBindings.create(List.class, JavaType.constructTypeVariableType("K"), JavaType.constructTypeVariableType("V"));
        TypeBindings bindings2 = TypeBindings.create(ArrayList.class, JavaType.constructTypeVariableType("K"), JavaType.constructTypeVariableType("V"));
        assertEquals(bindings1, bindings2);
    }

    @Test
    public void testCreateBindingsWithGenericTypes() {
        // Test that creating bindings for generic types (such as ArrayList<T>)
        // creates a single binding that covers all possible bound types
        TypeBindings bindings1 = TypeBindings.create(ArrayList.class, JavaType.constructTypeVariableType("T"));
        TypeBindings bindings2 = TypeBindings.create(List.class, JavaType.constructTypeVariableType("T"), JavaType.constructTypeVariableType("T"));
        assertEquals(bindings1, bindings2);
    }

    @Test
    public void testWithUnboundVariable() {
        TypeBindings bindings = TypeBindings.create(ArrayList.class, JavaType.constructTypeVariableType("T"));
        TypeBindings bindingsWithUnbound = bindings.withUnboundVariable("X");
        assertNotEquals(bindings, bindingsWithUnbound);
        assertEquals(bindingsWithUnbound.toString(), "[Ljava.lang.Object;[T, X]");
        assertNotNull(bindingsWithUnbound.findBoundType("X"));
        assertNull(bindingsWithUnbound.findBoundType("T"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithUnboundVariableWithInvalidName() {
        TypeBindings bindings = TypeBindings.create(ArrayList.class, JavaType.constructTypeVariableType("T"));
        bindings.withUnboundVariable("X"); // Valid call
        bindings.withUnboundVariable("X"); // Invalid call
    }
}