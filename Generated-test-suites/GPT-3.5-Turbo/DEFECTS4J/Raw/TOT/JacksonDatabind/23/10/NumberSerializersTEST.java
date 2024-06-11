import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonIntegerFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonNumberFormatVisitor;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

public class NumberSerializersTest {
    
    private Map<String, JsonSerializer<?>> allDeserializers;
    
    @Before
    public void setUp() {
        allDeserializers = new HashMap<String, JsonSerializer<?>>();
        NumberSerializers.addAll(allDeserializers);
    }
    
    @Test
    public void testShortSerializer() throws IOException {
        ShortSerializer shortSerializer = new ShortSerializer();
        assertEquals(Short.class, shortSerializer.handledType());
        assertEquals(JsonParser.NumberType.INT, shortSerializer._numberType);
        assertEquals("number", shortSerializer._schemaType);

        // Typical Use Case
        JsonGenerator mockJsonGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        shortSerializer.serialize((short) 10, mockJsonGenerator, mockProvider);
        verify(mockJsonGenerator).writeNumber((short) 10);

        // Edge Case
        shortSerializer.serialize((short) 32767, mockJsonGenerator, mockProvider);
        verify(mockJsonGenerator).writeNumber((short) 32767);
    }
    
    @Test
    public void testIntegerSerializer() throws IOException {
        IntegerSerializer integerSerializer = new IntegerSerializer();
        assertEquals(Object.class, integerSerializer.handledType());
        assertEquals(JsonParser.NumberType.INT, integerSerializer._numberType);
        assertEquals("integer", integerSerializer._schemaType);
        
        // Typical Use Case
        JsonGenerator mockJsonGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        integerSerializer.serialize(10, mockJsonGenerator, mockProvider);
        verify(mockJsonGenerator).writeNumber(10);

        // Edge Case
        integerSerializer.serialize(-2147483648, mockJsonGenerator, mockProvider);
        verify(mockJsonGenerator).writeNumber(-2147483648);
    }
    
    @Test
    public void testIntLikeSerializer() throws IOException {
        IntLikeSerializer intLikeSerializer = new IntLikeSerializer();
        assertEquals(Number.class, intLikeSerializer.handledType());
        assertEquals(JsonParser.NumberType.INT, intLikeSerializer._numberType);
        assertEquals("integer", intLikeSerializer._schemaType);
        
        // Typical Use Case
        JsonGenerator mockJsonGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        intLikeSerializer.serialize(10, mockJsonGenerator, mockProvider);
        verify(mockJsonGenerator).writeNumber(10);

        // Edge Case
        intLikeSerializer.serialize(-2147483648, mockJsonGenerator, mockProvider);
        verify(mockJsonGenerator).writeNumber(-2147483648);
    }
    
    @Test
    public void testLongSerializer() throws IOException {
        LongSerializer longSerializer = new LongSerializer();
        assertEquals(Object.class, longSerializer.handledType());
        assertEquals(JsonParser.NumberType.LONG, longSerializer._numberType);
        assertEquals("number", longSerializer._schemaType);
        
        // Typical Use Case
        JsonGenerator mockJsonGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        longSerializer.serialize(10L, mockJsonGenerator, mockProvider);
        verify(mockJsonGenerator).writeNumber(10L);

        // Edge Case
        longSerializer.serialize(-9223372036854775808L, mockJsonGenerator, mockProvider);
        verify(mockJsonGenerator).writeNumber(-9223372036854775808L);
    }
    
    @Test
    public void testFloatSerializer() throws IOException {
        FloatSerializer floatSerializer = new FloatSerializer();
        assertEquals(Object.class, floatSerializer.handledType());
        assertEquals(JsonParser.NumberType.FLOAT, floatSerializer._numberType);
        assertEquals("number", floatSerializer._schemaType);
        
        // Typical Use Case
        JsonGenerator mockJsonGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        floatSerializer.serialize(10f, mockJsonGenerator, mockProvider);
        verify(mockJsonGenerator).writeNumber(10f);

        // Edge Case
        floatSerializer.serialize(Float.MAX_VALUE, mockJsonGenerator, mockProvider);
        verify(mockJsonGenerator).writeNumber(Float.MAX_VALUE);
    }
    
    @Test
    public void testDoubleSerializer() throws IOException {
        DoubleSerializer doubleSerializer = new DoubleSerializer();
        assertEquals(Object.class, doubleSerializer.handledType());
        assertEquals(JsonParser.NumberType.DOUBLE, doubleSerializer._numberType);
        assertEquals("number", doubleSerializer._schemaType);
        
        // Typical Use Case
        JsonGenerator mockJsonGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        doubleSerializer.serialize(10d, mockJsonGenerator, mockProvider);
        verify(mockJsonGenerator).writeNumber(10d);

        // Edge Case
        doubleSerializer.serialize(Double.MIN_VALUE, mockJsonGenerator, mockProvider);
        verify(mockJsonGenerator).writeNumber(Double.MIN_VALUE);
    }
    
    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> deserializers = new HashMap<String, JsonSerializer<?>>();
        NumberSerializers.addAll(deserializers);
        assertEquals(7, deserializers.size());
        
        assertTrue(deserializers.containsKey(Integer.class.getName()));
        assertTrue(deserializers.containsKey(Integer.TYPE.getName()));
        assertTrue(deserializers.containsKey(Long.class.getName()));
        assertTrue(deserializers.containsKey(Long.TYPE.getName()));
        assertTrue(deserializers.containsKey(Byte.class.getName()));
        assertTrue(deserializers.containsKey(Byte.TYPE.getName()));
        assertTrue(deserializers.containsKey(Short.class.getName()));
        assertTrue(deserializers.containsKey(Short.TYPE.getName()));
        assertTrue(deserializers.containsKey(Float.class.getName()));
        assertTrue(deserializers.containsKey(Float.TYPE.getName()));
        assertTrue(deserializers.containsKey(Double.class.getName()));
        assertTrue(deserializers.containsKey(Double.TYPE.getName()));
    }

    @Test
    public void testBaseMethods() throws JsonMappingException {
        Base<Object> base = new IntegerSerializer();
        
        // Test getSchema method
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        JsonNode jsonNode = base.getSchema(mockProvider, null);
        assertNotNull(jsonNode);
        
        // Test acceptJsonFormatVisitor method
        JsonFormatVisitorWrapper mockVisitor = mock(JsonFormatVisitorWrapper.class);
        JavaType mockJavaType = mock(JavaType.class);
        base.acceptJsonFormatVisitor(mockVisitor, mockJavaType);
        if (base._isInt) {
            verify(mockVisitor).expectIntegerFormat(mockJavaType);
        } else {
            verify(mockVisitor).expectNumberFormat(mockJavaType);
        }
        
        // Test createContextual method
        BeanProperty mockProperty = mock(BeanProperty.class);
        base.createContextual(mockProvider, mockProperty);
        verify(mockProvider).getAnnotationIntrospector();
        verify(mockProperty).getMember();
    }
}