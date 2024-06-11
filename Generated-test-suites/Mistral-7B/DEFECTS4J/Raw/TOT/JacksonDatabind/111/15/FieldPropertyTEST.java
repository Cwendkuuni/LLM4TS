import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class FieldPropertyTest {

 private Object testInstance;
 private FieldProperty fieldProperty;
 private Object annotatedField;
 private JsonParser mockJsonParser;
 private DeserializationContext mockContext;
 private Object expectedValue;

 @Before
 public void setup() throws InstantiationException, IllegalAccessException {
 testInstance = new Object();
 annotatedField = mock(AnnotatedField.class);
 when(annotatedField.getAnnotated()).thenReturn(new Field(new Annotation[]{new Annotation(){}}, "testField", null));
 fieldProperty = new FieldProperty(new BeanPropertyDefinition(null, "testField", null), null, null, null, annotatedField);
 mockJsonParser = mock(JsonParser.class);
 mockContext = mock(DeserializationContext.class);
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 expectedValue = new Object();
 when(mockJsonParser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
 when(mockJsonParser.readValueAs(Object.class)).thenReturn(expectedValue);

 fieldProperty.deserializeAndSet(mockJsonParser, mockContext, testInstance);
 verify(testInstance).getClass();
 verify(testInstance).getClass().getDeclaredField("testField");
 verify(testInstance).getClass().getDeclaredField("testField").set(testInstance, expectedValue);
 }

 @Test
 public void testDeserializeAndSet_NullValue() throws IOException {
 when(mockJsonParser.getValueAsToken()).thenReturn(JsonToken.VALUE_NULL);

 fieldProperty.deserializeAndSet(mockJsonParser, mockContext, testInstance);
 verify(mockContext).handleNullValue(fieldProperty, null, null);
 }

 @Test
 public void testDeserializeAndSet_NullValueProvider() throws IOException {
 JsonDeserializer<?> nullDeserializer = mock(JsonDeserializer.class);
 TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
 Object nullValue = new Object();
 NullValueProvider nullValueProvider = new NullValueProvider() {
 @Override
 public Object getNullValue(DeserializationContext ctxt) throws IOException {
 return nullValue;
 }
 };

 fieldProperty = new FieldProperty(new BeanPropertyDefinition(null, "testField", null), null, nullDeserializer, null, annotatedField);
 when(mockJsonParser.getValueAsToken()).thenReturn(JsonToken.VALUE_NULL);

 fieldProperty.deserializeAndSet(mockJsonParser, mockContext, testInstance);
 verify(mockContext).handleNullValue(fieldProperty, nullValue, null);
 }

 @Test
 public void testDeserializeAndSet_TypeDeserializer() throws IOException {
 expectedValue = new Object();
 TypeDeserializer typeDeserializerMock = mock(TypeDeserializer.class);
 when(mockJsonParser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
 when(mockJsonParser.readValueAs(String.class)).thenReturn("TestString");
 when(typeDeserializerMock.deserializeWithType(mockJsonParser, mockContext, any(TypeDeserializer.class))).thenReturn(expectedValue);

 fieldProperty = new FieldProperty(new BeanPropertyDefinition(null, "testField", typeDeserializerMock), null, null, null, annotatedField);

 fieldProperty.deserializeAndSet(mockJsonParser, mockContext, testInstance);
 verify(testInstance).getClass().getDeclaredField("testField").set(testInstance, expectedValue);
 }

 @Test(expected = IOException.class)
 public void testDeserializeAndSet_IOException() throws IOException {
 when(mockJsonParser.getValueAsToken()).thenThrow(new IOException("Exception"));

 fieldProperty.deserializeAndSet(mockJsonParser, mockContext, testInstance);
 }

 @Test
 public void testSet() throws IOException {
 fieldProperty.set(testInstance, new Object());
 verify(testInstance).getClass().getDeclaredField("testField").set(testInstance, new Object());
 }

 @Test
 public void testSet_NullValue() {
 fieldProperty.set(testInstance, null);
 verify(testInstance).getClass().getDeclaredField("testField").set(testInstance, null);
 }

 @Test
 public void testSet_IOException() {
 try {
 fieldProperty.set(testInstance, new Object());
 fail();
 } catch (IOException e) {
 // Expected
 }
 }
}