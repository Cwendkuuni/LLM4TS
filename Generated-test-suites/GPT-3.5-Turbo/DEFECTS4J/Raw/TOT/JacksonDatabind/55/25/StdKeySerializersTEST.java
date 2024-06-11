import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class StdKeySerializersTest {

    private StdKeySerializers stdKeySerializers;

    @Before
    public void setUp() {
        stdKeySerializers = new StdKeySerializers();
    }

    @After
    public void tearDown() {
        stdKeySerializers = null;
    }

    @Test
    public void testGetStdKeySerializer_NullRawKeyType_ReturnsDynamicSerializer() {
        assertNull(stdKeySerializers.getStdKeySerializer(null, null, true));
    }

    @Test
    public void testGetStdKeySerializer_ObjectRawKeyType_ReturnsDynamicSerializer() {
        assertNull(stdKeySerializers.getStdKeySerializer(null, Object.class, true));
    }

    @Test
    public void testGetStdKeySerializer_StringRawKeyType_ReturnsDefaultStringSerializer() {
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, stdKeySerializers.getStdKeySerializer(null, String.class, true));
    }

    @Test
    public void testGetStdKeySerializer_PrimitiveRawKeyType_ReturnsDefaultKeySerializer() {
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, stdKeySerializers.getStdKeySerializer(null, int.class, true));
    }

    @Test
    public void testGetStdKeySerializer_NumberRawKeyType_ReturnsDefaultKeySerializer() {
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, stdKeySerializers.getStdKeySerializer(null, Number.class, true));
    }

    @Test
    public void testGetStdKeySerializer_ClassRawKeyType_ReturnsDefaultClassSerializer() {
        assertEquals(new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_CLASS, Class.class), stdKeySerializers.getStdKeySerializer(null, Class.class, true));
    }

    @Test
    public void testGetStdKeySerializer_DateRawKeyType_ReturnsDefaultDateSerializer() {
        assertEquals(new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_DATE, Date.class), stdKeySerializers.getStdKeySerializer(null, Date.class, true));
    }

    @Test
    public void testGetStdKeySerializer_CalendarRawKeyType_ReturnsDefaultCalendarSerializer() {
        assertEquals(new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_CALENDAR, Calendar.class), stdKeySerializers.getStdKeySerializer(null, Calendar.class, true));
    }

    @Test
    public void testGetStdKeySerializer_UUIDRawKeyType_ReturnsDefaultToStringSerializer() {
        assertEquals(new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_TO_STRING, java.util.UUID.class), stdKeySerializers.getStdKeySerializer(null, java.util.UUID.class, true));
    }

    @Test
    public void testGetStdKeySerializer_UseDefaultFalse_ReturnsNull() {
        assertNull(stdKeySerializers.getStdKeySerializer(null, Object.class, false));
    }

    @Test
    public void testGetFallbackKeySerializer_NullRawKeyType_ReturnsDefaultKeySerializer() {
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, stdKeySerializers.getFallbackKeySerializer(null, null));
    }

    @Test
    public void testGetFallbackKeySerializer_EnumRawKeyType_ReturnsDynamicSerializer() {
        assertEquals(new StdKeySerializers.Dynamic(), stdKeySerializers.getFallbackKeySerializer(null, Enum.class));
    }

    @Test
    public void testGetFallbackKeySerializer_ClassRawKeyType_ReturnsDefaultClassSerializer() {
        assertEquals(new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_CLASS, Class.class), stdKeySerializers.getFallbackKeySerializer(null, Class.class));
    }

    @Test
    public void testGetFallbackKeySerializer_EnumSubclassRawKeyType_ReturnsDefaultEnumSerializer() {
        assertEquals(new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_ENUM, EnumSubclass.class), stdKeySerializers.getFallbackKeySerializer(null, EnumSubclass.class));
    }

    @Test
    public void testGetFallbackKeySerializer_UseDefaultFalse_ReturnsDefaultKeySerializer() {
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, stdKeySerializers.getFallbackKeySerializer(null, Object.class));
    }

    // Edge Cases
    @Test
    public void testGetStdKeySerializer_NullUseDefault_ReturnsNull() {
        assertNull(stdKeySerializers.getStdKeySerializer(null, Object.class, null));
    }

    @Test
    public void testGetFallbackKeySerializer_NullRawKeyType_ReturnsDefaultKeySerializer() {
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, stdKeySerializers.getFallbackKeySerializer(null, null));
    }

    // Error Scenarios
    @Test
    public void testGetStdKeySerializer_ExceptionThrown() {
        try {
            stdKeySerializers.getStdKeySerializer(null, Exception.class, true);
            fail("Expected exception to be thrown");
        } catch (Exception e) {
            // Expected
        }
    }

    @Test
    public void testGetFallbackKeySerializer_ExceptionThrown() {
        try {
            stdKeySerializers.getFallbackKeySerializer(null, Exception.class);
            fail("Expected exception to be thrown");
        } catch (Exception e) {
            // Expected
        }
    }

}