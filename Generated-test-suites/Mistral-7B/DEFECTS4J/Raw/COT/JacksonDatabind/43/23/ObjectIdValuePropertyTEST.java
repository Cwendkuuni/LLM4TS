import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JsonNodeDeserializer;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.util.ClassUtil;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ObjectIdValuePropertyTest {

 private ObjectIdReader objectIdReader;
 private ObjectIdValueProperty objectIdValueProperty;
 private AnnotatedMember annotatedMember;
 private JsonParser jsonParser;
 private DeserializationContext deserializationContext;
 private SimpleBeanPropertyFilter filter;

 @Before
 public void setUp() {
 objectIdReader = mock(ObjectIdReader.class);
 annotatedMember = mock(AnnotatedMember.class);
 jsonParser = mock(JsonParser.class);
 deserializationContext = mock(DeserializationContext.class);
 filter = new SimpleBeanPropertyFilter();
 }

 @Test
 public void testObjectIdValuePropertyConstruction() {
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 assertNull(property.getIdProperty());
 assertNull(property.getValueDeserializer());
 assertNull(property.getMember());
 assertNull(property.getAnnotation(JsonNodeDeserializer.class));
 assertNull(property.getName());
 assertNull(property.getAnnotations());
 }

 @Test
 public void testObjectIdValuePropertyWithName() {
 PropertyName newName = PropertyName.construct("newName");
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, newName);
 assertEquals(newName, property.getName());
 assertNull(property.getIdProperty());
 assertNull(property.getValueDeserializer());
 assertNull(property.getMember());
 assertNull(property.getAnnotation(JsonNodeDeserializer.class));
 }

 @Test
 public void testObjectIdValuePropertyWithValueDeserializer() {
 JsonNodeDeserializer deserializer = mock(JsonNodeDeserializer.class);
 ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, deserializer);
 assertSame(deserializer, property.getValueDeserializer());
 assertNull(property.getIdProperty());
 assertNull(property.getMember());
 assertNull(property.getAnnotation(JsonNodeDeserializer.class));
 assertNull(property.getName());
 assertNull(property.getAnnotations());
 }

 @Test
 public void testObjectIdValuePropertyWithIdProperty() {
 ObjectIdReader idReader = mock(ObjectIdReader.class);
 SettableBeanProperty idProperty = mock(SettableBeanProperty.class);
 ObjectIdValueProperty property = new ObjectIdValueProperty(idReader, idProperty);
 assertSame(idProperty, property.getIdProperty());
 assertNull(property.getValueDeserializer());
 assertNull(property.getMember());
 assertNull(property.getAnnotation(JsonNodeDeserializer.class));
 assertNull(property.getName());
 assertNull(property.getAnnotations());
 }

 @Test
 public void testGetAnnotation() {
 assertNull(objectIdValueProperty.getAnnotation(JsonNodeDeserializer.class));
 }

 @Test
 public void testGetMember() {
 assertNull(objectIdValueProperty.getMember());
 }

 @Test
 public void testDeserializeAndSet() throws Exception {
 String id = "123";
 when(jsonParser.getText()).thenReturn(id);
 objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata()) {
 @Override public void deserializeSetAndReturn(JsonParser p, DeserializationContext ctxt, Object instance) throws IOException {
 Object idValue = _valueDeserializer.deserialize(p, ctxt);
 ReadableObjectId roid = ctxt.findObjectId(idValue, objectIdReader.generator, objectIdReader.resolver);
 roid.bindItem(instance);
 SettableBeanProperty idProp = _objectIdReader.idProperty;
 idProp.setAndReturn(instance, idValue);
 return instance;
 }
 };
 deserializationContext = mock(DeserializationContext.class);
 when(deserializationContext.findBeanPropertiesToIntrospect(filter, ClassUtil.getRawClass(Object.class), null)).thenReturn(new BeanPropertyDefinition[] {});
 when(deserializationContext.findObjectId(any(Object.class), any(ObjectIdReader.class), any(ObjectIdGenerator.class))).thenReturn(Mockito.mock(ReadableObjectId.class));
 when(deserializationContext.findBeanProperty(PropertyName.construct("idProperty"))).thenReturn(idProperty);
 when(objectIdReader.getIdType()).thenReturn(String.class);
 when(objectIdReader.getDeserializer()).thenReturn(Mockito.mock(JsonDeserializer.class));
 when(objectIdReader.generator).thenReturn(new ObjectIdGenerator() {
 @Override public ObjectId findId(JsonParser p, DeserializationContext ctxt) throws IOException {
 return "123";
 }
 });
 when(objectIdReader.resolver).thenReturn(Mockito.mock(ObjectIdResolver.class));
 when(objectIdReader.idProperty).thenReturn(idProperty);
 Object instance = new Object();
 objectIdValueProperty.deserializeAndSet(jsonParser, deserializationContext, instance);
 assertNotNull(instance);
 assertNotNull(instance.getClass().getDeclaredField("id"));
 assertEquals("123", instance.getId());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetWithNullId() throws Exception {
 Object instance = new Object();
 objectIdValueProperty.set(instance, null);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetWithIdPropertyNull() throws Exception {
 SettableBeanProperty idProperty = null;
 Object instance = new Object();
 objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, idProperty);
 objectIdValueProperty.set(instance, "123");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetWithValueDeserializerNull() throws Exception {
 ObjectIdValueProperty property = new ObjectIdValueProperty(null, new PropertyMetadata());
 property.set(new Object(), "123");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetWithObjectIdReaderNull() throws Exception {
 ObjectIdValueProperty property = new ObjectIdValueProperty(null, new PropertyMetadata());
 property.deserializeAndSet(jsonParser, deserializationContext, new Object());
 }
}