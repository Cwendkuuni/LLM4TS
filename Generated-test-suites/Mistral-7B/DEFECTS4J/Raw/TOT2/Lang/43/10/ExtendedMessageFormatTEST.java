import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.apache.commons.lang.text.FormatFactory;
import org.junit.Before;
import org.junit.Test;
import java.text.ParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ExtendedMessageFormatTest {

 private ExtendedMessageFormat messageFormat;
 private FormatFactory formatFactory;

 @Before
 public void setUp() {
 formatFactory = new FormatFactory() {
 @Override
 public Format getFormat(String name, String pattern, Locale locale) {
 throw new UnsupportedOperationException();
 }
 };
 messageFormat = new ExtendedMessageFormat("{0,format(pattern='{0} {1}')}", new HashMap<String, FormatFactory>() {{
 put("PatternFormat", formatFactory);
 }});
 }

 @Test
 public void testBasicUsage() {
 String expected = "Hello World";
 String actual = messageFormat.format("{0} {1}", "Hello", "World");
 assert (expected.equals(actual));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMissingFormatName() {
 messageFormat = new ExtendedMessageFormat("{0,format(pattern='{0'} {1}')}", new HashMap<String, FormatFactory>() {{
 put("PatternFormat", formatFactory);
 }});
 messageFormat.applyPattern("{0} {1}");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyFormatName() {
 messageFormat = new ExtendedMessageFormat("{0,format(pattern='')}", new HashMap<String, FormatFactory>() {{
 put("PatternFormat", formatFactory);
 }});
 messageFormat.applyPattern("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMalformedFormatName() {
 messageFormat = new ExtendedMessageFormat("{0,format(pattern='{0{1}')}", new HashMap<String, FormatFactory>() {{
 put("PatternFormat", formatFactory);
 }});
 messageFormat.applyPattern("{0{1}");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMissingFormatStyle() {
 messageFormat = new ExtendedMessageFormat("{0,format(pattern='{0,style} {1}')}", new HashMap<String, FormatFactory>() {{
 put("PatternFormat", formatFactory);
 }});
 messageFormat.applyPattern("{0} {1}");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMalformedFormatStyle() {
 messageFormat = new ExtendedMessageFormat("{0,format(pattern='{0,style{1}')}", new HashMap<String, FormatFactory>() {{
 put("PatternFormat", formatFactory);
 }});
 messageFormat.applyPattern("{0} {1}");
 }

 @Test
 public void testFormatFactoryUsage() throws ParseException {
 messageFormat = new ExtendedMessageFormat("{0,format(pattern='{0,number,currency}')}", new HashMap<String, FormatFactory>() {{
 put("CurrencyFormat", new FormatFactory() {
 @Override
 public Format getFormat(String name, String pattern, Locale locale) {
 return new DecimalFormat("#,##0.00");
 }
 });
 put("PatternFormat", formatFactory);
 }});
 String input = "12345.67";
 String expected = "$12,345.67";
 String actual = messageFormat.format("{0}", input);
 assert (expected.equals(actual));
 }

 @Test
 public void testFormatFactoryUsageWithLocale() throws ParseException {
 messageFormat = new ExtendedMessageFormat("{0,format(pattern='{0,number,currency}')}", new HashMap<String, FormatFactory>() {{
 put("CurrencyFormat", new FormatFactory() {
 @Override
 public Format getFormat(String name, String pattern, Locale locale) {
 return new DecimalFormat("#,##0.00", locale);
 }
 });
 put("PatternFormat", formatFactory);
 }});
 String input = "12345.67";
 String expectedFR = "\u00A312,345.67";
 String expectedUS = "$12,345.67";
 String actualFR = messageFormat.format("{0}", input, Locale.FRENCH);
 String actualUS = messageFormat.format("{0}", input);
 assert (expectedFR.equals(actualFR));
 assert (expectedUS.equals(actualUS));
 }
}