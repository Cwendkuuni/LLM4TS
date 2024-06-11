import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.lang.annotation.Annotation;

@RunWith(JUnit4.class)
public class ObjectIdValuePropertyTest {
    
    private ObjectMapper objectMapper = new ObjectMapper();
    
    @Test
    public void testWithAnnotation() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyName propertyName = new PropertyName("property");
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
        
        objectIdValueProperty.withName(propertyName);
        
        assertNotNull(objectIdValueProperty.getAnnotation(Annotation.class));
    }
    
    @Test
    public void testGetMember() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
        
        AnnotatedMember member = objectIdValueProperty.getMember();
        
        assertNull(member);
    }
    
    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
        
        JsonParser parser = objectMapper.getFactory().createParser("{}");
        DeserializationContext context = objectMapper.getDeserializationContext();
        Object instance = new Object();
        
        objectIdValueProperty.deserializeAndSet(parser, context, instance);
        
        assertNotNull(instance);
    }
    
    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
        
        JsonParser parser = objectMapper.getFactory().createParser("{}");
        DeserializationContext context = objectMapper.getDeserializationContext();
        Object instance = new Object();
        
        Object result = objectIdValueProperty.deserializeSetAndReturn(parser, context, instance);
        
        assertNotNull(result);
    }
    
    @Test
    public void testSerializeAndReturn() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
        
        Object instance = new Object();
        
        Object result = objectIdValueProperty.serializeAndReturn(objectMapper.getSerializerProvider(), instance);
        
        assertNotNull(result);
    }
    
    @Test
    public void testSet() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
        
        Object instance = new Object();
        Object value = new Object();
        
        objectIdValueProperty.set(instance, value);
        
        assertNotNull(instance);
    }
    
    @Test
    public void testSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
        
        Object instance = new Object();
        Object value = new Object();
        
        Object result = objectIdValueProperty.setAndReturn(instance, value);
        
        assertNotNull(result);
    }
    
}