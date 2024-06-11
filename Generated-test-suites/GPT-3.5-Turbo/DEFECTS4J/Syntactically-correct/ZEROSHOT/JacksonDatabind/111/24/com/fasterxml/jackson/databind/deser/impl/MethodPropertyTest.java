import static org.junit.Assert.*;
import org.junit.Test;

public class MethodPropertyTest {

  @Test
  public void testConstructor() {
    BeanPropertyDefinition propDef = new BeanPropertyDefinition();
    JavaType type = new JavaType();
    TypeDeserializer typeDeser = new TypeDeserializer();
    Annotations contextAnnotations = new Annotations();
    AnnotatedMethod method = new AnnotatedMethod();

    MethodProperty property = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);

    assertNotNull(property);
    assertEquals(propDef, property.getDefinition());
    assertEquals(type, property.getType());
    assertEquals(typeDeser, property.getTypeDeserializer());
    assertEquals(contextAnnotations, property.getContextAnnotations());
    assertEquals(method, property.getMember());
  }

  @Test
  public void testCopyConstructorWithDeserializerAndNullValueProvider() {
    BeanPropertyDefinition propDef = new BeanPropertyDefinition();
    JavaType type = new JavaType();
    TypeDeserializer typeDeser = new TypeDeserializer();
    Annotations contextAnnotations = new Annotations();
    AnnotatedMethod method = new AnnotatedMethod();

    MethodProperty src = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
    JsonDeserializer<?> deser = new JsonDeserializer<>();
    NullValueProvider nva = new NullValueProvider();

    MethodProperty property = new MethodProperty(src, deser, nva);

    assertNotNull(property);
    assertEquals(propDef, property.getDefinition());
    assertEquals(type, property.getType());
    assertEquals(typeDeser, property.getTypeDeserializer());
    assertEquals(contextAnnotations, property.getContextAnnotations());
    assertEquals(deser, property.getValueDeserializer());
    assertEquals(nva, property.getNullValueProvider());
  }

  @Test
  public void testCopyConstructorWithNewName() {
    BeanPropertyDefinition propDef = new BeanPropertyDefinition();
    JavaType type = new JavaType();
    TypeDeserializer typeDeser = new TypeDeserializer();
    Annotations contextAnnotations = new Annotations();
    AnnotatedMethod method = new AnnotatedMethod();

    MethodProperty src = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
    PropertyName newName = new PropertyName();

    MethodProperty property = new MethodProperty(src, newName);

    assertNotNull(property);
    assertEquals(propDef, property.getDefinition());
    assertEquals(newName, property.getFullName());
    assertEquals(type, property.getType());
    assertEquals(typeDeser, property.getTypeDeserializer());
    assertEquals(contextAnnotations, property.getContextAnnotations());
  }

  @Test
  public void testCopyConstructorWithMethod() {
    BeanPropertyDefinition propDef = new BeanPropertyDefinition();
    JavaType type = new JavaType();
    TypeDeserializer typeDeser = new TypeDeserializer();
    Annotations contextAnnotations = new Annotations();
    AnnotatedMethod method = new AnnotatedMethod();

    MethodProperty src = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
    Method m = new Method();

    MethodProperty property = new MethodProperty(src, m);

    assertNotNull(property);
    assertEquals(propDef, property.getDefinition());
    assertEquals(type, property.getType());
    assertEquals(typeDeser, property.getTypeDeserializer());
    assertEquals(contextAnnotations, property.getContextAnnotations());
  }
}