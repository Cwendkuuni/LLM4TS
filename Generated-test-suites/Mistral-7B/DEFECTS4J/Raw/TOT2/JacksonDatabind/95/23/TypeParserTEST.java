import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeParser;

public class TypeParserTest {
    @Test
    public void testParse() {
        String canonicalType = "java.lang.String";
        TypeFactory typeFactory = new TypeFactory();
        TypeParser typeParser = new TypeParser(typeFactory);
        JavaType parsedType = typeParser.parse(canonicalType);
        assertEquals(JavaType.getType(String.class), parsedType);
    }
}