
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.MyTokenizer;
import com.fasterxml.jackson.databind.type.TypeParser;
import com.fasterxml.jackson.databind.type.TypeReference;

import org.junit.Test;

public class TypeParserTest {
    @Test
    public void testParse() {
        TypeFactory factory = new TypeFactory();
        TypeParser typeParser = new TypeParser(factory);
        String canonical = "java.util.List<java.lang.Integer>";
        JavaType expectedType = new TypeReference<List<Integer>>() {}.getType();
        JavaType actualType = typeParser.parse(canonical);
        assertEquals(expectedType, actualType);
    }

    @Test
    public void testParseType() {
        TypeFactory factory = new TypeFactory();
        TypeParser typeParser = new TypeParser(factory);
        MyTokenizer tokens = new MyTokenizer("java.lang.String");
        Class<?> expectedBase = String.class;
        JavaType actualType = typeParser.parseType(tokens);
        assertEquals(expectedBase, actualType._class);
    }

    @Test
    public void testParseTypes() {
        TypeFactory factory = new TypeFactory();
        TypeParser typeParser = new TypeParser(factory);
        MyTokenizer tokens = new MyTokenizer("java.util.List<java.lang.Integer>,java.lang.String");
        TypeReference<List<Integer>> firstType = new TypeReference<List<Integer>>() {};
        TypeReference<String> secondType = new TypeReference<String>() {};
        List<JavaType> expectedTypes = new ArrayList<>();
        expectedTypes.add(firstType.getType());
        expectedTypes.add(secondType.getType());
        List<JavaType> actualTypes = typeParser.parseTypes(tokens);
        assertEquals(expectedTypes.size(), actualTypes.size());
        for (int i = 0; i < actualTypes.size(); i++) {
            assertEquals(expectedTypes.get(i), actualTypes.get(i));
        }
    }

    @Test
    public void testParseEmptyString() {
        TypeFactory factory = new TypeFactory();
        TypeParser typeParser = new TypeParser(factory);
        String canonical = "";
        try {
            typeParser.parse(canonical);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            assertEquals("Failed to parse type '', problem: Failed to parse type '' (remaining: ''): Unexpected end-of-string",
                    expected.getMessage());
        }
    }

    @Test
    public void testParseInvalidType() {
        TypeFactory factory = new TypeFactory();
        TypeParser typeParser = new TypeParser(factory);
        String canonical = "InvalidType";
        try {
            typeParser.parse(canonical);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            assertEquals("Failed to parse type 'InvalidType' (remaining: ''), problem: Can not locate class 'InvalidType', problem: ",
                    expected.getMessage());
        }
    }

    @Test
    public void testParseMissingClosingGreaterThan() {
        TypeFactory factory = new TypeFactory();
        TypeParser typeParser = new TypeParser(factory);
        String canonical = "java.util.List<java.lang.Integer";
        try {
            typeParser.parse(canonical);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            assertEquals("Failed to parse type 'java.util.List<java.lang.Integer' (remaining: ''), problem: Unexpected tokens after complete type",
                    expected.getMessage());
        }
    }

    @Test
    public void testFindClassInvalidClassName() {
        TypeFactory factory = new TypeFactory();
        TypeParser typeParser = new TypeParser(factory);
        String className = "InvalidClass";
        try {
            typeParser.findClass(className, new MyTokenizer(""));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            assertEquals("Can not locate class 'InvalidClass', problem: ", expected.getMessage());
        }
    }

    @Test
    public void testFindClassException() {
        TypeFactory factory = new TypeFactory();
        TypeParser typeParser = new TypeParser(factory);
        String className = "java.lang.String";
        try {
            typeParser.findClass(className, new MyTokenizer(""));
            fail("Expected IllegalArgumentException");
        } catch (RuntimeException expected) {
            // Assuming findClass() method throws a checked exception,
            // this test case should be updated accordingly
            // String expectedMessage = "Expected message from findClass() method";
            // assertEquals("Failed to parse type '"+className+"' (remaining: ''), problem: "+expected.getMessage(),
            //         expectedMessage);
        }
    }
}