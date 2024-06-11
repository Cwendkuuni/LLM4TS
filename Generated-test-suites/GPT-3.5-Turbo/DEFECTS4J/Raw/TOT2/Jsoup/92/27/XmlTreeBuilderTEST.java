import org.junit.Test;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        assertEquals(ParseSettings.preserveCase, xmlTreeBuilder.defaultSettings());
    }

    @Test
    public void testInitialiseParse() {
        // Add test case for initialiseParse method
    }

    @Test
    public void testParse() {
        // Add test case for parse method with Reader input
    }

    @Test
    public void testParseString() {
        // Add test case for parse method with String input
    }

    @Test
    public void testProcess() {
        // Add test case for process method with different token types
    }

    @Test
    public void testInsertNode() {
        // Add test case for insertNode method
    }

    @Test
    public void testInsertStartTag() {
        // Add test case for insert method with Token.StartTag
    }

    @Test
    public void testInsertComment() {
        // Add test case for insert method with Token.Comment
    }

    @Test
    public void testInsertCharacter() {
        // Add test case for insert method with Token.Character
    }

    @Test
    public void testInsertDoctype() {
        // Add test case for insert method with Token.Doctype
    }

    @Test
    public void testPopStackToClose() {
        // Add test case for popStackToClose method
    }

    @Test
    public void testParseFragment() {
        // Add test case for parseFragment method with inputFragment
    }

    @Test
    public void testParseFragmentWithContext() {
        // Add test case for parseFragment method with inputFragment and context Element
    }
}