import static org.junit.Assert.*;
import org.junit.Test;

public class ArgsParserTest {

    @Test
    public void testGetArgsCount() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2", "arg3"});
        assertEquals(3, parser.getArgsCount());
    }

    @Test
    public void testGetArgs() {
        String[] inputArgs = {"arg1", "arg2", "arg3"};
        ArgsParser parser = new ArgsParser(inputArgs);
        assertArrayEquals(inputArgs, parser.getArgs());
    }

    @Test
    public void testSetArgs() {
        String[] inputArgs = {"arg1", "arg2", "arg3"};
        ArgsParser parser = new ArgsParser();
        parser.setArgs(inputArgs);
        assertArrayEquals(inputArgs, parser.getArgs());
    }

    // Additional test cases for other methods can be added here

}