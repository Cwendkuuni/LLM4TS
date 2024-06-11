import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeParameterized;
import com.fasterxml.jackson.databind.type.TypeUtil;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertEquals(0, bindings.size());
        assertTrue(bindings.isEmpty());
    }

    @Test
    public void testCreate() {
        Class<List<Integer>> listClass = new ArrayList<>() {}.getClass();
        JavaType[] types = new JavaType[] { TypeUtil.handleNullableType(JavaType.getType(int[].class)), TypeUtil.handleNullableType(JavaType.getType(Integer.class)) };
        TypeBindings bindings = TypeBindings.create(listClass, types);
        assertEquals(2, bindings.size());
        assertFalse(bindings.isEmpty());
        assertEquals("java.lang.Integer", bindings.getBoundName(0));
        assertEquals(JavaType.getType(Integer.class), bindings.getBoundType(0));
        assertEquals("T[]", bindings.getBoundName(1));
        assertEquals(JavaType.getType(int[].class), bindings.getBoundType(1));
    }

    @Test
    public void testCreateWithNullTypes() {
        Class<List<Integer>> listClass = new ArrayList<>() {}.getClass();
        TypeBindings bindings = TypeBindings.create(listClass, null);
        assertEquals(0, bindings.size());
        assertTrue(bindings.isEmpty());
    }

    @Test
    public void testCreateWithNullTypeList() {
        Class<List<Integer>> listClass = new ArrayList<>() {}.getClass();
        TypeBindings bindings = TypeBindings.create(listClass, (List<JavaType>) null);
        assertEquals(0, bindings.size());
        assertTrue(bindings.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidNumberOfTypeArguments() {
        Class<List<Integer>> listClass = new ArrayList<>() {}.getClass();
        JavaType[] types = new JavaType[] { TypeUtil.handleNullableType(JavaType.getType(int[].class)), TypeUtil.handleNullableType(JavaType.getType(Integer.class)), TypeUtil.handleNullableType(JavaType.getType(String.class)) };
        TypeBindings bindings = TypeBindings.create(listClass, types);
        fail("Expected IllegalArgumentException but got: " + bindings);
    }

    @Test
    public void testCreateWithUnmatchedNumberOfTypeArguments() {
        Class<Map<String, Integer>> mapClass = HashMap.class;
        JavaType[] types = new JavaType[] { TypeUtil.handleNullableType(JavaType.getType(String.class)), TypeUtil.handleNullableType(JavaType.getType(Integer.class)) };
        try {
            TypeBindings bindings = TypeBindings.create(mapClass, types);
            fail("Expected IllegalArgumentException but got: " + bindings);
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testCreateIfNeeded() {
        Class<ArrayList<Integer>> listClass = ArrayList.class;
        JavaType types = JavaType.getType(List.class);
        TypeBindings bindings = TypeBindings.createIfNeeded(listClass, types);
        assertEquals(1, bindings.size());
        assertFalse(bindings.isEmpty());
        assertEquals("T", bindings.getBoundName(0));
        assertEquals(types, bindings.getBoundType(0));
    }

    @Test
    public void testCreateIfNeededWithNullTypes() {
        Class<ArrayList<Integer>> listClass = ArrayList.class;
        TypeBindings bindings = TypeBindings.createIfNeeded(listClass, null);
        assertEquals(0, bindings.size());
        assertTrue(bindings.isEmpty());
    }

    @Test
    public void testFindBoundType() {
        Class<Map<String, Integer>> mapClass = HashMap.class;
        TypeBindings bindings = TypeBindings.create(mapClass, new TypeParameterized(String.class, Integer.class));
        assertEquals(JavaType.getType(String.class), bindings.findBoundType("K"));
        assertEquals(JavaType.getType(Integer.class), bindings.findBoundType("V"));
        assertNull(bindings.findBoundType("X"));
    }

    @Test
    public void testHasUnbound() {
        Class<Map<String, Integer>> mapClass = HashMap.class;
        TypeBindings bindings = TypeBindings.create(mapClass, new TypeParameterized(String.class, Integer.class));
        assertTrue(bindings.hasUnbound("X"));
        assertFalse(bindings.hasUnbound("K"));
        assertFalse(bindings.hasUnbound("V"));
    }

    @Test
    public void testWithUnboundVariable() {
        String name = "X";
        TypeBindings bindings = TypeBindings.create(Object.class, JavaType.getType(List.class));
        TypeBindings newBindings = bindings.withUnboundVariable(name);
        assertTrue(newBindings.hasUnbound(name));
        assertFalse(bindings.hasUnbound(name));
    }
}