import static org.junit.Assert.*;
import org.junit.*;

public class InnerClassPropertyTest {

    @Test
    public void testConstructorWithDelegateAndCtor() {
        SettableBeanProperty delegate = new SettableBeanProperty();
        Constructor<?> ctor = new Constructor<>();
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, ctor);
        
        assertEquals(delegate, innerClassProperty._delegate);
        assertEquals(ctor, innerClassProperty._creator);
    }
    
    @Test
    public void testConstructorWithInnerClassPropertyAndAnn() {
        InnerClassProperty src = new InnerClassProperty();
        AnnotatedConstructor ann = new AnnotatedConstructor();
        InnerClassProperty innerClassProperty = new InnerClassProperty(src, ann);
        
        assertEquals(src._delegate, innerClassProperty._delegate);
        assertEquals(ann, innerClassProperty._annotated);
        assertEquals(ann.getAnnotated(), innerClassProperty._creator);
    }
    
    @Test
    public void testConstructorWithInnerClassPropertyAndDeser() {
        InnerClassProperty src = new InnerClassProperty();
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        InnerClassProperty innerClassProperty = new InnerClassProperty(src, deser);
        
        assertEquals(src._delegate.withValueDeserializer(deser), innerClassProperty._delegate);
        assertEquals(src._creator, innerClassProperty._creator);
    }
    
    @Test
    public void testConstructorWithInnerClassPropertyAndNewName() {
        InnerClassProperty src = new InnerClassProperty();
        PropertyName newName = new PropertyName();
        InnerClassProperty innerClassProperty = new InnerClassProperty(src, newName);
        
        assertEquals(src._delegate.withName(newName), innerClassProperty._delegate);
        assertEquals(src._creator, innerClassProperty._creator);
    }
    
    @Test
    public void testWithName() {
        PropertyName newName = new PropertyName();
        InnerClassProperty src = new InnerClassProperty();
        InnerClassProperty innerClassProperty = src.withName(newName);
        
        assertEquals(src, innerClassProperty);
        assertEquals(src._delegate.withName(newName), innerClassProperty._delegate);
        assertEquals(src._creator, innerClassProperty._creator);
    }
    
    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        InnerClassProperty src = new InnerClassProperty();
        InnerClassProperty innerClassProperty = src.withValueDeserializer(deser);
        
        assertEquals(src, innerClassProperty);
        assertEquals(src._delegate.withValueDeserializer(deser), innerClassProperty._delegate);
        assertEquals(src._creator, innerClassProperty._creator);
    }
    
    @Test
    public void testAssignIndex() {
        InnerClassProperty innerClassProperty = new InnerClassProperty();
        int index = 100;
        innerClassProperty.assignIndex(index);
        
        assertEquals(index, innerClassProperty._delegate.getPropertyIndex());
    }
    
    @Test
    public void testGetPropertyIndex() {
        InnerClassProperty innerClassProperty = new InnerClassProperty();
        int index = 100;
        innerClassProperty.assignIndex(index);
        
        assertEquals(index, innerClassProperty.getPropertyIndex());
    }
    
    @Test
    public void testGetAnnotation() {
        InnerClassProperty innerClassProperty = new InnerClassProperty();
        Class<A> acls = A.class;
        
        assertNull(innerClassProperty.getAnnotation(acls));
    }
    
    @Test
    public void testGetMember() {
        InnerClassProperty innerClassProperty = new InnerClassProperty();
        AnnotatedMember member = new AnnotatedMember();
        
        assertEquals(member, innerClassProperty.getMember());
    }
    
    @Test
    public void testDeserializeAndSet() {
        InnerClassProperty innerClassProperty = new InnerClassProperty();
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object bean = new Object();
        
        // Test with VALUE_NULL token
        jp.nextToken(JsonToken.VALUE_NULL);
        innerClassProperty.deserializeAndSet(jp, ctxt, bean);
        assertNull(innerClassProperty.get(bean));
        
        // Test with _valueTypeDeserializer
        jp.clearCurrentToken();
        ValueDeserializer<?> valueTypeDeserializer = new ValueDeserializer<>();
        jp.nextToken(JsonToken.START_OBJECT);
        innerClassProperty._valueTypeDeserializer = valueTypeDeserializer;
        // InnerClassProperty.deserialize should be called
        innerClassProperty.deserializeAndSet(jp, ctxt, bean);
        assertNull(innerClassProperty.get(bean));
        
        // Test with regular case
        jp.clearCurrentToken();
        Object value = new Object();
        Constructor<?> ctor = new Constructor<>();
        innerClassProperty._creator = ctor;
        innerClassProperty._valueDeserializer = new ValueDeserializer<>();
        innerClassProperty.deserializeAndSet(jp, ctxt, bean);
        assertEquals(value, innerClassProperty.get(bean));
    }
    
    @Test
    public void testDeserializeSetAndReturn() {
        InnerClassProperty innerClassProperty = new InnerClassProperty();
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        Object value = new Object();
        
        // Mock innerClassProperty.deserialize
        innerClassProperty.deserialize = value;
        Object result = innerClassProperty.deserializeSetAndReturn(jp, ctxt, instance);
        
        assertEquals(value, result);
        assertEquals(value, innerClassProperty.deserialize);
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
        
        assertEquals(innerClassProperty._annotated, result._annotated);
        assertEquals(innerClassProperty, result);
    }
    
    @Test
    public void testWriteReplace() {
        InnerClassProperty innerClassProperty = new InnerClassProperty();
        InnerClassProperty result = innerClassProperty.writeReplace();
        
        if (innerClassProperty._annotated != null) {
            assertEquals(innerClassProperty, result);
        } else {
            assertEquals(innerClassProperty._delegate, result._delegate);
            assertEquals(innerClassProperty._creator, result._creator);
        }
    }
}