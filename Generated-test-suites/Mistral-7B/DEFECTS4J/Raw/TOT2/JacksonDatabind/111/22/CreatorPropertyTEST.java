import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;

public class CreatorPropertyTest {
    // Test for constructor with valid arguments
    @Test
    public void testCreatorPropertyWithValidArguments() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);

        CreatorProperty creatorProperty = new CreatorProperty(
                new PropertyName("testProperty"),
                new SimpleType(String.class),
                null,
                null,
                null,
                null,
                0,
                null,
                null);

        String json = "{\"name\":\"testProperty\",\"type\":\"java.lang.String\",\"creatorIndex\":0}";
        CreatorProperty deserializedCreatorProperty = mapper.readValue(json, CreatorProperty.class);

        assertEquals(creatorProperty.getName(), deserializedCreatorProperty.getName());
        assertEquals(creatorProperty.getType(), deserializedCreatorProperty.getType());
        assertEquals(creatorProperty.isIgnorable(), deserializedCreatorProperty.isIgnorable());
    }

    // Test for constructor with null arguments
    @Test
    public void testCreatorPropertyWithNullArguments() {
        CreatorProperty creatorProperty = new CreatorProperty(
                null,
                null,
                null,
                null,
                null,
                null,
                0,
                null,
                null);

        assertNull(creatorProperty.getName());
        assertNull(creatorProperty.getType());
        assertNull(creatorProperty.getAnnotated());
        assertNull(creatorProperty._injectableValueId);
        assertNull(creatorProperty._fallbackSetter);
        assertFalse(creatorProperty.isIgnorable());
    }

    // Test for setFallbackSetter method
    @Test
    public void testSetFallbackSetter() {
        CreatorProperty creatorProperty = new CreatorProperty(
                new PropertyName("testProperty"),
                new SimpleType(String.class),
                null,
                null,
                null,
                null,
                0,
                null,
                null);

        SettableBeanProperty fallbackSetter = new SimpleBeanProperty("testFallbackSetter", String.class);
        creatorProperty.setFallbackSetter(fallbackSetter);

        assertSame(fallbackSetter, creatorProperty._fallbackSetter);
    }

    // Test for findInjectableValue method with valid injectable value id
    @Test
    public void testFindInjectableValueWithValidId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        CreatorProperty creatorProperty = new CreatorProperty(
                new PropertyName("testProperty"),
                new SimpleType(String.class),
                null,
                null,
                null,
                null,
                0,
                "testInjectableValueId",
                null);

        String json = "{\"name\":\"testProperty\",\"type\":\"java.lang.String\",\"injectableValueId\":\"testInjectableValueId\",\"creatorIndex\":0}";
        CreatorProperty deserializedCreatorProperty = mapper.readValue(json, CreatorProperty.class);

        Object injectedValue = deserializedCreatorProperty.findInjectableValue(null, null);
        assertEquals("testInjectableValue", injectedValue);
    }

    // Test for findInjectableValue method with invalid injectable value id
    @Test(expected = JsonMappingException.class)
    public void testFindInjectableValueWithInvalidId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        CreatorProperty creatorProperty = new CreatorProperty(
                new PropertyName("testProperty"),
                new SimpleType(String.class),
                null,
                null,
                null,
                null,
                0,
                "nonExistentInjectableValueId",
                null);

        creatorProperty.findInjectableValue(null, null);
    }

    // Test for inject method with valid injectable value
    @Test
    public void testInjectWithValidValue() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        CreatorProperty creatorProperty = new CreatorProperty(
                new PropertyName("testProperty"),
                new SimpleType(String.class),
                null,
                null,
                null,
                null,
                0,
                "testInjectableValueId",
                null);

        String json = "{\"name\":\"testProperty\",\"type\":\"java.lang.String\",\"injectableValueId\":\"testInjectableValueId\",\"creatorIndex\":0}";
        CreatorProperty deserializedCreatorProperty = mapper.readValue(json, CreatorProperty.class);

        Object beanInstance = new Object();
        deserializedCreatorProperty.inject(null, beanInstance);

        assertEquals("testInjectableValue", beanInstance.getClass().getField("testProperty").get(beanInstance));
    }

    // Test for inject method with null bean instance
    @Test(expected = IOException.class)
    public void testInjectWithNullBeanInstance() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        CreatorProperty creatorProperty = new CreatorProperty(
                new PropertyName("testProperty"),
                new SimpleType(String.class),
                null,
                null,
                null,
                null,
                0,
                "testInjectableValueId",
                null);

        creatorProperty.inject(null, null);
    }

    // Test for deserializeAndSet method
    @Test
    public void testDeserializeAndSet() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        CreatorProperty creatorProperty = new CreatorProperty(
                new PropertyName("testProperty"),
                new SimpleType(String.class),
                null,
                null,
                null,
                null,
                0,
                null,
                null);

        String json = "{\"name\":\"testProperty\",\"type\":\"java.lang.String\",\"value\":\"testValue\"}";

        Object beanInstance = new Object();
        creatorProperty.deserializeAndSet(mapper.getFactory().createParser(json), null, beanInstance);

        assertEquals("testValue", beanInstance.getClass().getField("testProperty").get(beanInstance));
    }

    // Test for deserializeAndSet method with null jsonParser
    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeAndSetWithNullJsonParser() throws Exception {
        CreatorProperty creatorProperty = new CreatorProperty(
                new PropertyName("testProperty"),
                new SimpleType(String.class),
                null,
                null,
                null,
                null,
                0,
                null,
                null);

        creatorProperty.deserializeAndSet(null, null, new Object());
    }

    // Test for deserializeSetAndReturn method
    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        CreatorProperty creatorProperty = new CreatorProperty(
                new PropertyName("testProperty"),
                new SimpleType(String.class),
                null,
                null,
                null,
                null,
                0,
                null,
                null);

        String json = "{\"name\":\"testProperty\",\"type\":\"java.lang.String\",\"value\":\"testValue\"}";

        Object beanInstance = new Object();
        Object returnValue = creatorProperty.deserializeSetAndReturn(mapper.getFactory().createParser(json), null, beanInstance);

        assertEquals("testValue", returnValue);
        assertEquals("testValue", beanInstance.getClass().getField("testProperty").get(beanInstance));
    }

    // Test for deserializeSetAndReturn method with null jsonParser
    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeSetAndReturnWithNullJsonParser() throws Exception {
        CreatorProperty creatorProperty = new CreatorProperty(
                new PropertyName("testProperty"),
                new SimpleType(String.class),
                null,
                null,
                null,
                null,
                0,
                null,
                null);

        creatorProperty.deserializeSetAndReturn(null, null, new Object());
    }

    // Test for set method
    @Test
    public void testSet() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        CreatorProperty creatorProperty = new CreatorProperty(
                new PropertyName("testProperty"),
                new SimpleType(String.class),
                null,
                null,
                null,
                null,
                0,
                null,
                null);

        String json = "{\"name\":\"testProperty\",\"type\":\"java.lang.String\",\"value\":\"testValue\"}";
        Object beanInstance = new Object();

        creatorProperty.set(beanInstance, mapper.readValue(json, String.class));

        assertEquals("testValue", beanInstance.getClass().getField("testProperty").get(beanInstance));
    }

    // Test for set method with null jsonParser
    @Test(expected = IllegalArgumentException.class)
    public void testSetWithNullJsonParser() throws Exception {
        CreatorProperty creatorProperty = new CreatorProperty(
                new PropertyName("testProperty"),
                new SimpleType(String.class),
                null,
                null,
                null,
                null,
                0,
                null,
                null);

        creatorProperty.set(null, null);
    }
}