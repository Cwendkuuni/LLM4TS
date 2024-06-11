import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for OptionBuilder.
 */
public class OptionBuilderTest
{
    /**
     * Test method for {@link OptionBuilder#withLongOpt(String)}.
     */
    @Test
    public void testWithLongOpt()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder expectedOptionBuilder = OptionBuilder.withLongOpt("testLongOpt");

        Assert.assertEquals(expectedOptionBuilder, optionBuilder.withLongOpt("testLongOpt"));
    }

    /**
     * Test method for {@link OptionBuilder#hasArg()}.
     */
    @Test
    public void testHasArg()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder expectedOptionBuilder = OptionBuilder.hasArg();

        Assert.assertEquals(expectedOptionBuilder, optionBuilder.hasArg());
    }

    /**
     * Test method for {@link OptionBuilder#hasArg(boolean)}.
     */
    @Test
    public void testHasArgBoolean()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder expectedOptionBuilder = OptionBuilder.hasArg(true);

        Assert.assertEquals(expectedOptionBuilder, optionBuilder.hasArg(true));
    }

    /**
     * Test method for {@link OptionBuilder#withArgName(String)}.
     */
    @Test
    public void testWithArgName()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder expectedOptionBuilder = OptionBuilder.withArgName("testArgName");

        Assert.assertEquals(expectedOptionBuilder, optionBuilder.withArgName("testArgName"));
    }

    /**
     * Test method for {@link OptionBuilder#isRequired()}.
     */
    @Test
    public void testIsRequired()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder expectedOptionBuilder = OptionBuilder.isRequired();

        Assert.assertEquals(expectedOptionBuilder, optionBuilder.isRequired());
    }

    /**
     * Test method for {@link OptionBuilder#withValueSeparator(char)}.
     */
    @Test
    public void testWithValueSeparator()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder expectedOptionBuilder = OptionBuilder.withValueSeparator('testValueSeparator');

        Assert.assertEquals(expectedOptionBuilder, optionBuilder.withValueSeparator('testValueSeparator'));
    }

    /**
     * Test method for {@link OptionBuilder#withValueSeparator()}.
     */
    @Test
    public void testWithValueSeparatorEmpty()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder expectedOptionBuilder = OptionBuilder.withValueSeparator();

        Assert.assertEquals(expectedOptionBuilder, optionBuilder.withValueSeparator());
    }

    /**
     * Test method for {@link OptionBuilder#isRequired(boolean)}.
     */
    @Test
    public void testIsRequiredBoolean()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder expectedOptionBuilder = OptionBuilder.isRequired(true);

        Assert.assertEquals(expectedOptionBuilder, optionBuilder.isRequired(true));
    }

    /**
     * Test method for {@link OptionBuilder#hasArgs()}.
     */
    @Test
    public void testHasArgs()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder expectedOptionBuilder = OptionBuilder.hasArgs();

        Assert.assertEquals(expectedOptionBuilder, optionBuilder.hasArgs());
    }

    /**
     * Test method for {@link OptionBuilder#hasArgs(int)}.
     */
    @Test
    public void testHasArgsInt()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder expectedOptionBuilder = OptionBuilder.hasArgs(2);

        Assert.assertEquals(expectedOptionBuilder, optionBuilder.hasArgs(2));
    }

    /**
     * Test method for {@link OptionBuilder#hasOptionalArg()}.
     */
    @Test
    public void testHasOptionalArg()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder expectedOptionBuilder = OptionBuilder.hasOptionalArg();

        Assert.assertEquals(expectedOptionBuilder, optionBuilder.hasOptionalArg());
    }

    /**
     * Test method for {@link OptionBuilder#hasOptionalArgs()}.
     */
    @Test
    public void testHasOptionalArgs()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder expectedOptionBuilder = OptionBuilder.hasOptionalArgs();

        Assert.assertEquals(expectedOptionBuilder, optionBuilder.hasOptionalArgs());
    }

    /**
     * Test method for {@link OptionBuilder#hasOptionalArgs(int)}.
     */
    @Test
    public void testHasOptionalArgsInt()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder expectedOptionBuilder = OptionBuilder.hasOptionalArgs(2);

        Assert.assertEquals(expectedOptionBuilder, optionBuilder.hasOptionalArgs(2));
    }

    /**
     * Test method for {@link OptionBuilder#withType(Object)}.
     */
    @Test
    public void testWithType()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        Object testType = new Object();
        OptionBuilder expectedOptionBuilder = OptionBuilder.withType(testType);

        Assert.assertEquals(expectedOptionBuilder, optionBuilder.withType(testType));
    }

    /**
     * Test method for {@link OptionBuilder#withDescription(String)}.
     */
    @Test
    public void testWithDescription()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        String testDescription = "testDescription";
        OptionBuilder expectedOptionBuilder = OptionBuilder.withDescription(testDescription);

        Assert.assertEquals(expectedOptionBuilder, optionBuilder.withDescription(testDescription));
    }

    /**
     * Test method for {@link OptionBuilder#create(char)}.
     */
    @Test
    public void testCreateChar()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        char testChar = 'testChar';
        Option expectedOption;

        try
        {
            expectedOption = OptionBuilder.create(testChar);
        } catch (IllegalArgumentException e)
        {
            Assert.fail("Unexpected exception: " + e.getMessage());
            return;
        }

        Option option = optionBuilder.create(testChar);

        Assert.assertEquals(expectedOption, option);
    }

    /**
     * Test method for {@link OptionBuilder#create(String)}.
     */
    @Test
    public void testCreateString()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        String testLongOpt = "testLongOpt";
        Option expectedOption;

        try
        {
            expectedOption = OptionBuilder.create(testLongOpt);
        } catch (IllegalArgumentException e)
        {
            Assert.fail("Unexpected exception: " + e.getMessage());
            return;
        }

        Option option = optionBuilder.create(testLongOpt);

        Assert.assertEquals(expectedOption, option);
    }
}