import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DateTimeSerializerBaseTest {
  
  private class DateTimeSerializerTest extends DateTimeSerializerBase<Date> {

    public DateTimeSerializerTest(Class<Date> type,
                                  Boolean useTimestamp, DateFormat customFormat) {
        super(type, useTimestamp, customFormat);
    }

    @Override
    public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
        return null;
    }

    @Override
    public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

    }

    @Override
    protected long _timestamp(Date value) {
        return 0;
    }
  }

  private static final boolean USE_TIMESTAMP_TRUE = true;
  private static final boolean USE_TIMESTAMP_FALSE = false;
  private static final boolean USE_TIMESTAMP_NULL = null;

  private static final DateFormat CUSTOM_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
  private static final DateFormat CUSTOM_FORMAT_NULL = null;
  
  private DateTimeSerializerBase<Date> dateTimeSerializerBase;
  
  @Before
  public void setup() {
    dateTimeSerializerBase = new DateTimeSerializerTest(Date.class, USE_TIMESTAMP_TRUE, CUSTOM_FORMAT);
  }

  @Test
  public void testEmpty() throws IOException {
    SerializerProvider serializerProvider = new SerializerProvider();
    StdDateFormat stdDateFormat = new StdDateFormat();
    serializerProvider.setConfig(stdDateFormat);
    Date value = new Date();
    assertFalse(dateTimeSerializerBase.isEmpty(serializerProvider, value));
  }

  @Test
  public void testGetSchema() {
    SerializerProvider serializerProvider = new SerializerProvider();
    StdDateFormat stdDateFormat = new StdDateFormat();
    serializerProvider.setConfig(stdDateFormat);
    Type typeHint = Date.class;
    JsonNode schema = dateTimeSerializerBase.getSchema(serializerProvider, typeHint);
    assertEquals("string", schema.get("type").asText());
  }

  @Test
  public void testAcceptJsonFormatVisitor() throws JsonMappingException {
    JsonFormatVisitorWrapper jsonFormatVisitorWrapper = null; // add visitor object
    JavaType typeHint = null; // add typeHint object
    dateTimeSerializerBase.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, typeHint);
    // assert something
  }
}