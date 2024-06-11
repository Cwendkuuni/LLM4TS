import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNodeDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.BeanDeserializer;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.util.NameTransformer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ObjectIdValuePropertyTest {

    private ObjectMapper objectMapper;
    private JsonParser jsonParser;
    private ObjectIdReader objectIdReader;

    @Before
    public void setup() throws Exception {
        objectMapper = new ObjectMapper();
        jsonParser = objectMapper.getFactory().createParser("{}");
        objectIdReader = mock(ObjectIdReader.class);
        when(objectIdReader.propertyName).thenReturn(new PropertyName("id"));
        when(objectIdReader.getIdType()).thenReturn(String.class);
        when(objectIdReader.getDeserializer()).thenReturn(new JsonNodeDeserializer());
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());

        Object id = "12345";
        JsonParser jsonParserSpy = spy(jsonParser);
        DeserializationContext context = mock(DeserializationContext.class);
        Object instance = new Object();

        when(jsonParserSpy.getValueAsToken()).thenReturn(ObjectMapper.ValueType.OBJECT);
        when(jsonParserSpy.readValueAs(String.class)).thenReturn(id);

        objectIdValueProperty.deserializeAndSet(jsonParserSpy, context, instance);

        verify(context, times(1)).findObjectId(id, eq(objectIdReader.generator), eq(objectIdReader.resolver));
        assertEquals("12345", instance.getClass().getDeclaredField("id").get(instance));
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());

        Object id = "12345";
        JsonParser jsonParserSpy = spy(jsonParser);
        DeserializationContext context = mock(DeserializationContext.class);
        Object instance = new Object();

        when(jsonParserSpy.getValueAsToken()).thenReturn(ObjectMapper.ValueType.OBJECT);
        when(jsonParserSpy.readValueAs(String.class)).thenReturn(id);

        Object result = objectIdValueProperty.deserializeSetAndReturn(jsonParserSpy, context, instance);

        verify(context, times(1)).findObjectId(id, eq(objectIdReader.generator), eq(objectIdReader.resolver));
        assertEquals("12345", result);
    }

    @Test
    public void testSetAndReturn() throws Exception {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());

        Object id = "12345";
        Object instance = new Object();

        BeanPropertyWriter idPropertyWriter = mock(BeanPropertyWriter.class);
        when(idPropertyWriter.getName()).thenReturn(new PropertyName("id"));
        when(idPropertyWriter.getSerializer()).thenReturn(mock(PropertyWriter.class));

        ObjectIdValueProperty idValuePropertySpy = spy(objectIdValueProperty);
        when(idValuePropertySpy.getIdProperty()).thenReturn(idPropertyWriter);

        BeanDeserializer beanDeserializer = mock(BeanDeserializer.class);
        when(beanDeserializer.getBeanProperty(anyString())).thenReturn(idValuePropertySpy);

        ObjectMapper objectMapperSpy = spy(objectMapper);
        when(objectMapperSpy.findValueSerializer(eq(String.class))).thenReturn(idPropertyWriter);
        when(objectMapperSpy.getFactory()).thenReturn(new JacksonFactory());

        BeanPropertyWriter nameWriter = mock(BeanPropertyWriter.class);
        when(nameWriter.getName()).thenReturn(new PropertyName("name"));
        when(nameWriter.getSerializer()).thenReturn(mock(PropertyWriter.class));

        ObjectMapper objectMapperContext = mock(ObjectMapper.class);
        when(objectMapperContext.getFactory()).thenReturn(newJacksonFactory());
        when(objectMapperContext.findBeanProperty(eq("id"))).thenReturn(idPropertyWriter);
        when(objectMapperContext.findPropertyName("name")).thenReturn(nameWriter);
        when(objectMapperContext.findValueSerializer(eq(String.class))).thenReturn(nameWriter.getSerializer());

        DeserializationContext context = new DeserializationContext(objectMapperContext, objectMapperSpy, null, null, null, null, null, null, null);

        when(objectMapperSpy.valueInContext(eq(Object.class), eq(context), anyObject())).thenCallRealMethod();

        Object instance = new Object();

        objectIdValueProperty.setAndReturn(instance, id);

        verify(idPropertyWriter, times(1)).set(instance, id);
        assertEquals("12345", instance.getClass().getDeclaredField("id").get(instance));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetWithoutIdProperty() throws Exception {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());

        Object instance = new Object();
        Object id = "12345";

        when(objectMapper.valueInContext(eq(Object.class), any(DeserializationContext.class), anyObject())).thenCallRealMethod();

        objectIdValueProperty.setAndReturn(instance, id);

        fail();
    }

    @Test
    public void testGetAnnotation() throws Exception {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());

        assertNull(objectIdValueProperty.getAnnotation(Object.class));
    }

    @Test
    public void testGetMember() throws Exception {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());

        assertNull(objectIdValueProperty.getMember());
    }
}