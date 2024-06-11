import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.Parser;
import org.apache.commons.cli2.exceptions.CannotParseException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;

public class PatternBuilderTest {

 private PatternBuilder pb;

 @Before
 public void setUp() {
 pb = new PatternBuilder();
 }

 @Test
 public void testBasicOption() {
 final Option option = pb.withPattern("-o,--output");
 assertTrue(option.isSettable());
 assertEquals("output", option.getLongOpt());
 assertEquals("o", option.getShortName());
 assertFalse(option.isRequired());
 }

 @Test
 public void testRequiredOption() {
 final Option option = pb.withPattern("-i,--input !");
 assertTrue(option.isSettable());
 assertEquals("input", option.getLongOpt());
 assertEquals("i", option.getShortName());
 assertTrue(option.isRequired());
 }

 @Test
 public void testClassOption() {
 final Option option = pb.withPattern("-c,--class:java.lang.String");
 assertTrue(option.isSettable());
 assertEquals("class", option.getLongOpt());
 assertEquals("c", option.getShortName());
 assertTrue(option.isRequired());
 }

 @Test
 public void testNumberOption() {
 final Option option = pb.withPattern("-n,--number %");
 assertTrue(option.isSettable());
 assertEquals("number", option.getLongOpt());
 assertEquals("n", option.getShortName());
 assertFalse(option.isRequired());
 }

 @Test
 public void testDateOption() {
 final Option option = pb.withPattern("-d,--date /");
 assertTrue(option.isSettable());
 assertEquals("date", option.getLongOpt());
 assertEquals("d", option.getShortName());
 assertFalse(option.isRequired());
 }

 @Test
 public void testFileOption() {
 final Option option = pb.withPattern("-f,--file <");
 assertTrue(option.isSettable());
 assertEquals("file", option.getLongOpt());
 assertEquals("f", option.getShortName());
 assertTrue(option.isRequired());
 }

 @Test
 public void testInvalidPattern() {
 try {
 pb.withPattern("!@#%^&*");
 fail("Expected an exception");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testParse() throws CannotParseException, ParseException {
 final Parser parser = new DefaultParser();
 final CommandLineParser clp = new DefaultCommandLineParser();
 final Option option = pb.withPattern("i:j,--input:java.lang.Integer:j");
 final Option option2 = pb.withPattern("o:,--output:");
 final Option option3 = pb.withPattern("f:,--file:");
 final Option option4 = pb.withPattern("d:,--date:/");
 final Option option5 = pb.withPattern("c:,--class:");

 pb.reset();
 pb.withPattern("i:j,--input:java.lang.Integer:j");
 pb.withPattern("o:,--output:");
 pb.withPattern("f:,--file:");
 pb.withPattern("d:,--date:/");
 pb.withPattern("c:,--class:");

 final String[] args = new String[]{"-i", "10", "-o", "out.txt", "-f", "input.txt", "-d", "2022-01-01", "-c", "String"};
 final CommandLine cmd = parser.parse(pb.create(), args);

 assertEquals(args.length, cmd.options().size());

 final Option o1 = cmd.getOption("input");
 assertNotNull(o1);
 assertEquals("10", o1.getValue());

 final Option o2 = cmd.getOption("output");
 assertNotNull(o2);
 assertEquals("out.txt", o2.getValue());

 final Option o3 = cmd.getOption("file");
 assertNotNull(o3);
 assertEquals("input.txt", o3.getValue());

 final Option o4 = cmd.getOption("date");
 assertNotNull(o4);
 assertNotNull(o4.getValue());
 final Date date = (Date)o4.getValue();
 assertEquals(2022, date.getYear());
 assertEquals(1, date.getMonth());
 assertEquals(1, date.getDate());

 final Option o5 = cmd.getOption("class");
 assertNotNull(o5);
 assertNotNull(o5.getValue());
 assertEquals("String", o5.getValue());
 }

 @Test
 public void testParseWithInvalidOptions() throws CannotParseException {
 final Parser parser = new DefaultParser();
 final CommandLineParser clp = new DefaultCommandLineParser();
 final Option option = pb.withPattern("i:j,--input:java.lang.Integer:j");
 final Option option2 = pb.withPattern("o:,--output:");
 final Option option3 = pb.withPattern("f:,--file:");
 final Option option4 = pb.withPattern("d:,--date:/");
 final Option option5 = pb.withPattern("c:,--class:");

 pb.reset();
 pb.withPattern("i:j,--input:java.lang.Integer:j");
 pb.withPattern("o:,--output:");
 pb.withPattern("f:,--file:");
 pb.withPattern("d:,--date:/");
 pb.withPattern("c:,--class:");

 final String[] args = new String[]{"-i", "10", "-o", "out.txt", "-f", "input.txt", "-d", "2022-01-01", "-c", "String", "--invalid"};
 try {
 final CommandLine cmd = parser.parse(pb.create(), args);
 fail("Expected an exception");
 } catch (CannotParseException e) {
 // expected
 }
 }
}