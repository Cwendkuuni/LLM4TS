import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

 @Test
 public void testWriteableCommandLineImpl() {
 WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(
 new Option("a", "optionA", true, "Description for optionA"),
 new ArrayList<String>()
 );
 commandLine.addOption(new Option("b", "optionB", false, "Description for optionB"));
 commandLine.addOption(new Option("c", "optionC", 1, "Description for optionC", new Argument("num", "number", true)));
 commandLine.addValue(commandLine.getOption("c"), "5");
 commandLine.addSwitch(commandLine.getOption("b"), true);
 commandLine.addProperty(commandLine.getOption("a"), "propertyA", "valueA");
 commandLine.addProperty(commandLine.getOption("c"), "propertyC", "valueC");

 assertEquals("optionA propertyA=valueA optionB true optionC propertyC=valueC 5", commandLine.toString());

 List options = commandLine.getOptions();
 assertEquals(3, options.size());
 Option optionA = (Option) options.get(0);
 Option optionB = (Option) options.get(1);
 Option optionC = (Option) options.get(2);

 assertTrue(optionA.isSettable());
 assertTrue(optionB.isSettable());
 assertTrue(optionC.isSettable());

 assertTrue(optionA.isRequired());
 assertFalse(optionB.isRequired());
 assertFalse(optionC.isRequired());

 assertEquals("optionA", optionA.getLongOpt());
 assertEquals("a", optionA.getOpt());
 assertEquals("Description for optionA", optionA.getDescription());
 assertEquals('a', optionA.getOptCharacter());
 assertEquals(true, optionA.getType() instanceof Argument.Type.UNBOUND_VALUE);
 assertEquals("optionA", optionA.getName());

 assertEquals("optionB", optionB.getLongOpt());
 assertEquals("b", optionB.getOpt());
 assertEquals("Description for optionB", optionB.getDescription());
 assertEquals(false, optionB.getType() instanceof Argument.Type);
 assertEquals('', optionB.getOptCharacter());
 assertEquals(false, optionB.getValueType() instanceof Argument.Type.UNBOUND_VALUE);
 assertEquals(null, optionB.getValue());

 assertEquals("optionC", optionC.getLongOpt());
 assertEquals("c", optionC.getOpt());
 assertEquals("Description for optionC", optionC.getDescription());
 assertEquals('c', optionC.getOptCharacter());
 assertEquals(Argument.Type.UNBOUND_VALUE, optionC.getType());
 assertEquals("num", optionC.getArgument().getName());
 assertEquals("number", optionC.getArgument().getLongOpt());
 assertEquals("Description for the number", optionC.getArgument().getDescription());
 assertEquals(Argument.Type.INTEGER, optionC.getArgument().getValueType());
 assertEquals("5", optionC.getValue().get(0).toString());

 List values = commandLine.getValues(commandLine.getOption("c"), null);
 assertEquals(1, values.size());
 assertEquals("5", values.get(0).toString());

 assertTrue(commandLine.hasOption(optionA));
 assertTrue(commandLine.hasOption(optionB));
 assertTrue(commandLine.hasOption(optionC));

 Option optionD = new Option("d", "optionD", false, "Description for optionD");
 commandLine.addOption(optionD);
 assertFalse(commandLine.hasOption(optionD));

 assertEquals(true, commandLine.getSwitch(optionB, false));

 Set properties = commandLine.getProperties(optionC);
 assertEquals(1, properties.size());
 assertEquals("propertyC", properties.iterator().next());

 Set allProperties = commandLine.getProperties();
 assertEquals(2, allProperties.size());
 assertEquals("propertyA", allProperties.iterator().next());
 assertEquals("propertyC", allProperties.iterator().next());
 }
}