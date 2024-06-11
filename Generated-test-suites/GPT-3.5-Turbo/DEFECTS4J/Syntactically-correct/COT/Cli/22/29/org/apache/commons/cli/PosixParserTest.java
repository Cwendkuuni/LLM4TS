import static org.junit.Assert.*;
import org.junit.Test;

public class PosixParserTest {
    
    @Test
    public void testFlatten() {
        Options options = new Options();
        String[] arguments = {"--foo", "-a", "-b", "--bar=hello", "world"};
        boolean stopAtNonOption = true;
        PosixParser parser = new PosixParser();
        
        String[] result = parser.flatten(options, arguments, stopAtNonOption);
        
        // Verify that all tokens are correctly flattened
        assertEquals("--foo", result[0]);
        assertEquals("-a", result[1]);
        assertEquals("-b", result[2]);
        assertEquals("--bar", result[3]);
        assertEquals("hello", result[4]);
        assertEquals("world", result[5]);
        
        // Verify that non-option tokens after stopAtNonOption are ignored
        assertEquals(6, result.length);
    }
    
    @Test
    public void testProcessNonOptionToken() {
        PosixParser parser = new PosixParser();
        
        parser.processNonOptionToken("value");
        parser.processNonOptionToken("123");
        
        List tokens = (List) TestUtils.getPrivateField(parser, "tokens");
        
        // Verify that "--" is added before each non-option token
        assertEquals(2, tokens.size());
        assertEquals("--", tokens.get(0));
        assertEquals("value", tokens.get(1));
        assertEquals("--", tokens.get(2));
        assertEquals("123", tokens.get(3));
    }
    
    @Test
    public void testProcessOptionToken() {
        Options options = new Options();
        Option option1 = new Option("a", false, "Option A");
        Option option2 = new Option("b", true, "Option B");
        options.addOption(option1);
        options.addOption(option2);
        
        PosixParser parser = new PosixParser();
        parser.setOptions(options);
        
        parser.processOptionToken("-a", true);
        parser.processOptionToken("-b", true);
        parser.processOptionToken("-c", true);
        parser.processOptionToken("value", true);
        
        List tokens = (List) TestUtils.getPrivateField(parser, "tokens");
        
        // Verify that tokens with valid options are added to the list
        assertEquals(2, tokens.size());
        assertEquals("-a", tokens.get(0));
        assertEquals("-b", tokens.get(1));
        
        // Verify that invalid token is not added to the list
        assertEquals(2, tokens.size());
    }
    
    @Test
    public void testBurstToken() {
        Options options = new Options();
        Option option1 = new Option("a", false, "Option A");
        Option option2 = new Option("b", true, "Option B");
        options.addOption(option1);
        options.addOption(option2);
        
        PosixParser parser = new PosixParser();
        parser.setOptions(options);
        
        parser.burstToken("-ab", true);
        parser.burstToken("-c", true);
        parser.burstToken("-d", false);
        
        List tokens = (List) TestUtils.getPrivateField(parser, "tokens");
        
        // Verify that each character in the token is processed correctly
        assertEquals(3, tokens.size());
        assertEquals("-a", tokens.get(0));
        assertEquals("-b", tokens.get(1));
        assertEquals("-d", tokens.get(2));
        
        // Verify that remaining characters are added as separate tokens
        assertEquals(4, tokens.size());
        assertEquals("-a", tokens.get(0));
        assertEquals("-b", tokens.get(1));
        assertEquals("c", tokens.get(2));
        assertEquals("-d", tokens.get(3));
        
        // Verify bursting when stopAtNonOption is false
        assertEquals(5, tokens.size());
        assertEquals("-a", tokens.get(0));
        assertEquals("-b", tokens.get(1));
        assertEquals("c", tokens.get(2));
        assertEquals("-d", tokens.get(3));
        assertEquals("", tokens.get(4));
    }
}