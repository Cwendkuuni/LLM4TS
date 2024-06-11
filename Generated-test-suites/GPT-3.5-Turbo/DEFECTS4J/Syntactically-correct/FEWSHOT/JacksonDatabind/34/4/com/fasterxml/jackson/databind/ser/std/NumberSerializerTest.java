import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.mockito.Mock;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;
import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberSerializerTest {

    @Test
    public void testSerialize() throws IOException {
        JsonGenerator mockJsonGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider mockSerializerProvider = Mockito.mock(SerializerProvider.class);

        BigDecimal bigDecimal = new BigDecimal(3.14159);
        BigInteger bigInteger = new BigInteger("1234567890");
        Integer integer = new Integer(42);
        Long longNumber = new Long(1234567890);
        Double doubleNumber = new Double(3.14159);
        Float floatNumber = new Float(2.71828);
        Byte byteNumber = new Byte("1");
        Short shortNumber = new Short("5");
        Number otherNumber = new Number() {
            public int intValue() { return 0; }
            public long longValue() { return 0; }
            public float floatValue() { return 0; }
            public double doubleValue() { return 0; }
        };

        NumberSerializer.instance.serialize(bigDecimal, mockJsonGenerator, mockSerializerProvider);
        NumberSerializer.instance.serialize(bigInteger, mockJsonGenerator, mockSerializerProvider);
        NumberSerializer.instance.serialize(integer, mockJsonGenerator, mockSerializerProvider);
        NumberSerializer.instance.serialize(longNumber, mockJsonGenerator, mockSerializerProvider);
        NumberSerializer.instance.serialize(doubleNumber, mockJsonGenerator, mockSerializerProvider);
        NumberSerializer.instance.serialize(floatNumber, mockJsonGenerator, mockSerializerProvider);
        NumberSerializer.instance.serialize(byteNumber, mockJsonGenerator, mockSerializerProvider);
        NumberSerializer.instance.serialize(shortNumber, mockJsonGenerator, mockSerializerProvider);
        NumberSerializer.instance.serialize(otherNumber, mockJsonGenerator, mockSerializerProvider);

        Mockito.verify(mockJsonGenerator, Mockito.times(1)).writeNumber(bigDecimal);
        Mockito.verify(mockJsonGenerator, Mockito.times(1)).writeNumber(bigInteger);
        Mockito.verify(mockJsonGenerator, Mockito.times(1)).writeNumber(integer.intValue());
        Mockito.verify(mockJsonGenerator, Mockito.times(1)).writeNumber(longNumber.longValue());
        Mockito.verify(mockJsonGenerator, Mockito.times(1)).writeNumber(doubleNumber.doubleValue());
        Mockito.verify(mockJsonGenerator, Mockito.times(1)).writeNumber(floatNumber.floatValue());
        Mockito.verify(mockJsonGenerator, Mockito.times(1)).writeNumber(byteNumber.intValue());
        Mockito.verify(mockJsonGenerator, Mockito.times(1)).writeNumber(shortNumber.intValue());
        Mockito.verify(mockJsonGenerator, Mockito.times(1)).writeNumber(otherNumber.toString());
    }

    @Test
    public void testGetSchema() {
        SerializerProvider mockSerializerProvider = Mockito.mock(SerializerProvider.class);
        Type typeHint = Type.class;

        JsonNode schema = NumberSerializer.instance.getSchema(mockSerializerProvider, typeHint);

        assertEquals(schema.isNumber(), true);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitorMock = Mockito.mock(JsonFormatVisitorWrapper.class);
        JavaType typeHintMock = Mockito.mock(JavaType.class);

        NumberSerializer.instance.acceptJsonFormatVisitor(visitorMock, typeHintMock);

        Mockito.verify(visitorMock, Mockito.times(1)).expectNumberFormat(typeHintMock);
    }
}