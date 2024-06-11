import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.Test;

import static org.junit.Assert.*;

public class TypeParserTest {

    private TypeParser typeParser = new TypeParser(TypeFactory.defaultInstance());

    @Test
    public void testParse_withEmptyString_throwsIllegalArgumentException() {
        try {
            typeParser.parse("");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testParse_withNonEmptyString_parsesCorrectly() {
        String canonical = "java.util.List<java.lang.String>";
        JavaType javaType = typeParser.parse(canonical);
        assertNotNull(javaType);
        assertEquals(List.class, javaType.getRawClass());
        assertEquals(String.class, javaType.getContentType().getRawClass());
    }

    @Test
    public void testParseType_withEmptyString_throwsIllegalArgumentException() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("");
        try {
            typeParser.parseType(tokens);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testParseType_withNonEmptyString_parsesCorrectly() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.util.List");
        JavaType javaType = typeParser.parseType(tokens);
        assertNotNull(javaType);
        assertEquals(List.class, javaType.getRawClass());
    }

    @Test
    public void testParseTypes_withEmptyString_throwsIllegalArgumentException() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("");
        try {
            typeParser.parseTypes(tokens);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testParseTypes_withNonEmptyString_parsesCorrectly() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.util.List<java.lang.String>");
        List<JavaType> javaTypes = typeParser.parseTypes(tokens);
        assertNotNull(javaTypes);
        assertEquals(1, javaTypes.size());
        JavaType javaType = javaTypes.get(0);
        assertEquals(List.class, javaType.getRawClass());
        assertEquals(String.class, javaType.getContentType().getRawClass());
    }

    @Test
    public void testFindClass_withNonExistingClass_throwsIllegalArgumentException() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("com.example.NonExistingClass");
        try {
            typeParser.findClass(tokens.nextToken(), tokens);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    // Additional edge cases and exception handling scenarios can be added here

}