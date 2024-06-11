import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {
    private PosixParser posixParser;
    private Options options;

    @Before
    public void setUp() {
        posixParser = new PosixParser();
        options = new Options();
    }

    @Test
    public void testFlatten() {
        String[] arguments = {"-a", "value1", "--option", "value2"};
        options.addOption("a", true, "Option a");
        options.addOption("option", true, "Option with value");
        String[] expected = {"-a", "value1", "--option", "value2"};
        String[] result = posixParser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        String[] arguments = {"-a", "value1", "--option", "value2", "nonOption"};
        options.addOption("a", true, "Option a");
        options.addOption("option", true, "Option with value");
        String[] expected = {"-a", "value1", "--option", "value2", "--", "nonOption"};
        String[] result = posixParser.flatten(options, arguments, true);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithHyphenAsOption() {
        String[] arguments = {"-"};
        String[] expected = {"-"};
        String[] result = posixParser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithOptionWithoutValue() {
        String[] arguments = {"-a"};
        options.addOption("a", false, "Option a");
        String[] expected = {"-a"};
        String[] result = posixParser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithOptionsAndRemainingValues() {
        String[] arguments = {"-a", "value1", "value2"};
        options.addOption("a", true, "Option a");
        String[] expected = {"-a", "value1", "value2"};
        String[] result = posixParser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithInvalidOptionAndStopAtNonOption() {
        String[] arguments = {"-b", "value1"};
        options.addOption("a", true, "Option a");
        String[] expected = {"--", "-b", "value1"};
        String[] result = posixParser.flatten(options, arguments, true);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithInvalidOptionAndNoStopAtNonOption() {
        String[] arguments = {"-b", "value1"};
        options.addOption("a", true, "Option a");
        String[] expected = {"-b"};
        String[] result = posixParser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testBurstTokenWithValidOption() throws Exception {
        String token = "-a";
        options.addOption("a", true, "Option a");
        options.addOption("b", false, "Option b");
        options.addOption("c", false, "Option c");
        options.addOption("d", false, "Option d");
        Object[] expected = {"-a"};
        Object[] result = getBurstTokenResult(token);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testBurstTokenWithValidOptionAndValue() throws Exception {
        String token = "-aValue";
        options.addOption("a", true, "Option a");
        options.addOption("b", false, "Option b");
        options.addOption("c", false, "Option c");
        options.addOption("d", false, "Option d");
        Object[] expected = {"-a", "Value"};
        Object[] result = getBurstTokenResult(token);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testBurstTokenWithInvalidOptionAndStopAtNonOption() throws Exception {
        String token = "-e";
        options.addOption("a", true, "Option a");
        options.addOption("b", false, "Option b");
        options.addOption("c", false, "Option c");
        options.addOption("d", false, "Option d");
        Object[] expected = {token.substring(1)};
        Object[] result = getBurstTokenResult(token);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testBurstTokenWithInvalidOptionAndNoStopAtNonOption() throws Exception {
        String token = "-e";
        options.addOption("a", true, "Option a");
        options.addOption("b", false, "Option b");
        options.addOption("c", false, "Option c");
        options.addOption("d", false, "Option d");
        Object[] expected = {token};
        Object[] result = getBurstTokenResult(token, false);
        Assert.assertArrayEquals(expected, result);
    }

    private Object[] getBurstTokenResult(String token) throws Exception {
        return getBurstTokenResult(token, true);
    }

    private Object[] getBurstTokenResult(String token, boolean stopAtNonOption) throws Exception {
        Class<?>[] parameterTypes = new Class<?>[]{String.class, boolean.class};
        Object[] parameters = new Object[]{token, stopAtNonOption};
        Option option = options.getOption(token.substring(1));
        Option[] optionsArray = {option};
        setField(posixParser, "options", options);
        setField(posixParser, "currentOption", option);
        setField(posixParser, "tokens", new ArrayList<String>());
        setField(posixParser, "eatTheRest", false);
        Object[] result = (Object[]) invokeMethod(posixParser, "burstToken", parameterTypes, parameters);
        return result;
    }

    private void setField(Object object, String fieldName, Object value) throws Exception {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(object, value);
    }

    private Object invokeMethod(Object object, String methodName, Class<?>[] parameterTypes, Object[] parameters) throws Exception {
        Method method = object.getClass().getDeclaredMethod(methodName, parameterTypes);
        method.setAccessible(true);
        return method.invoke(object, parameters);
    }
}