import static org.junit.Assert.*;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class PropertyBuilderTest {

    @Test
    public void testBuildWriter() {
        // Test for a simple property
        PropertyBuilder builder = new PropertyBuilder(config, beanDesc);
        BeanPropertyDefinition propDef = new BeanPropertyDefinition() {
            @Override 
            public String getName() {
                return "name";
            }
        };
        JavaType declaredType = new JavaType(String.class);
        JsonSerializer<?> ser = new JsonSerializer<String>() {
            @Override 
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException, JsonProcessingException {
                gen.writeString(value);
            }
        };
        TypeSerializer typeSer = new TypeSerializer();
        TypeSerializer contentTypeSer = new TypeSerializer();
        AnnotatedMember am = new AnnotatedMember();
        boolean defaultUseStaticTyping = true;
        BeanPropertyWriter result = builder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
        assertNotNull(result);
        assertEquals(propDef, result.getPropDef());
        assertEquals(am, result.getMember());
        assertEquals(declaredType, result.getDeclaredType());
        assertEquals(ser, result.getSerializer());
        assertEquals(typeSer, result.getTypeSerializer());
        assertEquals(contentTypeSer, result.getContentTypeSerializer());
        assertTrue(result.isSuppressNulls());
        assertNull(result.getValueToSuppress());
    }

    @Test
    public void testFindSerializationType() {
        // Test for when annotations are present
        PropertyBuilder builder = new PropertyBuilder(config, beanDesc);
        Annotated a = new Annotated() {
            @Override
            public <A extends java.lang.annotation.Annotation> A getAnnotation(Class<A> acls) {
                return acls.getAnnotation(JsonProperty.class);
            }
        };
        boolean useStaticTyping = false;
        JavaType declaredType = new JavaType(String.class);
        JavaType result = builder.findSerializationType(a, useStaticTyping, declaredType);
        assertNotNull(result);
        assertEquals(String.class, result.getRawClass());
    }

    @Test
    public void testGetDefaultValue() {
        PropertyBuilder builder = new PropertyBuilder(config, beanDesc);
        JavaType type = new JavaType(String.class);
        Object result = builder.getDefaultValue(type);
        assertNotNull(result);
        assertEquals("", result);
    }
}