package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.AnnotationIntrospector.FindConstructor;
import com.fasterxml.jackson.databind.AnnotationIntrospector.FindMethod;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer.NullValueProvider;
import com.fasterxml.jackson.databind.deser.impl.FromStringDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.type.ClassKey;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

 private static final String DEFAULT_KEY = "defaultKey";
 private static final String BOOLEAN_TRUE_KEY = "true";
 private static final String BOOLEAN_FALSE_KEY = "false";
 private static final String BYTE_KEY = "50";
 private static final String SHORT_KEY = "100";
 private static final String CHAR_KEY = "A";
 private static final String INT_KEY = "1000";
 private static final String LONG_KEY = "1000000";
 private static final String FLOAT_KEY = "1.23f";
 private static final String DOUBLE_KEY = "1.23";
 private static final String URI_KEY = "http://example.com";
 private static final String URL_KEY = "http://example.com/resource";
 private static final String CLASS_KEY = "java.lang.String";
 private static final String ENUM_KEY = "EnumConstant";
 private static final String ENUM_UNSUPPORTED_KEY = "UnsupportedEnum";
 private static final String LOCALE_KEY = "en_US";
 private static final String CURRENCY_KEY = "USD";
 private static final String DATE_KEY = "2022-01-01";
 private static final String CALENDAR_KEY = "2022-01-01";
 private static final String UUID_KEY = "123e4567-e89b-12d3-a456-426614174000";

 private DeserializationContext context;
 private StdKeyDeserializer stringKD;
 private StdKeyDeserializer stringObjectKD;
 private StdKeyDeserializer booleanKD;
 private StdKeyDeserializer byteKD;
 private StdKeyDeserializer shortKD;
 private StdKeyDeserializer charKD;
 private StdKeyDeserializer intKD;
 private StdKeyDeserializer longKD;
 private StdKeyDeserializer floatKD;
 private StdKeyDeserializer doubleKD;
 private StdKeyDeserializer uriKD;
 private StdKeyDeserializer urlKD;
 private StdKeyDeserializer classKD;
 private StdKeyDeserializer enumKD;
 private StdKeyDeserializer enumUnsupportedKD;
 private StdKeyDeserializer localeKD;
 private StdKeyDeserializer currencyKD;
 private StdKeyDeserializer dateKD;
 private StdKeyDeserializer calendarKD;
 private StdKeyDeserializer uuidKD;

 @Before
 public void setup() throws Exception {
 context = new DeserializationContext(new AnnotationIntrospector() {}, new JsonFactory(), new StandardSerializerFinder());
 stringKD = StdKeyDeserializer.forType(String.class);
 stringObjectKD = StdKeyDeserializer.forType(Object.class);
 booleanKD = StdKeyDeserializer.forType(Boolean.class);
 byteKD = StdKeyDeserializer.forType(Byte.class);
 shortKD = StdKeyDeserializer.forType(Short.class);
 charKD = StdKeyDeserializer.forType(Character.class);
 intKD = StdKeyDeserializer.forType(Integer.class);
 longKD = StdKeyDeserializer.forType(Long.class);
 floatKD = StdKeyDeserializer.forType(Float.class);
 doubleKD = StdKeyDeserializer.forType(Double.class);
 uriKD = StdKeyDeserializer.forType(URI.class);
 urlKD = StdKeyDeserializer.forType(URL.class);
 classKD = StdKeyDeserializer.forType(Class.class);
 enumKD = StdKeyDeserializer.forType(Enum.class);
 enumUnsupportedKD = StdKeyDeserializer.forType(UnsupportedEnum.class);
 localeKD = StdKeyDeserializer.forType(Locale.class);
 currencyKD = StdKeyDeserializer.forType(Currency.class);
 dateKD = StdKeyDeserializer.forType(Date.class);
 calendarKD = StdKeyDeserializer.forType(Calendar.class);
 uuidKD = StdKeyDeserializer.forType(UUID.class);
 }

 @Test
 public void testStringKD() throws Exception {
 assertEquals(DEFAULT_KEY, stringKD.deserializeKey(DEFAULT_KEY, context));
 assertEquals(DEFAULT_KEY, context.handleUnknownKey(String.class, DEFAULT_KEY));
 }

 @Test
 public void testStringObjectKD() throws Exception {
 assertEquals(DEFAULT_KEY, stringObjectKD.deserializeKey(DEFAULT_KEY, context));
 assertEquals(DEFAULT_KEY, context.handleUnknownKey(Object.class, DEFAULT_KEY));
 }

 @Test
 public void testBooleanTrueKD() throws Exception {
 assertTrue((Boolean) booleanKD.deserializeKey(BOOLEAN_TRUE_KEY, context));
 }

 @Test
 public void testBooleanFalseKD() throws Exception {
 assertFalse((Boolean) booleanKD.deserializeKey(BOOLEAN_FALSE_KEY, context));
 }

 @Test
 public void testByteKD() throws Exception {
 assertEquals((byte) 50, byteKD.deserializeKey(BYTE_KEY, context));
 }

 @Test
 public void testShortKD() throws Exception {
 assertEquals((short) 100, shortKD.deserializeKey(SHORT_KEY, context));
 }

 @Test
 public void testCharKD() throws Exception {
 assertEquals('A', charKD.deserializeKey(CHAR_KEY, context));
 }

 @Test
 public void testIntKD() throws Exception {
 assertEquals(1000, intKD.deserializeKey(INT_KEY, context));
 }

 @Test
 public void testLongKD() throws Exception {
 assertEquals(1000000, longKD.deserializeKey(LONG_KEY, context));
 }

 @Test
 public void testFloatKD() throws Exception {
 assertEquals(1.23f, floatKD.deserializeKey(FLOAT_KEY, context), 0.001f);
 }

 @Test
 public void testDoubleKD() throws Exception {
 assertEquals(1.23, doubleKD.deserializeKey(DOUBLE_KEY, context), 0.001);
 }

 @Test
 public void testURIKD() throws Exception {
 assertEquals(new URI(URI_KEY), uriKD.deserializeKey(URI_KEY, context));
 }

 @Test
 public void testURLKD() throws Exception {
 assertEquals(new URL(URL_KEY), urlKD.deserializeKey(URL_KEY, context));
 }

 @Test
 public void testClassKD() throws Exception {
 assertEquals(Class.forName(CLASS_KEY), classKD.deserializeKey(CLASS_KEY, context));
 }

 @Test
 public void testEnumKD() throws Exception {
 MyEnum myEnum = (MyEnum) enumKD.deserializeKey(ENUM_KEY, context);
 assertEquals(MyEnum.MY_ENUM_CONSTANT, myEnum);
 }

 @Test(expected = JsonMappingException.class)
 public void testEnumUnsupportedKD() throws Exception {
 enumUnsupportedKD.deserializeKey(ENUM_UNSUPPORTED_KEY, context);
 fail("Expected JsonMappingException");
 }

 @Test
 public void testLocaleKD() throws Exception {
 assertEquals(Locale.US, localeKD.deserializeKey(LOCALE_KEY, context));
 }

 @Test
 public void testCurrencyKD() throws Exception {
 assertEquals(Currency.getInstance(Currency.getInstance(CURRENCY_KEY).getCurrencyCode()),
         currencyKD.deserializeKey(CURRENCY_KEY, context));
 }

 @Test
 public void testDateKD() throws Exception {
 assertEquals(new Date(DATE_KEY.equals("") ? System.currentTimeMillis() : Long.parseLong(DATE_KEY)),
         dateKD.deserializeKey(DATE_KEY, context));
 }

 @Test
 public void testCalendarKD() throws Exception {
 AnnotatedClass ac = context.findConcreteClass(Calendar.class);
 BeanPropertyDefinition dateProperty = ac.findProperty("time");
 AnnotatedMethod dateSetter = context.findMethod(ac, dateProperty.getName(), Date.class);
 Calendar calendar = context.constructValue(Calendar.class, new Object[] { null });
 calendar.setTime(new Date(CALENDAR_KEY.equals("") ? System.currentTimeMillis() : Long.parseLong(CALENDAR_KEY)));
 assertEquals(calendar, calendarKD.deserializeKey(CALENDAR_KEY, context));
 }

 @Test
 public void testUUIDKD() throws Exception {
 assertEquals(UUID.fromString(UUID_KEY), uuidKD.deserializeKey(UUID_KEY, context));
 }
}