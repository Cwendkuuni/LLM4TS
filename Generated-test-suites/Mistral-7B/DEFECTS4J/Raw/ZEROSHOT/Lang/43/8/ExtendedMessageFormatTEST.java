import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.apache.commons.lang.text.FormatFactory;
import org.apache.commons.lang.text.SimpleDateFormat;
import org.junit.Before;
import org.junit.Test;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ExtendedMessageFormatTest {

 private ExtendedMessageFormat messageFormat;
 private Map<String, FormatFactory> registry;

 @Before
 public void setUp() {
 registry = new HashMap<>();
 registry.put("DATE", new DateFormatFactory());
 messageFormat = new ExtendedMessageFormat("Hello {0,date,DATE}", registry);
 }

 @Test
 public void testFormat() {
 Object[] args = new Object[]{"2023-03-01"};
 String result = messageFormat.format(args);
 assert result.equals("Hello 2023-03-01");
 }

 @Test
 public void testParse() throws ParseException {
 ParsePosition pos = new ParsePosition(0);
 Object[] args = messageFormat.parse(new String[]{"Hello {0,date,DATE}"}, pos);
 assert args.length == 1;
 assert args[0] instanceof String;
 SimpleDateFormat dateFormat = (SimpleDateFormat) messageFormat.getFormats()[0];
 assert dateFormat.parse((String) args[0], new ParsePosition(0))
 .equals(new java.util.Date("2023-03-01"));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormat() {
 messageFormat.setFormat(0, new SimpleDateFormat(""));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormatByArgumentIndex() {
 messageFormat.setFormatByArgumentIndex(0, new SimpleDateFormat(""));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormats() {
 messageFormat.setFormats(new SimpleDateFormat[] {new SimpleDateFormat("")});
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormatsByArgumentIndex() {
 messageFormat.setFormatsByArgumentIndex(new SimpleDateFormat[] {new SimpleDateFormat("")});
 }

 static class DateFormatFactory implements FormatFactory {
 @Override
 public Format getFormat(String format, String arg, Locale locale) {
 return new SimpleDateFormat(format);
 }
 }
}