import static org.junit.Assert.*;
import org.junit.Test;

public class TypeParserTest {

    @Test
    public void testParse_withSimpleName_shouldReturnJavaType() {
        TypeParser typeParser = new TypeParser(new TypeFactory());
        JavaType javaType = typeParser.parse("String");
        assertNotNull(javaType);
        assertEquals(String.class, javaType.getRawClass());
    }

    @Test
    public void testParse_withGenericClass_shouldReturnJavaType() {
        TypeParser typeParser = new TypeParser(new TypeFactory());
        JavaType javaType = typeParser.parse("List<String>");
        assertNotNull(javaType);
        assertTrue(javaType.isContainerType());
        assertEquals(List.class, javaType.getRawClass());
        assertEquals(String.class, javaType.getContentType().getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_withInvalidType_shouldThrowException() {
        TypeParser typeParser = new TypeParser(new TypeFactory());
        typeParser.parse("InvalidType");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_withUnexpectedTokens_shouldThrowException() {
        TypeParser typeParser = new TypeParser(new TypeFactory());
        typeParser.parse("List<String> UnexpectedTokens");
    }

    @Test
    public void testParse_withEmptyString_shouldThrowException() {
        TypeParser typeParser = new TypeParser(new TypeFactory());
        try {
            typeParser.parse("");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Unexpected end-of-string", e.getMessage());
        }
    }

    @Test
    public void testParse_withNestedGenerics_shouldReturnJavaType() {
        TypeParser typeParser = new TypeParser(new TypeFactory());
        JavaType javaType = typeParser.parse("Map<String, List<Integer>>");
        assertNotNull(javaType);
        assertTrue(javaType.isContainerType());
        assertEquals(Map.class, javaType.getRawClass());
        assertEquals(String.class, javaType.getKeyType().getRawClass());
        assertTrue(javaType.getContentType().isContainerType());
        assertEquals(List.class, javaType.getContentType().getRawClass());
        assertEquals(Integer.class, javaType.getContentType().getContentType().getRawClass());
    }

    @Test
    public void testParse_withExtraWhitespaces_shouldReturnJavaType() {
        TypeParser typeParser = new TypeParser(new TypeFactory());
        JavaType javaType = typeParser.parse("  Set  <   Boolean   >   ");
        assertNotNull(javaType);
        assertTrue(javaType.isContainerType());
        assertEquals(Set.class, javaType.getRawClass());
        assertEquals(Boolean.class, javaType.getContentType().getRawClass());
    }

    @Test
    public void testParse_withNoGenericType_shouldReturnJavaType() {
        TypeParser typeParser = new TypeParser(new TypeFactory());
        JavaType javaType = typeParser.parse("UUID");
        assertNotNull(javaType);
        assertFalse(javaType.isContainerType());
        assertEquals(UUID.class, javaType.getRawClass());
    }

}