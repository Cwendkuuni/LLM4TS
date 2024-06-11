import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.NullsConstantProvider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class MethodPropertyTest {

    private MethodProperty methodProperty;

    @Before
    public void setup() throws NoSuchMethodException {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType(Object.class);
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = new Annotations();
        Method method = MethodProperty.class.getMethod("deserializeAndSet", JsonParser.class, DeserializationContext.class, Object.class);
        methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, new AnnotatedMethod(method));
    }

    @Test
    public void testWithNullValue() throws IOException {
        JsonParser parser = new ObjectMapper().getFactory().createParser("{}");
        DeserializationContext context = new DeserializationContext();
        Object instance = new Object();

        methodProperty.deserializeAndSet(parser, context, instance);

        assertNotNull(instance);
        assertNull(methodProperty.get(instance));
    }

    @Test
    public void testWithNonNullValue() throws IOException {
        JsonParser parser = new ObjectMapper().getFactory().createParser("{\"value\": \"test\"}");
        DeserializationContext context = new DeserializationContext();
        Object instance = new Object();

        methodProperty.deserializeAndSet(parser, context, instance);

        assertNotNull(instance);
        assertEquals("test", methodProperty.get(instance));
    }
}