import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;

public class ObjectIdReferencePropertyTest {

    private ObjectIdReferenceProperty objectIdReferenceProperty;
    private SettableBeanProperty forward;
    private ObjectIdInfo objectIdInfo;
    private JsonDeserializer<?> deserializer;
    private NullValueProvider nullValueProvider;
    private PropertyName propertyName;

    @Before
    public void setUp() {
        forward = mock(SettableBeanProperty.class);
        objectIdInfo = mock(ObjectIdInfo.class);
        deserializer = mock(JsonDeserializer.class);
        nullValueProvider = mock(NullValueProvider.class);
        propertyName = mock(PropertyName.class);

        objectIdReferenceProperty = new ObjectIdReferenceProperty(forward, objectIdInfo);

}

    @Test
    public void testWithNullProvider_WhenNullProviderIsNotNull_ShouldReturnNewInstance() {
        ObjectIdReferenceProperty result = objectIdReferenceProperty.withNullProvider(nullValueProvider);

        assertEquals(nullValueProvider, result.getNullValueProvider());
    }

    @Test
    public void testWithNullProvider_WhenNullProviderIsNull_ShouldReturnNewInstanceWithNullProviderParam() {
        ObjectIdReferenceProperty result = objectIdReferenceProperty.withNullProvider(null);

        assertNull(result.getNullValueProvider());
    }

    @Test
    public void testWithNullProvider_WhenNullProviderEqualsNull_ShouldReturnSameInstance() {
        ObjectIdReferenceProperty result = objectIdReferenceProperty.withNullProvider(objectIdReferenceProperty.getNullValueProvider());

        assertEquals(objectIdReferenceProperty, result);
    }

    @Test
    public void testFixAccess_WhenForwardIsNotNull_ShouldInvokeFixAccessOnForward() {
        DeserializationConfig config = mock(DeserializationConfig.class);

        objectIdReferenceProperty.fixAccess(config);

        verify(forward, times(1)).fixAccess(config);
    }

    @Test
    public void testFixAccess_WhenForwardIsNull_ShouldNotInvokeFixAccessOnForward() {
        objectIdReferenceProperty = new ObjectIdReferenceProperty(null, objectIdInfo);
        DeserializationConfig config = mock(DeserializationConfig.class);

        objectIdReferenceProperty.fixAccess(config);

        verify(forward, never()).fixAccess(config);
    }

    @Test
    public void testGetAnnotation_ShouldInvokeGetAnnotationOnForwardAndReturnAnnotation() {
        @SuppressWarnings("unchecked")
        Class<AnnotatedMember> acls = mock(Class.class);
        AnnotatedMember annotation = mock(AnnotatedMember.class);
        when(forward.getAnnotation(acls)).thenReturn(annotation);

        AnnotatedMember result = objectIdReferenceProperty.getAnnotation(acls);

        verify(forward, times(1)).getAnnotation(acls);
        assertEquals(annotation, result);
    }

    @Test
    public void testGetMember_ShouldInvokeGetMemberOnForwardAndReturnMember() {
        AnnotatedMember member = mock(AnnotatedMember.class);
        when(forward.getMember()).thenReturn(member);

        AnnotatedMember result = objectIdReferenceProperty.getMember();

        verify(forward, times(1)).getMember();
        assertEquals(member, result);
    }

    @Test
    public void testGetCreatorIndex_ShouldInvokeGetCreatorIndexOnForwardAndReturnIndex() {
        int creatorIndex = 5;
        when(forward.getCreatorIndex()).thenReturn(creatorIndex);

        int result = objectIdReferenceProperty.getCreatorIndex();

        verify(forward, times(1)).getCreatorIndex();
        assertEquals(creatorIndex, result);
    }

    @Test
    public void testDeserializeAndSet_ShouldInvokeDeserializeSetAndReturn() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object instance = mock(Object.class);

        objectIdReferenceProperty.deserializeAndSet(parser, context, instance);

        verify(forward, times(1)).deserializeSetAndReturn(parser, context, instance);
    }

    @Test
    public void testDeserializeSetAndReturn_WhenNoUnresolvedForwardRef_ShouldInvokeSetAndReturnOnForward() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        Object value = mock(Object.class);
        when(forward.deserialize(parser, context)).thenReturn(value);

        Object result = objectIdReferenceProperty.deserializeSetAndReturn(parser, context, instance);

        verify(forward, times(1)).deserialize(parser, context);
        verify(forward, times(1)).setAndReturn(instance, value);
        assertEquals(value, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeSetAndReturn_WhenIdentityInfoIsNull_ShouldThrowJsonMappingException() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        UnresolvedForwardReference reference = mock(UnresolvedForwardReference.class);
        when(forward.deserialize(parser, context)).thenThrow(reference);

        objectIdReferenceProperty.deserializeSetAndReturn(parser, context, instance);
    }

    @Test
    public void testDeserializeSetAndReturn_WhenIdentityInfoIsNotNull_ShouldNotThrowJsonMappingException() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        UnresolvedForwardReference reference = mock(UnresolvedForwardReference.class);
        when(forward.deserialize(parser, context)).thenThrow(reference);
        when(forward.getRawType()).thenReturn(Object.class);
        when(reference.getRoid()).thenReturn(mock(Referring.class));

        assertNull(objectIdReferenceProperty.deserializeSetAndReturn(parser, context, instance));
    }

    @Test
    public void testSet_ShouldInvokeSetOnForward() throws IOException {
        Object instance = mock(Object.class);
        Object value = mock(Object.class);

        objectIdReferenceProperty.set(instance, value);

        verify(forward, times(1)).set(instance, value);
    }

    @Test
    public void testSetAndReturn_ShouldInvokeSetAndReturnOnForward() throws IOException {
        Object instance = mock(Object.class);
        Object value = mock(Object.class);
        when(forward.setAndReturn(instance, value)).thenReturn(value);

        Object result = objectIdReferenceProperty.setAndReturn(instance, value);

        verify(forward, times(1)).setAndReturn(instance, value);
        assertEquals(value, result);
    }

    @Test
    public void testPropertyReferringHandleResolvedForwardReference_WhenHasId_ShouldInvokeSetOnParent() throws IOException {
        ObjectIdReferenceProperty.PropertyReferring propertyReferring = new ObjectIdReferenceProperty.PropertyReferring(objectIdReferenceProperty, mock(UnresolvedForwardReference.class), Object.class, mock(Object.class));
        Object id = mock(Object.class);
        Object value = mock(Object.class);
        propertyReferring.addId(id);

        propertyReferring.handleResolvedForwardReference(id, value);

        verify(objectIdReferenceProperty, times(1)).set(propertyReferring._pojo, value);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testPropertyReferringHandleResolvedForwardReference_WhenDoesNotHaveId_ShouldThrowIllegalArgumentException() throws IOException {
        ObjectIdReferenceProperty.PropertyReferring propertyReferring = new ObjectIdReferenceProperty.PropertyReferring(objectIdReferenceProperty, mock(UnresolvedForwardReference.class), Object.class, mock(Object.class));
        Object id = mock(Object.class);
        Object value = mock(Object.class);

        propertyReferring.handleResolvedForwardReference(id, value);
    }

}