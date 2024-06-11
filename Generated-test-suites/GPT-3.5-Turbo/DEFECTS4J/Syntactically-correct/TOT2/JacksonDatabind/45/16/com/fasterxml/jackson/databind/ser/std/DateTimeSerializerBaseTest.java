import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.ser.std.StdDateFormat;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class DateTimeSerializerBaseTest {

    @Test
    public void testWithFormat() {
        DateTimeSerializerBase<TestObject> serializer = new DateTimeSerializerBase<TestObject>(TestObject.class, true, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        DateTimeSerializerBase<TestObject> result = serializer.withFormat(false, null);
        assertFalse(result._asTimestamp(null));
        assertNull(result._customFormat);
    }

    @Test
    public void testCreateContextual() {
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        BeanProperty property = Mockito.mock(BeanProperty.class);

        DateTimeSerializerBase<TestObject> serializer = new DateTimeSerializerBase<TestObject>(TestObject.class, true, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        JsonSerializer<?> result = serializer.createContextual(provider, property);
        assertNotNull(result);
    }

    @Test
    public void testIsEmpty() {
        TestObject nullObj = null;
        TestObject obj = new TestObject(0L);

        DateTimeSerializerBase<TestObject> serializer = new DateTimeSerializerBase<TestObject>(TestObject.class, true, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        assertTrue(serializer.isEmpty(nullObj));
        assertTrue(serializer.isEmpty(obj));
    }

    @Test
    public void testGetSchema() {
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        JavaType typeHint = Mockito.mock(JavaType.class);

        DateTimeSerializerBase<TestObject> serializer = new DateTimeSerializerBase<TestObject>(TestObject.class, true, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        assertNotNull(serializer.getSchema(provider, typeHint));
    }

    @Test
    public void testSerialize() {
        TestObject obj = new TestObject(1234567890L);
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);

        DateTimeSerializerBase<TestObject> serializer = new DateTimeSerializerBase<TestObject>(TestObject.class, true, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        assertDoesNotThrow(() -> serializer.serialize(obj, generator, provider));
    }

    @Test
    public void test_acceptJsonFormatVisitor() {
        JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = Mockito.mock(JavaType.class);

        DateTimeSerializerBase<TestObject> serializer = new DateTimeSerializerBase<TestObject>(TestObject.class, true, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        assertDoesNotThrow(() -> serializer.acceptJsonFormatVisitor(visitor, typeHint));
    }

    private static class TestObject {
        long timestamp;

        TestObject(long timestamp) {
            this.timestamp = timestamp;
        }
    }
}