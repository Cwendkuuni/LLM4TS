import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotations.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class CreatorPropertyTest {

    private CreatorProperty creatorProperty;
    private PropertyName name;
    private JavaType type;
    private PropertyName wrapperName;
    private TypeDeserializer typeDeser;
    private Annotations contextAnnotations;
    private AnnotatedParameter param;
    private int index;
    private Object injectableValueId;
    private PropertyMetadata metadata;

    @Before
    public void setup() {
        name = new PropertyName("dummyName");
        type = Mockito.mock(JavaType.class);
        wrapperName = Mockito.mock(PropertyName.class);
        typeDeser = Mockito.mock(TypeDeserializer.class);
        contextAnnotations = Mockito.mock(Annotations.class);
        param = Mockito.mock(AnnotatedParameter.class);
        index = 0;
        injectableValueId = "dummyValueId";
        metadata = Mockito.mock(PropertyMetadata.class);
        
        creatorProperty = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations,
                param, index, injectableValueId, metadata);
    }
    
    @Test
    public void testGetName() {
        assertEquals("dummyName", creatorProperty.getName().getSimpleName());
    }
    
    @Test
    public void testGetType() {
        assertEquals(type, creatorProperty.getType());
    }
    
    @Test
    public void testGetWrapperName() {
        assertEquals(wrapperName, creatorProperty.getWrapperName());
    }
    
    @Test
    public void testGetTypeDeser() {
        assertEquals(typeDeser, creatorProperty.getTypeDeser());
    }
    
    @Test
    public void testGetContextAnnotations() {
        assertEquals(contextAnnotations, creatorProperty.getContextAnnotations());
    }
    
    @Test
    public void testGetAnnotated() {
        assertEquals(param, creatorProperty.getAnnotated());
    }
    
    @Test
    public void testGetIndex() {
        assertEquals(0, creatorProperty.getIndex());
    }
    
    @Test
    public void testGetInjectableValueId() {
        assertEquals("dummyValueId", creatorProperty.getInjectableValueId());
    }
    
    @Test
    public void testIsIgnorable() {
        assertFalse(creatorProperty.isIgnorable());
        
        creatorProperty.markAsIgnorable();
        
        assertTrue(creatorProperty.isIgnorable());
    }
    
    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        
        creatorProperty.setFallbackSetter(Mockito.mock(SettableBeanProperty.class));
        
        creatorProperty.deserializeAndSet(parser, ctxt, Mockito.mock(Object.class));
        
        // Verify that set method is called on fallbackSetter
        Mockito.verify(creatorProperty._fallbackSetter, Mockito.times(1)).set(Mockito.any(), Mockito.any());
    }
    
    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        
        creatorProperty.setFallbackSetter(Mockito.mock(SettableBeanProperty.class));
        
        creatorProperty.deserializeSetAndReturn(parser, ctxt, Mockito.mock(Object.class));
        
        // Verify that setAndReturn method is called on fallbackSetter
        Mockito.verify(creatorProperty._fallbackSetter, Mockito.times(1)).setAndReturn(Mockito.any(), Mockito.any());
    }
    
    @Test
    public void testSet() throws IOException {
        creatorProperty.setFallbackSetter(Mockito.mock(SettableBeanProperty.class));
        
        creatorProperty.set(Mockito.mock(Object.class), Mockito.mock(Object.class));
        
        // Verify that set method is called on fallbackSetter
        Mockito.verify(creatorProperty._fallbackSetter, Mockito.times(1)).set(Mockito.any(), Mockito.any());
    }
    
    @Test
    public void testSetAndReturn() throws IOException {
        creatorProperty.setFallbackSetter(Mockito.mock(SettableBeanProperty.class));
        
        creatorProperty.setAndReturn(Mockito.mock(Object.class), Mockito.mock(Object.class));
        
        // Verify that setAndReturn method is called on fallbackSetter
        Mockito.verify(creatorProperty._fallbackSetter, Mockito.times(1)).setAndReturn(Mockito.any(), Mockito.any());
    }
    
    @Test(expected = InvalidDefinitionException.class)
    public void testDeserializeAndSetNullFallbackSetter() throws IOException {
        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        
        creatorProperty.deserializeAndSet(parser, ctxt, Mockito.mock(Object.class));
    }
    
    @Test(expected = InvalidDefinitionException.class)
    public void testDeserializeSetAndReturnNullFallbackSetter() throws IOException {
        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        
        creatorProperty.deserializeSetAndReturn(parser, ctxt, Mockito.mock(Object.class));
    }
    
    @Test(expected = InvalidDefinitionException.class)
    public void testSetNullFallbackSetter() throws IOException {
        creatorProperty.set(Mockito.mock(Object.class), Mockito.mock(Object.class));
    }
    
    @Test(expected = InvalidDefinitionException.class)
    public void testSetAndReturnNullFallbackSetter() throws IOException {
        creatorProperty.setAndReturn(Mockito.mock(Object.class), Mockito.mock(Object.class));
    }
    
    @Test
    public void testFindInjectableValue() throws JsonMappingException {
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        
        creatorProperty.findInjectableValue(ctxt, Mockito.mock(Object.class));
        
        Mockito.verify(ctxt, Mockito.times(1)).findInjectableValue(Mockito.any(), Mockito.any(), Mockito.any());
    }
    
    @Test(expected = JsonMappingException.class)
    public void testFindInjectableValueNullInjectableValueId() throws JsonMappingException {
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        creatorProperty = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations,
                param, index, null, metadata);
        
        creatorProperty.findInjectableValue(ctxt, Mockito.mock(Object.class));
    }
    
    @Test
    public void testInject() throws IOException {
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        
        creatorProperty.setFallbackSetter(Mockito.mock(SettableBeanProperty.class));
        
        creatorProperty.inject(ctxt, Mockito.mock(Object.class));
        
        Mockito.verify(creatorProperty._fallbackSetter, Mockito.times(1)).set(Mockito.any(), Mockito.any());
    }
    
    @Test(expected = InvalidDefinitionException.class)
    public void testInjectNullFallbackSetter() throws IOException {
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        
        creatorProperty.inject(ctxt, Mockito.mock(Object.class));
    }
    
    @Test
    public void testGetAnnotation() {
        AnnotatedParameter param = Mockito.mock(AnnotatedParameter.class);
        Mockito.when(param.getAnnotation(Mockito.any(Class.class))).thenReturn(null);
        
        creatorProperty = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations,
                null, index, injectableValueId, metadata);
        creatorProperty._annotated = param;
        
        assertNull(creatorProperty.getAnnotation(JsonDeserialize.class));
    }
    
    @Test
    public void testGetMember() {
        assertEquals(param, creatorProperty.getMember());
    }
}