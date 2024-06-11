import org.junit.Test;
import static org.junit.Assert.*;

public class ClassNameIdResolverTest {
    
  @Test
  public void testGetMechanism() {
    ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
    JsonTypeInfo.Id mechanism = resolver.getMechanism();
    assertEquals(JsonTypeInfo.Id.CLASS, mechanism);
  }
  
  @Test
  public void testRegisterSubtype() {
    ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
    resolver.registerSubtype(Object.class, "object");
    // No assertion, the method doesn't have any functionality.
  }
  
  @Test
  public void testIdFromValue() {
    ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
    String id = resolver.idFromValue("test");
    assertEquals("java.lang.String", id);
  }
  
  @Test
  public void testIdFromValueAndType() {
    ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
    String id = resolver.idFromValueAndType("test", String.class);
    assertEquals("java.lang.String", id);
  }
  
  @Test
  public void testTypeFromId() throws IOException {
    ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
    JavaType type = resolver.typeFromId(null, "java.lang.String");
    assertEquals(String.class, type.getRawClass());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testTypeFromIdInvalidId() throws IOException {
    ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
    JavaType type = resolver.typeFromId(null, "invalid.id");
    // Expecting IllegalArgumentException to be thrown
  }
  
  @Test(expected = ClassNotFoundException.class)
  public void testTypeFromIdClassNotFoundException() throws IOException {
    ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
    JavaType type = resolver.typeFromId(null, "com.example.DoesNotExist");
    // Expecting ClassNotFoundException to be thrown
  }
  
  @Test
  public void testTypeFromIdUnknownTypeId() throws IOException {
    ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
    try {
      JavaType type = resolver.typeFromId(new DeserializationContext(null, null), "unknown.id");
      fail("Expected JsonMappingException not thrown");
    } catch (JsonMappingException e) {
      // Assertion if the exception is thrown correctly
      assertEquals("no such class found", e.getMessage());
    }
  }

  @Test
  public void test_typeFromId_genericsInfoAdded() throws IOException {
    ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
    JavaType type = resolver.typeFromId(null, "java.util.List<java.lang.String>");
    assertEquals(List.class, type.getRawClass());
    assertEquals(String.class, type.getContentType().getRawClass());
  }
  
  @Test
  public void test_idFrom() {
    ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
    String id = resolver._idFrom("test", String.class, null);
    assertEquals("java.lang.String", id);
  }
  
  @Test
  public void test_idFrom_enumSubtype() {
    ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
    String id = resolver._idFrom(Object.class, EnumSubtype.class, null);
    assertEquals("EnumSubtype", id);
  }
  
  @Test
  public void test_idFrom_enumSubtypeSubclass() {
    ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
    String id = resolver._idFrom(Object.class, EnumSubtype.Subclass.class, null);
    assertEquals("EnumSubtype$Subclass", id);
  }
  
  @Test
  public void test_idFrom_javaUtilEnumSet() {
    ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
    String id = resolver._idFrom(EnumSet.noneOf(EnumSubtype.class), EnumSet.class, null);
    assertEquals("java.util.EnumSet<EnumSubtype>", id);
  }
  
  @Test
  public void test_idFrom_javaUtilEnumMap() {
    ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
    String id = resolver._idFrom(new EnumMap<>(EnumSubtype.class), EnumMap.class, null);
    assertEquals("java.util.EnumMap<EnumSubtype, java.lang.Object>", id);
  }
  
  @Test
  public void test_idFrom_javaUtilArraysAsList() {
    ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
    String id = resolver._idFrom(Arrays.asList("test"), List.class, null);
    assertEquals("java.util.ArrayList", id);
  }
  
  @Test
  public void test_idFrom_innerClass() {
    ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
    String id = resolver._idFrom(new InnerClass(), InnerClass.Inner.class, null);
    assertEquals("ClassNameIdResolverTest$InnerClass$Inner", id);
  }
  
  private static class EnumSubtype extends Enum<EnumSubtype> {
    enum Subclass {}
  }
  
  private static class InnerClass {
    static class Inner {}
  }

}