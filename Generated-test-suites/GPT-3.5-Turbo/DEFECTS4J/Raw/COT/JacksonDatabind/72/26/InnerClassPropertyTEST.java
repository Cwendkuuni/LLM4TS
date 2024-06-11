
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Constructor;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class InnerClassPropertyTest {

    @Test
    public void testConstructor() {
        SettableBeanProperty delegate = mock(SettableBeanProperty.class);
        Constructor<?> ctor = mock(Constructor.class);
        
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, ctor);
        
        assertEquals(delegate, innerClassProperty._delegate);
        assertEquals(ctor, innerClassProperty._creator);
    }

    @Test
    public void testConstructorDeserialization() {
        InnerClassProperty src = mock(InnerClassProperty.class);
        AnnotatedConstructor ann = mock(AnnotatedConstructor.class);
        
        InnerClassProperty innerClassProperty = new InnerClassProperty(src, ann);
        
        assertEquals(src._delegate, innerClassProperty._delegate);
        assertEquals(ann, innerClassProperty._annotated);
        assertEquals(ann.getAnnotated(), innerClassProperty._creator);
    }
    
    @Test
    public void testConstructorDeserializationWithNullAnnotated() {
        InnerClassProperty src = mock(InnerClassProperty.class);
        
        InnerClassProperty innerClassProperty = new InnerClassProperty(src, null);
        
        assertEquals(src._delegate, innerClassProperty._delegate);
        assertEquals(null, innerClassProperty._annotated);
        assertEquals(null, innerClassProperty._creator);
    }

    @Test
    public void testConstructorDeserializationWithBrokenJDK() {
        InnerClassProperty src = mock(InnerClassProperty.class);
        AnnotatedConstructor ann = mock(AnnotatedConstructor.class);
        when(ann.getAnnotated()).thenReturn(null);
        
        try {
            new InnerClassProperty(src, ann);
            fail("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Missing constructor (broken JDK (de)serialization?)", e.getMessage());
        }
    }
    
    @Test
    public void testConstructorDeserializationWithJsonDeserializer() {
        InnerClassProperty src = mock(InnerClassProperty.class);
        JsonDeserializer<?> deser = mock(JsonDeserializer.class);
        
        InnerClassProperty innerClassProperty = new InnerClassProperty(src, deser);
        
        assertEquals(src._delegate, innerClassProperty._delegate);
        assertEquals(src._creator, innerClassProperty._creator);
    }
    
    @Test
    public void testConstructorWithPropertyName() {
        InnerClassProperty src = mock(InnerClassProperty.class);
        PropertyName newName = mock(PropertyName.class);
        
        InnerClassProperty innerClassProperty = new InnerClassProperty(src, newName);
        
        assertEquals(src._delegate, innerClassProperty._delegate);
        assertEquals(src._creator, innerClassProperty._creator);
    }
    
    @Test
    public void testWithPropertyName() {
        InnerClassProperty src = mock(InnerClassProperty.class);
        PropertyName newName = mock(PropertyName.class);
        
        InnerClassProperty innerClassProperty = src.withName(newName);
        
        assertEquals(src._delegate.withName(newName), innerClassProperty._delegate);
        assertEquals(src._creator, innerClassProperty._creator);
    }
    
    @Test
    public void testWithValueDeserializer() {
        InnerClassProperty src = mock(InnerClassProperty.class);
        JsonDeserializer<?> deser = mock(JsonDeserializer.class);
        
        InnerClassProperty innerClassProperty = src.withValueDeserializer(deser);
        
        assertEquals(src._delegate.withValueDeserializer(deser), innerClassProperty._delegate);
        assertEquals(src._creator, innerClassProperty._creator);
    }
    
    @Test
    public void testAssignIndex() {
        SettableBeanProperty delegate = mock(SettableBeanProperty.class);
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, null);
        
        innerClassProperty.assignIndex(1);
        
        verify(delegate).assignIndex(1);
    }
    
    @Test
    public void testGetPropertyIndex() {
        SettableBeanProperty delegate = mock(SettableBeanProperty.class);
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, null);
        
        when(delegate.getPropertyIndex()).thenReturn(1);
        
        assertEquals(1, innerClassProperty.getPropertyIndex());
    }
    
    @Test
    public void testGetAnnotation() {
        SettableBeanProperty delegate = mock(SettableBeanProperty.class);
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, null);
        Class<JsonProperty> acls = JsonProperty.class;
        
        when(delegate.getAnnotation(acls)).thenReturn(mock(JsonProperty.class));
        
        assertEquals(delegate.getAnnotation(acls), innerClassProperty.getAnnotation(acls));
    }
    
    @Test
    public void testGetMember() {
        SettableBeanProperty delegate = mock(SettableBeanProperty.class);
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, null);
        AnnotatedMember member = mock(AnnotatedMember.class);
        
        when(delegate.getMember()).thenReturn(member);
        
        assertEquals(delegate.getMember(), innerClassProperty.getMember());
    }
    
    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object bean = mock(Object.class);
        
        SettableBeanProperty delegate = mock(SettableBeanProperty.class);
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, null);
        
        JsonToken t = JsonToken.VALUE_STRING;
        when(jp.getCurrentToken()).thenReturn(t);
        
        Object value = mock(Object.class);
        when(delegate.getNullValue(ctxt)).thenReturn(value);
        
        innerClassProperty.deserializeAndSet(jp, ctxt, bean);
        
        verify(delegate).deserialize(jp, ctxt, value);
    }
    
    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        Object value = mock(Object.class);
        
        SettableBeanProperty delegate = mock(SettableBeanProperty.class);
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, null);
        
        when(delegate.deserialize(jp, ctxt)).thenReturn(value);
        
        assertEquals(value, innerClassProperty.deserializeSetAndReturn(jp, ctxt, instance));
    }
    
    @Test
    public void testSet() throws IOException {
        Object instance = mock(Object.class);
        Object value = mock(Object.class);
        
        SettableBeanProperty delegate = mock(SettableBeanProperty.class);
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, null);
        
        innerClassProperty.set(instance, value);
        
        verify(delegate).set(instance, value);
    }
    
    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = mock(Object.class);
        Object value = mock(Object.class);
        
        SettableBeanProperty delegate = mock(SettableBeanProperty.class);
        InnerClassProperty innerClassProperty = new InnerClassProperty(delegate, null);
        
        when(delegate.setAndReturn(instance, value)).thenReturn(instance);
        
        assertEquals(instance, innerClassProperty.setAndReturn(instance, value));
    }
}
