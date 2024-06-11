import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import org.junit.Test;

public class TypeParserTest {

    @Test
    public void testParse() {
        TypeFactory typeFactory = new TypeFactory();
        TypeParser typeParser = new TypeParser(typeFactory);
        JavaType parsedType = typeParser.parse("int");
        assertEquals("java.lang.Integer", parsedType.getClassName());
    }
}