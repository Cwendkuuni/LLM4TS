import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonEditor;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class NumberSerializersTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 /**
 * Test for the addAll method
 */
 @Test
 public void testAddAll() {
 Map<String, JsonSerializer<?>> allDeserializers = new java.util.LinkedHashMap<>();
 NumberSerializers.addAll(allDeserializers);
 assertEquals(12, allDeserializers.size());
 }

 /**
 * Test for the IntegerSerializer
 */
 @Test
 public void testIntegerSerializer() throws JsonProcessingException, IOException {
 final int intValue = 42;
 final JsonParser jsonParser = objectMapper.getFactory().createParser("42");
 JsonEditor editor = objectMapper.readerForUpdating(new Integer(intValue));
 editor.readValue(jsonParser);
 assertEquals(intValue, editor.getValue());
 }

 /**
 * Test for the IntegerSerializer with incorrect input
 */
 @Test(expected = IOException.class)
 public void testIntegerSerializerIncorrectInput() throws IOException {
 final JsonParser jsonParser = objectMapper.getFactory().createParser("notAnInteger");
 IntegerSerializer integerSerializer = new IntegerSerializer();
 integerSerializer.deserialize(jsonParser, JsonSerializationContext.empty, new TypeIdentifier("int"), new SerializerProvider());
 fail("Expected IOException to be thrown");
 }

 /**
 * Test for the IntLikeSerializer
 */
 @Test
 public void testIntLikeSerializer() throws JsonProcessingException, IOException {
 final int intValue = 42;
 final Long longValue = 42L;
 final JsonParser jsonParser = objectMapper.getFactory().createParser("42");
 JsonEditor editor = objectMapper.readerForUpdating(intValue);
 editor.readValue(jsonParser);
 assertEquals(intValue, editor.getValue());

 editor = objectMapper.readerForUpdating(longValue);
 editor.readValue(jsonParser);
 assertEquals(intValue, editor.getValue());
 }

 /**
 * Test for the IntLikeSerializer with incorrect input
 */
 @Test(expected = IOException.class)
 public void testIntLikeSerializerIncorrectInput() throws IOException {
 final JsonParser jsonParser = objectMapper.getFactory().createParser("notAnInteger");
 IntLikeSerializer intLikeSerializer = new IntLikeSerializer();
 intLikeSerializer.deserialize(jsonParser, JsonSerializationContext.empty, new TypeIdentifier("int"), new SerializerProvider());
 fail("Expected IOException to be thrown");
 }

 /**
 * Test for the LongSerializer
 */
 @Test
 public void testLongSerializer() throws JsonProcessingException, IOException {
 final long longValue = 42L;
 final JsonParser jsonParser = objectMapper.getFactory().createParser("42");
 JsonEditor editor = objectMapper.readerForUpdating(longValue);
 editor.readValue(jsonParser);
 assertEquals(longValue, editor.getValue());
 }

 /**
 * Test for the LongSerializer with incorrect input
 */
 @Test(expected = IOException.class)
 public void testLongSerializerIncorrectInput() throws IOException {
 final JsonParser jsonParser = objectMapper.getFactory().createParser("notALong");
 LongSerializer longSerializer = new LongSerializer();
 longSerializer.deserialize(jsonParser, JsonSerializationContext.empty, new TypeIdentifier("long"), new SerializerProvider());
 fail("Expected IOException to be thrown");
 }

 /**
 * Test for the FloatSerializer
 */
 @Test
 public void testFloatSerializer() throws JsonProcessingException, IOException {
 final float floatValue = 42.0f;
 final JsonParser jsonParser = objectMapper.getFactory().createParser("42.0");
 JsonEditor editor = objectMapper.readerForUpdating(floatValue);
 editor.readValue(jsonParser);
 assertEquals(floatValue, editor.getValue(), 0.001f);
 }

 /**
 * Test for the FloatSerializer with incorrect input
 */
 @Test(expected = IOException.class)
 public void testFloatSerializerIncorrectInput() throws IOException {
 final JsonParser jsonParser = objectMapper.getFactory().createParser("notAFloat");
 FloatSerializer floatSerializer = new FloatSerializer();
 floatSerializer.deserialize(jsonParser, JsonSerializationContext.empty, new TypeIdentifier("float"), new SerializerProvider());
 fail("Expected IOException to be thrown");
 }

 /**
 * Test for the DoubleSerializer
 */
 @Test
 public void testDoubleSerializer() throws JsonProcessingException, IOException {
 final double doubleValue = 42.0;
 final JsonParser jsonParser = objectMapper.getFactory().createParser("42.0");
 JsonEditor editor = objectMapper.readerForUpdating(doubleValue);
 editor.readValue(jsonParser);
 assertEquals(doubleValue, editor.getValue(), 0.001);
 }

 /**
 * Test for the DoubleSerializer with incorrect input
 */
 @Test(expected = IOException.class)
 public void testDoubleSerializerIncorrectInput() throws IOException {
 final JsonParser jsonParser = objectMapper.getFactory().createParser("notADouble");
 DoubleSerializer doubleSerializer = new DoubleSerializer();
 doubleSerializer.deserialize(jsonParser, JsonSerializationContext.empty, new TypeIdentifier("double"), new SerializerProvider());
 fail("Expected IOException to be thrown");
 }

 /**
 * Test for the null case
 */
 @Test
 public void testNull() throws JsonProcessingException, IOException {
 JsonEditor editor = objectMapper.readerForUpdating(null);
 assertNull(editor.getValue());
 }
}