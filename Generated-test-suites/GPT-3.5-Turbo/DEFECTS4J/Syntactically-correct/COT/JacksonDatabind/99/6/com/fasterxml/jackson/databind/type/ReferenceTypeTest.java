import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;

import static org.junit.Assert.*;



public class ReferenceTypeTest {

    @Test
    public void testConstructor() {
        JavaType refType = ReferenceType.construct(String.class, null);
        ReferenceType referenceType = new ReferenceType(String.class, null, null, null,
                refType, null, null, null, false);
        assertNotNull(referenceType);
        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(refType, referenceType.getContentType());
        assertFalse(referenceType.isReferenceType());
    }

    @Test
    public void testWithContentType() {
        JavaType refType = ReferenceType.construct(String.class, null);
        ReferenceType referenceType = new ReferenceType(String.class, null, null, null,
                refType, null, null, null, false);
        assertTrue(referenceType.hasContentType());
        assertEquals(refType, referenceType.getContentType());
        assertEquals(referenceType, referenceType.withContentType(refType));
    }

    @Test
    public void testWithAnchorType() {
        JavaType refType = ReferenceType.construct(String.class, null);
        ReferenceType referenceType = new ReferenceType(String.class, null, null, null,
                refType, null, null, null, false);
        assertNull(referenceType.getAnchorType());
        assertEquals(referenceType, referenceType.withAnchorType(null));
    }

    @Test
    public void testWithStaticTyping() {
        JavaType refType = ReferenceType.construct(String.class, null);
        ReferenceType referenceType = new ReferenceType(String.class, null, null, null,
                refType, null, null, null, false);
        assertFalse(referenceType.isStaticTyping());
        assertEquals(referenceType, referenceType.withStaticTyping());
        assertTrue(referenceType.isStaticTyping());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType refType = ReferenceType.construct(String.class, null);
        ReferenceType referenceType = new ReferenceType(String.class, null, null, null,
                refType, null, null, null, false);
        assertNull(referenceType.getContentType().getTypeHandler());
        assertEquals(referenceType, referenceType.withContentTypeHandler(null));
    }

    @Test
    public void testToString() {
        JavaType refType = ReferenceType.construct(String.class, null);
        ReferenceType referenceType = new ReferenceType(String.class, null, null, null,
                refType, null, null, null, false);
        assertNotNull(referenceType.toString());
    }

    @Test
    public void testEquals() {
        JavaType refType = ReferenceType.construct(String.class, null);
        ReferenceType referenceType1 = new ReferenceType(String.class, null, null, null,
                refType, null, null, null, false);
        ReferenceType referenceType2 = new ReferenceType(String.class, null, null, null,
                refType, null, null, null, false);
        assertEquals(referenceType1, referenceType2);
    }

}

