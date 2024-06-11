import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;

public class PropertyBuilderTest {

  @Test
  public void testBuildWriter() {
    SerializationConfig config = new SerializationConfig();
    BeanDescription beanDesc = new BeanDescription();
    PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
    SerializerProvider prov = new SerializerProvider();
    BeanPropertyDefinition propDef = new BeanPropertyDefinition();
    JavaType declaredType = new JavaType();
    JsonSerializer<?> ser = new JsonSerializer<>();
    TypeSerializer typeSer = new TypeSerializer();
    TypeSerializer contentTypeSer = new TypeSerializer();
    AnnotatedMember am = new AnnotatedMember();
    boolean defaultUseStaticTyping = true;

    try {
      BeanPropertyWriter writer = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);

      assertNotNull(writer);
      assertEquals(propDef, writer.getPropDef());
      assertEquals(am, writer.getMember());
      assertEquals(declaredType, writer.getDeclaringType());
      assertEquals(ser, writer.getSerializer());
      assertEquals(typeSer, writer.getTypeSerializer());
      assertEquals(contentTypeSer, writer.getContentTypeSerializer());
      assertEquals(defaultUseStaticTyping, writer.getDefaultUseStaticTyping());

    } catch (JsonMappingException e) {
      fail("Exception should not be thrown");
    }
  }

  @Test
  public void testFindSerializationType() {
    PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), new BeanDescription());
    Annotated a = new Annotated();
    boolean useStaticTyping = true;
    JavaType declaredType = new JavaType();

    try {
      JavaType serializationType = propertyBuilder.findSerializationType(a, useStaticTyping, declaredType);
      assertNull(serializationType);

    } catch (JsonMappingException e) {
      fail("Exception should not be thrown");
    }
  }

  @Test
  public void testGetPropertyDefaultValue() {
    PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), new BeanDescription());
    String name = "property";
    AnnotatedMember member = new AnnotatedMember();
    JavaType type = new JavaType();

    assertNull(propertyBuilder.getPropertyDefaultValue(name, member, type));
  }

  @Test
  public void testGetDefaultValue() {
    PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), new BeanDescription());
    JavaType type = new JavaType();

    assertNull(propertyBuilder.getDefaultValue(type));
  }

  @Test
  public void testThrowWrapped() {
    PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), new BeanDescription());
    String propName = "property";
    Object defaultBean = new Object();

    try {
      propertyBuilder._throwWrapped(new Exception(), propName, defaultBean);
      fail("Exception should be thrown");

    } catch (IllegalArgumentException e) {
      assertEquals("Failed to get property 'property' of default java.lang.Object instance", e.getMessage());
    }
  }
}