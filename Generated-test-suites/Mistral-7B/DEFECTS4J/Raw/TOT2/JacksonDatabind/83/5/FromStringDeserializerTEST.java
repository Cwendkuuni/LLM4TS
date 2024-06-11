package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;

@RunWith(MockitoJUnitRunner.class)
public class FromStringDeserializerTest {

private static final String VALID_STRING_REPRESENTATION = "valid string representation";

@InjectMocks
private FromStringDeserializer<Object> fromStringDeserializer;

@Test
public void testDeserializeString() throws IOException, JsonProcessingException {
ObjectCodec objectMapper = Mockito.mock(ObjectCodec.class);
DeserializationContext context = Mockito.mock(DeserializationContext.class);
JsonParser parser = Mockito.mock(JsonParser.class);

Mockito.when(parser.getValueAsString()).thenReturn(VALID_STRING_REPRESENTATION);

assertEquals(VALID_STRING_REPRESENTATION, fromStringDeserializer.deserialize(parser, context));
}

@Test
public void testDeserializeEmptyString() throws IOException {
ObjectCodec objectMapper = Mockito.mock(ObjectCodec.class);
DeserializationContext context = Mockito.mock(DeserializationContext.class);
JsonParser parser = Mockito.mock(JsonParser.class);

Mockito.when(parser.getValueAsString()).thenReturn("");

assertNull(fromStringDeserializer.deserialize(parser, context));
}

@Test
public void testDeserializeNullString() throws IOException {
ObjectCodec objectMapper = Mockito.mock(ObjectCodec.class);
DeserializationContext context = Mockito.mock(DeserializationContext.class);
JsonParser parser = Mockito.mock(JsonParser.class);

Mockito.when(parser.getValueAsString()).thenReturn(null);

assertNull(fromStringDeserializer.deserialize(parser, context));
}

@Test
public void testDeserializeArray() throws IOException {
ObjectCodec objectMapper = Mockito.mock(ObjectCodec.class);
DeserializationContext context = Mockito.mock(DeserializationContext.class);
JsonParser parser = Mockito.mock(JsonParser.class);

Mockito.when(parser.getCurrentToken()).thenReturn(JsonToken.START_ARRAY);

assertThrows(IllegalArgumentException.class, () -> fromStringDeserializer.deserialize(parser, context));
}

@Test
public void testDeserializeEmbeddedObject() throws IOException {
ObjectCodec objectMapper = Mockito.mock(ObjectCodec.class);
DeserializationContext context = Mockito.mock(DeserializationContext.class);
Object obj = new Object();
JsonParser parser = Mockito.mock(JsonParser.class);

Mockito.when(parser.getEmbeddedObject()).thenReturn(obj);

assertEquals(obj, fromStringDeserializer.deserialize(parser, context));
}

@Test
public void testDeserializeStringWithMalformedURL() throws IOException {
ObjectCodec objectMapper = Mockito.mock(ObjectCodec.class);
DeserializationContext context = Mockito.mock(DeserializationContext.class);
JsonParser parser = Mockito.mock(JsonParser.class);

Mockito.when(parser.getValueAsString()).thenReturn("malformed:/URL");

assertThrows(MalformedURLException.class, () -> fromStringDeserializer.deserialize(parser, context));
}

@Test
public void testDeserializeCurrencyWithInvalidFormat() throws IOException {
ObjectCodec objectMapper = Mockito.mock(ObjectCodec.class);
DeserializationContext context = Mockito.mock(DeserializationContext.class);
JsonParser parser = Mockito.mock(JsonParser.class);

Mockito.when(parser.getValueAsString()).thenReturn("invalid_currency_format");

assertThrows(InvalidFormatException.class, () -> fromStringDeserializer.deserialize(parser, context));
}

@Test
public void testDeserializeLocaleWithInvalidFormat() throws IOException {
ObjectCodec objectMapper = Mockito.mock(ObjectCodec.class);
DeserializationContext context = Mockito.mock(DeserializationContext.class);
JsonParser parser = Mockito.mock(JsonParser.class);

Mockito.when(parser.getValueAsString()).thenReturn("invalid_locale_format");

assertThrows(JsonMappingException.class, () -> fromStringDeserializer.deserialize(parser, context));
}

@Test
public void testDeserializeTimeZoneWithInvalidFormat() throws IOException {
ObjectCodec objectMapper = Mockito.mock(ObjectCodec.class);
DeserializationContext context = Mockito.mock(DeserializationContext.class);
JsonParser parser = Mockito.mock(JsonParser.class);

Mockito.when(parser.getValueAsString()).thenReturn("invalid_timezone_format");

assertThrows(JsonMappingException.class, () -> fromStringDeserializer.deserialize(parser, context));
}

@Test
public void testDeserializeInetAddressWithIpv4Address() throws IOException {
ObjectCodec objectMapper = Mockito.mock(ObjectCodec.class);
DeserializationContext context = Mockito.mock(DeserializationContext.class);
JsonParser parser = Mockito.mock(JsonParser.class);

Mockito.when(parser.getValueAsString()).thenReturn("192.168.0.1");

assertEquals(Mockito.mock(InetAddress.class), fromStringDeserializer.deserialize(parser, context));
}

@Test
public void testDeserializeInetAddressWithIpv6Address() throws IOException {
ObjectCodec objectMapper = Mockito.mock(ObjectCodec.class);
DeserializationContext context = Mockito.mock(DeserializationContext.class);
JsonParser parser = Mockito.mock(JsonParser.class);

Mockito.when(parser.getValueAsString()).thenReturn("[2001:0db8:85a3:0000:0000:8a2e:0370:7334]");

assertEquals(Mockito.mock(InetSocketAddress.class), fromStringDeserializer.deserialize(parser, context));
}

@Test
public void testDeserializeInetAddressWithInvalidFormat() throws IOException {
ObjectCodec objectMapper = Mockito.mock(ObjectCodec.class);
DeserializationContext context = Mockito.mock(DeserializationContext.class);
JsonParser parser = Mockito.mock(JsonParser.class);

Mockito.when(parser.getValueAsString()).thenReturn("invalid_inet_address_format");

assertThrows(JsonMappingException.class, () -> fromStringDeserializer.deserialize(parser, context));
}

@Test
public void testDeserializeInetSocketAddressWithIpv4AddressAndPort() throws IOException {
ObjectCodec objectMapper = Mockito.mock(ObjectCodec.class);
DeserializationContext context = Mockito.mock(DeserializationContext.class);
JsonParser parser = Mockito.mock(JsonParser.class);

Mockito.when(parser.getValueAsString()).thenReturn("192.168.0.1:8080");

assertEquals(Mockito.mock(InetSocketAddress.class), fromStringDeserializer.deserialize(parser, context));
}

@Test
public void testDeserializeInetSocketAddressWithIpv6AddressAndPort() throws IOException {
ObjectCodec objectMapper = Mockito.mock(ObjectCodec.class);
DeserializationContext context = Mockito.mock(DeserializationContext.class);
JsonParser parser = Mockito.mock(JsonParser.class);

Mockito.when(parser.getValueAsString()).thenReturn("[2001:0db8:85a3:0000:0000:8a2e:0370:7334]:8080");

assertEquals(Mockito.mock(InetSocketAddress.class), fromStringDeserializer.deserialize(parser, context));
}

@Test
public void testDeserializeInetSocketAddressWithIpv6AddressOnly() throws IOException {
ObjectCodec objectMapper = Mockito.mock(ObjectCodec.class);
DeserializationContext context = Mockito.mock(DeserializationContext.class);
JsonParser parser = Mockito.mock(JsonParser.class);

Mockito.when(parser.getValueAsString()).thenReturn("[2001:0db8:85a3:0000:0000:8a2e:0370:7334]");

assertEquals(Mockito.mock(InetSocketAddress.class), fromStringDeserializer.deserialize(parser, context));
}

@Test
public void testDeserializeInetSocketAddressWithInvalidFormat() throws IOException {
ObjectCodec objectMapper = Mockito.mock(ObjectCodec.class);
DeserializationContext context = Mockito.mock(DeserializationContext.class);
JsonParser parser = Mockito.mock(JsonParser.class);

Mockito.when(parser.getValueAsString()).thenReturn("invalid_inet_socket_address_format");

assertThrows(JsonMappingException.class, () -> fromStringDeserializer.deserialize(parser, context));
}

@Test
public void testDeserializeStringBuilder() throws IOException {
ObjectCodec objectMapper = Mockito.mock(ObjectCodec.class);
DeserializationContext context = Mockito.mock(DeserializationContext.class);
JsonParser parser = Mockito.mock(JsonParser.class);

Mockito.when(parser.getValueAsString()).thenReturn("new StringBuilder()");

assertEquals(new StringBuilder(), fromStringDeserializer.deserialize(parser, context));
}
}