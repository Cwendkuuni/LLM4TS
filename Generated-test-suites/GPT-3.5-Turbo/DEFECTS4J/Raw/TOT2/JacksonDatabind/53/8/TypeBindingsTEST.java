import static org.junit.Assert.*;
import org.junit.Test;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings empty = TypeBindings.emptyBindings();
        assertTrue(empty.isEmpty());
    }

    @Test
    public void testCreateWithNoTypeParameters() {
        TypeBindings bindings = TypeBindings.create(ArrayList.class, null);
        assertEquals(0, bindings.size());
    }

    @Test
    public void testCreateWithOneTypeParameter() {
        JavaType type = new JavaType();
        TypeBindings bindings = TypeBindings.create(ArrayList.class, type);
        assertEquals(1, bindings.size());
        assertEquals(type, bindings.getBoundType(0));
    }

    @Test
    public void testCreateWithTwoTypeParameters() {
        JavaType type1 = new JavaType();
        JavaType type2 = new JavaType();
        TypeBindings bindings = TypeBindings.create(ArrayList.class, type1, type2);
        assertEquals(2, bindings.size());
        assertEquals(type1, bindings.getBoundType(0));
        assertEquals(type2, bindings.getBoundType(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithMismatchingTypes() {
        JavaType type = new JavaType();
        TypeBindings.create(ArrayList.class, type);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithMismatchingTypeParameters() {
        JavaType type1 = new JavaType();
        JavaType type2 = new JavaType();
        TypeBindings.create(ArrayList.class, type1, type2);
    }

    @Test
    public void testCreateIfNeededWithNoTypeParameters() {
        TypeBindings bindings = TypeBindings.createIfNeeded(ArrayList.class, null);
        assertTrue(bindings.isEmpty());
    }

    @Test
    public void testCreateIfNeededWithOneTypeParameter() {
        JavaType type = new JavaType();
        TypeBindings bindings = TypeBindings.createIfNeeded(ArrayList.class, type);
        assertEquals(1, bindings.size());
        assertEquals(type, bindings.getBoundType(0));
    }

    @Test
    public void testWithUnboundVariable() {
        TypeBindings bindings = TypeBindings.create(ArrayList.class, null);
        TypeBindings updatedBindings = bindings.withUnboundVariable("T");
        assertTrue(updatedBindings.hasUnbound("T"));
    }

    @Test
    public void testFindBoundType() {
        JavaType type = new JavaType();
        TypeBindings bindings = TypeBindings.create(ArrayList.class, type);
        assertEquals(type, bindings.findBoundType("E"));
    }

    @Test
    public void testToString() {
        JavaType type1 = new JavaType();
        JavaType type2 = new JavaType();
        TypeBindings bindings = TypeBindings.create(ArrayList.class, type1, type2);
        String expected = "<" + type1.getGenericSignature() + "," + type2.getGenericSignature() + ">";
        assertEquals(expected, bindings.toString());
    }

}