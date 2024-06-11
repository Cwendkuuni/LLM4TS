import static org.junit.Assert.*;
import org.junit.Test;

public class JavaTypeTest {

    @Test
    public void testWithStaticTyping() {
        JavaType javaType = new JavaType(JavaType.class);
        JavaType withStaticTyping = javaType.withStaticTyping();
        assertTrue(withStaticTyping.useStaticType());
    }

    @Test
    public void testNarrowBy() {
        JavaType javaType = new JavaType(JavaType.class);
        JavaType narrowed = javaType.narrowBy(Object.class);
        assertEquals(Object.class, narrowed.getRawClass());
    }

    @Test
    public void testForcedNarrowBy() {
        JavaType javaType = new JavaType(JavaType.class);
        JavaType forcedNarrowed = javaType.forcedNarrowBy(Object.class);
        assertEquals(Object.class, forcedNarrowed.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowByIncompatibleClass() {
        JavaType javaType = new JavaType(JavaType.class);
        javaType.narrowBy(String.class);
    }

    @Test
    public void testWidenBy() {
        JavaType javaType = new JavaType(Object.class);
        JavaType widened = javaType.widenBy(JavaType.class);
        assertEquals(JavaType.class, widened.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWidenByIncompatibleClass() {
        JavaType javaType = new JavaType(Object.class);
        javaType.widenBy(String.class);
    }

    @Test
    public void testUseStaticType() {
        JavaType javaType = new JavaType(Object.class);
        assertFalse(javaType.useStaticType());
    }

    @Test
    public void testContainedTypeOrUnknownWithNull() {
        JavaType javaType = new JavaType(Object.class);
        JavaType type = javaType.containedTypeOrUnknown(1);
        assertEquals(Object.class, type.getRawClass());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType javaType = new JavaType(Object.class);
        String signature = javaType.getGenericSignature();
        assertNotNull(signature);
    }

    @Test
    public void testGetErasedSignature() {
        JavaType javaType = new JavaType(Object.class);
        String signature = javaType.getErasedSignature();
        assertNotNull(signature);
    }

    @Test
    public void testEquals() {
        JavaType javaType1 = new JavaType(Object.class);
        JavaType javaType2 = new JavaType(Object.class);
        assertTrue(javaType1.equals(javaType2));
    }

    @Test
    public void testHashCode() {
        JavaType javaType = new JavaType(Object.class);
        assertEquals(System.identityHashCode(javaType), javaType.hashCode());
    }

}