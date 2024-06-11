package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.StandardDeserializer;
import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ArrayBuilders;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ExternalTypeHandlerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Mock
    private JsonDeserializer<String> stringDeserializer;
    @Mock
    private TypeDeserializer typeDeserializer;
    @Mock
    private SettableBeanProperty property;
    @InjectMocks
    private ExternalTypeHandler handler;
    @InjectMocks
    private ExternalTypeHandler.Builder builder;

    private DeserializationContext context;
    private JsonParser parser;

    @Before
    public void setup() throws IOException {
        context = mock(DeserializationContext.class);
        parser = mock(JsonParser.class);

        when(parser.nextToken()).thenCallRealMethod();

        // setup stringDeserializer as a mock for the sake of completeness
        when(stringDeserializer.createContextual(any(), any(), any())).thenReturn(context);
        when(stringDeserializer.getPropertyName()).thenReturn(PropertyName.construct(PropertyName.IDENTIFIER_PREFIX + "str"));

        when(typeDeserializer.getPropertyName()).thenReturn(PropertyName.construct(PropertyName.IDENTIFIER_PREFIX + "type"));
        when(typeDeserializer.getDefaultImpl()).thenReturn(String.class);

        builder.addExternal(property, stringDeserializer);
        builder.addExternal(property, typeDeserializer);
        handler = builder.build();
    }

    @Test
    public void testHandleTypePropertyValue_HandlesExternalTypeId() throws IOException {
        String expectedTypeId = "expectedTypeId";
        when(parser.getText()).thenReturn(expectedTypeId);

        handler.handleTypePropertyValue(parser, context, "propertyName", new Object());

        assertEquals(expectedTypeId, handler._typeIds[0]);
        assertNull(handler._tokens[0]);
    }

    @Test
    public void testHandleTypePropertyValue_PropertyDoesNotMatch_ReturnsFalse() throws IOException {
        when(parser.getText()).thenReturn("notMatchedTypeId");

        assertFalse(handler.handleTypePropertyValue(parser, context, "propertyName", new Object()));

        assertNull(handler._typeIds[0]);
        assertNull(handler._tokens[0]);
    }

    @Test
    public void testHandleTypePropertyValue_NoBean_ReturnsFalse() throws IOException {
        handler.handleTypePropertyValue(parser, context, "propertyName", null);

        assertNull(handler._typeIds[0]);
        assertNull(handler._tokens[0]);
    }

    @Test
    public void testHandleTypePropertyValue_NoTokens_DeserializesProperty() throws IOException {
        String expectedValue = "expectedValue";

        when(parser.getText()).thenCallRealMethod();
        when(parser.nextValue()).thenReturn(expectedValue);

        handler.handleTypePropertyValue(parser, context, "propertyName", new Object());

        assertNotNull(handler._tokens[0]);
        assertNull(handler._typeIds[0]);
        assertEquals(expectedValue, handler.getProperty().getValue(handler._tokens[0].asParser(parser)));
    }

    @Test
    public void testHandleTypePropertyValue_HasTokens_StoresTypeId() throws IOException {
        String expectedTypeId = "expectedTypeId";

        when(parser.getText()).thenReturn(expectedTypeId);
        when(parser.nextValue()).thenCallRealMethod();

        handler.handleTypePropertyValue(parser, context, "propertyName", new Object());

        assertNull(handler._tokens[0]);
        assertEquals(expectedTypeId, handler._typeIds[0]);
    }

    @Test
    public void testHandlePropertyValue_HandlesValue() throws IOException {
        String expectedValue = "expectedValue";

        when(parser.getText()).thenReturn(expectedValue);

        handler.handlePropertyValue(parser, context, "propertyName", new Object());

        assertNull(handler._typeIds[0]);
        assertSame(expectedValue, handler.getProperty().getValue(handler._tokens[0].asParser(parser)));
    }

    @Test
    public void testHandlePropertyValue_HasTypeId_StoresValue() throws IOException {
        String expectedValue = "expectedValue";
        String expectedTypeId = "expectedTypeId";

        when(parser.getText()).thenReturn(expectedTypeId);

        handler.handlePropertyValue(parser, context, "propertyName", new Object());
        handler.handlePropertyValue(parser, context, "propertyName", new Object());

        assertEquals(expectedTypeId, handler._typeIds[0]);
        assertEquals(expectedValue, handler.getProperty().getValue(handler._tokens[1].asParser(parser)));
    }

    @Test
    public void testComplete_HandlesTokensAndTypeIds() throws IOException {
        String expectedValue1 = "expectedValue1";
        String expectedValue2 = "expectedValue2";
        String expectedTypeId1 = "expectedTypeId1";
        String expectedTypeId2 = "expectedTypeId2";

        // setup first handler instance
        when(parser.getText()).thenReturn(expectedTypeId1);

        handler.handlePropertyValue(parser, context, "propertyName1", new Object());

        // setup second handler instance
        String[] typeIds = new String[2];
        TokenBuffer[] tokens = new TokenBuffer[2];
        handler = new ExternalTypeHandler(handler, typeIds, tokens);

        when(parser.getText()).thenReturn(expectedTypeId2);

        handler.handlePropertyValue(parser, context, "propertyName2", new Object());

        Object result = handler.complete(parser, context, new Object());

        Map<String, Object> expectedMap = new HashMap<>();
        expectedMap.put("propertyName1", expectedValue1);
        expectedMap.put("propertyName2", expectedValue2);

        assertEquals(expectedMap, BeanUtil.convertValueToMap(result));
    }

    @Test
    public void testComplete_HandlesNulls() throws IOException {
        handler.handleTypePropertyValue(parser, context, "propertyName1", null);
        handler.handlePropertyValue(parser, context, "propertyName1", null);

        handler.handleTypePropertyValue(parser, context, "propertyName2", null);
        handler.handlePropertyValue(parser, context, "propertyName2", null);

        Object result = handler.complete(parser, context, null);

        Map<String, Object> expectedMap = new HashMap<>();
        expectedMap.put("propertyName1", null);
        expectedMap.put("propertyName2", null);

        assertEquals(expectedMap, BeanUtil.convertValueToMap(result));
    }

    @Test
    public void testComplete_HandlesMissingProperties() throws IOException {
        handler.handleTypePropertyValue(parser, context, "propertyName1", null);
        handler.handlePropertyValue(parser, context, "propertyName1", null);

        handler.handleTypePropertyValue(parser, context, "propertyName2", null);
        handler.handlePropertyValue(parser, context, "propertyName3", null);

        assertThrows(JsonProcessingException.class, () -> handler.complete(parser, context, new Object()));
    }

    @Test
    public void testComplete_HandlesMissingTypeId() throws IOException {
        handler.handleTypePropertyValue(parser, context, "propertyName1", null);
        handler.handlePropertyValue(parser, context, "propertyName1", null);

        handler.handleTypePropertyValue(parser, context, "propertyName2", null);
        handler.handlePropertyValue(parser, context, "propertyName3", null);

        handler.handleTypePropertyValue(parser, context, "propertyName4", null);

        // setup typeDeserializer to return null for the missing property
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        when(typeDeserializer.deserializeTypedFromObject(any(), any())).thenReturn(null);

        handler = new ExternalTypeHandler(handler, null, null);
        handler._typeDeserializer = typeDeserializer;

        assertThrows(JsonProcessingException.class, () -> handler.complete(parser, context, new Object()));
    }

    @Test
    public void testComplete_HandlesMissingProperty_DefaultTypeId() throws IOException {
        handler.handleTypePropertyValue(parser, context, "propertyName1", null);
        handler.handlePropertyValue(parser, context, "propertyName1", null);

        handler.handleTypePropertyValue(parser, context, "propertyName2", null);
        handler.handlePropertyValue(parser, context, "propertyName3", null);

        handler.handleTypePropertyValue(parser, context, "propertyName4", null);

        // setup typeDeserializer to return default typeId for the missing property
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        String defaultTypeId = "defaultTypeId";
        when(typeDeserializer.getTypeIdResolver()).thenReturn(m -> defaultTypeId);
        when(typeDeserializer.getDefaultImpl()).thenReturn(String.class);
        when(typeDeserializer.getPropertyName()).thenReturn(PropertyName.construct(PropertyName.IDENTIFIER_PREFIX + "type"));

        handler = new ExternalTypeHandler(handler, null, null);
        handler._typeDeserializer = typeDeserializer;

        Object result = handler.complete(parser, context, new Object());

        Map<String, Object> expectedMap = new HashMap<>();
        expectedMap.put("propertyName1", null);
        expectedMap.put("propertyName2", null);
        expectedMap.put("propertyName3", null);
        expectedMap.put("propertyName4", defaultTypeId);

        assertEquals(expectedMap, BeanUtil.convertValueToMap(result));
    }

    @Test
    public void testComplete_HandlesMissingProperty_DefaultValue() throws IOException {
        handler.handleTypePropertyValue(parser, context, "propertyName1", null);
        handler.handlePropertyValue(parser, context, "propertyName1", null);

        handler.handleTypePropertyValue(parser, context, "propertyName2", null);
        handler.handlePropertyValue(parser, context, "propertyName3", null);

        handler.handleTypePropertyValue(parser, context, "propertyName4", null);

        // setup typeDeserializer to return default value for the missing property
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        when(typeDeserializer.getDefaultImpl()).thenReturn(String.class);
        when(typeDeserializer.getPropertyName()).thenReturn(PropertyName.construct(PropertyName.IDENTIFIER_PREFIX + "type"));

        handler = new ExternalTypeHandler(handler, null, null);
        handler._typeDeserializer = typeDeserializer;

        Object result = handler.complete(parser, context, new Object());

        Map<String, Object> expectedMap = new HashMap<>();
        expectedMap.put("propertyName1", null);
        expectedMap.put("propertyName2", null);
        expectedMap.put("propertyName3", null);
        expectedMap.put("propertyName4", "");

        assertEquals(expectedMap, BeanUtil.convertValueToMap(result));
    }

    @Test
    public void testComplete_HandlesMultipleProperties() throws IOException {
        when(parser.nextToken()).thenCallRealMethod();

        handler.handleTypePropertyValue(parser, context, "propertyName1", new Object());
        handler.handlePropertyValue(parser, context, "propertyName1", new Object());

        handler.handleTypePropertyValue(parser, context, "propertyName2", new Object());
        handler.handlePropertyValue(parser, context, "propertyName2", new Object());

        handler.handleTypePropertyValue(parser, context, "propertyName3", new Object());
        handler.handlePropertyValue(parser, context, "propertyName3", new Object());

        Object result = handler.complete(parser, context, new Object());

        List<Map<String, Object>> expectedList = Arrays.asList(
                Map.of("propertyName1", "propertyValue1"),
                Map.of("propertyName1", "propertyValue2"),
                Map.of("propertyName2", "propertyValue1"),
                Map.of("propertyName2", "propertyValue2"),
                Map.of("propertyName3", "propertyValue1"),
                Map.of("propertyName3", "propertyValue2")
        );

        List<Map<String, Object>> actualList = BeanUtil.mapper().convertValue(result, List.class);

        assertEquals(expectedList, actualList);
    }
}