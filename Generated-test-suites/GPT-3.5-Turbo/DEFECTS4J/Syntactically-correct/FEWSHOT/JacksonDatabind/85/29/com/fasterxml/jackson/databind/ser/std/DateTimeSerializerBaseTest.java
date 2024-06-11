package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

public class DateTimeSerializerBaseTest {

    @Test
    public void testConstructor() {
        DateTimeSerializerBase<DateTime> serializer = new DateTimeSerializerBase<DateTime>(DateTime.class,
                false, null) {
            
            @Override
    		protected long _timestamp(DateTime value) {
    			return 0;
    		}

    		@Override
    		public void serialize(DateTime value, JsonGenerator gen, SerializerProvider serializers)
    				throws IOException {
    			
    		}
    	
            @Override
            public DateTimeSerializerBase<DateTime> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }
        };
        
        assertEquals(DateTime.class, serializer.handledType());

        assertFalse(serializer.isEmpty(null));
        assertTrue(serializer.isEmpty(mock(SerializerProvider.class), null));

        assertNull(serializer.getSchema(mock(SerializerProvider.class), null));

        assertNull(serializer.createSchemaNode("string", true));

        JsonFormatVisitorWrapper visitorWrapper = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);

        serializer.acceptJsonFormatVisitor(visitorWrapper, typeHint);

        verify(visitorWrapper).getProvider();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAsTimestampWithNullSerializerProvider() {
        DateTimeSerializerBase<DateTime> serializer = new DateTimeSerializerBase<DateTime>(DateTime.class,
                null, null) {
            
            @Override
    		protected long _timestamp(DateTime value) {
    			return 0;
    		}

    		@Override
    		public void serialize(DateTime value, JsonGenerator gen, SerializerProvider serializers)
    				throws IOException {
    			
    		}
    	
            @Override
            public DateTimeSerializerBase<DateTime> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }
        };
        
        serializer._asTimestamp(null);
    }

    @Test
    public void testAsTimestampWithUseTimestampFalse() {
        DateTimeSerializerBase<DateTime> serializer = new DateTimeSerializerBase<DateTime>(DateTime.class,
                false, null) {
            
            @Override
    		protected long _timestamp(DateTime value) {
    			return 0;
    		}

    		@Override
    		public void serialize(DateTime value, JsonGenerator gen, SerializerProvider serializers)
    				throws IOException {
    			
    		}
    	
            @Override
            public DateTimeSerializerBase<DateTime> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }
        };
        
        SerializerProvider serializers = mock(SerializerProvider.class);
        assertFalse(serializer._asTimestamp(serializers));
    }

    @Test
    public void testAsTimestampWithUseTimestampTrue() {
        DateTimeSerializerBase<DateTime> serializer = new DateTimeSerializerBase<DateTime>(DateTime.class,
                true, null) {
            
            @Override
    		protected long _timestamp(DateTime value) {
    			return 0;
    		}

    		@Override
    		public void serialize(DateTime value, JsonGenerator gen, SerializerProvider serializers)
    				throws IOException {
    			
    		}
    	
            @Override
            public DateTimeSerializerBase<DateTime> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }
        };
        
        SerializerProvider serializers = mock(SerializerProvider.class);
        assertTrue(serializer._asTimestamp(serializers));
    }

    @Test
    public void testAsTimestampWithCustomFormatNull() {
        DateTimeSerializerBase<DateTime> serializer = new DateTimeSerializerBase<DateTime>(DateTime.class,
                null, null) {
            
            @Override
    		protected long _timestamp(DateTime value) {
    			return 0;
    		}

    		@Override
    		public void serialize(DateTime value, JsonGenerator gen, SerializerProvider serializers)
    				throws IOException {
    			
    		}
    	
            @Override
            public DateTimeSerializerBase<DateTime> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }
        };
        
        SerializerProvider serializers = mock(SerializerProvider.class);
        assertFalse(serializer._asTimestamp(serializers));
    }

    @Test
    public void testAsTimestampWithCustomFormatNotNull() {
        DateTimeSerializerBase<DateTime> serializer = new DateTimeSerializerBase<DateTime>(DateTime.class,
                null, new SimpleDateFormat()) {
            
            @Override
    		protected long _timestamp(DateTime value) {
    			return 0;
    		}

    		@Override
    		public void serialize(DateTime value, JsonGenerator gen, SerializerProvider serializers)
    				throws IOException {
    			
    		}
    	
            @Override
            public DateTimeSerializerBase<DateTime> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }
        };
        
        SerializerProvider serializers = mock(SerializerProvider.class);
        assertFalse(serializer._asTimestamp(serializers));
    }
}