import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleTypeTest {

    // Test cases for constructor
    @Test
    public void testConstructor() {
        SimpleType simpleType = new SimpleType(String.class);
        assertEquals("com.fasterxml.jackson.databind.type.SimpleType", simpleType.getClass().getName());
        assertEquals(String.class, simpleType.getRawClass());
    }

    // Test cases for constructUnsafe
    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertEquals("com.fasterxml.jackson.databind.type.SimpleType", simpleType.getClass().getName());
        assertEquals(String.class, simpleType.getRawClass());
    }

    // Test cases for construct
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
    public void testConstruct() {
        SimpleType simpleType = SimpleType.construct(String.class);
        assertEquals("com.fasterxml.jackson.databind.type.SimpleType", simpleType.getClass().getName());
        assertEquals(String.class, simpleType.getRawClass());
    }

    // Test cases for narrow
    @Test
    public void testNarrow() {
        SimpleType simpleType = new SimpleType(String.class);
        JavaType narrowedType = simpleType._narrow(Object.class);
        assertEquals(Object.class, narrowedType.getRawClass());
    }

    // Test case for withContentType
    @Test(expected = IllegalArgumentException.class)
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(String.class);
        simpleType.withContentType(String.class);
    }

    // Test case for withTypeHandler
    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        Object typeHandler = new TypeHandler();
        SimpleType newSimpleType = simpleType.withTypeHandler(typeHandler);
        assertEquals(typeHandler, newSimpleType.getTypeHandler());
    }

    // Test case for withContentTypeHandler
    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        simpleType.withContentTypeHandler(new TypeHandler());
    }

    // Test case for withValueHandler
    @Test
    public void testWithValueHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        Object valueHandler = new ValueHandler();
        SimpleType newSimpleType = simpleType.withValueHandler(valueHandler);
        assertEquals(valueHandler, newSimpleType.getValueHandler());
    }

    // Test case for withContentValueHandler
    @Test(expected = IllegalArgumentException.class)
    public void testWithContentValueHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        simpleType.withContentValueHandler(new ValueHandler());
    }

    // Test case for withStaticTyping
    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(String.class);
        assertFalse(simpleType.isStaticTyping());
        SimpleType newSimpleType = simpleType.withStaticTyping();
        assertTrue(newSimpleType.isStaticTyping());
    }

    // Test case for refine
    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(String.class);
        JavaType refinedType = simpleType.refine(String.class, null, null, null);
        assertNull(refinedType);
    }

    // Test case for buildCanonicalName
    @Test
    public void testBuildCanonicalName() {
        SimpleType simpleType = new SimpleType(String.class);
        assertEquals("java.lang.String", simpleType.buildCanonicalName());
    }

    // Test case for isContainerType
    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(String.class);
        assertFalse(simpleType.isContainerType());
    }

    // Test case for getErasedSignature
    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = new SimpleType(String.class);
        StringBuilder sb = new StringBuilder();
        assertEquals("Ljava/lang/String;", simpleType.getErasedSignature(sb).toString());
    }

    // Test case for getGenericSignature
    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(String.class);
        StringBuilder sb = new StringBuilder();
        assertEquals("Ljava/lang/String;;", simpleType.getGenericSignature(sb).toString());
    }

    // Test case for toString
    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(String.class);
        assertEquals("[simple type, class java.lang.String]", simpleType.toString());
    }

    // Test case for equals
    @Test
    public void testEquals() {
        SimpleType simpleType1 = new SimpleType(String.class);
        SimpleType simpleType2 = new SimpleType(String.class);
        SimpleType simpleType3 = new SimpleType(Integer.class);

        assertTrue(simpleType1.equals(simpleType1));
        assertTrue(simpleType1.equals(simpleType2));
        assertFalse(simpleType1.equals(simpleType3));
    }

}