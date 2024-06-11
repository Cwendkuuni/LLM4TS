import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings empty = TypeBindings.emptyBindings();
        assertEquals(0, empty.size());
        assertNull(empty.findBoundType("foo"));
    }

    @Test
    public void testCreate() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType intType = TypeFactory.defaultInstance().constructType(int.class);

        TypeBindings bindings = TypeBindings.create(List.class, stringType, intType);
        assertEquals(2, bindings.size());
        assertSame(stringType, bindings.getBoundType(0));
        assertSame(intType, bindings.getBoundType(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateInvalidNumberOfTypeArgs() {
        JavaType[] types = new JavaType[3];
        TypeBindings bindings = TypeBindings.create(List.class, types);
    }

    @Test
    public void testCreateIfNeeded() {
        JavaType listType = TypeFactory.defaultInstance().constructType(List.class);
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);

        TypeBindings bindings = TypeBindings.createIfNeeded(List.class, stringType);
        assertEquals(1, bindings.size());
        assertSame(stringType, bindings.getBoundType(0));
    }

    @Test
    public void testCreateIfNeededEmpty() {
        TypeBindings bindings = TypeBindings.createIfNeeded(List.class, null);
        assertEquals(0, bindings.size());
    }

    @Test
    public void testWithUnboundVariable() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType intType = TypeFactory.defaultInstance().constructType(int.class);

        TypeBindings bindings = TypeBindings.create(List.class, stringType, intType);
        TypeBindings bindingsWithUnbound = bindings.withUnboundVariable("foo");
        assertEquals(3, bindingsWithUnbound.size());
        assertSame(stringType, bindingsWithUnbound.getBoundType(0));
        assertSame(intType, bindingsWithUnbound.getBoundType(1));
        assertNull(bindingsWithUnbound.findBoundType("foo"));
    }

    @Test
    public void testFindBoundType() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType intType = TypeFactory.defaultInstance().constructType(int.class);

        TypeBindings bindings = TypeBindings.create(List.class, stringType, intType);
        assertSame(stringType, bindings.findBoundType("String"));
        assertSame(intType, bindings.findBoundType("Integer"));
        assertNull(bindings.findBoundType("foo"));
    }

    @Test
    public void testToString() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType intType = TypeFactory.defaultInstance().constructType(int.class);
        JavaType listType = TypeFactory.defaultInstance().constructType(List.class);

        TypeBindings bindings = TypeBindings.create(List.class, stringType, intType);
        assertEquals("<List<String, Integer>>", bindings.toString());
    }

    @Test
    public void testHashCodeAndEquals() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType intType = TypeFactory.defaultInstance().constructType(int.class);

        TypeBindings bindings1 = TypeBindings.create(List.class, stringType, intType);
        TypeBindings bindings2 = TypeBindings.create(List.class, stringType, intType);
        TypeBindings bindings3 = TypeBindings.create(List.class, intType, stringType);

        assertEquals(bindings1, bindings1);
        assertEquals(bindings1, bindings2);
        assertNotEquals(bindings1, bindings3);
    }
}