import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeParser;
import org.junit.Test;

public class TypeParserTest {

    @Test
    public void testParse() {
        TypeParser typeParser = new TypeParser(new TypeFactory());
        JavaType type = typeParser.parse("java.lang.String");
        assertEquals("java.lang.String", type.getClassName());
    }
}