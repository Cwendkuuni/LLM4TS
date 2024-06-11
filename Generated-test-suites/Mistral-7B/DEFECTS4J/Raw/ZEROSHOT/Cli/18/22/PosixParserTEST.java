import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

public class PosixParserTest {

 private Options options;
 private String[] args;
 private PosixParser parser;

 @Before
 public void setUp() {
 options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B with an argument");
 options.addOption("c", 3, "Option C with three arguments");
 }

 @Test
 public void testFlattenSimple() throws ParseException {
 parser = new PosixParser();
 args = new String[]{"--a", "arg1", "arg2", "--b", "value", "--c", "arg1", "arg2", "arg3"};
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEquals(new String[]{"--a", "arg1", "arg2", "--b", "value", "--c", "arg1", "arg2", "arg3"}, result);
 }

 @Test
 public void testFlattenStopAtNonOption() throws ParseException {
 parser = new PosixParser();
 args = new String[]{"--a", "arg1", "arg2", "-c", "arg1", "arg2", "arg3"};
 String[] result = parser.flatten(options, args, true);
 Assert.assertArrayEquals(new String[]{"--a", "arg1", "arg2", "-c", "arg1", "arg2", "arg3"}, result);
 }

 @Test
 public void testFlattenBursting() throws ParseException {
 parser = new PosixParser();
 args = new String[]{"--abc", "arg1", "arg2", "arg3"};
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEquals(new String[]{"--a", "--b", "arg1", "--c", "arg2", "arg3"}, result);
 }

 @Test
 public void testFlattenBurstingWithStopAtNonOption() throws ParseException {
 parser = new PosixParser();
 args = new String[]{"--abc", "arg1", "arg2", "arg3", "--d"};
 String[] result = parser.flatten(options, args, true);
 Assert.assertArrayEquals(new String[]{"--abc", "--d"}, result);
 }

}