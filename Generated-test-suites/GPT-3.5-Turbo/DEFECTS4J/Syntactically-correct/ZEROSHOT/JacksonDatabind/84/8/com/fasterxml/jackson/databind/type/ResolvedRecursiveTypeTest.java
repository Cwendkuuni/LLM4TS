import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        assertNull(type.getSelfReferencedType());

        JavaType ref = new JavaType();
        type.setReference(ref);
        assertEquals(ref, type.getSelfReferencedType());

        // Try setting reference again should throw IllegalStateException
        assertThrows(IllegalStateException.class, () -> {
            type.setReference(new JavaType());
        });
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        assertNull(type.getGenericSignature(new StringBuilder()));
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        assertNull(type.getErasedSignature(new StringBuilder()));
    }

    @Test
    public void testWithContentType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        assertEquals(type, type.withContentType(new JavaType()));
    }

    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        assertEquals(type, type.withTypeHandler(new Object()));
    }

    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        assertEquals(type, type.withContentTypeHandler(new Object()));
    }

    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        assertEquals(type, type.withValueHandler(new Object()));
    }

    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        assertEquals(type, type.withContentValueHandler(new Object()));
    }

    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        assertEquals(type, type.withStaticTyping());
    }

    @Test
    public void test_narrow() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        assertEquals(type, type._narrow(Object.class));
    }

    @Test
    public void testRefine() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        assertNull(type.refine(Object.class, null, null, null));
    }

    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        assertFalse(type.isContainerType());
    }

    @Test
    public void testToString() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(Object.class, null);
        assertEquals("[recursive type; UNRESOLVED]", type1.toString());

        ResolvedRecursiveType type2 = new ResolvedRecursiveType(String.class, null);
        assertEquals("[recursive type; java.lang.String]", type2.toString());
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(Object.class, null);
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(Object.class, null);
        assertEquals(type1, type2);

        ResolvedRecursiveType type3 = new ResolvedRecursiveType(String.class, null);
        assertNotEquals(type1, type3);
    }
}