import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(JUnit4.class)
public class PosixParserTest {

 private Options options;
 private PosixParser parser;

 @Before
 public void setup() {
 options = new Options();
 options.addOption("a", "alpha", false, "An option with no argument.");
 options.addOption("b", "beta", true, "An option that requires an argument.");
 }

 @Test
 public void testFlattenEmptyArguments() {
 String[] arguments = new String[0];
 String[] expected = new String[0];
 String[] actual = parser.flatten(options, arguments, false);
 assertEquals(expected, actual);
 }

 @Test
 public void testFlattenLongOption() {
 String[] arguments = new String[]{"--foo"};
 String[] expected = new String[]{"--foo"};
 String[] actual = parser.flatten(options, arguments, false);
 assertEquals(expected, actual);
 }

 @Test
 public void testFlattenSingleHyphen() {
 String[] arguments = new String[]{"-"};
 String[] expected = new String[]{"-"};
 String[] actual = parser.flatten(options, arguments, false);
 assertEquals(expected, actual);
 }

 @Test
 public void testFlattenSingleLetterOption() {
 String[] arguments = new String[]{"-a"};
 String[] expected = new String[]{"-a"};
 String[] actual = parser.flatten(options, arguments, false);
 assertEquals(expected, actual);
 }

 @Test
 public void testFlattenTwoCharacterOption() {
 String[] arguments = new String[]{"-ab"};
 String[] expected = new String[]{"-a", "-b"};
 String[] actual = parser.flatten(options, arguments, false);
 assertEquals(expected, actual);
 }

 @Test
 public void testFlattenTwoCharacterOptionWithArgument() {
 String[] arguments = new String[]{"-ab", "arg"};
 String[] expected = new String[]{"-a", "-b", "arg"};
 String[] actual = parser.flatten(options, arguments, false);
 assertEquals(expected, actual);
 }

 @Test
 public void testFlattenLongOptionWithArgument() {
 String[] arguments = new String[]{"--alpha", "arg"};
 String[] expected = new String[]{"--alpha", "arg"};
 String[] actual = parser.flatten(options, arguments, false);
 assertEquals(expected, actual);
 }

 @Test
 public void testFlattenStopAtNonOption() {
 String[] arguments = new String[]{"-a", "--non-option", "-b"};
 String[] expected = new String[]{"-a", "--non-option", "-b"};
 String[] actual = parser.flatten(options, arguments, true);
 assertEquals(expected, actual);
 }

 @Test(expected = ParseException.class)
 public void testFlattenUnknownOption() {
 String[] arguments = new String[]{"-x"};
 try {
 parser.flatten(options, arguments, false);
 } catch (ParseException e) {
 // expected
 }
 }

 @Test(expected = ParseException.class)
 public void testFlattenUnknownOptionWithArguments() {
 String[] arguments = new String[]{"-x", "arg"};
 try {
 parser.flatten(options, arguments, false);
 } catch (ParseException e) {
 // expected
 }
 }

 @Test(expected = ParseException.class)
 public void testFlattenMultipleUnknownOptions() {
 String[] arguments = new String[]{"-x", "-y"};
 try {
 parser.flatten(options, arguments, false);
 } catch (ParseException e) {
 // expected
 }
 }

 @Test(expected = ParseException.class)
 public void testFlattenUnknownOptionWithArgumentsAndStopAtNonOption() {
 String[] arguments = new String[]{"-x", "arg"};
 try {
 parser.flatten(options, arguments, true);
 } catch (ParseException e) {
 // expected
 }
 }

 @Test(expected = ParseException.class)
 public void testFlattenMultipleUnknownOptionsWithArguments() {
 String[] arguments = new String[]{"-x", "arg", "-y", "arg"};
 try {
 parser.flatten(options, arguments, false);
 } catch (ParseException e) {
 // expected
 }
 }

 @Test
 public void testFlattenBurstToken() {
 String[] arguments = new String[]{"-aba"};
 String[] expected = new String[]{"-a", "-a", "b"};
 String[] actual = parser.flatten(options, arguments, false);
 assertEquals(expected, actual);
 }

 @Test
 public void testFlattenBurstTokenWithArgument() {
 String[] arguments = new String[]{"-abac", "arg"};
 String[] expected = new String[]{"-a", "-b", "a", "c", "arg"};
 String[] actual = parser.flatten(options, arguments, false);
 assertEquals(expected, actual);
 }

 @Test
 public void testFlattenBurstTokenWithStopAtNonOption() {
 String[] arguments = new String[]{"-ab", "--non-option", "-ac"};
 String[] expected = new String[]{"-a", "-b", "--non-option", "-c"};
 String[] actual = parser.flatten(options, arguments, true);
 assertEquals(expected, actual);
 }

 @Test(expected = ParseException.class)
 public void testFlattenBurstTokenWithUnknownOption() {
 String[] arguments = new String[]{"-xba"};
 try {
 parser.flatten(options, arguments, false);
 } catch (ParseException e) {
 // expected
 }
 }

 @Test(expected = ParseException.class)
 public void testFlattenBurstTokenWithUnknownOptionAndArguments() {
 String[] arguments = new String[]{"-xba", "arg"};
 try {
 parser.flatten(options, arguments, false);
 } catch (ParseException e) {
 // expected
 }
 }

 @Test(expected = ParseException.class)
 public void testFlattenBurstTokenWithUnknownOptionAndStopAtNonOption() {
 String[] arguments = new String[]{"-xba", "--non-option"};
 try {
 parser.flatten(options, arguments, true);
 } catch (ParseException e) {
 // expected
 }
 }
}