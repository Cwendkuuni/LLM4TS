import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeParameter;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertEquals(0, bindings.size());
        assertTrue(bindings.isEmpty());
    }

    @Test
    public void testCreateWithOneTypeParameter() {
        Class<List<String>> listClass = new ArrayList<>() {}.getClass();
        JavaType javaType = TypeFactory.defaultInstance().constructType(listClass);
        TypeBindings bindings = TypeBindings.create(listClass, javaType);

        assertEquals(1, bindings.size());
        String name = TypeParameter.INTERNAL_TYPE_PARAMETER_NAME;
        JavaType boundType = bindings.findBoundType(name);
        assertEquals(JavaType.constructCollectionType(List.class, String.class), boundType);
    }

    @Test
    public void testCreateWithTwoTypeParameters() {
        Class<Map<String, List<Integer>>> mapClass = new HashMap<>() {}.getClass();
        JavaType mapType = TypeFactory.defaultInstance().constructType(mapClass);
        TypeBindings bindings = TypeBindings.create(mapClass, mapType);

        assertEquals(2, bindings.size());
        String name1 = "K";
        JavaType keyType = bindings.findBoundType(name1);
        String name2 = "V";
        JavaType valueType = bindings.findBoundType(name2);
        assertEquals(String.class, keyType.getRawClass());
        assertEquals(JavaType.constructCollectionType(List.class, Integer.class), valueType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithMismatchedParameters() {
        Class<List<String>> listClass = new ArrayList<>() {}.getClass();
        JavaType javaType1 = TypeFactory.defaultInstance().constructType(List.class, Integer.class);
        TypeBindings bindings = TypeBindings.create(listClass, javaType1);

        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testCreateIfNeededWithOneTypeParameter() {
        Class<List<String>> listClass = new ArrayList<>() {}.getClass();
        JavaType javaType = TypeFactory.defaultInstance().constructType(listClass);
        TypeBindings bindings = TypeBindings.createIfNeeded(listClass, javaType);

        assertEquals(1, bindings.size());
        String name = TypeParameter.INTERNAL_TYPE_PARAMETER_NAME;
        JavaType boundType = bindings.findBoundType(name);
        assertEquals(JavaType.constructCollectionType(List.class, String.class), boundType);
    }

    @Test
    public void testCreateIfNeededWithNoTypeParameters() {
        Class<String> stringClass = String.class;
        TypeBindings bindings = TypeBindings.createIfNeeded(stringClass, null);

        assertEquals(0, bindings.size());
        assertTrue(bindings.isEmpty());
    }

    @Test
    public void testCreateIfNeededWithTwoTypeParameters() {
        Class<Map<String, List<Integer>>> mapClass = new HashMap<>() {}.getClass();
        JavaType mapType = TypeFactory.defaultInstance().constructType(mapClass);
        TypeBindings bindings = TypeBindings.createIfNeeded(mapClass, mapType);

        assertEquals(2, bindings.size());
        String name1 = "K";
        JavaType keyType = bindings.findBoundType(name1);
        String name2 = "V";
        JavaType valueType = bindings.findBoundType(name2);
        assertEquals(String.class, keyType.getRawClass());
        assertEquals(JavaType.constructCollectionType(List.class, Integer.class), valueType);
    }

    @Test
    public void testWithUnboundVariable() {
        String[] names = {"T", "U"};
        JavaType[] types = {String.class, List.class};
        TypeBindings bindings = new TypeBindings(names, types, null);
        TypeBindings bindingsWithUUnbound = bindings.withUnboundVariable("U");

        assertEquals(names.length, bindingsWithUUnbound._names.length);
        assertEquals(types.length, bindingsWithUUnbound._types.length);
        assertTrue(bindingsWithUUnbound.hasUnbound("U"));
        assertFalse(bindingsWithUUnbound.hasUnbound("T"));
    }
}