import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("StringArrayDeserializer tests")
public class StringArrayDeserializerTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @BeforeAll
 static void setup() {
 mapper.registerModule(new SimpleModule()
 .addDeserializer(String[].class, StringArrayDeserializer.instance)
 .addDeserializer(String.class, new SimpleStringDeserializer()));
 }

 @Nested
 @DisplayName("deserialize method tests")
 class DeserializeTests {

 @Test
 void testEmptyArray() throws Exception {
 String[] result = mapper.readValue("[]", String[].class);
 assertArrayEquals(new String[0], result);
 }

 @Test
 void testEmptyStringArrayWithNullElement() throws Exception {
 String json = "[]";
 String[] result = mapper.readValue(json, String[].class);
 assertArrayEquals(new String[0], result);

 json = "[]";
 result = mapper.readValue(JsonFactory.instance.createParser(json), String[].class);
 assertArrayEquals(new String[0], result);
 }

 @Test
 void testStringArrayWithOneElement() throws Exception {
 String json = "[\"abc\"]";
 String[] result = mapper.readValue(json, String[].class);
 assertArrayEquals(new String[]{"abc"}, result);
 }

 @Test
 void testStringArrayWithMultipleElements() throws Exception {
 String json = "[\"abc\",\"def\",\"ghi\"]";
 String[] result = mapper.readValue(json, String[].class);
 assertArrayEquals(new String[]{"abc", "def", "ghi"}, result);
 }

 @Test
 void testStringArrayWithNullElements() throws Exception {
 String json = "[null]";
 String[] result = mapper.readValue(json, String[].class);
 assertArrayEquals(new String[]{null}, result);

 json = "[null, \"abc\", null]";
 result = mapper.readValue(json, String[].class);
 assertArrayEquals(new String[]{"null", "abc", null}, result);
 }

 @Test
 void testStringArrayWithCustomValueDeserializer() throws Exception {
 JsonDeserializer<String> customDeserializer = new StandardDeserializer<String>(String.class) {
 @Override
 public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return p.getValueAsString() + "_custom";
 }
 };
 String[] result = mapper.readValue("[\"abc\",null,\"def\"]", String[].class, mapper.constructType(String[].class), customDeserializer);
 assertArrayEquals(new String[]{"abc_custom", null, "def_custom"}, result);
 }

 @Test
 void testStringArrayWithTypeDeserializer() throws Exception {
 TypeDeserializer typeDeserializer = new BeanPropertyTypeDeserializer(String[].class) {
 @Override
 public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer vd) throws IOException {
 String[] result = vd.deserializeTypedFromArray(p, ctxt);
 for (int i = 0; i < result.length; i++) {
 result[i] = p.getText();
 }
 return result;
 }
 };
 String[] result = mapper.readValue("[\"abc\",\"def\",\"ghi\"]", String[].class, null, typeDeserializer);
 assertArrayEquals(new String[]{"abc", "def", "ghi"}, result);
 }
 }

 @Nested
 @DisplayName("handleNonArray method tests")
 class HandleNonArrayTests {

 @Test
 void testNullInput() throws Exception {
 JsonParser jsonParser = JsonFactory.instance.createParser("null");
 String[] result = StringArrayDeserializer.instance.handleNonArray(jsonParser, null);
 assertArrayEquals(new String[0], result);
 }

 @Test
 void testStringInput() throws Exception {
 JsonParser jsonParser = JsonFactory.instance.createParser("{\"value\":\"abc\"}");
 String[] result = StringArrayDeserializer.instance.handleNonArray(jsonParser, null);
 assertArrayEquals(new String[]{"abc"}, result);
 }

 @Test
 void testArrayInputWithSingleValue() throws Exception {
 JsonParser jsonParser = JsonFactory.instance.createParser("[\"abc\"]");
 String[] result = StringArrayDeserializer.instance.handleNonArray(jsonParser, null);
 assertArrayEquals(new String[]{"abc"}, result);
 }

 @Test
 void testArrayInputWithMultipleValues() throws Exception {
 JsonParser jsonParser = JsonFactory.instance.createParser("[\"abc\",\"def\",\"ghi\"]");
 String[] result = StringArrayDeserializer.instance.handleNonArray(jsonParser, null);
 assertArrayEquals(new String[]{"abc", "def", "ghi"}, result);
 }
 }
}