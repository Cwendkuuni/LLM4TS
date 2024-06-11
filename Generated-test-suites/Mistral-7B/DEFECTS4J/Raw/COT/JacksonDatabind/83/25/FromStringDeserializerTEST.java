package com.example.junit.fromstringdeserializer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializable;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.deser.std.Std;

@RunWith(MockitoJUnitRunner.class)
public class FromStringDeserializerTest {

 private ObjectMapper objectMapper;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 }

 @Test
 public void testDeserializeFile() throws IOException {
 FromStringDeserializer<File> fileDeserializer = new FromStringDeserializer<File>(File.class);
 TypeDeserializer typeDeserializer = new Std.StdFile();
 TypeSerializable type = (TypeSerializable) typeDeserializer.getTypeDeserializer();
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 ctxt.setTypeDeserializers(new TypeDeserializer[] { typeDeserializer });
 File expectedFile = new File("test.txt");
 String fileString = getClass().getResource("/test.txt").getFile();
 String fileJson = "\"" + fileString + "\"";
 T test = fileDeserializer.deserialize(objectMapper.getFactory().createParser(fileJson), ctxt);
 assertEquals(expectedFile, test);
 }

 @Test
 public void testDeserializeURL() throws IOException {
 FromStringDeserializer<URL> urlDeserializer = new FromStringDeserializer<URL>(URL.class);
 TypeDeserializer typeDeserializer = new Std.StdURL();
 TypeSerializable type = (TypeSerializable) typeDeserializer.getTypeDeserializer();
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 ctxt.setTypeDeserializers(new TypeDeserializer[] { typeDeserializer });
 URL expectedURL = new URL("http://test.com");
 String urlJson = "\"" + expectedURL.toString() + "\"";
 T test = urlDeserializer.deserialize(objectMapper.getFactory().createParser(urlJson), ctxt);
 assertEquals(expectedURL, test);
 }

 @Test
 public void testDeserializeURI() throws IOException {
 FromStringDeserializer<URI> uriDeserializer = new FromStringDeserializer<URI>(URI.class);
 TypeDeserializer typeDeserializer = new Std.StdURI();
 TypeSerializable type = (TypeSerializable) typeDeserializer.getTypeDeserializer();
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 ctxt.setTypeDeserializers(new TypeDeserializer[] { typeDeserializer });
 URI expectedURI = new URI("http://test.com");
 String uriJson = "\"" + expectedURI.toASCIIString() + "\"";
 T test = uriDeserializer.deserialize(objectMapper.getFactory().createParser(uriJson), ctxt);
 assertEquals(expectedURI, test);
 }

 @Test
 public void testDeserializeClass() throws Exception {
 FromStringDeserializer<Class<?>> classDeserializer = new FromStringDeserializer<Class<?>>(Class.class);
 TypeDeserializer typeDeserializer = new Std.StdClass();
 TypeSerializable type = (TypeSerializable) typeDeserializer.getTypeDeserializer();
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 ctxt.setTypeDeserializers(new TypeDeserializer[] { typeDeserializer });
 Class<?> expectedClass = String.class;
 String className = "java.lang.String";
 String classNameJson = "\"" + className + "\"";
 T test = classDeserializer.deserialize(objectMapper.getFactory().createParser(classNameJson), ctxt);
 assertEquals(expectedClass, test.getClass());
 }

 @Test
 public void testDeserializeJavaType() throws IOException {
 FromStringDeserializer<JavaType> javaTypeDeserializer = new FromStringDeserializer<JavaType>(JavaType.class);
 TypeDeserializer typeDeserializer = new Std.StdJavatype();
 TypeSerializable type = (TypeSerializable) typeDeserializer.getTypeDeserializer();
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 ctxt.setTypeDeserializers(new TypeDeserializer[] { typeDeserializer });
 JavaType expectedJavaType = objectMapper.getTypeFactory().constructType(String.class);
 String javaTypeJson = "class java.lang.String";
 T test = javaTypeDeserializer.deserialize(objectMapper.getFactory().createParser(javaTypeJson), ctxt);
 assertEquals(expectedJavaType, test);
 }

 @Test
 public void testDeserializeCurrency() throws IOException {
 FromStringDeserializer<Currency> currencyDeserializer = new FromStringDeserializer<Currency>(Currency.class);
 TypeDeserializer typeDeserializer = new Std.StdCurrency();
 TypeSerializable type = (TypeSerializable) typeDeserializer.getTypeDeserializer();
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 ctxt.setTypeDeserializers(new TypeDeserializer[] { typeDeserializer });
 Currency expectedCurrency = Currency.getInstance("USD");
 String currencyJson = "\"" + expectedCurrency.getCurrencyCode() + "\"";
 T test = currencyDeserializer.deserialize(objectMapper.getFactory().createParser(currencyJson), ctxt);
 assertEquals(expectedCurrency, test);
 }

 @Test
 public void testDeserializePattern() throws IOException {
 FromStringDeserializer<Pattern> patternDeserializer = new FromStringDeserializer<Pattern>(Pattern.class);
 TypeDeserializer typeDeserializer = new Std.StdPattern();
 TypeSerializable type = (TypeSerializable) typeDeserializer.getTypeDeserializer();
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 ctxt.setTypeDeserializers(new TypeDeserializer[] { typeDeserializer });
 Pattern expectedPattern = Pattern.compile("[a-z]+");
 String patternJson = "\"" + Pattern.quote(expectedPattern.toString()) + "\"";
 T test = patternDeserializer.deserialize(objectMapper.getFactory().createParser(patternJson), ctxt);
 assertEquals(expectedPattern, test);
 }

 @Test
 public void testDeserializeLocale() throws IOException {
 FromStringDeserializer<Locale> localeDeserializer = new FromStringDeserializer<Locale>(Locale.class);
 TypeDeserializer typeDeserializer = new Std.StdLocale();
 TypeSerializable type = (TypeSerializable) typeDeserializer.getTypeDeserializer();
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 ctxt.setTypeDeserializers(new TypeDeserializer[] { typeDeserializer });
 Locale expectedLocale = Locale.US;
 String localeJson = "\"" + expectedLocale.toString() + "\"";
 T test = localeDeserializer.deserialize(objectMapper.getFactory().createParser(localeJson), ctxt);
 assertEquals(expectedLocale, test);
 }

 @Test
 public void testDeserializeCharset() throws IOException {
 FromStringDeserializer<Charset> charsetDeserializer = new FromStringDeserializer<Charset>(Charset.class);
 TypeDeserializer typeDeserializer = new Std.StdCharset();
 TypeSerializable type = (TypeSerializable) typeDeserializer.getTypeDeserializer();
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 ctxt.setTypeDeserializers(new TypeDeserializer[] { typeDeserializer });
 Charset expectedCharset = StandardCharsets.UTF_8;
 String charsetJson = "\"" + expectedCharset.name() + "\"";
 T test = charsetDeserializer.deserialize(objectMapper.getFactory().createParser(charsetJson), ctxt);
 assertEquals(expectedCharset, test);
 }

 @Test
 public void testDeserializeTimeZone() throws IOException {
 FromStringDeserializer<TimeZone> timeZoneDeserializer = new FromStringDeserializer<TimeZone>(TimeZone.class);
 TypeDeserializer typeDeserializer = new Std.StdTimeZone();
 TypeSerializable type = (TypeSerializable) typeDeserializer.getTypeDeserializer();
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 ctxt.setTypeDeserializers(new TypeDeserializer[] { typeDeserializer });
 TimeZone expectedTimeZone = TimeZone.getTimeZone("UTC");
 String timeZoneJson = "\"" + expectedTimeZone.getID() + "\"";
 T test = timeZoneDeserializer.deserialize(objectMapper.getFactory().createParser(timeZoneJson), ctxt);
 assertEquals(expectedTimeZone, test);
 }

 @Test
 public void testDeserializeInetAddress() throws IOException {
 FromStringDeserializer<InetAddress> inetAddressDeserializer = new FromStringDeserializer<InetAddress>(InetAddress.class);
 TypeDeserializer typeDeserializer = new Std.StdInetAddress();
 TypeSerializable type = (TypeSerializable) typeDeserializer.getTypeDeserializer();
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 ctxt.setTypeDeserializers(new TypeDeserializer[] { typeDeserializer });
 InetAddress expectedInetAddress = InetAddress.getByName("127.0.0.1");
 String inetAddressJson = "\"" + expectedInetAddress.getHostAddress() + "\"";
 T test = inetAddressDeserializer.deserialize(objectMapper.getFactory().createParser(inetAddressJson), ctxt);
 assertEquals(expectedInetAddress, test);
 }

 @Test
 public void testDeserializeInetSocketAddress() throws IOException {
 FromStringDeserializer<InetSocketAddress> inetSocketAddressDeserializer = new FromStringDeserializer<InetSocketAddress>(InetSocketAddress.class);
 TypeDeserializer typeDeserializer = new Std.StdInetSocketAddress();
 TypeSerializable type = (TypeSerializable) typeDeserializer.getTypeDeserializer();
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 ctxt.setTypeDeserializers(new TypeDeserializer[] { typeDeserializer });
 InetSocketAddress expectedInetSocketAddress = new InetSocketAddress("127.0.0.1", 80);
 String inetSocketAddressJson = "\"" + expectedInetSocketAddress.toString() + "\"";
 T test = inetSocketAddressDeserializer.deserialize(objectMapper.getFactory().createParser(inetSocketAddressJson), ctxt);
 assertEquals(expectedInetSocketAddress, test);
 }

 @Test
 public void testDeserializeStringBuilder() throws IOException {
 FromStringDeserializer<StringBuilder> stringBuilderDeserializer = new FromStringDeserializer<StringBuilder>(StringBuilder.class);
 TypeDeserializer typeDeserializer = new Std.StdStringBuilder();
 TypeSerializable type = (TypeSerializable) typeDeserializer.getTypeDeserializer();
 DeserializationContext ctxt = objectMapper.getDeserializationContext();
 ctxt.setTypeDeserializers(new TypeDeserializer[] { typeDeserializer });
 StringBuilder expectedStringBuilder = new StringBuilder();
 String stringBuilderJson = "\"" + expectedStringBuilder.toString() + "\"";
 T test = stringBuilderDeserializer.deserialize(objectMapper.getFactory().createParser(stringBuilderJson), ctxt);
 assertEquals(expectedStringBuilder, test);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeNull() throws IOException {
 FromStringDeserializer<String> nullDeserializer = new FromStringDeserializer<String>(String.class);
 String nullJson = "null";
 T test = nullDeserializer.deserialize(objectMapper.getFactory().createParser(nullJson), null);
 assertNull(test);
 }

 @Test(expected = InvalidFormatException.class)
 public void testDeserializeInvalidStringFormat() throws IOException, JsonProcessingException {
 FromStringDeserializer<Integer> invalidFormatDeserializer = new FromStringDeserializer<Integer>(Integer.class);
 String invalidFormatJson = "invalid";
 T test = invalidFormatDeserializer.deserialize(objectMapper.getFactory().createParser(invalidFormatJson), null);
 // This test will throw an InvalidFormatException, which is expected
 }

 @Test
 public void testDeserializeEmptyString() throws IOException {
 FromStringDeserializer<String> emptyStringDeserializer = new FromStringDeserializer<String>(String.class);
 String emptyStringJson = "\"\"";
 T test = emptyStringDeserializer.deserialize(objectMapper.getFactory().createParser(emptyStringJson), null);
 assertNull(test);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidType() throws IOException {
 FromStringDeserializer<Integer> integerDeserializer = new FromStringDeserializer<Integer>(Integer.class);
 String invalidTypeJson = "\"" + Boolean.FALSE.toString() + "\"";
 T test = integerDeserializer.deserialize(objectMapper.getFactory().createParser(invalidTypeJson), null);
 // This test will throw a JsonMappingException, which is expected
 }

}