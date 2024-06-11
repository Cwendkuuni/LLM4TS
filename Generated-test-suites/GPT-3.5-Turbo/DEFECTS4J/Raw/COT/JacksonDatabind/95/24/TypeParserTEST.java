import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class TypeParserTest {

    @Test
    public void testParse() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        JavaType type = parser.parse("java.util.List");
        assertEquals(List.class, type.getRawClass());
    }

    @Test
    public void testParseType() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("java.util.List<java.lang.Integer>");
        JavaType type = parser.parseType(tokens);
        assertEquals(List.class, type.getRawClass());
        assertEquals(Integer.class, type.containedType(0).getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseTypeWithInvalidToken() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("java.util.List<java.lang.Integer,java.lang.String>");
        parser.parseType(tokens);
    }

    // Additional edge cases and exception handling scenarios go here

}

