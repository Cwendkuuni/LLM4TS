package umd.cs.shop;

import org.junit.Test;
import static org.junit.Assert.*;

public class JSTermTest {

    @Test
    public void testJSTermConstructor() {
        JSTerm term = new JSTerm();
        assertNotNull(term);
    }

    @Test
    public void testParseList() {
        JSTerm term = new JSTerm();
        final StreamTokenizer st = new StreamTokenizer(new StringReader("(1 . (2 . (3 . nil)))"));
        JSTerm parsedList = term.parseList(st);
        assertNotNull(parsedList);
        assertEquals("1", parsedList.elementAt(0));
        assertEquals(".", parsedList.elementAt(1));
        assertEquals("2", parsedList.elementAt(1).elementAt(0));
        assertEquals(".", parsedList.elementAt(2));
        assertEquals("3", parsedList.elementAt(2).elementAt(0));
        assertEquals("nil", parsedList.elementAt(2).elementAt(1));
    }

    @Test(expected = JSParserError.class)
    public void testParseListWithInvalidInput() {
        JSTerm term = new JSTerm();
        final StreamTokenizer st = new StreamTokenizer(new StringReader("(1 . 2 . 3 . nil)"));
        JSTerm parsedList = term.parseList(st);
    }

    @Test
    public void testPrint() {
        JSTerm term = new JSTerm();
        term.addElement("test");
        term.makeConstant();
        term.print();
    }
    
    // Add more test cases for other methods as needed

}