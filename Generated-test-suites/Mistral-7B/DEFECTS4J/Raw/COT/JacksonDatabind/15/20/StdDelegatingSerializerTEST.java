import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdDelegatingSerializerTest {

 private static final ObjectNode STANDARD_JSON = ObjectMapper.findDefaultInstance()
         .createObjectNode();

 private StdDelegatingSerializer<Object> standardSerializer;

 @Before
 public void setup() throws JsonProcessingException {
 Converter<Object, String> converter = new SimpleStringConverter();
 standardSerializer = new StdDelegatingSerializer<Object>(converter);
 }

 @Test
 public void testDefaultConstructor() {
 // Testing default constructor
 assertNotNull(standardSerializer);
 assertEquals(Object.class, standardSerializer.getClass());
 assertNull(standardSerializer._delegateType);
 assertNull(standardSerializer._delegateSerializer);
 }

 @Test
 public void testConstructorWithConverter() {
 // Testing constructor with Converter
 Object converterInput = new Object();
 String delegateValue = standardSerializer.getConverter().convert(converterInput);
 assertNotNull(delegateValue);
 assertNull(standardSerializer._delegateType);
 assertNull(standardSerializer._delegateSerializer);
 }

 @Test
 public void testConstructorWithClass() {
 // Testing constructor with Class
 Class<String> stringClass = String.class;
 standardSerializer = new StdDelegatingSerializer<Object>(String.class, standardSerializer.getConverter());
 assertEquals(String.class, standardSerializer._delegateType);
 assertNull(standardSerializer._delegateSerializer);
 }

 @Test
 public void testConstructorWithConverterDelegateTypeAndSerializer() {
 // Testing constructor with Converter, delegateType, and serializer
 JsonSerializer<String> delegateSerializer = new StdSerializer<String>(String.class) {};
 standardSerializer = new StdDelegatingSerializer<Object>(standardSerializer.getConverter(),
 String.class, delegateSerializer);
 assertEquals(String.class, standardSerializer._delegateType);
 assertEquals(delegateSerializer, standardSerializer._delegateSerializer);
 }

 @Test
 public void testWithDelegate() {
 // Testing withDelegate method
 Converter<Object, Integer> converter = new SimpleIntegerConverter();
 JavaType delegateType = ObjectMapper.findDefaultInstance().getTypeFactory()
 .constructType(Integer.class);
 JsonSerializer<Integer> delegateSerializer = new StdSerializer<Integer>(Integer.class) {};
 StdDelegatingSerializer<Object> newSerializer = standardSerializer.withDelegate(converter, delegateType, delegateSerializer);
 assertEquals(newSerializer.getClass(), StdDelegatingSerializer.class);
 assertEquals(delegateType, newSerializer._delegateType);
 assertEquals(delegateSerializer, newSerializer._delegateSerializer);
 }

 @Test
 public void testResolve() throws JsonProcessingException {
 // Testing resolve method
 Object input = new Object();
 ObjectMapper mapper = ObjectMapper.findDefaultInstance();
 ObjectNode output = mapper.writeValueAsObject(input);
 standardSerializer.resolve(mapper.getSerializerProvider());
 JsonSerializer<Object> delegateSerializer = mapper.findValueSerializer(String.class);
 ObjectMapper newMapper = ObjectMapper.findDefaultInstance();
 newMapper.registerSerializer(String.class, delegateSerializer);
 ObjectNode newOutput = newMapper.writeValueAsObject(input);
 assertEquals(output, newOutput);
 }

 @Test
 public void testCreateContextual() throws JsonMappingException {
 // Testing createContextual method
 ObjectMapper mapper = ObjectMapper.findDefaultInstance();
 Object input = new Object();
 JsonSerializer<Object> delegateSerializer = mapper.findValueSerializer(String.class);
 JsonSerializer<Object> serializer = standardSerializer.createContextual(mapper.getSerializerProvider(), null);
 assertEquals(delegateSerializer, serializer);
 }

 @Test
 public void testSerialize() throws JsonProcessingException {
 // Testing serialize method
 Object input = "test";
 ObjectMapper mapper = ObjectMapper.findDefaultInstance();
 standardSerializer.serialize(input, STANDARD_JSON, mapper.getSerializerProvider());
 assertEquals("\"test\"", STANDARD_JSON.getTextValue(""));
 }

 @Test
 public void testSerializeWithType() throws JsonProcessingException {
 // Testing serializeWithType method
 Object input = "test";
 ObjectMapper mapper = ObjectMapper.findDefaultInstance();
 TypeSerializer typeSerializer = mapper.getTypeSerializer();
 standardSerializer.serializeWithType(input, STANDARD_JSON, mapper.getSerializerProvider(), typeSerializer);
 assertEquals("\"test\"", STANDARD_JSON.getTextValue(""));
 }

 @Test(expected = IllegalStateException.class)
 public void testIllegalStateException() {
 // Testing IllegalStateException when subclass does not override 'withDelegate' method
 StdDelegatingSerializer<String> subclass = new StdDelegatingSerializer<String>() {};
 }

 @Test(expected = JsonMappingException.class)
 public void testJsonMappingException() throws JsonProcessingException {
 // Testing JsonMappingException when no delegate serializer found
 ObjectMapper mapper = ObjectMapper.findDefaultInstance();
 ObjectMapper newMapper = ObjectMapper.findDefaultInstance();
 Object input = new Object();
 standardSerializer = new StdDelegatingSerializer<Object>(standardSerializer.getConverter(),
 Object.class, null);
 ObjectNode output = mapper.writeValueAsObject(input);
 newMapper.registerSerializer(String.class, null);
 ObjectNode newOutput = newMapper.writeValueAsObject(input);
 assertNotEquals(output, newOutput);
 }

 @Test
 public void testGetConverter() {
 // Testing getConverter method
 Converter<Object, String> converter = standardSerializer.getConverter();
 assertNotNull(converter);
 }

 @Test
 public void testGetDelegatee() {
 // Testing getDelegatee method
 JsonSerializer<Object> delegatee = standardSerializer.getDelegatee();
 assertNotNull(delegatee);
 }

 // Edge Case Tests

 @Test
 public void testNullDelegateType() throws JsonProcessingException {
 // Testing NullDelegateType
 standardSerializer = new StdDelegatingSerializer<Object>(standardSerializer.getConverter(), null, null);
 ObjectMapper mapper = ObjectMapper.findDefaultInstance();
 standardSerializer.serialize(null, STANDARD_JSON, mapper.getSerializerProvider());
 assertEquals("null", STANDARD_JSON.getTextValue(""));
 }

 @Test
 public void testNullDelegateSerializer() throws JsonProcessingException {
 // Testing NullDelegateSerializer
 standardSerializer = new StdDelegatingSerializer<Object>(standardSerializer.getConverter(),
 Object.class, null);
 ObjectMapper mapper = ObjectMapper.findDefaultInstance();
 standardSerializer.serialize(null, STANDARD_JSON, mapper.getSerializerProvider());
 assertEquals("null", STANDARD_JSON.getTextValue(""));
 }

 @Test
 public void testNullInput() throws JsonProcessingException {
 // Testing NullInput
 standardSerializer.serialize(null, STANDARD_JSON, ObjectMapper.findDefaultInstance().getSerializerProvider());
 assertEquals("null", STANDARD_JSON.getTextValue(""));
 }

 @Test
 public void testNullConverter() {
 // Testing NullConverter
 Converter<Object, ?> nullConverter = null;
 try {
 standardSerializer = new StdDelegatingSerializer<Object>(nullConverter);
 } catch (IllegalArgumentException expected) {
 // Expected exception
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidConverter() {
 // Testing InvalidConverter
 Converter<Object, Integer> invalidConverter = new Converter<Object, Integer>() {
 @Override
 public Integer convert(Object value) {
 throw new IllegalArgumentException();
 }
 };
 try {
 standardSerializer = new StdDelegatingSerializer<Object>(invalidConverter);
 } catch (IllegalArgumentException expected) {
 // Expected exception
 }
 }

 @Test
 public void testInvalidDelegateType() {
 // Testing InvalidDelegateType
 try {
 standardSerializer = new StdDelegatingSerializer<Object>(standardSerializer.getConverter(),
 new TypeReference<Integer>() {}, null);
 } catch (IllegalArgumentException expected) {
 // Expected exception
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidDelegateSerializer() {
 // Testing InvalidDelegateSerializer
 try {
 standardSerializer = new StdDelegatingSerializer<Object>(standardSerializer.getConverter(),
 Object.class, new StdSerializer<JsonNode>(JsonNode.class) {});
 } catch (IllegalArgumentException expected) {
 // Expected exception
 }
 }

 @Test
 public void testInvalidTypeSerializer() {
 // Testing InvalidTypeSerializer
 TypeSerializer invalidTypeSerializer = new TypeSerializer() {};
 try {
 standardSerializer.serializeWithType(null, STANDARD_JSON, ObjectMapper.findDefaultInstance().getSerializerProvider(), invalidTypeSerializer);
 } catch (IllegalArgumentException expected) {
 // Expected exception
 }
 }
}