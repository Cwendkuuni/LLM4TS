import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ReferenceTypeTest {

    private ReferenceType referenceType;

    @Before
    public void setUp() {
        referenceType = new ReferenceType(String.class, null, null, null, null, null, null, null, false);
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = new SimpleType();
        ReferenceType newReferenceType = referenceType.withContentType(contentType);
        assertEquals(contentType, newReferenceType.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        Object typeHandler = new Object();
        ReferenceType newReferenceType = referenceType.withTypeHandler(typeHandler);
        assertEquals(typeHandler, newReferenceType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Object contentTypeHandler = new Object();
        ReferenceType newReferenceType = referenceType.withContentTypeHandler(contentTypeHandler);
        assertEquals(contentTypeHandler, newReferenceType.getContentTypeHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Object contentValueHandler = new Object();
        ReferenceType newReferenceType = referenceType.withContentValueHandler(contentValueHandler);
        assertEquals(contentValueHandler, newReferenceType.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        ReferenceType newReferenceType = referenceType.withStaticTyping();
        assertTrue(newReferenceType.isStaticTyping());
    }

    @Test
    public void testRefine() {
        Class<?> rawType = Object.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        ReferenceType newReferenceType = referenceType.refine(rawType, bindings, superClass, superInterfaces);
        assertEquals(rawType, newReferenceType.getRawType());
    }

    @Test
    public void testHasContentType() {
        assertTrue(referenceType.hasContentType());
    }

    @Test
    public void testIsReferenceType() {
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testGetAnchorType() {
        JavaType anchorType = referenceType.getAnchorType();
        assertEquals(referenceType, anchorType);
    }

    @Test
    public void testIsAnchorType() {
        assertTrue(referenceType.isAnchorType());
    }

    @Test
    public void testToString() {
        String toString = referenceType.toString();
        assertNotNull(toString);
    }

    @Test
    public void testEquals() {
        ReferenceType other = new ReferenceType(String.class, null, null, null, null, null, null, null, false);
        assertTrue(referenceType.equals(other));
    }
}