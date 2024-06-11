import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;

public class StringArrayDeserializerTest {
    
    private StringArrayDeserializer stringArrayDeserializer;
    
    @Before
    public void setUp() {
        stringArrayDeserializer = new StringArrayDeserializer();
    }
    
    @Test
    public void testDeserialize_ValidArray() {
        String[] expected = {"Hello", "World", "JUnit"};
        String[] actual = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testDeserialize_EmptyArray() {
        String[] expected = {};
        String[] actual = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testDeserialize_NullValue() {
        String[] expected = {null};
        String[] actual = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testDeserialize_NonArrayInput() {
        String[] expected = null;
        String[] actual = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testDeserialize_CustomDeserializer() {
        JsonDeserializer<String> customDeserializer = new CustomStringDeserializer();
        StringArrayDeserializer stringArrayDeserializer = new StringArrayDeserializer(customDeserializer);
        String[] expected = {"Hello", "World", "Custom"};
        String[] actual = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testDeserializeWithType() {
        TypeDeserializer mockTypeDeserializer = Mockito.mock(TypeDeserializer.class);
        Object expected = new Object();
        Mockito.when(mockTypeDeserializer.deserializeTypedFromArray(jsonParser, deserializationContext)).thenReturn(expected);
        Object actual = stringArrayDeserializer.deserializeWithType(jsonParser, deserializationContext, mockTypeDeserializer);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testHandleNonArray_AcceptSingleValue_AsArrayEnabled() {
        DeserializationFeature.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        String[] expected = {"Hello"};
        String[] actual = stringArrayDeserializer.handleNonArray(jsonParser, deserializationContext);
        assertArrayEquals(expected, actual);
    }
    
    @Test(expected = IOException.class)
    public void testHandleNonArray_AcceptSingleValue_AsArrayDisabled() {
        DeserializationFeature.disable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        stringArrayDeserializer.handleNonArray(jsonParser, deserializationContext);
    }
    
    @Test
    public void testContextual() {
        BeanProperty mockProperty = Mockito.mock(BeanProperty.class);
        JsonDeserializer<?> expected = new CustomStringDeserializer();
        Mockito.when(deserializationContext.findConvertingContentDeserializer(deserializationContext, mockProperty, null)).thenReturn(expected);
        JsonDeserializer<?> actual = stringArrayDeserializer.contextual(deserializationContext, mockProperty);
        assertEquals(expected, actual);
    }
}