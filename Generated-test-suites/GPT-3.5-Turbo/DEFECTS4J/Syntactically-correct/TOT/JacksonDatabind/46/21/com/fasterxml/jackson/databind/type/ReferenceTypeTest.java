import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        // Test typical case
        ReferenceType refType = ReferenceType.construct(String.class, JavaType.STRING, null, null);
        assertNotNull(refType);
        assertEquals(String.class, refType.getRawClass());
        assertEquals(JavaType.STRING, refType.getReferencedType());

        // Test with null valueHandler and typeHandler
        ReferenceType refType2 = ReferenceType.construct(Integer.class, JavaType.INT, null, null);
        assertNotNull(refType2);
        assertEquals(Integer.class, refType2.getRawClass());
        assertEquals(JavaType.INT, refType2.getReferencedType());
    }

    @Test
    public void testWithTypeHandler() {
        // Test with the same type handler
        ReferenceType refType = ReferenceType.construct(String.class, JavaType.STRING, null, null);
        ReferenceType refTypeWithSameTypeHandler = refType.withTypeHandler(null);
        assertSame(refType, refTypeWithSameTypeHandler);

        // Test with different type handler
        Object typeHandler = new Object();
        ReferenceType refTypeWithDifferentTypeHandler = refType.withTypeHandler(typeHandler);
        assertNotNull(refTypeWithDifferentTypeHandler);
        assertEquals(typeHandler, refTypeWithDifferentTypeHandler.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        // Test with the same content type handler
        ReferenceType refType = ReferenceType.construct(String.class, JavaType.STRING, null, null);
        ReferenceType refTypeWithSameContentTypeHandler = refType.withContentTypeHandler(null);
        assertSame(refType, refTypeWithSameContentTypeHandler);

        // Test with different content type handler
        Object contentTypeHandler = new Object();
        ReferenceType refTypeWithDifferentContentTypeHandler = refType.withContentTypeHandler(contentTypeHandler);
        assertNotNull(refTypeWithDifferentContentTypeHandler);
        assertEquals(contentTypeHandler, refTypeWithDifferentContentTypeHandler.getContentType().getTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        // Test with the same value handler
        ReferenceType refType = ReferenceType.construct(String.class, JavaType.STRING, null, null);
        ReferenceType refTypeWithSameValueHandler = refType.withValueHandler(null);
        assertSame(refType, refTypeWithSameValueHandler);

        // Test with different value handler
        Object valueHandler = new Object();
        ReferenceType refTypeWithDifferentValueHandler = refType.withValueHandler(valueHandler);
        assertNotNull(refTypeWithDifferentValueHandler);
        assertEquals(valueHandler, refTypeWithDifferentValueHandler.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        // Test with the same content value handler
        ReferenceType refType = ReferenceType.construct(String.class, JavaType.STRING, null, null);
        ReferenceType refTypeWithSameContentValueHandler = refType.withContentValueHandler(null);
        assertSame(refType, refTypeWithSameContentValueHandler);

        // Test with different content value handler
        Object contentValueHandler = new Object();
        ReferenceType refTypeWithDifferentContentValueHandler = refType.withContentValueHandler(contentValueHandler);
        assertNotNull(refTypeWithDifferentContentValueHandler);
        assertEquals(contentValueHandler, refTypeWithDifferentContentValueHandler.getContentType().getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        // Test with static typing already enabled
        ReferenceType refType = ReferenceType.construct(String.class, JavaType.STRING, null, null);
        ReferenceType refTypeWithStaticTyping = refType.withStaticTyping();
        assertSame(refType, refTypeWithStaticTyping);

        // Test with static typing not enabled
        ReferenceType refType2 = ReferenceType.construct(Integer.class, JavaType.INT, null, null);
        ReferenceType refTypeWithStaticTyping2 = refType2.withStaticTyping();
        assertNotNull(refTypeWithStaticTyping2);
        assertTrue(refTypeWithStaticTyping2.isStaticTyping());
    }

    @Test
    public void testBuildCanonicalName() {
        // Test typical case
        ReferenceType refType = ReferenceType.construct(String.class, JavaType.STRING, null, null);
        assertEquals("java.lang.String<java.lang.String>",
                refType.buildCanonicalName());
    }

    @Test
    public void testNarrow() {
        // Test narrowing to subclass
        ReferenceType refType = ReferenceType.construct(String.class, JavaType.STRING, null, null);
        ReferenceType narrowedType = (ReferenceType) refType.narrow(Object.class);
        assertNotNull(narrowedType);
        assertEquals(Object.class, narrowedType.getRawClass());
        assertEquals(JavaType.STRING, narrowedType.getReferencedType());
    }

    @Test
    public void testGetReferencedType() {
        ReferenceType refType = ReferenceType.construct(Integer.class, JavaType.INT, null, null);
        assertEquals(JavaType.INT, refType.getReferencedType());
    }

    @Test
    public void testIsReferenceType() {
        ReferenceType refType = ReferenceType.construct(Integer.class, JavaType.INT, null, null);
        assertTrue(refType.isReferenceType());
    }

    @Test
    public void testContainedTypeCount() {
        ReferenceType refType = ReferenceType.construct(Integer.class, JavaType.INT, null, null);
        assertEquals(1, refType.containedTypeCount());
    }

    @Test
    public void testContainedType() {
        ReferenceType refType = ReferenceType.construct(Integer.class, JavaType.INT, null, null);
        assertEquals(JavaType.INT, refType.containedType(0));
        assertNull(refType.containedType(1));
    }

    @Test
    public void testContainedTypeName() {
        ReferenceType refType = ReferenceType.construct(Integer.class, JavaType.INT, null, null);
        assertEquals("T", refType.containedTypeName(0));
        assertNull(refType.containedTypeName(1));
    }

    @Test
    public void testGetParameterSource() {
        ReferenceType refType = ReferenceType.construct(Integer.class, JavaType.INT, null, null);
        assertEquals(Integer.class, refType.getParameterSource());
    }

    @Test
    public void testGetErasedSignature() {
        // Test with canonical name
        ReferenceType refType = ReferenceType.construct(String.class, JavaType.STRING, null, null);
        StringBuilder sb = new StringBuilder();
        refType.getErasedSignature(sb);
        assertEquals("java.lang.String", sb.toString());

        // Test without canonical name
        ReferenceType refType2 = ReferenceType.construct(Integer.class, JavaType.INT, null, null);
        StringBuilder sb2 = new StringBuilder();
        refType2.getErasedSignature(sb2);
        assertEquals("Integer", sb2.toString());
    }

    @Test
    public void testGetGenericSignature() {
        // Test with canonical name
        ReferenceType refType = ReferenceType.construct(String.class, JavaType.STRING, null, null);
        StringBuilder sb = new StringBuilder();
        refType.getGenericSignature(sb);
        assertEquals("java.lang.String<java.lang.String;>", sb.toString());

        // Test without canonical name
        ReferenceType refType2 = ReferenceType.construct(Integer.class, JavaType.INT, null, null);
        StringBuilder sb2 = new StringBuilder();
        refType2.getGenericSignature(sb2);
        assertEquals("Integer<;", sb2.toString());
    }

    @Test
    public void testToString() {
        ReferenceType refType = ReferenceType.construct(String.class, JavaType.STRING, null, null);
        assertEquals("[reference type, class java.lang.String<java.lang.String>]", refType.toString());
    }

    @Test
    public void testEquals() {
        ReferenceType refType = ReferenceType.construct(String.class, JavaType.STRING, null, null);

        // Test with the same ReferenceType
        assertTrue(refType.equals(refType));

        // Test with null
        assertFalse(refType.equals(null));

        // Test with different class
        assertFalse(refType.equals(JavaType.STRING));

        // Test with different referencedType
        ReferenceType refType2 = ReferenceType.construct(String.class, JavaType.OBJECT, null, null);
        assertFalse(refType.equals(refType2));

        // Test with equal referencedType and class
        ReferenceType refType3 = ReferenceType.construct(String.class, JavaType.STRING, null, null);
        assertTrue(refType.equals(refType3));
    }
}