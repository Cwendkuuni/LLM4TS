import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.SettableAnyProperty;
import com.fasterxml.jackson.databind.deser.impl.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SettableAnyPropertyTest {

    @Mock
    DeserializationConfig deserializationConfigMock;
    @Mock
    JsonParser jsonParserMock;
    @Mock
    DeserializationContext deserializationContextMock;

    private SettableAnyProperty settableAnyProperty;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        settableAnyProperty = new SettableAnyProperty(null, null, null, null, null, null);
    }

    @Test
    public void testDeserializeAndSet_WithValidKeyAndValue_ShouldSetSuccessfully() throws IOException {
        Object instance = new Object();
        String propName = "testProp";
        Map<Object, Object> mapInstance = new HashMap<>();
        settableAnyProperty = new SettableAnyProperty(null, null, null, null, null, null);
        Mockito.when(jsonParserMock.getCurrentToken()).thenReturn(null);

        settableAnyProperty.set(instance, propName, 123);

        assert(mapInstance.get(propName) == 123);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeAndSet_WithUnresolvedForwardReference_ShouldThrowJsonMappingException() throws IOException {
        Object instance = new Object();
        String propName = "testProp";
        UnresolvedForwardReference reference = new UnresolvedForwardReference(null, null);
        settableAnyProperty = new SettableAnyProperty(null, null, null, null, null, null);
        Mockito.when(jsonParserMock.getCurrentToken()).thenReturn(null);
        Mockito.when(jsonParserMock.getText()).thenReturn("test");
        Mockito.when(jsonParserMock.readValuesAs(null)).thenThrow(JsonParseException.class);

        settableAnyProperty.deserializeAndSet(jsonParserMock, deserializationContextMock, instance, propName);
    }

    @Test
    public void testSet_WithAnnotatedMethod_ShouldCallMethodSuccessfully() throws IOException {
        Object instance = new Object();
        String propName = "testProp";
        settableAnyProperty = new SettableAnyProperty(null, null, null, null, null, null);
        AnnotatedMethod setterMethodMock = Mockito.mock(AnnotatedMethod.class);
        Mockito.when(setterMethodMock.callOnWith(instance, propName, 123)).thenReturn(null);
        settableAnyProperty.set(instance, propName, 123);
        Mockito.verify(setterMethodMock, Mockito.times(1)).callOnWith(instance, propName, 123);
    }

    @Test(expected = IOException.class)
    public void test_throwAsIOE_WithIllegalArgumentException_ShouldThrowJsonMappingException() throws IOException {
        settableAnyProperty = new SettableAnyProperty(null, null, null, null, null, null);
        Object propName = "testProp";
        Object value = new Object();

        try {
            throw new IllegalArgumentException();
        } catch (IllegalArgumentException e) {
            settableAnyProperty._throwAsIOE(e, propName, value);
        }
    }

    @Test(expected = IOException.class)
    public void test_throwAsIOE_WithRuntimeException_ShouldThrowJsonMappingException() throws IOException {
        settableAnyProperty = new SettableAnyProperty(null, null, null, null, null, null);
        Object propName = "testProp";
        Object value = new Object();

        try {
            throw new RuntimeException();
        } catch (RuntimeException e) {
            settableAnyProperty._throwAsIOE(e, propName, value);
        }
    }

    @Test
    public void test_getClassName_ShouldReturnClassName() {
        settableAnyProperty = new SettableAnyProperty(null, null, null, null, null, null);
        assert(settableAnyProperty.getClassName()).isNotNull();
    }

}