import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.apache.commons.lang.text.FormatFactory;
import org.apache.commons.lang.text.SimpleFormatFactory;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

 private String pattern = "{0,date,dd-MM-yyyy}{1,choice,0#Hello{2}{3},1#Hi{2}}";
 private Map<String, FormatFactory> registry = new LinkedHashMap<>();

 @BeforeClass
 public static void setupRegistry() {
 registry.put("date", new SimpleDateFormatFactory());
 registry.put("choice", new ChoiceFormatFactory());
 }

 @Before
 public void setup() {
 ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern, registry);
 }

 @Test
 public void testFormat() {
 Object[] args = new Object[]{new Date(), "John", "you"};
 String result = fmt.format(args);
 assertEquals("31-12-2021Hi you", result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidFormat() {
 ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0,date,dd-MM-yyyy}{1,choice,0#Hello{2}{3},1#Hi{2}}", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMissingFormat() {
 ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0,missingFormat,1}{1}", registry);
 }

 @Test
 public void testFormatWithNulls() {
 Object[] args = new Object[]{new Date(), null, null};
 String result = fmt.format(args);
 assertNull(result);
 }

 @Test
 public void testFormatWithEmptyString() {
 Object[] args = new Object[]{new Date(), "", ""};
 String result = fmt.format(args);
 assertEquals("", result);
 }

 @Test
 public void testFormatWithMultipleArguments() {
 Object[] args = new Object[]{new Date(), "Alice", "good morning"};
 String result = fmt.format(args);
 assertEquals("31-12-2021Hello Alice good morning", result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidFormatIndex() {
 ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern, registry);
 fmt.setFormat(10, new SimpleDateFormat());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormatByArgumentIndex() {
 ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern, registry);
 fmt.setFormatByArgumentIndex(1, new SimpleDateFormat());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormats() {
 ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern, registry);
 Format[] formats = new Format[3];
 formats[0] = new SimpleDateFormat();
 fmt.setFormats(formats);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormatsByArgumentIndex() {
 ExtendedMessageFormat fmt = new ExtendedMessageFormat(pattern, registry);
 Format[] formats = new Format[3];
 formats[0] = new SimpleDateFormat();
 fmt.setFormatsByArgumentIndex(formats);
 }
}