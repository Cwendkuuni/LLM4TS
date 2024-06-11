import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class OptionBuilderTest {

    // Test withLongOpt method
    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("testLongOpt");
        OptionBuilderTest.optionBuilder = optionBuilder;
        
        assertEquals(OptionBuilderTest.optionBuilder, optionBuilder);
        assertEquals("testLongOpt", OptionBuilderTest.optionBuilder.getLongopt());
    }

    // Test hasArg method
    @Test
    public void testHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        OptionBuilderTest.optionBuilder = optionBuilder;
        
        assertEquals(OptionBuilderTest.optionBuilder, optionBuilder);
        assertEquals(1, OptionBuilderTest.optionBuilder.getNumberOfArgs());
    }

    // Test hasArg(boolean hasArg) method
    @Test
    public void testHasArgWithBoolean() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg(true);
        OptionBuilderTest.optionBuilder = optionBuilder;
        
        assertEquals(OptionBuilderTest.optionBuilder, optionBuilder);
        assertEquals(1, OptionBuilderTest.optionBuilder.getNumberOfArgs());

        optionBuilder = OptionBuilder.hasArg(false);
        OptionBuilderTest.optionBuilder = optionBuilder;
        
        assertEquals(OptionBuilderTest.optionBuilder, optionBuilder);
        assertEquals(Option.UNINITIALIZED, OptionBuilderTest.optionBuilder.getNumberOfArgs());
    }

    // Test withArgName method
    @Test
    public void testWithArgName() {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("testArgName");
        OptionBuilderTest.optionBuilder = optionBuilder;
        
        assertEquals(OptionBuilderTest.optionBuilder, optionBuilder);
        assertEquals("testArgName", OptionBuilderTest.optionBuilder.getArgName());
    }

    // Test isRequired method
    @Test
    public void testIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        OptionBuilderTest.optionBuilder = optionBuilder;
        
        assertEquals(OptionBuilderTest.optionBuilder, optionBuilder);
        assertEquals(true, OptionBuilderTest.optionBuilder.isRequired());
    }

    // Test withValueSeparator char method
    @Test
    public void testWithValueSeparatorChar() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        OptionBuilderTest.optionBuilder = optionBuilder;
        
        assertEquals(OptionBuilderTest.optionBuilder, optionBuilder);
        assertEquals(':', OptionBuilderTest.optionBuilder.getValueSeparator());
    }

    // Test withValueSeparator method
    @Test
    public void testWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator();
        OptionBuilderTest.optionBuilder = optionBuilder;
        
        assertEquals(OptionBuilderTest.optionBuilder, optionBuilder);
        assertEquals('=', OptionBuilderTest.optionBuilder.getValueSeparator());
    }

    // Test isRequired boolean method
    @Test
    public void testIsRequiredWithBoolean() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired(true);
        OptionBuilderTest.optionBuilder = optionBuilder;
        
        assertEquals(OptionBuilderTest.optionBuilder, optionBuilder);
        assertEquals(true, OptionBuilderTest.optionBuilder.isRequired());

        optionBuilder = OptionBuilder.isRequired(false);
        OptionBuilderTest.optionBuilder = optionBuilder;
        
        assertEquals(OptionBuilderTest.optionBuilder, optionBuilder);
        assertEquals(false, OptionBuilderTest.optionBuilder.isRequired());
    }

    // Test hasArgs method
    @Test
    public void testHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        OptionBuilderTest.optionBuilder = optionBuilder;
        
        assertEquals(OptionBuilderTest.optionBuilder, optionBuilder);
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilderTest.optionBuilder.getNumberOfArgs());
    }

    // Test hasArgs int method
    @Test
    public void testHasArgsWithInt() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(2);
        OptionBuilderTest.optionBuilder = optionBuilder;
        
        assertEquals(OptionBuilderTest.optionBuilder, optionBuilder);
        assertEquals(2, OptionBuilderTest.optionBuilder.getNumberOfArgs());
    }

    // Test hasOptionalArg method
    @Test
    public void testHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        OptionBuilderTest.optionBuilder = optionBuilder;
        
        assertEquals(OptionBuilderTest.optionBuilder, optionBuilder);
        assertEquals(1, OptionBuilderTest.optionBuilder.getNumberOfArgs());
        assertEquals(true, OptionBuilderTest.optionBuilder.hasOptionalArg());
    }

    // Test hasOptionalArgs method
    @Test
    public void testHasOptionalArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        OptionBuilderTest.optionBuilder = optionBuilder;
        
        assertEquals(OptionBuilderTest.optionBuilder, optionBuilder);
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilderTest.optionBuilder.getNumberOfArgs());
        assertEquals(true, OptionBuilderTest.optionBuilder.hasOptionalArg());
    }

    // Test hasOptionalArgs int method
    @Test
    public void testHasOptionalArgsWithInt() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(3);
        OptionBuilderTest.optionBuilder = optionBuilder;
        
        assertEquals(OptionBuilderTest.optionBuilder, optionBuilder);
        assertEquals(3, OptionBuilderTest.optionBuilder.getNumberOfArgs());
        assertEquals(true, OptionBuilderTest.optionBuilder.hasOptionalArg());
    }

    // Test withType method
    @Test
    public void testWithType() {
        OptionBuilder optionBuilder = OptionBuilder.withType(Object.class);
        OptionBuilderTest.optionBuilder = optionBuilder;
        
        assertEquals(OptionBuilderTest.optionBuilder, optionBuilder);
        assertEquals(Object.class, OptionBuilderTest.optionBuilder.getType());
    }

    // Test withDescription method
    @Test
    public void testWithDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("testDescription");
        OptionBuilderTest.optionBuilder = optionBuilder;
        
        assertEquals(OptionBuilderTest.optionBuilder, optionBuilder);
        assertEquals("testDescription", OptionBuilderTest.optionBuilder.getDescription());
    }

    // Test create char method
    @Test
    public void testCreateWithChar() {
        Option option = OptionBuilder.create('t');
        
        assertEquals('t', option.getOpt());
        assertEquals(null, option.getLongOpt());
        assertEquals(null, option.getDescription());
    }

    // Test create method
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        OptionBuilder.create();
    }

    // Test create method
    @Test
    public void testCreate() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("testLongOpt");
        OptionBuilderTest.optionBuilder = optionBuilder;

        Option option = OptionBuilder.create();

        assertEquals(null, option.getOpt());
        assertEquals("testLongOpt", option.getLongOpt());
        assertEquals(null, option.getDescription());
    }

    // Test create String method
    @Test
    public void testCreateWithString() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("testLongOpt");
        OptionBuilderTest.optionBuilder = optionBuilder;

        Option option = OptionBuilder.create("t");

        assertEquals("t", option.getOpt());
        assertEquals("testLongOpt", option.getLongOpt());
        assertEquals(null, option.getDescription());
    }
}