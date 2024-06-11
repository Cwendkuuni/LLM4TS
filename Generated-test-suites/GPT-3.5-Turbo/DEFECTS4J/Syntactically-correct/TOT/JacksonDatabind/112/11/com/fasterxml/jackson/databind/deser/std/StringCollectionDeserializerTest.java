
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import static org.mockito.Mockito.when;

public class StringCollectionDeserializerTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testDeserialize_WhenValueDeserializerIsNull() throws IOException {
        // Arrange
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);
        ValueInstantiator valueInstantiator = Mockito.mock(ValueInstantiator.class);
        BeanProperty beanProperty = Mockito.mock(BeanProperty.class);
        JavaType javaType = Mockito.mock(JavaType.class);
        when(javaType.getContentType()).thenReturn(Mockito.mock(JavaType.class));
        NullValueProvider nullValueProvider = Mockito.mock(NullValueProvider.class);
        when(valueInstantiator.createUsingDefault(deserializationContext)).thenReturn(Collections.emptyList());
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(javaType, null, valueInstantiator);

        // Act
        Collection<String> result = deserializer.deserialize(jsonParser, deserializationContext);

        // Assert
        Assert.assertNotNull(result);
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testDeserialize_WhenValueDeserializerIsNotNull() throws IOException {
        // Arrange
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);
        ValueInstantiator valueInstantiator = Mockito.mock(ValueInstantiator.class);
        BeanProperty beanProperty = Mockito.mock(BeanProperty.class);
        JavaType javaType = Mockito.mock(JavaType.class);
        when(javaType.getContentType()).thenReturn(Mockito.mock(JavaType.class));
        NullValueProvider nullValueProvider = Mockito.mock(NullValueProvider.class);
        when(valueInstantiator.createUsingDefault(deserializationContext)).thenReturn(Collections.emptyList());
        when(jsonParser.nextTextValue()).thenReturn("Value1", "Value2", null);
        JsonDeserializer<String> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        when(valueDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn("Value1", "Value2", "Value3");
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(javaType, valueDeserializer, valueInstantiator);

        // Act
        Collection<String> result = deserializer.deserialize(jsonParser, deserializationContext);

        // Assert
        Assert.assertNotNull(result);
        Assert.assertEquals(3, result.size());
        Assert.assertTrue(result.containsAll(Arrays.asList("Value1", "Value2", "Value3")));
    }

    @Test
    public void testDeserialize_WhenDelegateDeserializerIsNotNull() throws IOException {
        // Arrange
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);
        ValueInstantiator valueInstantiator = Mockito.mock(ValueInstantiator.class);
        BeanProperty beanProperty = Mockito.mock(BeanProperty.class);
        JavaType javaType = Mockito.mock(JavaType.class);
        when(javaType.getContentType()).thenReturn(Mockito.mock(JavaType.class));
        NullValueProvider nullValueProvider = Mockito.mock(NullValueProvider.class);
        when(valueInstantiator.createUsingDelegate(deserializationContext, null)).thenReturn(Collections.emptyList());
        JsonDeserializer<Object> delegateDeserializer = Mockito.mock(JsonDeserializer.class);
        when(delegateDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn(Collections.emptyList());
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(javaType, valueInstantiator, delegateDeserializer, null, nullValueProvider, Boolean.FALSE);

        // Act
        Collection<String> result = deserializer.deserialize(jsonParser, deserializationContext);

        // Assert
        Assert.assertNotNull(result);
        Assert.assertTrue(result.isEmpty());
    }

}

