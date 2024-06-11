import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class InnerClassPropertyTest {
    private InnerClassProperty property;
    private SettableBeanProperty delegateMock;
    private Constructor<?> constructorMock;
    private Object beanMock;
    private JsonParser jsonParserMock;
    private DeserializationContext ctxtMock;
    private Object valueMock;
    
    @Before
    public void setUp() {
        delegateMock = mock(SettableBeanProperty.class);
        constructorMock = mock(Constructor.class);
        beanMock = mock(Object.class);
        jsonParserMock = mock(JsonParser.class);
        ctxtMock = mock(DeserializationContext.class);
        valueMock = mock(Object.class);
        
        property = new InnerClassProperty(delegateMock, constructorMock);
    }
    
    @Test
    public void testConstructor() {
        assertEquals(delegateMock, property._delegate);
        assertEquals(constructorMock, property._creator);
    }
    
    @Test
    public void testConstructorWithAnnotatedConstructor() {
        AnnotatedConstructor constructorMock = mock(AnnotatedConstructor.class);
        InnerClassProperty newProperty = new InnerClassProperty(property, constructorMock);
        
        assertEquals(property._delegate, newProperty._delegate);
        assertEquals(property._creator, newProperty._creator);
        assertEquals(constructorMock, newProperty._annotated);
    }
    
    @Test
    public void testConstructorWithJsonDeserializer() {
        JsonDeserializer<?> deserializerMock = mock(JsonDeserializer.class);
        InnerClassProperty newProperty = new InnerClassProperty(property, deserializerMock);
        
        assertEquals(property._delegate.withValueDeserializer(deserializerMock), newProperty._delegate);
        assertEquals(property._creator, newProperty._creator);
    }
    
    @Test
    public void testConstructorWithPropertyName() {
        PropertyName newName = mock(PropertyName.class);
        InnerClassProperty newProperty = new InnerClassProperty(property, newName);
        
        assertEquals(property._delegate.withName(newName), newProperty._delegate);
        assertEquals(property._creator, newProperty._creator);
    }
    
    @Test
    public void testAssignIndex() {
        int index = 0;
        property.assignIndex(index);
        verify(delegateMock, times(1)).assignIndex(index);
    }
    
    @Test
    public void testGetPropertyIndex() {
        int expectedIndex = 0;
        when(delegateMock.getPropertyIndex()).thenReturn(expectedIndex);
        
        int actualIndex = property.getPropertyIndex();
        
        assertEquals(expectedIndex, actualIndex);
    }
    
    @Test
    public void testGetAnnotation() {
        Class<Annotation> annotationClass = Annotation.class;
        Annotation expectedAnnotation = mock(Annotation.class);
        when(delegateMock.getAnnotation(annotationClass)).thenReturn(expectedAnnotation);
        
        Annotation actualAnnotation = property.getAnnotation(annotationClass);
        
        assertEquals(expectedAnnotation, actualAnnotation);
    }
    
    @Test
    public void testGetMember() {
        AnnotatedMember expectedMember = mock(AnnotatedMember.class);
        when(delegateMock.getMember()).thenReturn(expectedMember);
        
        AnnotatedMember actualMember = property.getMember();
        
        assertEquals(expectedMember, actualMember);
    }
    
    @Test
    public void testDeserializeAndSetWithNullValue() throws IOException {
        JsonToken token = JsonToken.VALUE_NULL;
        when(jsonParserMock.getCurrentToken()).thenReturn(token);
        JsonDeserializer<?> valueDeserializerMock = mock(JsonDeserializer.class);
        when(property._valueDeserializer.getNullValue(ctxtMock)).thenReturn(valueMock);
        
        property.deserializeAndSet(jsonParserMock, ctxtMock, beanMock);
        
        verify(property._valueDeserializer, times(1)).getNullValue(ctxtMock);
        verify(property._delegate, times(1)).set(beanMock, valueMock);
    }
    
    @Test
    public void testDeserializeAndSetWithTypeDeserializer() throws IOException {
        JsonToken token = JsonToken.VALUE_STRING;
        when(jsonParserMock.getCurrentToken()).thenReturn(token);
        JsonDeserializer<?> valueDeserializerMock = mock(JsonDeserializer.class);
        when(property._valueDeserializer.deserializeWithType(jsonParserMock, ctxtMock, property._valueTypeDeserializer)).thenReturn(valueMock);
        
        property.deserializeAndSet(jsonParserMock, ctxtMock, beanMock);
        
        verify(property._delegate, times(1)).set(beanMock, valueMock);
    }
    
    @Test
    public void testDeserializeAndSetWithCreatorException() throws Exception {
        JsonToken token = JsonToken.VALUE_STRING;
        when(jsonParserMock.getCurrentToken()).thenReturn(token);
        when(constructorMock.newInstance(beanMock)).thenThrow(new Exception());
        
        property.deserializeAndSet(jsonParserMock, ctxtMock, beanMock);
        
        verify(property._creator, times(1)).newInstance(beanMock);
        verify(property._delegate, times(0)).set(any(), any());
    }
    
    @Test
    public void testDeserializeAndSet() throws Exception {
        JsonToken token = JsonToken.VALUE_STRING;
        when(jsonParserMock.getCurrentToken()).thenReturn(token);
        when(constructorMock.newInstance(beanMock)).thenReturn(valueMock);
        
        property.deserializeAndSet(jsonParserMock, ctxtMock, beanMock);
        
        verify(property._creator, times(1)).newInstance(beanMock);
        verify(property._delegate, times(1)).set(beanMock, valueMock);
    }
    
    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        Object expectedReturn = mock(Object.class);
        when(property.deserialize(jsonParserMock, ctxtMock)).thenReturn(valueMock);
        
        Object actualReturn = property.deserializeSetAndReturn(jsonParserMock, ctxtMock, beanMock);
        
        assertEquals(expectedReturn, actualReturn);
        verify(property._delegate, times(1)).setAndReturn(beanMock, valueMock);
    }
    
    @Test
    public void testSet() throws IOException {
        property.set(beanMock, valueMock);
        
        verify(property._delegate, times(1)).set(beanMock, valueMock);
    }
    
    @Test
    public void testSetAndReturn() throws IOException {
        Object expectedReturn = mock(Object.class);
        when(property._delegate.setAndReturn(beanMock, valueMock)).thenReturn(expectedReturn);
        
        Object actualReturn = property.setAndReturn(beanMock, valueMock);
        
        assertEquals(expectedReturn, actualReturn);
    }
    
    @Test
    public void testReadResolve() {
        InnerClassProperty newProperty = (InnerClassProperty) property.readResolve();
        
        assertEquals(property._delegate, newProperty._delegate);
        assertEquals(property._creator, newProperty._creator);
        assertEquals(property._annotated, newProperty._annotated);
    }
    
    @Test
    public void testWriteReplace() {
        InnerClassProperty newProperty = (InnerClassProperty) property.writeReplace();
        
        assertEquals(property._delegate, newProperty._delegate);
        assertEquals(property._creator, newProperty._creator);
        assertNull(newProperty._annotated);
    }
}