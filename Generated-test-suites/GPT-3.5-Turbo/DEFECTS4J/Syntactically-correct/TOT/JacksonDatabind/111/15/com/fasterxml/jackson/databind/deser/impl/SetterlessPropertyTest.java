import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.Annotation;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

import java.lang.reflect.Method;

public class SetterlessPropertyTest {

    private SetterlessProperty setterlessProperty;
    private BeanPropertyDefinition propDef;
    private JavaType type;
    private TypeDeserializer typeDeser;
    private Annotations contextAnnotations;
    private AnnotatedMethod method;
    private Method getter;
    private JsonDeserializer<?> deser;
    private NullValueProvider nva;
    private PropertyName newName;
    private DeserializationContext config;
    private JsonParser jsonParser;
    private Object instance;
    private JsonToken currentToken;
    private ObjectMapper objectMapper;
    private DeserializationContext context;

    @Before
    public void setUp() {
        propDef = Mockito.mock(BeanPropertyDefinition.class);
        type = Mockito.mock(JavaType.class);
        typeDeser = Mockito.mock(TypeDeserializer.class);
        contextAnnotations = Mockito.mock(Annotations.class);
        method = Mockito.mock(AnnotatedMethod.class);
        getter = Mockito.mock(Method.class);
        deser = Mockito.mock(JsonDeserializer.class);
        nva = Mockito.mock(NullValueProvider.class);
        newName = Mockito.mock(PropertyName.class);
        config = Mockito.mock(DeserializationContext.class);
        jsonParser = Mockito.mock(JsonParser.class);
        instance = Mockito.mock(Object.class);
        currentToken = Mockito.mock(JsonToken.class);
        objectMapper = Mockito.mock(ObjectMapper.class);
        context = Mockito.mock(DeserializationContext.class);

        Mockito.when(method.getAnnotated()).thenReturn(getter);
        Mockito.when(jsonParser.getCurrentToken()).thenReturn(currentToken);
        setterlessProperty = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
    }

    @Test
    public void testWithNullValueToken() throws Exception {
        Mockito.when(currentToken).thenReturn(JsonToken.VALUE_NULL);

        setterlessProperty.deserializeAndSet(jsonParser, config, instance);

        // No exception should be thrown
    }

    @Test
    public void testWithTypeDeserializer() throws Exception {
        Mockito.when(setterlessProperty.getType()).thenReturn(type);
        Mockito.when(setterlessProperty.getName()).thenReturn("testName");

        setterlessProperty.deserializeAndSet(jsonParser, context, instance);

        Mockito.verify(context).reportBadDefinition(type, "Problem deserializing 'setterless' property (\"testName\"): no way to handle typed deser with setterless yet");
    }

    @Test
    public void testToModifyWithNull() throws Exception {
        Mockito.when(getter.invoke(instance, (Object[]) null)).thenReturn(null);
        Mockito.when(setterlessProperty.getType()).thenReturn(type);
        Mockito.when(setterlessProperty.getName()).thenReturn("testName");

        setterlessProperty.deserializeAndSet(jsonParser, context, instance);

        Mockito.verify(context).reportBadDefinition(type, "Problem deserializing 'setterless' property 'testName': get method returned null");
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        Object toModify = Mockito.mock(Object.class);
        Mockito.when(getter.invoke(instance, (Object[]) null)).thenReturn(toModify);

        setterlessProperty.deserializeAndSet(jsonParser, context, instance);

        Mockito.verify(deser).deserialize(jsonParser, context, toModify);
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        Object toModify = Mockito.mock(Object.class);
        Mockito.when(getter.invoke(instance, (Object[]) null)).thenReturn(toModify);

        Object result = setterlessProperty.deserializeSetAndReturn(jsonParser, context, instance);

        Mockito.verify(deser).deserialize(jsonParser, context, toModify);
        assertEquals(instance, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws Exception {
        setterlessProperty.set(instance, new Object());
    }

    @Test
    public void testSetAndReturn() throws Exception {
        Object result = setterlessProperty.setAndReturn(instance, new Object());

        assertEquals(instance, result);
    }
}