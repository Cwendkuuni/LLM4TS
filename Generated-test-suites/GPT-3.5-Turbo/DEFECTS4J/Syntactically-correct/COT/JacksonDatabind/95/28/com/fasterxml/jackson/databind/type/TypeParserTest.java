import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TypeParserTest {

    private TypeParser typeParser;

    @Before
    public void setUp() {
        typeParser = new TypeParser(new TypeFactory());
    }

    @Test
    public void testParse_shouldReturnJavaType() {
        String canonical = "java.util.Map<java.lang.String, java.lang.Integer>";
        JavaType result = typeParser.parse(canonical);

        assertNotNull(result);
        assertTrue(result instanceof MapType);
    }

    @Test
    public void testParse_shouldThrowIllegalArgumentExceptionWhenCanonicalIsEmpty() {
        try {
            typeParser.parse("");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type '' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
    }

    @Test
    public void testParseType_shouldReturnJavaType() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.util.Map<java.lang.String, java.lang.Integer>");
        JavaType result = typeParser.parseType(tokens);

        assertNotNull(result);
        assertTrue(result instanceof MapType);
    }

    @Test
    public void testParseType_shouldThrowIllegalArgumentExceptionWhenTokensIsEmpty() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("");

        try {
            typeParser.parseType(tokens);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type '' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
    }

    @Test
    public void testParseTypes_shouldReturnListOfJavaTypes() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.lang.String, java.lang.Integer");
        List<JavaType> result = typeParser.parseTypes(tokens);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testParseTypes_shouldThrowIllegalArgumentExceptionWhenTokensIsEmpty() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("");

        try {
            typeParser.parseTypes(tokens);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type '' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
    }

    @Test
    public void testFindClass_shouldReturnClass() {
        String className = "java.util.Map";
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.util.Map");

        Class<?> result = typeParser.findClass(className, tokens);

        assertNotNull(result);
    }

    @Test
    public void testFindClass_shouldThrowIllegalArgumentExceptionWhenClassNotFound() {
        String className = "com.myapp.MyClass";
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("com.myapp.MyClass");

        try {
            typeParser.findClass(className, tokens);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'com.myapp.MyClass' (remaining: 'com.myapp.MyClass'): Can not locate class 'com.myapp.MyClass', problem: null", e.getMessage());
        }
    }
}