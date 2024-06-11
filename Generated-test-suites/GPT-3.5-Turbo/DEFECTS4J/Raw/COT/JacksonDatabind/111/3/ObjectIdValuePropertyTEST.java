
import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.annotation.Annotation;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;

public class ObjectIdValuePropertyTest {

    private ObjectIdValueProperty property;
    
    @Before
    public void setUp() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectIdReader objectIdReader = new ObjectIdReader(mapper, new ObjectIdGenerator());
        PropertyMetadata metadata = new PropertyMetadata(null, null, null, null);
        property = new ObjectIdValueProperty(objectIdReader, metadata);
    }

    @Test
    public void testGetName() {
        assertEquals("propertyName", property.getName());
    }

    @Test
    public void testGetType() {
        assertEquals(Object.class, property.getType());
    }

    @Test
    public void testGetMetadata() {
        assertNotNull(property.getMetadata());
    }

    @Test
    public void testGetValueDeserializer() {
        assertNotNull(property.getValueDeserializer());
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider nullValueProvider = new NullValueProvider();
        property.withNullProvider(nullValueProvider);
        assertEquals(nullValueProvider, property.getNullValueProvider());
    }

    @Test
    public void testGetAnnotation() {
        assertNull(property.getAnnotation(TestAnnotation.class));
    }

    @Test
    public void testGetMember() {
        assertNull(property.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser parser = new JsonParser();
        DeserializationContext context = new DeserializationContext();
        Object instance = new Object();
        property.deserializeAndSet(parser, context, instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser parser = new JsonParser();
        DeserializationContext context = new DeserializationContext();
        Object instance = new Object();
        assertNull(property.deserializeSetAndReturn(parser, context, instance));
    }

    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        SettableBeanProperty idProp = new SettableBeanProperty();
        idProp.set("idProp");
        property.set(instance, value);
        assertEquals(value, idProp.get());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturn_noIdProperty() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        property.setAndReturn(instance, value);
    }
    
    @Test
    public void testSetAndReturn_withIdProperty() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        SettableBeanProperty idProp = new SettableBeanProperty();
        idProp.set("idProp");
        property._objectIdReader.idProperty = idProp;
        assertEquals(value, property.setAndReturn(instance, value));
    }
    
    private static class TestAnnotation implements Annotation {}
    
    private static class ObjectIdReader {
        private ObjectMapper mapper;
        private ObjectIdGenerator generator;
        private ObjectIdResolver resolver;
        private SettableBeanProperty idProperty;
        
        public ObjectIdReader(ObjectMapper mapper, ObjectIdGenerator generator) {
            this.mapper = mapper;
            this.generator = generator;
            this.resolver = new ObjectIdResolver();
            this.idProperty = null;
        }
    }
    
    private static class ObjectMapper {
        public ObjectIdReader reader;
        
        public ObjectMapper() {
            this.reader = new ObjectIdReader(this, new ObjectIdGenerator());
        }
    }
    
    private static class ObjectIdGenerator {}
    
    private static class ObjectIdResolver {}
    
    private static class SettableBeanProperty {
        private Object object;
        
        public void set(Object value) {
            this.object = value;
        }
        
        public Object get() {
            return object;
        }
    }
    
    private static class NullValueProvider {}
    
    private static class JsonParser {
        public boolean hasToken(JsonToken token) {
            return false;
        }
    }
    
    private static class DeserializationContext {
        public ReadableObjectId findObjectId(Object id, ObjectIdGenerator generator, ObjectIdResolver resolver) {
            return new ReadableObjectId();
        }
    }
    
    private static class ReadableObjectId {
        public void bindItem(Object instance) {
            // do nothing
        }
    }
    
    private static class PropertyMetadata {}
}
