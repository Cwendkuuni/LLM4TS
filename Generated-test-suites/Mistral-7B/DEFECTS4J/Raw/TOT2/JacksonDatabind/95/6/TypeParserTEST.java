
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.TypeReference;
import com.fasterxml.jackson.databind.type.TypeParser;
import com.fasterxml.jackson.databind.type.TypeParser.MyTokenizer;

public class TypeParserTest {

    @Test
    public void testParseCanonicalType() {
        TypeParser parser = new TypeParser(new TypeFactory());
        String canonicalType = "java.lang.String";
        JavaType expectedType = new TypeReference<String>() {}.getType();
        JavaType actualType = parser.parse(canonicalType);
        assertEquals(expectedType, actualType);
    }

    @Test
    public void testParseTypeWithValidTokens() {
        TypeParser parser = new TypeParser(new TypeFactory());
        String canonicalType = "java.lang.String";
        MyTokenizer tokens = new MyTokenizer(canonicalType.trim());
        JavaType expectedType = new TypeReference<String>() {}.getType();
        JavaType actualType = parser.parseType(tokens);
        assertEquals(expectedType, actualType);
    }

    @Test
    public void testParseTypesWithValidTokens() {
        TypeParser parser = new TypeParser(new TypeFactory());
        String canonicalType = "java.lang.List<java.lang.String>";
        MyTokenizer tokens = new MyTokenizer(canonicalType.trim());
        List<JavaType> expectedTypes = Arrays.asList(String.class, Collection.class, List.class);
        List<JavaType> actualTypes = parser.parseTypes(tokens);
        assertContentEquals(expectedTypes, actualTypes);
    }

    @Test
    public void testFindClassWithValidClassName() {
        TypeParser parser = new TypeParser(new TypeFactory());
        String className = "java.lang.String";
        JavaType expectedType = new TypeReference<String>() {}.getType();
        Class<?> actualType = parser.findClass(className, new MyTokenizer(""));
        assertEquals(expectedType.getRawClass(), actualType);
    }

    @Test
    public void testParseEmptyString() {
        TypeParser parser = new TypeParser(new TypeFactory());
        String canonicalType = "";
        parser.parse(canonicalType);
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testParseTypeNoMoreTokens() {
        TypeParser parser = new TypeParser(new TypeFactory());
        MyTokenizer tokens = new MyTokenizer("");
        parser.parseType(tokens);
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testParseTypesNoMoreTokens() {
        TypeParser parser = new TypeParser(new TypeFactory());
        MyTokenizer tokens = new MyTokenizer("");
        parser.parseTypes(tokens);
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testFindClassInvalidClassName() {
        TypeParser parser = new TypeParser(new TypeFactory());
        String className = "InvalidClassName";
        parser.findClass(className, new MyTokenizer(""));
        fail("Expected IllegalArgumentException");
    }
}
