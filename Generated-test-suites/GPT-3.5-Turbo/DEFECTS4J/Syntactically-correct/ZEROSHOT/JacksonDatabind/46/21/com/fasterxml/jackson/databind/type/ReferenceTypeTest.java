import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ReferenceTypeTest {

    private ReferenceType referenceType;

    @Before
    public void setUp() {
        referenceType = ReferenceType.construct(null, null, null, null);
    }

    @Test
    public void testWithContentTypeHandler() {
        Object h = null;
        ReferenceType result = referenceType.withContentTypeHandler(h);
        assertNotNull(result);
    }

    @Test
    public void testWithContentValueHandler() {
        Object h = null;
        ReferenceType result = referenceType.withContentValueHandler(h);
        assertNotNull(result);
    }

    @Test
    public void testWithStaticTyping() {
        ReferenceType result = referenceType.withStaticTyping();
        assertNotNull(result);
    }

    @Test
    public void testBuildCanonicalName() {
        String result = referenceType.buildCanonicalName();
        assertNotNull(result);
    }

    @Test
    public void test_narrow() {
        Class<?> subclass = null;
        JavaType result = referenceType._narrow(subclass);
        assertNotNull(result);
    }

    @Test
    public void testGetReferencedType() {
        JavaType result = referenceType.getReferencedType();
        assertNotNull(result);
    }

    @Test
    public void testIsReferenceType() {
        boolean result = referenceType.isReferenceType();
        assertTrue(result);
    }

    @Test
    public void testContainedTypeCount() {
        int result = referenceType.containedTypeCount();
        assertEquals(1, result);
    }

    @Test
    public void testContainedType() {
        int index = 0;
        JavaType result = referenceType.containedType(index);
        assertNotNull(result);
    }

    @Test
    public void testContainedTypeName() {
        int index = 0;
        String result = referenceType.containedTypeName(index);
        assertNotNull(result);
    }

    @Test
    public void testGetParameterSource() {
        Class<?> result = referenceType.getParameterSource();
        assertNotNull(result);
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder result = referenceType.getErasedSignature(sb);
        assertNotNull(result);
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder result = referenceType.getGenericSignature(sb);
        assertNotNull(result);
    }

    @Test
    public void testToString() {
        String result = referenceType.toString();
        assertNotNull(result);
    }

    @Test
    public void testEquals() {
        Object o = null;
        boolean result = referenceType.equals(o);
        assertFalse(result);
    }
}