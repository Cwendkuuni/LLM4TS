import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    private ReferenceType referenceType;

    @Before
    public void setUp() {
        referenceType = ReferenceType.construct(String.class, JavaType.simpleType(Integer.class), null, null);
    }

    @Test
    public void testWithReferencedType() {
        JavaType referencedType = referenceType.getReferencedType();
        assertNotNull(referencedType);
    }

    @Test
    public void testIsReferenceType() {
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testContainedTypeCount() {
        int count = referenceType.containedTypeCount();
        assertEquals(1, count);
    }

    @Test
    public void testContainedType() {
        JavaType type = referenceType.containedType(0);
        assertNotNull(type);
        assertEquals(JavaType.simpleType(Integer.class), type);
    }

    @Test
    public void testContainedTypeName() {
        String typeName = referenceType.containedTypeName(0);
        assertEquals("T", typeName);
    }

    @Test
    public void testGetParameterSource() {
        Class<?> parameterSource = referenceType.getParameterSource();
        assertEquals(String.class, parameterSource);
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder signature = referenceType.getErasedSignature(sb);
        assertNotNull(signature);
        assertEquals("Lcom/fasterxml/jackson/databind/type/ReferenceType;", signature.toString());
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder signature = referenceType.getGenericSignature(sb);
        assertNotNull(signature);
        assertEquals("Lcom/fasterxml/jackson/databind/type/ReferenceType<Ljava/lang/Integer;>;", signature.toString());
    }

    @Test
    public void testToString() {
        String string = referenceType.toString();
        assertNotNull(string);
        assertTrue(string.startsWith("[reference type, class com.fasterxml.jackson.databind.type.ReferenceType<"));
        assertTrue(string.endsWith(">]"));
    }

    @Test
    public void testEquals() {
        ReferenceType other = ReferenceType.construct(String.class, JavaType.simpleType(Integer.class), null, null);
        assertEquals(referenceType, other);
    }
}