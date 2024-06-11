import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableAnyProperty;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SettableAnyPropertyTest {

 private SettableAnyProperty _settableAnyProperty;

 @Before
 public void setup() {
 JsonParser parser = new JsonParser();
 DeserializationConfig config = new DeserializationConfig();
 ObjectMapper mapper = new ObjectMapper(config);
 AnnotatedField field = new AnnotatedField(Object.class, "field");
 JavaType javaType = ObjectType.getTypeFactory().constructType(Object.class);
 AnnotatedMethod setter = new AnnotatedMethod(Object.class, "set", javaType.getRawClass(), Access.PUBLIC);
 SettableAnyProperty settableAnyProperty = new SettableAnyProperty(new BeanProperty.StandardGetter("field", field), setter, javaType, null, null, null);
 _settableAnyProperty = settableAnyProperty;
 }

 @Test
 public void testSettableAnyPropertyConstructor() {
 // Test the default constructor
 BeanProperty property = new BeanProperty.StandardGetter("property", new AnnotatedField(Object.class, "property"));
 AnnotatedMember setter = new AnnotatedMethod(Object.class, "set", java.lang.Object.class, Access.PUBLIC);
 JavaType type = ObjectType.getTypeFactory().constructType(Object.class);
 KeyDeserializer keyDeserializer = null;
 JsonDeserializer<Object> valueDeserializer = null;
 TypeDeserializer valueTypeDeserializer = null;
 SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
 assertNotNull(settableAnyProperty);
 assertEquals(property, settableAnyProperty.getProperty());
 assertEquals(setter, settableAnyProperty._setter);
 assertEquals(type, settableAnyProperty._type);
 assertNull(settableAnyProperty._valueDeserializer);
 assertNull(settableAnyProperty._valueTypeDeserializer);
 assertNull(settableAnyProperty._keyDeserializer);
 }

 @Test
 public void testSettableAnyPropertyWithValueDeserializer() {
 // Test the constructor with valueDeserializer
 JsonDeserializer<Object> valueDeserializer = new StubJsonDeserializer<>();
 SettableAnyProperty settableAnyProperty = new SettableAnyProperty(_property, _setter, _type, _keyDeserializer, valueDeserializer, _valueTypeDeserializer);
 assertEquals(valueDeserializer, settableAnyProperty._valueDeserializer);
 }

 @Test
 public void testWithValueDeserializer() {
 // Test the withValueDeserializer method
 JsonDeserializer<Object> valueDeserializer = new StubJsonDeserializer<>();
 SettableAnyProperty settableAnyProperty = new SettableAnyProperty(_property, _setter, _type, _keyDeserializer, null, _valueTypeDeserializer);
 SettableAnyProperty newSettableAnyProperty = settableAnyProperty.withValueDeserializer(valueDeserializer);
 assertEquals(valueDeserializer, newSettableAnyProperty._valueDeserializer);
 assertNotSame(settableAnyProperty, newSettableAnyProperty);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFixAccessWithDisabled() throws IOException {
 _settableAnyProperty.fixAccess(new DeserializationConfig(new ObjectMapper().getDefaultConfig()));
 }

 @Test
 public void testSetterIsFieldIsTrue() throws IOException {
 Object instance = new Object();
 Map<Object,Object> map = (Map<Object,Object>) _settableAnyProperty.getSetter().getValue(instance);
 assertNotNull(map);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetterIsFieldIsNull() throws IOException {
 Object instance = new Object();
 Map<Object,Object> map = (Map<Object,Object>) _settableAnyProperty.getSetter().getValue(null);
 fail("Should throw IllegalArgumentException for null instance");
 }

 @Test
 public void testDeserializeAndSetNullValue() throws IOException {
 JsonParser p = new JsonParser(new StringReader("{ \"property\": null }"));
 _settableAnyProperty.deserializeAndSet(p, new TestContext(), new Object(), "property");
 assertNull(_settableAnyProperty.getValueDeserializer().getValue(p, new TestContext()));
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeAndSetValueDeserializerNull() throws IOException {
 JsonParser p = new JsonParser(new StringReader("{ \"property\": { \"foo\": 1 } }"));
 _settableAnyProperty.deserializeAndSet(p, new TestContext(), new Object(), "property");
 fail("Should throw JsonMappingException for null valueDeserializer");
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeAndSetValueDeserializerTypeMismatch() throws IOException {
 JsonParser p = new JsonParser(new StringReader("{ \"property\": 1 }"));
 _settableAnyProperty.deserializeAndSet(p, new TestContext(), new Object(), "property");
 fail("Should throw JsonMappingException for valueDeserializer type mismatch");
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 JsonParser p = new JsonParser(new StringReader("{ \"property\": { \"foo\": 1 } }"));
 _settableAnyProperty.deserializeAndSet(p, new TestContext(), new Object(), "property");
 assertEquals(1, _settableAnyProperty.getValueDeserializer().getValue(p, new TestContext()));
 }

 @Test
 public void testSet() throws IOException {
 Object instance = new Object();
 String propName = "property";
 Object value = new Object();
 _settableAnyProperty.set(instance, propName, value);
 assertEquals(value, _settableAnyProperty.getSetter().getValue(instance));
 }

 @Test(expected = IOException.class)
 public void testSetIOException() throws IOException {
 Object instance = new Object();
 String propName = "property";
 Object value = new Object();
 _settableAnyProperty.set(instance, propName, value);
 throw new IOException("Test IOException");
 }

 @Test
 public void testThrowAsIOE() throws IOException {
 Object instance = new Object();
 String propName = "property";
 Object value = new Object();
 _settableAnyProperty._throwAsIOE(new IllegalArgumentException("Test IllegalArgumentException"), propName, value);
 fail("Should throw JsonMappingException for IllegalArgumentException");
 }

 @Test
 public void testToString() {
 assertEquals("[any property on class com.fasterxml.jackson.databind.deser.SettableAnyProperty]", _settableAnyProperty.toString());
 }
}

class StubJsonDeserializer<T> implements JsonDeserializer<T> {
 @Override
 public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 throw new UnsupportedOperationException();
 }
}

class TestContext extends JsonDeserializer.Context {
}