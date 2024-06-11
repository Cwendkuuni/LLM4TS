import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MethodPropertyTest {

    private MethodProperty methodProperty;
    private BeanPropertyDefinition propDef;
    private JavaType type;
    private TypeDeserializer typeDeser;
    private Annotations contextAnnotations;
    private AnnotatedMethod method;

    @Before
    public void setup() {
        propDef = mock(BeanPropertyDefinition.class);
        type = mock(JavaType.class);
        typeDeser = mock(TypeDeserializer.class);
        contextAnnotations = mock(Annotations.class);
        method = mock(AnnotatedMethod.class);

        methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
    }

    @Test
    public void testConstructor() {
        assertEquals(propDef, methodProperty.getMember());

        // Test other getters
        assertEquals(propDef, methodProperty.getMember());
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider nva = mock(NullValueProvider.class);
        SettableBeanProperty newProperty = methodProperty.withNullProvider(nva);

        assertEquals(nva, newProperty.getNullValueProvider());
    }

    // Add more test cases for other methods

    // Edge cases and exception handling scenarios

    @Test
    public void testDeserializeAndSet_ValueNull_SkipNulls() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object instance = new Object();

        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
        when(methodProperty.getNullValueProvider()).thenReturn(mock(NullValueProvider.class));
        methodProperty.setSkipNulls(true);

        methodProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        // Verify that the property is not set
        verifyZeroInteractions(method);
    }

    // Add more test cases for other edge cases and exception handling scenarios

}