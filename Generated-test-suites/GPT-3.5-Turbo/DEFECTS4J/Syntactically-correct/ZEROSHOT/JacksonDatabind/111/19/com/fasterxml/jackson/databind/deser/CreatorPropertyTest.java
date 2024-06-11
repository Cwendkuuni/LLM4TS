import static org.junit.Assert.*;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class CreatorPropertyTest {

    private CreatorProperty creatorProperty;
    private ObjectMapper mapper;
    private DeserializationContext ctxt;
    private DeserializationConfig config;
    private AnnotatedParameter param;
    private PropertyMetadata metadata;
    private JsonDeserializer<?> deser;
    private NullValueProvider nva;
    private AnnotatedMember annotatedMember;

    @Before
    public void setUp() {
        creatorProperty = new CreatorProperty(null, null, null, null, null, null, 0, null, null);
        mapper = new ObjectMapper();
        ctxt = mapper.getDeserializationContext();
        config = mapper.getDeserializationConfig();
        param = null;
        metadata = null;
        deser = null;
        nva = null;
        annotatedMember = null;
    }

    @Test
    public void testConstructor() {
        CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, null, null);
        assertEquals(null, property.getName());
        assertEquals(null, property.getType());
        assertEquals(null, property.getWrapperName());
        assertEquals(null, property.getTypeDeserializer());
        assertEquals(null, property.getContextAnnotations());
        assertEquals(null, property.getAnnotated());
        assertEquals(0, property.getCreatorIndex());
        assertEquals(null, property.getInjectableValueId());
        assertEquals(null, property.getMetadata());
        assertEquals(null, property.getValueDeserializer());
        assertEquals(null, property.getNullValueProvider());
        assertEquals(false, property.isIgnorable());
        assertEquals(null, property.getMember());
    }

    @Test
    public void testConstructorWithParameters() {
        PropertyName name = new PropertyName("name");
        JavaType type = null;
        PropertyName wrapperName = new PropertyName("wrapperName");
        TypeDeserializer typeDeser = null;
        Annotations annotations = null;
        Object injectableValueId = null;

        CreatorProperty property = new CreatorProperty(name, type, wrapperName, typeDeser, annotations, param, 0, injectableValueId, metadata);

        assertEquals(name, property.getName());
        assertEquals(type, property.getType());
        assertEquals(wrapperName, property.getWrapperName());
        assertEquals(typeDeser, property.getTypeDeserializer());
        assertEquals(annotations, property.getContextAnnotations());
        assertEquals(param, property.getAnnotated());
        assertEquals(0, property.getCreatorIndex());
        assertEquals(injectableValueId, property.getInjectableValueId());
        assertEquals(metadata, property.getMetadata());
    }

    @Test
    public void testConstructorWithProperty() {
        PropertyName name = new PropertyName("name");
        CreatorProperty src = new CreatorProperty(name, null, null, null, null, null, 2, null, null);

        CreatorProperty property = new CreatorProperty(src, new PropertyName("newName"));

        assertEquals(new PropertyName("newName"), property.getName());
        assertEquals(null, property.getType());
        assertEquals(null, property.getWrapperName());
        assertEquals(null, property.getTypeDeserializer());
        assertEquals(null, property.getContextAnnotations());
        assertEquals(null, property.getAnnotated());
        assertEquals(2, property.getCreatorIndex());
        assertEquals(null, property.getInjectableValueId());
        assertEquals(null, property.getMetadata());
    }

    @Test
    public void testConstructorWithDeserializer() {
        CreatorProperty src = new CreatorProperty(null, null, null, null, null, null, 0, null, null);
        JsonDeserializer<?> deser = null;

        CreatorProperty property = new CreatorProperty(src, deser, nva);

        assertEquals(null, property.getValueDeserializer());
        assertEquals(null, property.getNullValueProvider());
    }

    @Test
    public void testWithMethods() {
        CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, null, null);

        assertEquals(null, property.withName(new PropertyName("newName")));
        assertEquals(null, property.withValueDeserializer(null));
        assertEquals(null, property.withNullProvider(null));
    }

    @Test
    public void testFixAccess() {
        CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, null, null);
        property.setFallbackSetter(null);

        property.fixAccess(config);
    }

    @Test
    public void testSetterMethods() throws IOException {
        CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, null, null);
        JsonParser p = mapper.createParser("{\"key\":\"value\"}");

        try {
            property.deserializeAndSet(p, ctxt, null);
        } catch (Exception e) {
            assertEquals(InvalidDefinitionException.class, e.getClass());
            assertEquals("No fallback setter/field defined for creator property 'null'", e.getMessage());
        }

        try {
            property.deserializeSetAndReturn(p, ctxt, null);
        } catch (Exception e) {
            assertEquals(InvalidDefinitionException.class, e.getClass());
            assertEquals("No fallback setter/field defined for creator property 'null'", e.getMessage());
        }

        try {
            property.set(null, null);
        } catch (Exception e) {
            assertEquals(InvalidDefinitionException.class, e.getClass());
            assertEquals("No fallback setter/field defined for creator property 'null'", e.getMessage());
        }

        try {
            property.setAndReturn(null, null);
        } catch (Exception e) {
            assertEquals(InvalidDefinitionException.class, e.getClass());
            assertEquals("No fallback setter/field defined for creator property 'null'", e.getMessage());
        }
    }

    @Test
    public void testInjectableValueMethods() throws JsonMappingException {
        CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, null, null);
        Object beanInstance = new Object();

        try {
            property.findInjectableValue(ctxt, beanInstance);
        } catch (Exception e) {
            assertEquals(NullPointerException.class, e.getClass());
            assertEquals("Property 'null' (type null) has no injectable value id configured", e.getMessage());
        }

        try {
            property.inject(ctxt, beanInstance);
        } catch (Exception e) {
            assertEquals(NullPointerException.class, e.getClass());
            assertEquals("Property 'null' (type null) has no injectable value id configured", e.getMessage());
        }
    }

    @Test
    public void testBeanPropertyMethods() {
        CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, null, null);

        assertEquals(null, property.getAnnotation(null));
        assertEquals(null, property.getMember());
        assertEquals(0, property.getCreatorIndex());
    }

    @Test
    public void testToString() {
        CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, null, null);
        assertEquals("[creator property, name 'null'; inject id 'null']", property.toString());
    }
}