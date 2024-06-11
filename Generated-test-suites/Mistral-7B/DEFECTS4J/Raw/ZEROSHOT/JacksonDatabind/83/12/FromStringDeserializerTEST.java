package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URI;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer.Std;
import com.fasterxml.jackson.databind.deser.std.Std.StdFile;
import com.fasterxml.jackson.databind.deser.std.Std.StdLocale;
import com.fasterxml.jackson.databind.deser.std.Std.StdTimeZone;

public class FromStringDeserializerTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @SuppressWarnings("unchecked")
 @Test
 public void testFindDeserializer() throws Exception {
 Std<File> fileDeserializer = (Std<File>) FromStringDeserializer.findDeserializer(File.class);
 assertEquals(StdFile.class, fileDeserializer.getClass());

 Std<URL> urlDeserializer = (Std<URL>) FromStringDeserializer.findDeserializer(URL.class);
 assertEquals(Std.STD_URL, urlDeserializer._kind);

 Std<URI> uriDeserializer = (Std<URI>) FromStringDeserializer.findDeserializer(URI.class);
 assertEquals(Std.STD_URI, uriDeserializer._kind);

 Std<StringBuilder> stringBuilderDeserializer = (Std<StringBuilder>) FromStringDeserializer.findDeserializer(StringBuilder.class);
 assertEquals(Std.STD_STRING_BUILDER, stringBuilderDeserializer._kind);

 Std<Locale> localeDeserializer = (Std<Locale>) FromStringDeserializer.findDeserializer(Locale.class);
 assertEquals(StdLocale.class, localeDeserializer.getClass());

 Std<TimeZone> timeZoneDeserializer = (Std<TimeZone>) FromStringDeserializer.findDeserializer(TimeZone.class);
 assertEquals(StdTimeZone.class, timeZoneDeserializer.getClass());

 Std<Currency> currencyDeserializer = (Std<Currency>) FromStringDeserializer.findDeserializer(Currency.class);
 assertEquals(Std.STD_CURRENCY, currencyDeserializer._kind);
 }

 @SuppressWarnings("result-of-method-call-ignored")
 @Test
 public void testDeserialize() throws Exception {
 JsonParser parser = MAPPER.getFactory().createParser("\"file.txt\"");
 Std<File> fileDeserializer = (Std<File>) FromStringDeserializer.findDeserializer(File.class);
 File result = fileDeserializer.deserialize(parser, MAPPER.getDeserializationContext());
 assertEquals("file.txt", result.getName());

 parser = MAPPER.getFactory().createParser("\"http://example.com/\"");
 Std<URL> urlDeserializer = (Std<URL>) FromStringDeserializer.findDeserializer(URL.class);
 URL resultUrl = urlDeserializer.deserialize(parser, MAPPER.getDeserializationContext());
 assertEquals("http://example.com/", resultUrl.toString());

 parser = MAPPER.getFactory().createParser("\"URI/foo/bar\"");
 Std<URI> uriDeserializer = (Std<URI>) FromStringDeserializer.findDeserializer(URI.class);
 URI resultUri = uriDeserializer.deserialize(parser, MAPPER.getDeserializationContext());
 assertEquals("URI/foo/bar", resultUri.toURI().toString());

 parser = MAPPER.getFactory().createParser("\"java.lang.StringBuilder\"");
 Std<StringBuilder> stringBuilderDeserializer = (Std<StringBuilder>) FromStringDeserializer.findDeserializer(StringBuilder.class);
 StringBuilder resultStringBuilder = stringBuilderDeserializer.deserialize(parser, MAPPER.getDeserializationContext());
 assertNull(resultStringBuilder.length());

 parser = MAPPER.getFactory().createParser("\"en_US\"");
 Std<Locale> localeDeserializer = (Std<Locale>) FromStringDeserializer.findDeserializer(Locale.class);
 Locale resultLocale = localeDeserializer.deserialize(parser, MAPPER.getDeserializationContext());
 assertEquals("en_US", resultLocale.toString());

 parser = MAPper.getFactory().createParser("\"America/New_York\"");
 Std<TimeZone> timeZoneDeserializer = (Std<TimeZone>) FromStringDeserializer.findDeserializer(TimeZone.class);
 TimeZone resultTimeZone = timeZoneDeserializer.deserialize(parser, MAPper.getDeserializationContext());
 assertEquals("America/New_York", resultTimeZone.getID());

 parser = MAPper.getFactory().createParser("\"USD\"");
 Std<Currency> currencyDeserializer = (Std<Currency>) FromStringDeserializer.findDeserializer(Currency.class);
 Currency resultCurrency = currencyDeserializer.deserialize(parser, MAPper.getDeserializationContext());
 assertEquals("USD", resultCurrency.getCurrencyCode());

 // Test empty string
 parser = MAPper.getFactory().createParser("\"\"");
 for (Std<?> deserializer : new Std<?>[] { fileDeserializer, urlDeserializer, uriDeserializer,
 stringBuilderDeserializer, localeDeserializer, timeZoneDeserializer, currencyDeserializer }) {
 Object result = deserializer.deserialize(parser, MAPPER.getDeserializationContext());
 assertNull(result);
 }
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testDeserializeFromEmptyString() throws Exception {
 Std<StringBuilder> stringBuilderDeserializer = (Std<StringBuilder>) FromStringDeserializer.findDeserializer(StringBuilder.class);
 assertEquals(StringBuilder.class, stringBuilderDeserializer.getValueClass());
 assertNull(stringBuilderDeserializer._deserializeFromEmptyString());

 Std<Locale> localeDeserializer = (Std<Locale>) FromStringDeserializer.findDeserializer(Locale.class);
 assertEquals(Locale.class, localeDeserializer.getValueClass());
 assertEquals(Locale.ROOT, localeDeserializer._deserializeFromEmptyString());

 Std<TimeZone> timeZoneDeserializer = (Std<TimeZone>) FromStringDeserializer.findDeserializer(TimeZone.class);
 assertEquals(TimeZone.class, timeZoneDeserializer.getValueClass());
 assertEquals(TimeZone.getDefault(), timeZoneDeserializer._deserializeFromEmptyString());
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testDeserializeFromArray() throws Exception {
 JsonParser parser = MAPPER.getFactory().createParser("[\"File1.txt\", \"File2.txt\"]");
 Std<File> fileDeserializer = (Std<File>) FromStringDeserializer.findDeserializer(File.class);
 File[] results = (File[]) fileDeserializer.deserialize(parser, MAPPER.getDeserializationContext());
 assertEquals(2, results.length);
 assertEquals("File1.txt", results[0].getName());
 assertEquals("File2.txt", results[1].getName());
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testDeserializeEmbeddedObject() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 Std<Locale> localeDeserializer = (Std<Locale>) FromStringDeserializer.findDeserializer(Locale.class);

 JsonFactory factory = mapper.getFactory();
 JsonParser parser = factory.createParser("{\"locale\": \"en_US\"}");
 Object obj = parser.readValueAsTree();

 Object result = localeDeserializer._deserializeEmbedded(obj, mapper.getDeserializationContext());
 assertEquals(Locale.ENGLISH, result);

 parser = factory.createParser("{\"locale\": \"en_US\", \"foo\": 1}");
 result = localeDeserializer._deserializeEmbedded(obj, mapper.getDeserializationContext());
 assertNull(result);
 }
}