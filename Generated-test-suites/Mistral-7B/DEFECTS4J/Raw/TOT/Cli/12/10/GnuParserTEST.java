import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class GnuParserTest {

 private static final String[] DEFAULT_ARGUMENTS = new String[]{"-Dproperty=value", "--foo=bar", "-a", "--", "arg1", "arg2", "arg3"};

 @Test
 public void testFlattenWithOptionsAndValues() {
 Options options = new Options();
 options.addOption("D", "property", true, "property value");
 options.addOption("f", "foo", true, "foo value");

 GnuParser parser = new GnuParser();
 String[] expected = new String[]{"-D", "property", "value", "--", "f", "foo", "bar"};
 String[] result = parser.flatten(options, DEFAULT_ARGUMENTS, false);
 assertArrayEquals(expected, result);
 }

 @Test(expected = ParseException.class)
 public void testFlattenWithInvalidOption() {
 Options options = new Options();

 GnuParser parser = new GnuParser();
 String[] arguments = new String[]{"-x", "--", "arg1", "arg2", "arg3"}; // non-existent option '-x'
 parser.flatten(options, arguments, false);
 }

 @Test
 public void testFlattenWithStopAtNonOption() {
 Options options = new Options();
 options.addOption("D", "property", true, "property value");
 options.addOption("f", "foo", true, "foo value");

 GnuParser parser = new GnuParser();
 String[] arguments = new String[]{"-Dproperty=value", "--foo=bar", "-a", "arg1", "arg2", "arg3"};
 String[] expected = new String[]{"-D", "property", "value", "--", "foo", "bar", "-a", "arg1", "arg2", "arg3"};
 String[] result = parser.flatten(options, arguments, true);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithEmptyArguments() {
 Options options = new Options();

 GnuParser parser = new GnuParser();
 String[] arguments = new String[]{};
 String[] expected = new String[]{};
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithLongOptions() {
 Options options = new Options();
 options.addOption("longOptionName", true, "long option description");

 GnuParser parser = new GnuParser();
 String[] arguments = new String[]{"--longOptionName=value", "--foo=bar", "-a", "--", "arg1", "arg2", "arg3"};
 String[] expected = new String[]{"--longOptionName", "value", "--", "foo", "bar", "-a", "arg1", "arg2", "arg3"};
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithShortOptions() {
 Options options = new Options();
 options.addOption("l", "longOptionName", true, "long option description");

 GnuParser parser = new GnuParser();
 String[] arguments = new String[]{"--longOptionName=value", "--foo=bar", "-a", "--", "arg1", "arg2", "arg3"};
 String[] expected = new String[]{"--", "l", "longOptionName", "value", "--", "foo", "bar", "-a", "arg1", "arg2", "arg3"};
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithDashBeforeShortOption() {
 Options options = new Options();
 options.addOption("l", "longOptionName", true, "long option description");

 GnuParser parser = new GnuParser();
 String[] arguments = new String[]{"-l", "--longOptionName=value", "--foo=bar", "-a", "--", "arg1", "arg2", "arg3"};
 String[] expected = new String[]{"-l", "--longOptionName", "value", "--", "foo", "bar", "-a", "arg1", "arg2", "arg3"};
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithDoubleShortOptions() {
 Options options = new Options();
 options.addOption("ss", "shortDoubleOption", true, "short double option description");

 GnuParser parser = new GnuParser();
 String[] arguments = new String[]{"--ss", "--shortDoubleOption=value", "--foo=bar", "-a", "--", "arg1", "arg2", "arg3"};
 String[] expected = new String[]{"--ss", "--shortDoubleOption", "value", "--", "foo", "bar", "-a", "arg1", "arg2", "arg3"};
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithLongAndShortOptions() {
 Options options = new Options();
 options.addOption("lo", "longOption", true, "long option description");
 options.addOption("s", "shortOption", true, "short option description");

 GnuParser parser = new GnuParser();
 String[] arguments = new String[]{"--lo=value", "--", "-s", "shortOptionValue", "--foo=bar", "-a", "--", "arg1", "arg2", "arg3"};
 String[] expected = new String[]{"--lo", "value", "--", "-s", "shortOption", "shortOptionValue", "--", "foo", "bar", "-a", "arg1", "arg2", "arg3"};
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithMixedOptions() {
 Options options = new Options();
 options.addOption("b", "booleanOption", false, "boolean option description");
 options.addOption("i", "integerOption", true, "integer option description");
 options.addOption("s", "stringOption", true, "string option description");
 options.addOption("f", "fileOption", File.class, "file option description");

 GnuParser parser = new GnuParser();
 String[] arguments = new String[]{"--booleanOption", "--integerOption=123", "--stringOption=stringValue", "--fileOption=filePath", "--foo=bar", "-a", "--", "arg1", "arg2", "arg3"};
 String[] expected = new String[]{"--booleanOption", "--integerOption", "123", "--", "stringOption", "stringValue", "--", "fileOption", "filePath", "--", "foo", "bar", "-a", "arg1", "arg2", "arg3"};
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithUnknownOptionType() {
 Options options = new Options();
 options.addOption("o", "optionWithUnknownType", null, "option with unknown type description");

 GnuParser parser = new GnuParser();
 String[] arguments = new String[]{"--optionWithUnknownType=value", "--foo=bar", "-a", "--", "arg1", "arg2", "arg3"};
 String[] expected = new String[]{"--optionWithUnknownType", "value", "--", "foo", "bar", "-a", "arg1", "arg2", "arg3"};
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithUnknownOptionTypeWithValue() {
 Options options = new Options();
 options.addOption("o", "optionWithUnknownType", null, "option with unknown type description");

 GnuParser parser = new GnuParser();
 String[] arguments = new String[]{"--optionWithUnknownType=value", "--foo=bar", "-a", "--", "arg1", "arg2", "arg3"};
 String[] expected = new String[]{"--optionWithUnknownType", "value", "--", "foo", "bar", "-a", "arg1", "arg2", "arg3"};
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithGivenDirectives() {
 Options options = new Options();
 options.addOption("g", "given", true, "given directive description");
 options.addOption("a", "allow-extra-args", true, "allow extra args option description");

 GnuParser parser = new GnuParser();
 String[] arguments = new String[]{"--given=value", "--allow-extra-args", "--foo=bar", "-a", "--", "arg1", "arg2", "arg3"};
 String[] expected = new String[]{"--given", "value", "--allow-extra-args", "--", "foo", "bar", "-a", "arg1", "arg2", "arg3"};
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithGivenDirectivesAndStopAtNonOption() {
 Options options = new Options();
 options.addOption("g", "given", true, "given directive description");
 options.addOption("a", "allow-extra-args", true, "allow extra args option description");

 GnuParser parser = new GnuParser();
 String[] arguments = new String[]{"--given=value", "--allow-extra-args", "--foo=bar", "-a", "arg1", "arg2", "arg3"};
 String[] expected = new String[]{"--given", "value", "--allow-extra-args", "--foo", "bar", "arg1", "arg2", "arg3"};
 String[] result = parser.flatten(options, arguments, true);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithNoOptions() {
 Options options = new Options();

 GnuParser parser = new GnuParser();
 String[] arguments = new String[]{"--", "arg1", "arg2", "arg3"};
 String[] expected = new String[]{"--", "arg1", "arg2", "arg3"};
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithMultipleOccurrencesOfSameOption() {
 Options options = new Options();
 options.addOption("o", "option", true, "option description");

 GnuParser parser = new GnuParser();
 String[] arguments = new String[]{"--option=value1", "--option=value2", "--option=value3", "--foo=bar", "-a", "--", "arg1", "arg2", "arg3"};
 String[] expected = new String[]{"--option", "value1", "--option", "value2", "--option", "value3", "--", "foo", "bar", "-a", "arg1", "arg2", "arg3"};
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(expected, result);
 }
}