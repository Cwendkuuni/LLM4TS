import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        // Test initialising the parse
        // Test stack.add() method
        // Test doc.outputSettings().syntax()

        // Implement test here
    }

    @Test
    public void testProcess() {
        // Test processing different types of tokens
        // Test each case in the switch statement
        // Test Validate.fail() for unexpected token type

        // Implement test here
    }

    @Test
    public void testInsertNode() {
        // Test inserting a node to the current element

        // Implement test here
    }

    @Test
    public void testInsertStartTag() {
        // Test inserting a start tag
        // Test for self-closing tags
        // Test for unknown tags

        // Implement test here
    }

    @Test
    public void testInsertComment() {
        // Test inserting a comment
        // Test for bogus comments
        // Test parsing data as an element

        // Implement test here
    }

    @Test
    public void testInsertCharacter() {
        // Test inserting a character
        // Test creating a TextNode

        // Implement test here
    }

    @Test
    public void testInsertDoctype() {
        // Test inserting a doctype
        // Test creating a DocumentType

        // Implement test here
    }

    @Test
    public void testPopStackToClose() {
        // Test popping the stack to close an element
        // Test finding the first occurrence of an element
        // Test removing element from stack

        // Implement test here
    }

    @Test
    public void testParseFragment() {
        // Test parsing a fragment
        // Test running the parser
        // Test returning the list of child nodes

        // Implement test here
    }
}