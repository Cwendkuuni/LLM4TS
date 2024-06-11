package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.jupiter.api.*;
import org.mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SettableAnyPropertyTest {

 private SettableAnyProperty settableAnyProperty;
 private AnnotatedMember setter;
 private BeanProperty property;
 private JavaType type;
 private KeyDeserializer keyDeserializer;
 private JsonDeserializer<Object> valueDeserializer;
 private TypeDeserializer valueTypeDeserializer;

 @BeforeEach
 public void setUp() {
 property = Mockito.mock(BeanProperty.class);
 setter = Mockito.mock(AnnotatedMember.class);
 type = Mockito.mock(JavaType.class);
 keyDeserializer = Mockito.mock(KeyDeserializer.class);
 valueDeserializer = Mockito.mock(JsonDeserializer.class);
 valueTypeDeserializer = Mockito.mock(TypeDeserializer.class);
 settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
 }

 @Test
 public void testSettableAnyPropertyCreation() {
 assertNotNull(settableAnyProperty);
 assertEquals(property, settableAnyProperty.getProperty());
 assertEquals(setter, settableAnyProperty._setter);
 assertEquals(type, settableAnyProperty._type);
 assertNull(settableAnyProperty._valueDeserializer);
 assertNull(settableAnyProperty._valueTypeDeserializer);
 assertNull(settableAnyProperty._keyDeserializer);
 }

 @Test
 public void testSettableAnyPropertyWithValueDeserializer() {
 JsonDeserializer<Object> newValueDeserializer = Mockito.mock(JsonDeserializer.class);
 SettableAnyProperty newSettableAnyProperty = settableAnyProperty.withValueDeserializer(newValueDeserializer);
 assertEquals(settableAnyProperty.getProperty(), newSettableAnyProperty.getProperty());
 assertEquals(settableAnyProperty._setter, newSettableAnyProperty._setter);
 assertEquals(settableAnyProperty._type, newSettableAnyProperty._type);
 assertEquals(newValueDeserializer, newSettableAnyProperty._valueDeserializer);
 assertEquals(settableAnyProperty._valueTypeDeserializer, newSettableAnyProperty._valueTypeDeserializer);
 assertEquals(settableAnyProperty._keyDeserializer, newSettableAnyProperty._keyDeserializer);
 }

 @Test
 public void testFixAccess() {
 DeserializationConfig config = Mockito.mock(DeserializationConfig.class);
 settableAnyProperty.fixAccess(config);
 verify(setter).fixAccess(config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 Object instance = Mockito.mock(Object.class);
 String propName = "testProp";
 Object key = "testKey";
 Object value = "testValue";
 when(keyDeserializer.deserializeKey(eq("testKey"), any(DeserializationContext.class)))
         .thenReturn(key);
 when(valueDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class)))
         .thenReturn(value);
 settableAnyProperty.deserializeAndSet(Mockito.mock(JsonParser.class), Mockito.mock(DeserializationContext.class), instance, propName);
 verify(instance).getClass();
 verify(instance, times(2)).getClass(); // check if getDeclaringClass() is called
 verify(setter).callOnWith(instance, eq(propName), eq(value));
 }

 @Test
 public void testDeserialize() throws IOException {
 JsonParser jsonParser = Mockito.mock(JsonParser.class);
 DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);
 Object result = settableAnyProperty.deserialize(jsonParser, deserializationContext);
 verify(jsonParser).getCurrentToken();
 verify(jsonParser).getValueAsToken();
 verify(valueDeserializer).deserialize(jsonParser, deserializationContext);
 assertEquals(valueDeserializer.deserialize(jsonParser, deserializationContext), result);
 }

 @Test
 public void testSet() throws IOException {
 Object instance = Mockito.mock(Object.class);
 Object propName = "testProp";
 Object value = "testValue";
 try {
 settableAnyProperty.set(instance, propName, value);
 } catch (IOException e) {
 fail("set method should not throw an IOException");
 }
 if (_settableAnyProperty._setterIsField) {
 // test setting value to a Map
 AnnotatedField annotatedField = Mockito.mock(AnnotatedField.class);
 Map<Object, Object> map = Mockito.mock(Map.class);
 when(setter.getValue(instance)).thenReturn(map);
 verify(map).put(eq(propName), eq(value));
 } else {
 // test setting value to a non-map property
 AnnotatedMethod annotatedMethod = Mockito.mock(AnnotatedMethod.class);
 when(setter.callOnWith(instance, eq(propName), any()))
         .then(invocation -> invocation.getArgument(2)); // this allows us to check the value passed to the setter
 verify(annotatedMethod).callOnWith(instance, eq(propName), eq(value));
 }
 }

 @Test
 public void testThrowAsIOE() throws IOException {
 Throwable exception = Mockito.mock(Throwable.class);
 settableAnyProperty._throwAsIOE(exception, "testProp", "testValue");
 String expectedMessage = "Problem deserializing \"" + "testProp" + "\" of class [any property on class " +
         getClass().getName() + "] (expected type: " + settableAnyProperty._type +
         "; actual type: null)";
 assertEquals(expectedMessage, exception.getMessage());
 }
}