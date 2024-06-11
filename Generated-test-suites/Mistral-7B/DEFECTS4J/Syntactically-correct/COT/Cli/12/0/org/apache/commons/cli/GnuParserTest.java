import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(Parameterized.class)
public class GnuParserTest {

 private GnuParser parser;
 private Options options;

 @Parameterized.Parameters(name = "{index}: flatten method with arguments {0}")
 public static Iterable<Object[]> data() {
 return Arrays.asList(new Object[][]{
 // normal cases
 {"", new String[]{}},
 {"-a", new String[]{"-a"}},
 {"--help", new String[]{"--help"}},
 {"-Dproperty=value", new String[]{"-Dproperty=value"}},
 {"--foo=value", new String[]{"--foo", "value"}},
 {"--foo=value1 value2", new String[]{"--foo", "value1", "value2"}},
 {"-abc", new String[]{"-a", "-b", "-c"}},
 // edge cases
 {"-", new String[]{"-"}},
 {"--", new String[]{"--"}},
 {"--foo", new String[]{"--foo"}},
 {"--foo=value", new String[]{"--foo", "value"}},
 {"--foo=value1 value2", new String[]{"--foo", "value1", "value2"}},
 {"--foo=value1 value2 --help", new String[]{"--foo", "value1", "value2", "--help"}},
 {"--foo=value1 value2 --foo=value3", new String[]{"--foo", "value1", "value2", "--foo", "value3"}},
 {"--foo=value1 value2 --", new String[]{"--foo", "value1", "value2", "--"}},
 {"--foo=value1 value2 --foo", new String[]{"--foo", "value1", "value2", "--foo"}},
 {"--foo=value1 value2 --foo=", new String[]{"--foo", "value1", "value2", "--foo="}},
 {"--foo=value1 value2 --foo=value3=", new String[]{"--foo", "value1", "value2", "--foo=value3="}},
 // exception cases
 {"-", new String[]{"-a", "-b"}},
 {"--", new String[]{"--help"}},
 {"-a--", new String[]{"-a--"}},
 {"--a--", new String[]{"--a--"}},
 {"--foo=value1 value2 --foo=value3=value4", new String[]{"--foo", "value1", "value2", "--foo=value3=value4"}}
 });
 }

 @Before
 public void setup() {
 options = new Options();
 parser = new GnuParser();
 }

 @Test(expected = ParseException.class)
 public void testParseExceptionCases() throws ParseException {
 for (Object[] args : data()) {
 parser.flatten(options, (String[]) args[1], false);
 }
 }

 @Test
 public void testFlattenNormalCases() throws ParseException {
 for (Object[] args : data()) {
 String[] expected = (String[]) args[0];
 String[] actual = parser.flatten(options, (String[]) args[1], false);
 Assert.assertArrayEquals(expected, actual);
 }
 }

 @Test
 public void testFlattenEdgeCases() throws ParseException {
 for (Object[] args : data()) {
 String[] expected = (String[]) args[0];
 String[] actual = parser.flatten(options, (String[]) args[1], false);
 Assert.assertArrayEquals(expected, actual);
 }
 }
}