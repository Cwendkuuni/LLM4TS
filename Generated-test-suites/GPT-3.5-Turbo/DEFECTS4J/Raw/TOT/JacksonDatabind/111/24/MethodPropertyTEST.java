
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Method;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.MethodProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

import static org.mockito.Mockito.*;

public class MethodPropertyTest {

    @Test
    public void testDeserializeAndSet() throws IOException {
        // Arrange
        JsonParser mockJsonParser = mock(JsonParser.class);
        DeserializationContext mockDeserializationContext = mock(DeserializationContext.class);
        Object mockInstance = new Object();
        MethodProperty methodProperty = createMethodProperty();

        // Act
        methodProperty.deserializeAndSet(mockJsonParser, mockDeserializationContext, mockInstance);

        // Assert
        verify(mockJsonParser, times(1)).hasToken(JsonToken.VALUE_NULL);
        verify(mockDeserializationContext, times(1)).isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS);
        verify(methodProperty.getSetter(), times(1)).invoke(eq(mockInstance), any());
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        // Arrange
        JsonParser mockJsonParser = mock(JsonParser.class);
        DeserializationContext mockDeserializationContext = mock(DeserializationContext.class);
        Object mockInstance = new Object();
        MethodProperty methodProperty = createMethodProperty();

        // Act
        Object result = methodProperty.deserializeSetAndReturn(mockJsonParser, mockDeserializationContext, mockInstance);

        // Assert
        verify(mockJsonParser, times(1)).hasToken(JsonToken.VALUE_NULL);
        verify(mockDeserializationContext, times(1)).isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS);
        verify(methodProperty.getSetter(), times(1)).invoke(eq(mockInstance), any());
        Assert.assertEquals(mockInstance, result);
    }

    @Test
    public void testSet() throws IOException {
        // Arrange
        Object mockInstance = new Object();
        Object mockValue = new Object();
        MethodProperty methodProperty = createMethodProperty();

        // Act
        methodProperty.set(mockInstance, mockValue);

        // Assert
        verify(methodProperty.getSetter(), times(1)).invoke(mockInstance, mockValue);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        // Arrange
        Object mockInstance = new Object();
        Object mockValue = new Object();
        MethodProperty methodProperty = createMethodProperty();

        // Act
        Object result = methodProperty.setAndReturn(mockInstance, mockValue);

        // Assert
        verify(methodProperty.getSetter(), times(1)).invoke(mockInstance, mockValue);
        Assert.assertEquals(mockInstance, result);
    }

    private MethodProperty createMethodProperty() {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedMethod method = mock(AnnotatedMethod.class);
        Method setter = mock(Method.class);
        when(method.getAnnotated()).thenReturn(setter);
        return new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
    }
}
