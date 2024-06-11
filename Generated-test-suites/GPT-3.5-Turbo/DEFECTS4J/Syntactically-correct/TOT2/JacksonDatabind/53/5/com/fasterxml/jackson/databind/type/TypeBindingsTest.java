import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeBindingsTest {

    private TypeBindings typeBindings;

    @Before
    public void setUp() {
        typeBindings = TypeBindings.emptyBindings();
    }

    @Test
    public void testCreateWithNoTypes() {
        TypeBindings result = TypeBindings.create(Object.class, null);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testCreateWithSingleType() {
        TypeBindings result = TypeBindings.create(Object.class, JavaType.construct(Object.class));
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Object", result.getBoundName(0));
    }

    @Test
    public void testCreateWithMultipleTypes() {
        JavaType type1 = JavaType.construct(Object.class);
        JavaType type2 = JavaType.construct(String.class);
        TypeBindings result = TypeBindings.create(Object.class, type1, type2);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Object", result.getBoundName(0));
        assertEquals("String", result.getBoundName(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithIncorrectNumberOfTypes() {
        TypeBindings.create(Object.class, JavaType.construct(Object.class), JavaType.construct(String.class), JavaType.construct(Integer.class));
    }

    @Test
    public void testCreateIfNeededWithItemNotRequiringTypeParameters() {
        TypeBindings result = TypeBindings.createIfNeeded(Object.class, JavaType.construct(Object.class));
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testCreateIfNeededWithItemRequiringTypeParameters() {
        TypeBindings result = TypeBindings.createIfNeeded(List.class, JavaType.construct(String.class));
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("E", result.getBoundName(0));
    }

    @Test
    public void testWithUnboundVariable() {
        TypeBindings result = typeBindings.withUnboundVariable("Test");
        assertNotNull(result);
        assertTrue(result.hasUnbound("Test"));
    }

    @Test
    public void testFindBoundType() {
        JavaType type1 = JavaType.construct(Object.class);
        JavaType type2 = JavaType.construct(String.class);
        TypeBindings bindings = TypeBindings.create(Object.class, type1, type2);

        JavaType result = bindings.findBoundType("Object");
        assertNotNull(result);
        assertEquals(type1, result);

        result = bindings.findBoundType("String");
        assertNotNull(result);
        assertEquals(type2, result);

        result = bindings.findBoundType("Integer");
        assertNull(result);
    }
}