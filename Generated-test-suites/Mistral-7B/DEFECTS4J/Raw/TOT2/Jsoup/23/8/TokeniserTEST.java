import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.helper.Validate;

public class TokeniserTest {
   @Test
   public void testReadMethod() {
      CharacterReader reader = new CharacterReader("test.html"); // replace test.html with the actual test file path
      ParseErrorList errors = new ParseErrorList();
      Tokeniser tokeniser = new Tokeniser(reader, errors);
      Token token = tokeniser.read();
      assertTrue("Expected token to be emitted", token != null);
   }
}