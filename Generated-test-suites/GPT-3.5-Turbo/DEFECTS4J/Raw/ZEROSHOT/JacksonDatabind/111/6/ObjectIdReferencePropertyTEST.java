import java.io.IOException;
import java.lang.annotation.Annotation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import org.junit.*;
import static org.junit.Assert.*;

public class ObjectIdReferencePropertyTest {

    private ObjectIdReferenceProperty property;
    private JsonParser jsonParser;
    private DeserializationContext deserializationContext;
    private Object instance;

    @Before
    public void setUp() {
        SettableBeanProperty forward = new SettableBeanProperty();
        ObjectIdInfo objectIdInfo = new ObjectIdInfo();
        property = new ObjectIdReferenceProperty(forward, objectIdInfo);
        jsonParser = new JsonParser(null);
        deserializationContext = new DeserializationContext(null);
        instance = new Object();
    }

    @Test
    public void testWithNullValueProvider() {
        NullValueProvider nva = new NullValueProvider();
        ObjectIdReferenceProperty newProperty = property.withNullProvider(nva);
        assertNotNull(newProperty);
        assertEquals(nva, newProperty.nullValueProvider());
    }

    @Test
    public void testFixAccess() {
        DeserializationConfig config = new DeserializationConfig(null, null, null);
        property.fixAccess(config);
    }

    @Test
    public void testGetAnnotation() {
        Class<A> acls = A.class;
        Annotation annotation = property.getAnnotation(acls);
        assertNull(annotation);
    }

    @Test
    public void testGetMember() {
        AnnotatedMember member = property.getMember();
        assertNull(member);
    }

    @Test
    public void testGetCreatorIndex() {
        int index = property.getCreatorIndex();
        assertEquals(0, index);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        property.deserializeAndSet(jsonParser, deserializationContext, instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        Object result = property.deserializeSetAndReturn(jsonParser, deserializationContext, instance);
        assertNull(result);
    }

    @Test
    public void testSet() throws IOException {
        Object value = new Object();
        property.set(instance, value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object value = new Object();
        Object result = property.setAndReturn(instance, value);
        assertEquals(instance, result);
    }

    @Test
    public void testPropertyReferring() {
        UnresolvedForwardReference reference = new UnresolvedForwardReference();
        Class<?> type = Object.class;
        Object ob = new Object();
        ObjectIdReferenceProperty.PropertyReferring referring = new ObjectIdReferenceProperty.PropertyReferring(property, reference, type, ob);
        assertNotNull(referring);
        assertEquals(property, referring._parent);
        assertEquals(ob, referring._pojo);
    }

    @Test
    public void testHandleResolvedForwardReference() throws IOException {
        UnresolvedForwardReference reference = new UnresolvedForwardReference();
        Class<?> type = Object.class;
        Object ob = new Object();
        ObjectIdReferenceProperty.PropertyReferring referring = new ObjectIdReferenceProperty.PropertyReferring(property, reference, type, ob);
        referring.handleResolvedForwardReference("id", "value");
    }
}