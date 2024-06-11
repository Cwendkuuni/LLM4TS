import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class GnuParserTest {

 @Test
 public void testFlattenBasic() {
 Options options = new Options();
 String[] args = new String[]{"-Dfoo=bar", "-o", "out.txt", "arg1", "--"};
 GnuParser parser = new GnuParser();
 String[] flattenedArgs = parser.flatten(options, args, false);
 assertArrayEquals(new String[]{"-D", "foo", "bar", "-o", "out.txt", "arg1"}, flattenedArgs);
 }

 @Test
 public void testFlattenWithDuplicateOption() {
 Options options = new Options();
 String[] args = new String[]{"-Dfoo=bar", "-o", "--foo", "arg1"};
 GnuParser parser = new GnuParser();
 String[] flattenedArgs = parser.flatten(options, args, false);
 assertArrayEquals(new String[]{"-D", "foo", "bar", "-o", "--", "foo"}, flattenedArgs);
 }

 @Test(expected = ParseException.class)
 public void testFlattenWithNonOption() {
 Options options = new Options();
 String[] args = new String[]{"-Dfoo=bar", "-o", "arg1", "val1"};
 GnuParser parser = new GnuParser();
 parser.flatten(options, args, false);
 fail("Expected ParseException when encountering non-option 'val1'");
 }

 @Test
 public void testFlattenWithLongOption() {
 Options options = new Options();
 String[] args = new String[]{"-Dfoo=bar", "--long-option", "arg1"};
 GnuParser parser = new GnuParser();
 String[] flattenedArgs = parser.flatten(options, args, false);
 assertArrayEquals(new String[]{"-D", "foo", "bar", "--long-option", "arg1"}, flattenedArgs);
 }

 @Test
 public void testFlattenWithMultipleLongOptions() {
 Options options = new Options();
 String[] args = new String[]{"-Dfoo=bar", "--long-option1", "--long-option2", "arg1"};
 GnuParser parser = new GnuParser();
 String[] flattenedArgs = parser.flatten(options, args, false);
 assertArrayEquals(new String[]{"-D", "foo", "bar", "--long-option1", "--long-option2", "arg1"}, flattenedArgs);
 }

 @Test
 public void testFlattenWithStopAtNonOption() {
 Options options = new Options();
 String[] args = new String[]{"-Dfoo=bar", "-o", "arg1", "val1"};
 GnuParser parser = new GnuParser();
 String[] flattenedArgs = parser.flatten(options, args, true);
 assertArrayEquals(new String[]{"-D", "foo", "bar", "-o", "arg1"}, flattenedArgs);
 }

 @Test
 public void testFlattenWithDoubleDash() {
 Options options = new Options();
 String[] args = new String[]{"--", "arg1", "arg2"};
 GnuParser parser = new GnuParser();
 String[] flattenedArgs = parser.flatten(options, args, false);
 assertArrayEquals(new String[]{"--", "arg1", "arg2"}, flattenedArgs);
 }
}