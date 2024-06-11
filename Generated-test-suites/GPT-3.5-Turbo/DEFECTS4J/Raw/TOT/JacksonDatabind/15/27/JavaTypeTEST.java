import org.junit.Test;
import static org.junit.Assert.*;

public class JavaTypeTest {

    @Test
    public void testWithStaticTyping() {
        JavaType type = new JavaTypeImpl(Object.class);
        type = type.withStaticTyping();
        assertTrue(type.useStaticType());
    }

    @Test
    public void testNarrowBy() {
        JavaType type = new JavaTypeImpl(String.class);
        JavaType narrowType = type.narrowBy(Object.class);
        assertNotEquals(type, narrowType);
        assertEquals(Object.class, narrowType.getRawClass());
    }

    @Test
    public void testNarrowBySameClass() {
        JavaType type = new JavaTypeImpl(String.class);
        JavaType narrowType = type.narrowBy(String.class);
        assertEquals(type, narrowType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowByIncompatibleClass() {
        JavaType type = new JavaTypeImpl(String.class);
        type.narrowBy(Integer.class);
    }

    @Test
    public void testForcedNarrowBy() {
        JavaType type = new JavaTypeImpl(String.class);
        JavaType narrowType = type.forcedNarrowBy(Object.class);
        assertNotEquals(type, narrowType);
        assertEquals(Object.class, narrowType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testForcedNarrowByIncompatibleClass() {
        JavaType type = new JavaTypeImpl(String.class);
        type.forcedNarrowBy(Integer.class);
    }

    @Test
    public void testWidenBy() {
        JavaType type = new JavaTypeImpl(String.class);
        JavaType widenType = type.widenBy(Object.class);
        assertNotEquals(type, widenType);
        assertEquals(Object.class, widenType.getRawClass());
    }

    @Test
    public void testWidenBySameClass() {
        JavaType type = new JavaTypeImpl(Integer.class);
        JavaType widenType = type.widenBy(Integer.class);
        assertEquals(type, widenType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWidenByIncompatibleClass() {
        JavaType type = new JavaTypeImpl(String.class);
        type.widenBy(Integer.class);
    }

    @Test
    public void testContainedTypeOrUnknown() {
        JavaType type = new JavaTypeImpl(java.util.List.class);
        assertEquals(TypeFactory.unknownType(), type.containedTypeOrUnknown(0));
    }

}