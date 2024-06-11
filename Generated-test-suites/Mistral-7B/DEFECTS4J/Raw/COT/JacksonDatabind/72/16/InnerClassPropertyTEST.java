import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.InnerClassProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class InnerClassPropertyTest {

 private ObjectBean objectBean;
 private SettableBeanProperty delegate;
 private InnerClassProperty innerClassProperty;
 private Constructor<?> constructor;
 private AnnotatedConstructor annotatedConstructor;
 private JsonParser jsonParser;
 private DeserializationContext deserializationContext;
 private JsonDeserializer<?> valueDeserializer;
 private JsonDeserializer<?> valueTypeDeserializer;
 private TypeDeserializer typeDeserializer;

 @Before
 public void setup() throws InstantiationException, IllegalAccessException, NoSuchMethodException {
 ObjectBean.class.getDeclaredConstructor().setAccessible(true);
 objectBean = new ObjectBean();
 delegate = new SettableBeanProperty(new SettableBeanProperty.Std(ObjectBean.class, "field"), null);
 innerClassProperty = new InnerClassProperty(delegate, constructor = ObjectInnerClass.class.getDeclaredConstructor());
 constructor.setAccessible(true);
 annotatedConstructor = new AnnotatedConstructor(new JavaClass(), constructor, null, null);
 jsonParser = new JsonParser();
 deserializationContext = new DeserializationContext(new StandardDeserializationContext());
 valueDeserializer = new StdDeserializer<>((Class<?>) ObjectInnerClass.class);
 valueTypeDeserializer = new StdTypeDeserializer(null, null, ObjectInnerClass.class);
 typeDeserializer = new StdTypeDeserializer(null, null, ObjectInnerClass.class);
 }

 @Test
 public void testConstructor() {
 InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, constructor);
 assertEquals(delegate, innerClassProperty._delegate);
 assertNotNull(innerClassProperty._creator);
 assertNull(innerClassProperty._annotated);
 }

 @Test
 public void testInnerClassPropertyWithValueDeserializer() throws IOException {
 innerClassProperty = new InnerClassProperty(delegate, valueDeserializer);
 assertNull(innerClassProperty._creator);
 assertNull(innerClassProperty._annotated);
 assertEquals(valueDeserializer, innerClassProperty._valueDeserializer);
 }

 @Test
 public void testInnerClassPropertyWithAnnotatedConstructor() {
 innerClassProperty = new InnerClassProperty(delegate, annotatedConstructor);
 assertNull(innerClassProperty._creator);
 assertEquals(annotatedConstructor, innerClassProperty._annotated);
 assertNull(innerClassProperty._valueDeserializer);
 }

 @Test
 public void testInnerClassPropertyWithJsonDeserializer() {
 innerClassProperty = new InnerClassProperty(delegate, valueDeserializer, jsonParser, deserializationContext);
 assertNull(innerClassProperty._creator);
 assertNull(innerClassProperty._annotated);
 assertEquals(valueDeserializer, innerClassProperty._valueDeserializer);
 }

 @Test
 public void testInnerClassPropertyWithPropertyName() {
 PropertyName newName = new PropertyName("newName");
 innerClassProperty = new InnerClassProperty(delegate, newName);
 assertEquals(newName, innerClassProperty._name);
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 Object object = new Object();
 innerClassProperty.deserializeAndSet(jsonParser, deserializationContext, object);
 assertEquals(new ObjectInnerClass(), object);
 }

 @Test(expected = IOException.class)
 public void testDeserializeAndSetWithNullValue() throws IOException {
 Object object = new Object();
 jsonParser.nextToken(); // consume START_OBJECT token
 innerClassProperty.deserializeAndSet(jsonParser, deserializationContext, object);
 fail("Expected IOException: Cannot deserialize instance of com.fasterxml.jackson.databind.deser.impl.ObjectInnerClass out of NULL value");
 }

 @Test(expected = IOException.class)
 public void testDeserializeAndSetWithTypeMismatch() throws IOException {
 Object object = new Object();
 jsonParser.setValue("\"StringValue\""); // set unexpected String value
 innerClassProperty.deserializeAndSet(jsonParser, deserializationContext, object);
 fail("Expected IOException: Cannot construct instance of com.fasterxml.jackson.databind.deser.impl.ObjectInnerClass from String value (expected class java.lang.Object)");
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 Object object = new Object();
 Object newObject = innerClassProperty.deserializeSetAndReturn(jsonParser, deserializationContext, object);
 assertEquals(new ObjectInnerClass(), newObject);
 }

 @Test
 public void testSet() throws IOException {
 Object object = new Object();
 Object value = new ObjectInnerClass();
 innerClassProperty.set(object, value);
 assertSame(value, object.getInnerClass());
 }

 @Test(expected = IOException.class)
 public void testSetWithNullValue() throws IOException {
 Object object = new Object();
 Object value = null;
 innerClassProperty.set(object, value);
 fail("Expected IOException: Cannot set Null value for property 'innerClass'");
 }

 @Test
 public void testSetWithWrongTypeValue() throws IOException {
 Object object = new Object();
 Object value = new String("StringValue");
 innerClassProperty.set(object, value);
 assertNull(object.getInnerClass());
 }

 @Test
 public void testReadResolve() {
 InnerClassProperty innerClassProperty = new InnerClassProperty(this, annotatedConstructor);
 InnerClassProperty readResolve = (InnerClassProperty) innerClassProperty.readResolve();
 assertEquals(innerClassProperty, readResolve);
 }

 @Test
 public void testWriteReplace() throws IOException {
 InnerClassProperty innerClassProperty = new InnerClassProperty(this, new AnnotatedConstructor(null, constructor, null, null));
 Object writeReplace = innerClassProperty.writeReplace();
 assertEquals(innerClassProperty, writeReplace);
 }
}

class ObjectBean {
 private Object innerClass;
 // getter and setter for 'innerClass'
}

class ObjectInnerClass {
 // empty class
}