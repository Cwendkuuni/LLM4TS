import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ReferenceTypeTest {

    private ReferenceType referenceType;

    @Before
    public void setUp() {
        referenceType = ReferenceType.construct(Object.class, JavaType.EMPTY, null, null);
    }

    @Test
    public void testWithContentTypeHandler() {
        Object h = new Object();
        ReferenceType result = referenceType.withContentTypeHandler(h);

        assertNull(result.getTypeHandler());
        assertEquals(h, result.getContentType().getTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Object h = new Object();
        ReferenceType result = referenceType.withValueHandler(h);

        assertNull(result.getValueHandler());
        assertEquals(h, result.getReferencedType().getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Object h = new Object();
        ReferenceType result = referenceType.withContentValueHandler(h);

        assertNull(result.getValueHandler());
        assertEquals(h, result.getContentType().getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        ReferenceType result = referenceType.withStaticTyping();

        assertTrue(result.isStaticTyping());
    }

    @Test
    public void testGetReferencedType() {
        JavaType refType = JavaType.EMPTY;
        ReferenceType result = referenceType.withTypeHandler(refType);

        assertEquals(refType, result.getReferencedType());
    }

    @Test
    public void testIsReferenceType() {
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testContainedTypeCount() {
        assertEquals(1, referenceType.containedTypeCount());
    }

    @Test
    public void testContainedType() {
        assertEquals(JavaType.EMPTY, referenceType.containedType(0));
        assertNull(referenceType.containedType(1));
    }

    @Test
    public void testContainedTypeName() {
        assertEquals("T", referenceType.containedTypeName(0));
        assertNull(referenceType.containedTypeName(1));
    }

    @Test
    public void testGetParameterSource() {
        assertEquals(Object.class, referenceType.getParameterSource());
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder result = referenceType.getErasedSignature(sb);

        assertNotNull(result);
        assertEquals(Object.class.getName(), result.toString());
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder result = referenceType.getGenericSignature(sb);

        assertNotNull(result);
        assertEquals(Object.class.getName() + "<" + JavaType.EMPTY.getGenericSignature(new StringBuilder()) + ";", result.toString());
    }

    @Test
    public void testToString() {
        String expected = "[reference type, class " + Object.class.getName() + "<" + JavaType.EMPTY + ">]";
        String result = referenceType.toString();

        assertEquals(expected, result);
    }

    @Test
    public void testEquals_sameObject() {
        assertTrue(referenceType.equals(referenceType));
    }

    @Test
    public void testEquals_nullObject() {
        assertFalse(referenceType.equals(null));
    }

    @Test
    public void testEquals_differentClass() {
        assertFalse(referenceType.equals(new Object()));
    }

    @Test
    public void testEquals_differentClass_sameReferencedType() {
        assertFalse(referenceType.equals(new SimpleType(Object.class)));
    }

    @Test
    public void testEquals_differentReferencedType() {
        assertFalse(referenceType.equals(ReferenceType.construct(Object.class, JavaType.NOT_EMPTY, null, null)));
    }

    @Test
    public void testEquals_sameReferencedType() {
        assertTrue(referenceType.equals(ReferenceType.construct(Object.class, JavaType.EMPTY, null, null)));
    }
}