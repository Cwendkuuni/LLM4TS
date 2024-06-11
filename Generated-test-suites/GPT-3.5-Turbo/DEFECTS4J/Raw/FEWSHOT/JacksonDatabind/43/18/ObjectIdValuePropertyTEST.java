import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class ObjectIdValuePropertyTest {
    private ObjectIdReader objectIdReader;
    private PropertyName propertyName;
    private PropertyMetadata metadata;
    private JsonDeserializer<?> deserializer;
    
    @Before
    public void setUp() {
        objectIdReader = mock(ObjectIdReader.class);
        propertyName = mock(PropertyName.class);
        metadata = mock(PropertyMetadata.class);
        deserializer = new StdDeserializer<Object>(ObjectIdValueProperty.class) {
            private static final long serialVersionUID = 1L;
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt)
                    throws IOException {
                return null;
            }
        };
    }
    
    @Test
    public void constructorTest() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        assertEquals(objectIdReader, property._objectIdReader);
    }
    
    @Test
    public void constructorWithDeserializerTest() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, deserializer, metadata);
        assertEquals(objectIdReader, property._objectIdReader);
        assertEquals(deserializer, property._valueDeserializer);
    }
    
    @Test
    public void constructorWithNewNameTest() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, propertyName);
        assertEquals(objectIdReader, property._objectIdReader);
        assertEquals(propertyName, property.getFullName());
    }
    
    @Test
    public void withNameTest() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        PropertyName newName = mock(PropertyName.class);
        ObjectIdValueProperty newProperty = property.withName(newName);
        assertEquals(objectIdReader, newProperty._objectIdReader);
        assertEquals(newName, newProperty.getFullName());
    }
    
    @Test
    public void withValueDeserializerTest() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        JsonDeserializer<?> newDeserializer = mock(JsonDeserializer.class);
        ObjectIdValueProperty newProperty = property.withValueDeserializer(newDeserializer);
        assertEquals(objectIdReader, newProperty._objectIdReader);
        assertEquals(newDeserializer, newProperty._valueDeserializer);
    }
    
    @Test
    public void getAnnotationTest() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        assertNull(property.getAnnotation(Annotation.class));
    }
    
    @Test
    public void getMemberTest() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        assertNull(property.getMember());
    }
    
    @Test
    public void deserializeAndSetTest() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object instance = new Object();
        property.deserializeAndSet(parser, context, instance);
    }
    
    @Test
    public void deserializeSetAndReturnTest() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object instance = new Object();
        property.deserializeSetAndReturn(parser, context, instance);
    }
    
    @Test
    public void setTest() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        Object instance = new Object();
        Object value = new Object();
        property.set(instance, value);
    }
    
    @Test
    public void setAndReturnTest() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        Object instance = new Object();
        Object value = new Object();
        property.setAndReturn(instance, value);
    }
}