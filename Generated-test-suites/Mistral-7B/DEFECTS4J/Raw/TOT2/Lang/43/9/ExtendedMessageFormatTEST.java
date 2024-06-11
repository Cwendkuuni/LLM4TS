import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.apache.commons.lang.text.FormatFactory;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ExtendedMessageFormatTest {

 private String pattern;
 private Locale locale;
 private Map<String, FormatFactory> registry;
 private ExtendedMessageFormat format;

 @Before
 public void setUp() {
 // Create a test registry with a single format factory instance
 registry = new java.util.HashMap<>();
 registry.put("customFormat", new CustomFormatFactory());

 // Test pattern with no custom formats
 pattern = "{0}";
 locale = Locale.US;

 // Test pattern with a single custom format
 pattern = "{customFormat,choice,0#Custom Format 1|1#Custom Format 2}{0}";
 }

 @Test
 public void testBasicFunctionality() {
 String message = format.format(new Object[]{"Test"}, pattern, locale);
 assertEquals("Test", message);
 }

 @Test
 public void testCustomFormatWithSingleFormat() {
 format = new ExtendedMessageFormat(pattern, locale, registry);
 format.applyPattern(pattern);
 String message = format.format(new Object[]{"Test"}, pattern, locale);
 assertEquals("Test", message);
 // Test custom format
 format.setArgument(0, "Custom Test");
 message = format.format(new Object[0], pattern, locale);
 assertEquals("Custom Format 1:Custom Test", message);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidCustomFormat() {
 pattern = "{customFormat,choice,0#Custom Format 1|1#Custom Format 2}{0,choice,0#A|1#B}{0}";
 format = new ExtendedMessageFormat(pattern, locale, registry);
 format.applyPattern(pattern);
 format.setArgument(0, "Test");
 format.format(new Object[0], pattern, locale);
 }

 @Test
 public void testMaximumNestedFormats() {
 String nestedFormatsPattern = "{0}{1}{2}";
 format = new ExtendedMessageFormat(nestedFormatsPattern, locale, registry);
 format.applyPattern(nestedFormatsPattern);
 String message = format.format(new Object[]{"{3}{4}{5}", "Test1", "Test2",
 "Test3", "Test4", "Test5"}, nestedFormatsPattern, locale);
 assertEquals("Test1Test2Test3Test4Test5", message);
 }

 @Test
 public void testThreadSafety() {
 // Test thread safety by having multiple threads format the same message
 final int numThreads = 10;
 final String message = "Test {0}";
 final Object[] args = {"Thread {0}"};
 final ExtendedMessageFormat format = new ExtendedMessageFormat(message, locale, registry);
 final ArrayList<String> results = new ArrayList<>();
 for (int i = 0; i < numThreads; i++) {
 new Thread(() -> results.add(format.format(args, message, locale).toString()))
 .start();
 }
 for (int i = 0; i < numThreads; i++) {
 try {
 Thread.sleep(1);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 }
 for (String result : results) {
 assertEquals("Test Thread " + StringUtils.leftPad(Integer.toString(i), 2,
 "0"), result);
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSettingFormat() {
 format = new ExtendedMessageFormat(pattern, locale, registry);
 format.applyPattern(pattern);
 format.setFormat(0, new StringFormat());
 }
}

class CustomFormatFactory implements FormatFactory {

 @Override
 public Format getFormat(String formatName, String formatStyle, Locale locale) {
 return new StringFormat();
 }
}

class StringFormat extends Format {
 @Override
 public Object format(Object obj, Locale locale) {
 return obj.toString();
 }

 @Override
 public StringBuffer format(StringBuffer toAppendTo, Object object, Locale locale) {
 toAppendTo.append(object.toString());
 return toAppendTo;
 }

 @Override
 public ParsePosition parseObject(String source, ParsePosition pos) {
 throw new UnsupportedOperationException();
 }

 @Override
 public StringBuffer parse(String source, StringBuffer toAppendTo, ParsePosition pos) {
 throw new UnsupportedOperationException();
 }
}