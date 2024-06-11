import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.arguments.Argument;
import org.apache.commons.cli2.options.PropertyOption;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

 private WriteableCommandLineImpl cmd;

 @Test
 public void testWriteableCommandLineImpl() {
 cmd = new WriteableCommandLineImpl(new Option.Builder("opt").build(), new ArrayList<>());
 cmd.addOption(new Option.Builder("opt1").build());
 cmd.addOption(new Option.Builder("opt2").longOpt("longOpt2").build());
 cmd.addOption(new Option.Builder("opt3").longOpt("longOpt3").valueSeparator('=').build());
 cmd.addOption(new PropertyOption("prop1", true).build());
 cmd.addOption(new PropertyOption("prop2", true).build());

 assertEquals("", cmd.toString());
 }

 @Test
 public void testAddOption() {
 cmd = new WriteableCommandLineImpl(new Option.Builder("opt").build(), new ArrayList<>());
 Option opt1 = new Option.Builder("opt1").build();
 Option opt2 = new Option.Builder("opt2").longOpt("longOpt2").build();
 Option opt3 = new Option.Builder("opt3").longOpt("longOpt3").valueSeparator('=').build();
 PropertyOption prop1 = new PropertyOption("prop1", true);
 PropertyOption prop2 = new PropertyOption("prop2", true);

 cmd.addOption(opt1);
 cmd.addOption(opt2);
 cmd.addOption(opt3);
 cmd.addOption(prop1);
 cmd.addOption(prop2);

 assertEquals("", cmd.toString());
 }

 @Test
 public void testAddValue() {
 cmd = new WriteableCommandLineImpl(new Option.Builder("opt").build(), new ArrayList<>());
 Option opt = new Option.Builder("opt").argName("arg").build();
 Argument arg = new Argument("1").build();

 cmd.addOption(opt);
 cmd.addValue(opt, arg);

 assertEquals("[1]", cmd.toString());
 }

 @Test
 public void testAddSwitch() {
 cmd = new WriteableCommandLineImpl(new Option.Builder("opt").build(), new ArrayList<>());
 Option opt = new Option.Builder("opt").build();

 cmd.addOption(opt);
 cmd.addSwitch(opt, true);

 assertEquals("--opt", cmd.toString());
 }

 @Test
 public void testHasOption() {
 cmd = new WriteableCommandLineImpl(new Option.Builder("opt").build(), new ArrayList<>());
 Option opt1 = new Option.Builder("opt1").build();
 Option opt2 = new Option.Builder("opt2").longOpt("longOpt2").build();

 cmd.addOption(opt1);
 cmd.addOption(opt2);

 assertTrue(cmd.hasOption(opt1));
 assertTrue(cmd.hasOption(opt2));
 }

 @Test
 public void testGetOption() {
 cmd = new WriteableCommandLineImpl(new Option.Builder("opt").build(), new ArrayList<>());
 Option opt1 = new Option.Builder("opt1").build();
 Option opt2 = new Option.Builder("opt2").longOpt("longOpt2").build();

 cmd.addOption(opt1);
 cmd.addOption(opt2);

 assertEquals(opt1, cmd.getOption("opt1"));
 assertEquals(opt2, cmd.getOption("longOpt2"));
 }

 @Test
 public void testGetValues() {
 cmd = new WriteableCommandLineImpl(new Option.Builder("opt").argName("arg").build(), new ArrayList<>());
 Option opt = new Option.Builder("opt").argName("arg").build();
 Argument arg1 = new Argument("1");
 Argument arg2 = new Argument("2");

 cmd.addOption(opt);
 cmd.addValue(opt, arg1);
 cmd.addValue(opt, arg2);

 List values = cmd.getValues(opt, null);

 assertEquals(2, values.size());
 assertEquals("1", values.get(0).toString());
 assertEquals("2", values.get(1).toString());
 }

 @Test
 public void testGetUndefaultedValues() {
 cmd = new WriteableCommandLineImpl(new Option.builder("opt").argName("arg").build(), new ArrayList<>());
 Option opt = new Option.builder("opt").argName("arg").build();
 Argument arg1 = new Argument("1");
 Argument arg2 = new Argument("2");
 cmd.addOption(opt);
 cmd.addValue(opt, arg1);
 cmd.addValue(opt, arg2);

 List values = cmd.getUndefaultedValues(opt);

 assertEquals(0, values.size());
 }

 @Test
 public void testGetSwitch() {
 cmd = new WriteableCommandLineImpl(new Option.builder("opt").build(), new ArrayList<>());
 Option opt = new Option.builder("opt").build();

 cmd.addOption(opt);
 cmd.addSwitch(opt, true);
 cmd.addSwitch(opt, false);

 assertTrue(cmd.getSwitch(opt, false));
 assertFalse(cmd.getSwitch(opt, true));
 }

 @Test
 public void testGetProperty() {
 cmd = new WriteableCommandLineImpl(new Option.builder("opt").build(), new ArrayList<>());
 Option opt = new Option.builder("opt").build();
 PropertyOption prop = new PropertyOption("prop", true);

 cmd.addOption(opt);
 cmd.addOption(prop);
 cmd.addProperty(opt, "prop", "value");

 assertEquals("value", cmd.getProperty("opt.prop"));
 }

 @Test
 public void testGetProperties() {
 cmd = new WriteableCommandLineImpl(new Option.builder("opt").build(), new ArrayList<>());
 Option opt = new Option.builder("opt").build();
 PropertyOption prop1 = new PropertyOption("prop1", true);
 PropertyOption prop2 = new PropertyOption("prop2", true);

 cmd.addOption(opt);
 cmd.addOption(prop1);
 cmd.addOption(prop2);
 cmd.addProperty(opt, "prop1", "value1");
 cmd.addProperty(opt, "prop2", "value2");

 Set properties = cmd.getProperties(opt);

 assertEquals(2, properties.size());
 assertTrue(properties.contains("prop1"));
 assertTrue(properties.contains("prop2"));
 }

 @Test
 public void testLooksLikeOption() {
 cmd = new WriteableCommandLineImpl(new Option.builder("opt").build(), new ArrayList<>());

 assertTrue(cmd.looksLikeOption("opt"));
 assertTrue(cmd.looksLikeOption("opt--longOpt"));
 assertTrue(cmd.looksLikeOption("opt--longOpt="));
 assertFalse(cmd.looksLikeOption("arg"));
 }

 @Test
 public void testToString() {
 cmd = new WriteableCommandLineImpl(new Option.builder("opt").build(), new ArrayList<>());
 Option opt1 = new Option.builder("opt1").build();
 Option opt2 = new Option.builder("opt2").longOpt("longOpt2").build();
 Argument arg1 = new Argument("arg1");
 Argument arg2 = new Argument("arg2");

 cmd.addOption(opt1);
 cmd.addOption(opt2);
 cmd.addValue(opt1, arg1);
 cmd.addValue(opt2, arg2);
 cmd.addProperty(opt1, "prop", "value");

 assertEquals("opt opt1=\"arg1\" opt2 --longOpt2=\"arg2\" --prop=value", cmd.toString());
 }

 @Test
 public void testGetOptions() {
 cmd = new WriteableCommandLineImpl(new Option.builder("opt").build(), new ArrayList<>());
 Option opt1 = new Option.builder("opt1").build();
 Option opt2 = new Option.builder("opt2").longOpt("longOpt2").build();

 cmd.addOption(opt1);
 cmd.addOption(opt2);

 assertEquals(2, cmd.getOptions().size());
 assertTrue(cmd.getOptions().contains(opt1));
 assertTrue(cmd.getOptions().contains(opt2));
 }

 @Test
 public void testGetOptionTriggers() {
 cmd = new WriteableCommandLineImpl(new Option.builder("opt").build(), new ArrayList<>());
 Option opt1 = new Option.builder("opt1").build();
 Option opt2 = new Option.builder("opt2").longOpt("longOpt2").build();

 cmd.addOption(opt1);
 cmd.addOption(opt2);

 assertEquals(2, cmd.getOptionTriggers().size());
 assertTrue(cmd.getOptionTriggers().containsAll(Collections.singletonList("opt1")));
 assertTrue(cmd.getOptionTriggers().containsAll(Collections.singletonList("longOpt2")));
 }
}