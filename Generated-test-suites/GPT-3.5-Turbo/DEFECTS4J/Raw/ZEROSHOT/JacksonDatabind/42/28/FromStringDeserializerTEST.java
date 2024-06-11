package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
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
    public void testDeserialize() {
        FromStringDeserializer<Object> deserializer = new FromStringDeserializer.Std(Object.class, 1);
        assertNull(deserializer._deserialize("", null));
    }

    @Test
    public void testDeserializeFromEmptyString() throws IOException {
        FromStringDeserializer<Object> deserializer = new FromStringDeserializer.Std(Object.class, 1);
        assertNull(deserializer._deserializeFromEmptyString());
    }

    @Test
    public void testDeserializeWithEmptyString() throws IOException {
        FromStringDeserializer<Object> deserializer = new FromStringDeserializer.Std(Object.class, 1);
        assertNull(deserializer._deserialize("", null));
    }
}