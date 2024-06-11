import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Type;

public class StdDelegatingSerializerTest {

 @SuppressWarnings("unchecked")
 private static final Converter<String, Integer> STRING_TO_INTEGER_CONVERTER = new Converter.Std() {
 @Override
 public Integer convert(String src, Class<Integer> destType) {
 return Integer.parseInt(src);
 }

 @Override
 public String convertAndCreate(Integer src, Class<String> destType) {
 return Integer.toString(src);
 }
 };

 @SuppressWarnings("unchecked")
 private static final JsonSerializer<String> STRING_SERIALIZER = new StdDelegatingSerializer<>(STRING_TO_INTEGER_CONVERTER, Integer.class) {
 @Override
 public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
 // Delegate serialization to IntegerSerializer
 super.serialize(((Integer) convertValue(value)).intValue(), gen, provider);
 }
 };

 @Test
 public void testStdDelegatingSerializerWithStringToIntegerConversion() throws JsonMappingException, JsonGenerationException, IOException {
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 mapper.registerSerializer(String.class, STRING_SERIALIZER);

 @JsonSerialize(using = StdDelegatingSerializer.class)
 class TestObject {
 private String str;

 public String getStr() {
 return str;
 }

 public void setStr(String str) {
 this.str = str;
 }
 }

 TestObject testObject = new TestObject();
 testObject.setStr("42");
 String json = mapper.writeValueAsString(testObject);
 assertNotNull(json);
 assertEquals("\"42\"", json);

 Object readValue = mapper.readValue(json, TestObject.class);
 TestObject readObject = (TestObject) readValue;
 assertEquals("42", readObject.getStr());
 }
}