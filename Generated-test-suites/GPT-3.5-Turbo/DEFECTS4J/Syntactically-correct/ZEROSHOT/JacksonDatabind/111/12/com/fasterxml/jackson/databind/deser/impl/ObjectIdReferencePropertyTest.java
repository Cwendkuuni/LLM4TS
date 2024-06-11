import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ObjectIdReferencePropertyTest {

    @Test
    public void testWithName() {
        ObjectIdReferenceProperty property = mock(ObjectIdReferenceProperty.class);
        PropertyName newName = mock(PropertyName.class);

        ObjectIdReferenceProperty result = property.withName(newName);

        assertEquals(newName, result.getFullName());
        verify(property).withName(newName);
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectIdReferenceProperty property = mock(ObjectIdReferenceProperty.class);
        JsonDeserializer<?> deser = mock(JsonDeserializer.class);

        ObjectIdReferenceProperty result = property.withValueDeserializer(deser);

        assertEquals(deser, result.getValueDeserializer());
        verify(property).withValueDeserializer(deser);
    }

    @Test
    public void testWithNullProvider() {
        ObjectIdReferenceProperty property = mock(ObjectIdReferenceProperty.class);
        NullValueProvider nva = mock(NullValueProvider.class);

        ObjectIdReferenceProperty result = property.withNullProvider(nva);

        assertEquals(nva, result.getNullValueProvider());
        verify(property).withNullProvider(nva);
    }

    @Test
    public void testFixAccess() {
        ObjectIdReferenceProperty property = mock(ObjectIdReferenceProperty.class);
        DeserializationConfig config = mock(DeserializationConfig.class);

        property.fixAccess(config);

        verify(property).fixAccess(config);
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdReferenceProperty property = mock(ObjectIdReferenceProperty.class);
        Class<A> acls = mock(Class.class);
        A annotation = mock(A.class);

        when(property.getAnnotation(acls)).thenReturn(annotation);

        A result = property.getAnnotation(acls);

        assertEquals(annotation, result);
        verify(property).getAnnotation(acls);
    }

    @Test
    public void testGetMember() {
        ObjectIdReferenceProperty property = mock(ObjectIdReferenceProperty.class);
        AnnotatedMember member = mock(AnnotatedMember.class);

        when(property.getMember()).thenReturn(member);

        AnnotatedMember result = property.getMember();

        assertEquals(member, result);
        verify(property).getMember();
    }

    @Test
    public void testGetCreatorIndex() {
        ObjectIdReferenceProperty property = mock(ObjectIdReferenceProperty.class);

        property.getCreatorIndex();

        verify(property).getCreatorIndex();
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdReferenceProperty property = mock(ObjectIdReferenceProperty.class);
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);

        property.deserializeAndSet(p, ctxt, instance);

        verify(property).deserializeAndSet(p, ctxt, instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdReferenceProperty property = mock(ObjectIdReferenceProperty.class);
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);

        property.deserializeSetAndReturn(p, ctxt, instance);

        verify(property).deserializeSetAndReturn(p, ctxt, instance);
    }

    @Test
    public void testSet() throws IOException {
        ObjectIdReferenceProperty property = mock(ObjectIdReferenceProperty.class);
        Object instance = mock(Object.class);
        Object value = mock(Object.class);

        property.set(instance, value);

        verify(property).set(instance, value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        ObjectIdReferenceProperty property = mock(ObjectIdReferenceProperty.class);
        Object instance = mock(Object.class);
        Object value = mock(Object.class);

        property.setAndReturn(instance, value);

        verify(property).setAndReturn(instance, value);
    }

    @Test
    public void testPropertyReferring() {
        ObjectIdReferenceProperty parent = mock(ObjectIdReferenceProperty.class);
        UnresolvedForwardReference ref = mock(UnresolvedForwardReference.class);
        Class<?> type = mock(Class.class);
        Object ob = mock(Object.class);

        ObjectIdReferenceProperty.PropertyReferring result = new ObjectIdReferenceProperty.PropertyReferring(parent, ref, type, ob);

        assertEquals(parent, result._parent);
        assertEquals(ob, result._pojo);
    }
}