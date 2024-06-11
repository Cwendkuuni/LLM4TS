import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyMetadata;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty;
import com.fasterxml.jackson.databind.deser.std.StdDeSerializable;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class ObjectIdValuePropertyTest {

    @Test
    public void testDeserializeAndSet() {
        try {
            ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null);
            PropertyMetadata metadata = new PropertyMetadata(null, null, null);
            ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
            
            String json = "{\"field1\": \"value1\", \"field2\": \"value2\"}";
            ObjectMapper objectMapper = new ObjectMapper();
            JsonFactory jsonFactory = new JsonFactory();
            JsonParser jsonParser = jsonFactory.createParser(json);
            DeserializationContext deserializationContext = objectMapper.getDeserializationContext();
            
            Object instance = new Object();
            property.deserializeAndSet(jsonParser, deserializationContext, instance);
            
            // TODO: Add assertion
            
        } catch (IOException e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testDeserializeSetAndReturn() {
        try {
            ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null);
            PropertyMetadata metadata = new PropertyMetadata(null, null, null);
            ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
            
            String json = "{\"field1\": \"value1\", \"field2\": \"value2\"}";
            ObjectMapper objectMapper = new ObjectMapper();
            JsonFactory jsonFactory = new JsonFactory();
            JsonParser jsonParser = jsonFactory.createParser(json);
            DeserializationContext deserializationContext = objectMapper.getDeserializationContext();
            
            Object instance = new Object();
            Object result = property.deserializeSetAndReturn(jsonParser, deserializationContext, instance);
            
            // TODO: Add assertion
            
        } catch (IOException e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testSet() {
        try {
            ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null);
            PropertyMetadata metadata = new PropertyMetadata(null, null, null);
            ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
            
            Object instance = new Object();
            Object value = new Object();
            property.set(instance, value);
            
            // TODO: Add assertion
            
        } catch (IOException e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testSetAndReturn() {
        try {
            ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null);
            PropertyMetadata metadata = new PropertyMetadata(null, null, null);
            ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
            
            Object instance = new Object();
            Object value = new Object();
            Object result = property.setAndReturn(instance, value);
            
            // TODO: Add assertion
            
        } catch (IOException e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testGetName() {
        try {
            ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null);
            PropertyMetadata metadata = new PropertyMetadata(null, null, null);
            ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
            
            // TODO: Add assertion
            
        } catch (IOException e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testWithValueDeserializer() {
        try {
            ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null);
            PropertyMetadata metadata = new PropertyMetadata(null, null, null);
            ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
            
            // TODO: Add assertion
            
        } catch (IOException e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }

}