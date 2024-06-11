
import com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
import com.fasterxml.jackson.databind.PropertyMetadata;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import org.junit.Test;

import java.io.IOException;
import java.lang.annotation.Annotation;
import com.fasterxml.jackson.core.JsonToken;

public class ObjectIdValuePropertyTest {
    
    @Test
    public void testDeserializeAndSetNull() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = null;
        
        property.deserializeAndSet(p, ctxt, instance);
    }
    
    @Test
    public void testDeserializeAndSetNonNull() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = null;
        
        property.deserializeAndSet(p, ctxt, instance);
    }
    
    @Test
    public void testDeserializeSetAndReturnNull() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = null;
        
        property.deserializeSetAndReturn(p, ctxt, instance);
    }
    
    @Test
    public void testDeserializeSetAndReturnNonNull() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = null;
        
        property.deserializeSetAndReturn(p, ctxt, instance);
    }
    
    @Test
    public void testSetNull() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        
        Object instance = null;
        Object value = null;
        
        property.set(instance, value);
    }
    
    @Test
    public void testSetNonNull() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        
        Object instance = null;
        Object value = null;
        
        property.set(instance, value);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturnNoSettableBeanProperty() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        
        Object instance = null;
        Object value = null;
        
        property.setAndReturn(instance, value);
    }
    
    @Test
    public void testSetAndReturnNonNull() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        
        Object instance = null;
        Object value = null;
        
        property.setAndReturn(instance, value);
    }
    
}
