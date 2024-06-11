
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;

import static org.mockito.Mockito.*;

public class ExternalTypeHandlerTest {

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        // Given
        ExternalTypeHandler.ExtTypedProperty[] properties = new ExternalTypeHandler.ExtTypedProperty[1];
        properties[0] = mock(ExternalTypeHandler.ExtTypedProperty.class);
        HashMap<String, Integer> nameToPropertyIndex = new HashMap<>();
        nameToPropertyIndex.put("propName", 0);
        String[] typeIds = new String[1];
        TokenBuffer[] tokens = new TokenBuffer[1];
        ExternalTypeHandler handler = new ExternalTypeHandler(properties, nameToPropertyIndex, typeIds, tokens);

        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        String propName = "propName";
        Object bean = new Object();

        // When
        handler.handleTypePropertyValue(jp, ctxt, propName, bean);

        // Then
        verify(properties[0]).hasTypePropertyName(propName);
        verifyNoMoreInteractions(properties[0]);
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        // Given
        ExternalTypeHandler.ExtTypedProperty[] properties = new ExternalTypeHandler.ExtTypedProperty[1];
        properties[0] = mock(ExternalTypeHandler.ExtTypedProperty.class);
        HashMap<String, Integer> nameToPropertyIndex = new HashMap<>();
        nameToPropertyIndex.put("propName", 0);
        String[] typeIds = new String[1];
        TokenBuffer[] tokens = new TokenBuffer[1];
        ExternalTypeHandler handler = new ExternalTypeHandler(properties, nameToPropertyIndex, typeIds, tokens);

        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        String propName = "propName";
        Object bean = new Object();

        // When
        handler.handlePropertyValue(jp, ctxt, propName, bean);

        // Then
        verify(properties[0]).hasTypePropertyName(propName);
        verifyNoMoreInteractions(properties[0]);
    }

    @Test
    public void testComplete() throws IOException {
        // Given
        ExternalTypeHandler.ExtTypedProperty[] properties = new ExternalTypeHandler.ExtTypedProperty[1];
        properties[0] = new ExternalTypeHandler.ExtTypedProperty(mock(SettableBeanProperty.class), mock(TypeDeserializer.class));
        HashMap<String, Integer> nameToPropertyIndex = new HashMap<>();
        nameToPropertyIndex.put("propName", 0);
        String[] typeIds = new String[1];
        typeIds[0] = "typeId";
        TokenBuffer[] tokens = new TokenBuffer[1];
        ExternalTypeHandler handler = new ExternalTypeHandler(properties, nameToPropertyIndex, typeIds, tokens);

        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object bean = new Object();

        // When
        handler.complete(p, ctxt, bean);

        // Then
        verify(p, times(1)).asParser(any(TokenBuffer.class));
        verify(properties[0].getProperty(), times(1)).deserialize(any(JsonParser.class), any(DeserializationContext.class));
    }

    @Test
    public void testCompleteWithPropertyValueBuffer() throws IOException {
        // Given
        ExternalTypeHandler.ExtTypedProperty[] properties = new ExternalTypeHandler.ExtTypedProperty[1];
        properties[0] = new ExternalTypeHandler.ExtTypedProperty(mock(SettableBeanProperty.class), mock(TypeDeserializer.class));
        HashMap<String, Integer> nameToPropertyIndex = new HashMap<>();
        nameToPropertyIndex.put("propName", 0);
        String[] typeIds = new String[1];
        typeIds[0] = "typeId";
        TokenBuffer[] tokens = new TokenBuffer[1];
        ExternalTypeHandler handler = new ExternalTypeHandler(properties, nameToPropertyIndex, typeIds, tokens);

        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        PropertyValueBuffer buffer = mock(PropertyValueBuffer.class);
        PropertyBasedCreator creator = mock(PropertyBasedCreator.class);

        // When
        handler.complete(p, ctxt, buffer, creator);

        // Then
        verify(p, times(1)).asParser(any(TokenBuffer.class));
        verify(properties[0].getProperty(), times(1)).deserialize(any(JsonParser.class), any(DeserializationContext.class));
        verify(creator, times(1)).findCreatorProperty(anyString());
        verify(buffer, times(1)).assignParameter(any(SettableBeanProperty.class), anyObject());
        verify(creator, times(1)).build(ctxt, buffer);
        verify(properties[0], times(1)).getProperty();
        verify(properties[0].getProperty(), times(2)).getName();
        verify(properties[0].getTypeDeserializer(), times(1)).getDefaultImpl();
        verify(properties[0].getTypeDeserializer().getTypeIdResolver(), times(1)).idFromValueAndType(null, null);
        verify(properties[0].getProperty(), times(1)).set(anyObject(), anyObject());
    }
}
