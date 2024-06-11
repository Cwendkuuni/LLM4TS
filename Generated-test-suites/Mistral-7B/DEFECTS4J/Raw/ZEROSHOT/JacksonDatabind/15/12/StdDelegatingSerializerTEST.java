import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdDelegatingSerializerTest {

 private StdDelegatingSerializer serializer;
 private JsonSerializer<String> delegateSerializer;
 private Converter<Object, String> converter;

 @Before
 public void setup() {
 converter = new SimpleStringConverter();
 delegateSerializer = new StringSerializer();
 serializer = new StdDelegatingSerializer(converter, String.class, delegateSerializer);
 }

 @Test
 public void testSerializeNull() throws JsonProcessingException {
 JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), null);
 serializer.serialize(null, gen, new ObjectMapper().getSerializerProvider());
 assertEquals("null", gen.toString());
 }

 @Test
 public void testSerializeObject() throws JsonProcessingException {
 JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), null);
 Object obj = new Object();
 serializer.serialize(obj, gen, new ObjectMapper().getSerializerProvider());
 assertEquals("{}", gen.toString());
 }

 @Test
 public void testSerializeString() throws JsonProcessingException {
 JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), null);
 String str = "test";
 serializer.serialize(str, gen, new ObjectMapper().getSerializerProvider());
 assertEquals("\"test\"", gen.toString());
 }

 @Test
 public void testGetSchema() throws JsonMappingException {
 JsonSchema schema = serializer.getSchema(new ObjectMapper(), String.class);
 assertNotNull(schema);
 }

 @Test
 public void testSerializeWithType() throws JsonProcessingException {
 ByteArrayOutputStream output = new ByteArrayOutputStream();
 JsonGenerator gen = new JsonFactory().createGenerator(output, null);
 Object obj = new Object();
 TypeSerializer typeSerializer = new BasicTypeSerializerProvider().findTypeSerializer(Object.class, null);
 serializer.serializeWithType(obj, gen, new ObjectMapper().getSerializerProvider(), typeSerializer);
 assertEquals("null", output.toString());
 }

 @Test(expected = IllegalStateException.class)
 public void testInvalidSelfDelegation() {
 // This test case checks that the serializer does not allow self-delegation
 StdDelegatingSerializer selfDelegatingSerializer = new StdDelegatingSerializer(converter, String.class, serializer);
 }

 @Test
 public void testIsEmpty() {
 assertFalse(serializer.isEmpty(new ObjectMapper().getSerializerProvider(), null));
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(new StandardSerializers.Instance().findValueSerializer(Object.class));
 serializer.acceptJsonFormatVisitor(visitor, null);
 }

 @Test
 public void testConvertValue() {
 String result = serializer.convertValue("test");
 assertEquals("test", result);
 }
}

class SimpleStringConverter implements Converter<Object, String> {
 @Override
 public String convert(Object value) {
 return value.toString();
 }
}

class StringSerializer extends StdSerializer<String> {
 @Override
 public void serialize(String value, JsonGenerator gen, SerializerProvider provider) throws IOException {
 gen.writeString(value);
 }
}