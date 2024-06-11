package com.example.aswrappertypedeserializertest;

import com.fasterxml.jackson.core.BaseJsonParser;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.lang.reflect.Type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class AsWrapperTypeDeserializerTest {

    @Mock
    private DeserializationContext ctxt;

    @Mock
    private TypeIdResolver idRes;

    @Mock
    private JsonParser jp;

    @Mock
    private BaseJsonParser baseJp;

    @Captor
    private ArgumentCaptor<DeserializationContext> ctxtCaptor;

    @Captor
    private ArgumentCaptor<Type> typeCaptor;

    private ObjectMapper objectMapper;

    private AsWrapperTypeDeserializer asWrapperTypeDeserializer;

    @Before
    public void setup() throws Exception {
        objectMapper = new ObjectMapper();
        JavaType javaType = Mockito.mock(JavaType.class);
        asWrapperTypeDeserializer = new AsWrapperTypeDeserializer(javaType, idRes, "_typePropertyName", true, Object.class);
    }

    @Test
    public void testDeserializeTypedFromObject_ValidInput() throws IOException {
        Mockito.when(jp.isExpectedStartElement(Mockito.eq(JsonToken.START_OBJECT)))
                .thenReturn(true);
        Mockito.when(jp.nextToken()).thenReturn(JsonToken.FIELD_NAME);
        Mockito.when(jp.getText()).thenReturn("typeId");
        Mockito.when(idRes.findDeserializer(ctxt, "typeId")).thenReturn(Mockito.mock(TypeDeserializer.class));

        Object result = asWrapperTypeDeserializer.deserializeTypedFromObject(jp, ctxt);
        Mockito.verify(jp).nextToken(); // should consume END_OBJECT
        Mockito.verify(jp).close();
        assertEquals(Object.class, result.getClass());
    }

    @Test(expected = JsonParseException.class)
    public void testDeserializeTypedFromObject_InvalidJsonFormat() throws IOException {
        Mockito.when(jp.isExpectedStartElement(Mockito.eq(JsonToken.START_OBJECT)))
                .thenReturn(false);
        asWrapperTypeDeserializer.deserializeTypedFromObject(jp, ctxt);
    }

    @Test(expected = JsonParseException.class)
    public void testDeserializeTypedFromObject_MissingTypeIdField() throws IOException {
        Mockito.when(jp.isExpectedStartElement(Mockito.eq(JsonToken.START_OBJECT)))
                .thenReturn(true);
        Mockito.when(jp.nextToken()).thenReturn(JsonToken.FIELD_NAME);
        Mockito.when(jp.getText()).thenReturn("name");
        asWrapperTypeDeserializer.deserializeTypedFromObject(jp, ctxt);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeTypedFromObject_DeserializationError() throws IOException {
        Mockito.when(jp.isExpectedStartElement(Mockito.eq(JsonToken.START_OBJECT)))
                .thenReturn(true);
        Mockito.when(jp.nextToken()).thenReturn(JsonToken.FIELD_NAME);
        Mockito.when(jp.getText()).thenReturn("typeId");
        Mockito.when(idRes.findDeserializer(ctxt, "typeId")).thenThrow(JsonMappingException.class);

        asWrapperTypeDeserializer.deserializeTypedFromObject(jp, ctxt);
    }

    @Test
    public void testDeserialize_WithNativeTypeId() throws IOException {
        Mockito.when(jp.canReadTypeId()).thenReturn(true);
        Mockito.when(jp.getTypeId()).thenReturn("id");
        TypeDeserializer deserializer = Mockito.mock(TypeDeserializer.class);
        Mockito.when(idRes.findDeserializer(ctxt, "id")).thenReturn(deserializer);

        Object result = asWrapperTypeDeserializer.deserializeTypedFromObject(jp, ctxt);
        Mockito.verify(deserializer).deserialize(baseJp, ctxtCaptor.capture());
        assertEquals(ctxt, ctxtCaptor.getValue());
        assertNull(result); // asWrapperTypeDeserializer does not deserialize the value itself
    }
}