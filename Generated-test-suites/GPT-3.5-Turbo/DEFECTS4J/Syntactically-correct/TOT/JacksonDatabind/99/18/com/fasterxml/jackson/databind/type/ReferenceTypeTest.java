import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    private ReferenceType referenceType;

    @Before
    public void setUp() {
        referenceType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, null, null);
    }

    @Test
    public void testConstructor() {
        assertNotNull(referenceType);
        assertTrue(referenceType.isReferenceType());
        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(String.class.hashCode(), referenceType.hashCode());
        assertNull(referenceType.getSuperClass());
        assertNull(referenceType.getContentValueHandler());
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = SimpleType.constructSimpleType(Integer.class, null, null);
        ReferenceType newType = (ReferenceType) referenceType.withContentType(contentType);
        assertNotNull(newType);
        assertNotSame(referenceType, newType);
        assertEquals(Integer.class, newType.getContentType().getRawClass());
    }

    @Test
    public void testWithTypeHandler() {
        Object typeHandler = new Object();
        ReferenceType newType = referenceType.withTypeHandler(typeHandler);
        assertNotNull(newType);
        assertNotSame(referenceType, newType);
        assertSame(typeHandler, newType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Object contentTypeHandler = new Object();
        ReferenceType newType = referenceType.withContentTypeHandler(contentTypeHandler);
        assertNotNull(newType);
        assertNotSame(referenceType, newType);
        assertSame(contentTypeHandler, newType.getContentTypeHandler());
    }

    @Test
    public void testWithStaticTyping() {
        ReferenceType newType = referenceType.withStaticTyping();
        assertNotNull(newType);
        assertNotSame(referenceType, newType);
        assertTrue(newType.isStaticTyping());
    }

    @Test
    public void testRefine() {
        JavaType superClass = SimpleType.constructSimpleType(Number.class, null, null);
        JavaType[] superInterfaces = new JavaType[]{SimpleType.constructSimpleType(Comparable.class, null, null)};
        ReferenceType newType = (ReferenceType) referenceType.refine(Object.class, null, superClass, superInterfaces);
        assertNotNull(newType);
        assertNotSame(referenceType, newType);
        assertEquals(Object.class, newType.getRawClass());
        assertEquals(superClass, newType.getSuperClass());
        assertArrayEquals(superInterfaces, newType.getInterfaces());
    }

    @Test
    public void testGetContentType() {
        JavaType contentType = referenceType.getContentType();
        assertNotNull(contentType);
        assertSame(contentType, referenceType.getReferencedType());
    }

    @Test
    public void testGetAnchorType() {
        JavaType anchorType = referenceType.getAnchorType();
        assertNotNull(anchorType);
        assertSame(referenceType, anchorType);
        assertTrue(referenceType.isAnchorType());
    }

    @Test
    public void testToString() {
        String expected = "[reference type, class java.lang.String]";
        assertEquals(expected, referenceType.toString());
    }

    @Test
    public void testEquals() {
        ReferenceType otherType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, null, null);
        assertTrue(referenceType.equals(otherType));
    }

}