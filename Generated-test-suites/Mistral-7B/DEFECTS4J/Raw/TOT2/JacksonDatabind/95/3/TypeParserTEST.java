import static org.junit.Assert.assertEquals;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import org.junit.Test;

public class TypeParserTest {
    @Test
    public void testParse_ValidType() throws Exception {
        String canonicalType = "int";
        TypeFactory typeFactory = new DefaultTypeFactory(); // Assuming DefaultTypeFactory is available
        TypeParser typeParser = new TypeParser(typeFactory);
        JavaType parsedType = typeParser.parse(canonicalType);
        assertEquals(JavaType.getType(int.class), parsedType);
    }
}