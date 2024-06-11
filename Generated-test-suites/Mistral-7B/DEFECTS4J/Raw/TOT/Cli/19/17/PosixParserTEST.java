import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

 @Test
 public void testFlattenSimple() {
 Options options = new Options();
 String[] arguments = {"--foo", "-o", "bar", "-a"};
 PosixParser parser = new PosixParser();
 String[] parsedArgs = parser.flatten(options, arguments, false);
 assertArrayEquals(new String[]{"--foo", "-o", "bar", "-a"}, parsedArgs);
 }

 @Test
 public void testFlattenWithArgs() {
 Options options = new Options();
 Option option = new Option("o", "output", true, "default output");
 options.addOption(option);
 String[] arguments = {"--foo", "-o", "output.txt", "-a"};
 PosixParser parser = new PosixParser();
 String[] parsedArgs = parser.flatten(options, arguments, false);
 assertArrayEquals(new String[]{"--foo", "--output", "output.txt", "-a"}, parsedArgs);
 }

 @Test
 public void testFlattenStopAtNonOption() {
 Options options = new Options();
 String[] arguments = {"--foo", "-o", "bar", "-a", "--"};
 PosixParser parser = new PosixParser();
 String[] parsedArgs = parser.flatten(options, arguments, true);
 assertArrayEquals(new String[]{"--foo", "-o", "bar", "--", "-a"}, parsedArgs);
 }

 @Test
 public void testFlattenNoOption() {
 Options options = new Options();
 String[] arguments = {"--foo", "bar"};
 PosixParser parser = new PosixParser();
 try {
 parser.flatten(options, arguments, false);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testFlattenInvalidOption() {
 Options options = new Options();
 String[] arguments = {"--invalid", "-o", "bar"};
 PosixParser parser = new PosixParser();
 try {
 parser.flatten(options, arguments, false);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testFlattenBurstToken() {
 Options options = new Options();
 Option option = new Option("o", "output", true, "default output");
 options.addOption(option);
 String[] arguments = {"-foo-o", "output.txt", "-a"};
 PosixParser parser = new PosixParser();
 String[] parsedArgs = parser.flatten(options, arguments, false);
 assertArrayEquals(new String[]{"--foo", "--output", "output.txt", "-a"}, parsedArgs);
 }

 @Test
 public void testFlattenBurstTokenWithArgs() {
 Options options = new Options();
 Option option = new Option("o", "output", true, "default output");
 options.addOption(option);
 String[] arguments = {"-foo-o", "output.txt", "arg1", "arg2"};
 PosixParser parser = new PosixParser();
 String[] parsedArgs = parser.flatten(options, arguments, false);
 assertArrayEquals(new String[]{"--foo", "--output", "output.txt", "--", "arg1", "arg2"}, parsedArgs);
 }

}