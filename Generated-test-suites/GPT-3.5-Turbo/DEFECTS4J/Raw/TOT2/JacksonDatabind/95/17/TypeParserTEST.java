import static org.junit.Assert.*;
import org.junit.Test;

public class TypeParserTest {
  
  // Test for parse method
  @Test
  public void testParse() {
    TypeParser typeParser = new TypeParser(new TypeFactory());
    JavaType type = typeParser.parse("java.util.List<java.lang.String>");
    assertNotNull(type);
    assertEquals("java.util.List<java.lang.String>", type.toCanonical());
  }
  
  // Test for parseType method
  @Test
  public void testParseType() {
    TypeParser typeParser = new TypeParser(new TypeFactory());
    MyTokenizer tokens = new MyTokenizer("java.util.List");
    JavaType type = typeParser.parseType(tokens);
    assertNotNull(type);
    assertEquals("java.util.List", type.toCanonical());
    
    tokens = new MyTokenizer("");
    try {
      typeParser.parseType(tokens);
    } catch (IllegalArgumentException e) {
      assertEquals("Unexpected end-of-string", e.getMessage());
    }
  }
  
  // Test for parseTypes method
  @Test
  public void testParseTypes() {
    TypeParser typeParser = new TypeParser(new TypeFactory());
    MyTokenizer tokens = new MyTokenizer("java.util.List<java.lang.String>");
    List<JavaType> types = typeParser.parseTypes(tokens);
    assertNotNull(types);
    assertEquals(1, types.size());
    assertEquals("java.util.List<java.lang.String>", types.get(0).toCanonical());
    
    tokens = new MyTokenizer("");
    try {
      typeParser.parseTypes(tokens);
    } catch (IllegalArgumentException e) {
      assertEquals("Unexpected end-of-string", e.getMessage());
    }
  }
  
  // Test for findClass method
  @Test
  public void testFindClass() {
    TypeParser typeParser = new TypeParser(new TypeFactory());
    MyTokenizer tokens = new MyTokenizer("java.lang.String");
    Class<?> clazz = typeParser.findClass("java.lang.String", tokens);
    assertNotNull(clazz);
    assertEquals(String.class, clazz);
    
    tokens = new MyTokenizer("invalid.class.name");
    try {
      typeParser.findClass("invalid.class.name", tokens);
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("Can not locate class"));
    }
  }
  
}