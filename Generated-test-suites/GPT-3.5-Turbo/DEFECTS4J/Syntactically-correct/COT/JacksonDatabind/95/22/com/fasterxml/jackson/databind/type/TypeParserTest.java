
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.util.ArrayList;

import org.junit.Test;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class TypeParserTest {

    @Test
    public void testParse_withValidInput_shouldReturnJavaType() {
        // Arrange
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        String canonical = "java.util.List<java.lang.String>";
        JavaType expected = factory._fromClass(null, ArrayList.class, TypeBindings.create(String.class));

        // Act
        JavaType result = parser.parse(canonical);

        // Assert
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_withInvalidInput_shouldThrowIllegalArgumentException() {
        // Arrange
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        String canonical = "invalid";

        // Act
        parser.parse(canonical);

        // Assert (exception is expected)
        fail("IllegalArgumentException should have been thrown");
    }

    @Test
    public void testParseType_withNonGenericType_shouldReturnJavaType() {
        // Arrange
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("java.lang.String");
        Class<?> base = String.class;
        JavaType expected = factory._fromClass(null, base, null);

        // Act
        JavaType result = parser.parseType(tokens);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void testParseType_withGenericType_shouldReturnJavaType() {
        // Arrange
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("java.util.List");
        tokens.pushBack("<");
        tokens.pushBack("java.lang.String");
        Class<?> base = ArrayList.class;
        JavaType expected = factory._fromClass(null, base, TypeBindings.create(String.class));

        // Act
        JavaType result = parser.parseType(tokens);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void testParseTypes_withValidInput_shouldReturnListOfJavaTypes() {
        // Arrange
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("java.util.List<java.lang.String, java.lang.Integer>");
        ArrayList<JavaType> expected = new ArrayList<>();
        expected.add(factory._fromClass(null, String.class, null));
        expected.add(factory._fromClass(null, Integer.class, null));

        // Act
        ArrayList<JavaType> result = parser.parseTypes(tokens);

        // Assert
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseTypes_withInvalidInput_shouldThrowIllegalArgumentException() {
        // Arrange
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("java.util.List<java.lang.String");

        // Act
        parser.parseTypes(tokens);

        // Assert (exception is expected)
        fail("IllegalArgumentException should have been thrown");
    }

    @Test
    public void testFindClass_withExistingClass_shouldReturnClass() {
        // Arrange
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("java.util.List");
        String className = "java.util.List";
        Class<?> expected = ArrayList.class;

        // Act
        Class<?> result = parser.findClass(className, tokens);

        // Assert
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindClass_withInvalidClass_shouldThrowIllegalArgumentException() {
        // Arrange
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("java.util.Unknown");
        String className = "java.util.Unknown";

        // Act
        parser.findClass(className, tokens);

        // Assert (exception is expected)
        fail("IllegalArgumentException should have been thrown");
    }

    @Test
    public void testProblem_withErrorMessage_shouldThrowIllegalArgumentException() {
        // Arrange
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("java.util.Unknown");
        String msg = "Unknown class";

        // Act
        IllegalArgumentException ex = parser._problem(tokens, msg);

        // Assert
        assertEquals("Failed to parse type 'java.util.Unknown' (remaining: ''): Unknown class", ex.getMessage());
    }
}
