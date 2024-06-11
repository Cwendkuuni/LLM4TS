import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;

import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TypeBindingsTest {

    @org.junit.Test
    public void testEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertTrue(bindings.isEmpty());
        assertEquals(0, bindings.size());
        assertNull(bindings.findBoundType("test"));
    }

    @org.junit.Test
    public void testCreate() {
        Class<List<String>> listOfStrings = List.of(String.class);
        TypeBindings bindings = TypeBindings.create(listOfStrings, Arrays.asList(JavaType.constructParametricType(String.class), JavaType.constructParametricType(Integer.class)));
        assertEquals(2, bindings.size());
        assertEquals("java.lang.Integer", bindings.getBoundName(1));
        assertEquals(JavaType.constructParametricType(Integer.class), bindings.getBoundType(1));
    }

    @org.junit.Test
    public void testCreateIfNeeded() {
        Class<List<String>> listOfStrings = List.of(String.class);
        TypeBindings bindings = TypeBindings.createIfNeeded(listOfStrings, null);
        assertEquals(1, bindings.size());
        assertEquals("java.lang.String", bindings.getBoundName(0));
        assertEquals(JavaType.constructParametricType(String.class), bindings.getBoundType(0));

        bindings = TypeBindings.createIfNeeded(listOfStrings, Arrays.asList(JavaType.constructParametricType(String.class), JavaType.constructParametricType(Integer.class)));
        assertEquals(2, bindings.size());
        assertEquals("java.lang.Integer", bindings.getBoundName(1));
        assertEquals(JavaType.constructParametricType(Integer.class), bindings.getBoundType(1));
    }

    @org.junit.Test
    public void testFindBoundType() {
        Class<List<String>> listOfStrings = List.of(String.class);
        TypeBindings bindings = TypeBindings.create(listOfStrings, Arrays.asList(JavaType.constructParametricType(String.class), JavaType.constructParametricType(Integer.class)));
        assertEquals("java.lang.Integer", bindings.getBoundName(1));
        assertEquals(JavaType.constructParametricType(Integer.class), bindings.findBoundType("java.lang.Integer"));
        assertNull(bindings.findBoundType("java.lang.Boolean"));
    }

    @org.junit.Test
    public void testWithUnboundVariable() {
        Class<List<String>> listOfStrings = List.of(String.class);
        TypeBindings bindings = TypeBindings.create(listOfStrings, Arrays.asList(JavaType.constructParametricType(String.class), JavaType.constructParametricType(Integer.class)));
        TypeBindings newBindings = bindings.withUnboundVariable("java.lang.Object");
        assertTrue(newBindings.hasUnbound("java.lang.Object"));
        assertFalse(bindings.hasUnbound("java.lang.Object"));
    }

    @org.junit.Test
    public void testToString() {
        Class<List<String>> listOfStrings = List.of(String.class);
        TypeBindings bindings = TypeBindings.create(listOfStrings, Arrays.asList(JavaType.constructParametricType(String.class), JavaType.constructParametricType(Integer.class)));
        assertEquals("java.util.List<java.lang.String, java.lang.Integer>", bindings.toString());
    }

    @org.junit.Test
    public void testHashCodeAndEquals() {
        Class<List<String>> listOfStrings = List.of(String.class);
        TypeBindings bindings1 = TypeBindings.create(listOfStrings, Arrays.asList(JavaType.constructParametricType(String.class), JavaType.constructParametricType(Integer.class)));
        TypeBindings bindings2 = TypeBindings.create(listOfStrings, Arrays.asList(JavaType.constructParametricType(String.class), JavaType.constructParametricType(Integer.class)));
        TypeBindings bindings3 = TypeBindings.create(ArrayList.class, Arrays.asList(JavaType.constructParametricType(String.class), JavaType.constructParametricType(Integer.class)));

        assertEquals(bindings1, bindings1);
        assertEquals(bindings1, bindings2);
        assertNotEquals(bindings1, bindings3);
    }

}