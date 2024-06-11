import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;

public class TypeBindingsTest {

    

    @Test
    public void testEmptyBindings() {
        TypeBindings empty = TypeBindings.emptyBindings();
        assertTrue(empty.isEmpty());
        assertEquals(0, empty.size());
    }

    @Test
    public void testCreateWithNoTypeArguments() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = TypeBindings.create(erasedType, null);
        assertTrue(bindings.isEmpty());
        assertEquals(0, bindings.size());
    }

    @Test
    public void testCreateWithOneTypeArgument() {
        Class<?> erasedType = List.class;
        JavaType typeArg = JavaType.constructParametricType(List.class, Integer.class);
        TypeBindings bindings = TypeBindings.create(erasedType, typeArg);
        assertFalse(bindings.isEmpty());
        assertEquals(1, bindings.size());
        assertEquals("java.lang.Integer", bindings.getBoundName(0));
        assertEquals(typeArg, bindings.getBoundType(0));
    }

    @Test
    public void testCreateWithTwoTypeArguments() {
        Class<?> erasedType = Map.class;
        JavaType typeArg1 = JavaType.constructParametricType(Map.class, String.class, Integer.class);
        TypeBindings bindings = TypeBindings.create(erasedType, typeArg1);
        assertFalse(bindings.isEmpty());
        assertEquals(2, bindings.size());
        assertEquals("java.lang.String", bindings.getBoundName(0));
        assertEquals(typeArg1, bindings.getBoundType(0));
        assertEquals("java.lang.Integer", bindings.getBoundName(1));
        assertEquals(typeArg1.getComponentType(), bindings.getBoundType(1));
    }

    @Test
    public void testCreateIfNeededWithOneTypeArgument() {
        Class<?> erasedType = List.class;
        JavaType typeArg = JavaType.constructParametricType(List.class, Integer.class);
        TypeBindings bindings = TypeBindings.createIfNeeded(erasedType, typeArg);
        assertFalse(bindings.isEmpty());
        assertEquals(1, bindings.size());
        assertEquals("java.lang.Integer", bindings.getBoundName(0));
        assertEquals(typeArg, bindings.getBoundType(0));
    }

    @Test
    public void testCreateIfNeededWithTwoTypeArguments() {
        Class<?> erasedType = Map.class;
        JavaType typeArg1 = JavaType.constructParametricType(Map.class, String.class, Integer.class);
        TypeBindings bindings = TypeBindings.createIfNeeded(erasedType, typeArg1);
        assertFalse(bindings.isEmpty());
        assertEquals(2, bindings.size());
        assertEquals("java.lang.String", bindings.getBoundName(0));
        assertEquals(typeArg1, bindings.getBoundType(0));
        assertEquals("java.lang.Integer", bindings.getBoundName(1));
        assertEquals(typeArg1.getComponentType(), bindings.getBoundType(1));
    }

    @Test
    public void testFindBoundType() {
        Class<?> erasedType = Map.class;
        JavaType typeArg1 = JavaType.constructParametricType(Map.class, String.class, Integer.class);
        TypeBindings bindings = TypeBindings.create(erasedType, typeArg1);
        assertEquals(typeArg1, bindings.findBoundType("java.lang.Integer"));
        assertEquals(null, bindings.findBoundType("java.lang.String"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithMismatchedTypeArguments() {
        Class<?> erasedType = Map.class;
        JavaType typeArg1 = JavaType.constructParametricType(Map.class, String.class, Integer.class);
        JavaType[] incorrectTypes = new JavaType[] { typeArg1, JavaType.constructParametricType(List.class, String.class) };
        try {
            TypeBindings bindings = TypeBindings.create(erasedType, incorrectTypes);
            fail();
        } catch (IllegalArgumentException expected) {
            // expected exception
        }
    }

    
}