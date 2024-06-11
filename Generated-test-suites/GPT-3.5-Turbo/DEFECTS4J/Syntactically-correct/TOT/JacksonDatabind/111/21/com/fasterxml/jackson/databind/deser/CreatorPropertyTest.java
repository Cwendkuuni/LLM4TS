import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class CreatorPropertyTest {

    @Test
    public void testConstructor() {
        PropertyName name = new PropertyName("test");
        JavaType type = TypeFactory.defaultInstance().constructType(String.class);
        PropertyName wrapperName = new PropertyName("wrapperName");
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedParameter param = null;
        int index = 0;
        Object injectableValueId = null;
        PropertyMetadata metadata = PropertyMetadata.STD_REQUIRED;

        CreatorProperty creatorProperty = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, metadata);

        assertEquals(name, creatorProperty.getName());
        assertEquals(type, creatorProperty.getType());
        assertEquals(wrapperName, creatorProperty.getWrapperName());
        assertEquals(typeDeser, creatorProperty.getTypeDeserializer());
        assertEquals(contextAnnotations, creatorProperty.getContextAnnotations());
        assertEquals(param, creatorProperty.getMember());
        assertEquals(index, creatorProperty.getCreatorIndex());
        assertEquals(injectableValueId, creatorProperty.getInjectableValueId());
        assertEquals(metadata, creatorProperty.getMetadata());
    }

    @Test(expected = InvalidDefinitionException.class)
    public void testDeserializeAndSetWithoutSetter() throws IOException {
        CreatorProperty creatorProperty = createCreatorProperty();
        JsonParser p = createJsonParser();
        DeserializationContext ctxt = createDeserializationContext();
        Object instance = new Object();

        creatorProperty.deserializeAndSet(p, ctxt, instance);
    }

    @Test(expected = InvalidDefinitionException.class)
    public void testDeserializeSetAndReturnWithoutSetter() throws IOException {
        CreatorProperty creatorProperty = createCreatorProperty();
        JsonParser p = createJsonParser();
        DeserializationContext ctxt = createDeserializationContext();
        Object instance = new Object();

        creatorProperty.deserializeSetAndReturn(p, ctxt, instance);
    }

    @Test(expected = InvalidDefinitionException.class)
    public void testSetWithoutSetter() throws IOException {
        CreatorProperty creatorProperty = createCreatorProperty();
        Object instance = new Object();
        Object value = new Object();

        creatorProperty.set(instance, value);
    }

    @Test(expected = InvalidDefinitionException.class)
    public void testSetAndReturnWithoutSetter() throws IOException {
        CreatorProperty creatorProperty = createCreatorProperty();
        Object instance = new Object();
        Object value = new Object();

        creatorProperty.setAndReturn(instance, value);
    }

    private CreatorProperty createCreatorProperty() {
        PropertyName name = new PropertyName("test");
        JavaType type = TypeFactory.defaultInstance().constructType(String.class);
        PropertyName wrapperName = new PropertyName("wrapperName");
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedParameter param = null;
        int index = 0;
        Object injectableValueId = null;
        PropertyMetadata metadata = PropertyMetadata.STD_REQUIRED;

        return new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, metadata);
    }

    private JsonParser createJsonParser() {
        // Create JsonParser object
        return new ObjectMapper().getFactory().createParser("{}");
    }

    private DeserializationContext createDeserializationContext() {
        // Create DeserializationContext object
        return new ObjectMapper().getDeserializationContext();
    }
}