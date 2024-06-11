import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class TypeDeserializerBaseTest {

    @Mock
    private DeserializationContext mockDeserializationContext;

    @Test
    public void testForProperty() {
        // Test the forProperty() method
        BeanProperty mockBeanProperty = mock(BeanProperty.class);
        TypeDeserializerBase typeDeserializerBase = mock(TypeDeserializerBase.class);
        typeDeserializerBase.forProperty(mockBeanProperty);

        verify(mockBeanProperty, times(1)).getType();
        verify(typeDeserializerBase, times(1)).forProperty(mockBeanProperty);
    }

    @Test
    public void testGetTypeInclusion() {
        // Test the getTypeInclusion() method
        TypeDeserializerBase typeDeserializerBase = mock(TypeDeserializerBase.class);
        typeDeserializerBase.getTypeInclusion();

        verify(typeDeserializerBase, times(1)).getTypeInclusion();
    }

    @Test
    public void testBaseTypeName() {
        // Test the baseTypeName() method
        TypeDeserializerBase typeDeserializerBase = mock(TypeDeserializerBase.class);
        typeDeserializerBase.baseTypeName();

        verify(typeDeserializerBase, times(1)).baseTypeName();
    }

    @Test
    public void testGetPropertyName() {
        // Test the getPropertyName() method
        TypeDeserializerBase typeDeserializerBase = mock(TypeDeserializerBase.class);
        typeDeserializerBase.getPropertyName();

        verify(typeDeserializerBase, times(1)).getPropertyName();
    }

    @Test
    public void testGetTypeIdResolver() {
        // Test the getTypeIdResolver() method
        TypeDeserializerBase typeDeserializerBase = mock(TypeDeserializerBase.class);
        typeDeserializerBase.getTypeIdResolver();

        verify(typeDeserializerBase, times(1)).getTypeIdResolver();
    }

    @Test
    public void testGetDefaultImpl() {
        // Test the getDefaultImpl() method
        TypeDeserializerBase typeDeserializerBase = mock(TypeDeserializerBase.class);
        typeDeserializerBase.getDefaultImpl();

        verify(typeDeserializerBase, times(1)).getDefaultImpl();
    }

    @Test
    public void testToString() {
        // Test the toString() method
        TypeDeserializerBase typeDeserializerBase = mock(TypeDeserializerBase.class);
        typeDeserializerBase.toString();

        verify(typeDeserializerBase, times(1)).toString();
    }

    @Test
    public void testFindDeserializer() throws IOException {
        // Test the _findDeserializer() method
        DeserializationContext mockDeserializationContext = mock(DeserializationContext.class);
        JsonParser mockJsonParser = mock(JsonParser.class);
        TypeDeserializerBase typeDeserializerBase = mock(TypeDeserializerBase.class);
        typeDeserializerBase._findDeserializer(mockDeserializationContext, "test");

        verify(mockDeserializationContext, times(1)).getTypeFactory();
        verify(mockDeserializationContext, times(1)).findContextualValueDeserializer(any(), any());
        verify(typeDeserializerBase, times(1))._findDeserializer(mockDeserializationContext, "test");
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        // Test the _findDefaultImplDeserializer() method
        DeserializationContext mockDeserializationContext = mock(DeserializationContext.class);
        TypeDeserializerBase typeDeserializerBase = mock(TypeDeserializerBase.class);

        when(mockDeserializationContext.isEnabled(any())).thenReturn(true);
        typeDeserializerBase._findDefaultImplDeserializer(mockDeserializationContext);

        verify(mockDeserializationContext, times(1)).findContextualValueDeserializer(any(), any());
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        // Test the _deserializeWithNativeTypeId() method
        JsonParser mockJsonParser = mock(JsonParser.class);
        DeserializationContext mockDeserializationContext = mock(DeserializationContext.class);
        TypeDeserializerBase typeDeserializerBase = mock(TypeDeserializerBase.class);

        when(mockJsonParser.getTypeId()).thenReturn("testTypeId");
        when(typeDeserializerBase._deserializeWithNativeTypeId(mockJsonParser, mockDeserializationContext, "testTypeId")).thenCallRealMethod();

        typeDeserializerBase._deserializeWithNativeTypeId(mockJsonParser, mockDeserializationContext);

        verify(mockDeserializationContext, times(1)).reportMappingException(any());
        verify(typeDeserializerBase, times(1))._deserializeWithNativeTypeId(mockJsonParser, mockDeserializationContext, "testTypeId");
    }

    @Test
    public void testDeserializeWithNativeTypeIdNullTypeId() throws IOException {
        // Test the _deserializeWithNativeTypeId() method when typeId is null
        JsonParser mockJsonParser = mock(JsonParser.class);
        DeserializationContext mockDeserializationContext = mock(DeserializationContext.class);
        TypeDeserializerBase typeDeserializerBase = mock(TypeDeserializerBase.class);

        when(mockJsonParser.getTypeId()).thenReturn(null);
        when(mockDeserializationContext.isEnabled(any())).thenReturn(true);
        when(typeDeserializerBase._deserializeWithNativeTypeId(mockJsonParser, mockDeserializationContext, null)).thenCallRealMethod();

        typeDeserializerBase._deserializeWithNativeTypeId(mockJsonParser, mockDeserializationContext);

        verify(mockDeserializationContext, times(1)).reportMappingException(any());
        verify(typeDeserializerBase, times(1))._deserializeWithNativeTypeId(mockJsonParser, mockDeserializationContext, null);
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        // Test the _handleUnknownTypeId() method
        DeserializationContext mockDeserializationContext = mock(DeserializationContext.class);
        TypeDeserializerBase typeDeserializerBase = mock(TypeDeserializerBase.class);
        TypeDeserializerBase.SerializerTypeResolverBinder.IdResolverWrapper mockIdResolver = mock(TypeDeserializerBase.SerializerTypeResolverBinder.IdResolverWrapper.class);

        when(mockIdResolver.getDescForKnownTypeIds()).thenReturn("testDescription");
        when(mockDeserializationContext.handleUnknownTypeId(any(), any(), any(), any())).thenReturn(null);
        when(typeDeserializerBase._handleUnknownTypeId(eq(mockDeserializationContext), anyString(), any(), any())).thenCallRealMethod();

        typeDeserializerBase._handleUnknownTypeId(mockDeserializationContext, "testTypeId", mockIdResolver, null);

        verify(mockIdResolver, times(1)).getDescForKnownTypeIds();
        verify(mockDeserializationContext, times(1)).handleUnknownTypeId(any(), any(), any(), any());
        verify(typeDeserializerBase, times(1))._handleUnknownTypeId(eq(mockDeserializationContext), anyString(), any(), any());
    }
}