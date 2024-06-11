
import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.Locale;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.core.JsonGenerator;
import java.io.IOException;

public class DateTimeSerializerBaseTest {

    @Test
    public void testWithFormat() {
        DateTimeSerializerBase serializer = new DateTimeSerializerBaseTestImpl();
        Boolean timestamp = false;
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateTimeSerializerBase result = serializer.withFormat(timestamp, customFormat);
        assertNotNull(result);
        assertEquals(timestamp, result._useTimestamp);
        assertEquals(customFormat, result._customFormat);
    }

    private class DateTimeSerializerBaseTestImpl extends DateTimeSerializerBase {
		private DateTimeSerializerBaseTestImpl() {
			super(null, null, null);
		}

		@Override
		protected long _timestamp(Object value) {
			return 0;
		}

		@Override
		public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
				throws IOException {
		}
	}
}
