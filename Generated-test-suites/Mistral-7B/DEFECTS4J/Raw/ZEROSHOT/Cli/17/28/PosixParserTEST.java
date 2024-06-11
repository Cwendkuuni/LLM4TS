import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PosixParserTest {

 @Test
 public void testFlattenEmpty() {
 Options options = new Options();
 String[] args = new String[0];
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertEquals(new String[0], result);
 }

 @Test
 public void testFlattenSimple() {
 Options options = new Options();
 String[] args = {"--option1", "--option2"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertEquals(new String[]{"--option1", "--option2"}, result);
 }

 @Test
 public void testFlattenSingleArgument() {
 Options options = new Options();
 options.addOption("option1", true, "o");
 String[] args = {"-o", "value"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertEquals(new String[]{"-o", "value"}, result);
 }

 @Test
 public void testFlattenMultipleArguments() {
 Options options = new Options();
 options.addOption("option1", true, "o");
 options.addOption("option2", true, "i");
 String[] args = {"-o", "value1", "-i", "value2"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertEquals(new String[]{"-o", "value1", "-i", "value2"}, result);
 }

 @Test
 public void testFlattenLongOption() {
 Options options = new Options();
 options.addOption("longOption", true, "l");
 String[] args = {"--longOption", "value"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertEquals(new String[]{"--longOption", "value"}, result);
 }

 @Test
 public void testFlattenLongOptionWithValue() {
 Options options = new Options();
 options.addOption("longOption", true, "l");
 String[] args = {"--longOption=value"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertEquals(new String[]{"--longOption", "value"}, result);
 }

 @Test
 public void testFlattenLongOptionWithArgument() {
 Options options = new Options();
 options.addOption("longOption", true, "l");
 options.addOption("option1", true, "o");
 String[] args = {"--longOption", "value", "--option1", "value2"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertEquals(new String[]{"--longOption", "value", "--option1", "value2"}, result);
 }

 @Test
 public void testFlattenLongOptionWithMultipleArguments() {
 Options options = new Options();
 options.addOption("longOption", true, "l");
 options.addOption("option1", true, "o");
 String[] args = {"--longOption=value1,value2", "--option1", "value"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertEquals(new String[]{"--longOption=value1,value2", "--option1", "value"}, result);
 }

 @Test(expected = ParseException.class)
 public void testFlattenInvalidOption() {
 Options options = new Options();
 String[] args = {"--invalidOption"};
 PosixParser parser = new PosixParser();
 parser.flatten(options, args, false);
 }

 @Test
 public void testFlattenStopAtNonOption() {
 Options options = new Options();
 options.addOption("option1", true, "o");
 String[] args = {"--option1", "value", "invalid"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, true);
 assertEquals(new String[]{"--option1", "value"}, result);
 }
}