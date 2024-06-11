import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty.Std;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.PropertyBasedCreator;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler.ExtTypedProperty;
import com.fasterxml.jackson.databind.deser.impl.TokenBuffer;

@RunWith(MockitoJUnitRunner.class)
public class ExternalTypeHandlerTest {

    @Mock
    private ExternalTypeHandler.ExtTypedProperty extTypedProperty;

    @Mock
    private SettableBeanProperty settableBeanProperty;

    @Mock
    private DeserializationContext deserializationContext;

    @InjectMocks
    private ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler.Builder()
            .build();

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        when(extTypedProperty.hasTypePropertyName(any())).thenReturn(true);
        when(extTypedProperty.getProperty()).thenReturn(settableBeanProperty);
        when(settableBeanProperty.getName()).thenReturn("type");
        when(jp.getText()).thenReturn("type1");
        assertTrue(externalTypeHandler.handleTypePropertyValue(jp, deserializationContext, "type", new Object()));
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        when(extTypedProperty.hasTypePropertyName(any())).thenReturn(false);
        when(extTypedProperty.getProperty()).thenReturn(settableBeanProperty);
        when(settableBeanProperty.getName()).thenReturn("property");
        when(jp.getText()).thenReturn("property1");
        when(jp.nextToken()).thenReturn(null);
        assertTrue(externalTypeHandler.handlePropertyValue(jp, deserializationContext, "property", new Object()));
    }

    @Test
    public void testComplete() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        PropertyValueBuffer buffer = mock(PropertyValueBuffer.class);
        PropertyBasedCreator creator = mock(PropertyBasedCreator.class);
        when(creator.findCreatorProperty(any())).thenReturn(settableBeanProperty);
        when(extTypedProperty.getProperty()).thenReturn(settableBeanProperty);
        when(settableBeanProperty.getName()).thenReturn("property");
        when(jp.nextToken()).thenReturn(null);
        Object bean = new Object();
        assertEquals(bean, externalTypeHandler.complete(jp, deserializationContext, bean));
    }

    @Test
    public void testCompleteWithBuffer() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        PropertyValueBuffer buffer = mock(PropertyValueBuffer.class);
        PropertyBasedCreator creator = mock(PropertyBasedCreator.class);
        when(creator.findCreatorProperty(any())).thenReturn(settableBeanProperty);
        when(extTypedProperty.getProperty()).thenReturn(settableBeanProperty);
        when(settableBeanProperty.getName()).thenReturn("property");
        when(jp.nextToken()).thenReturn(null);
        Object bean = new Object();
        assertEquals(bean, externalTypeHandler.complete(jp, deserializationContext, buffer, creator));
    }
}