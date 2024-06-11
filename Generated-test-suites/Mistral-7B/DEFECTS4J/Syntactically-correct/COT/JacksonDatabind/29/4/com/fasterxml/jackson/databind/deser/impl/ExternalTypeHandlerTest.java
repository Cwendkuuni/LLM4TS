import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import junit.framework.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.mockito.Mockito.*;

public class ExternalTypeHandlerTest {

    @Test
    public void testHandleTypePropertyValueSuccess() throws Exception {
        // Given
        Object bean = new Object();
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        String propName = "testProperty";
        ExtTypedProperty testProperty = new ExtTypedProperty(mock(SettableBeanProperty.class), mock(TypeDeserializer.class));
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{testProperty},
                new HashMap<String, Integer>() {{ put(propName, 0); }},
                new String[]{}, new TokenBuffer[0]);

        // When
        handler.handleTypePropertyValue(jp, ctxt, propName, bean);

        // Then
        verify(jp, times(1)).getText();
        verifyNoMoreInteractions(jp);
        verify(ctxt).handleUnknownProperty(eq(bean), eq(propName));
    }

    @Test(expected = IOException.class)
    public void testHandleTypePropertyValueFailure() throws Exception {
        // Given
        Object bean = new Object();
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        String propName = "testProperty";
        ExtTypedProperty testProperty = new ExtTypedProperty(mock(SettableBeanProperty.class), mock(TypeDeserializer.class));
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{testProperty},
                new HashMap<String, Integer>() {{ put(propName, 0); }},
                new String[]{}, new TokenBuffer[0]);

        // When
        handler.handleTypePropertyValue(jp, ctxt, "wrongPropertyName", bean);

        // Then
        verify(jp, never()).getText();
        verify(ctxt, times(1)).mappingException(containsString("Missing external type id property 'wrongPropertyName'"));
    }

    @Test
    public void testHandlePropertyValueSuccess() throws Exception {
        // Given
        Object bean = new Object();
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        String propName = "testProperty";
        ExtTypedProperty testProperty = new ExtTypedProperty(mock(SettableBeanProperty.class), mock(TypeDeserializer.class));
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{testProperty},
                new HashMap<String, Integer>() {{ put(propName, 0); }},
                new String[]{}, new TokenBuffer[0]);

        // When
        handler.handlePropertyValue(jp, ctxt, propName, bean);

        // Then
        verify(jp, times(1)).nextToken();
        verify(jp, times(1)).getText();
        verifyNoMoreInteractions(jp);
        verify(ctxt).handleUnknownProperty(eq(bean), eq(propName));
    }

    @Test(expected = IOException.class)
    public void testHandlePropertyValueFailure() throws Exception {
        // Given
        Object bean = new Object();
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        String propName = "testProperty";
        ExtTypedProperty testProperty = new ExtTypedProperty(mock(SettableBeanProperty.class), mock(TypeDeserializer.class));
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{testProperty},
                new HashMap<String, Integer>() {{ put(propName, 0); }},
                new String[]{}, new TokenBuffer[0]);

        // When
        handler.handlePropertyValue(jp, ctxt, "wrongPropertyName", bean);

        // Then
        verify(jp, times(1)).nextToken();
        verify(jp, never()).getText();
        verify(ctxt, times(1)).mappingException(containsString("Missing property 'wrongPropertyName' for external type id 'testProperty'"));
    }

    @Test
    public void testCompleteSuccess() throws Exception {
        // Given
        Object bean = new Object();
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        ExtTypetypedProperty[] properties = new ExtTypedProperty[]{
                new ExtTypedProperty(mock(SettableBeanProperty.class), mock(TypeDeserializer.class)),
                new ExtTypedProperty(mock(SettableBeanProperty.class), mock(TypeDeserializer.class))
        };
        HashMap<String, Integer> nameToPropertyIndex = new HashMap<String, Integer>() {{
            put("property1", 0);
            put("property2", 1);
        }};
        String[] typeIds = new String[]{"type1", "type2"};
        TokenBuffer[] tokens = new TokenBuffer[2];
        ExternalTypeHandler handler = new ExternalTypeHandler(properties,
                nameToPropertyIndex, typeIds, tokens);

        // When
        handler.complete(jp, ctxt, bean);

        // Then
        for (int i = 0; i < properties.length; i++) {
            ExtTypedProperty extTypedProperty = properties[i];
            SettableBeanProperty property = extTypedProperty.getProperty();
            String typeId = typeIds[i];
            Object value = property.getType().isPrimitive() ? 0 : new Object();
            verify(ctxt, times(1)).getValueProcessor(property.getType());
            verify(ctxt, times(1)).handleComplexValue(eq(bean), eq(property), eq(value), anyObject());
            if (typeId != null) {
                verify(jp, times(1)).getText();
                verify(property, times(1)).deserialize(jp, ctxt);
            }
        }
    }

    @Test(expected = IOException.class)
    public void testCompleteMissingProperty() throws Exception {
        // Given
        Object bean = new Object();
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        ExtTypetypedProperty[] properties = new ExtTypedProperty[]{
                new ExtTypedProperty(mock(SettableBeanProperty.class), mock(TypeDeserializer.class))
        };
        HashMap<String, Integer> nameToPropertyIndex = new HashMap<String, Integer>() {{
            put("property1", 0);
        }};
        String[] typeIds = new String[]{"type1"};
        TokenBuffer[] tokens = new TokenBuffer[1];
        ExternalTypeHandler handler = new ExternalTypeHandler(properties,
                nameToPropertyIndex, typeIds, tokens);

        // When
        handler.complete(jp, ctxt, bean);

        // Then
        SettableBeanProperty property = properties[0].getProperty();
        String propertyName = property.getName();
        verify(ctxt, times(1)).mappingException(containsString("Missing property '"));
        verify(ctxt, times(1)).mappingException(containsString(propertyName));
        verify(ctxt, times(1)).mappingException(containsString("for external type id '"));
        verify(ctxt, times(1)).mappingException(containsString("'"));
    }

    @Test(expected = IOException.class)
    public void testCompleteMissingTypeId() throws Exception {
        // Given
        Object bean = new Object();
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        ExtTypetypedProperty[] properties = new ExtTypedProperty[]{
                new ExtTypedProperty(mock(SettableBeanProperty.class), mock(TypeDeserializer.class))
        };
        HashMap<String, Integer> nameToPropertyIndex = new HashMap<String, Integer>() {{
            put("property1", 0);
        }};
        String[] typeIds = new String[0];
        TokenBuffer[] tokens = new TokenBuffer[1];
        ExternalTypeHandler handler = new ExternalTypeHandler(properties,
                nameToPropertyIndex, typeIds, tokens);

        // When
        handler.complete(jp, ctxt, bean);

        // Then
        SettableBeanProperty property = properties[0].getProperty();
        String propertyName = property.getName();
        verify(ctxt, times(1)).mappingException(containsString("Missing external type id property '"));
        verify(ctxt, times(1)).mappingException(containsString(propertyName));
    }

    @Test(expected = IOException.class)
    public void testCompleteNullBean() throws Exception {
        // Given
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[0],
                new HashMap<String, Integer>(), new String[0], new TokenBuffer[0]);

        // When
        handler.complete(jp, ctxt, null);

        // Then
        verify(ctxt, times(1)).mappingException(containsString("Cannot construct instance"));
    }

    @Test(expected = IOException.class)
    public void testHandlePropertyValueNoTypeId() throws Exception {
        // Given
        Object bean = new Object();
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        ExtTypedProperty testProperty = new ExtTypedProperty(mock(SettableBeanProperty.class), mock(TypeDeserializer.class));
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{testProperty},
                new HashMap<String, Integer>() {{ put("testProperty", 0); }},
                new String[0], new TokenBuffer[0]);

        // When
        handler.handlePropertyValue(jp, ctxt, "testProperty", bean);

        // Then
        verify(jp, times(1)).nextToken();
        verify(ctxt, times(1)).mappingException(containsString("Missing external type id property 'testProperty'"));
    }

    @Test(expected = IOException.class)
    public void testHandlePropertyValueNullTokens() throws Exception {
        // Given
        Object bean = new Object();
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        ExtTypedProperty testProperty = new ExtTypedProperty(mock(SettableBeanProperty.class), mock(TypeDeserializer.class));
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{testProperty},
                new HashMap<String, Integer>() {{ put("testProperty", 0); }},
                null, new TokenBuffer[0]);

        // When
        handler.handlePropertyValue(jp, ctxt, "testProperty", bean);

        // Then
        verify(jp, times(1)).nextToken();
        verify(ctxt, times(1)).mappingException(containsString("Missing property 'testProperty' for external type id 'testProperty'"));
    }
}