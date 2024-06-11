import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CreatorPropertyTest {

    private CreatorProperty creatorProperty;

    @Before
    public void setup() {
        creatorProperty = new CreatorProperty(
            new PropertyName("name"),
            null,
            null,
            null,
            null,
            null,
            0,
            null,
            null
        );
    }

    @Test
    public void testWithName() {
        PropertyName newName = new PropertyName("newName");
        SettableBeanProperty result = creatorProperty.withName(newName);
        assertEquals(newName, result.getName());
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> deser = null;
        SettableBeanProperty result = creatorProperty.withValueDeserializer(deser);
        assertEquals(deser, result.getValueDeserializer());
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider nva = null;
        SettableBeanProperty result = creatorProperty.withNullProvider(nva);
        assertEquals(nva, result.getNullProvider());
    }

    @Test
    public void testFixAccess() {
        DeserializationConfig config = null;
        creatorProperty.fixAccess(config);
    }

    @Test
    public void testSetFallbackSetter() {
        SettableBeanProperty fallbackSetter = null;
        creatorProperty.setFallbackSetter(fallbackSetter);
    }

    @Test
    public void testMarkAsIgnorable() {
        creatorProperty.markAsIgnorable();
        assertTrue(creatorProperty.isIgnorable());
    }

    @Test
    public void testFindInjectableValue() throws Exception {
        DeserializationContext context = null;
        Object beanInstance = null;
        creatorProperty.findInjectableValue(context, beanInstance);
    }

    @Test
    public void testInject() throws Exception {
        DeserializationContext context = null;
        Object beanInstance = null;
        creatorProperty.inject(context, beanInstance);
    }

    @Test
    public void testGetAnnotation() {
        Class<A> acls = null;
        creatorProperty.getAnnotation(acls);
    }

    @Test
    public void testGetMember() {
        assertNotNull(creatorProperty.getMember());
    }

    @Test
    public void testGetCreatorIndex() {
        assertEquals(0, creatorProperty.getCreatorIndex());
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = null;
        creatorProperty.deserializeAndSet(p, ctxt, instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = null;
        creatorProperty.deserializeSetAndReturn(p, ctxt, instance);
    }

    @Test
    public void testSet() throws Exception {
        Object instance = null;
        Object value = null;
        creatorProperty.set(instance, value);
    }

    @Test
    public void testSetAndReturn() throws Exception {
        Object instance = null;
        Object value = null;
        creatorProperty.setAndReturn(instance, value);
    }

    @Test
    public void testGetInjectableValueId() {
        assertNull(creatorProperty.getInjectableValueId());
    }

    @Test
    public void testToString() {
        assertEquals("[creator property, name 'name'; inject id 'null']", creatorProperty.toString());
    }

    @Test(expected = IOException.class)
    public void testVerifySetter() throws Exception {
        creatorProperty._verifySetter();
    }

    @Test(expected = IOException.class)
    public void testReportMissingSetter() throws Exception {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        creatorProperty._reportMissingSetter(p, ctxt);
    }
}