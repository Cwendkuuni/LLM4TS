import org.junit.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class FromStringDeserializerTest {

    @Test
    public void testTypes() {
        Class<?>[] expectedTypes = new Class[]{
            File.class,
            URL.class,
            URI.class,
            Class.class,
            JavaType.class,
            Currency.class,
            Pattern.class,
            Locale.class,
            Charset.class,
            TimeZone.class,
            InetAddress.class,
            InetSocketAddress.class,
        };
        assertArrayEquals(expectedTypes, FromStringDeserializer.types());
    }

    @Test
    public void testFindDeserializer() {
        assertNull(FromStringDeserializer.findDeserializer(Integer.class));
        assertSame(FromStringDeserializer.Std.class, FromStringDeserializer.findDeserializer(File.class).getClass());
        assertSame(FromStringDeserializer.Std.class, FromStringDeserializer.findDeserializer(URL.class).getClass());
        assertSame(FromStringDeserializer.Std.class, FromStringDeserializer.findDeserializer(URI.class).getClass());
        assertSame(FromStringDeserializer.Std.class, FromStringDeserializer.findDeserializer(Class.class).getClass());
        assertSame(FromStringDeserializer.Std.class, FromStringDeserializer.findDeserializer(JavaType.class).getClass());
        assertSame(FromStringDeserializer.Std.class, FromStringDeserializer.findDeserializer(Currency.class).getClass());
        assertSame(FromStringDeserializer.Std.class, FromStringDeserializer.findDeserializer(Pattern.class).getClass());
        assertSame(FromStringDeserializer.Std.class, FromStringDeserializer.findDeserializer(Locale.class).getClass());
        assertSame(FromStringDeserializer.Std.class, FromStringDeserializer.findDeserializer(Charset.class).getClass());
        assertSame(FromStringDeserializer.Std.class, FromStringDeserializer.findDeserializer(TimeZone.class).getClass());
        assertSame(FromStringDeserializer.Std.class, FromStringDeserializer.findDeserializer(InetAddress.class).getClass());
        assertSame(FromStringDeserializer.Std.class, FromStringDeserializer.findDeserializer(InetSocketAddress.class).getClass());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserialize() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        FromStringDeserializer<Object> deserializer = new FromStringDeserializer.Std(Object.class, 0);
        
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING).thenReturn(JsonToken.END_ARRAY);
        when(jsonParser.getValueAsString()).thenReturn("testValue");
        when(deserializationContext.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)).thenReturn(true);

        deserializer.deserialize(jsonParser, deserializationContext);

        verify(jsonParser).getCurrentToken();
        verify(jsonParser, times(2)).getValueAsString();
        verify(jsonParser).nextToken();
        verify(jsonParser).wrongTokenException(any(JsonToken.class), any(JsonToken.class), anyString());
        verifyNoMoreInteractions(jsonParser);
        verifyZeroInteractions(deserializationContext);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithException() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        FromStringDeserializer<Object> deserializer = new FromStringDeserializer.Std(Object.class, 0);
        
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getValueAsString()).thenReturn("testValue");
        when(deserializationContext.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)).thenReturn(true);
        doThrow(new IllegalArgumentException()).when(deserializer)._deserialize("testValue", deserializationContext);

        deserializer.deserialize(jsonParser, deserializationContext);

        verify(jsonParser).getCurrentToken();
        verify(jsonParser).getValueAsString();
        verify(jsonParser).getType();
        verify(deserializationContext).weirdStringException("testValue", Object.class, "not a valid textual representation");
        verify(deserializationContext).isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS);
        verifyNoMoreInteractions(jsonParser, deserializationContext);
    }

}