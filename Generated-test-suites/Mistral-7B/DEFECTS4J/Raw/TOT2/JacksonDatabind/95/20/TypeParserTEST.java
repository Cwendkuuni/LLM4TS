import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;

public class TypeParserTest {
    private TypeFactory typeFactory;
    private TypeParser typeParser;

    @org.junit.Before
    public void setUp() {
        typeFactory = new DefaultTypeFactory();
        typeParser = new TypeParser(typeFactory);
    }

    @org.junit.Test
    public void testParseString() {
        JavaType javaType = typeParser.parse("java.util.List<java.lang.String>");
        assertNotNull(javaType);
        assertEquals(List.class, javaType.getRawClass());
        assertEquals(String.class, javaType.getContentType());
    }
}