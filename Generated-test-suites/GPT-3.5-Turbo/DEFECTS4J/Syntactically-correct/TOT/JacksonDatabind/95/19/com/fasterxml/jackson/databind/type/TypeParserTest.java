import org.junit.*;
import static org.junit.Assert.*;

public class TypeParserTest {

  TypeFactory typeFactory = new TypeFactory();

  @Test
  public void testParse_ValidClass_Generics() {
    TypeParser typeParser = new TypeParser(typeFactory);
    JavaType javaType = typeParser.parse("java.util.List<java.lang.String>");
    
    assertTrue(javaType.isCollectionLikeType());
    assertFalse(javaType.isArrayType());
    assertEquals(List.class, javaType.getRawClass());
    assertEquals(1, javaType.getBindings().size());
    assertEquals(String.class, javaType.getBindings().getBoundType(0).getRawClass());
  }
  
  @Test
  public void testParse_ValidClass_NotGenerics() {
    TypeParser typeParser = new TypeParser(typeFactory);
    JavaType javaType = typeParser.parse("java.lang.Integer");
    
    assertFalse(javaType.isCollectionLikeType());
    assertFalse(javaType.isArrayType());
    assertEquals(Integer.class, javaType.getRawClass());
    assertNull(javaType.getBindings());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testParse_InvalidClass() {
    TypeParser typeParser = new TypeParser(typeFactory);
    JavaType javaType = typeParser.parse("com.some.invalid.Class");
    
    // This test should throw an IllegalArgumentException as it is trying to parse an invalid class
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testParse_UnexpectedTokensAfterCompleteType() {
    TypeParser typeParser = new TypeParser(typeFactory);
    JavaType javaType = typeParser.parse("java.lang.String extraTokens");
    
    // This test should throw an IllegalArgumentException as there are unexpected tokens after the complete type
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testParse_UnexpectedEndOfString() {
    TypeParser typeParser = new TypeParser(typeFactory);
    JavaType javaType = typeParser.parse("java.util.List<java.lang.String");
    
    // This test should throw an IllegalArgumentException as the string ends unexpectedly
  }

  @Test(expected = IllegalArgumentException.class)
  public void testParse_UnexpectedToken_Error() {
    TypeParser typeParser = new TypeParser(typeFactory);
    JavaType javaType = typeParser.parse("java.util.List<java.lang.String,false>");
    
    // This test should throw an IllegalArgumentException as the token is unexpected
  }

}