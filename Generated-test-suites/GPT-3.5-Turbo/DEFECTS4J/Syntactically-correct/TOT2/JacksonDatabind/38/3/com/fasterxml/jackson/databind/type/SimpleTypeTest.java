import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructor() {
        SimpleType simpleType = new SimpleType(String.class);
        assertNotNull(simpleType);
    }

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(Integer.class);
        assertNotNull(simpleType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMap() {
        SimpleType.construct(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollection() {
        SimpleType.construct(Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArray() {
        SimpleType.construct(String[].class);
    }

    @Test
    public void testTypeEquality() {
        SimpleType simpleType1 = SimpleType.constructUnsafe(Double.class);
        SimpleType simpleType2 = SimpleType.constructUnsafe(Double.class);
        assertEquals(simpleType1, simpleType2);
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = SimpleType.constructUnsafe(Float.class);
        SimpleType newSimpleType = simpleType.withStaticTyping();
        assertTrue(newSimpleType.isStaticTyping());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = SimpleType.constructUnsafe(Boolean.class);
        StringBuilder sb = simpleType.getErasedSignature(new StringBuilder());
        assertNotNull(sb);
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = SimpleType.constructUnsafe(Character.class);
        StringBuilder sb = simpleType.getGenericSignature(new StringBuilder());
        assertNotNull(sb);
    }

    @Test
    public void testConstructorWithSuperClass() {
        SimpleType simpleType = new SimpleType(String.class, null, new JavaType(), new JavaType[]{new JavaType()});
        assertNotNull(simpleType);
    }

    @Test
    public void testCopyConstructor() {
        SimpleType simpleType = new SimpleType(new TypeBase());
        assertNotNull(simpleType);
    }

}