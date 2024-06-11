import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testWithStaticTyping() {
        ReferenceType type = ReferenceType.construct(String.class, null, null, null);
        assertFalse(type.withStaticTyping().isReferenceType());
    }

    @Test
    public void testWithContentValueHandler() {
        Object h = new Object();
        ReferenceType type = ReferenceType.construct(String.class, null, null, null);
        assertEquals(type, type.withContentValueHandler(null));
        assertTrue(type.withContentValueHandler(h).getReferencedType().getValueHandler() == h);
    }

    @Test
    public void testWithContentTypeHandler() {
        Object h = new Object();
        ReferenceType type = ReferenceType.construct(String.class, null, null, null);
        assertEquals(type, type.withContentTypeHandler(null));
        assertTrue(type.withContentTypeHandler(h).getReferencedType().getTypeHandler() == h);
    }

    @Test
    public void testWithValueHandler() {
        Object h = new Object();
        ReferenceType type = ReferenceType.construct(String.class, null, null, null);
        assertEquals(type, type.withValueHandler(null));
        assertTrue(type.withValueHandler(h).getValueHandler() == h);
    }

    @Test
    public void testWithContentValueHandler() {
        Object h = new Object();
        ReferenceType type = ReferenceType.construct(String.class, null, null, null);
        assertEquals(type, type.withContentValueHandler(null));
        assertTrue(type.withContentValueHandler(h).getReferencedType().getValueHandler() == h);
    }

    @Test
    public void testWithContentValueHandler() {
        Object h = new Object();
        ReferenceType type = ReferenceType.construct(String.class, null, null, null);
        assertEquals(type, type.withContentValueHandler(null));
        assertTrue(type.withContentValueHandler(h).getReferencedType().getValueHandler() == h);
    }

    @Test
    public void testWithContentValueHandler() {
        Object h = new Object();
        ReferenceType type = ReferenceType.construct(String.class, null, null, null);
        assertEquals(type, type.withContentValueHandler(null));
        assertTrue(type.withContentValueHandler(h).getReferencedType().getValueHandler() == h);
    }

    @Test
    public void testWithContentValueHandler() {
        Object h = new Object();
        ReferenceType type = ReferenceType.construct(String.class, null, null, null);
        assertEquals(type, type.withContentValueHandler(null));
        assertTrue(type.withContentValueHandler(h).getReferencedType().getValueHandler() == h);
    }

    @Test
    public void testBuildCanonicalName() {
        ReferenceType type = ReferenceType.construct(String.class, null, null, null);
        assertEquals("java.lang.String<T>", type.buildCanonicalName());
    }

    @Test
    public void test_narrow() {
        ReferenceType type = ReferenceType.construct(String.class, null, null, null);
        assertEquals(type, type._narrow(String.class));
    }

    @Test
    public void testToString() {
        ReferenceType type = ReferenceType.construct(String.class, null, null, null);
        assertEquals("[reference type, class java.lang.String<T>]", type.toString());
    }

    @Test
    public void testEquals() {
        ReferenceType type1 = ReferenceType.construct(String.class, null, null, null);
        ReferenceType type2 = ReferenceType.construct(String.class, null, null, null);
        assertEquals(true, type1.equals(type2));
    }
}