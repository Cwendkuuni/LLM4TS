import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
    }

    @Test
    public void testConstruct() {
        SimpleType simpleType = SimpleType.construct(Integer.class);
        assertNotNull(simpleType);
        assertEquals(Integer.class, simpleType.getRawClass());
    }

    @Test
    public void testNarrow() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType narrowedType = (SimpleType) simpleType._narrow(Object.class);
        assertNotNull(narrowedType);
        assertEquals(Object.class, narrowedType.getRawClass());
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(String.class);
        Throwable exception = assertThrows(Exception.class, () -> {
            simpleType.withContentType(String.class);
        });
        assertEquals("Simple types have no content types; can not call withContentType()", exception.getMessage());
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        Throwable exception = assertThrows(Exception.class, () -> {
            simpleType.withContentTypeHandler("handler");
        });
        assertEquals("Simple types have no content types; can not call withContenTypeHandler()", exception.getMessage());
    }

    @Test
    public void testWithContentValueHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        Throwable exception = assertThrows(Exception.class, () -> {
            simpleType.withContentValueHandler("handler");
        });
        assertEquals("Simple types have no content types; can not call withContenValueHandler()", exception.getMessage());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(String.class);
        JavaType refinedType = simpleType.refine(Number.class, new TypeBindings(), null, null);
        assertNull(refinedType);
    }
}