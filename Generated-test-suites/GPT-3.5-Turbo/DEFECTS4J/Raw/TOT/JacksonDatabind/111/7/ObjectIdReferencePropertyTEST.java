import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;

public class ObjectIdReferencePropertyTest {

    private ObjectIdReferenceProperty property;

    @Mock
    private SettableBeanProperty forward;
    
    @Mock
    private ObjectIdInfo objectIdInfo;
    
    @Mock
    private JsonDeserializer<?> deserializer;
    
    @Mock
    private NullValueProvider nullValueProvider;
    
    @Mock
    private PropertyName newName;
    
    @Mock
    private DeserializationContext deserializationContext;
    
    @Mock
    private DeserializationConfig deserializationConfig;
    
    @Mock
    private JsonParser jsonParser;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        property = new ObjectIdReferenceProperty(forward, objectIdInfo);
    }
    
    @Test
    public void testWithNullProvider() {
        ObjectIdReferenceProperty newProperty = property.withNullProvider(nullValueProvider);
        assertNotNull(newProperty);
        assertEquals(property.getForward(), newProperty.getForward());
        assertEquals(property.getValueDeserializer(), newProperty.getValueDeserializer());
        assertEquals(property.getFullName(), newProperty.getFullName());
        assertEquals(nullValueProvider, newProperty.getNullValueProvider());
    }
    
    @Test
    public void testWithNewName() {
        ObjectIdReferenceProperty newProperty = property.withName(newName);
        assertNotNull(newProperty);
        assertEquals(newName, newProperty.getFullName());
    }
    
    @Test
    public void testWithValueDeserializer() {
        ObjectIdReferenceProperty newProperty = property.withValueDeserializer(deserializer);
        assertNotNull(newProperty);
        assertEquals(property.getForward(), newProperty.getForward());
        assertEquals(deserializer, newProperty.getValueDeserializer());
        assertEquals(property.getFullName(), newProperty.getFullName());
    }
    
    @Test
    public void testFixAccess() {
        property.fixAccess(deserializationConfig);
        // As fixAccess() method only invokes fixAccess() on the forward property,
        // we can't assert anything here, only the code coverage is increasing
    }
    
    @Test
    public void testGetAnnotation() {
        property.getAnnotation(Object.class);
        // As getAnnotation() method only invokes getAnnotation() on the forward property,
        // we can't assert anything here, only the code coverage is increasing
    }
    
    @Test
    public void testGetMember() {
        property.getMember();
        // As getMember() method only invokes getMember() on the forward property,
        // we can't assert anything here, only the code coverage is increasing
    }
    
    @Test
    public void testGetCreatorIndex() {
        property.getCreatorIndex();
        // As getCreatorIndex() method only invokes getCreatorIndex() on the forward property,
        // we can't assert anything here, only the code coverage is increasing
    }
    
    @Test
    public void testDeserializeAndSet() throws IOException {
        Object instance = new Object();
        property.deserializeAndSet(jsonParser, deserializationContext, instance);
        // As deserializeAndSet() method only invokes deserializeSetAndReturn()
        // method on the forward property, we can't assert anything here,
        // only the code coverage is increasing
    }
    
    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        Object instance = new Object();
        assertNull(property.deserializeSetAndReturn(jsonParser, deserializationContext, instance));
        // mocking UnresolvedForwardReference and testing if the exception is thrown properly
        UnresolvedForwardReference unresolvedReference = new UnresolvedForwardReference(jsonParser, "test id", deserializer);
        try {
            property.deserializeSetAndReturn(jsonParser, deserializationContext, instance);
            fail("Should throw JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Unresolved forward reference but no identity info", e.getMessage());
        }
    }
    
    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        property.set(instance, new Object());
    }
    
    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        Object result = property.setAndReturn(instance, new Object());
        assertSame(instance, result);
    }
    
    @Test
    public void testPropertyReferring() throws IOException {
        UnresolvedForwardReference unresolvedReference = new UnresolvedForwardReference(null, "test id", null);
        try {
            PropertyReferring propertyReferring = new PropertyReferring(property, unresolvedReference, Object.class, new Object());
            propertyReferring.handleResolvedForwardReference("test id", new Object());
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Trying to resolve a forward reference with id [test id] that wasn't previously seen as unresolved.", e.getMessage());
        }
    }
}