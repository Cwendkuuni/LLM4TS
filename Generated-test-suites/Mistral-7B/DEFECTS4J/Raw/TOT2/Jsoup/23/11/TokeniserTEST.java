package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.TokenType;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.ParseError;
import org.jsoup.nodes.ParseErrorList;

public class TokeniserTest {
// ... (previous test cases)

// Test EOF for each method

@Test
public void testRead_EOF() {
CharacterReader reader = new CharacterReader("test.html");
ParseErrorList errors = new ParseErrorList();
Tokeniser tokeniser = new Tokeniser(reader, errors);

tokeniser.read();
tokeniser.read();
tokeniser.read();

Token token = tokeniser.read(); // EOF
assertTrue(errors.size() > 0);
assertEquals("Unexpectedly reached end of file (EOF) in input state [Data]", errors.get(0).getMessage());
}

// ... (add similar test cases for other methods)

}