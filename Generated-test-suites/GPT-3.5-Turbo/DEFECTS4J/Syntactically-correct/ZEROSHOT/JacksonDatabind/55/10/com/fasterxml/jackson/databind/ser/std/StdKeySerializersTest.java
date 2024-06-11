import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.SerializationConfig;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.StringKeySerializer;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.Default;

@RunWith(PowerMockRunner.class)
public class StdKeySerializersTest {

    @Mock
    private SerializationConfig config;

    @Mock
    private Class<?> rawKeyType;

    @Mock
    private JsonSerializer<Object> serializer;

    @Mock
    private JsonGenerator generator;

    @Mock
    private SerializerProvider provider;

    @Test
    public void testGetStdKeySerializer_WhenRawKeyTypeIsNull() {
        boolean useDefault = true;
        assertNotNull(StdKeySerializers.getStdKeySerializer(config, null, useDefault));
    }

    @Test
    public void testGetStdKeySerializer_WhenRawKeyTypeIsString() {
        boolean useDefault = true;
        assertEquals(StringKeySerializer.class, StdKeySerializers.getStdKeySerializer(config, String.class, useDefault).getClass());
    }

    @Test
    public void testGetStdKeySerializer_WhenRawKeyTypeIsPrimitiveClass() {
        boolean useDefault = true;
        assertEquals(Default.class, StdKeySerializers.getStdKeySerializer(config, int.class, useDefault).getClass());
    }
    
    @Test
    public void testGetStdKeySerializer_WhenRawKeyTypeIsNumberClass() {
        boolean useDefault = true;
        assertEquals(Default.class, StdKeySerializers.getStdKeySerializer(config, Double.class, useDefault).getClass());
    }
    
    @Test
    public void testGetStdKeySerializer_WhenRawKeyTypeIsClassClass() {
        boolean useDefault = true;
        assertEquals(Default.class, StdKeySerializers.getStdKeySerializer(config, Class.class, useDefault).getClass());
    }
    
    @Test
    public void testGetStdKeySerializer_WhenRawKeyTypeIsDateClass() {
        boolean useDefault = true;
        assertEquals(Default.class, StdKeySerializers.getStdKeySerializer(config, Date.class, useDefault).getClass());
    }
    
    @Test
    public void testGetStdKeySerializer_WhenRawKeyTypeIsCalendarClass() {
        boolean useDefault = true;
        assertEquals(Default.class, StdKeySerializers.getStdKeySerializer(config, Calendar.class, useDefault).getClass());
    }
    
    @Test
    public void testGetStdKeySerializer_WhenRawKeyTypeIsUUIDClass() {
        boolean useDefault = true;
        assertEquals(Default.class, StdKeySerializers.getStdKeySerializer(config, UUID.class, useDefault).getClass());
    }
    
    @Test
    public void testGetStdKeySerializer_WhenRawKeyTypeIsNotMatched() {
        boolean useDefault = true;
        assertNull(StdKeySerializers.getStdKeySerializer(config, StdKeySerializers.class, useDefault));
    }
    
    @Test
    public void testGetStdKeySerializer_WhenUseDefaultIsFalse() {
        boolean useDefault = false;
        assertNull(StdKeySerializers.getStdKeySerializer(config, null, useDefault));
    }
    
    @Test
    public void testGetFallbackKeySerializer_WhenRawKeyTypeIsNull() {
        assertNull(StdKeySerializers.getFallbackKeySerializer(config, null));
    }
    
    @Test
    public void testGetFallbackKeySerializer_WhenRawKeyTypeIsEnum() {
        assertEquals(Default.class, StdKeySerializers.getFallbackKeySerializer(config, TestEnum.class).getClass());
    }
    
    @Test
    public void testGetFallbackKeySerializer_WhenRawKeyTypeIsNotEnum() {
        assertNull(StdKeySerializers.getFallbackKeySerializer(config, Integer.class));
    }
    
    @Test
    public void testGetDefault() {
        assertEquals(Default.class, StdKeySerializers.getDefault().getClass());
    }
    
    @Test
    public void testDefaultSerialize() throws IOException {
       Default keySerializer = new Default(Default.TYPE_DATE, Date.class);
       Date date = new Date();
       keySerializer.serialize(date, generator, provider);
    }
    
    @Test
    public void testDynamicSerialize() throws IOException {
        StdKeySerializers.Dynamic keySerializer = new StdKeySerializers.Dynamic();
        Object value = "test";
        keySerializer.serialize(value, generator, provider);
    }

    @Test
    public void testStringKeySerializer() throws IOException {
        StdKeySerializers.StringKeySerializer keySerializer = new StdKeySerializers.StringKeySerializer();
        Object value = "test";
        keySerializer.serialize(value, generator, provider);
    }

    private enum TestEnum {
        VALUE1,
        VALUE2
    }
}