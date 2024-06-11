import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionImplTest {

 private OptionImpl option;
 private WriteableCommandLine commandLine;

 @Before
 public void setUp() {
 option = new OptionImpl(1, "optionName", false, "o") {{
 setDescription("This is an option");
 setPrefixes(new java.util.LinkedHashSet() {{
 add("op");
 add("opt");
 }});
 setTriggers(new java.util.ArrayList() {{
 add("opt");
 add("o");
 }});
 }};
 commandLine = new WriteableCommandLine();
 }

 @Test
 public void testConstructor() {
 OptionImpl option = new OptionImpl(1, true, "o");
 assertEquals(1, option.getId());
 assertTrue(option.isRequired());
 }

 @Test
 public void testCanProcess() {
 assertFalse(option.canProcess(commandLine, null));

 commandLine.addArgument("arg1");
 commandLine.addArgument("o");
 commandLine.addArgument("arg2");

 assertTrue(option.canProcess(commandLine, commandLine.getArguments()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCheckPrefixesWithInvalidTrigger() {
 option.checkPrefixes(java.util.Collections.emptySet());
 option.findOption("invalidTrigger");
 }

 @Test
 public void testCheckPrefixesWithValidTrigger() {
 option.checkPrefixes(java.util.Collections.emptySet());
 option.findOption("o");
 option.findOption("opt");
 }

 @Test
 public void testCheckPrefixesWithPrefixes() {
 Set prefixes = new java.util.LinkedHashSet() {{
 add("op");
 add("opt");
 }};
 option.checkPrefixes(prefixes);
 option.findOption("opt");
 option.findOption("o");
 }

 @Test
 public void testEquals() {
 OptionImpl option1 = new OptionImpl(1, "optionName1", false, "o1");
 OptionImpl option2 = new OptionImpl(1, "optionName2", false, "o2");
 OptionImpl option3 = new OptionImpl(1, "optionName1", false, "o1");

 assertEquals(option1, option1);
 assertEquals(option1, option3);
 assertNotEquals(option1, option2);
 }

 @Test
 public void testHashCode() {
 OptionImpl option1 = new OptionImpl(1, "optionName1", false, "o1");
 OptionImpl option2 = new OptionImpl(1, "optionName2", false, "o2");
 OptionImpl option3 = new OptionImpl(1, "optionName1", false, "o1");

 assertEquals(option1.hashCode(), option1.hashCode());
 assertEquals(option1.hashCode(), option3.hashCode());
 assertNotEquals(option1.hashCode(), option2.hashCode());
 }

 @Test
 public void testToString() {
 assertEquals("-o <option-name>\n" +
         "  This is an option\n", option.toString());
 }
}