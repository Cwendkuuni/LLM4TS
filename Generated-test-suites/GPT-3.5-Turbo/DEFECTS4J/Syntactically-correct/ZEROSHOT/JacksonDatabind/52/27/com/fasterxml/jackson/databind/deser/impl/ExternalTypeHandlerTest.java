import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class ExternalTypeHandlerTest {
    
    private ExternalTypeHandler.ExtTypedProperty[] _properties;
    private HashMap<String, Integer> _nameToPropertyIndex;
    private String[] _typeIds;
    private TokenBuffer[] _tokens;
    
    private ExternalTypeHandler externalTypeHandler;
    private JsonParser jsonParser;
    private DeserializationContext deserializationContext;
    
    @Before
    public void setUp() {
        _properties = new ExternalTypeHandler.ExtTypedProperty[1];
        _properties[0] = new ExternalTypeHandler.ExtTypedProperty(mock(SettableBeanProperty.class), mock(TypeDeserializer.class));
        
        _nameToPropertyIndex = new HashMap<String, Integer>();
        _nameToPropertyIndex.put("property1", 0);
        
        _typeIds = new String[1];
        _tokens = new TokenBuffer[1];
        
        externalTypeHandler = new ExternalTypeHandler(_properties, _nameToPropertyIndex, _typeIds, _tokens);
        jsonParser = mock(JsonParser.class);
        deserializationContext = mock(DeserializationContext.class);
    }
    
    @Test
    public void testStart() {
        ExternalTypeHandler handler = externalTypeHandler.start();
        assertNotSame(externalTypeHandler, handler);
    }
    
    @Test
    public void testHandleTypePropertyValue() throws Exception {
        when(jsonParser.getText()).thenReturn("typeId");
        Object bean = new Object();
        
        externalTypeHandler.handleTypePropertyValue(jsonParser, deserializationContext, "property1", bean);
        
        verify(jsonParser, times(1)).getText();
    }
    
    @Test
    public void testHandlePropertyValue() throws Exception {
        when(jsonParser.getText()).thenReturn("typeId");
        Object bean = new Object();
        
        externalTypeHandler.handlePropertyValue(jsonParser, deserializationContext, "property1", bean);
        
        verify(jsonParser, times(1)).getText();
    }
    
    @Test
    public void testComplete() throws Exception {
        externalTypeHandler.complete(jsonParser, deserializationContext, new Object());
    }
    
    @Test
    public void testCompleteWithBuffer() throws Exception {
        ExternalTypeHandler.PropertyValueBuffer buffer = mock(ExternalTypeHandler.PropertyValueBuffer.class);
        ExternalTypeHandler.PropertyBasedCreator creator = mock(ExternalTypeHandler.PropertyBasedCreator.class);
        
        externalTypeHandler.complete(jsonParser, deserializationContext, buffer, creator);
        
        verify(buffer, times(1)).assignParameter(any(SettableBeanProperty.class), anyObject());
        verify(creator, times(1)).build(any(DeserializationContext.class), any(ExternalTypeHandler.PropertyValueBuffer.class));
    }
    
    @Test
    public void testDeserialize() throws Exception {
        ExternalTypeHandler handler = spy(externalTypeHandler);
        when(handler._tokens[0]).thenReturn(mock(TokenBuffer.class));
        
        handler._deserialize(jsonParser, deserializationContext, 0, "typeId");
        
        verify(handler, times(1))._deserialize(jsonParser, deserializationContext, 0, "typeId");
    }
    
    @Test
    public void testDeserializeAndSet() throws Exception {
        ExternalTypeHandler handler = spy(externalTypeHandler);
        when(handler._tokens[0]).thenReturn(mock(TokenBuffer.class));
        
        Object bean = new Object();
        handler._deserializeAndSet(jsonParser, deserializationContext, bean, 0, "typeId");
        
        verify(handler, times(1))._deserializeAndSet(jsonParser, deserializationContext, bean, 0, "typeId");
    }
}