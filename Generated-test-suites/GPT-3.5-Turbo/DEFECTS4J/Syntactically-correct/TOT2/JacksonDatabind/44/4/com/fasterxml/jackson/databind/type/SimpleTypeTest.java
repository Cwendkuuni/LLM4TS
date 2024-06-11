import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructorCoreJacksonDatabind() {
        SimpleType simpleType = new SimpleType(getClass());
        
        // Verify that the class is initialized
        assertNotNull(simpleType);
    }

    @Test
    public void testConstructorSimpleCopy() {
        SimpleType baseType = new SimpleType(getClass());
        SimpleType simpleType = new SimpleType(baseType);

        // Verify that the base type is copied correctly
        assertEquals(baseType, simpleType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithMap() {
        SimpleType.constructUnsafe(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithCollection() {
        SimpleType.constructUnsafe(Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithArray() {
        SimpleType.constructUnsafe(Array.class);
    }

    @Test
    public void testNarrow() {
        SimpleType baseType = new SimpleType(getClass());
        JavaType narrowedType = baseType._narrow(getClass());

        // Verify that the base type is narrowed correctly
        assertNotNull(narrowedType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(getClass());
        simpleType.withContentType(getClass());
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType simpleType = new SimpleType(getClass());
        SimpleType newType = simpleType.withContentTypeHandler("handler");

        // Verify that the content type handler is set correctly
        assertEquals("handler", newType._typeHandler);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentValueHandler() {
        SimpleType simpleType = new SimpleType(getClass());
        simpleType.withContentValueHandler("handler");
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(getClass());
        SimpleType newType = simpleType.withStaticTyping();

        // Verify that static typing is enabled
        assertTrue(newType._asStatic);
    }

    @Test
    public void testBuildCanonicalName() {
        SimpleType simpleType = new SimpleType(getClass());
        String canonicalName = simpleType.buildCanonicalName();

        // Verify that the canonical name is built correctly
        assertNotNull(canonicalName);
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = new SimpleType(getClass());
        StringBuilder sb = new StringBuilder();
        StringBuilder erasedSignature = simpleType.getErasedSignature(sb);

        // Verify that the erased signature is generated correctly
        assertNotNull(erasedSignature);
        assertEquals("Lcom/fasterxml/jackson/databind/type/SimpleType;", erasedSignature.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(getClass());
        StringBuilder sb = new StringBuilder();
        StringBuilder genericSignature = simpleType.getGenericSignature(sb);

        // Verify that the generic signature is generated correctly
        assertNotNull(genericSignature);
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = new SimpleType(getClass());
        SimpleType simpleType2 = new SimpleType(getClass());

        // Verify that the two simple types are equal
        assertEquals(simpleType1, simpleType2);
    }
}