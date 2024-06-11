import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeSerializerBaseTest {

    @Test
    public void testDateTimeSerializerBaseSerializesAndDeserializesDate() throws JsonMappingException, JsonGenerationException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        DateTimeSerializerBase<Date> dateTimeSerializerBase = mapper.findValueSerializer(Date.class);

        Date date = new Date();
        String serializedDate = mapper.writeValueAsString(date);
        Date deserializedDate = mapper.readValue(serializedDate, Date.class);

        Assert.assertEquals(date, deserializedDate);
    }

    @Test
    public void testDateTimeSerializerBaseUsesCustomFormat() throws JsonMappingException, JsonGenerationException, IOException {
        ObjectMapper mapper = new ObjectMapper();

        DateTimeSerializerBase<Date> dateTimeSerializerBase = mapper.findValueSerializer(Date.class);
        DateTimeSerializerBase<Date> customFormatterSerializerBase = dateTimeSerializerBase.withFormat(Boolean.FALSE, new SimpleDateFormat("dd-MM-yyyy"));

        Date date = new Date();
        String serializedDate = mapper.writeValueAsString(date);

        SimpleDateFormat customFormatter = new SimpleDateFormat("dd-MM-yyyy");
        Date deserializedDate = customFormatter.parse(mapper.writeValueAsString(customFormatterSerializerBase.serialize(date, null, null)));

        Assert.assertEquals(date, deserializedDate);
    }

    @Test(expected = JsonMappingException.class)
    public void testDateTimeSerializerBaseThrowsJsonMappingExceptionWhenCustomFormatIsInvalid() throws JsonMappingException, JsonGenerationException, IOException {
        ObjectMapper mapper = new ObjectMapper();

        DateTimeSerializerBase<Date> dateTimeSerializerBase = mapper.findValueSerializer(Date.class);
        DateTimeSerializerBase<Date> customFormatterSerializerBase = dateTimeSerializerBase.withFormat(Boolean.FALSE, new SimpleDateFormat("dd-MM-yy")); // Invalid format

        Date date = new Date();
        String serializedDate = mapper.writeValueAsString(date);

        try {
            mapper.readValue(serializedDate, Date.class);
        } catch (IOException e) {
            throw new JsonMappingException("Expected JsonMappingException but got IOException: " + e.getMessage());
        }

        customFormatterSerializerBase.serialize(date, null, null);
    }

    @Test
    public void testDateTimeSerializerBaseUsesDefaultLocaleAndTimeZone() throws JsonMappingException, JsonGenerationException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        Locale defaultLocale = Locale.getDefault();
        TimeZone defaultTimeZone = TimeZone.getDefault();

        DateTimeSerializerBase<Date> dateTimeSerializerBase = mapper.findValueSerializer(Date.class);
        Date date = new Date();
        String serializedDate = mapper.writeValueAsString(date);

        DateTimeSerializerBase<Date> serializerWithDefaultLocaleAndTimeZone = mapper.findValueSerializer(Date.class);
        Date deserializedDate = serializerWithDefaultLocaleAndTimeZone.deserialize(serializedDate, null, null);

        Assert.assertEquals(date, deserializedDate);
        Assert.assertEquals(defaultLocale, serializerWithDefaultLocaleAndTimeZone.getLocale());
        Assert.assertEquals(defaultTimeZone, serializerWithDefaultLocaleAndTimeZone.getTimeZone());
    }

    @Test
    public void testDateTimeSerializerBaseUsesConfiguredLocaleAndTimeZone() throws JsonMappingException, JsonGenerationException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        Locale customLocale = new Locale("fr", "FR");
        TimeZone customTimeZone = TimeZone.getTimeZone("Europe/Paris");

        DateTimeSerializerBase<Date> dateTimeSerializerBase = mapper.findValueSerializer(Date.class);
        DateTimeSerializerBase<Date> customLocaleAndTimeZoneSerializerBase = dateTimeSerializerBase.withFormat(Boolean.FALSE, new SimpleDateFormat("dd-MM-yyyy"))
                .withLocale(customLocale).withTimeZone(customTimeZone);

        Date date = new Date();
        String serializedDate = mapper.writeValueAsString(date);

        Date deserializedDate = customLocaleAndTimeZoneSerializerBase.deserialize(serializedDate, null, null);

        Assert.assertEquals(date, deserializedDate);
        Assert.assertEquals(customLocale, customLocaleAndTimeZoneSerializerBase.getLocale());
        Assert.assertEquals(customTimeZone, customLocaleAndTimeZoneSerializerBase.getTimeZone());
    }

    @Test
    public void testDateTimeSerializerBaseUsesCustomFormatWithLocaleAndTimeZone() throws JsonMappingException, JsonGenerationException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        Locale customLocale = new Locale("fr", "FR");
        TimeZone customTimeZone = TimeZone.getTimeZone("Europe/Paris");

        DateTimeSerializerBase<Date> dateTimeSerializerBase = mapper.findValueSerializer(Date.class);
        DateTimeSerializerBase<Date> customFormatterSerializerBase = dateTimeSerializerBase.withFormat(Boolean.FALSE, new SimpleDateFormat("dd-MM-yyyy"))
                .withLocale(customLocale).withTimeZone(customTimeZone);

        Date date = new Date();
        String serializedDate = mapper.writeValueAsString(date);

        SimpleDateFormat customFormatter = new SimpleDateFormat("dd-MM-yyyy");
        customFormatter.setLocale(customLocale);
        customFormatter.setTimeZone(customTimeZone);

        Date deserializedDate = customFormatter.parse(mapper.writeValueAsString(customFormatterSerializerBase.serialize(date, null, null)));

        Assert.assertEquals(date, deserializedDate);
        Assert.assertEquals(customLocale, customFormatterSerializerBase.getLocale());
        Assert.assertEquals(customTimeZone, customFormatterSerializerBase.getTimeZone());
    }
}