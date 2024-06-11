import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeySerializerTest {

 private static final Object NULL_VALUE = null;
 private static final Date DATE_VALUE = new Date();
 private static final String STRING_VALUE = "testString";
 private static final JavaType STRING_TYPE = new JavaType(String.class);

 @Test
 public void testSerialize_NullValue() throws JsonProcessingException {
 StdKeySerializer serializer = new StdKeySerializer();
 JsonGenerator gen = createMockJsonGenerator();
 SerializerProvider provider = createMockSerializerProvider();
 serializer.serialize(NULL_VALUE, gen, provider);
 assertEquals("null", gen.getOutputBuffer().toString());
 }

 @Test
 public void testSerialize_DateValue() throws JsonProcessingException {
 StdKeySerializer serializer = new StdKeySerializer();
 JsonGenerator gen = createMockJsonGenerator();
 SerializerProvider provider = createMockSerializerProvider();
 serializer.serialize(DATE_VALUE, gen, provider);
 provider.defaultSerializeDateKey(DATE_VALUE, gen);
 }

 @Test
 public void testSerialize_StringValue() throws JsonProcessingException {
 StdKeySerializer serializer = new StdKeySerializer();
 JsonGenerator gen = createMockJsonGenerator();
 SerializerProvider provider = createMockSerializerProvider();
 serializer.serialize(STRING_VALUE, gen, provider);
 assertEquals(STRING_VALUE, gen.getOutputBuffer().toString());
 }

 @Test(expected = IOException.class)
 public void testSerialize_ExceptionWhenValueIsNotDateOrString() throws JsonProcessingException {
 StdKeySerializer serializer = new StdKeySerializer();
 JsonGenerator gen = createMockJsonGenerator();
 SerializerProvider provider = createMockSerializerProvider();
 serializer.serialize(new Object() { /* some other type */ }, gen, provider);
 }

 @Test
 public void testGetSchema() throws JsonMappingException {
 StdKeySerializer serializer = new StdKeySerializer();
 JsonSchemaProvider schemaProvider = createMockJsonSchemaProvider();
 assertEquals("string", serializer.getSchema(new SerializerProvider(), StdKeySerializer.class).getDescription().get(0));
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 StdKeySerializer serializer = new StdKeySerializer();
 JsonFormatVisitorWrapper visitor = createMockJsonFormatVisitor();
 serializer.acceptJsonFormatVisitor(visitor, STRING_TYPE);
 }

 @Test(expected = JsonMappingException.class)
 public void testAcceptJsonFormatVisitor_ExceptionWhenTypeHintIsNotStringType() throws JsonMappingException {
 StdKeySerializer serializer = new StdKeySerializer();
 JsonFormatVisitorWrapper visitor = createMockJsonFormatVisitor();
 serializer.acceptJsonFormatVisitor(visitor, new JavaType(Integer.class));
 }
}