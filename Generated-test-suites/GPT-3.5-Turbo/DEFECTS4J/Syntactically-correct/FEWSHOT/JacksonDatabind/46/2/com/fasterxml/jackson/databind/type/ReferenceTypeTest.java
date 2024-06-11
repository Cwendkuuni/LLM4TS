import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ReferenceTypeTest {
    @Test
    public void testConstruct() {
        ReferenceType referenceType = ReferenceType.construct(String.class, null, null, null);
        assertEquals("java.lang.String", referenceType.getReferencedType().toString());
        assertEquals(1, referenceType.containedTypeCount());
        assertNull(referenceType.containedType(1));
        assertNull(referenceType.containedTypeName(1));
        assertEquals(String.class, referenceType.getParameterSource());
    }

    @Test
    public void testWithContentTypeHandler() {
        ReferenceType referenceType = ReferenceType.construct(String.class, null, null, null);
        Object contentHandler = new Object();
        ReferenceType withContentHandler = referenceType.withContentTypeHandler(contentHandler);
        assertEquals(contentHandler, withContentHandler.getReferencedType().getTypeHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        ReferenceType referenceType = ReferenceType.construct(String.class, null, null, null);
        Object contentValueHandler = new Object();
        ReferenceType withContentValueHandler = referenceType.withContentValueHandler(contentValueHandler);
        assertEquals(contentValueHandler, withContentValueHandler.getReferencedType().getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        ReferenceType referenceType = ReferenceType.construct(String.class, null, null, null);
        ReferenceType withStaticTyping = referenceType.withStaticTyping();
        assertEquals(true, withStaticTyping.isStaticTyping());
    }

    @Test
    public void testContainedTypeName() {
        ReferenceType referenceType = ReferenceType.construct(String.class, null, null, null);
        assertEquals("T", referenceType.containedTypeName(0));
        assertNull(referenceType.containedTypeName(1));
    }

    @Test
    public void testBuildCanonicalName() {
        ReferenceType referenceType = ReferenceType.construct(String.class, null, null, null);
        assertEquals("java.lang.String", referenceType.buildCanonicalName());
    }

    @Test
    public void testToString() {
        ReferenceType referenceType = ReferenceType.construct(String.class, null, null, null);
        assertEquals("[reference type, class java.lang.String]", referenceType.toString());
    }

    @Test
    public void testEquals() {
        ReferenceType referenceType1 = ReferenceType.construct(String.class, null, null, null);
        ReferenceType referenceType2 = ReferenceType.construct(Integer.class, null, null, null);
        assertEquals(true, referenceType1.equals(referenceType1));
        assertEquals(false, referenceType1.equals(null));
        assertEquals(false, referenceType1.equals(new Object()));
        assertEquals(false, referenceType1.equals(referenceType2));
    }
}