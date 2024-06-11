import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;

@RunWith(JUnit4.class)
public class PosixParserTest {

 private Options options;

 @Before
 public void setup() {
 options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 2, "Option B with an argument");
 options.addOption("c", 3, "Option C with an optional argument");
 }

 @Test
 public void testFlatten_EmptyArguments() {
 String[] arguments = {};
 String[] expected = {};
 String[] result = new PosixParser().flatten(options, arguments, false);
 Assert.assertThat(result, is(equalTo(expected)));
 }

 @Test
 public void testFlatten_SimpleOption() {
 String[] arguments = {"--a"};
 String[] expected = {"--a"};
 String[] result = new PosixParser().flatten(options, arguments, false);
 Assert.assertThat(result, is(equalTo(expected)));
 }

 @Test
 public void testFlatten_SingleHyphen() {
 String[] arguments = {"-"};
 String[] expected = {"-"};
 String[] result = new PosixParser().flatten(options, arguments, false);
 Assert.assertThat(result, is(equalTo(expected)));
 }

 @Test
 public void testFlatten_SimpleOptionWithArgument() {
 String[] arguments = {"--a=value"};
 String[] expected = {"--a", "value"};
 String[] result = new PosixParser().flatten(options, arguments, false);
 Assert.assertThat(result, is(equalTo(expected)));
 }

 @Test
 public void testFlatten_MultipleOptions() {
 String[] arguments = {"--a", "--b=value", "--c=value2"};
 String[] expected = {"--a", "--b=value", "--c=value2"};
 String[] result = new PosixParser().flatten(options, arguments, false);
 Assert.assertThat(result, is(equalTo(expected)));
 }

 @Test
 public void testFlatten_OptionWithLongId() {
 String[] arguments = {"--long-option-id"};
 String[] expected = {"--long-option-id"};
 String[] result = new PosixParser().flatten(options, arguments, false);
 Assert.assertThat(result, is(equalTo(expected)));
 }

 @Test
 public void testFlatten_OptionWithArgAndValue() {
 String[] arguments = {"--option-with-arg=value"};
 String[] expected = {"--option-with-arg", "value"};
 String[] result = new PosixParser().flatten(options, arguments, false);
 Assert.assertThat(result, is(equalTo(expected)));
 }

 @Test
 public void testFlatten_OptionWithOptionalArg() {
 String[] arguments = {"--option-with-optional-arg=value"};
 String[] expected = {"--option-with-optional-arg", "value"};
 String[] result = new PosixParser().flatten(options, arguments, false);
 Assert.assertThat(result, is(equalTo(expected)));
 }

 @Test
 public void testFlatten_OptionWithOptionalArg_NoValue() {
 String[] arguments = {"--option-with-optional-arg"};
 String[] expected = {"--option-with-optional-arg"};
 String[] result = new PosixParser().flatten(options, arguments, false);
 Assert.assertThat(result, is(equalTo(expected)));
 }

 @Test
 public void testFlatten_StopAtNonOption() {
 String[] arguments = {"--a", "--b", "nonOption"};
 String[] expected = {"--a", "--b"};
 String[] result = new PosixParser().flatten(options, arguments, true);
 Assert.assertThat(result, is(equalTo(expected)));
 }

 @Test
 public void testFlatten_StopAtNonOption_MultipleNonOptions() {
 String[] arguments = {"--a", "--b", "nonOption1", "nonOption2"};
 String[] expected = {"--a", "--b"};
 String[] result = new PosixParser().flatten(options, arguments, true);
 Assert.assertThat(result, is(equalTo(expected)));
 }

 @Test(expected = ParseException.class)
 public void testFlatten_InvalidOption() {
 String[] arguments = {"--invalidOption"};
 String[] expected = {};
 String[] result = new PosixParser().flatten(options, arguments, false);
 Assert.assertThat(result, is(notNullValue()));
 Assert.assertThat(result.length, is(equalTo(expected.length)));
 }

 @Test(expected = ParseException.class)
 public void testFlatten_LongArgWithSingleHyphen() {
 String[] arguments = {"longArg--"};
 String[] expected = {};
 String[] result = new PosixParser().flatten(options, arguments, false);
 Assert.assertThat(result, is(notNullValue()));
 Assert.assertThat(result.length, is(equalTo(expected.length)));
 }

 @Test(expected = ParseException.class)
 public void testFlatten_LongArgWithTwoHyphens() {
 String[] arguments = {"--longArg---"};
 String[] expected = {};
 String[] result = new PosixParser().flatten(options, arguments, false);
 Assert.assertThat(result, is(notNullValue()));
 Assert.assertThat(result.length, is(equalTo(expected.length)));
 }

 @Test(expected = ParseException.class)
 public void testFlatten_InvalidLongArg() {
 String[] arguments = {"--longArgInvalid"};
 String[] expected = {};
 String[] result = new PosixParser().flatten(options, arguments, false);
 Assert.assertThat(result, is(notNullValue()));
 Assert.assertThat(result.length, is(equalTo(expected.length)));
 }

 @Test
 public void testFlatten_BurstOption() {
 String[] arguments = {"--option-with-long-id"};
 String[] expected = {"--option-with-long-id"};
 String[] result = new PosixParser().flatten(options, arguments, false);
 Assert.assertThat(result, is(equalTo(expected)));
 }

 @Test
 public void testFlatten_BurstOptionWithArg() {
 String[] arguments = {"--option-with-long-id=value"};
 String[] expected = {"--option-with-long-id", "value"};
 String[] result = new PosixParser().flatten(options, arguments, false);
 Assert.assertThat(result, is(equalTo(expected)));
 }

 @Test
 public void testFlatten_BurstOptionWithMultipleArgs() {
 String[] arguments = {"--option-with-long-id=value1 value2"};
 String[] expected = {"--option-with-long-id", "value1", "value2"};
 String[] result = new PosixParser().flatten(options, arguments, false);
 Assert.assertThat(result, is(equalTo(expected)));
 }

 @Test(expected = ParseException.class)
 public void testFlatten_BurstInvalidOption() {
 String[] arguments = {"--invalidOptionWithLongId"};
 String[] expected = {};
 String[] result = new PosixParser().flatten(options, arguments, false);
 Assert.assertThat(result, is(notNullValue()));
 Assert.assertThat(result.length, is(equalTo(expected.length)));
 }

 @Test(expected = ParseException.class)
 public void testFlatten_BurstOptionWithTwoHyphens() {
 String[] arguments = {"--option-with-long-id--"};
 String[] expected = {};
 String[] result = new PosixParser().flatten(options, arguments, false);
 Assert.assertThat(result, is(notNullValue()));
 Assert.assertThat(result.length, is(equalTo(expected.length)));
 }

 @Test(expected = ParseException.class)
 public void testFlatten_BurstOptionWithSingleHyphen() {
 String[] arguments = {"--option-with-long-id-"};
 String[] expected = {};
 String[] result = new PosixParser().flatten(options, arguments, false);
 Assert.assertThat(result, is(notNullValue()));
 Assert.assertThat(result.length, is(equalTo(expected.length)));
 }
}