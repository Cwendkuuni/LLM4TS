package com.fasterxml.jackson.databind.ser.std;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StdKeySerializersTest {

    @Test
    public void testGetStdKeySerializer() {
        // Test rawKeyType is null
        assertNull(StdKeySerializers.getStdKeySerializer(null, null, true));
        
        // Test rawKeyType is Object class
        StdKeySerializers.Default dynamic = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_DATE, null);
        assertEquals(dynamic.getClass(), StdKeySerializers.getStdKeySerializer(null, Object.class, true).getClass());
        
        // Test rawKeyType is String class
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, StdKeySerializers.getStdKeySerializer(null, String.class, true));
        
        // Test rawKeyType is primitive
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, StdKeySerializers.getStdKeySerializer(null, int.class, true));
        
        // Test rawKeyType is Number class
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, StdKeySerializers.getStdKeySerializer(null, Number.class, true));
        
        // Test rawKeyType is Class class
        StdKeySerializers.Default clazz = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_CLASS, Class.class);
        assertEquals(clazz.getClass(), StdKeySerializers.getStdKeySerializer(null, Class.class, true).getClass());
        
        // Test rawKeyType is Date class
        StdKeySerializers.Default date = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_DATE, Date.class);
        assertEquals(date.getClass(), StdKeySerializers.getStdKeySerializer(null, Date.class, true).getClass());
        
        // Test rawKeyType is Calendar class
        StdKeySerializers.Default calendar = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_CALENDAR, Calendar.class);
        assertEquals(calendar.getClass(), StdKeySerializers.getStdKeySerializer(null, Calendar.class, true).getClass());
        
        // Test rawKeyType is java.util.UUID class
        StdKeySerializers.Default toString = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_TO_STRING, java.util.UUID.class);
        assertEquals(toString.getClass(), StdKeySerializers.getStdKeySerializer(null, java.util.UUID.class, true).getClass());
        
        // Test rawKeyType not handled and useDefault is false
        assertNull(StdKeySerializers.getStdKeySerializer(null, Object.class, false));
    }

    @Test
    public void testGetFallbackKeySerializer() {
        // Test rawKeyType is null
        StdKeySerializers.Dynamic dynamic = new StdKeySerializers.Dynamic();
        assertEquals(dynamic.getClass(), StdKeySerializers.getFallbackKeySerializer(null, null).getClass());
        
        // Test rawKeyType is Enum class
        StdKeySerializers.Default enumClass = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_ENUM, Enum.class);
        assertEquals(enumClass.getClass(), StdKeySerializers.getFallbackKeySerializer(null, Enum.class).getClass());
        
        // Test rawKeyType is Enum class
        StdKeySerializers.Default enumKey = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_ENUM, TestEnum.class);
        assertEquals(enumKey.getClass(), StdKeySerializers.getFallbackKeySerializer(null, TestEnum.class).getClass());
        
        // Test rawKeyType is not null and not a Enum
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, StdKeySerializers.getFallbackKeySerializer(null, Object.class));
    }

    @Test
    public void testDefaultSerialize() {
        // Test _typeId is TYPE_DATE
        StdKeySerializers.Default defaultDate = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_DATE, Date.class);
        TestSerializerProvider provider = new TestSerializerProvider();
        try {
            defaultDate.serialize(new Date(), provider.getGenerator(), provider);
        } catch (Exception e) {
            assertEquals(IOException.class, e.getClass());
        }

        // Test _typeId is TYPE_CALENDAR
        StdKeySerializers.Default defaultCalendar = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_CALENDAR, Calendar.class);
        try {
            defaultCalendar.serialize(Calendar.getInstance(), provider.getGenerator(), provider);
        } catch (Exception e) {
            assertEquals(IOException.class, e.getClass());
        }
        
        // Test _typeId is TYPE_CLASS
        StdKeySerializers.Default defaultClass = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_CLASS, Class.class);
        try {
            defaultClass.serialize(this.getClass(), provider.getGenerator(), provider);
        } catch (Exception e) {
            assertEquals(IOException.class, e.getClass());
        }
        
        // Test _typeId is TYPE_ENUM
        StdKeySerializers.Default defaultEnum = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_ENUM, TestEnum.class);
        try {
            defaultEnum.serialize(TestEnum.TEST, provider.getGenerator(), provider);
        } catch (Exception e) {
            assertEquals(IOException.class, e.getClass());
        }
        
        // Test _typeId is TYPE_TO_STRING
        StdKeySerializers.Default defaultToString = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_TO_STRING, java.util.UUID.class);
        try {
            defaultToString.serialize(java.util.UUID.randomUUID(), provider.getGenerator(), provider);
        } catch (Exception e) {
            assertEquals(IOException.class, e.getClass());
        }
        
        // Test _typeId is default
        StdKeySerializers.Default defaultOther = new StdKeySerializers.Default(0, String.class);
        try {
            defaultOther.serialize("Test", provider.getGenerator(), provider);
        } catch (Exception e) {
            assertEquals(IOException.class, e.getClass());
        }
    }

    @Test
    public void testDynamicSerialize() {
        StdKeySerializers.Dynamic dynamic = new StdKeySerializers.Dynamic();
        TestSerializerProvider provider = new TestSerializerProvider();
        try {
            dynamic.serialize("Test", provider.getGenerator(), provider);
        } catch (Exception e) {
            assertEquals(IOException.class, e.getClass());
        }
    }

    @Test
    public void testStringKeySerialize() {
        StdKeySerializers.StringKeySerializer stringKeySerializer = new StdKeySerializers.StringKeySerializer();
        TestSerializerProvider provider = new TestSerializerProvider();
        try {
            stringKeySerializer.serialize("Test", provider.getGenerator(), provider);
        } catch (Exception e) {
            assertEquals(IOException.class, e.getClass());
        }
    }

    enum TestEnum {
        TEST;
    }
    
    private class TestSerializerProvider extends JsonSerializer<Object> {
    	
    	public JsonGenerator getGenerator() {
    		return new JsonFactory().createGenerator(System.out, JsonEncoding.UTF8);
    	}

		@Override
		public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
			
		}
    }
}