import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.apache.commons.lang.text.FormatFactory;
import org.junit.Test;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ExtendedMessageFormatTest {

 private static final FormatFactory formatFactory = new FormatFactory() {
 @Override
 public Format getFormat(String name, String arguments, Locale locale) {
 throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
 }
 };

 @Test
 public void testEmptyMessageFormat() {
 ExtendedMessageFormat emptyFormat = new ExtendedMessageFormat("", new HashMap<String, FormatFactory>());
 assertEquals("", emptyFormat.format(new Object[0]));
 }

 @Test
 public void testMessageFormatWithArguments() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, your age is {1}.", new HashMap<String, FormatFactory>() {{
 put("int", new IntFormatFactory());
 put("locale", new LocaleFormatFactory());
 }});
 format.applyPattern("Hello {0}, your age is {1}.");
 Object[] args = new Object[]{"John", 25};
 assertEquals("Hello John, your age is 25.", format.format(args));
 }

 @Test
 public void testMessageFormatWithFormatName() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0,date,MM-dd-yyyy}, your age is {1}.", new HashMap<String, FormatFactory>() {{
 put("int", new IntFormatFactory());
 put("date", new DateFormatFactory());
 }});
 format.applyPattern("Hello {0,date,MM-dd-yyyy}, your age is {1}.");
 Object[] args = new Object[]{"John", 25};
 assertEquals("Hello 12-31-2017, your age is 25.", format.format(args));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidFormatArgumentIndex() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, your age is {1}.", new HashMap<String, FormatFactory>() {{
 put("int", new IntFormatFactory());
 put("locale", new LocaleFormatFactory());
 }});
 format.applyPattern("Hello {1}, your age is {0}.");
 Object[] args = new Object[]{"John", 25};
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnterminatedFormatElement() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0, your age is {1}.", new HashMap<String, FormatFactory>() {{
 put("int", new IntFormatFactory());
 put("locale", new LocaleFormatFactory());
 }});
 }

 @Test(expected = ParseException.class)
 public void testInvalidFormatDescription() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0,invalid_format,MM-dd-yyyy}, your age is {1}.", new HashMap<String, FormatFactory>() {{
 put("int", new IntFormatFactory());
 put("locale", new LocaleFormatFactory());
 put("invalid_format", new InvalidFormatFactory());
 }});
 Object[] args = new Object[]{"John", 25};
 }

 @Test
 public void testMessageFormatWithException() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0,choice,0#Exception {1},1#Success {1}}.", new HashMap<String, FormatFactory>() {{
 put("int", new IntFormatFactory());
 put("choice", new ChoiceFormatFactory());
 put("Exception", new ExceptionFormatFactory());
 put("Success", new SuccessFormatFactory());
 }});
 Object[] args = new Object[]{"0"};
 assertEquals("Hello Exception John.", format.format(args));
 args[0] = "1";
 assertEquals("Hello Success John.", format.format(args));
 }

}

class IntFormatFactory implements FormatFactory {
 @Override
 public Format getFormat(String name, String arguments, Locale locale) {
 return new NumberFormat();
 }
}

class LocaleFormatFactory implements FormatFactory {
 @Override
 public Format getFormat(String name, String arguments, Locale locale) {
 return new LocaleFormat();
 }
}

class DateFormatFactory implements FormatFactory {
 @Override
 public Format getFormat(String name, String arguments, Locale locale) {
 return new SimpleDateFormat("MM-dd-yyyy");
 }
}

class InvalidFormatFactory implements FormatFactory {
 @Override
 public Format getFormat(String name, String arguments, Locale locale) {
 throw new UnsupportedOperationException("Invalid format factory.");
 }
}

class ChoiceFormatFactory implements FormatFactory {
 @Override
 public Format getFormat(String name, String arguments, Locale locale) {
 return new ChoiceFormat("Exception", "Success");
 }
}

class ExceptionFormatFactory implements FormatFactory {
 @Override
 public Format getFormat(String name, String arguments, Locale locale) {
 throw new RuntimeException("This is an exception.");
 }
}

class SuccessFormatFactory implements FormatFactory {
 @Override
 public Format getFormat(String name, String arguments, Locale locale) {
 return new MessageFormat("Success {0}");
 }
}