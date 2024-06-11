import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class ObjectIdReferencePropertyTest {

    private ObjectIdReferenceProperty objectIdReferenceProperty;

    @Mock
    private SettableBeanProperty forward;

    @Mock
    private PropertyName propertyName;
    
    @Mock
    private JsonDeserializer jsonDeserializer;

    @Mock
    private NullValueProvider nullValueProvider;

    @Mock
    private ObjectIdInfo objectIdInfo;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);
    }

    @Test
    public void testWithName() {
        when(forward.withName(propertyName)).thenReturn(forward);
        SettableBeanProperty result = objectIdReferenceProperty.withName(propertyName);
        verify(forward, times(1)).withName(propertyName);
    }

    @Test
    public void testWithValueDeserializer() {
        when(forward.withValueDeserializer(jsonDeserializer)).thenReturn(forward);
        SettableBeanProperty result = objectIdReferenceProperty.withValueDeserializer(jsonDeserializer);
        verify(forward, times(1)).withValueDeserializer(jsonDeserializer);
    }

    @Test
    public void testWithNullProvider() {
        when(forward.withNullProvider(nullValueProvider)).thenReturn(forward);
        SettableBeanProperty result = objectIdReferenceProperty.withNullProvider(nullValueProvider);
        verify(forward, times(1)).withNullProvider(nullValueProvider);
    }

    @Test
    public void testFixAccess() {
        objectIdReferenceProperty.fixAccess(null);
        verify(forward, times(1)).fixAccess(null);
    }

    @Test
    public void testGetAnnotation() {
        Class<A> acls = A.class;
        A expected = mock(A.class);
        when(forward.getAnnotation(acls)).thenReturn(expected);
        A result = objectIdReferenceProperty.getAnnotation(acls);
        assertEquals(expected, result);
    }

    @Test
    public void testGetMember() {
        AnnotatedMember expected = mock(AnnotatedMember.class);
        when(forward.getMember()).thenReturn(expected);
        AnnotatedMember result = objectIdReferenceProperty.getMember();
        assertEquals(expected, result);
    }

    @Test
    public void testGetCreatorIndex() {
        int expected = 1;
        when(forward.getCreatorIndex()).thenReturn(expected);
        int result = objectIdReferenceProperty.getCreatorIndex();
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object instance = new Object();
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        JsonDeserializer<?> jsonDeserializer = mock(JsonDeserializer.class);
        when(objectIdReferenceProperty.deserialize(jsonParser, deserializationContext)).thenReturn(new Object());
        objectIdReferenceProperty.deserializeAndSet(jsonParser, deserializationContext, instance);
        verify(objectIdReferenceProperty, times(1)).setAndReturn(instance, any());
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object instance = new Object();
        UnresolvedForwardReference unresolvedForwardReference = mock(UnresolvedForwardReference.class);
        ObjectIdReferenceProperty.PropertyReferring propertyReferring = mock(ObjectIdReferenceProperty.PropertyReferring.class);
        when(unresolvedForwardReference.getRoid()).thenReturn(propertyReferring);
        when(objectIdReferenceProperty.deserializeandSet(jsonParser, deserializationContext, instance)).thenThrow(new UnresolvedForwardReference(null, null, null));
        when(objectIdReferenceProperty._objectIdInfo).thenReturn(objectIdInfo);
        when(objectIdReferenceProperty._valueDeserializer.getObjectIdReader()).thenReturn(null);
        when(propertyReferring.hasId(any())).thenReturn(true);
        when(objectIdReferenceProperty.setAndReturn(instance, null)).thenReturn(null);
        objectIdReferenceProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);
        verify(propertyReferring, times(1)).handleResolvedForwardReference(any(), any());
    }

    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        objectIdReferenceProperty.set(instance, value);
        verify(forward, times(1)).set(instance, value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        when(forward.setAndReturn(instance, value)).thenReturn(value);
        Object result = objectIdReferenceProperty.setAndReturn(instance, value);
        assertEquals(value, result);
    }

    @Test
    public void testPropertyReferringHandleResolvedForwardReference() {
        ObjectIdReferenceProperty.PropertyReferring propertyReferring = new ObjectIdReferenceProperty.PropertyReferring(null, null, null, null);
        propertyReferring.handleResolvedForwardReference(null, null);
    }
}