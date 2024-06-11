import org.junit.Test;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.ObjectIdReader.IdType;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.NamedPropertyIdentifier;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

public class ObjectIdValuePropertyTest {

 private ObjectIdReader mockObjectIdReader;
 private JsonParser mockJsonParser;
 private DeserializationContext mockCtxt;
 private ObjectIdValueProperty property;
 private AnnotatedMember member;

 @Before
 public void setup() throws Exception {
 mockObjectIdReader = mock(ObjectIdReader.class);
 mockJsonParser = mock(JsonParser.class);
 mockCtxt = mock(DeserializationContext.class);
 property = new ObjectIdValueProperty(mockObjectIdReader, new PropertyMetadata());
 member = new AnnotatedMember(new NamedPropertyIdentifier("id"), new BeanPropertyDefinition(new NamedPropertyIdentifier("id"), null, null, null, null, null, false, false, null, TypeSerializer.empty));
 }

 @Test
 public void testConstructor() {
 // No arguments constructor test
 property = new ObjectIdValueProperty(null, null);
 assertNull(property.getName());
 assertNull(property.getIdType());
 assertNull(property.getMetadata());
 assertNull(property.getValueDeserializer());

 // Constructor with arguments test
 ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
 when(objectIdReader.propertyName).thenReturn(new PropertyName("id"));
 when(objectIdReader.getIdType()).thenReturn(IdType.STRING);
 when(objectIdReader.getDeserializer()).thenReturn(new FromStringDeserializer(String.class));
 property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 assertNotNull(property.getName());
 assertNotNull(property.getIdType());
 assertNotNull(property.getMetadata());
 assertNotNull(property.getValueDeserializer());
 }

 @Test
 public void testGetAnnotation() {
 assertNull(property.getAnnotation(Null.class));
 }

 @Test
 public void testGetMember() {
 assertNull(property.getMember());
 }

 @Test
 public void testDeserializeAndSet() throws Exception {
 String id = "123";
 when(mockJsonParser.getValueAsString()).thenReturn(id);
 Object instance = new Object();
 property.deserializeAndSet(mockJsonParser, mockCtxt, instance);
 AnnotationIntrospector introspector = new AnnotationIntrospector();
 member = introspector.findProperty(instance.getClass(), "id");
 assertNotNull(member);
 assertNotNull(member.getAnnotations());
 Object value = member.getAnnotations()[0].getValue();
 assertNotNull(value);
 assertSame("123", value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetNullId() throws Exception {
 Object instance = new Object();
 property.set(instance, null);
 }

 @Test
 public void testSetAndReturn() throws Exception {
 String id = "456";
 Object instance = new Object();
 when(mockJsonParser.getValueAsString()).thenReturn(id);
 Object value = property.setAndReturn(instance, id);
 assertSame(instance, value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetWithoutIdProperty() throws Exception {
 Object idProperty = null;
 Object instance = new Object();
 property = new ObjectIdValueProperty(mockObjectIdReader, new PropertyMetadata(), idProperty);
 property.set(instance, "123");
 }

 @Test
 public void testToString() throws Exception {
 String propertyName = "id";
 String idType = "String";
 JsonDeserializer<?> valueDeserializer = new FromStringDeserializer(String.class);
 String metadataString = "{}";
 String toString = String.format("ObjectIdValueProperty(%s, %s, %s, %s)", propertyName, idType, metadataString, valueDeserializer);
 assertSame(toString, property.toString());
 }

}