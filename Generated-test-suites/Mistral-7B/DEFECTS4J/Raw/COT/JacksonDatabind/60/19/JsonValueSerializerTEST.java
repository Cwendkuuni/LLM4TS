package com.fasterxml.jackson.databind.ser.std; // Import statements go here

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema.DefaultSchemaNode;
import com.fasterxml.jackson.databind.jsonschema.JsonSchemaProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

@RunWith(MockitoJUnitRunner.class)
public class JsonValueSerializerTest {

    @Mock
    private JsonSerializer<Object> valueSerializer;

    @Mock
    private BeanProperty property;

    @Mock
    private SerializerProvider provider;

    @Mock
    private TypeSerializer typeSerializer;

    @Mock
    private JsonSchemaProvider jsonSchemaProvider;

    @InjectMocks
    private JsonValueSerializer jsonValueSerializer;

    private Object testObject;

    @Before
    public void setup() throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException {
        testObject = Mockito.mock(Class.forName("TestClass").newInstance());
        Method accessorMethod = TestClass.class.getDeclaredMethod("getValue");
        accessorMethod.setAccessible(true);

        jsonValueSerializer = new JsonValueSerializer(accessorMethod, valueSerializer);
    }

    @Test
    public void testSerialize() throws JsonMappingException, JsonProcessingException, IOException {
        when(accessorMethod.getValue(testObject)).thenReturn("testString");
        JsonGenerator gen = mock(JsonGenerator.class);

        jsonValueSerializer.serialize(testObject, gen, provider);

        assertEquals("testString", gen.getOutputString());
    }

    @Test
    public void testSerializeWithType() throws JsonMappingException, JsonProcessingException, IOException {
        when(accessorMethod.getValue(testObject)).thenReturn("testString");
        when(provider.findValueSerializer(String.class, property)).thenReturn(valueSerializer);

        JsonGenerator gen = mock(JsonGenerator.class);
        TypeSerializer typeSer = mock(TypeSerializer.class);

        jsonValueSerializer.serializeWithType(testObject, gen, provider, typeSer);

        assertEquals("testString", gen.getOutputString());
        verify(typeSer).writeTypePrefixForScalar(testObject, gen);
        verify(valueSerializer).serializeWithType("testString", gen, provider, typeSer);
        verify(typeSer).writeTypeSuffixForScalar(testObject, gen);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        when(valueSerializer.getSchema(provider, null)).thenReturn(DefaultSchemaNode.construct(JsonSchema.Type.STRING));

        JsonSchema schema = jsonValueSerializer.getSchema(provider, JsonType.INT.getRawClass());

        assertEquals(JsonSchema.Type.STRING, schema.getType());
    }

    @Test(expected = JsonMappingException.class)
    public void testExceptionHandling() throws JsonMappingException, JsonProcessingException, IOException {
        when(accessorMethod.getValue(testObject)).thenThrow(new RuntimeException("Test exception"));

        JsonGenerator gen = mock(JsonGenerator.class);

        jsonValueSerializer.serialize(testObject, gen, provider);
    }

    @Test(expected = JsonMappingException.class)
    public void testNaturalTypeSerialization() throws JsonMappingException, JsonProcessingException, IOException {
        when(accessorMethod.getValue(testObject)).thenReturn(123);

        JsonGenerator gen = mock(JsonGenerator.class);
        TypeSerializer typeSer = mock(TypeSerializer.class);

        jsonValueSerializer.serializeWithType(testObject, gen, provider, typeSer);

        verify(typeSer).writeTypePrefixForScalar(testObject, gen);
        verify(provider).findValueSerializer(Integer.class, property);
        verify(valueSerializer).serializeWithType(123, gen, provider, typeSer);
        verify(typeSer).writeTypeSuffixForScalar(testObject, gen);
    }

}

class TestClass {
    public Object getValue() {
        return null;
    }
}