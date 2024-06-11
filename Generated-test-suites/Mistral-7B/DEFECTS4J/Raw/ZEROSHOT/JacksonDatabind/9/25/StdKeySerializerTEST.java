import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializer;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class StdKeySerializerTest {

 private StdKeySerializer serializer;
 private JsonGenerator mockJsonGenerator;
 private SerializerProvider mockSerializerProvider;
 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 serializer = new StdKeySerializer();
 mockJsonGenerator = Mockito.mock(JsonGenerator.class);
 mockSerializerProvider = Mockito.mock(SerializerProvider.class);
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testSerializeDate() throws IOException, JsonProcessingException {
 Date date = new Date();
 Mockito.when(mockSerializerProvider.defaultSerializeDateKey(date, mockJsonGenerator))
 .thenCallRealMethod();
 serializer.serialize(date, mockJsonGenerator, mockSerializerProvider);
 Mockito.verify(mockJsonGenerator).writeFieldName(Mockito.anyString());
 }

 @Test
 public void testSerializeString() throws IOException, JsonProcessingException {
 String testString = "testString";
 serializer.serialize(testString, mockJsonGenerator, mockSerializerProvider);
 Mockito.verify(mockJsonGenerator).writeFieldName(testString);
 }

 @Test
 public void testGetSchema() throws JsonMappingException {
 JsonNode expectedSchema = objectMapper.valueToTree(new Type[] { String.class });
 JsonNode resultSchema = serializer.getSchema(mockSerializerProvider, JavaType.getCollectionType(Map.class, String.class));
 assertThat(resultSchema, is(expectedSchema));
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 JsonFormatVisitorWrapper mockJsonFormatVisitorWrapper = Mockito.mock(JsonFormatVisitorWrapper.class);
 serializer.acceptJsonFormatVisitor(mockJsonFormatVisitorWrapper, TypeFactory.mapType(Map.class, String.class));
 Mockito.verify(mockJsonFormatVisitorWrapper).expectStringFormat(TypeFactory.mapType(Map.class, String.class));
 }
}