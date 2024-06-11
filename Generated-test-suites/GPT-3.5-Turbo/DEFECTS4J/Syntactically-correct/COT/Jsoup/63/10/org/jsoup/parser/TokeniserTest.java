import static org.junit.Assert.*;
import org.junit.Test;

public class TokeniserTest {

    @Test
    public void testRead() {
        //Test the read method
        Tokeniser tokeniser = new Tokeniser();
        Token token = tokeniser.read();
        assertNotNull(token);
    }

    @Test
    public void testEmit() {
        //Test the emit method
        Tokeniser tokeniser = new Tokeniser();
        tokeniser.emit(new Token());
        Token token = tokeniser.read();
       	assertNotNull(token);
    }

    @Test
    public void testCreateTagPending() {
        //Test the createTagPending method
        Tokeniser tokeniser = new Tokeniser();
        Token.Tag tag = tokeniser.createTagPending(true);
        assertNotNull(tag);
    }
    
    @Test
    public void testCurrentNodeInHtmlNS(){
	    //Test the currentNodeInHtmlNS method
        Tokeniser tokeniser = new Tokeniser();
    	boolean result = tokeniser.currentNodeInHtmlNS();
    	assertTrue(result);
    }

}