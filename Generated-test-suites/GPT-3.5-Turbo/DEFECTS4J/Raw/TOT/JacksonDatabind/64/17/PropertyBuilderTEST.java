import static org.junit.Assert.*;
import org.junit.Test;

public class PropertyBuilderTest {

  @Test
  public void testGetClassAnnotations() {
    PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
    Annotations annotations = propertyBuilder.getClassAnnotations();
    assertNotNull(annotations);
  }

  @Test
  public void testBuildWriter() throws JsonMappingException {
    PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
    SerializerProvider prov = null;
    BeanPropertyDefinition propDef = null;
    JavaType declaredType = null;
    JsonSerializer<?> ser = null;
    TypeSerializer typeSer = null;
    TypeSerializer contentTypeSer = null;
    AnnotatedMember am = null;
    boolean defaultUseStaticTyping = false;
    BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
    assertNotNull(bpw);
  }

  @Test
  public void testFindSerializationType() throws JsonMappingException {
    PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
    Annotated a = null;
    boolean useStaticTyping = false;
    JavaType declaredType = null;
    JavaType serializationType = propertyBuilder.findSerializationType(a, useStaticTyping, declaredType);
    assertNull(serializationType);
  }

  @Test
  public void testGetPropertyDefaultValue() {
    PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
    String name = "test";
    AnnotatedMember member = null;
    JavaType type = null;
    Object defaultValue = propertyBuilder.getPropertyDefaultValue(name, member, type);
    assertNull(defaultValue);
  }

  @Test
  public void testGetDefaultValue() {
    PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
    JavaType type = null;
    Object defaultValue = propertyBuilder.getDefaultValue(type);
    assertNull(defaultValue);
  }

}