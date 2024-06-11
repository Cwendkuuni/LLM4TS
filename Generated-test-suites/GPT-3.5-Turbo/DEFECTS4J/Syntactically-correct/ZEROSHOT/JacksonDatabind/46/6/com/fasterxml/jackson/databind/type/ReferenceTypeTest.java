import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    private ReferenceType referenceType;

    @Before
    public void setUp() {
        referenceType = new ReferenceType(null, null, null, null, false);
    }

    @Test
    public void testConstruct() {
        ReferenceType result = ReferenceType.construct(null, null, null, null);
        assertNotNull(result);
        assertEquals(referenceType.getClass(), result.getClass());
    }

    @Test
    public void testWithTypeHandler() {
        ReferenceType result = referenceType.withTypeHandler(null);
        assertNotNull(result);
        assertEquals(referenceType.getClass(), result.getClass());
    }

    @Test
    public void testWithContentTypeHandler() {
        ReferenceType result = referenceType.withContentTypeHandler(null);
        assertNotNull(result);
        assertEquals(referenceType.getClass(), result.getClass());
    }

    @Test
    public void testWithValueHandler() {
        ReferenceType result = referenceType.withValueHandler(null);
        assertNotNull(result);
        assertEquals(referenceType.getClass(), result.getClass());
    }

    @Test
    public void testWithContentValueHandler() {
        ReferenceType result = referenceType.withContentValueHandler(null);
        assertNotNull(result);
        assertEquals(referenceType.getClass(), result.getClass());
    }

    @Test
    public void testWithStaticTyping() {
        ReferenceType result = referenceType.withStaticTyping();
        assertNotNull(result);
        assertEquals(referenceType.getClass(), result.getClass());
    }

    @Test
    public void testBuildCanonicalName() {
        String result = referenceType.buildCanonicalName();
        assertNotNull(result);
        assertEquals(String.class.getName() + "<" + null + ">", result);
    }

    @Test
    public void test_narrow() {
        JavaType result = referenceType._narrow(null);
        assertNotNull(result);
        assertEquals(ReferenceType.class, result.getClass());
    }

    @Test
    public void testGetReferencedType() {
        JavaType result = referenceType.getReferencedType();
        assertNull(result);
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
        JavaType result = referenceType.containedType(0);
        assertEquals(null, result);
    }

    @Test
    public void testContainedTypeName() {
        String result = referenceType.containedTypeName(0);
        assertEquals(null, result);
    }

    @Test
    public void testGetParameterSource() {
        Class<?> result = referenceType.getParameterSource();
        assertEquals(null, result);
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder result = referenceType.getErasedSignature(sb);
        assertNotNull(result);
        assertEquals(sb, result);
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder result = referenceType.getGenericSignature(sb);
        assertNotNull(result);
        assertEquals(sb, result);
    }

    @Test
    public void testToString() {
        String result = referenceType.toString();
        assertNotNull(result);
    }

    @Test
    public void testEquals() {
        assertTrue(referenceType.equals(referenceType));
        assertFalse(referenceType.equals(null));
        assertFalse(referenceType.equals(""));
        assertTrue(referenceType.equals(new ReferenceType(null, null, null, null, false)));
        assertFalse(referenceType.equals(new ReferenceType(null, null, null, null, true)));
    }
}