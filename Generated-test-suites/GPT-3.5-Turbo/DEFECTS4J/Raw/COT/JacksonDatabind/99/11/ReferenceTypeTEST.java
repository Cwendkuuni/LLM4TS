import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    @Test
    public void testWithContentType() {
        // Test with empty content type
        ReferenceType type = ReferenceType.construct(Object.class, TypeBindings.emptyBindings(), null, null, null);

        // Set content type
        JavaType contentType = SimpleType.construct(Object.class);
        ReferenceType result = type.withContentType(contentType);

        assertSame(contentType, result.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        // Test with empty type handler
        ReferenceType type = ReferenceType.construct(Object.class, TypeBindings.emptyBindings(), null, null, null);

        // Set type handler
        Object typeHandler = new Object();
        ReferenceType result = type.withTypeHandler(typeHandler);

        assertSame(typeHandler, result.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        // Test with empty content type handler
        ReferenceType type = ReferenceType.construct(Object.class, TypeBindings.emptyBindings(), null, null, null);

        // Set content type handler
        Object contentTypeHandler = new Object();
        ReferenceType result = type.withContentTypeHandler(contentTypeHandler);

        assertSame(contentTypeHandler, result.getContentTypeHandler());
    }

    @Test
    public void testWithStaticTyping() {
        // Test with non-static typing
        ReferenceType type = ReferenceType.construct(Object.class, TypeBindings.emptyBindings(), null, null, null);

        // Set static typing
        ReferenceType result = type.withStaticTyping();

        assertTrue(result.isStaticTyping());
    }

    @Test
    public void testGetContentType() {
        // Test with empty content type
        ReferenceType type = ReferenceType.construct(Object.class, TypeBindings.emptyBindings(), null, null, null);

        assertNull(type.getContentType());
    }

    @Test
    public void testGetReferencedType() {
        // Test with empty referenced type
        ReferenceType type = ReferenceType.construct(Object.class, TypeBindings.emptyBindings(), null, null, null);

        assertNull(type.getReferencedType());
    }

    @Test
    public void testHasContentType() {
        // Test with empty content type
        ReferenceType type = ReferenceType.construct(Object.class, TypeBindings.emptyBindings(), null, null, null);

        assertFalse(type.hasContentType());
    }

    @Test
    public void testIsReferenceType() {
        // Test with empty referenced type
        ReferenceType type = ReferenceType.construct(Object.class, TypeBindings.emptyBindings(), null, null, null);

        assertTrue(type.isReferenceType());
    }

    @Test
    public void testBuildCanonicalName() {
        // Test with empty class
        ReferenceType type = ReferenceType.construct(null, TypeBindings.emptyBindings(), null, null, null);

        assertEquals("null<null>", type.buildCanonicalName());
    }

    @Test
    public void testRefine() {
        // Test with null raw type
        ReferenceType type = ReferenceType.construct(null, TypeBindings.emptyBindings(), null, null, null);

        // Refine type
        JavaType refinedType = type.refine(String.class, TypeBindings.emptyBindings(), null, null);

        assertEquals(String.class, refinedType.getRawClass());
    }

    @Test
    public void testGetAnchorType() {
        // Test with empty anchor type
        ReferenceType type = ReferenceType.construct(Object.class, TypeBindings.emptyBindings(), null, null, null);

        // Set anchor type
        JavaType anchorType = SimpleType.construct(Object.class);
        ReferenceType result = type.withAnchorType(anchorType);

        assertSame(anchorType, result.getAnchorType());
    }

    @Test
    public void testIsAnchorType() {
        // Test with empty anchor type
        ReferenceType type = ReferenceType.construct(Object.class, TypeBindings.emptyBindings(), null, null, null);

        assertFalse(type.isAnchorType());
    }

    @Test
    public void testToString() {
        // Test with empty class and referenced type
        ReferenceType type = ReferenceType.construct(null, TypeBindings.emptyBindings(), null, null, null);

        assertEquals("[reference type, class null<null>]", type.toString());
    }

    @Test
    public void testEquals() {
        // Test with empty class and referenced type
        ReferenceType type1 = ReferenceType.construct(null, TypeBindings.emptyBindings(), null, null, null);

        // Create another reference type with the same class and referenced type
        ReferenceType type2 = ReferenceType.construct(null, TypeBindings.emptyBindings(), null, null, null);

        // Test equality
        assertTrue(type1.equals(type2));
    }
}