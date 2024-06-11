package umd.cs.shop;

import static org.junit.Assert.*;
import org.junit.Test;

public class JSTermTest {

    @Test
    public void testParseList() {
        StreamTokenizer tokenizer = new StreamTokenizer();
        JSTerm jt = new JSTerm(tokenizer);
        
        // Typical use case
        assertTrue(jt.parseList(tokenizer).get(0).equals("nil"));
        
        // Edge case
        tokenizer = null;
        try {
            jt.parseList(tokenizer);
            fail("Expected JSParserError");
        } catch (JSParserError e) {
            // Success
        }
        
        // Error scenario
        tokenizer = new StreamTokenizer();
        try {
            jt.parseList(tokenizer);
            fail("Expected JSParserError");
        } catch (JSParserError e) {
            // Success
        }
    }

}