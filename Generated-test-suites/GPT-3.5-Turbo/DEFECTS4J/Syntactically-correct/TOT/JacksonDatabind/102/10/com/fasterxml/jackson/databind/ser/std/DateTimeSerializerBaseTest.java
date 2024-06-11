import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeSerializerBaseTest {

    @Mock
    private SerializerProvider serializerProvider;

    @Test
    public void testSerialize_withCustomFormat() {
        // Arrange
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializer(customFormat);
        Date date = new Date(2021, 10, 1);

        // Act
        String serialized = serializer.serializeAsString(date, null, serializerProvider);

        // Assert
        Assert.assertEquals("2021-10-01", serialized);
    }

    @Test
    public void testSerialize_withoutCustomFormat() {
        // Arrange
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializer(null);
        Date date = new Date(2021, 10, 1);

        // Act
        String serialized = serializer.serializeAsString(date, null, serializerProvider);

        // Assert
        Assert.assertEquals("2021-10-01", serialized);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerialize_withNullSerializerProvider() {
        // Arrange
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializer(null);
        Date date = new Date(2021, 10, 1);

        // Act
        serializer.serializeAsString(date, null, null);

        // Assert - IllegalArgumentException should be thrown
    }

    @Test
    public void testSerialize_withTimestampFlag() {
        // Arrange
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializer(true, null);
        Date date = new Date(2021, 10, 1);

        // Act
        String serialized = serializer.serializeAsString(date, null, serializerProvider);

        // Assert
        Assert.assertEquals("1635721200000", serialized);
    }

    @Test
    public void testSerialize_emptyDate() {
        // Arrange
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializer(null);
        Date date = null;

        // Act
        String serialized = serializer.serializeAsString(date, null, serializerProvider);

        // Assert
        Assert.assertEquals("", serialized);
    }

    @Test
    public void testSerialize_defaultSerializeDateValueCalled() {
        // Arrange
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializer(null);
        Date date = new Date(2021, 10, 1);

        // Mock the defaultSerializeDateValue method
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider provider = mapper.getSerializerProvider();
        provider.defaultSerializeDateValue(date, null);

        // Act
        String serialized = serializer.serializeAsString(date, null, provider);

        // Assert
        Assert.assertEquals(mapper.writeValueAsString(date), serialized);
    }
}