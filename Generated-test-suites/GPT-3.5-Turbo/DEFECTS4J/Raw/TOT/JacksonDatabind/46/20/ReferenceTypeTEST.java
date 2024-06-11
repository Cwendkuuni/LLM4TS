
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    ReferenceType referenceType;

    @Before
    public void setUp() {
        referenceType = ReferenceType.construct(String.class, new JavaType(), null, null);
    }

    @Test
    public void testWithNullTypeHandler() {
        Object h = null;
        ReferenceType result = referenceType.withTypeHandler(h);
        assertNotEquals(referenceType, result);
        assertNull(result.getTypeHandler());
    }

    @Test
    public void testWithSameTypeHandler() {
        Object h = new Object();
        ReferenceType result = referenceType.withTypeHandler(h);
        assertEquals(referenceType, result);
        assertEquals(h, result.getTypeHandler());
    }

    @Test
    public void testWithNullContentTypeHandler() {
        Object h = null;
        ReferenceType result = referenceType.withContentTypeHandler(h);
        assertNotEquals(referenceType, result);
        assertNull(result.getContentTypeHandler());
    }

    @Test
    public void testWithSameContentTypeHandler() {
        Object h = new Object();
        ReferenceType result = referenceType.withContentTypeHandler(h);
        assertEquals(referenceType, result);
        assertEquals(h, result.getContentTypeHandler());
    }

    @Test
    public void testWithNullValueHandler() {
        Object h = null;
        ReferenceType result = referenceType.withValueHandler(h);
        assertNotEquals(referenceType, result);
        assertNull(result.getValueHandler());
    }

    @Test
    public void testWithSameValueHandler() {
        Object h = new Object();
        ReferenceType result = referenceType.withValueHandler(h);
        assertEquals(referenceType, result);
        assertEquals(h, result.getValueHandler());
    }

    @Test
    public void testWithNullContentValueHandler() {
        Object h = null;
        ReferenceType result = referenceType.withContentValueHandler(h);
        assertNotEquals(referenceType, result);
        assertNull(result.getContentValueHandler());
    }

    @Test
    public void testWithSameContentValueHandler() {
        Object h = new Object();
        ReferenceType result = referenceType.withContentValueHandler(h);
        assertEquals(referenceType, result);
        assertEquals(h, result.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        ReferenceType result = referenceType.withStaticTyping();
        assertNotEquals(referenceType, result);
        assertTrue(result.isStaticTyping());
    }

    @Test
    public void testWithSameStaticTyping() {
        ReferenceType result = referenceType.withStaticTyping();
        assertEquals(referenceType, result);
        assertEquals(referenceType.isStaticTyping(), result.isStaticTyping());
    }

    @Test
    public void testGetReferencedType() {
        JavaType expectedType = new JavaType();
        referenceType = new ReferenceType(String.class, expectedType, null, null);
        assertEquals(expectedType, referenceType.getReferencedType());
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
    public void testContainedTypeWithIndex0() {
        JavaType expectedType = new JavaType();
        referenceType = new ReferenceType(String.class, expectedType, null, null);
        assertEquals(expectedType, referenceType.containedType(0));
    }

    @Test
    public void testContainedTypeWithindexOutOfBounds() {
        assertNull(referenceType.containedType(1));
    }

    @Test
    public void testContainedTypeNameWithIndex0() {
        String expectedName = "T";
        assertEquals(expectedName, referenceType.containedTypeName(0));
    }

    @Test
    public void testContainedTypeNameWithIndexOutOfBounds() {
        assertNull(referenceType.containedTypeName(1));
    }

    @Test
    public void testGetParameterSource() {
        assertEquals(String.class, referenceType.getParameterSource());
    }

    @Test
    public void testBuildCanonicalName() {
        String expectedName = "com.fasterxml.jackson.databind.type.ReferenceType<T>";
        assertEquals(expectedName, referenceType.buildCanonicalName());
    }

    @Test
    public void testEqualsWithSameObject() {
        assertTrue(referenceType.equals(referenceType));
    }

    @Test
    public void testEqualsWithNull() {
        assertFalse(referenceType.equals(null));
    }

    @Test
    public void testEqualsWithDifferentClass() {
        assertFalse(referenceType.equals(new SimpleType()));
    }

    @Test
    public void testEqualsWithDifferentReferencedType() {
        ReferenceType other = new ReferenceType(String.class, new JavaType(), null, null);
        assertFalse(referenceType.equals(other));
    }

    @Test
    public void testEqualsWithSameReferencedType() {
        ReferenceType other = new ReferenceType(String.class, referenceType.getReferencedType(), null, null);
        assertTrue(referenceType.equals(other));
    }

    @Test
    public void testToString() {
        String expectedString = "[reference type, class com.fasterxml.jackson.databind.type.ReferenceType<java.lang.String>]";
        assertEquals(expectedString, referenceType.toString());
    }

}
