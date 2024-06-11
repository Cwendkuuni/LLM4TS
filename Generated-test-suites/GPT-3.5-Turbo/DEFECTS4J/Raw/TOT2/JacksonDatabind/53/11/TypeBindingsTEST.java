import static org.junit.Assert.*;
import org.junit.Test;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings empty = TypeBindings.emptyBindings();
        assertTrue(empty.isEmpty());
    }

    @Test
    public void testCreateWithNoTypes() {
        TypeBindings bindings = TypeBindings.create(TypeBindings.class, null);
        assertEquals(0, bindings.size());
    }

    @Test
    public void testCreateWithSingleType() {
        JavaType type = new JavaType();
        TypeBindings bindings = TypeBindings.create(TypeBindings.class, type);
        assertEquals(1, bindings.size());
        assertEquals(type, bindings.getBoundType(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithMismatchTypeLength() {
        JavaType type1 = new JavaType();
        JavaType type2 = new JavaType();
        TypeBindings.create(TypeBindings.class, type1, type2);
    }

    @Test
    public void testWithTypeParamStash() {
        TypeBindings.TypeParamStash stash = new TypeBindings.TypeParamStash();
        assertEquals(1, stash.paramsFor1(List.class).length);
        assertEquals(2, stash.paramsFor2(Map.class).length);
    }

    @Test
    public void testWithUnboundVariable() {
        TypeBindings bindings = TypeBindings.create(TypeBindings.class, new JavaType());
        TypeBindings newBindings = bindings.withUnboundVariable("T");
        assertTrue(newBindings.hasUnbound("T"));
    }

    @Test
    public void testFindBoundType() {
        JavaType type1 = new JavaType();
        JavaType type2 = new JavaType();
        TypeBindings bindings = TypeBindings.create(TypeBindings.class, type1, type2);

        assertEquals(type1, bindings.findBoundType("_names[0]"));
    }

    @Test
    public void testToString() {
        JavaType type1 = new JavaType();
        JavaType type2 = new JavaType();
        TypeBindings bindings = TypeBindings.create(TypeBindings.class, type1, type2);

        assertEquals("<,>", bindings.toString());
    }

    @Test
    public void testEquals() {
        TypeBindings bindings1 = TypeBindings.create(TypeBindings.class, new JavaType());
        TypeBindings bindings2 = TypeBindings.create(TypeBindings.class, new JavaType());

        assertEquals(bindings1, bindings2);
    }

}