import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertEquals(0, bindings.size());
        assertNull(bindings.findBoundType(null));
    }

    @Test
    public void testCreateWithSingleTypeParameter() {
        JavaType typeArg = TypeFactory.defaultInstance().constructType(String.class);
        TypeBindings bindings = TypeBindings.create(String[].class, typeArg);
        assertEquals(1, bindings.size());
        assertEquals("java.lang.String", bindings.getBoundName(0));
        assertEquals(typeArg, bindings.getBoundType(0));
    }

    @Test
    public void testCreateWithTwoTypeParameters() {
        JavaType typeArg1 = TypeFactory.defaultInstance().constructType(String.class);
        JavaType typeArg2 = TypeFactory.defaultInstance().constructType(Integer.class);
        TypeBindings bindings = TypeBindings.create(Map.class, typeArg1, typeArg2);
        assertEquals(2, bindings.size());
        assertEquals("java.lang.String", bindings.getBoundName(0));
        assertEquals(typeArg1, bindings.getBoundType(0));
        assertEquals("java.lang.Integer", bindings.getBoundName(1));
        assertEquals(typeArg2, bindings.getBoundType(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithMismatchedNumberOfTypeParameters() {
        JavaType[] types = new JavaType[1];
        String[] names = {"String", "Integer"};
        TypeBindings bindings = new TypeBindings(names, types, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithMismatchedNamesAndTypes() {
        JavaType[] types = new JavaType[]{TypeFactory.defaultInstance().constructType(Integer.class), TypeFactory.defaultInstance().constructType(String.class)};
        String[] names = {"String", "Integer"};
        TypeBindings bindings = new TypeBindings(names, types, null);
    }

    @Test
    public void testCreateIfNeededWithSingleTypeParameter() {
        JavaType typeArg = TypeFactory.defaultInstance().constructType(String.class);
        TypeBindings bindings = TypeBindings.createIfNeeded(String[].class, typeArg);
        assertEquals(1, bindings.size());
        assertEquals("java.lang.String", bindings.getBoundName(0));
        assertEquals(typeArg, bindings.getBoundType(0));
    }

    @Test
    public void testCreateIfNeededWithTwoTypeParameters() {
        JavaType typeArg1 = TypeFactory.defaultInstance().constructType(String.class);
        JavaType typeArg2 = TypeFactory.defaultInstance().constructType(Integer.class);
        TypeBindings bindings = TypeBindings.createIfNeeded(Map.class, typeArg1, typeArg2);
        assertEquals(2, bindings.size());
        assertEquals("java.lang.String", bindings.getBoundName(0));
        assertEquals(typeArg1, bindings.getBoundType(0));
        assertEquals("java.lang.Integer", bindings.getBoundName(1));
        assertEquals(typeArg2, bindings.getBoundType(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateIfNeededWithMismatchedTypeArguments() {
        JavaType typeArg1 = TypeFactory.defaultInstance().constructType(String.class);
        JavaType typeArg2 = TypeFactory.defaultInstance().constructType(List.class);
        TypeBindings bindings = TypeBindings.createIfNeeded(List.class, typeArg1, typeArg2);
    }

    @Test
    public void testFindBoundType() {
        JavaType[] types = new JavaType[]{TypeFactory.defaultInstance().constructType(String.class),
                TypeFactory.defaultInstance().constructType(Integer.class)};
        TypeBindings bindings = new TypeBindings(new String[]{"String", "Integer"}, types, null);
        assertEquals(TypeFactory.defaultInstance().constructType(String.class), bindings.findBoundType("String"));
        assertEquals(TypeFactory.defaultInstance().constructType(Integer.class), bindings.findBoundType("Integer"));
    }

    @Test
    public void testFindBoundTypeNull() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertNull(bindings.findBoundType(null));
    }

    @Test
    public void testIsEmpty() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertTrue(bindings.isEmpty());
        TypeBindings nonEmptyBindings = new TypeBindings(new String[]{"String"}, new JavaType[0], null);
        assertFalse(bindings.isEmpty());
    }

    @Test
    public void testSize() {
        TypeBindings bindings = new TypeBindings(new String[]{"String", "Integer"}, new JavaType[]{TypeFactory.defaultInstance().constructType(String.class), TypeFactory.defaultInstance().constructType(Integer.class)}, null);
        assertEquals(2, bindings.size());
    }

    @Test
    public void testGetBoundName() {
        JavaType[] types = new JavaType[]{TypeFactory.defaultInstance().constructType(String.class),
                TypeFactory.defaultInstance().constructType(Integer.class)};
        TypeBindings bindings = new TypeBindings(new String[]{"String", "Integer"}, types, null);
        assertEquals("String", bindings.getBoundName(0));
        assertEquals("Integer", bindings.getBoundName(1));
    }

    @Test
    public void testGetBoundType() {
        JavaType[] types = new JavaType[]{TypeFactory.defaultInstance().constructType(String.class),
                TypeFactory.defaultInstance().constructType(Integer.class)};
        TypeBindings bindings = new TypeBindings(new String[]{"String", "Integer"}, types, null);
        assertEquals(TypeFactory.defaultInstance().constructType(String.class), bindings.getBoundType(0));
        assertEquals(TypeFactory.defaultInstance().constructType(Integer.class), bindings.getBoundType(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetBoundTypeInvalidIndex() {
        JavaType[] types = new JavaType[]{TypeFactory.defaultInstance().constructType(String.class),
                TypeFactory.defaultInstance().constructType(Integer.class)};
        TypeBindings bindings = new TypeBindings(new String[]{"String", "Integer"}, types, null);
        bindings.getBoundType(3);
    }

    @Test
    public void testGetTypeParameters() {
        JavaType[] types = new JavaType[]{TypeFactory.defaultInstance().constructType(String.class),
                TypeFactory.defaultInstance().constructType(Integer.class)};
        TypeBindings bindings = new TypeBindings(new String[]{"String", "Integer"}, types, null);
        List<JavaType> boundTypes = bindings.getTypeParameters();
        assertEquals(2, boundTypes.size());
        assertEquals(TypeFactory.defaultInstance().constructType(String.class), boundTypes.get(0));
        assertEquals(TypeFactory.defaultInstance().constructType(Integer.class), boundTypes.get(1));
    }

    @Test
    public void testHasUnbound() {
        JavaType[] types = new JavaType[]{TypeFactory.defaultInstance().constructType(String.class),
                TypeFactory.defaultInstance().constructType(Integer.class)};
        TypeBindings bindings = new TypeBindings(new String[]{"String", "Integer"}, types, null);
        assertFalse(bindings.hasUnbound("String"));
        assertFalse(bindings.hasUnbound("Integer"));

        TypeBindings unboundBindings = new TypeBindings(new String[]{"String", "Integer"}, types, new String[]{"T", "U"});
        assertTrue(unboundBindings.hasUnbound("T"));
        assertTrue(unboundBindings.hasUnbound("U"));
    }

    @Test
    public void testWithUnboundVariable() {
        JavaType[] types = new JavaType[]{TypeFactory.defaultInstance().constructType(String.class),
                TypeFactory.defaultInstance().constructType(Integer.class)};
        TypeBindings bindings = new TypeBindings(new String[]{"String", "Integer"}, types, null);
        TypeBindings unboundBindings = bindings.withUnboundVariable("T");
        assertEquals(3, unboundBindings.size());
        assertEquals("String", unboundBindings.getBoundName(0));
        assertEquals("Integer", unboundBindings.getBoundName(1));
        assertEquals("T", unboundBindings.getBoundName(2));
    }
}