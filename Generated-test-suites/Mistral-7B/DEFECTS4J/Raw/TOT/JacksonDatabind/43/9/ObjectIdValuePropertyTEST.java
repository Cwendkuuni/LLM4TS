import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.std.ObjectIdReaderForString;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.databind.util.NameTransformer;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test cases for ObjectIdValueProperty class
 */
public class ObjectIdValuePropertyTest {

    private ObjectMapper mapper;
    private JsonFactory jsonFactory;
    private ObjectIdReader objectIdReader;

    @Before
    public void setup() throws Exception {
        jsonFactory = new JsonFactory();
        mapper = new ObjectMapper(jsonFactory);
        ObjectIdReaderForString stringObjectIdReader = new ObjectIdReaderForString();
        objectIdReader = new ObjectIdReader(null, stringObjectIdReader, null, null);
    }

    @Test
    public void testDeserializeAndSetNullId() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);

        when(parser.getValueAsToken()).thenReturn(null);

        Object instance = new Object();
        property.deserializeAndSet(parser, context, instance);

        verify(context).findObjectId(eq(null), any(), any());
        assertEquals(instance, property.getLeafProperty().get(instance));
        assertNull(property.getLeafProperty().get(instance).getValue());
    }

    @Test
    public void testDeserializeAndSetValidId() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object id = "123";
        String idString = "id: " + id;
        when(parser.getValueAsString()).thenReturn(idString);

        Object instance = new Object();
        property.deserializeAndSet(parser, context, instance);

        ReadableObjectId roid = verify(context).findObjectId(eq(id), any(), any());
        roid.expects(once()).method("getIdType").andReturn(ObjectIdType.STRING);
        roid.expects(once()).method("getName").andReturn("id");
        roid.expects(once()).method("getId").andReturn(id);
        roid.expects(once()).method("bindItem").andReturns(instance);

        ObjectIdProperty idProperty = mock(ObjectIdProperty.class);
        when(context.findPropertyByName("id")).thenReturn(idProperty);
        when(idProperty.setAndReturn(instance, id)).thenReturn(instance);

        assertEquals(instance, property.getLeafProperty().get(instance));
        assertEquals(id, property.getLeafProperty().get(instance).getValue());
    }

    @Test
    public void testSetAndReturnNullId() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        Object instance = new Object();

        Object newId = null;
        Object result = property.setAndReturn(instance, newId);

        assertSame(instance, result);
        assertNull(property.getLeafProperty().get(instance).getValue());
    }

    @Test
    public void testSetAndReturnValidId() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        Object instance = new Object();
        Object id = "123";

        SettableBeanProperty idProperty = mock(SettableBeanProperty.class);
        when(idProperty.setAndReturn(instance, id)).thenReturn(instance);

        BeanPropertyDefinition propertyDefinition = mock(BeanPropertyDefinition.class);
        when(propertyDefinition.getName()).thenReturn("id");
        when(propertyDefinition.getSerializationVisibility()).thenReturn(JsonAutoDetect.Visibility.NONE);
        when(propertyDefinition.getSetterInfo()).thenReturn(new AnnotatedMethod(idProperty.getClass(), "set", new Class[0]));
        when(idProperty.getMember()).thenReturn(new AnnotatedMember(propertyDefinition));
        when(propertyDefinition.hasAnnotation(JsonProperty.class)).thenReturn(true);
        when(propertyDefinition.getAnnotation(JsonProperty.class)).thenReturn(new JsonProperty(JsonProperty.Value.class, "id"));

        ObjectIdProperty idPropertyWrapper = new ObjectIdProperty(propertyDefinition, null, idProperty, null, null);
        when(property.getLeafProperty()).thenReturn(idPropertyWrapper);

        when(propertyDefinition.getSerializationVisibility()).thenCallRealMethod();
        Object result = property.setAndReturn(instance, id);

        assertSame(instance, result);
        assertEquals(id, property.getLeafProperty().get(instance).getValue());
    }
}