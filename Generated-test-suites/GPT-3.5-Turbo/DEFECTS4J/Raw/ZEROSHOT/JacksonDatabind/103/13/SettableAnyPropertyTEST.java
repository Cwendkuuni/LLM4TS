import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SettableAnyPropertyTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private BeanProperty mockProperty;

    @Mock
    private AnnotatedMember mockSetter;

    @Mock
    private JavaType mockType;

    @Mock
    private KeyDeserializer mockKeyDeserializer;

    @Mock
    private JsonDeserializer<Object> mockValueDeserializer;

    @Mock
    private TypeDeserializer mockTypeDeserializer;

    private SettableAnyProperty settableAnyProperty;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        settableAnyProperty = new SettableAnyProperty(mockProperty, mockSetter, mockType, mockKeyDeserializer, mockValueDeserializer, mockTypeDeserializer);
    }

    @Test
    public void testGetProperty() {
        BeanProperty result = settableAnyProperty.getProperty();
        assertEquals(mockProperty, result);
    }

    @Test
    public void testHasValueDeserializer() {
        when(mockValueDeserializer != null).thenReturn(true);
        boolean result = settableAnyProperty.hasValueDeserializer();
        assertTrue(result);
    }

    @Test
    public void testGetType() {
        JavaType result = settableAnyProperty.getType();
        assertEquals(mockType, result);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser mockParser = mock(JsonParser.class);
        DeserializationContext mockContext = mock(DeserializationContext.class);
        Object mockInstance = mock(Object.class);
        String propName = "testProp";
        
        JsonToken mockToken = mock(JsonToken.class);
        when(mockParser.getCurrentToken()).thenReturn(mockToken);

        UnresolvedForwardReference mockReference = mock(UnresolvedForwardReference.class);
        when(mockValueDeserializer.getObjectIdReader() != null).thenReturn(false);
        AnySetterReferring mockReferring = mock(AnySetterReferring.class);
        ReadableObjectId.Referring mockRoid = mock(ReadableObjectId.Referring.class);
        when(mockReference.getRoid()).thenReturn(mockRoid);
        
        settableAnyProperty.deserializeAndSet(mockParser, mockContext, mockInstance, propName);
        
        verify(mockParser).getCurrentToken();
        verify(mockValueDeserializer).getNullValue(mockContext);
        verify(mockValueDeserializer).deserialize(mockParser, mockContext);
    }

    @Test
    public void testDeserialize() throws IOException {
        JsonParser mockParser = mock(JsonParser.class);
        DeserializationContext mockContext = mock(DeserializationContext.class);
        
        JsonToken mockToken = mock(JsonToken.class);
        when(mockParser.getCurrentToken()).thenReturn(mockToken);

        Object result = settableAnyProperty.deserialize(mockParser, mockContext);
        
        verify(mockParser).getCurrentToken();
        verify(mockValueDeserializer).deserialize(mockParser, mockContext);
        
        assertEquals(result, mockValueDeserializer);
    }

    @Test
    public void testSet_withFieldsetter() throws IOException {
        AnnotatedField mockField = mock(AnnotatedField.class);
        when(mockSetter instanceof AnnotatedField).thenReturn(true);
        when(mockSetter).thenReturn(mockField);

        Object mockInstance = mock(Object.class);
        Map<Object,Object> mockValue = mock(Map.class);
        Object propName = "testProp";
        Object value = "testValue";

        when(mockField.getValue(mockInstance)).thenReturn(mockValue);

        settableAnyProperty.set(mockInstance, propName, value);

        verify(mockSetter, never()).callOnWith(any(), any(), any());
        verify(mockField).getValue(mockInstance);
        verify(mockValue).put(propName, value);
    }

    @Test
    public void testSet_withMethodSetter() throws IOException {
        AnnotatedMethod mockMethod = mock(AnnotatedMethod.class);
        when(mockSetter instanceof AnnotatedField).thenReturn(false);
        when(mockSetter).thenReturn(mockMethod);

        Object mockInstance = mock(Object.class);
        Object propName = "testProp";
        Object value = "testValue";

        settableAnyProperty.set(mockInstance, propName, value);

        verify(mockSetter).callOnWith(mockInstance, propName, value);
        verify(mockMethod, never()).invoke(any(), any(), any());
    }

    @Test(expected = IOException.class)
    public void test_set_throwException() throws Exception {
        AnnotatedMethod mockMethod = mock(AnnotatedMethod.class);
        when(mockSetter instanceof AnnotatedField).thenReturn(false);
        when(mockSetter).thenReturn(mockMethod);

        Object mockInstance = mock(Object.class);
        Object propName = "testProp";
        Object value = "testValue";

        doThrow(Exception.class).when(mockMethod).callOnWith(any(), any(), any());

        settableAnyProperty.set(mockInstance, propName, value);
    }

    private SettableAnyProperty.AnySetterReferring createMockReferring() {
        UnresolvedForwardReference mockReference = mock(UnresolvedForwardReference.class);
        Class<?> type = Object.class;
        Object instance = mock(Object.class);
        String propName = "testProp";
        return new SettableAnyProperty.AnySetterReferring(settableAnyProperty, mockReference, type, instance, propName);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAnySetterReferring_handleResolvedForwardReference_throwException() throws IOException{
        SettableAnyProperty.AnySetterReferring referring = createMockReferring();
        Object id = new Object();
        Object value = new Object();
        referring.handleResolvedForwardReference(id, value);
    }

    @Test
    public void testAnySetterReferring_handleResolvedForwardReference() throws IOException{
        SettableAnyProperty.AnySetterReferring referring = createMockReferring();
        Object id = new Object();
        Object value = new Object();

        referring.addId(id);
        referring.handleResolvedForwardReference(id, value);

        verify(settableAnyProperty).set(any(), any(), any());
    }

    @Test
    public void testThrowAsIOE(){
        Exception mockException = mock(Exception.class);
        Object propName = "testProp";
        Object value = "testValue";

        settableAnyProperty._throwAsIOE(mockException, propName, value);

        verify(mockException).getMessage();
        verify(mockValueDeserializer, never()).getNullValue(any());
        verify(mockException, never()).getCause();
    }

    @Test(expected = IOException.class)
    public void testThrowAsIOE_throwIOException() throws IOException {
        IOException mockException = mock(IOException.class);
        Object propName = "testProp";
        Object value = "testValue";

        settableAnyProperty._throwAsIOE(mockException, propName, value);
    }
  
    @Test(expected = RuntimeException.class)
    public void testThrowAsIOE_throwRuntimeException() throws IOException {
        RuntimeException mockException = mock(RuntimeException.class);
        Object propName = "testProp";
        Object value = "testValue";

        settableAnyProperty._throwAsIOE(mockException, propName, value);
    }

    @Test
    public void testToString() {
        String result = settableAnyProperty.toString();
        assertEquals("[any property on class " + settableAnyProperty.getClassName() + "]", result);
    }
  
}