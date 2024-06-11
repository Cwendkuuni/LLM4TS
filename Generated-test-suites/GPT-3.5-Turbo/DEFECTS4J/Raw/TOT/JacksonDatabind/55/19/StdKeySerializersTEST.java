import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;

public class StdKeySerializersTest {

    private StdKeySerializers stdKeySerializers;
    private SerializationConfig configMock;
    private JsonGenerator jsonGeneratorMock;
    private SerializerProvider providerMock;

    @Before
    public void setUp() {
        stdKeySerializers = new StdKeySerializers();
        configMock = mock(SerializationConfig.class);
        jsonGeneratorMock = mock(JsonGenerator.class);
        providerMock = mock(SerializerProvider.class);
    }

    @Test
    public void testGetStdKeySerializer_DefaultKeyType_ReturnsDynamic() {
        JsonSerializer<Object> result = stdKeySerializers.getStdKeySerializer(configMock, null, true);
        assertEquals(StdKeySerializers.Dynamic.class, result.getClass());
    }

    @Test
    public void testGetStdKeySerializer_StringKeyType_ReturnsStringKeySerializer() {
        JsonSerializer<Object> result = stdKeySerializers.getStdKeySerializer(configMock, String.class, true);
        assertEquals(StdKeySerializers.Default.class, result.getClass());
    }

    @Test
    public void testGetStdKeySerializer_PrimitiveKeyTypeOrNumber_ReturnsDefaultKeySerializer() {
        JsonSerializer<Object> result1 = stdKeySerializers.getStdKeySerializer(configMock, int.class, true);
        JsonSerializer<Object> result2 = stdKeySerializers.getStdKeySerializer(configMock, Number.class, true);
        assertEquals(StdKeySerializers.Default.class, result1.getClass());
        assertEquals(StdKeySerializers.Default.class, result2.getClass());
    }

    @Test
    public void testGetFallbackKeySerializer_RawKeyTypeIsNull_ReturnsDefaultKeySerializer() {
        JsonSerializer<Object> result = stdKeySerializers.getFallbackKeySerializer(configMock, null);
        assertEquals(StdKeySerializers.Default.class, result.getClass());
    }

    @Test
    public void testGetFallbackKeySerializer_RawKeyTypeIsEnum_ReturnsDynamic() {
        JsonSerializer<Object> result = stdKeySerializers.getFallbackKeySerializer(configMock, Enum.class);
        assertEquals(StdKeySerializers.Dynamic.class, result.getClass());
    }

    @Test
    public void testDefaultSerializeDateKey_TypeIsDate_CallsProviderDefaultSerializeDateKey() throws Exception {
        Date date = new Date();
        stdKeySerializers.new Default(StdKeySerializers.Default.TYPE_DATE, Date.class)
                .serialize(date, jsonGeneratorMock, providerMock);
        Mockito.verify(providerMock).defaultSerializeDateKey(Mockito.eq(date), Mockito.eq(jsonGeneratorMock));
    }

    @Test
    public void testDefaultSerializeDateKey_TypeIsCalendar_CallsProviderDefaultSerializeDateKey() throws Exception {
        Calendar calendar = Calendar.getInstance();
        stdKeySerializers.new Default(StdKeySerializers.Default.TYPE_CALENDAR, Calendar.class)
                .serialize(calendar, jsonGeneratorMock, providerMock);
        Mockito.verify(providerMock)
                .defaultSerializeDateKey(Mockito.eq(calendar.getTimeInMillis()), Mockito.eq(jsonGeneratorMock));
    }

    @Test
    public void testDefaultSerializeDateKey_TypeIsClass_WritesClassName() throws Exception {
        Class<?> clazz = String.class;
        stdKeySerializers.new Default(StdKeySerializers.Default.TYPE_CLASS, Class.class)
                .serialize(clazz, jsonGeneratorMock, providerMock);
        Mockito.verify(jsonGeneratorMock).writeFieldName(Mockito.eq(clazz.getName()));
    }

    @Test
    public void testDefaultSerializeDateKey_TypeIsEnumAndWriteEnumsUsingToStringEnabled_CallsEnumToString() throws Exception {
        Enum<?> enumObject = EnumTest.TEST1;
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        Mockito.when(mockProvider.isEnabled(Mockito.eq(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)))
                .thenReturn(true);
        stdKeySerializers.new Default(StdKeySerializers.Default.TYPE_ENUM, enumObject.getClass())
                .serialize(enumObject, jsonGeneratorMock, mockProvider);
        Mockito.verify(jsonGeneratorMock).writeFieldName(Mockito.eq(enumObject.toString()));
    }

    @Test
    public void testDefaultSerializeDateKey_TypeIsEnumAndWriteEnumsUsingToStringDisabled_CallsEnumName() throws Exception {
        Enum<?> enumObject = EnumTest.TEST1;
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        Mockito.when(mockProvider.isEnabled(Mockito.eq(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)))
                .thenReturn(false);
        stdKeySerializers.new Default(StdKeySerializers.Default.TYPE_ENUM, enumObject.getClass())
                .serialize(enumObject, jsonGeneratorMock, mockProvider);
        Mockito.verify(jsonGeneratorMock).writeFieldName(Mockito.eq(enumObject.name()));
    }

    @Test
    public void testDefaultSerializeDateKey_TypeIsToString_WritesToStringValue() throws Exception {
        Object defaultValue = new Object() {
            public String toString() {
                return "default";
            }
        };
        stdKeySerializers.new Default(StdKeySerializers.Default.TYPE_TO_STRING, defaultValue.getClass())
                .serialize(defaultValue, jsonGeneratorMock, providerMock);
        Mockito.verify(jsonGeneratorMock).writeFieldName(Mockito.eq(defaultValue.toString()));
    }

    @Test
    public void testStringKeySerializer_SerializeStringField_CallsJsonGeneratorWriteFieldName() throws Exception {
        String value = "test";
        stdKeySerializers.new StringKeySerializer().serialize(value, jsonGeneratorMock, providerMock);
        Mockito.verify(jsonGeneratorMock).writeFieldName(Mockito.eq(value));
    }

    enum EnumTest {
        TEST1, TEST2
    }

}