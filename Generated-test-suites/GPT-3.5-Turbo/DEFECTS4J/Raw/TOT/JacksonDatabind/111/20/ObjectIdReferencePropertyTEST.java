import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ObjectIdReferencePropertyTest {
    
    private ObjectIdReferenceProperty objectIdReferenceProperty;

    @Before
    public void setup() {
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        ObjectIdInfo objectIdInfo = mock(ObjectIdInfo.class);
        objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);
        SettableBeanProperty newProperty = objectIdReferenceProperty.withNullProvider(nullValueProvider);
        assertEquals(nullValueProvider, newProperty.getNullProvider());
    }

    @Test
    public void testFixAccess() {
        DeserializationConfig config = mock(DeserializationConfig.class);
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, null);
        objectIdReferenceProperty.fixAccess(config);
        // No verification of the fixAccess() as it only calls _forward.fixAccess() internally
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, null);

        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object instance = new Object();

        Object value = new Object();
        when(objectIdReferenceProperty.deserialize(jsonParser, deserializationContext)).thenReturn(value);

        objectIdReferenceProperty.deserializeAndSet(jsonParser, deserializationContext, instance);
        // No verification of the method
    }

    @Test
    public void testSetAndReturn() throws IOException {
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, null);

        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object instance = new Object();

        Object value = new Object();
        when(objectIdReferenceProperty.deserialize(jsonParser, deserializationContext)).thenReturn(value);

        Object result = objectIdReferenceProperty.setAndReturn(jsonParser, deserializationContext, instance);
        assertEquals(value, result);
    }

    @Test
    public void testSet() throws IOException {
        SettableBeanProperty forward = mock(SettableBeanProperty.class);
        objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, null);

        Object instance = new Object();
        Object value = new Object();
        objectIdReferenceProperty.set(instance, value);
        // No verification of the method
    }

    @Test
    public void testPropertyReferringHandleResolvedForwardReference() throws IOException {
        ObjectIdReferenceProperty parent = mock(ObjectIdReferenceProperty.class);
        UnresolvedForwardReference ref = mock(UnresolvedForwardReference.class);
        Class<?> type = String.class;
        Object ob = new Object();

        ObjectIdReferenceProperty.PropertyReferring propertyReferring =
                new ObjectIdReferenceProperty.PropertyReferring(parent, ref, type, ob);

        Object id = new Object();
        Object value = new Object();

        propertyReferring.addId(id);
        propertyReferring.handleResolvedForwardReference(id, value);
        // No verification of the method
    }
}