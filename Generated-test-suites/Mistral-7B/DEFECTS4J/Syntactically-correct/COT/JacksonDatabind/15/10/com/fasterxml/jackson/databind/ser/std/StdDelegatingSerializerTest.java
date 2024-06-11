import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonschema.SchemaValidationFailure;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Type;

public class StdDelegatingSerializerTest {

 private StdDelegatingSerializer<Object> serializer;
 private ObjectMapper mapper;

 @Before
 public void setUp() throws Exception {
 mapper = new ObjectMapper();
 serializer = new StdDelegatingSerializer<Object>(mapper.getFactory().getConverter(String.class, Integer.class));
 }

 @Test
 public void testConstructorWithConverter() throws Exception {
 Object value = new Object();
 Object delegateValue = serializer.getConverter().convert(value);
 assertTrue(delegateValue instanceof String);
 }

 @Test
 public void testConstructorWithClass() throws Exception {
 Class<Integer> intClass = Integer.class;
 serializer = new StdDelegatingSerializer<Object>(intClass, mapper.getFactory().getConverter(String.class, Integer.class));
 Object value = new Object();
 Object delegateValue = serializer.getConverter().convert(value);
 assertTrue(delegateValue instanceof Integer);
 }

 @Test
 public void testConstructorWithConverterDelegateTypeAndSerializer() throws Exception {
 String delegateValue = "test";
 JsonSerializer<Object> delegateSerializer = mapper.findValueSerializer(String.class);
 serializer = new StdDelegatingSerializer<Object>(mapper.getFactory().getConverter(String.class, Integer.class),
 mapper.constructType(String.class), delegateSerializer);
 Object value = new Object();
 Object serialzedValue = serializer.serializeValue(mapper.getSerializerProvider(), value);
 assertEquals(delegateValue, serialzedValue);
 }

 @Test
 public void testResolve() throws Exception {
 Object value = new Object();
 serializer.resolve(mapper.getSerializerProvider());
 JsonSerializer<Object> delegateSerializer = mapper.findValueSerializer(String.class);
 assertEquals(delegateSerializer, serializer.getDelegatee());
 }

 @Test
 public void testCreateContextual() throws Exception {
 Object value = new Object();
 JsonSerializer<Object> delegateSerializer = mapper.findValueSerializer(String.class);
 assertEquals(delegateSerializer, serializer.createContextual(mapper.getSerializerProvider(), null));
 }

 @Test
 public void testSerialize() throws Exception {
 Object value = new Object();
 String expected = "test";
 Object delegateValue = serializer.getConverter().convert(value);
 String actual = serializer.serializeValue(mapper.getSerializerProvider(), value).toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializeWithType() throws Exception {
 Object value = new Object();
 String expected = "test";
 JsonSerializer<String> delegateSerializer = mapper.findValueSerializer(String.class);
 TypeSerializer typeSerializer = mapper.getTypeSerializer();
 Object actual = serializer.serializeValue(mapper.getSerializerProvider(), value, typeSerializer);
 assertEquals(expected, actual.toString());
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeWithNullValue() throws Exception {
 Object value = null;
 serializer.serializeValue(mapper.getSerializerProvider(), value);
 fail("Expected JsonMappingException when serializing null value");
 }

 @Test
 public void testGetSchema() throws Exception {
 assertNull(serializer.getSchema(mapper.getSerializerProvider(), Object.class));
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws Exception {
 fail("Expected JsonMappingException when accepting JsonFormatVisitor");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnsupportedDynamicSerialization() throws Exception {
 serializer = new StdDelegatingSerializer<Object>(mapper.getFactory().getConverter(Object.class, String.class));
 Object value = new Object();
 try {
 serializer.serializeValue(mapper.getSerializerProvider(), value);
 fail("Expected IllegalArgumentException when serializing unsupported dynamic value");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializationWithInvalidDelegateType() throws Exception {
 serializer = new StdDelegatingSerializer<Object>(mapper.getFactory().getConverter(String.class, Integer.class),
 mapper.constructType(String.class), mapper.findValueSerializer(String.class));
 Object value = new Object();
 try {
 serializer.serializeValue(mapper.getSerializerProvider(), value);
 fail("Expected JsonMappingException when serializing to incompatible delegate type");
 } catch (JsonMappingException e) {
 // pass
 }
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializationWithInvalidDelegateType() throws Exception {
 String delegateValue = "test";
 JsonDeserializer<Object> deserializer = mapper.findValueDeserializer(String.class);
 serializer = new StdDelegatingSerializer<Object>(deserializer, mapper.constructType(Integer.class), null);
 String json = "{\"test\": \"test\"}";
 try {
 Object deserializedValue = mapper.readValue(json, Object.class, serializer);
 fail("Expected JsonMappingException when deserializing to incompatible delegate type");
 } catch (JsonMappingException e) {
 // pass
 }
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializationWithInvalidConverter() throws Exception {
 String json = "{\"test\": 1}";
 serializer = new StdDelegatingSerializer<Object>(mapper.getFactory().getConverter(String.class, String.class),
 mapper.constructType(Integer.class), null);
 try {
 Object deserializedValue = mapper.readValue(json, Object.class, serializer);
 fail("Expected JsonMappingException when deserializing with invalid converter");
 } catch (JsonMappingException e) {
 // pass
 }
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializationWithNullDelegateValue() throws Exception {
 String json = "{\"test\": null}";
 serializer = new StdDelegatingSerializer<Object>(mapper.getFactory().getConverter(String.class, Integer.class),
 mapper.constructType(Integer.class), null);
 try {
 Object deserializedValue = mapper.readValue(json, Object.class, serializer);
 fail("Expected JsonMappingException when deserializing with null delegate value");
 } catch (JsonMappingException e) {
 // pass
 }
 }

 @Test(expected = JsonGenerationException.class)
 public void testSerializationWithExceptionInDelegateSerializer() throws Exception {
 String delegateValue = "test";
 JsonSerializer<Object> exceptionSerializer = new JsonSerializer<Object>() {
 @Override
 public void serialize(Object value, JsonGenerator gen, SerializerProvider serializerProvider)
 throws IOException {
 throw new JsonGenerationException("Test exception");
 }
 };
 serializer = new StdDelegatingSerializer<Object>(mapper.getFactory().getConverter(String.class, Integer.class),
 mapper.constructType(String.class), exceptionSerializer);
 Object value = new Object();
 try {
 serializer.serializeValue(mapper.getSerializerProvider(), value);
 fail("Expected JsonGenerationException when serializing with exception in delegate serializer");
 } catch (JsonGenerationException e) {
 // pass
 }
 }

 @Test(expected = SchemaValidationFailure.class)
 public void testSchemaValidation() throws Exception {
 ObjectMapper schemaMapper = new ObjectMapper();
 ObjectNode schema = schemaMapper.readTree("{\"title\": \"TestSchema\", \"type\": \"object\"}");
 serializer = new StdDelegatingSerializer<Object>(mapper.getFactory().getConverter(String.class, Integer.class),
 mapper.constructType(String.class), null);
 try {
 serializer.validateSchema(mapper.getSerializerProvider(), schema);
 fail("Expected SchemaValidationFailure when validating with invalid schema");
 } catch (SchemaValidationFailure e) {
 // pass
 }
 }
}