import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.impl.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

@RunWith(MockitoJUnitRunner.class)
public class SettableAnyPropertyTest {

    @Mock
    private BeanProperty property;

    @Mock
    private AnnotatedField setterField;

    @Mock
    private AnnotatedMethod setterMethod;

    @Mock
    private DeserializationConfig config;

    @Mock
    private JsonParser parser;

    @Mock
    private DeserializationContext ctxt;

    @Mock
    private UnresolvedForwardReference reference;

    @Mock
    private Referring referring;

    private SettableAnyProperty settableAnyProperty;

    @Test
    public void testDeserializeAndSet() throws IOException {
        settableAnyProperty = new SettableAnyProperty(property, setterField, null, null, null, null);

        Map<Object, Object> instance = new HashMap<>();
        String propName = "test";
        JsonNode value = mock(JsonNode.class);

        settableAnyProperty.deserializeAndSet(parser, ctxt, instance, propName);

        assertEquals(value, instance.get(propName));
    }

    @Test
    public void testSet_SetterField() throws IOException {
        settableAnyProperty = new SettableAnyProperty(property, setterField, null, null, null, null);

        Map<Object, Object> instance = new HashMap<>();
        Object key = "test";
        Object value = "value";

        settableAnyProperty.set(instance, key, value);

        assertEquals(value, instance.get(key));
    }

    @Test
    public void testSet_SetterMethod() throws IOException {
        settableAnyProperty = new SettableAnyProperty(property, setterMethod, null, null, null, null);

        Object instance = new Object();
        Object key = "test";
        Object value = "value";

        settableAnyProperty.set(instance, key, value);

        verify(setterMethod, times(1)).callOnWith(instance, key, value);
    }

    @Test(expected = JsonMappingException.class)
    public void testThrowAsIOE() throws IOException {
        settableAnyProperty = new SettableAnyProperty(property, setterMethod, null, null, null, null);

        Exception e = new IllegalArgumentException();
        Object propName = "test";
        Object value = new Object();

        settableAnyProperty._throwAsIOE(e, propName, value);
    }

    @Test
    public void testReadResolve() {
        settableAnyProperty = new SettableAnyProperty(property, setterField, null, null, null, null);

        settableAnyProperty._setter = setterField;

        assertEquals(settableAnyProperty, settableAnyProperty.readResolve());
    }
}