import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMap() {
        SimpleType.construct(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollection() {
        SimpleType.construct(Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArray() {
        SimpleType.construct(Array.class);
    }

    @Test
    public void testNarrow() {
        SimpleType simpleType = SimpleType.constructUnsafe(Object.class);
        JavaType narrowedType = simpleType._narrow(String.class);
        assertNotNull(narrowedType);
        assertEquals(String.class, narrowedType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentType() {
        SimpleType simpleType = SimpleType.constructUnsafe(Object.class);
        simpleType.withContentType(String.class);
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType = SimpleType.constructUnsafe(Object.class);
        Object typeHandler = new Object();
        SimpleType newSimpleType = simpleType.withTypeHandler(typeHandler);
        assertNotNull(newSimpleType);
        assertEquals(typeHandler, newSimpleType.getTypeHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeHandler() {
        SimpleType simpleType = SimpleType.constructUnsafe(Object.class);
        simpleType.withContentTypeHandler(new Object());
    }

    @Test
    public void testWithValueHandler() {
        SimpleType simpleType = SimpleType.constructUnsafe(Object.class);
        Object valueHandler = new Object();
        SimpleType newSimpleType = simpleType.withValueHandler(valueHandler);
        assertNotNull(newSimpleType);
        assertEquals(valueHandler, newSimpleType.getValueHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentValueHandler() {
        SimpleType simpleType = SimpleType.constructUnsafe(Object.class);
        simpleType.withContentValueHandler(new Object());
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        SimpleType staticType = simpleType.withStaticTyping();
        assertNotNull(staticType);
        assertTrue(staticType.isStaticTyping());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = SimpleType.constructUnsafe(Object.class);
        JavaType refinedType = simpleType.refine(String.class, TypeBindings.emptyBindings(), null, null);
        assertNull(refinedType);
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = SimpleType.constructUnsafe(Object.class);
        assertFalse(simpleType.isContainerType());
    }

}