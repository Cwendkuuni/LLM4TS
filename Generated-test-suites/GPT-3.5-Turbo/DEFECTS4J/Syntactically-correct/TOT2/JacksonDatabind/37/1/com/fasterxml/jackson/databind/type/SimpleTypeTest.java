import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertNotNull(simpleType);
    }

    @Test
    public void testConstruct_MapType() {
        try {
            SimpleType.simpleType = SimpleType.construct(Map.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    @Test
    public void testConstruct_CollectionType() {
        try {
            SimpleType.simpleType = SimpleType.construct(Collection.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    @Test
    public void testConstruct_ArrayType() {
        try {
            SimpleType.simpleType = SimpleType.construct(String[].class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        try {
            simpleType.withContentType(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        try {
            simpleType.withContentTypeHandler(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    @Test
    public void testWithContentValueHandler() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        try {
            simpleType.withContentValueHandler(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    @Test
    public void testBuildCanonicalName() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        String canonicalName = simpleType.buildCanonicalName();
        assertNotNull(canonicalName);
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder erasedSignature = simpleType.getErasedSignature(sb);
        assertNotNull(erasedSignature);
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder genericSignature = simpleType.getGenericSignature(sb);
        assertNotNull(genericSignature);
    }

    @Test
    public void testToString() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        String stringRepresentation = simpleType.toString();
        assertNotNull(stringRepresentation);
    }

    @Test
    public void testEquals_SameInstance() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertTrue(simpleType.equals(simpleType));
    }

    @Test
    public void testEquals_EqualObjects() {
        SimpleType simpleType1 = SimpleType.constructUnsafe(String.class);
        SimpleType simpleType2 = SimpleType.constructUnsafe(String.class);
        assertTrue(simpleType1.equals(simpleType2));
    }

    @Test
    public void testEquals_DifferentObjects() {
        SimpleType simpleType1 = SimpleType.constructUnsafe(String.class);
        SimpleType simpleType2 = SimpleType.constructUnsafe(Integer.class);
        assertFalse(simpleType1.equals(simpleType2));
    }
}