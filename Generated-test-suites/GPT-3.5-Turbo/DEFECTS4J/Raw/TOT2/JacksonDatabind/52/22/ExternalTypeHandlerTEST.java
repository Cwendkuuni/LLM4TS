import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

@RunWith(MockitoJUnitRunner.class)
public class ExternalTypeHandlerTest {

    @Mock
    JsonParser jsonParser;

    @Mock
    DeserializationContext deserializationContext;

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler.Builder().build();
        
        when(jsonParser.getText()).thenReturn("typeId");
        
        assertFalse(externalTypeHandler.handleTypePropertyValue(jsonParser, deserializationContext, "propName", null));
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler.Builder().build();
        
        TokenBuffer tokenBuffer = mock(TokenBuffer.class);
        when(tokenBuffer.firstToken()).thenReturn(null);
        
        when(jsonParser.getText()).thenReturn("typeId");
        when(tokenBuffer.asParser(jsonParser)).thenReturn(jsonParser);
        
        assertFalse(externalTypeHandler.handlePropertyValue(jsonParser, deserializationContext, "propName", null));
    }

    @Test
    public void testComplete() throws IOException {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler.Builder().build();
        
        TokenBuffer tokenBuffer = mock(TokenBuffer.class);
        when(tokenBuffer.firstToken()).thenReturn(JsonToken.VALUE_STRING);
        
        when(tokenBuffer.asParser(jsonParser)).thenReturn(jsonParser);
        
        ExtTypedProperty property = mock(ExtTypedProperty.class);
        SettableBeanProperty settableBeanProperty = mock(SettableBeanProperty.class);
        Object bean = new Object();
        
        ExternalTypeHandler.ExtTypedProperty[] properties = { property };
        
        externalTypeHandler.complete(jsonParser, deserializationContext, bean);
        
        verify(property, times(1)).getProperty();
    }

    @Test
    public void testCompleteWithCreator() throws IOException {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler.Builder().build();

        PropertyValueBuffer buffer = mock(PropertyValueBuffer.class);
        PropertyBasedCreator creator = new PropertyBasedCreator() {
            public Object build(DeserializationContext ctxt, PropertyValueBuffer buffer) throws IOException {
                return new Object();
            }
        };

        ExternalTypeHandler.ExtTypedProperty prop = mock(ExternalTypeHandler.ExtTypedProperty.class);
        SettableBeanProperty settableBeanProperty = mock(SettableBeanProperty.class);
        Object bean = new Object();

        ExternalTypeHandler.ExtTypedProperty[] properties = { prop };

        externalTypeHandler.complete(jsonParser, deserializationContext, buffer, creator);

        verify(prop, times(1)).getProperty();
    }

    @Test(expected = JsonParseException.class)
    public void testDeserializeThrowsJsonParseException() throws IOException {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler.Builder().build();

        TokenBuffer tokenBuffer = mock(TokenBuffer.class);
        when(tokenBuffer.asParser(jsonParser)).thenReturn(jsonParser);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        
        ExternalTypeHandler.ExtTypedProperty property = mock(ExternalTypeHandler.ExtTypedProperty.class);
        SettableBeanProperty settableBeanProperty = mock(SettableBeanProperty.class);
        Object bean = new Object();

        ExternalTypeHandler.ExtTypedProperty[] properties = { property };

        externalTypeHandler._deserialize(jsonParser, deserializationContext, 0, "typeId");
    }

    @Test(expected = JsonParseException.class)
    public void testDeserializeAndSetThrowsJsonParseException() throws IOException {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler.Builder().build();

        TokenBuffer tokenBuffer = mock(TokenBuffer.class);
        when(tokenBuffer.asParser(jsonParser)).thenReturn(jsonParser);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        
        ExternalTypeHandler.ExtTypedProperty property = mock(ExternalTypeHandler.ExtTypedProperty.class);
        SettableBeanProperty settableBeanProperty = mock(SettableBeanProperty.class);
        Object bean = new Object();

        ExternalTypeHandler.ExtTypedProperty[] properties = { property };

        externalTypeHandler._deserializeAndSet(jsonParser, deserializationContext, bean, 0, "typeId");
    }

}