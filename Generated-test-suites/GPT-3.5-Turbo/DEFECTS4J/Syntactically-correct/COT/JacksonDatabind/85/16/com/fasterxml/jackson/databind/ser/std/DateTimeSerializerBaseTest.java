
import static org.junit.Assert.*;

import org.junit.Test;

public class DateTimeSerializerBaseTest {

    @Test
    public void testWithFormat() {
        DateTimeSerializerBase<?> serializer = new DateTimeSerializerBaseTestImpl();
        DateTimeSerializerBase<?> result = serializer.withFormat(false, null);
        assertFalse(result._useTimestamp);
        assertNull(result._customFormat);

        result = serializer.withFormat(true, null);
        assertTrue(result._useTimestamp);
        assertNull(result._customFormat);

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        result = serializer.withFormat(false, format);
        assertFalse(result._useTimestamp);
        assertEquals(format, result._customFormat);
    }

    private static class DateTimeSerializerBaseTestImpl extends DateTimeSerializerBase<Object> {

        private DateTimeSerializerBaseTestImpl() {
            super(Object.class, null, null);
        }

        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
                throws IOException {
            // TODO Auto-generated method stub
        }

        @Override
        protected long _timestamp(Object value) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
                throws JsonMappingException {
            // TODO Auto-generated method stub
        }
    }

}
