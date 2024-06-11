import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.IntegerSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.ShortSerializer;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonIntegerFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonNumberFormatVisitor;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.Base;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.DoubleSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.FloatSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.IntLikeSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.LongSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.ShortSerializer;

public class NumberSerializersTest {

    private NumberSerializers numberSerializers;

    @Before
    public void setUp() {
        numberSerializers = new NumberSerializers();
    }

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        numberSerializers.addAll(allDeserializers);
        assertEquals(7, allDeserializers.size());
    }

    @Test
    public void testShortSerializerSerialize() throws IOException {
        ShortSerializer shortSerializer = new ShortSerializer();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        shortSerializer.serialize(Short.MAX_VALUE, gen, provider);

        verify(gen, times(1)).writeNumber(Short.MAX_VALUE);
    }

    @Test
    public void testIntegerSerializerSerialize() throws IOException {
        IntegerSerializer integerSerializer = new IntegerSerializer();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        integerSerializer.serialize(123, gen, provider);

        verify(gen, times(1)).writeNumber(123);
    }

    @Test
    public void testIntegerSerializerSerializeWithType() throws IOException {
        IntegerSerializer integerSerializer = new IntegerSerializer();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        TypeSerializer typeSer = mock(TypeSerializer.class);

        integerSerializer.serializeWithType(456, gen, provider, typeSer);

        verify(gen, times(1)).writeNumber(456);
    }

    @Test
    public void testIntLikeSerializerSerialize() throws IOException {
        IntLikeSerializer intLikeSerializer = new IntLikeSerializer();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        intLikeSerializer.serialize(789, gen, provider);

        verify(gen, times(1)).writeNumber(789);
    }

    @Test
    public void testLongSerializerSerialize() throws IOException {
        LongSerializer longSerializer = new LongSerializer();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        longSerializer.serialize(Long.MAX_VALUE, gen, provider);

        verify(gen, times(1)).writeNumber(Long.MAX_VALUE);
    }

    @Test
    public void testFloatSerializerSerialize() throws IOException {
        FloatSerializer floatSerializer = new FloatSerializer();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        floatSerializer.serialize(1.23f, gen, provider);

        verify(gen, times(1)).writeNumber(1.23f);
    }

    @Test
    public void testDoubleSerializerSerialize() throws IOException {
        DoubleSerializer doubleSerializer = new DoubleSerializer();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        doubleSerializer.serialize(4.56d, gen, provider);

        verify(gen, times(1)).writeNumber(4.56d);
    }

    @Test
    public void testBaseGetSchema() {
        Base<Object> base = new IntegerSerializer();
        SerializerProvider provider = mock(SerializerProvider.class);

        assertEquals(null, base.getSchema(provider, null));
    }

    @Test
    public void testBaseAcceptJsonFormatVisitorForInteger() throws JsonMappingException {
        Base<Object> base = new IntegerSerializer();
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        JsonIntegerFormatVisitor intVisitor = mock(JsonIntegerFormatVisitor.class);

        when(visitor.expectIntegerFormat(typeHint)).thenReturn(intVisitor);

        base.acceptJsonFormatVisitor(visitor, typeHint);

        verify(intVisitor, times(1)).numberType(JsonParser.NumberType.INT);
    }

    @Test
    public void testBaseAcceptJsonFormatVisitorForFloat() throws JsonMappingException {
        Base<Object> base = new FloatSerializer();
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        JsonNumberFormatVisitor numberVisitor = mock(JsonNumberFormatVisitor.class);

        when(visitor.expectNumberFormat(typeHint)).thenReturn(numberVisitor);

        base.acceptJsonFormatVisitor(visitor, typeHint);

        verify(numberVisitor, times(1)).numberType(JsonParser.NumberType.FLOAT);
    }

    @Test
    public void testBaseCreateContextual() throws JsonMappingException {
        Base<Object> base = new IntegerSerializer();
        SerializerProvider provider = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember member = mock(AnnotatedMember.class);
        JsonFormat.Value format = mock(JsonFormat.Value.class);

        when(property.getMember()).thenReturn(member);
        when(provider.getAnnotationIntrospector().findFormat(member)).thenReturn(format);

        base.createContextual(provider, property);

        verify(provider.getAnnotationIntrospector(), times(1)).findFormat(member);
    }

}