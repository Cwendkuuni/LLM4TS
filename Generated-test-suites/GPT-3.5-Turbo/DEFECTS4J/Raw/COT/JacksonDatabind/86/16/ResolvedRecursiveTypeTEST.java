
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;
import com.fasterxml.jackson.databind.JavaType;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

    private ResolvedRecursiveType recursiveType;

    @Before
    public void setUp() {
        recursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
    }

    @Test
    public void testSetReference() {
        JavaType ref = new JavaType(Object.class);
        recursiveType.setReference(ref);
        assertEquals(ref, recursiveType.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceMultipleTimes() {
        JavaType ref1 = new JavaType(Object.class);
        JavaType ref2 = new JavaType(String.class);
        recursiveType.setReference(ref1);
        recursiveType.setReference(ref2);
    }

    @Test
    public void testGetGenericSignature() {
        JavaType ref = new JavaType(Object.class);
        recursiveType.setReference(ref);
        StringBuilder sb = new StringBuilder();
        recursiveType.getGenericSignature(sb);
        assertEquals(ref.getGenericSignature(sb), recursiveType.getGenericSignature(sb));
    }

    @Test
    public void testGetErasedSignature() {
        JavaType ref = new JavaType(Object.class);
        recursiveType.setReference(ref);
        StringBuilder sb = new StringBuilder();
        recursiveType.getErasedSignature(sb);
        assertEquals(ref.getErasedSignature(sb), recursiveType.getErasedSignature(sb));
    }

    @Test
    public void testWithContentType() {
        JavaType ref = new JavaType(Object.class);
        recursiveType.setReference(ref);
        assertSame(recursiveType, recursiveType.withContentType(ref));
    }

    @Test
    public void testWithTypeHandler() {
        JavaType ref = new JavaType(Object.class);
        recursiveType.setReference(ref);
        assertSame(recursiveType, recursiveType.withTypeHandler(null));
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType ref = new JavaType(Object.class);
        recursiveType.setReference(ref);
        assertSame(recursiveType, recursiveType.withContentTypeHandler(null));
    }

    @Test
    public void testWithValueHandler() {
        JavaType ref = new JavaType(Object.class);
        recursiveType.setReference(ref);
        assertSame(recursiveType, recursiveType.withValueHandler(null));
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType ref = new JavaType(Object.class);
        recursiveType.setReference(ref);
        assertSame(recursiveType, recursiveType.withContentValueHandler(null));
    }

    @Test
    public void testWithStaticTyping() {
        JavaType ref = new JavaType(Object.class);
        recursiveType.setReference(ref);
        assertSame(recursiveType, recursiveType.withStaticTyping());
    }

    @Test
    public void testNarrow() {
        JavaType ref = new JavaType(Object.class);
        recursiveType.setReference(ref);
        assertSame(recursiveType, recursiveType._narrow(String.class));
    }

    @Test
    public void testRefine() {
        assertNull(recursiveType.refine(Object.class, new TypeBindings(), null, null));
    }

    @Test
    public void testIsContainerType() {
        assertFalse(recursiveType.isContainerType());
    }

    @Test
    public void testToString() {
        assertEquals("[recursive type; UNRESOLVED]", recursiveType.toString());
        JavaType ref = new JavaType(Object.class);
        recursiveType.setReference(ref);
        assertEquals("[recursive type; java.lang.Object]", recursiveType.toString());
    }

    @Test
    public void testEquals() {
        assertEquals(recursiveType, recursiveType);
        assertFalse(recursiveType.equals(null));
        assertFalse(recursiveType.equals(new Object()));
        assertFalse(recursiveType.equals(new ResolvedRecursiveType(Object.class, new TypeBindings())));
        JavaType ref1 = new JavaType(Object.class);
        JavaType ref2 = new JavaType(Object.class);
        recursiveType.setReference(ref1);
        ResolvedRecursiveType otherType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        otherType.setReference(ref2);
        assertFalse(recursiveType.equals(otherType));
        JavaType ref3 = new JavaType(String.class);
        recursiveType.setReference(ref3);
        otherType.setReference(ref3);
        assertTrue(recursiveType.equals(otherType));
    }
}

