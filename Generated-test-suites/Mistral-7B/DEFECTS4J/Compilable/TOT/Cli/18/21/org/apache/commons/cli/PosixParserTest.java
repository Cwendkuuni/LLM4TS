package org.apache.commons.cli;
import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

 private String[] testArgs = new String[]{"--option1", "--option2=value2", "-o", "--option3", "-a", "arg1", "--option4=arg4"};

 @Test
 public void testFlatten() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 String[] result = parser.flatten(options, testArgs, false);

 assertArrayEquals(new String[]{"--option1", "-o", "--option3", "-a", "--option4=arg4"}, result);
 }

 @Test
 public void testFlattenWithStopAtNonOption() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 String[] result = parser.flatten(options, testArgs, true);

 assertArrayEquals(new String[]{"--option1", "--option2=value2", "-o", "--option3", "-a", "--option4=arg4"}, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNonExistentOption() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 String[] result = parser.flatten(options, new String[]{"--nonExistentOption"}, false);
 }

 @Test
 public void testLongOptionWithArgument() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("longOption", true, "lop");
 String[] result = parser.flatten(options, new String[]{"--longOption"}, false);

 assertEquals(new String[]{"--longOption"}, result);
 }

 @Test
 public void testLongOptionWithArgumentValue() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("longOption", true, "lop");
 String[] result = parser.flatten(options, new String[]{"--longOption=value"}, false);

 assertEquals(new String[]{"--longOption=value"}, result);
 }

 @Test
 public void testLongOptionWithArgumentValueAndAnotherOption() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("longOption1", true, "lo1");
 options.addOption("longOption2", true, "lo2");
 String[] result = parser.flatten(options, new String[]{"--longOption1", "--longOption2=value"}, false);

 assertEquals(new String[]{"--longOption1", "--longOption2=value"}, result);
 }

 @Test
 public void testBurstingSingleOption() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("o", true, "opt");
 String[] result = parser.flatten(options, new String[]{"-o"}, false);

 assertEquals(new String[]{"-o"}, result);
 }

 @Test
 public void testBurstingOptionWithValue() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("o", true, "opt");
 String[] result = parser.flatten(options, new String[]{"--o=val"}, false);

 assertEquals(new String[]{"--o=val"}, result);
 }

 @Test
 public void testBurstingLongOptionWithValue() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("lo", true, "lot");
 String[] result = parser.flatten(options, new String[]{"--lo=val"}, false);

 assertEquals(new String[]{"--lo=val"}, result);
 }

 @Test
 public void testBurstingLongOptionWithValueAndAnotherOption() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("lo1", true, "lo1");
 options.addOption("lo2", true, "lo2");
 String[] result = parser.flatten(options, new String[]{"--lo1", "--lo2=val"}, false);

 assertEquals(new String[]{"--lo1", "--lo2=val"}, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBurstingNonOption() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 String[] result = parser.flatten(options, new String[]{"arg"}, false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBurstingNonOptionWithStopAtNonOption() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 String[] result = parser.flatten(options, new String[]{"arg"}, true);
 }

 @Test
 public void testBurstingLongOptionWithValueAndAnotherLongOption() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("lo1", true, "lo1");
 options.addOption("lo2", true, "lo2");
 String[] result = parser.flatten(options, new String[]{"--lo1=val1", "--lo2=val2"}, false);

 assertEquals(new String[]{"--lo1=val1", "--lo2=val2"}, result);
 }

 @Test
 public void testBurstingLongOptionWithValueAndAnotherLongOptionWithStopAtNonOption() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("lo1", true, "lo1");
 options.addOption("lo2", true, "lo2");
 String[] result = parser.flatten(options, new String[]{"--lo1=val1", "--lo2=val2"}, true);

 assertEquals(new String[]{"--lo1=val1", "--lo2=val2"}, result);
 }
}