import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.CommandLine;

public class PosixParserTest {

    @Test
    public void testInit() {
        // Arrange
        PosixParser parser = new PosixParser();

        // Act
        parser.init();

        // Assert
        assertNull(parser.currentOption);
        assertFalse(parser.eatTheRest);
        assertEquals(0, parser.tokens.size());
    }

    @Test
    public void testFlatten() {
        // Arrange
        PosixParser parser = new PosixParser();
        Options options = new Options();

        // Act
        String[] result = parser.flatten(options, new String[] {}, false);

        // Assert
        assertEquals(0, result.length);
    }

    @Test
    public void testProcessOptionToken() {
        // Arrange
        PosixParser parser = new PosixParser();
        options.addOption("a", false, "Option A");

        // Act
        parser.processOptionToken("-a", false);

        // Assert
        assertEquals("Option A", parser.currentOption.getDescription());
        assertEquals(1, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
    }

    @Test
    public void testBurstToken() {
        // Arrange
        PosixParser parser = new PosixParser();
        options.addOption("a", false, "Option A");

        // Act
        parser.burstToken("-ab", false);

        // Assert
        assertEquals(2, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
        assertEquals("-b", parser.tokens.get(1));
    }

    @Test
    public void testProcess() {
        // Arrange
        PosixParser parser = new PosixParser();

        // Act
        parser.process("value");

        // Assert
        assertEquals(1, parser.tokens.size());
        assertEquals("value", parser.tokens.get(0));
    }

    @Test
    public void testGobble() {
        // Arrange
        PosixParser parser = new PosixParser();
        Iterator iter = Arrays.asList("value1", "value2").iterator();

        // Act
        parser.gobble(iter);

        // Assert
        assertEquals(2, parser.tokens.size());
        assertEquals("value1", parser.tokens.get(0));
        assertEquals("value2", parser.tokens.get(1));
    }
}