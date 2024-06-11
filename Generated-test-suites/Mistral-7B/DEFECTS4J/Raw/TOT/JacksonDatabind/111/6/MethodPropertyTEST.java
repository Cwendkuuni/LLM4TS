import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class MethodPropertyTest {

 private Object testInstance;
 private MethodProperty property;
 private JsonParser mockParser;
 private DeserializationContext mockContext;
 private JsonDeserializer<String> mockStringDeserializer;
 private TypeDeserializer mockTypeDeserializer;
 private NullValueProvider mockNullValueProvider;

 @Before
 public void setup() throws Exception {
 testInstance = new Object();
 mockParser = mock(JsonParser.class);
 mockContext = mock(DeserializationContext.class);
 mockStringDeserializer = mock(JsonDeserializer.class);
 mockTypeDeserializer = mock(TypeDeserializer.class);
 mockNullValueProvider = mock(NullValueProvider.class);
 property = new MethodProperty(new BeanPropertyDefinition(null, "testProperty"),
 JsonTypeFactory.instance.constructType(String.class), mockTypeDeserializer,
 Annotations.empty(), new AnnotatedMethod(Test.class.getMethod("setTestProperty", String.class), Test.class), true, true, mockParser, null, null, mockStringDeserializer, mockNullValueProvider);
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 String value = "testValue";
 when(mockParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 when(mockParser.getValueAsString()).thenReturn(value);
 when(mockValueDeserializer.deserialize(mockParser, mockContext)).thenReturn(value);
 property.deserializeAndSet(mockParser, mockContext, testInstance);
 verify(testInstance).setTestProperty(value);
 }

 @Test
 public void testDeserializeAndSkipNull() throws IOException {
 when(mockParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
 property.deserializeAndSet(mockParser, mockContext, testInstance);
 verify(mockContext).handleMissingValueException(eq(testInstance.getClass()), anyString());
 }

 @Test
 public void testDeserializeWithType() throws IOException {
 TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
 when(typeDeserializer.deserializeTypedFromObject(any(), eq(String[].class))).thenReturn(new String[]{"testValue1", "testValue2"});
 when(mockParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
 when(mockParser.readValueAs(String[].class)).thenReturn(new String[]{"testValue1", "testValue2"});
 when(mockParser.nextToken()).thenReturn(JsonToken.END_ARRAY);
 when(mockTypeDeserializer.deserializeTypedFromObject(any(), eq(String[].class))).thenReturn(typeDeserializer);
 when(mockParser.getValueAsString()).thenThrow(IOException.class);
 property = new MethodProperty(new BeanPropertyDefinition(null, "testProperty"),
 JsonTypeFactory.instance.constructType(String[].class), mockTypeDeserializer,
 Annotations.empty(), new AnnotatedMethod(Test.class.getMethod("setTestProperty", String[].class), Test.class), true, false, mockParser, null, null, mockValueDeserializer);
 property.deserializeAndSet(mockParser, mockContext, testInstance);
 verify(testInstance).setTestProperty((Object)new String[]{"testValue1", "testValue2"});
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithTypeThrowsException() throws IOException {
 when(mockParser.getValueAsString()).thenThrow(IOException.class);
 property.deserializeAndSet(mockParser, mockContext, testInstance);
 }

 @Test
 public void testSet() throws IOException {
 property.set(testInstance, "testValue");
 verify(testInstance).setTestProperty("testValue");
 }

 @Test
 public void testSetAndReturnNull() throws IOException {
 when(testInstance.getClass()).thenReturn(Null.class);
 property.set(testInstance, "testValue");
 verifyNoMoreInteractions(testInstance);
 }

 @Test
 public void testSetAndReturnNotNull() throws IOException {
 property.set(testInstance, "testValue");
 verify(testInstance).setTestProperty("testValue");
 verify(testInstance).toString();
 }

 @Test
 public void testSerializeAndSet() throws IOException {
 ObjectWriter writer = new ObjectMapper().writerFor(new TypeReference<Object>() {});
 String json = "{\"testProperty\":\"testValue\"}";
 JsonParser parser = new ObjectMapper().readTree(json);
 property.deserializeAndSet(parser, mockContext, testInstance);
 Object result = writer.writeValueAsString(testInstance);
 assertEquals(json, result);
 }
}