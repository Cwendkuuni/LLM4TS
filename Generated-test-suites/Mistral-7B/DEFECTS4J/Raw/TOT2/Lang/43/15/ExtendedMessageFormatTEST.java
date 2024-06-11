import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.apache.commons.lang.text.FormatFactory;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

 private ExtendedMessageFormat messageFormat;
 private FormatFactory formatFactory;

 @Before
 public void setup() {
 formatFactory = new FormatFactory() {
 @Override
 public Format getFormat(String name, String args, Locale locale) {
 return new MockFormat(name, args);
 }
 };
 messageFormat = new ExtendedMessageFormat("{0, format=\"{1} {2}\"}", new HashMap<String, FormatFactory>() {{
 put("mock", formatFactory);
 }});
 }

 @Test
 public void testBasicUsage() {
 messageFormat.applyPattern("{0, format=\"{1} {2}\"}");
 assertEquals("expected {1} {2}", messageFormat.format(new Object[]{"one", "two"}));
 }

 @Test
 public void testMissingFormat() {
 messageFormat.applyPattern("{3, format=\"{1} {2}\"}");
 try {
 messageFormat.format(new Object[]{"one", "two", "three"});
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testInvalidFormat() {
 messageFormat.applyPattern("{0, format=\"{1} {2} {3}\"}");
 try {
 messageFormat.format(new Object[]{"one", "two"});
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testEscapedQuotes() {
 messageFormat.applyPattern("{0, format=\"{1}'{2}'{3}\"}");
 assertEquals("expected {1}'{2}'{3}", messageFormat.format(new Object[]{"one", "two'", "three"}));
 }

 @Test
 public void testNestedFormats() {
 messageFormat.applyPattern("{0, format=\"{1, format=\"{2} {3}\"}{4}\"}");
 assertEquals("expected {1, format=\"{2} {3}\"}{4}", messageFormat.format(new Object[]{"one", "two", "three", "four"}));
 }
}

class MockFormat implements Format {
 private String name;
 private String args;

 public MockFormat(String name, String args) {
 this.name = name;
 this.args = args;
 }

 @Override
 public String format(Object obj, Locale locale, String pattern, ParsePosition pos) {
 if (name.equals("mock")) {
 if (args != null && args.equals("format")) {
 return "formatted";
 } else {
 throw new IllegalArgumentException("Invalid format argument");
 }
 }
 return "";
 }

 @Override
 public String format(Object obj, Locale locale) {
 return format(obj, locale, null);
 }

 @Override
 public StringBuffer format(Object obj, Locale locale, StringBuffer sb, ParsePosition pos) {
 throw new UnsupportedOperationException();
 }

 @Override
 public StringBuffer format(Object obj, Locale locale, StringBuffer sb) {
 throw new UnsupportedOperationException();
 }

 @Override
 public int parseObject(String source, ParsePosition pos) {
 throw new UnsupportedOperationException();
 }

 @Override
 public int parse(String source, ParsePosition pos) {
 throw new UnsupportedOperationException();
 }

 @Override
 public ParsePosition parse(String source, ParsePosition pos, Object param) {
 throw new UnsupportedOperationException();
 }

 @Override
 public boolean matches(CharSequence arg0) {
 throw new UnsupportedOperationException();
 }

 @Override
 public void setParsePosition(ParsePosition arg0) {
 throw new UnsupportedOperationException();
 }
}