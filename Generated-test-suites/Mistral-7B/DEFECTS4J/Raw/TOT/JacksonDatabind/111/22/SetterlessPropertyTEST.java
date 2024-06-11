import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.BeanUtil;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class SetterlessPropertyTest {

 private Object testInstance;
 private SettableBeanProperty setterlessProperty;
 private JsonParser jsonParserMock;
 private DeserializationContext contextMock;

 @Before
 public void setup() throws InstantiationException, IllegalAccessException, NoSuchMethodException {
 testInstance = new Object();
 JsonDeserializer<Object> jsonDeserializerMock = mock(JsonDeserializer.class);
 TypeDeserializer typeDeserializerMock = mock(TypeDeserializer.class);
 Annotations contextAnnotations = new Annotations();
 AnnotatedMethod annotatedMethodMock = mock(AnnotatedMethod.class);
 Method getterMock = mock(Method.class);
 when(annotatedMethodMock.getAnnotated()).thenReturn(contextAnnotations);
 when(annotatedMethodMock.getMethod()).thenReturn(getterMock);
 when(getterMock.getName()).thenReturn("getTestProperty");
 when(getterMock.getParameterTypes()).thenReturn(new Class[0]);
 setterlessProperty = new SetterlessProperty(
 new BeanPropertyDefinition(null, "testProperty", Object.class, null),
 ObjectType.INSTANCETYPE, typeDeserializerMock, contextAnnotations, annotatedMethodMock);
 jsonParserMock = mock(JsonParser.class);
 contextMock = mock(DeserializationContext.class);
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 Object testProperty = new ArrayList<>();
 when(getterMock.invoke(testInstance, (Object[]) null)).thenReturn(testProperty);

 SettableBeanProperty setterlessPropertySpy = spy(setterlessProperty);
 doCallRealMethod().when(setterlessPropertySpy).deserializeAndSet(jsonParserMock, contextMock, testInstance);

 JsonToken jsonToken = JsonToken.VALUE_STRING;
 when(jsonParserMock.getCurrentToken()).thenReturn(jsonToken);
 String value = "testValue";

 setterlessPropertySpy.deserializeAndSet(jsonParserMock, contextMock, testInstance);
 assertEquals(value, testProperty);

 // Test deserialization with typed property
 ObjectType type = ObjectType.constructUnboundType(Map.class);
 when(typeDeserializerMock.mangleTypeNames(type)).thenReturn("HashMap");
 SettableBeanProperty setterlessPropertyWithType = new SetterlessProperty(
 setterlessProperty, jsonDeserializerMock, null) {
 @Override
 public TypeDeserializer getValueTypeDeserializer() {
 return typeDeserializerMock;
 }
 };
 doCallRealMethod().when(setterlessPropertySpy).deserializeAndSet(jsonParserMock, contextMock, testInstance);
 when(jsonParserMock.getCurrentToken()).thenReturn(jsonToken);
 when(jsonParserMock.getValueAsString()).thenReturn("{\"key\":\"value\"}");
 Object expectedMap = new HashMap<>();
 expectedMap.put("key", "value");
 setterlessPropertyWithType.deserializeAndSet(jsonParserMock, contextMock, testInstance);
 assertEquals(expectedMap, testProperty);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSet() throws IOException {
 setterlessProperty.set(testInstance, new Object());
 fail("Should not call `set()` on setterless property");
 }
}