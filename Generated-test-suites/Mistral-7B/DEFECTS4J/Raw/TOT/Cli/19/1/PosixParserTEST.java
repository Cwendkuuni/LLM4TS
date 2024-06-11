import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

 @Test
 public void testFlattenSimple() {
 Options options = new Options();
 String[] args = new String[]{"--foo", "bar", "-a", "--b=qux", "-c", "quux", "--", "baz"};
 PosixParser parser = new PosixParser();
 String[] flattened = parser.flatten(options, args, false);
 assertArrayEquals(new String[]{"--foo", "-", "foo", "--b", "-", "b", "qux", "-c", "-", "c", "--", "", "baz"}, flattened);
 }

 @Test
 public void testFlattenWithStopAtNonOption() {
 Options options = new Options();
 String[] args = new String[]{"--foo", "bar", "-a", "--b=qux", "-c", "quux", "--", "baz", "-x"};
 PosixParser parser = new PosixParser();
 String[] flattened = parser.flatten(options, args, true);
 assertArrayEquals(new String[]{"--foo", "-", "foo", "--b", "-", "b", "qux", "--", "baz", "-x"}, flattened);
 }

 @Test
 public void testFlattenWithInvalidOption() {
 Options options = new Options();
 String[] args = new String[]{"--foo", "bar", "-z", "--b=qux", "-c", "quux", "--", "baz"};
 PosixParser parser = new PosixParser();
 String[] flattened = parser.flatten(options, args, false);
 assertArrayEquals(new String[]{"--foo", "-", "foo", "--z", "--", "", "bar", "--b", "-", "b", "qux", "-c", "-", "c", "--", "", "baz"}, flattened);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFlattenWithInvalidOptionAndStopAtNonOption() {
 Options options = new Options();
 String[] args = new String[]{"--foo", "bar", "-z", "--b=qux", "-c", "quux", "--", "baz"};
 PosixParser parser = new PosixParser();
 parser.flatten(options, args, true);
 }

 @Test
 public void testFlattenWithOptionWithArg() {
 Options options = new Options();
 options.addOption("a", true, "An optional boolean option.");
 String[] args = new String[]{"--foo", "bar", "-a", "true", "--b=qux", "-c", "quux", "--", "baz"};
 PosixParser parser = new PosixParser();
 String[] flattened = parser.flatten(options, args, false);
 assertArrayEquals(new String[]{"--foo", "-", "foo", "--a", "-", "a", "true", "--b", "-", "b", "qux", "-c", "-", "c", "--", "", "baz"}, flattened);
 }

 @Test
 public void testFlattenWithOptionWithArgs() {
 Options options = new Options();
 options.addOption("a", null, "An optional option with multiple values.");
 String[] args = new String[]{"--foo", "bar", "-a", "one", "two", "three", "--b=qux", "-c", "quux", "--", "baz"};
 PosixParser parser = new PosixParser();
 String[] flattened = parser.flatten(options, args, false);
 assertArrayEquals(new String[]{"--foo", "-", "foo", "--a", "-", "a", "one", "-", "a", "two", "-", "a", "three", "--b", "-", "b", "qux", "-c", "-", "c", "--", "", "baz"}, flattened);
 }

 @Test
 public void testFlattenWithMultipleOptionsWithArg() {
 Options options = new Options();
 options.addOption("a", true, "An optional boolean option.");
 options.addOption("b", null, "An optional option with multiple values.");
 String[] args = new String[]{"--foo", "bar", "-a", "true", "--b", "one", "two", "--b", "three", "--b", "four", "--c=quux", "-d", "quux", "--", "baz"};
 PosixParser parser = new PosixParser();
 String[] flattened = parser.flatten(options, args, false);
 assertArrayEquals(new String[]{"--foo", "-", "foo", "--a", "-", "a", "true", "--b", "-", "b", "one", "-", "b", "two", "-", "b", "three", "-", "b", "four", "--c", "-", "c", "quux", "-d", "-", "d", "quux", "--", "", "baz"}, flattened);
 }
}