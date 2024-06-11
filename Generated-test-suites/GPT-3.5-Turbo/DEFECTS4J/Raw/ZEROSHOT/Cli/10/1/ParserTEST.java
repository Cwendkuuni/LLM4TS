import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ParserTest {
    private Options options;
    private Parser parser;
    private String[] arguments;

    @Before
    public void setUp() {
        options = mock(Options.class);
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return null;
            }

            @Override
            protected void processOption(String arg, ListIterator iter) throws ParseException {

            }
        };
        parser.setOptions(options);
        arguments = new String[]{"arg1", "arg2"};
    }

    @Test
    public void testParse() throws ParseException {
        CommandLine expected = new CommandLine();
        when(options.helpOptions()).thenReturn(Arrays.asList(new Option("help", "display help message")));
        CommandLine actual = parser.parse(options, arguments);
        assertEquals(expected, actual);
        verify(options).helpOptions();
    }

    @Test
    public void testProcessProperties() {
        Properties properties = new Properties();
        properties.setProperty("option1", "value1");
        Option option1 = new Option("option1", true, "description1");
        when(options.getOption("option1")).thenReturn(option1);
        CommandlLine cmd = new CommandLine();
        parser.cmd = cmd;
        parser.processProperties(properties);
        verify(options).getOption("option1");
    }

    @Test
    public void testCheckRequiredOptions() throws MissingOptionException {
        List requiredOptions = mock(List.class);
        when(options.getRequiredOptions()).thenReturn(requiredOptions);
        try {
            parser.checkRequiredOptions();
        } catch (MissingOptionException e) {
            verify(options).getRequiredOptions();
        }
    }

    @Test
    public void testProcessArgs() throws ParseException {
        Option option = mock(Option.class);
        ListIterator iter = mock(ListIterator.class);
        parser.processArgs(option, iter);
        verify(iter, times(2)).next();
        verify(iter).previous();
        verify(iter, never()).hasNext();
        verify(option).addValueForProcessing(anyString());
    }

    @Test
    public void testProcessOption() throws ParseException {
        Option option = mock(Option.class);
        ListIterator iter = mock(ListIterator.class);
        when(options.hasOption(anyString())).thenReturn(true);
        when(options.getOption(anyString())).thenReturn(option);
        CommandLine cmd = new CommandLine();
        parser.cmd = cmd;
        parser.processOption("option1", iter);
        verify(options).hasOption("option1");
        verify(options).getOption("option1");
        verify(option).hasArg();
        verify(option, never()).addValueForProcessing(anyString());
        verify(cmd).addOption(option);
    }
}