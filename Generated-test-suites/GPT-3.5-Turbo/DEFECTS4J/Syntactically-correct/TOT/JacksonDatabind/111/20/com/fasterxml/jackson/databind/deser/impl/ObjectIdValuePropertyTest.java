import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty;
import com.fasterxml.jackson.databind.PropertyMetadata;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.PropertyName;
import java.io.IOException;
import java.lang.annotation.Annotation;

public class ObjectIdValuePropertyTest {
    
    @Test
    public void testConstructor() {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null);
        ObjectIdValueProperty objIdValueProp = new ObjectIdValueProperty(objectIdReader, metadata);
        
        assertNotNull(objIdValueProp);
        assertEquals(objectIdReader, objIdValueProp._objectIdReader);
    }
    
    @Test
    public void testWithNullProvider() {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null);
        ObjectIdValueProperty objIdValueProp = new ObjectIdValueProperty(objectIdReader, metadata);
        
        NullValueProvider nva = new NullValueProvider() {
            @Override
            public Object getNullValue(DeserializationContext ctxt) throws JsonMappingException {
                return null;
            }
        };
        objIdValueProp = objIdValueProp.withNullProvider(nva);
        
        assertNotNull(objIdValueProp);
        assertEquals(nva, objIdValueProp._nullProvider);
    }
    
    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null,null,null);
        ObjectIdValueProperty objIdValueProp = new ObjectIdValueProperty(objectIdReader, metadata);
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser parser = jsonFactory.createParser("{\"id\":123}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object instance = new Object();
        
        try {
            objIdValueProp.deserializeAndSet(parser, ctxt, instance);
        } catch(IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null,null,null);
        ObjectIdValueProperty objIdValueProp = new ObjectIdValueProperty(objectIdReader, metadata);
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser parser = jsonFactory.createParser("{\"id\":123}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object instance = new Object();
        
        try {
            objIdValueProp.deserializeSetAndReturn(parser, ctxt, instance);
        } catch(IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testSet() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null,null,null);
        ObjectIdValueProperty objIdValueProp = new ObjectIdValueProperty(objectIdReader, metadata);
        Object instance = new Object();
        Object value = new Object();
        
        try {
            objIdValueProp.set(instance, value);
        } catch(IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null,null,null);
        ObjectIdValueProperty objIdValueProp = new ObjectIdValueProperty(objectIdReader, metadata);
        Object instance = new Object();
        Object value = new Object();
        
        try {
            objIdValueProp.setAndReturn(instance, value);
        } catch(IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testGetAnnotation() {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null,null,null);
        ObjectIdValueProperty objIdValueProp = new ObjectIdValueProperty(objectIdReader, metadata);
        Class<Annotation> annotationClass = Annotation.class;
        
        assertNull(objIdValueProp.getAnnotation(annotationClass));
    }
    
    @Test
    public void testGetMember() {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null,null,null);
        ObjectIdValueProperty objIdValueProp = new ObjectIdValueProperty(objectIdReader, metadata);
        
        assertNull(objIdValueProp.getMember());
    }
    
    @Test
    public void testWithName() {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null,null,null);
        ObjectIdValueProperty objIdValueProp = new ObjectIdValueProperty(objectIdReader, metadata);
        PropertyName newName = new PropertyName("newName");
        
        SettableBeanProperty newBeanProp = objIdValueProp.withName(newName);
        assertNotNull(newBeanProp);
        assertEquals(newName, newBeanProp.getFullName());
    }
    
    @Test
    public void testWithValueDeserializer() {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null,null,null);
        ObjectIdValueProperty objIdValueProp = new ObjectIdValueProperty(objectIdReader, metadata);
        JsonDeserializer<?> newDeserializer = new JsonDeserializer<>() {
            @Override
            public JsonNode deserialize(JsonParser p, DeserializationContext ctxt) {
                return null;
            }
        };
        
        SettableBeanProperty newBeanProp = objIdValueProp.withValueDeserializer(newDeserializer);
        assertNotNull(newBeanProp);
        assertEquals(newDeserializer, newBeanProp.getValueDeserializer());
    }
    
}