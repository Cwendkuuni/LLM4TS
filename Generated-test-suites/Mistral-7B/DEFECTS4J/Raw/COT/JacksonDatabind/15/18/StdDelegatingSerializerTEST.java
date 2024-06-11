import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdDelegatingSerializerTest {

 private StdDelegatingSerializer<Object> serializer;
 private ObjectMapper mapper;

 @Before
 public void setup() {
 serializer = new StdDelegatingSerializer<Object>(new IdentityConverter<>()) {
 @Override
 public JsonSerializer<?> getDelegatee() {
 return mapper.findValueSerializer(String.class);
 }
 };
 mapper = new ObjectMapper();
 }

 @Test
 public void testEmptySerialization() throws JsonProcessingException {
 Object testValue = null;
 JsonGenerator gen = mapper.getFactory().createGenerator();
 serializer.serialize(testValue, gen, mapper.getSerializerProvider());
 mapper.writeValueAsString(gen.getOutputStream());
 assertTrue(mapper.readTree(mapper.readValueAsString(gen.getOutputString())).isEmpty());
 }

 @Test
 public void testSerializationWithValue() throws JsonProcessingException {
 Object testValue = "Test Value";
 String expectedResult = "\"Test Value\"";
 JsonGenerator gen = mapper.getFactory().createGenerator();
 serializer.serialize(testValue, gen, mapper.getSerializerProvider());
 assertEquals(expectedResult, mapper.writeValueAsString(gen.getOutputString()));
 }

 @Test(expected = IllegalStateException.class)
 public void testSubClassMustOverrideWithDelegateMethod() {
 class TestSubClass extends StdDelegatingSerializer<Object> {
 @Override
 protected StdDelegatingSerializer<Object> withDelegate(Converter<Object,?> converter,
 JavaType delegateType, JsonSerializer<Object> delegateSerializer) {
 // Should not reach this point as this class must override 'withDelegate' method
 }
 }
 }

 @Test
 public void testSerializationWithType() throws JsonProcessingException {
 Object testValue = new Object() {};
 String expectedResult = "null";
 JsonGenerator gen = mapper.getFactory().createGenerator();
 TypeSerializer typeSerializer = mapper.getTypeSerializer();
 serializer.serializeWithType(testValue, gen, mapper.getSerializerProvider(), typeSerializer);
 assertEquals(expectedResult, mapper.writeValueAsString(gen.getOutputString()));
 }

 @Test
 public void testGetConverter() {
 assertEquals(new IdentityConverter<>(), serializer.getConverter());
 }

 @Test(expected = JsonMappingException.class)
 public void testNullDelegateSerializer() {
 serializer = new StdDelegatingSerializer<Object>(new IdentityConverter<>()) {
 @SuppressWarnings("unchecked")
 @Override
 public <T> StdDelegatingSerializer<T> withDelegate(Converter<T,?> converter,
 JavaType delegateType, JsonSerializer<T> delegateSerializer) {
 _delegateSerializer = null;
 return this;
 }
 };
 Object testValue = new Object();
 JsonGenerator gen = mapper.getFactory().createGenerator();
 try {
 serializer.serialize(testValue, gen, mapper.getSerializerProvider());
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testInvalidDelegateType() throws JsonProcessingException {
 serializer = new StdDelegatingSerializer<Object>(new IdentityConverter<>()) {
 @SuppressWarnings("unchecked")
 @Override
 public <T> StdDelegatingSerializer<T> withDelegate(Converter<T,?> converter,
 JavaType delegateType, JsonSerializer<T> delegateSerializer) {
 _delegateSerializer = new StdDelegatingSerializer<String>(null, null, null) {
 @Override
 public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
 fail("Should not be called");
 }
 };
 _delegateType = JavaType.construct(int.class);
 }
 };
 Object testValue = new Object();
 JsonGenerator gen = mapper.getFactory().createGenerator();
 serializer.serialize(testValue, gen, mapper.getSerializerProvider());
 // Verify that the delegate serializer was not called
 assertTrue(mapper.readTree(mapper.readValueAsString(gen.getOutputString())).isEmpty());
 }

}