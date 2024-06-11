import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Constructor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;

public class InnerClassPropertyTest {
    
    private InnerClassProperty innerClassProperty;
    
    @Before
    public void setUp() {
        SettableBeanProperty delegate = new SettableBeanProperty(null, null, null, null);
        Constructor<?> ctor = InnerClassProperty.class.getConstructors()[0];
        innerClassProperty = new InnerClassProperty(delegate, ctor);
    }
    
    @After
    public void tearDown() {
        innerClassProperty = null;
    }
    
    @Test
    public void testWithValidName() {
        PropertyName newName = PropertyName.construct("newName");
        
        InnerClassProperty result = innerClassProperty.withName(newName);
        
        assertEquals(newName, result.getFullName());
    }
    
    @Test
    public void testWithNullName() {
        PropertyName newName = null;
        
        InnerClassProperty result = innerClassProperty.withName(newName);
        
        assertNull(result.getFullName());
    }
    
    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> deser = new JsonDeserializer<Object>() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };
        
        InnerClassProperty result = innerClassProperty.withValueDeserializer(deser);
        
        assertNotNull(result.getValueDeserializer());
    }
    
    @Test
    public void testWithNullValueDeserializer() {
        JsonDeserializer<?> deser = null;
        
        InnerClassProperty result = innerClassProperty.withValueDeserializer(deser);
        
        assertNull(result.getValueDeserializer());
    }
    
    @Test
    public void testAssignIndex() {
        int index = 1;
        
        innerClassProperty.assignIndex(index);
        int result = innerClassProperty.getPropertyIndex();
        
        assertEquals(index, result);
    }
    
    @Test
    public void testAssignNegativeIndex() {
        int index = -1;
        
        innerClassProperty.assignIndex(index);
        int result = innerClassProperty.getPropertyIndex();
        
        assertEquals(index, result);
    }
    
    @Test
    public void testGetAnnotation() {
        Class<JsonFactory.Feature> acls = JsonFactory.Feature.class;
        
        assertNotNull(innerClassProperty.getAnnotation(acls));
    }
    
    @Test
    public void testGetNullAnnotation() {
        Class<JsonParser.Feature> acls = JsonParser.Feature.class;
        
        assertNull(innerClassProperty.getAnnotation(acls));
    }
    
    @Test
    public void testGetMember() {
        assertNotNull(innerClassProperty.getMember());
    }
    
    @Test
    public void testDeserializeAndSetWithNullValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object bean = new Object();
        
        innerClassProperty.deserializeAndSet(null, ctxt, bean);
        
        assertNull(innerClassProperty.get(bean));
    }
    
    @Test
    public void testDeserializeAndSetWithNonNullValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object bean = new Object();
        
        JsonParser jp = mapper.createParser("{}");
        
        innerClassProperty.deserializeAndSet(jp, ctxt, bean);
        
        assertNotNull(innerClassProperty.get(bean));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeAndSetWithException() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object bean = new Object();
        
        // Missing constructor, will throw an exception
        JsonParser jp = mapper.createParser("{}");
        
        innerClassProperty.deserializeAndSet(jp, ctxt, bean);
    }
    
    @Test
    public void testDeserializeSetAndReturnWithNullValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object instance = new Object();
        
        Object result = innerClassProperty.deserializeSetAndReturn(null, ctxt, instance);
        
        assertNull(result);
    }
    
    @Test
    public void testDeserializeSetAndReturnWithNonNullValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object instance = new Object();
        
        JsonParser jp = mapper.createParser("{}");
        
        Object result = innerClassProperty.deserializeSetAndReturn(jp, ctxt, instance);
        
        assertNotNull(result);
    }
    
    @Test
    public void testSetWithNullInstance() throws IOException {
        Object value = new Object();
        
        innerClassProperty.set(null, value);
        
        // No assertion as the method should not throw any exception
    }
    
    @Test
    public void testSetWithNonNullInstance() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        
        innerClassProperty.set(instance, value);
        
        assertEquals(value, innerClassProperty.get(instance));
    }
    
    @Test
    public void testSetAndReturnWithNullInstance() throws IOException {
        Object value = new Object();
        
        Object result = innerClassProperty.setAndReturn(null, value);
        
        assertNull(result);
    }
    
    @Test
    public void testSetAndReturnWithNonNullInstance() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        
        Object result = innerClassProperty.setAndReturn(instance, value);
        
        assertEquals(value, result);
    }
    
    @Test
    public void testReadResolveWithNotNullAnnotated() {
        Object result = innerClassProperty.readResolve();
        
        assertNotNull(result);
    }
    
    @Test
    public void testReadResolveWithNullAnnotated() {
        innerClassProperty = new InnerClassProperty(innerClassProperty, null);
        
        Object result = innerClassProperty.readResolve();
        
        assertNotNull(result);
    }
    
    @Test
    public void testWriteReplaceWithNullAnnotated() {
        Object result = innerClassProperty.writeReplace();
        
        assertNotNull(result);
    }
    
    @Test
    public void testWriteReplaceWithNotNullAnnotated() {
        innerClassProperty = new InnerClassProperty(innerClassProperty, new AnnotatedConstructor(null, null, null, null));
        
        Object result = innerClassProperty.writeReplace();
        
        assertNotNull(result);
    }
    
}