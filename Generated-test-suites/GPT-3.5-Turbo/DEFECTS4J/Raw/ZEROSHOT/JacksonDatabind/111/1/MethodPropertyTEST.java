import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MethodPropertyTest {
    private MethodProperty methodProperty;

    @Before
    public void setUp() {
        methodProperty = new MethodProperty(null, null, null, null, null);
    }

    @Test
    public void testWithPropertyName() {
        PropertyName newName = new PropertyName("newName");
        SettableBeanProperty newProperty = methodProperty.withName(newName);
        assertEquals(newName, newProperty.getFullName());
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> deser = null;
        SettableBeanProperty newProperty = methodProperty.withValueDeserializer(deser);
        assertEquals(deser, newProperty.getValueDeserializer());
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider nva = null;
        SettableBeanProperty newProperty = methodProperty.withNullProvider(nva);
        assertEquals(nva, newProperty.getNullProvider());
    }

    @Test
    public void testFixAccess() {
        DeserializationConfig config = null;
        methodProperty.fixAccess(config);
    }

    @Test
    public void testGetAnnotation() {
        Class<Annotation> acls = null;
        Annotation annotation = methodProperty.getAnnotation(acls);
        assertNull(annotation);
    }

    @Test
    public void testGetMember() {
        AnnotatedMember member = methodProperty.getMember();
        assertNull(member);
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = new Object();
        methodProperty.deserializeAndSet(p, ctxt, instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = new Object();
        methodProperty.deserializeSetAndReturn(p, ctxt, instance);
    }

    @Test
    public void testSet() throws Exception {
        Object instance = new Object();
        Object value = new Object();
        methodProperty.set(instance, value);
    }

    @Test
    public void testSetAndReturn() throws Exception {
        Object instance = new Object();
        Object value = new Object();
        methodProperty.setAndReturn(instance, value);
    }

    @Test
    public void testReadResolve() throws Exception {
        Object result = methodProperty.readResolve();
        assertNotNull(result);
    }
}