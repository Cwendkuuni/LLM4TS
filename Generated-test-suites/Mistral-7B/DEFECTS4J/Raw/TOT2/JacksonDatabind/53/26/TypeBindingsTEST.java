import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings empty = TypeBindings.emptyBindings();
        assertNotNull(empty);
        assertNull(empty.findBoundType("T"));
        assertFalse(empty.hasUnbound("T"));
        assertFalse(empty.isEmpty());
        assertEquals(0, empty.size());
    }

    @Test
    public void testCreateWithNoTypes() {
        TypeBindings noTypes = TypeBindings.create(List.class, null);
        assertNotNull(noTypes);
        assertNull(noTypes.findBoundType("T"));
        assertTrue(noTypes.isEmpty());
        assertEquals(0, noTypes.size());
    }

    @Test
    public void testCreateWithOneType() {
        TypeBindings oneType = TypeBindings.create(List.class, Integer.TYPE);
        assertNotNull(oneType);
        assertNotNull(oneType.findBoundType("T"));
        assertEquals(Integer.TYPE, oneType.findBoundType("T"));
        assertFalse(oneType.isEmpty());
        assertEquals(1, oneType.size());
    }

    @Test
    public void testCreateWithTwoTypes() {
        TypeBindings twoTypes = TypeBindings.create(Map.class, Integer.TYPE, String.class);
        assertNotNull(twoTypes);
        assertNotNull(twoTypes.findBoundType("K"));
        assertNotNull(twoTypes.findBoundType("V"));
        assertEquals(Integer.TYPE, twoTypes.findBoundType("K"));
        assertEquals(String.class, twoTypes.findBoundType("V"));
        assertFalse(twoTypes.isEmpty());
        assertEquals(2, twoTypes.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithMismatchedTypes() {
        TypeBindings bindings = TypeBindings.create(List.class, Integer.TYPE, String[].class);
    }

    @Test
    public void testCreateIfNeeded() {
        TypeBindings noTypes = TypeBindings.createIfNeeded(List.class, null);
        assertNotNull(noTypes);
        assertNull(noTypes.findBoundType("T"));
        assertTrue(noTypes.isEmpty());
        assertEquals(0, noTypes.size());

        TypeBindings oneType = TypeBindings.createIfNeeded(List.class, Integer.TYPE);
        assertNotNull(oneType);
        assertNotNull(oneType.findBoundType("T"));
        assertEquals(Integer.TYPE, oneType.findBoundType("T"));
        assertFalse(oneType.isEmpty());
        assertEquals(1, oneType.size());
    }

    @Test
    public void testCreateWithUnboundVariables() {
        TypeBindings bindings = TypeBindings.create(Map.class, Integer.TYPE, String.class, "K");
        assertNotNull(bindings);
        assertNotNull(bindings.findBoundType("K"));
        assertEquals(Integer.TYPE, bindings.findBoundType("K"));
        assertNotNull(bindings.findBoundType("V"));
        assertNull(bindings.findBoundType("T"));
        assertTrue(bindings.hasUnbound("T"));
        assertFalse(bindings.isEmpty());
        assertEquals(2, bindings.size());
    }

    @Test
    public void testWithUnboundVariable() {
        TypeBindings bindings = TypeBindings.create(List.class, String.class);
        TypeBindings withUnbound = bindings.withUnboundVariable("T");
        assertNotNull(withUnbound);
        assertNull(withUnbound.findBoundType("T"));
        assertTrue(withUnbound.hasUnbound("T"));
        assertFalse(withUnbound.isEmpty());
        assertEquals(1, withUnbound.size());
    }

    @Test
    public void testTypeParameters() {
        TypeBindings bindings = TypeBindings.create(List.class, Integer.TYPE, String.class);
        List<JavaType> types = bindings.getTypeParameters();
        assertNotNull(types);
        assertEquals(2, types.size());
        JavaType intType = types.get(0);
        JavaType stringType = types.get(1);
        assertNotNull(intType);
        assertEquals(Integer.TYPE, intType.getRawClass());
        assertNotNull(stringType);
        assertEquals(String.class, stringType.getRawClass());
    }

    @Test
    public void testHashCodeEquals() {
        TypeBindings bindings1 = TypeBindings.create(List.class, Integer.TYPE, String.class);
        TypeBindings bindings2 = TypeBindings.create(List.class, Integer.TYPE, String.class);
        TypeBindings bindings3 = new TypeBindings(new String[]{"T1", "T2"}, new JavaType[2], null);

        assertEquals(bindings1.hashCode(), bindings1.hashCode());
        assertEquals(bindings1.hashCode(), bindings2.hashCode());
        assertNotEquals(bindings1.hashCode(), bindings3.hashCode());

        assertFalse(bindings1.equals(null));
        assertFalse(bindings1.equals(""));
        assertFalse(bindings1.equals(new Object()));

        assertTrue(bindings1.equals(bindings1));
        assertTrue(bindings1.equals(bindings2));
        assertFalse(bindings1.equals(bindings3));
    }

    @Test
    public void testToString() {
        TypeBindings bindings = TypeBindings.create(List.class, Integer.TYPE, String.class);
        assertNotNull(bindings.toString());
        String string = bindings.toString();
        assertFalse(string.isEmpty());
        assertTrue(string.startsWith("<"));
        assertTrue(string.endsWith(">"));
        assertTrue(string.contains("List<Integer,String>"));
    }
}