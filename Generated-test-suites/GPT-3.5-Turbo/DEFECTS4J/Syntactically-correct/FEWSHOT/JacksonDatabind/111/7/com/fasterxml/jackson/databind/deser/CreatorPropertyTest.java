import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.BeforeClass;
import org.junit.Test;

public class CreatorPropertyTest {

    private static CreatorProperty creatorProperty;

    @BeforeClass
    public static void setup() {
        creatorProperty = new CreatorProperty(
            new PropertyName("name"),
            com.fasterxml.jackson.databind.JavaType,
            new PropertyName("wrapperName"),
            new com.fasterxml.jackson.databind.jsontype.TypeDeserializer(),
            new com.fasterxml.jackson.databind.util.Annotations(),
            new com.fasterxml.jackson.databind.introspect.AnnotatedParameter(),
            1,
            new Object(),
            new PropertyMetadata()
        );
    }

    @Test
    public void testConstructor() {
        assertEquals("name", creatorProperty.getName().getSimpleName());
        assertEquals(com.fasterxml.jackson.databind.JavaType, creatorProperty.getType().getClass());
        assertEquals("wrapperName", creatorProperty.getWrapperName().getSimpleName());
        assertEquals(com.fasterxml.jackson.databind.jsontype.TypeDeserializer, creatorProperty.getTypeDeserializer().getClass());
        assertNotNull(creatorProperty.getContextAnnotations());
        assertNotNull(creatorProperty.getMember());
        assertEquals(1, creatorProperty.getCreatorIndex());
        assertNotNull(creatorProperty.getInjectableValueId());
        assertNotNull(creatorProperty.getMetadata());
    }

    @Test
    public void testWithMethods() {
        CreatorProperty newCreatorProperty = creatorProperty.withName(new PropertyName("newName"));
        assertEquals("newName", newCreatorProperty.getName().getSimpleName());

        newCreatorProperty = creatorProperty.withValueDeserializer(new com.fasterxml.jackson.databind.JsonDeserializer<>());
        assertTrue(newCreatorProperty.getValueDeserializer() instanceof com.fasterxml.jackson.databind.JsonDeserializer);

        newCreatorProperty = creatorProperty.withNullProvider(new com.fasterxml.jackson.databind.deser.NullValueProvider());
        assertTrue(newCreatorProperty.getNullProvider() instanceof com.fasterxml.jackson.databind.deser.NullValueProvider);

        creatorProperty.fixAccess(new com.fasterxml.jackson.databind.DeserializationConfig());
        assertNull(creatorProperty.getFallbackSetter());
        
        creatorProperty.setFallbackSetter(new com.fasterxml.jackson.databind.deser.SettableBeanProperty());
        assertNotNull(creatorProperty.getFallbackSetter());
        
        creatorProperty.markAsIgnorable();
        assertTrue(creatorProperty.isIgnorable());
    }

    @Test
    public void testInjection() throws Exception {
        com.fasterxml.jackson.databind.DeserializationContext context = new com.fasterxml.jackson.databind.DeserializationContext(null);
        Object beanInstance = new Object();
        assertNotNull(creatorProperty.findInjectableValue(context, beanInstance));

        creatorProperty.inject(context, beanInstance);
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        com.fasterxml.jackson.databind.JsonParser parser = new com.fasterxml.jackson.databind.JsonFactory().createParser("");
        com.fasterxml.jackson.databind.DeserializationContext context = new com.fasterxml.jackson.databind.DeserializationContext(null);
        Object instance = new Object();
        creatorProperty.deserializeAndSet(parser, context, instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        com.fasterxml.jackson.databind.JsonParser parser = new com.fasterxml.jackson.databind.JsonFactory().createParser("");
        com.fasterxml.jackson.databind.DeserializationContext context = new com.fasterxml.jackson.databind.DeserializationContext(null);
        Object instance = new Object();
        assertNotNull(creatorProperty.deserializeSetAndReturn(parser, context, instance));
    }

    @Test
    public void testSet() throws Exception {
        Object instance = new Object();
        creatorProperty.set(instance, "value");
    }

    @Test
    public void testSetAndReturn() throws Exception {
        Object instance = new Object();
        assertNotNull(creatorProperty.setAndReturn(instance, "value"));
    }

    @Test
    public void testGetInjectableValueId() {
        assertNotNull(creatorProperty.getInjectableValueId());
    }

    @Test
    public void testToString() {
        assertNotNull(creatorProperty.toString());
    }

    @Test
    public void testVerifySetter() throws Exception {
        creatorProperty.setFallbackSetter(new com.fasterxml.jackson.databind.deser.SettableBeanProperty());
        creatorProperty._verifySetter();
    }

    @Test(expected = IOException.class)
    public void testReportMissingSetter() throws Exception {
        creatorProperty._reportMissingSetter(new com.fasterxml.jackson.databind.JsonParser(), new com.fasterxml.jackson.databind.DeserializationContext(null));
    }
}