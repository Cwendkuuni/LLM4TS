import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class PosixParserTest {

 @Test
 public void testSimpleOptions() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", false, "Option B");

 String[] args = new String[]{"-a", "--b"};
 PosixParser parser = new PosixParser();
 String[] result = parser.parse(options, args, false);

 assertEquals(args.length, result.length);
 assertEquals("-a", result[0]);
 assertEquals("--b", result[1]);
 }

 @Test
 public void testOptionWithArg() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", true, "Option B with argument");

 String[] args = new String[]{"-a", "--b=test"};
 PosixParser parser = new PosixParser();
 String[] result = parser.parse(options, args, false);

 assertEquals(args.length, result.length);
 assertEquals("-a", result[0]);
 assertEquals("--b=test", result[1]);
 }

 @Test(expected = ParseException.class)
 public void testInvalidOption() throws ParseException {
 Options options = new Options();
 String[] args = new String[]{"--invalid"};
 PosixParser parser = new PosixParser();

 parser.parse(options, args, false);
 }

 @Test(expected = ParseException.class)
 public void testMissingOption() throws ParseException {
 Options options = new Options();
 String[] args = new String[]{"-a"};
 PosixParser parser = new PosixParser();

 parser.parse(options, args, false);
 }

 @Test
 public void testDoubleHyphen() throws ParseException {
 Options options = new Options();
 String[] args = new String[]{"--", "--help"};
 PosixParser parser = new PosixParser();
 String[] result = parser.parse(options, args, false);

 assertEquals(args.length, result.length);
 assertEquals("--", result[0]);
 assertEquals("--help", result[1]);
 }

 @Test
 public void testLongOptions() throws ParseException {
 Options options = new Options();
 options.addOption("longOption", false, "Long Option");

 String[] args = new String[]{"--longOption"};
 PosixParser parser = new PosixParser();
 String[] result = parser.parse(options, args, false);

 assertEquals(args.length, result.length);
 assertEquals("--longOption", result[0]);
 }

 @Test
 public void testLongOptionsWithArg() throws ParseException {
 Options options = new Options();
 options.addOption("longOption", true, "Long Option with argument");

 String[] args = new String[]{"--longOption=test"};
 PosixParser parser = new PosixParser();
 String[] result = parser.parse(options, args, false);

 assertEquals(args.length, result.length);
 assertEquals("--longOption=test", result[0]);
 }

 @Test(expected = ParseException.class)
 public void testMissingLongOptionArg() throws ParseException {
 Options options = new Options();
 options.addOption("longOption", true, "Long Option with argument");

 String[] args = new String[]{"--longOption"};
 PosixParser parser = new PosixParser();

 parser.parse(options, args, false);
 }
}