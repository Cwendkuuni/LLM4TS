import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest{

    @Test
    public void testGetToken(){
        Token token = new Token("abc", "123");
        String actual = token.getToken();
        assertEquals("abc", actual);
    }
    
    @Test
    public void testGetSecret(){
        Token token = new Token("abc", "123");
        String actual = token.getSecret();
        assertEquals("123", actual);
    }
    
    @Test
    public void testGetRawResponse(){
        Token token = new Token("abc", "123", "response");
        String actual = token.getRawResponse();
        assertEquals("response", actual);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetRawResponse_Exception(){
        Token token = new Token("abc", "123");
        token.getRawResponse();
    }
    
    @Test
    public void testToString(){
        Token token = new Token("abc", "123");
        String expected = "Token[abc , 123]";
        String actual = token.toString();
        assertEquals(expected, actual);
    }
}