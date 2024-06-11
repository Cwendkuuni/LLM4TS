import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertNotNull(simpleType);
    }

    @Test
    public void testConstruct() {
        SimpleType simpleType = SimpleType.construct(Integer.class);
        assertNotNull(simpleType);
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(String.class);
        assertThrows(IllegalArgumentException.class, () -> {
            simpleType.withContentType(null);
        });
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        assertThrows(IllegalArgumentException.class, () -> {
            simpleType.withContentTypeHandler(null);
        });
    }

    @Test
    public void testWithContentValueHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        assertThrows(IllegalArgumentException.class, () -> {
            simpleType.withContentValueHandler(null);
        });
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(String.class);
        assertEquals(false, simpleType.withStaticTyping().isDynamic());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(String.class);
        assertNull(simpleType.refine(String.class, TypeBindings.emptyBindings(), null, null));
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = new SimpleType(String.class);
        StringBuilder sb = new StringBuilder();
        assertNotNull(simpleType.getErasedSignature(sb));
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(String.class);
        StringBuilder sb = new StringBuilder();
        assertNotNull(simpleType.getGenericSignature(sb));
    }

    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(String.class);
        assertNotNull(simpleType.toString());
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = new SimpleType(String.class);
        SimpleType simpleType2 = new SimpleType(String.class);
        assertEquals(simpleType1, simpleType2);
    }
}