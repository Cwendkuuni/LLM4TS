import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.ParseException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionImplTest {

private Option option;
private CommandLineParser parser;

@Before
public void setUp() {
option = new OptionImpl(1, "input", true, "The input file");
parser = new CommandLineParser();
}

@Test
public void testCanProcess_WithArg_ReturnsTrue() {
String[] args = {"-i", "test.txt"};
try {
parser.parse(args);
assertTrue(option.canProcess(parser.getCommandLine(), Arrays.asList(args).listIterator()));
} catch (ParseException e) {
fail("ParseException thrown: " + e.getMessage());
}
}

@Test
public void testCanProcess_WithoutArg_ReturnsFalse() {
String[] args = {"-i"};
assertFalse(option.canProcess(parser.getCommandLine(), Arrays.asList(args).listIterator()));
}

@Test
public void testCanProcess_WithInvalidArg_ReturnsFalse() {
String[] args = {"-o", "test.txt"};
assertFalse(option.canProcess(parser.getCommandLine(), Arrays.asList(args).listIterator()));
}

@Test
public void testEquals_WithSameOption_ReturnsTrue() {
Option sameOption = new OptionImpl(1, "input", true, "The input file");
assertTrue(option.equals(sameOption));
}

@Test
public void testEquals_WithDifferentOption_ReturnsFalse() {
Option differentOption = new OptionImpl(2, "output", false, "The output file");
assertFalse(option.equals(differentOption));
}

@Test
public void testHashCode_WithSameOption_ReturnsSameHashCode() {
Option sameOption = new OptionImpl(1, "input", true, "The input file");
assertEquals(option.hashCode(), sameOption.hashCode());
}

@Test
public void testHashCode_WithDifferentOption_ReturnsDifferentHashCode() {
Option differentOption = new OptionImpl(2, "output", false, "The output file");
assertNotSame(option.hashCode(), differentOption.hashCode());
}

@Test
public void testFindOption_WithTrigger_ReturnsOption() {
Option foundOption = option.findOption("-i");
assertEquals(option, foundOption);
}

@Test
public void testFindOption_WithInvalidTrigger_ReturnsNull() {
Option foundOption = option.findOption("-o");
assertNull(foundOption);
}

@Test
public void testIsRequired_ReturnsTrue() {
assertTrue(option.isRequired());
}

@Test
public void testToString_ReturnsCorrectStringRepresentation() {
String expectedOptionString = "[-i, --input] The input file";
assertEquals(expectedOptionString, option.toString());
}
}