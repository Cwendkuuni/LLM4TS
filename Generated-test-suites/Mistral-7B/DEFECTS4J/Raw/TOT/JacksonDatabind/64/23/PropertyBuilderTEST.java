import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

 private ObjectMapper mapper;
 private PropertyBuilder propertyBuilder;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testBuildWriter_StringProperty_DefaultValues() throws JsonMappingException {
 String propertyName = "testProperty";
 BeanPropertyDefinition propDef = new BeanPropertyDefinition(propertyName, String.class);
 JavaType declaredType = mapper.getTypeFactory().constructType(String.class);
 BeanPropertyWriter expectedWriter = new BeanPropertyWriter(propDef, null, null, declaredType, null, null, null, false, null);

 SerializationConfig config = mapper.getSerializationConfig();
 BeanDescription beanDesc = new BeanDescription(String.class);
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
 BeanPropertyWriter actualWriter = propertyBuilder.buildWriter(mapper.getSerializerProvider(), propDef, declaredType, null, null, null, null, false);

 assertEquals(expectedWriter, actualWriter);
 }

 @Test
 public void testBuildWriter_IntegerProperty_DefaultUseStaticTyping_DeclaredType() throws JsonMappingException {
 String propertyName = "testProperty";
 BeanPropertyDefinition propDef = new BeanPropertyDefinition(propertyName, Integer.TYPE);
 JavaType declaredType = mapper.getTypeFactory().constructType(Integer.TYPE);
 JsonSerialize.Typing typing = JsonSerialize.Typing.STATIC;

 SerializationConfig config = mapper.getSerializationConfig();
 BeanDescription beanDesc = new BeanDescription(Integer.class);
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
 BeanPropertyWriter actualWriter = propertyBuilder.buildWriter(mapper.getSerializerProvider(), propDef, declaredType, null, null, null, null, true, null);

 JavaType actualType = actualWriter.getType();
 assertEquals(IntTypeSerializer.instance.getType(Integer.TYPE), actualType);
 }

 @Test
 public void testBuildWriter_IntegerProperty_DefaultUseStaticTyping_ContentTypeSerializer() throws JsonMappingException {
 String propertyName = "testProperty";
 BeanPropertyDefinition propDef = new BeanPropertyDefinition(propertyName, Integer.TYPE);
 JavaType declaredType = mapper.getTypeFactory().constructType(Integer.TYPE);
 TypeSerializer contentTypeSer = IntTypeSerializer.instance;

 SerializationConfig config = mapper.getSerializationConfig();
 BeanDescription beanDesc = new BeanDescription(Integer.class);
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
 BeanPropertyWriter actualWriter = propertyBuilder.buildWriter(mapper.getSerializerProvider(), propDef, declaredType, null, null, null, null, true, contentTypeSer);

 JavaType actualType = actualWriter.getType();
 assertEquals(IntTypeSerializer.instance.getType(Integer.TYPE), actualType);
 }

 @Test
 public void testBuildWriter_IntegerProperty_DefaultUseStaticTyping_ArrayDefaultValue() throws JsonMappingException {
 String propertyName = "testProperty";
 BeanPropertyDefinition propDef = new BeanPropertyDefinition(propertyName, Integer[].class);
 JavaType declaredType = mapper.getTypeFactory().constructType(Integer[].class);
 JsonSerialize.Typing typing = JsonSerialize.Typing.STATIC;

 SerializationConfig config = mapper.getSerializationConfig();
 BeanDescription beanDesc = new BeanDescription(Integer[].class);
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
 BeanPropertyWriter actualWriter = propertyBuilder.buildWriter(mapper.getSerializerProvider(), propDef, declaredType, null, null, null, null, true, null);

 BeanPropertyWriter expectedWriter = new BeanPropertyWriter(propDef, null, null, declaredType, null, null, null, false, ArrayBuilders.getArrayComparator(new int[0]));
 assertEquals(expectedWriter, actualWriter);
 }

 @Test
 public void testBuildWriter_NullSuppression() throws JsonMappingException {
 String propertyName = "testProperty";
 BeanPropertyDefinition propDef = new BeanPropertyDefinition(propertyName, String.class);
 JavaType declaredType = mapper.getTypeFactory().constructType(String.class);

 SerializationConfig config = mapper.getSerializationConfig();
 config.setSerializationInclusion(JsonInclude.Include.NON_NULL);
 BeanDescription beanDesc = new BeanDescription(String.class);
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
 BeanPropertyWriter expectedWriter = new BeanPropertyWriter(propDef, null, null, declaredType, null, null, null, true, BeanPropertyWriter.MARKER_FOR_EMPTY);
 BeanPropertyWriter actualWriter = propertyBuilder.buildWriter(mapper.getSerializerProvider(), propDef, declaredType, null, null, null, null, false);

 assertEquals(expectedWriter, actualWriter);
 }

 @Test
 public void testFindSerializationType_StringPropertyWithJsonSerializeAnnotation() throws JsonMappingException {
 String propertyName = "testProperty";
 AnnotatedMember am = new AnnotatedMember(propertyName, String.class);
 JsonSerialize annotation = new JsonSerialize();
 annotation.using(new StdSerializerProvider.StdSerializerFactory(StringSerializer.class));
 am.addAnnotation(annotation);
 JavaType declaredType = mapper.getTypeFactory().constructType(String.class);

 SerializationConfig config = mapper.getSerializationConfig();
 BeanDescription beanDesc = new BeanDescription(String.class);
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
 JavaType actualType = propertyBuilder.findSerializationType(am, false, declaredType);

 assertEquals(StringSerializer.class, actualType.getRawClass());
 }

 @Test
 public void testFindSerializationType_Null() throws JsonMappingException {
 String propertyName = "testProperty";
 BeanPropertyDefinition propDef = new BeanPropertyDefinition(propertyName, String.class);
 JavaType declaredType = mapper.getTypeFactory().constructType(String.class);

 SerializationConfig config = mapper.getSerializationConfig();
 BeanDescription beanDesc = new BeanDescription(String.class);
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
 JavaType actualType = propertyBuilder.findSerializationType(null, false, declaredType);
 assertNull(actualType);
 }

 @Test(expected = JsonMappingException.class)
 public void testFindSerializationType_UnsupportedType() throws JsonMappingException {
 String propertyName = "testProperty";
 AnnotatedMember am = new AnnotatedMember(propertyName, new Object() {}.getClass());
 JavaType declaredType = mapper.getTypeFactory().constructType(new Object() {}.getClass());

 SerializationConfig config = mapper.getSerializationConfig();
 BeanDescription beanDesc = new BeanDescription(Object.class);
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
 propertyBuilder.findSerializationType(am, false, declaredType);
 }
}