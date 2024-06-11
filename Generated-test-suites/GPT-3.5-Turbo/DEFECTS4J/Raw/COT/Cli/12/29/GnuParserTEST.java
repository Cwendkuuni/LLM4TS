
import static org.junit.Assert.*;
import org.junit.Test;

public class GnuParserTest {

    @Test
    public void testFlatten() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = {"-D", "-abc", "-name=value", "arg1", "--arg2"};
        boolean stopAtNonOption = true;
        
        String[] result = parser.flatten(options, arguments, stopAtNonOption);
        
        String[] expected = {"-D", "-a", "-b", "-c", "-name=value", "arg1", "--arg2"};
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlattenStopAtNonOptionFalse() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = {"-D", "-abc", "-name=value", "arg1", "--arg2"};
        boolean stopAtNonOption = false;
        
        String[] result = parser.flatten(options, arguments, stopAtNonOption);
        
        String[] expected = {"-D", "-a", "-b", "-c", "-name=value", "arg1", "--arg2"};
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlattenWithDash() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = {"-", "-abc", "-name=value", "arg1", "--arg2"};
        boolean stopAtNonOption = true;
        
        String[] result = parser.flatten(options, arguments, stopAtNonOption);
        
        String[] expected = {"-", "-a", "-b", "-c", "-name=value", "arg1", "--arg2"};
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlattenWithDoubleDash() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = {"--", "-abc", "-name=value", "arg1", "--arg2"};
        boolean stopAtNonOption = true;
        
        String[] result = parser.flatten(options, arguments, stopAtNonOption);
        
        String[] expected = {"--", "-abc", "-name=value", "arg1", "--arg2"};
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlattenWithNonOption() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = {"-D", "-abc", "-foo", "-name=value", "arg1", "--arg2"};
        boolean stopAtNonOption = true;
        
        String[] result = parser.flatten(options, arguments, stopAtNonOption);
        
        String[] expected = {"-D", "-a", "-b", "-foo", "-name=value", "arg1", "--arg2"};
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlattenWithNonOptionStopAtNonOptionFalse() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = {"-D", "-abc", "-foo", "-name=value", "arg1", "--arg2"};
        boolean stopAtNonOption = false;
        
        String[] result = parser.flatten(options, arguments, stopAtNonOption);
        
        String[] expected = {"-D", "-a", "-b", "-foo", "-name=value", "arg1", "--arg2"};
        assertArrayEquals(expected, result);
    }
}
