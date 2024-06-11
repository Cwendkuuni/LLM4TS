import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.apache.commons.lang.text.FormatFactory;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

 private static final String DUMMY_PATTERN = "";
 private static final String ESCAPED_QUOTE = "''";
 private static final char START_FMT = ',';
 private static final char END_FE = '}';
 private static final char START_FE_WITH_ARG = '{';
 private static final char START_FE_WITH_FORMAT = '{';
 private static final char QUOTE = '\'';

 @Test
 public void testConstructors() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("{0}");
 ExtendedMessageFormat formatWithRegistry = new ExtendedMessageFormat("{0}", new HashMap<>());
 }

 @Test
 public void testApplyPattern() {
 String pattern = "{0} {1,choice,0#Hello|1#Hi}";
 Map<String, FormatFactory> registry = new HashMap<>();
 registry.put("choice", new ChoiceFormatFactory());
 ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
 format.applyPattern(pattern);
 assertEquals(pattern, format.toPattern());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyPatternWithInvalidPattern() {
 String pattern = "{0} {1,choice,0#Hello|1Hi}"; // missing comma
 Map<String, FormatFactory> registry = new HashMap<>();
 ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
 format.applyPattern(pattern);
 }

 @Test
 public void testFormatWithNoCustomFormats() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1}", new HashMap<>());
 Object[] args = {"Hello", "World"};
 String result = format.format(args);
 assertEquals("Hello World", result);
 }

 @Test
 public void testFormatWithCustomFormat() {
 Map<String, FormatFactory> registry = new HashMap<>();
 registry.put("choice", new ChoiceFormatFactory());
 ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1,choice,0#Hello|1#Hi}", registry);
 format.applyPattern("{0} {1,choice,0#Hello|1#Hi}");
 Object[] args = {"John", "1"};
 String result = format.format(args);
 assertEquals("John Hi", result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatWithInvalidFormatIndex() {
 Map<String, FormatFactory> registry = new HashMap<>();
 registry.put("choice", new ChoiceFormatFactory());
 ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1,choice,2#Hello|1#Hi}", registry);
 format.applyPattern("{0} {1,choice,2#Hello|1#Hi}");
 Object[] args = {"John", "1"};
 format.format(args);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormat() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1}", new HashMap<>());
 format.setFormat(1, new Format() {}); // UnsupportedOperationException
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormatByArgumentIndex() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1}", new HashMap<>());
 format.setFormatsByArgumentIndex(new Format[] {new Format() {}}); // UnsupportedOperationException
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormats() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1}", new HashMap<>());
 format.setFormats(new Format[] {new Format() {}}); // UnsupportedOperationException
 }

 @Test
 public void testSetFormatsByArgumentIndex() {
 Map<String, FormatFactory> registry = new HashMap<>();
 registry.put("choice", new ChoiceFormatFactory());
 ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1,choice,0#Hello|1#Hi}", registry);
 format.applyPattern("{0} {1,choice,0#Hello|1#Hi}");
 Format format1 = new ChoiceFormat("{0}, {1}");
 Format format2 = new Format() {};
 Format[] formats = {format1, format2};
 format.setFormatsByArgumentIndex(formats);
 Object[] args = {"John", "1"};
 String result = format.format(args);
 assertEquals("John, 1", result);
 }

 @Test
 public void testFormatWithCustomFormatAndLocale() {
 Map<String, FormatFactory> registry = new HashMap<>();
 registry.put("choice", new ChoiceFormatFactoryWithLocale());
 ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1,choice,0#Hello|1#Hi}", registry);
 format.applyPattern("{0} {1,choice,0#Hello|1#Hi}");
 Object[] args = {"John", "1"};
 String result = format.format(args, Locale.ENGLISH);
 assertEquals("John Hi", result);
 String resultWithLocale = format.format(args, Locale.FRENCH);
 assertEquals("John Bonjour", resultWithLocale);
 }

 @Test
 public void testFormatWithCustomFormatAndEmptyRegistry() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1}", new HashMap<>());
 format.applyPattern("{0} {1,choice,0#Hello|1#Hi}");
 Object[] args = {"John", "1"};
 try {
 format.format(args);
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Pass
 }
 }

 @Test
 public void testFormatWithCustomFormatAndInvalidFormatDescription() {
 Map<String, FormatFactory> registry = new HashMap<>();
 registry.put("choice", new ChoiceFormatFactory());
 ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1,choice,invalidFormatDescription#Hello|1#Hi}", registry);
 format.applyPattern("{0} {1,choice,invalidFormatDescription#Hello|1#Hi}");
 Object[] args = {"John", "1"};
 try {
 format.format(args);
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Pass
 }
 }

 @Test
 public void testFormatWithCustomFormatAndArgumentOutOfRange() {
 Map<String, FormatFactory> registry = new HashMap<>();
 registry.put("choice", new ChoiceFormatFactory());
 ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1,choice,0#Hello|1#Hi}", registry);
 format.applyPattern("{0} {1,choice,0#Hello|1#Hi}");
 Object[] args = {"John", "3"};
 try {
 format.format(args);
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Pass
 }
 }

 @Test
 public void testFormatWithCustomFormatAndNullArgument() {
 Map<String, FormatFactory> registry = new HashMap<>();
 registry.put("choice", new ChoiceFormatFactory());
 ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1,choice,0#Hello|1#Hi}", registry);
 format.applyPattern("{0} {1,choice,0#Hello|1#Hi}");
 Object[] args = {"John", null};
 try {
 format.format(args);
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Pass
 }
 }

 @Test
 public void testFormatWithCustomFormatAndEmptyArgument() {
 Map<String, FormatFactory> registry = new HashMap<>();
 registry.put("choice", new ChoiceFormatFactory());
 ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1,choice,0#Hello|1#Hi}", registry);
 format.applyPattern("{0} {1,choice,0#Hello|1#Hi}");
 Object[] args = {"", "1"};
 String result = format.format(args);
 assertEquals("", result);
 }

 @Test
 public void testFormatWithCustomFormatAndNegativeArgument() {
 Map<String, FormatFactory> registry = new HashMap<>();
 registry.put("choice", new ChoiceFormatFactory());
 ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1,choice,0#Hello|1#Hi}", registry);
 format.applyPattern("{0} {1,choice,0#Hello|1#Hi}");
 Object[] args = {"", -1};
 try {
 format.format(args);
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Pass
 }
 }

 @Test
 public void testFormatWithCustomFormatAndInvalidArgumentType() {
 Map<String, FormatFactory> registry = new HashMap<>();
 registry.put("choice", new ChoiceFormatFactory());
 ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1,choice,0#Hello|1#Hi}", registry);
 format.applyPattern("{0} {1,choice,0#Hello|1#Hi}");
 Object[] args = {"", new Object() {}};
 try {
 format.format(args);
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Pass
 }
 }

 @Test
 public void testFormatWithCustomFormatAndFormatFactoryThrowsException() {
 Map<String, FormatFactory> registry = new HashMap<>();
 registry.put("choice", new ChoiceFormatFactoryThrowingException());
 ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1,choice,0#Hello|1#Hi}", registry);
 format.applyPattern("{0} {1,choice,0#Hello|1#Hi}");
 Object[] args = {"John", "1"};
 try {
 format.format(args);
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Pass
 }
 }

 @Test
 public void testFormatWithCustomFormatAndFormatFactoryReturnsNull() {
 Map<String, FormatFactory> registry = new HashMap<>();
 registry.put("choice", new ChoiceFormatFactoryReturningNull());
 ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1,choice,0#Hello|1#Hi}", registry);
 format.applyPattern("{0} {1,choice,0#Hello|1#Hi}");
 Object[] args = {"John", "1"};
 try {
 format.format(args);
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Pass
 }
 }
}