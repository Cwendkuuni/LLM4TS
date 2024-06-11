import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class InnerClassPropertyTest {
    
    private InnerClassProperty innerClassProperty;
    
    @Mock
    private SettableBeanProperty delegate;
    
    @Mock
    private Constructor<?> constructor;
    
    @Mock
    private AnnotatedConstructor annotated;
    
    @Mock
    private JsonDeserializer<?> deserializer;
    
    @Mock
    private PropertyName newName;
    
    @Mock
    private JsonParser jsonParser;
    
    @Mock
    private DeserializationContext context;
    
    @Mock
    private Object bean;
    
    @Mock
    private JsonDeserializer<?> valueDeserializer;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        innerClassProperty = new InnerClassProperty(delegate, constructor);
    }
    
    @Test
    public void testConstructor() {
        assertNotNull(innerClassProperty);
        assertEquals(delegate, innerClassProperty._delegate);
        assertEquals(constructor, innerClassProperty._creator);
    }
    
    @Test
    public void testDeserializingAndSetting() throws IOException {
        JsonToken token = mock(JsonToken.class);
        when(jsonParser.getCurrentToken()).thenReturn(token);
        when(token == JsonToken.VALUE_NULL).thenReturn(true);
        when(valueDeserializer.getNullValue(context)).thenReturn(null);
        
        innerClassProperty.deserializeAndSet(jsonParser, context, bean);
        
        verify(delegate).set(bean, null);
    }
    
    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        
        when(delegate.setAndReturn(instance, value)).thenReturn(value);
        
        Object result = innerClassProperty.deserializeSetAndReturn(jsonParser, context, instance);
        
        assertEquals(value, result);
        verify(delegate).setAndReturn(instance, value);
    }
    
    @Test
    public void testDeserializationAndSetting() throws IOException, Exception {
        JsonToken token = mock(JsonToken.class);
        Object value = new Object();
        
        when(jsonParser.getCurrentToken()).thenReturn(token);
        when(token == JsonToken.VALUE_NULL).thenReturn(false);
        when(innerClassProperty._creator.newInstance(bean)).thenReturn(value);
        
        innerClassProperty.deserializeAndSet(jsonParser, context, bean);
        
        verify(delegate).deserialize(jsonParser, context, value);
        verify(delegate).set(bean, value);
    }
    
    
}