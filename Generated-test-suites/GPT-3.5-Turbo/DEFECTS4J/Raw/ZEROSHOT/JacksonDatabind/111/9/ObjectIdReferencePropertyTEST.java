import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.annotation.Annotation;

import static org.mockito.Mockito.*;

public class ObjectIdReferencePropertyTest {

    private SettableBeanProperty forward;
    private ObjectIdInfo objectIdInfo;
    private JsonDeserializer<?> deser;
    private NullValueProvider nva;
    private ObjectIdReferenceProperty property;

    @Before
    public void setup() {
        forward = mock(SettableBeanProperty.class);
        objectIdInfo = mock(ObjectIdInfo.class);
        deser = mock(JsonDeserializer.class);
        nva = mock(NullValueProvider.class);
        property = new ObjectIdReferenceProperty(forward, objectIdInfo);
    }

    @Test
    public void testWithName() {
        PropertyName newName = mock(PropertyName.class);
        SettableBeanProperty newProperty = mock(SettableBeanProperty.class);

        when(forward.getAnnotation(any(Class.class))).thenReturn(mock(Annotation.class));
        when(forward.getMember()).thenReturn(mock(AnnotatedMember.class));
        when(forward.getCreatorIndex()).thenReturn(anyInt());
        when(forward.withName(newName)).thenReturn(newProperty);

        SettableBeanProperty result = property.withName(newName);

        assert result == newProperty;
        verify(forward, times(1)).getAnnotation(any(Class.class));
        verify(forward, times(1)).getMember();
        verify(forward, times(1)).getCreatorIndex();
        verify(forward, times(1)).withName(newName);
    }

    @Test
    public void testWithValueDeserializer() {
        when(property.getValueDeserializer()).thenReturn(deser);
        when(property.getNullProvider()).thenReturn(nva);
        when(property.withValueDeserializer(deser)).thenCallRealMethod();

        SettableBeanProperty result = property.withValueDeserializer(deser);

        assert result != property;
        verify(property, times(1)).getValueDeserializer();
        verify(property, times(1)).getNullProvider();
    }

    @Test
    public void testWithNullProvider() {
        when(property.getValueDeserializer()).thenReturn(deser);
        when(property.withNullProvider(nva)).thenCallRealMethod();

        SettableBeanProperty result = property.withNullProvider(nva);

        assert result != property;
        verify(property, times(1)).getValueDeserializer();
    }

    @Test
    public void testFixAccess() {
        when(forward != null).thenReturn(true);
        when(forward.fixAccess(any(DeserializationConfig.class))).thenCallRealMethod();
        DeserializationConfig config = mock(DeserializationConfig.class);

        property.fixAccess(config);

        verify(forward, times(1)).fixAccess(config);
    }

    @Test
    public void testGetAnnotation() {
        Class<A> acls = mock(Class.class);

        property.getAnnotation(acls);

        verify(forward, times(1)).getAnnotation(acls);
    }

    @Test
    public void testGetMember() {
        property.getMember();

        verify(forward, times(1)).getMember();
    }

    @Test
    public void testGetCreatorIndex() {
        property.getCreatorIndex();

        verify(forward, times(1)).getCreatorIndex();
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();

        property.deserializeAndSet(p, ctxt, instance);

        verify(property, times(1)).deserializeSetAndReturn(p, ctxt, instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        UnresolvedForwardReference reference = mock(UnresolvedForwardReference.class);
        ReadableObjectId roid = mock(ReadableObjectId.class);

        when(property.deserialize(p, ctxt)).thenReturn(new Object());
        when(reference.getRoid()).thenReturn(roid);
        when(reference.getRoid().appendReferring(any(PropertyReferring.class))).thenReturn(null);

        property.deserializeSetAndReturn(p, ctxt, instance);

        verify(property, times(1)).setAndReturn(instance, any(Object.class));
        verify(reference, times(1)).getRoid();
        verify(reference.getRoid(), times(1)).appendReferring(any(PropertyReferring.class));
    }

    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Object value = new Object();

        property.set(instance, value);

        verify(forward, times(1)).set(instance, value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        Object value = new Object();

        property.setAndReturn(instance, value);

        verify(forward, times(1)).setAndReturn(instance, value);
    }

    public final static class PropertyReferring extends Referring {
        private final ObjectIdReferenceProperty _parent;
        public final Object _pojo;

        public PropertyReferring(ObjectIdReferenceProperty parent,
                                 UnresolvedForwardReference ref, Class<?> type, Object ob)
        {
            super(ref, type);
            _parent = parent;
            _pojo = ob;
        }

        @Override
        public void handleResolvedForwardReference(Object id, Object value) throws IOException
        {
            if (!hasId(id)) {
                throw new IllegalArgumentException("Trying to resolve a forward reference with id [" + id
                        + "] that wasn't previously seen as unresolved.");
            }
            _parent.set(_pojo, value);
        }
    }
}

