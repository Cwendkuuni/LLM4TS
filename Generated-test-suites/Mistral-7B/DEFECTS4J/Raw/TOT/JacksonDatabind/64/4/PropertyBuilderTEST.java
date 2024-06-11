import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.fasterxml.jackson.databind.ser.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.ser.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.Types;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

 private SerializationConfig _config;
 private BeanDescription _beanDesc;
 private AnnotationIntrospector _annotationIntrospector;
 private Object _defaultBean;
 private PropertyBuilder _propertyBuilder;

 @Before
 public void setup() throws JsonMappingException {
 _config = new SerializationConfig();
 _beanDesc = new BeanDescription(new ClassInfo(MyPOJO.class));
 _annotationIntrospector = _config.getDefaultAnnotationIntrospector();
 _propertyBuilder = new PropertyBuilder(_config, _beanDesc);
 }

 @Test
 public void testDefaultInclusion() throws JsonMappingException {
 // Test default inclusion
 _config.setSerializationInclusion(JsonInclude.Include.ALWAYS);
 BeanPropertyWriter propWriter = _propertyBuilder.buildWriter(null, null, Types.constructType(MyPOJO.class), null, null, null, null, false);
 assertEquals(0, propWriter.countSerializedValues());

 MyPOJO myPojo = new MyPOJO();
 propWriter = _propertyBuilder.buildWriter(null, null, Types.constructType(MyPOJO.class), null, null, null, null, false);
 propWriter.writeValue(_config.getOutputFactory(), myPojo);
 String json = _config.getOutputFactory().getValueAsString();
 assertEquals("{}", json);
 }

 @Test
 public void testNonDefaultInclusion() throws JsonMappingException {
 // Test non-default inclusion
 _config.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
 MyPOJO myPojo = new MyPOJO();
 myPojo.setName("John Doe");
 BeanPropertyWriter propWriter = _propertyBuilder.buildWriter(null, null, Types.constructType(MyPOJO.class), null, null, null, null, false);
 propWriter = _propertyBuilder.buildWriter(null, null, Types.constructType(MyPOJO.class), null, null, null, null, false);
 propWriter.writeValue(_config.getOutputFactory(), myPojo);
 String json = _config.getOutputFactory().getValueAsString();
 assertEquals("{\"name\":\"John Doe\"}", json);

 MyPOJO defaultMyPojo = _propertyBuilder.getDefaultBean();
 BeanPropertyWriter defaultPropWriter = _propertyBuilder.buildWriter(null, null, Types.constructType(MyPOJO.class), null, null, null, null, false);
 defaultPropWriter = _propertyBuilder.buildWriter(null, null, Types.constructType(MyPOJO.class), null, null, null, null, false);
 defaultPropWriter.writeValue(_config.getOutputFactory(), defaultMyPojo);
 String defaultJson = _config.getOutputFactory().getValueAsString();
 assertNull(defaultJson);
 }

 @Test
 public void testNullSuppression() throws JsonMappingException {
 // Test null suppression
 _config.setSerializationInclusion(JsonInclude.Include.NON_NULL);
 MyPOJO myPojo = null;
 BeanPropertyWriter propWriter = _propertyBuilder.buildWriter(null, null, Types.constructType(MyPOJO.class), null, null, null, null, false);
 propWriter = _propertyBuilder.buildWriter(null, null, Types.constructType(MyPOJO.class), null, null, null, null, false);
 propWriter.writeValue(_config.getOutputFactory(), myPojo);
 String json = _config.getOutputFactory().getValueAsString();
 assertEquals("null", json);
 }

 @Test
 public void testEmptyCollectionSuppression() throws JsonMappingException {
 // Test empty collection suppression
 _config.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
 MyPOJO myPojo = new MyPOJO();
 myPojo.setList(new ArrayList<>());
 BeanPropertyWriter propWriter = _propertyBuilder.buildWriter(null, null, Types.constructType(MyPOJO.class), null, null, null, null, false);
 propWriter = _propertyBuilder.buildWriter(null, null, Types.constructType(MyPOJO.class), null, null, null, null, false);
 propWriter.writeValue(_config.getOutputFactory(), myPojo);
 String json = _config.getOutputFactory().getValueAsString();
 assertEquals("{}", json);
 }

 @Test
 public void testClassAnnotations() throws JsonMappingException {
 // Test class annotations
 MyPOJOAnnotated myPojoAnnotated = new MyPOJOAnnotated();
 myPojoAnnotated.setName("John Doe");
 BeanPropertyWriter propWriter = _propertyBuilder.buildWriter(null, null, Types.constructType(MyPOJOAnnotated.class), null, null, null, null, false);
 assertEquals(MyPOJOAnnotated.class.getAnnotations(), propWriter.getClassAnnotations());
 }

 @Test
 public void testTypeAnnotations() throws JsonMappingException {
 // Test type annotations
 MyPOJOTyped myPojoTyped = new MyPOJOTyped();
 myPojoTyped.setName("John Doe");
 BeanPropertyWriter propWriter = _propertyBuilder.buildWriter(null, null, Types.constructType(MyPOJOTyped.class), null, null, null, null, false);
 JsonSerializer<?> serializer = propWriter.getSerializer();
 assertEquals(MyPOJOTypedSerializer.class, serializer.getClass());
 }

 @Test
 public void testNullSerializer() throws JsonMappingException {
 // Test null serializer
 MyPOJONull myPojoNull = new MyPOJONull();
 Object serDef = _annotationIntrospector.findNullSerializer(new AnnotatedClassMember(MyPOJONull.class, "name"));
 BeanPropertyWriter propWriter = _propertyBuilder.buildWriter(null, null, Types.constructType(MyPOJONull.class), null, null, null, null, false);
 propWriter.assignNullSerializer(_config.getSerializerProvider().serializerInstance(new AnnotatedMethod(MyPOJONull.class, "getName", null), serDef));
 propWriter = _propertyBuilder.buildWriter(null, null, Types.constructType(MyPOJONull.class), null, null, null, null, false);
 propWriter.writeValue(_config.getOutputFactory(), myPojoNull);
 String json = _config.getOutputFactory().getValueAsString();
 assertEquals("null", json);
 }

 @Test
 public void testUnwrappingWriter() throws JsonMappingException {
 // Test unwrapping writer
 MyPOJOWrapper myPojoWrapper = new MyPOJOWrapper();
 myPojoWrapper.setMyPojo(new MyPOJO());
 NameTransformer unwrapper = _annotationIntrospector.findUnwrappingNameTransformer(new AnnotatedClassMember(MyPOJOWrapper.class, "myPojo"));
 BeanPropertyWriter propWriter = _propertyBuilder.buildWriter(null, null, Types.constructType(MyPOJO.class), null, null, null, null, false);
 propWriter = propWriter.unwrappingWriter(unwrapper);
 propWriter = _propertyBuilder.buildWriter(null, null, Types.constructType(MyPOJO.class), null, null, null, null, false);
 propWriter.writeValue(_config.getOutputFactory(), myPojoWrapper);
 String json = _config.getOutputFactory().getValueAsString();
 assertEquals("{\"name\":\"John Doe\"}", json);
 }

 @Test
 public void testSerializationType() throws JsonMappingException {
 // Test serialization type
 MyPOJOSub myPojoSub = new MyPOJOSub();
 myPojoSub.setName("John Doe");
 JavaType serializationType = _propertyBuilder.findSerializationType(new AnnotatedMethod(MyPOJOSub.class, "getName", null), false, Types.constructType(MyPOJOSub.class));
 assertEquals(MyPOJOSub.SubNameSerializer.class, serializationType.getRawClass().getDeclaredConstructor().newInstance().getClass());
 }

 @Test
 public void testSerializationTypeFromAnnotation() throws JsonMappingException {
 // Test serialization type from annotation
 MyPOJOWithType myPojoWithType = new MyPOJOWithType();
 myPojoWithType.setName("John Doe");
 MyPOJOTypeMySerializer mySerializer = new MyPOJOTypeMySerializer();
 JavaType serializationType = _propertyBuilder.findSerializationType(new AnnotatedField(MyPOJOWithType.class, "name"), false, Types.constructType(MyPOJOWithType.class));
 assertEquals(MyPOJOTypeMySerializer.class, serializationType.getRawClass());
 }

 @Test
 public void testSerializationTypeFromTypeOverride() throws JsonMappingException {
 // Test serialization type from type override
 MyPOJOType myPojoType = new MyPOJOType();
 myPojoType.setName("John Doe");
 JsonSerialize.Type serializationTypeAnnotation = MyPOJOType.class.getAnnotation(JsonSerialize.class);
 java.lang.reflect.Type serializationTypeOverride = serializationTypeAnnotation.using();
 JavaType javaTypeOverride = Types.constructType(serializationTypeOverride);
 JavaType serializationType = _propertyBuilder.findSerializationType(new AnnotatedField(MyPOJOType.class, "name"), false, Types.constructType(MyPOJOType.class));
 assertEquals(MyPOJOTypeSerializer.class, serializationType.getRawClass());
 }

 @Test
 public void testSerializationTypeFromTypeSerialization() throws JsonMappingException {
 // Test serialization type from type serialization
 MyPOJOType myPojoType = new MyPOJOType();
 myPojoType.setName("John Doe");
 JsonSerialize mySerializer = new JsonSerialize();
 java.lang.reflect.Type type = new TypeReference<>() {}.getType();
 MyPOJOTypeSerializer myPOJOTypeSerializer = new MyPOJOTypeSerializer();
 MySerializerProvider mySerializerProvider = new MySerializerProvider();
 mySerializerProvider.registerSerializer(MyPOJOType.class, myPOJOTypeSerializer);
 SerializerProvider serializerProvider = new SerializerProvider();
 serializerProvider.setSerializerProvider(mySerializerProvider);
 TypeSerializer typeSerializer = BeanSerializerFactory.instance.findTypeSerializer(serializerProvider, Types.constructType(MyPOJOType.class), type, null);
 JavaType serializationType = _propertyBuilder.findSerializationType(new AnnotatedField(MyPOJOType.class, "name"), false, Types.constructType(MyPOJOType.class));
 assertEquals(MyPOJOTypeSerializer.class, serializationType.getRawClass());
 }
}