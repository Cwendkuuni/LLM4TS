import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class AsWrapperTypeDeserializerTest {

    @Mock
    private JavaType bt;

    @Mock
    private TypeIdResolver idRes;

    @Mock
    private BeanProperty prop;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testConstructor() {
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(bt, idRes, "typeId", true, Object.class);
        assertNotNull(deserializer);
    }

    @Test
    public void testForProperty() {
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(bt, idRes, "typeId", true, Object.class);
        TypeDeserializer typeDeserializer = deserializer.forProperty(prop);
        assertNotNull(typeDeserializer);
        assertNotSame(deserializer, typeDeserializer);
    }

    @Test
    public void testGetTypeInclusion() {
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(bt, idRes, "typeId", true, Object.class);
        assertEquals(JsonTypeInfo.As.WRAPPER_OBJECT, deserializer.getTypeInclusion());
    }

    @Test
    public void testDeserializeTypedFromObject() throws IOException {
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(bt, idRes, "typeId", true, Object.class);
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object result = deserializer.deserializeTypedFromObject(jp, ctxt);
        assertNotNull(result);
        verify(jp, times(1)).canReadTypeId();
        verify(jp, times(1)).getTypeId();
        verify(deserializer, times(1))._deserialize(jp, ctxt);
    }

    @Test
    public void testDeserializeTypedFromArray() throws IOException {
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(bt, idRes, "typeId", true, Object.class);
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object result = deserializer.deserializeTypedFromArray(jp, ctxt);
        assertNotNull(result);
        verify(jp, times(1)).canReadTypeId();
        verify(jp, times(1)).getTypeId();
        verify(deserializer, times(1))._deserialize(jp, ctxt);
    }

    @Test
    public void testDeserializeTypedFromScalar() throws IOException {
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(bt, idRes, "typeId", true, Object.class);
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object result = deserializer.deserializeTypedFromScalar(jp, ctxt);
        assertNotNull(result);
        verify(jp, times(1)).canReadTypeId();
        verify(jp, times(1)).getTypeId();
        verify(deserializer, times(1))._deserialize(jp, ctxt);
    }

    @Test
    public void testDeserializeTypedFromAny() throws IOException {
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(bt, idRes, "typeId", true, Object.class);
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object result = deserializer.deserializeTypedFromAny(jp, ctxt);
        assertNotNull(result);
        verify(jp, times(1)).canReadTypeId();
        verify(jp, times(1)).getTypeId();
        verify(deserializer, times(1))._deserialize(jp, ctxt);
    }

    @Test
    public void test_deserialize() throws IOException {
        AsWrapperTypeDeserializer deserializer = spy(new AsWrapperTypeDeserializer(bt, idRes, "typeId", true, Object.class));
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JsonDeserializer<Object> deser = mock(JsonDeserializer.class);
        when(p.canReadTypeId()).thenReturn(true);
        when(p.getTypeId()).thenReturn("typeId");
        when(deserializer._findDeserializer(ctxt, "typeId")).thenReturn(deser);
        when(p.getCurrentToken()).thenReturn(JsonToken.START_OBJECT).thenReturn(JsonToken.FIELD_NAME).thenReturn(JsonToken.VALUE_STRING).thenReturn(JsonToken.END_OBJECT);
        when(p.getText()).thenReturn("typeId");
        when(deser.deserialize(p, ctxt)).thenReturn("value");
        Object result = deserializer._deserialize(p, ctxt);
        assertNotNull(result);
        verify(p, times(2)).canReadTypeId();
        verify(p, times(1)).getTypeId();
        verify(p, times(3)).getCurrentToken();
        verify(p, times(1)).nextToken();
        verify(p, times(1)).getText();
        verify(deserializer, times(1))._findDeserializer(ctxt, "typeId");
        verify(deser, times(1)).deserialize(p, ctxt);
        verify(p, times(1)).nextToken();
        verify(p, times(1)).nextToken();
        verify(p, times(1)).nextToken();
    }

    @Test(expected = JsonMappingException.class)
    public void test_deserialize_wrongTokenException1() throws IOException {
        AsWrapperTypeDeserializer deserializer = spy(new AsWrapperTypeDeserializer(bt, idRes, "typeId", true, Object.class));
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        when(p.getCurrentToken()).thenReturn(JsonToken.END_OBJECT);
        deserializer._deserialize(p, ctxt);
    }

    @Test(expected = JsonMappingException.class)
    public void test_deserialize_wrongTokenException2() throws IOException {
        AsWrapperTypeDeserializer deserializer = spy(new AsWrapperTypeDeserializer(bt, idRes, "typeId", true, Object.class));
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        when(p.getCurrentToken()).thenReturn(JsonToken.START_OBJECT).thenReturn(JsonToken.VALUE_STRING);
        deserializer._deserialize(p, ctxt);
    }

    @Test(expected = JsonMappingException.class)
    public void test_deserialize_wrongTokenException3() throws IOException {
        AsWrapperTypeDeserializer deserializer = spy(new AsWrapperTypeDeserializer(bt, idRes, "typeId", true, Object.class));
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        when(p.getCurrentToken()).thenReturn(JsonToken.START_OBJECT).thenReturn(JsonToken.FIELD_NAME);
        deserializer._deserialize(p, ctxt);
    }

    @Test
    public void test_deserialize_typeIdVisible() throws IOException {
        AsWrapperTypeDeserializer deserializer = spy(new AsWrapperTypeDeserializer(bt, idRes, "typeId", true, Object.class));
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JsonParser mockParser = mock(JsonParser.class);
        TokenBuffer tb = mock(TokenBuffer.class);
        when(p.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        when(p.nextToken()).thenReturn(JsonToken.START_OBJECT);
        when(tb.asParser(p)).thenReturn(mockParser);
        when(mockParser.nextToken()).thenReturn(JsonToken.FIELD_NAME).thenReturn(JsonToken.END_OBJECT);
        when(p.readValueAsTree()).thenReturn(tb);
        Object result = deserializer._deserialize(p, ctxt);
        assertNotNull(result);
        verify(p, times(1)).getCurrentToken();
        verify(p, times(1)).nextToken();
        verify(tb, times(1)).asParser(p);
        verify(mockParser, times(2)).nextToken();
        verify(p, times(1)).readValueAsTree();
        verify(p, times(1)).nextToken();
    }

}