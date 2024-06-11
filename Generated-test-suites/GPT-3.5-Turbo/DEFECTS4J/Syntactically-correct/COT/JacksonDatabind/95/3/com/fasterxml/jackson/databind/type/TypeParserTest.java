
import com.fasterxml.jackson.databind.type.TypeParser;
import com.fasterxml.jackson.databind.JavaType;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeParserTest {
    
    private TypeParser typeParser;
    
    @Before
    public void setUp() {
        typeParser = new TypeParser();
    }
    
    @Test
    public void testParse_withValidCanonical() {
        String canonical = "java.util.List<java.lang.String>";
        JavaType javaType = typeParser.parse(canonical);
        assertNotNull(javaType);
        assertEquals("java.util.List", javaType.getRawClass().getName());
        assertEquals("java.lang.String", javaType.getContentType().getRawClass().getName());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParse_withInvalidCanonical() {
        String canonical = "java.util.List<";
        typeParser.parse(canonical);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParse_withUnexpectedTokens() {
        String canonical = "java.util.List<java.lang.String> unexpected tokens";
        typeParser.parse(canonical);
    }
    
    @Test
    public void testParseType_withNonGeneric() {
        TypeParser.MyTokenizer tokens = typeParser.new MyTokenizer("java.lang.String");
        JavaType javaType = typeParser.parseType(tokens);
        assertNotNull(javaType);
        assertEquals("java.lang.String", javaType.getRawClass().getName());
        assertFalse(tokens.hasMoreTokens());
    }
    
    @Test
    public void testParseType_withGeneric() {
        TypeParser.MyTokenizer tokens = typeParser.new MyTokenizer("java.util.List<java.lang.String>");
        JavaType javaType = typeParser.parseType(tokens);
        assertNotNull(javaType);
        assertEquals("java.util.List", javaType.getRawClass().getName());
        assertEquals("java.lang.String", javaType.getContentType().getRawClass().getName());
        assertFalse(tokens.hasMoreTokens());
    }
    
    @Test
    public void testParseTypes_withSingleType() {
        TypeParser.MyTokenizer tokens = typeParser.new MyTokenizer("java.lang.String");
        List<JavaType> types = typeParser.parseTypes(tokens);
        assertNotNull(types);
        assertEquals(1, types.size());
        assertEquals("java.lang.String", types.get(0).getRawClass().getName());
        assertFalse(tokens.hasMoreTokens());
    }
    
    @Test
    public void testParseTypes_withMultipleTypes() {
        TypeParser.MyTokenizer tokens = typeParser.new MyTokenizer("java.lang.String, java.lang.Integer");
        List<JavaType> types = typeParser.parseTypes(tokens);
        assertNotNull(types);
        assertEquals(2, types.size());
        assertEquals("java.lang.String", types.get(0).getRawClass().getName());
        assertEquals("java.lang.Integer", types.get(1).getRawClass().getName());
        assertFalse(tokens.hasMoreTokens());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseTypes_withInvalidToken() {
        TypeParser.MyTokenizer tokens = typeParser.new MyTokenizer("java.lang.String, invalidToken");
        typeParser.parseTypes(tokens);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseTypes_withUnexpectedEnd() {
        TypeParser.MyTokenizer tokens = typeParser.new MyTokenizer("java.lang.String, java.lang.Integer");
        typeParser.parseTypes(tokens);
    }
    
    @Test
    public void testFindClass_withExistingClass() {
        TypeParser.MyTokenizer tokens = typeParser.new MyTokenizer("java.lang.String");
        Class<?> clazz = typeParser.findClass(tokens.nextToken(), tokens);
        assertEquals("java.lang.String", clazz.getName());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testFindClass_withNonExistingClass() {
        TypeParser.MyTokenizer tokens = typeParser.new MyTokenizer("nonexistingclass");
        typeParser.findClass(tokens.nextToken(), tokens);
    }
    
}
