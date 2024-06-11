import org.junit.Test;
import org.scribe.model.Token;
import java.lang.IllegalStateException;

public class TokenTest {
    
    @Test
    public void testGetToken() {
        Token token = new Token("token123", "secret123");
        assert(token.getToken().equals("token123"));
    }
    
    @Test
    public void testGetSecret() {
        Token token = new Token("token123", "secret123");
        assert(token.getSecret().equals("secret123"));
    }
    
    @Test
    public void testGetRawResponse_Null() {
        Token token = new Token("token123", "secret123");
        assert(token.getRawResponse() == null);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetRawResponse_IllegalStateException() {
        Token token = new Token("token123", "secret123", "rawResponseValue");
        token.getRawResponse();
    }

    @Test
    public void testToString() {
        Token token = new Token("token123", "secret123");
        String expected = "Token[token123 , secret123]";
        assert(token.toString().equals(expected));
    }
}