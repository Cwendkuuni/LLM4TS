import org.junit.Test;
import static org.junit.Assert.*;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings empty = TypeBindings.emptyBindings();

        assertTrue(empty.isEmpty());
        assertEquals(0, empty.size());
    }

    @Test
    public void testCreateWithNoTypes() {
        TypeBindings typeBindings = TypeBindings.create(ArrayList.class, null);

        assertTrue(typeBindings.isEmpty());
        assertEquals(0, typeBindings.size());
    }

    @Test
    public void testCreateWithSingleType() {
        JavaType singleType = JavaType.construct(Object.class);
        TypeBindings typeBindings = TypeBindings.create(ArrayList.class, singleType);

        assertFalse(typeBindings.isEmpty());
        assertEquals(1, typeBindings.size());
        assertSame(singleType, typeBindings.getBoundType(0));
    }

    @Test
    public void testFindBoundType() {
        JavaType type1 = JavaType.construct(String.class);
        JavaType type2 = JavaType.construct(Integer.class);
        TypeBindings typeBindings = TypeBindings.create(Map.class, type1, type2);

        JavaType foundType1 = typeBindings.findBoundType("K");
        JavaType foundType2 = typeBindings.findBoundType("V");

        assertSame(type1, foundType1);
        assertSame(type2, foundType2);
    }

    @Test
    public void testWithUnboundVariable() {
        JavaType type = JavaType.construct(Long.class);
        TypeBindings originalBindings = TypeBindings.createIfNeeded(LinkedHashMap.class, type);

        TypeBindings updatedBindings = originalBindings.withUnboundVariable("K");

        assertTrue(updatedBindings.hasUnbound("K"));
    }

    @Test
    public void testHashCodeAndEquals() {
        JavaType type = JavaType.construct(Float.class);
        TypeBindings bindings1 = TypeBindings.createIfNeeded(HashMap.class, type);
        TypeBindings bindings2 = TypeBindings.createIfNeeded(HashMap.class, type);

        assertEquals(bindings1.hashCode(), bindings2.hashCode());
        assertTrue(bindings1.equals(bindings2));
    }

    @Test
    public void testGetBoundNameAndType() {
        JavaType type = JavaType.construct(Double.class);
        TypeBindings typeBindings = TypeBindings.createIfNeeded(List.class, type);

        assertEquals("E", typeBindings.getBoundName(0));
        assertSame(type, typeBindings.getBoundType(0));
    }

    @Test
    public void testGetTypeParameters() {
        JavaType type1 = JavaType.construct(Boolean.class);
        JavaType type2 = JavaType.construct(Character.class);
        TypeBindings typeBindings = TypeBindings.createIfNeeded(ArrayList.class, new JavaType[] { type1, type2 });

        assertEquals(2, typeBindings.getTypeParameters().size());
        assertSame(type1, typeBindings.getTypeParameters().get(0));
        assertSame(type2, typeBindings.getTypeParameters().get(1));
    }

}