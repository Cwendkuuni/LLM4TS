
import org.junit.Test;
import org.junit.Assert;

public class TokeniserTest {

  @Test
  public void testRead() {
    Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());

    Token result = tokeniser.read();

    // test code here to assert the functionality of the read method
    // ...

    Assert.assertNotNull(result);
  }

  @Test
  public void testEmit() {
    Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());

    Token token = new Token();
    token.setType(Token.TokenType.StartTag);

    tokeniser.emit(token);

    // test code here to assert the functionality of the emit method
    // ...

    Assert.assertEquals(token, tokeniser.emitPending);
    Assert.assertEquals(true, tokeniser.isEmitPending);
  }

  // ... add test methods for each public method in the Tokeniser class

  @Test
  public void testError() {
    Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());

    tokeniser.error(TokeniserState.Data);

    // test code here to assert the functionality of the error method
    // ...

    Assert.assertEquals(1, tokeniser.errors.size());
    Assert.assertEquals("Unexpected character '%s' in input state [%s]", tokeniser.errors.get(0).getMessage());
  }

  @Test
  public void testEofError() {
    Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());

    tokeniser.eofError(TokeniserState.Data);

    // test code here to assert the functionality of the eofError method
    // ...

    Assert.assertEquals(1, tokeniser.errors.size());
    Assert.assertEquals("Unexpectedly reached end of file (EOF) in input state [%s]", tokeniser.errors.get(0).getMessage());
  }

  // ... add test methods for potential edge cases and exceptions

}
