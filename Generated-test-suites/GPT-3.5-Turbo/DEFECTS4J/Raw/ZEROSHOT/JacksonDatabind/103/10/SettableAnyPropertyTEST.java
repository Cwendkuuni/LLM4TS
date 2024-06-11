import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Map;

public class SettableAnyPropertyTest {
    private SettableAnyProperty settableAnyProperty;
    private BeanProperty mockBeanProperty;
    private AnnotatedMember mockAnnotatedMember;
    private JavaType mockJavaType;
    private KeyDeserializer mockKeyDeserializer;
    private JsonDeserializer<Object> mockJsonDeserializer;
    private TypeDeserializer mockTypeDeserializer;
    private JsonParser mockJsonParser;
    private DeserializationContext mockDeserializationContext;
    private UnresolvedForwardReference mockUnresolvedForwardReference;
    private Object mockInstance;
    private String propName = "property";
    private Object value = "value";

    @Before
    public void setUp() {
        mockBeanProperty = Mockito.mock(BeanProperty.class);
        mockAnnotatedMember = Mockito.mock(AnnotatedMember.class);
        mockJavaType = Mockito.mock(JavaType.class);
        mockKeyDeserializer = Mockito.mock(KeyDeserializer.class);
        mockJsonDeserializer = Mockito.mock(JsonDeserializer.class);
        mockTypeDeserializer = Mockito.mock(TypeDeserializer.class);
        mockJsonParser = Mockito.mock(JsonParser.class);
        mockDeserializationContext = Mockito.mock(DeserializationContext.class);
        mockUnresolvedForwardReference = Mockito.mock(UnresolvedForwardReference.class);
        mockInstance = Mockito.mock(Object.class);

        settableAnyProperty = new SettableAnyProperty(mockBeanProperty, mockAnnotatedMember, mockJavaType,
                mockKeyDeserializer, mockJsonDeserializer, mockTypeDeserializer);
    }

    @After
    public void tearDown() {
        settableAnyProperty = null;
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Mockito.when(mockKeyDeserializer.deserializeKey(propName, mockDeserializationContext)).thenReturn("key");
        Mockito.when(mockJsonDeserializer.getNullValue(mockDeserializationContext)).thenReturn(null);
        Mockito.when(mockJsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);

        settableAnyProperty.deserializeAndSet(mockJsonParser, mockDeserializationContext, mockInstance, propName);

        Mockito.verify(mockKeyDeserializer).deserializeKey(propName, mockDeserializationContext);
        Mockito.verify(mockJsonDeserializer).getNullValue(mockDeserializationContext);
        Mockito.verify(mockJsonParser).getCurrentToken();
    }

    @Test
    public void testDeserializeAndSetWithUnresolvedForwardReference() throws IOException {
        Mockito.when(mockJsonDeserializer.getObjectIdReader()).thenReturn(Mockito.mock(ReadableObjectId.class));
        Mockito.when(mockUnresolvedForwardReference.getRoid()).thenReturn(Mockito.mock(ReadableObjectId.class));
        Mockito.when(mockUnresolvedForwardReference.getRoid().appendReferring(Mockito.any())).thenReturn(null);
        Mockito.when(mockKeyDeserializer.deserializeKey(propName, mockDeserializationContext)).thenReturn("key");
        Mockito.when(mockJsonDeserializer.getNullValue(mockDeserializationContext)).thenReturn(null);
        Mockito.when(mockJsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);

        settableAnyProperty.deserializeAndSet(mockJsonParser, mockDeserializationContext, mockInstance, propName);

        Mockito.verify(mockJsonDeserializer).getObjectIdReader();
        Mockito.verify(mockUnresolvedForwardReference).getRoid();
        Mockito.verify(mockKeyDeserializer).deserializeKey(propName, mockDeserializationContext);
        Mockito.verify(mockJsonDeserializer).getNullValue(mockDeserializationContext);
        Mockito.verify(mockJsonParser).getCurrentToken();
    }

    @Test
    public void testDeserialize() throws IOException {
        Mockito.when(mockJsonToken.VALUE_NULL).thenReturn(JsonToken.VALUE_NULL);
        Mockito.when(mockValueDeserializer.getNullValue(mockDeserializationContext)).thenReturn(null);

        Object result = settableAnyProperty.deserialize(mockJsonParser, mockDeserializationContext);

        Assert.assertNull(result);
        Mockito.verify(mockJsonToken).VALUE_NULL;
        Mockito.verify(mockValueDeserializer).getNullValue(mockDeserializationContext);
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        Mockito.when(mockJsonDeserializer.deserializeWithType(mockJsonParser, mockDeserializationContext,
                mockTypeDeserializer)).thenReturn(value);

        Object result = settableAnyProperty.deserialize(mockJsonParser, mockDeserializationContext);

        Assert.assertEquals(value, result);
        Mockito.verify(mockJsonDeserializer).deserializeWithType(mockJsonParser, mockDeserializationContext,
                mockTypeDeserializer);
    }

    @Test
    public void testSet() throws IOException {
        settableAnyProperty.set(mockInstance, propName, value);

        Mockito.verify(mockAnnotatedMember).callOnWith(mockInstance, propName, value);
    }
}