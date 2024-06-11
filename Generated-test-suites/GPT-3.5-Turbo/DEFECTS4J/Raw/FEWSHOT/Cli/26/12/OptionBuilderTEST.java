import static org.junit.Assert.*;
import org.junit.Test;

public class OptionBuilderTest {
    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("longOpt");
        // assert
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        // assert
    }

    @Test
    public void testHasArgBoolean() {
        OptionBuilder.hasArg(true);
        // assert
        OptionBuilder.hasArg(false);
        // assert
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("argName");
        // assert
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        // assert
    }

    @Test
    public void testWithOptionalArg() {
        OptionBuilder.hasOptionalArg();
        // assert
    }

    @Test
    public void testHasOptionalArgsInt() {
        OptionBuilder.hasOptionalArgs(1);
        // assert
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("description");
        // assert
    }

    @Test
    public void testCreateChar() {
        OptionBuilder.create('c');
        // assert
    }

    @Test
    public void testCreate() {
        OptionBuilder.create();
        // assert
    }

    @Test
    public void testCreateString() {
        OptionBuilder.create("opt");
        // assert
    }
}