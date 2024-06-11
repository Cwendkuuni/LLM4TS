import org.junit.*;
import static org.junit.Assert.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

public class StdKeySerializersTest {

    @Mock
    private SerializationConfig config;
    
    @Mock
    private Class<?> rawKeyType;
    
    @Mock
    private JsonSerializer<Object> jsonSerializer;
    
    @Mock
    private JsonGenerator jsonGenerator;
    
    @Mock
    private SerializerProvider serializerProvider;
    
    private StdKeySerializers.Default keySerializer;
    private StdKeySerializers.Dynamic dynamicKeySerializer;
    private StdKeySerializers.StringKeySerializer stringKeySerializer;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        keySerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_DATE, rawKeyType);
        dynamicKeySerializer = new StdKeySerializers.Dynamic();
        stringKeySerializer = new StdKeySerializers.StringKeySerializer();
    }
    
    @Test
    public void testGetStdKeySerializer_NullRawKeyType_ReturnsDynamicSerializer() {
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, null, false);
        assertTrue(serializer instanceof StdKeySerializers.Dynamic);
    }
    
    @Test
    public void testGetStdKeySerializer_StringRawKeyType_ReturnsStringKeySerializer() {
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, String.class, false);
        assertTrue(serializer instanceof StdKeySerializers.StringKeySerializer);
    }
    
    @Test
    public void testGetStdKeySerializer_NumbersRawKeyType_ReturnsDefaultKeySerializer() {
        when(rawKeyType.isPrimitive()).thenReturn(true);
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
        assertTrue(serializer instanceof StdKeySerializers.Default);
    }
    
    @Test
    public void testGetStdKeySerializer_ClassRawKeyType_ReturnsDefaultKeySerializer() {
        when(rawKeyType == Class.class).thenReturn(true);
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
        assertTrue(serializer instanceof StdKeySerializers.Default);
    }
    
    @Test
    public void testGetStdKeySerializer_DateRawKeyType_ReturnsDefaultKeySerializer() {
        when(Date.class.isAssignableFrom(rawKeyType)).thenReturn(true);
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
        assertTrue(serializer instanceof StdKeySerializers.Default);
    }
    
    @Test
    public void testGetStdKeySerializer_CalendarRawKeyType_ReturnsDefaultKeySerializer() {
        when(Calendar.class.isAssignableFrom(rawKeyType)).thenReturn(true);
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
        assertTrue(serializer instanceof StdKeySerializers.Default);
    }
    
    @Test
    public void testGetStdKeySerializer_UUIDRawKeyType_ReturnsDefaultKeySerializer() {
        when(rawKeyType == java.util.UUID.class).thenReturn(true);
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);
        assertTrue(serializer instanceof StdKeySerializers.Default);
    }
    
    @Test
    public void testGetFallbackKeySerializer_EnumClassRawKeyType_ReturnsDynamicSerializer() {
        when(rawKeyType == Enum.class).thenReturn(true);
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertTrue(serializer instanceof StdKeySerializers.Dynamic);
    }
    
    @Test
    public void testGetFallbackKeySerializer_EnumRawKeyType_ReturnsDefaultKeySerializer() {
        when(rawKeyType.isEnum()).thenReturn(true);
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);
        assertTrue(serializer instanceof StdKeySerializers.Default);
    }
    
    @Test
    public void testSerialize_DefaultKeySerializer_DateValue_SerializesUsingDefaultSerializeDateKey() throws Exception {
        Date date = new Date();
        keySerializer.serialize(date, jsonGenerator, serializerProvider);
        verify(serializerProvider).defaultSerializeDateKey(date, jsonGenerator);
    }
    
    @Test
    public void testSerialize_DefaultKeySerializer_CalendarValue_SerializesUsingDefaultSerializeDateKey() throws Exception {
        Calendar calendar = Calendar.getInstance();
        keySerializer.serialize(calendar, jsonGenerator, serializerProvider);
        verify(serializerProvider).defaultSerializeDateKey(calendar.getTimeInMillis(), jsonGenerator);
    }
    
    @Test
    public void testSerialize_DefaultKeySerializer_ClassValue_WritesFieldName() throws Exception {
        Class<?> clazz = String.class;
        keySerializer.serialize(clazz, jsonGenerator, serializerProvider);
        verify(jsonGenerator).writeFieldName(clazz.getName());
    }
    
    @Test
    public void testSerialize_DefaultKeySerializer_EnumWriteEnumsUsingToString_SerializesUsingToString() throws Exception {
        when(serializerProvider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(true);
        Enum<?> enumValue = Enum.TEST;
        keySerializer.serialize(enumValue, jsonGenerator, serializerProvider);
        verify(jsonGenerator).writeFieldName(enumValue.toString());
    }
    
    @Test
    public void testSerialize_DefaultKeySerializer_EnumWriteEnumsNotUsingToString_SerializesUsingEnumName() throws Exception {
        when(serializerProvider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(false);
        Enum<?> enumValue = Enum.TEST;
        keySerializer.serialize(enumValue, jsonGenerator, serializerProvider);
        verify(jsonGenerator).writeFieldName(enumValue.name());
    }
    
    @Test
    public void testSerialize_DefaultKeySerializer_OtherValue_WritesValueToString() throws Exception {
        Object value = new Object();
        String valueToString = value.toString();
        keySerializer.serialize(value, jsonGenerator, serializerProvider);
        verify(jsonGenerator).writeFieldName(valueToString);
    }
    
    @Test
    public void testSerialize_DynamicKeySerializer_SerializeObject_CallsSerializeOnDynamicSerializer() throws Exception {
        Object value = new Object();
        when(rawKeyType.getClass()).thenReturn(Object.class);
        when(dynamicKeySerializer._dynamicSerializers.serializerFor(Object.class)).thenReturn(jsonSerializer);
        dynamicKeySerializer.serialize(value, jsonGenerator, serializerProvider);
        verify(jsonSerializer).serialize(value, jsonGenerator, serializerProvider);
    }
    
    @Test
    public void testSerialize_DynamicKeySerializer_SerializeClass_CallsSerializeOnDynamicSerializer() throws Exception {
        Object value = String.class;
        when(rawKeyType.getClass()).thenReturn(Class.class);
        when(dynamicKeySerializer._dynamicSerializers.serializerFor(Class.class)).thenReturn(jsonSerializer);
        dynamicKeySerializer.serialize(value, jsonGenerator, serializerProvider);
        verify(jsonSerializer).serialize(value, jsonGenerator, serializerProvider);
    }
    
    @Test
    public void testSerialize_StringKeySerializer_SerializesStringKey() throws Exception {
        Object key = "key";
        stringKeySerializer.serialize(key, jsonGenerator, serializerProvider);
        verify(jsonGenerator).writeFieldName((String) key);
    }
    
    private enum Enum {
        TEST
    }
}