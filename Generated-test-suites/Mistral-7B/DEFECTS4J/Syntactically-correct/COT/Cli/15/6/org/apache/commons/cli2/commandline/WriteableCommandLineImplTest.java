import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

 private WriteableCommandLineImpl commandLine;

 @Test
 public void testAddOption() {
 commandLine = new WriteableCommandLineImpl(new Option.Builder("opt").build(), new ArrayList());
 commandLine.addOption(new Option.Builder("opt2").build());
 Assert.assertEquals(2, commandLine.getOptions().size());
 }

 @Test
 public void testAddValue() {
 Option option = new Option.Builder("opt").build();
 commandLine = new WriteableCommandLineImpl(option, new ArrayList());
 commandLine.addValue(option, "value1");
 commandLine.addValue(option, "value2");
 Assert.assertEquals(Arrays.asList("value1", "value2"), commandLine.getValues(option, null));
 }

 @Test
 public void testAddSwitch() {
 Option option = new Option.Builder("opt").build();
 commandLine = new WriteableCommandLineImpl(option, new ArrayList());
 commandLine.addSwitch(option, true);
 Assert.assertTrue(commandLine.getSwitch(option, false));
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchAlreadySet() {
 Option option = new Option.Builder("opt").build();
 commandLine = new WriteableCommandLineImpl(option, new ArrayList());
 commandLine.addSwitch(option, true);
 commandLine.addSwitch(option, true);
 }

 @Test
 public void testHasOption() {
 Option option = new Option.Builder("opt").build();
 commandLine = new WriteableCommandLineImpl(option, new ArrayList());
 commandLine.addOption(option);
 Assert.assertTrue(commandLine.hasOption(option));
 }

 @Test
 public void testGetOption() {
 Option option = new Option.Builder("opt").build();
 commandLine = new WriteableCommandLineImpl(option, new ArrayList());
 commandLine.addOption(option);
 Assert.assertSame(option, commandLine.getOption("opt"));
 }

 @Test
 public void testGetValues() {
 Option option = new Option.Builder("opt").build();
 commandLine = new WriteableCommandLineImpl(option, new ArrayList());
 commandLine.addValue(option, "value1");
 commandLine.addValue(option, "value2");
 Assert.assertEquals(Arrays.asList("value1", "value2"), commandLine.getValues(option, null));
 }

 @Test
 public void testGetUndefaultedValues() {
 Option option = new Option.Builder("opt").build();
 commandLine = new WriteableCommandLineImpl(option, new ArrayList());
 commandLine.addValue(option, "value1");
 Assert.assertEquals(Collections.emptyList(), commandLine.getUndefaultedValues(option));
 }

 @Test
 public void testGetSwitch() {
 Option option = new Option.Builder("opt").build();
 commandLine = new WriteableCommandLineImpl(option, new ArrayList());
 commandLine.addSwitch(option, true);
 Assert.assertTrue(commandLine.getSwitch(option, false));
 }

 @Test
 public void testGetProperty() {
 Option option = new OptionBuilder("opt").hasArg(true).build();
 commandLine = new WriteableCommandLineImpl(option, new ArrayList());
 commandLine.addProperty(option, "property", "value");
 Assert.assertEquals("value", commandLine.getProperty("opt.property"));
 }

 @Test
 public void testAddProperty() {
 Option option = new OptionBuilder("opt").hasArg(true).build();
 commandLine = new WriteableCommandLineImpl(option, new ArrayList());
 commandLine.addProperty(option, "property", "value1");
 commandLine.addProperty(option, "property", "value2");
 Assert.assertEquals("value2", commandLine.getProperty("opt.property"));
 }

 @Test
 public void testLooksLikeOption() {
 commandLine = new WriteableCommandLineImpl(null, new ArrayList());
 commandLine.setNormalised(Arrays.asList("--opt", "--otheropt", "--opt2"));
 Assert.assertTrue(commandLine.looksLikeOption("--opt"));
 }

 @Test
 public void testToString() {
 commandLine = new WriteableCommandLineImpl(new Option.Builder("opt").build(), new ArrayList());
 commandLine.setNormalised(Arrays.asList("--opt", "value1", "value2"));
 Assert.assertEquals("--opt value1 value2", commandLine.toString());
 }

 @Test
 public void testGetOptions() {
 commandLine = new WriteableCommandLineImpl(new Option.Builder("opt").build(), new ArrayList());commandLine.addOption(new Option.Builder("opt2").build());
 Assert.assertEquals(Arrays.asList(new Option[] { new Option("opt"), new Option("opt2") }), commandLine.getOptions());
 }

 @Test
 public void testGetOptionTriggers() {
 commandLine = new WriteableCommandLineImpl(new Option.Builder("opt").build(), new ArrayList());
 commandLine.addOption(new Option.Builder("opt2").build());
 Assert.assertEquals(Arrays.asList("opt", "opt2"), commandLine.getOptionTriggers());
 }

 @Test
 public void testSetDefaultValues() {
 Option option = new Option.Builder("opt").hasArg(true).build();
 commandLine = new WriteableCommandLineImpl(option, new ArrayList());
 commandLine.addProperty(option, "property", "defaultValue");
 commandLine.setDefaultValues(option, Arrays.asList("defaultValue2", "defaultValue3"));
 Assert.assertEquals(Arrays.asList("defaultValue2", "defaultValue3"), commandLine.getValues(option, Arrays.asList("defaultValue")));
 }

 @Test
 public void testSetDefaultSwitch() {
 Option option = new Option.Builder("opt").build();
 commandLine = new WriteableCommandLineImpl(option, new ArrayList());
 commandLine.setDefaultSwitch(option, false);
 Assert.assertFalse(commandLine.getSwitch(option, true));
 }

 @Test
 public void testGetNormalised() {
 commandLine = new WriteableCommandLineImpl(new Option.Builder("opt").build(), new ArrayList());
 commandLine.setNormalised(Arrays.asList("--opt", "value1", "value2"));
 Assert.assertEquals(Arrays.asList("--opt", "value1", "value2"), commandLine.getNormalised());
 }
}