import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.apache.commons.lang.text.FormatFactory;
import org.junit.Before;
import org.junit.Test;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

 private ExtendedMessageFormat messageFormat;
 private FormatFactory formatFactory;

 @Before
 public void setup() {
 messageFormat = new ExtendedMessageFormat("{0,date,short} {1,message,format:customFormat}", Locale.US, getFormatRegistry());
 formatFactory = new FormatFactory() {
 @Override
 public Format getFormat(String formatName, String formatStyle, Locale locale) {
 switch (formatName) {
 case "customFormat":
 return new CustomFormat();
 default:
 return null;
 }
 }
 };
 messageFormat.setRegistry(getFormatRegistry());
 }

 private Map<String, FormatFactory> getFormatRegistry() {
 Map<String, FormatFactory> registry = new HashMap<>();
 registry.put("customFormat", formatFactory);
 return registry;
 }

 @Test
 public void testDefaultFormat() {
 Object[] args = {"2022-01-01", "Hello {0}!"};
 String result = messageFormat.format(args);
 assertEquals("Hello 2022-01-01!", result);
 }

 @Test
 public void testCustomFormat() {
 Object[] args = {"2022-01-01", "Custom format: [{}]"};
 messageFormat.applyPattern("{0,format:customFormat}");
 String result = messageFormat.format(args);
 assertEquals("[2022-01-01]", result);
 }

 @Test
 public void testEmptyFormat() {
 Object[] args = {};
 String pattern = "";
 messageFormat.applyPattern(pattern);
 String result = messageFormat.format(args);
 assertEquals("", result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidFormatElement() {
 Object[] args = {};
 String pattern = "{0,invalid,short}";
 messageFormat.applyPattern(pattern);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMissingFormat() {
 Object[] args = {"2022-01-01", "Missing format: {1,customFormat}"};
 messageFormat.applyPattern("{0,date,short} {1,message,format:customFormat}");
 }

 @Test
 public void testParseException() {
 Object[] args = {"2022-01-01", "Invalid format: {1,date,invalid}"};
 String pattern = "{0,date,short} {1,message,format:customFormat}";
 messageFormat.applyPattern(pattern);
 try {
 messageFormat.format(args);
 fail();
 } catch (ParseException e) {
 // expected
 }
 }
}

class CustomFormat implements Format {
 @Override
 public String format(Object obj, Locale locale, ParsePosition parsePosition) {
 if (obj == null) {
 throw new NullPointerException();
 }
 return "[" + obj.toString() + "]";
 }

 @Override
 public Object parseObject(String text, ParsePosition parsePosition) {
 throw new UnsupportedOperationException();
 }
}