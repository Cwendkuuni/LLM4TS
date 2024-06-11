import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.databind.type.TypeParser;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.JavaType;

public class TypeParserTest {

    private TypeParser typeParser;

    @Before
    public void setUp() {
        TypeFactory typeFactory = new TypeFactory();
        typeParser = new TypeParser(typeFactory);
    }

    @Test
    public void testParse() {
        String canonical = "java.util.List<Integer>";
        JavaType result = typeParser.parse(canonical);
        assertNotNull(result);
        assertEquals("java.util.List", result.getRawClass().getName());
        assertEquals(Integer.class, result.getBindings().getTypeParameters().get(0).getRawClass());
    }

    @Test
    public void testParseType() {
        TypeParser.MyTokenizer tokens = typeParser.new MyTokenizer("java.util.List<Integer>");
        JavaType result = typeParser.parseType(tokens);
        assertNotNull(result);
        assertEquals("java.util.List", result.getRawClass().getName());
        assertEquals(Integer.class, result.getBindings().getTypeParameters().get(0).getRawClass());
    }

    @Test
    public void testParseTypes() {
        TypeParser.MyTokenizer tokens = typeParser.new MyTokenizer("java.util.List<Integer>");
        JavaType result = typeParser.parseType(tokens);
        assertNotNull(result);
        assertEquals("java.util.List", result.getRawClass().getName());
        assertEquals(Integer.class, result.getBindings().getTypeParameters().get(0).getRawClass());
    }

    @Test
    public void testFindClass() {
        String className = "java.util.List";
        TypeParser.MyTokenizer tokens = typeParser.new MyTokenizer("java.util.List<Integer>");
        Class<?> result = typeParser.findClass(className, tokens);
        assertNotNull(result);
        assertEquals(List.class, result);
    }

}