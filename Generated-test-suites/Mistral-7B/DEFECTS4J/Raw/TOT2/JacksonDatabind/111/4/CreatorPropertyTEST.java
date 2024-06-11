import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;
import com.fasterxml.jackson.databind.util.TypeUtil;

public class CreatorPropertyTest extends TestBase {

 private ObjectMapper mapper;
 private CreatorProperty property;

 @Before
 public void setup() throws Exception {
 mapper = new ObjectMapper();
 property = new CreatorProperty(PropertyName.construct("testProperty"),
 TypeFactory.defaultInstance.constructType(String.class), null, null, null, null, 0, null, null);
 }

 @Test
 public void testConstructor() throws Exception {
 Object value = "testValue";
 property.setFallbackSetter(new CreatorProperty(property, PropertyName.construct("testFallbackProperty")));
 property.inject(new TestContext(), new Object());
 property.set(new Object(), value);
 assertEquals("testValue", mapper.readValue(mapper.writeValueAsString(new Object()), String.class));
 }

 @Test
 public void testInjectValue() throws Exception {
 Object injectValue = new Object();
 property.markAsIgnorable();
 property.inject(new TestContext(), new Object());
 assertFalse(property.isIgnorable());
 assertEquals(injectValue, property.get(new Object()));
 }

 @Test
 public void testNullValue() throws Exception {
 property.inject(new TestContext(), new Object());
 assertNull(property.get(new Object()));
 }

 @Test
 public void testSetValue() throws Exception {
 Object value = "testValue";
 property.set(new Object(), value);
 assertEquals(value, property.get(new Object()));
 }

 @Test
 public void testSetValueDeserializer() throws Exception {
 String json = "{\"testProperty\":\"testValue\"}";
 JavaType valueType = TypeFactory.defaultInstance.constructType(String.class);
 property.setValueDeserializer(new StubJsonDeserializer());
 Object result = mapper.readValue(json, mapper.getTypeFactory().constructType(new PropertyType()));
 assertEquals(valueType.getValueType(), result.getClass());
 assertEquals("testValue", property.get(result));
 }

 @Test
 public void testIgnoreProperty() throws Exception {
 property.markAsIgnorable();
 Object value = mapper.readValue(mapper.writeValueAsString(new Object()), String.class);
 assertNull(property.get(value));
 }

 @Test(expected = IOException.class)
 public void testMissingSetter() throws Exception {
 property.markAsIgnorable();
 property.deserializeAndSet(JsonParserFactory.instance.getEmptyParser(), context, new Object());
 }

 @Test
 public void testAnnotatedParameter() throws Exception {
 property = new CreatorProperty(PropertyName.construct("testProperty"),
 TypeFactory.defaultInstance.constructType(String.class), null, null, null,
 new AnnotatedParameter(PropertyName.construct("testParameter"), String.class, JsonProperty.Value.AS_IS), 0, null, null);
 property.inject(new TestContext(), new Object());
 Object value = "testValue";
 property.set(new Object(), value);
 assertEquals(value, mapper.readValue(mapper.writeValueAsString(new Object()), String.class));
 }

 @Test
 public void testTypeDeserializer() throws Exception {
 property = new CreatorProperty(PropertyName.construct("testProperty"),
 TypeFactory.defaultInstance.constructType(String.class), null, null, null, null, 0, null,
 new TypeDeserializer() {
 @Override public Object deserializeTypedFromAny(DeserializationContext ctxt, JsonParser p,
 TypeDeserializer nullValueProvider) throws IOException {
 return "testValue";
 }

 @Override public TypeDeserializer findContentDeserializer(DeserializationContext ctxt, BeanDescription beanDesc, TypeDeserializer valueDeserializer, JavaType type) throws IOException {
 return null;
 }
 });
 Object result = mapper.readValue(mapper.writeValueAsString(new Object()), property.getType());
 assertEquals("testValue", property.get(result));
 }

 @Test
 public void testValueDeserializer() throws Exception {
 property = new CreatorProperty(PropertyName.construct("testProperty"),
 TypeFactory.defaultInstance.constructType(String.class), null, null, null, null, 0, null, null);
 property.setValueDeserializer(new StubJsonDeserializer());
 Object result = mapper.readValue(mapper.writeValueAsString(new Object()), mapper.getTypeFactory().constructType(new PropertyType()));
 assertEquals("testValue", property.get(result));
 }

 @Test
 public void testSetterFixAccess() throws Exception {
 property.markAsIgnorable();
 property.fixAccess(context);
 property.set(new Object(), "testValue");
 assertEquals("testValue", mapper.readValue(mapper.writeValueAsString(new Object()), String.class));
 }
}

class StubJsonDeserializer extends JsonDeserializer<String> {
 @Override public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return "testValue";
 }
}

class PropertyType {
 @JsonDeserialize(using = CreatorProperty.class)
 private CreatorProperty testProperty;

 public CreatorProperty getTestProperty() {
 return testProperty;
 }
}

class TestContext extends DeserializationContext {
 @Override public Object findInjectableValue(Object id, BeanProperty property, Object bean) throws JsonMappingException {
 return null;
 }

 @Override public void reportBadDefinition(Class<?> valueType, String msg) throws JsonMappingException {
 fail(msg);
 }

 @Override public JsonParser currentLocationForException(Exception e) {
 return null;
 }

 @Override public void reportError(String msg) throws IOException {
 fail(msg);
 }

 @Override public void reportWarning(String msg) {
 // do nothing
 }

 @Override public void reportWarning(String msg, Object source) {
 // do nothing
 }
}