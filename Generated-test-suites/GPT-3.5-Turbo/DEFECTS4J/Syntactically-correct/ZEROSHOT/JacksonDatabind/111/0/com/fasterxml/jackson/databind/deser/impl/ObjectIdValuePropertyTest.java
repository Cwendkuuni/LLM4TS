import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ObjectIdValuePropertyTest {

    private ObjectIdReader objectIdReader;
    private PropertyMetadata metadata;
    private JsonDeserializer<?> deserializer;
    private NullValueProvider nullValueProvider;
    private JsonParser jsonParser;
    private DeserializationContext deserializationContext;
    private Object instance;
    private ReadableObjectId readableObjectId;
    private SettableBeanProperty idProperty;

    private ObjectIdValueProperty objectIdValueProperty;

    @Before
    public void setUp() {
        objectIdReader = mock(ObjectIdReader.class);
        metadata = mock(PropertyMetadata.class);
        deserializer = mock(JsonDeserializer.class);
        nullValueProvider = mock(NullValueProvider.class);
        jsonParser = mock(JsonParser.class);
        deserializationContext = mock(DeserializationContext.class);
        instance = new Object();
        readableObjectId = mock(ReadableObjectId.class);
        idProperty = mock(SettableBeanProperty.class);

        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
    }

    @Test
    public void testWithNullValueProvider() {
        assertNull(objectIdValueProperty.withNullProvider(nullValueProvider).getNullProvider());
        assertNotNull(objectIdValueProperty.withNullProvider(nullValueProvider)._nullProvider);
    }

    @Test
    public void testWithNewName() {
        PropertyName newName = mock(PropertyName.class);

        assertNotNull(objectIdValueProperty.withName(newName)._propertyName);
    }

    @Test
    public void testDeserializeAndSetWhenValueNull() throws IOException {
        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);

        assertNull(objectIdValueProperty.deserializeAndSet(jsonParser, deserializationContext, instance));
    }

    @Test
    public void testDeserializeAndSetWhenValueNotNull() throws IOException {
        Object id = new Object();
        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        when(objectIdReader.getDeserializer().deserialize(jsonParser, deserializationContext)).thenReturn(id);
        when(deserializationContext.findObjectId(id, objectIdReader.generator, objectIdReader.resolver)).thenReturn(readableObjectId);
        when(idProperty.setAndReturn(instance, id)).thenReturn(instance);

        assertEquals(instance, objectIdValueProperty.deserializeAndSet(jsonParser, deserializationContext, instance));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturnWhenIdPropertyIsNull() throws IOException {
        objectIdValueProperty.setAndReturn(instance, new Object());
    }

    @Test
    public void testSetAndReturnWhenIdPropertyIsNotNull() throws IOException {
        Object id = new Object();
        when(objectIdReader.idProperty).thenReturn(idProperty);
        when(idProperty.setAndReturn(instance, id)).thenReturn(instance);

        assertEquals(instance, objectIdValueProperty.setAndReturn(instance, id));
    }

    @Test
    public void testGetAnnotation() {
        assertNull(objectIdValueProperty.getAnnotation(Object.class));
    }

    @Test
    public void testGetMember() {
        assertNull(objectIdValueProperty.getMember());
    }
}