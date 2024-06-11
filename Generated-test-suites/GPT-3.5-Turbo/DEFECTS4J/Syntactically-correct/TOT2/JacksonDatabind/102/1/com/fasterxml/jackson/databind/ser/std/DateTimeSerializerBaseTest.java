import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;

@RunWith(MockitoJUnitRunner.class)
public class DateTimeSerializerBaseTest {

    @Mock
    private JsonGenerator jsonGenerator;

    @Mock
    private SerializerProvider serializerProvider;

    @Test
    public void testSerialize() throws Exception {
        Date date = new Date();
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerMock(Date.class, true, null);

        serializer.serialize(date, jsonGenerator, serializerProvider);

        verify(jsonGenerator, times(1)).writeString(anyString());
    }

    private static class DateTimeSerializerMock extends DateTimeSerializerBase<Date> {

        public DateTimeSerializerMock(Class<Date> type, Boolean useTimestamp, DateFormat customFormat) {
            super(type, useTimestamp, customFormat);
        }

        @Override
        public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
            return null;
        }

        @Override
        protected long _timestamp(Date value) {
            return value.getTime();
        }
    }

    @Test
    public void testGetSchema() {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerMock(Date.class, true, null);

        assertNotNull(serializer.getSchema(serializerProvider, null));
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerMock(Date.class, true, null);

        serializer.acceptJsonFormatVisitor(mock(JsonFormatVisitorWrapper.class), mock(JavaType.class));
    }

    @Test
    public void testIsEmpty() {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerMock(Date.class, true, null);
        Date date = new Date();

        assertFalse(serializer.isEmpty(serializerProvider, date));
    }

    @Test
    public void test_asTimestamp() {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerMock(Date.class, true, null);
        assertTrue(serializer._asTimestamp(serializerProvider));
    }

    @Test
    public void testAcceptJsonFormatVisitor_withoutAsNumber() throws Exception {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerMock(Date.class, true, null);

        serializer._acceptJsonFormatVisitor(mock(JsonFormatVisitorWrapper.class), mock(JavaType.class), false);
    }

    @Test
    public void testAcceptJsonFormatVisitor_withAsNumber() throws Exception {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerMock(Date.class, true, null);

        serializer._acceptJsonFormatVisitor(mock(JsonFormatVisitorWrapper.class), mock(JavaType.class), true);
    }

    @Test
    public void test_serializeAsString() throws Exception {
        Date date = new Date();
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerMock(Date.class, true, new SimpleDateFormat("yyyy-MM-dd"));

        serializer._serializeAsString(date, jsonGenerator, serializerProvider);

        verify(jsonGenerator, times(1)).writeString(anyString());
    }
}