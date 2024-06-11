import org.junit.Test;
import static org.junit.Assert.*;

public class InnerClassPropertyTest {

    @Test
    public void testConstructor1() {
        SettableBeanProperty delegate = new SettableBeanProperty();
        Constructor<?> ctor = new Constructor<>();
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, ctor);
        assertEquals(delegate, innerClassProperty._delegate);
        assertEquals(ctor, innerClassProperty._creator);
    }
    
    @Test
    public void testConstructor2() {
        SettableBeanProperty delegate = new SettableBeanProperty();
        Constructor<?> ctor = null;
        try {
            InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, ctor);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testConstructor3() {
        SettableBeanProperty delegate = new SettableBeanProperty();
        Constructor<?> ctor = new Constructor<>();
        AnnotatedConstructor ann = new AnnotatedConstructor();
        InnerClassProperty src = new InnerClassProperty(delegate, ctor);
        InnerClassProperty innerClassProperty = new InnerClassProperty(src, ann);
        assertEquals(src._delegate, innerClassProperty._delegate);
        assertEquals(ann, innerClassProperty._annotated);
        assertEquals(ctor, innerClassProperty._creator);
    }
    
    @Test
    public void testConstructor4() {
        SettableBeanProperty delegate = new SettableBeanProperty();
        Constructor<?> ctor = new Constructor<>();
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        InnerClassProperty src = new InnerClassProperty(delegate, ctor);
        InnerClassProperty innerClassProperty = new InnerClassProperty(src, deser);
        assertEquals(src._delegate.withValueDeserializer(deser), innerClassProperty._delegate);
        assertEquals(ctor, innerClassProperty._creator);
    }
    
    @Test
    public void testConstructor5() {
        SettableBeanProperty delegate = new SettableBeanProperty();
        Constructor<?> ctor = new Constructor<>();
        PropertyName newName = new PropertyName();
        InnerClassProperty src = new InnerClassProperty(delegate, ctor);
        InnerClassProperty innerClassProperty = new InnerClassProperty(src, newName);
        assertEquals(src._delegate.withName(newName), innerClassProperty._delegate);
        assertEquals(ctor, innerClassProperty._creator);
    }
    
    @Test
    public void testWithName() {
        PropertyName newName = new PropertyName();
        InnerClassProperty src = new InnerClassProperty();
        InnerClassProperty innerClassProperty = src.withName(newName);
        assertEquals(newName, innerClassProperty._name);
        assertEquals(src._delegate, innerClassProperty._delegate);
        assertEquals(src._creator, innerClassProperty._creator);
    }
    
    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        InnerClassProperty src = new InnerClassProperty();
        InnerClassProperty innerClassProperty = src.withValueDeserializer(deser);
        assertEquals(src._delegate.withValueDeserializer(deser), innerClassProperty._delegate);
        assertEquals(src._creator, innerClassProperty._creator);
    }
    
    @Test
    public void testAssignIndex() {
        InnerClassProperty innerClassProperty = new InnerClassProperty();
        int index = 1;
        innerClassProperty.assignIndex(index);
        assertEquals(index, innerClassProperty._delegate.getPropertyIndex());
    }
    
    @Test
    public void testGetPropertyIndex() {
        InnerClassProperty innerClassProperty = new InnerClassProperty();
        int index = 1;
        innerClassProperty.assignIndex(index);
        assertEquals(index, innerClassProperty.getPropertyIndex());
    }
    
    @Test
    public void testGetAnnotation() {
        InnerClassProperty innerClassProperty = new InnerClassProperty();
        Class<A> acls = A.class;
        A annotation = innerClassProperty.getAnnotation(acls);
        assertEquals(innerClassProperty._delegate.getAnnotation(acls), annotation);
    }
    
    @Test
    public void testGetMember() {
        InnerClassProperty innerClassProperty = new InnerClassProperty();
        AnnotatedMember member = innerClassProperty.getMember();
        assertEquals(innerClassProperty._delegate.getMember(), member);
    }
    
    @Test
    public void testDeserializeAndSet() {
        InnerClassProperty innerClassProperty = new InnerClassProperty();
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object bean = new Object();
        
        // Test null value
        jp.setCurrentToken(JsonToken.VALUE_NULL);
        Object value = innerClassProperty._valueDeserializer.getNullValue(ctxt);
        innerClassProperty.deserializeAndSet(jp, ctxt, bean);
        assertEquals(value, innerClassProperty.get(bean));
        
        // Test value with type deserializer
        jp.setCurrentToken(JsonToken.VALUE_STRING);
        innerClassProperty._valueTypeDeserializer = new ValueTypeDeserializer();
        value = innerClassProperty._valueDeserializer.deserializeWithType(jp, ctxt, innerClassProperty._valueTypeDeserializer);
        innerClassProperty.deserializeAndSet(jp, ctxt, bean);
        assertEquals(value, innerClassProperty.get(bean));
        
        // Test regular case
        jp.setCurrentToken(JsonToken.VALUE_STRING);
        value = innerClassProperty._creator.newInstance(bean);
        innerClassProperty._valueDeserializer.deserialize(jp, ctxt, value);
        innerClassProperty.deserializeAndSet(jp, ctxt, bean);
        assertEquals(value, innerClassProperty.get(bean));
    }
    
    @Test
    public void testDeserializeSetAndReturn() {
        InnerClassProperty innerClassProperty = new InnerClassProperty();
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        
        // Test with deserialized value
        jp.setCurrentToken(JsonToken.VALUE_STRING);
        Object value = innerClassProperty._creator.newInstance(instance);
        innerClassProperty._valueDeserializer.deserialize(jp, ctxt, value);
        Object result = innerClassProperty.deserializeSetAndReturn(jp, ctxt, instance);
        assertEquals(value, result);
    }
    
    @Test
    public void testSet() {
        InnerClassProperty innerClassProperty = new InnerClassProperty();
        Object instance = new Object();
        Object value = new Object();
        innerClassProperty.set(instance, value);
        assertEquals(value, innerClassProperty.get(instance));
    }
    
    @Test
    public void testSetAndReturn() {
        InnerClassProperty innerClassProperty = new InnerClassProperty();
        Object instance = new Object();
        Object value = new Object();
        Object result = innerClassProperty.setAndReturn(instance, value);
        assertEquals(value, innerClassProperty.get(instance));
        assertEquals(value, result);
    }
    
    @Test
    public void testReadResolve() {
        InnerClassProperty innerClassProperty = new InnerClassProperty();
        InnerClassProperty result = innerClassProperty.readResolve();
        assertEquals(innerClassProperty, result);
        assertEquals(innerClassProperty._annotated, result._annotated);
    }
    
    @Test
    public void testWriteReplace() {
        InnerClassProperty innerClassProperty = new InnerClassProperty();
        Object result = innerClassProperty.writeReplace();
        if (innerClassProperty._annotated != null) {
            assertEquals(innerClassProperty, result);
        } else {
            assertTrue(result instanceof InnerClassProperty);
            assertEquals(innerClassProperty._creator, ((InnerClassProperty)result)._creator);
        }
    }
}