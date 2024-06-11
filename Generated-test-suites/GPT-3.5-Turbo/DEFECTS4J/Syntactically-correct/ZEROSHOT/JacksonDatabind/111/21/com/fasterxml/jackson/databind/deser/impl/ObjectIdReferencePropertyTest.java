import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReferenceProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdReferencePropertyTest {

    @Test
    public void testWithNullName() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(
                null, new ObjectIdInfo(null, null, null));
        assertNull(property.withName(null));
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(
                null, new ObjectIdInfo(null, null, null));
        assertNull(property.withValueDeserializer(null));
    }

    @Test
    public void testWithNullProvider() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(
                null, new ObjectIdInfo(null, null, null));
        assertNull(property.withNullProvider(null));
    }

    @Test
    public void testFixAccess() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(
                null, new ObjectIdInfo(null, null, null));
        property.fixAccess(null);
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(
                null, new ObjectIdInfo(null, null, null));
        assertNull(property.getAnnotation(null));
    }

    @Test
    public void testGetMember() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(
                null, new ObjectIdInfo(null, null, null));
        assertNull(property.getMember());
    }

    @Test
    public void testGetCreatorIndex() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(
                null, new ObjectIdInfo(null, null, null));
        assertEquals(0, property.getCreatorIndex());
    }

    @Test
    public void testDeserializeAndSet() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(
                null, new ObjectIdInfo(null, null, null));
        try {
            property.deserializeAndSet(null, null, null);
        } catch (Exception e) {
            assertTrue(e instanceof IOException);
        }
    }

    @Test
    public void testDeserializeSetAndReturn() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(
                null, new ObjectIdInfo(null, null, null));
        try {
            property.deserializeSetAndReturn(null, null, null);
        } catch (Exception e) {
            assertTrue(e instanceof IOException);
        }
    }

    @Test
    public void testSet() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(
                null, new ObjectIdInfo(null, null, null));
        try {
            property.set(null, null);
        } catch (Exception e) {
            assertTrue(e instanceof IOException);
        }
    }

    @Test
    public void testSetAndReturn() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(
                null, new ObjectIdInfo(null, null, null));
        try {
            property.setAndReturn(null, null);
        } catch (Exception e) {
            assertTrue(e instanceof IOException);
        }
    }

    @Test
    public void testPropertyReferring() {
        ObjectIdReferenceProperty.PropertyReferring referring = new ObjectIdReferenceProperty.PropertyReferring(
                null, new UnresolvedForwardReference(null, null, null), String.class, null);
        try {
            referring.handleResolvedForwardReference(null, null);
        } catch (Exception e) {
            assertTrue(e instanceof IOException);
        }
    }
}