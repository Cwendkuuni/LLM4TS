import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TypeDeserializerBaseTest {

    @Mock
    private DeserializationContext mockContext;
    
    @Mock
    private JsonParser mockParser;
    
    @Mock
    private TypeIdResolver mockResolver;
    
    private TypeDeserializerBase typeDeserializer;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        typeDeserializer = new TypeDeserializerBase(null, mockResolver, null, false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
            
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
    }
        
    @Test
    public void test_serializeWithNativeTypeId_withNullTypeId_shouldReturnDefaultImplDeserializer() throws IOException {
        assertNull(typeDeserializer._deserializeWithNativeTypeId(mockParser, mockContext, null));
        verify(mockContext, times(1)).isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE);
        verify(mockContext, times(0)).mappingException(anyString());
    }
    
    @Test
    public void test_serializeWithNativeTypeId_withValidTypeId_shouldReturnDeserializer() throws IOException {
        when(mockParser.getTypeId()).thenReturn("type_id");
        JsonDeserializer<Object> mockDeserializer = mock(JsonDeserializer.class);
        when(typeDeserializer._findDeserializer(mockContext, "type_id")).thenReturn(mockDeserializer);
        
        assertEquals(mockDeserializer, typeDeserializer._deserializeWithNativeTypeId(mockParser, mockContext));
        verify(mockContext, times(0)).isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE);
        verify(mockContext, times(0)).mappingException(anyString());
    }
    
    @Test(expected = IOException.class)
    public void test_serializeWithNativeTypeId_withInvalidTypeId_shouldThrowIOException() throws IOException {
        when(mockParser.getTypeId()).thenReturn("type_id");
        when(typeDeserializer._findDeserializer(mockContext, "type_id")).thenReturn(null);
        
        typeDeserializer._deserializeWithNativeTypeId(mockParser, mockContext);
        verify(mockContext, times(0)).isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE);
        verify(mockContext, times(1)).mappingException(anyString());
    }
    
    @Test
    public void test_findDefaultImplDeserializer_withNullDefaultImpl_shouldReturnNullifyingDeserializer() throws IOException {
        when(mockContext.isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)).thenReturn(false);
        assertNull(typeDeserializer._findDefaultImplDeserializer(mockContext));
        verify(mockContext, times(1)).isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE);
    }
    
    @Test
    public void test_findDefaultImplDeserializer_withVoidDefaultImpl_shouldReturnNullifyingDeserializer() throws IOException {
        typeDeserializer = new TypeDeserializerBase(null, mockResolver, null, false, Void.class) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
            
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
        
        when(mockContext.isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)).thenReturn(true);
        assertNull(typeDeserializer._findDefaultImplDeserializer(mockContext));
        verify(mockContext, times(1)).isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE);
    }
    
    @Test
    public void test_findDefaultImplDeserializer_withValidDefaultImpl_shouldReturnContextualValueDeserializer() throws IOException {
        typeDeserializer = new TypeDeserializerBase(null, mockResolver, null, false, String.class) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
            
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        JsonDeserializer<Object> mockDeserializer = mock(JsonDeserializer.class);
        when(mockContext.findContextualValueDeserializer(eq(String.class), any())).thenReturn(mockDeserializer);
        
        assertEquals(mockDeserializer, typeDeserializer._findDefaultImplDeserializer(mockContext));
        verify(mockContext, times(1)).isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE);
    }
    
    @Test
    public void test_findDefaultImplDeserializer_withBogusClassDefaultImpl_shouldReturnNullifyingDeserializer() throws IOException {
        typeDeserializer = new TypeDeserializerBase(null, mockResolver, null, false, BogusClass.class) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
            
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
        
        when(mockContext.isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)).thenReturn(true);
        assertNull(typeDeserializer._findDefaultImplDeserializer(mockContext));
        verify(mockContext, times(1)).isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE);
    }
    
    @Test
    public void test_baseTypeName_withBaseType_shouldReturnTypeName() {
        typeDeserializer = new TypeDeserializerBase(JavaType.construct(Void.class), mockResolver, null, false, String.class) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
            
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
        
        assertEquals("java.lang.Void", typeDeserializer.baseTypeName());
    }
    
    @Test
    public void test_getPropertyName_shouldReturnTypePropertyName() {
        typeDeserializer = new TypeDeserializerBase(null, mockResolver, "type_property", false, String.class) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
            
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
        
        assertEquals("type_property", typeDeserializer.getPropertyName());
    }
    
    @Test
    public void test_getTypeIdResolver_shouldReturnTypeIdResolver() {
        assertEquals(mockResolver, typeDeserializer.getTypeIdResolver());
    }
    
    @Test
    public void test_getDefaultImpl_shouldReturnDefaultImplRawClass() {
        typeDeserializer = new TypeDeserializerBase(null, mockResolver, null, false, String.class) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
            
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
        
        assertEquals(String.class, typeDeserializer.getDefaultImpl());
    }

    final static class BogusClass {}
    
}