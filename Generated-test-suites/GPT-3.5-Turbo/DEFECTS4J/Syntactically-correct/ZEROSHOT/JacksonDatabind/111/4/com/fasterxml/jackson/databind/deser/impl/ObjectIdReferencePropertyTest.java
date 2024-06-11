import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ObjectIdReferencePropertyTest {

    private ObjectIdReferenceProperty property;
    private SettableBeanProperty forward;
    private ObjectIdInfo objectIdInfo;

    @Before
    public void setUp() {
        forward = mock(SettableBeanProperty.class);
        objectIdInfo = mock(ObjectIdInfo.class);
        
        property = new ObjectIdReferenceProperty(forward, objectIdInfo);
    }

    @Test
    public void testWithName() {
        PropertyName newName = mock(PropertyName.class);

        SettableBeanProperty result = property.withName(newName);

        assertNotNull(result);
        assertTrue(result instanceof ObjectIdReferenceProperty);
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> deser = mock(JsonDeserializer.class);

        SettableBeanProperty result = property.withValueDeserializer(deser);

        assertNotNull(result);
        assertTrue(result instanceof ObjectIdReferenceProperty);
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider nva = mock(NullValueProvider.class);

        SettableBeanProperty result = property.withNullProvider(nva);

        assertNotNull(result);
        assertTrue(result instanceof ObjectIdReferenceProperty);
    }

    @Test
    public void testFixAccess() {
        DeserializationConfig config = mock(DeserializationConfig.class);

        property.fixAccess(config);

        verify(forward).fixAccess(config);
    }

    @Test
    public void testGetAnnotation() {
        Class<A> acls = mock(Class.class);

        A result = property.getAnnotation(acls);

        assertEquals(null, result);
        verify(forward).getAnnotation(acls);
    }

    @Test
    public void testGetMember() {
        AnnotatedMember result = property.getMember();

        assertEquals(null, result);
        verify(forward).getMember();
    }

    @Test
    public void testGetCreatorIndex() {
        int result = property.getCreatorIndex();

        assertEquals(0, result);
        verify(forward).getCreatorIndex();
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();

        property.deserializeAndSet(p, ctxt, instance);

        verify(forward).deserializeAndSet(p, ctxt, instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();

        Object result = property.deserializeSetAndReturn(p, ctxt, instance);

        assertEquals(null, result);
        verify(forward).deserializeSetAndReturn(p, ctxt, instance);
    }

    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Object value = new Object();

        property.set(instance, value);

        verify(forward).set(instance, value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        Object value = new Object();

        Object result = property.setAndReturn(instance, value);

        assertEquals(null, result);
        verify(forward).setAndReturn(instance, value);
    }

    @Test
    public void testPropertyReferringHandleResolvedForwardReference() throws IOException {
        ObjectIdReferenceProperty.PropertyReferring referring = new ObjectIdReferenceProperty.PropertyReferring(property,
                null, null, null);

        referring.handleResolvedForwardReference(null, null);
    }
}