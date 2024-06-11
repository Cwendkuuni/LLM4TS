import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

    public static class isForInTest {
        @Test
        public void testIsForIn() {
            Node node = new Node("for in");
            assertTrue(NodeUtil.isForIn(node));
        }

        // Add test cases for nodes with different types and multiple children

        @Test
        public void testIsNotForIn() {
            Node node = new Node("if");
            assertFalse(NodeUtil.isForIn(node));
        }
    }

    public static class isStatementTest {
        @Test
        public void testIsStatement() {
            Node node = new Node("print('Hello')");
            assertTrue(NodeUtil.isStatement(node));
        }

        // Add test cases for nodes with different types and null parents

        @Test
        public void testIsNotStatement() {
            Node node = new Node("1 + 1");
            assertFalse(NodeUtil.isStatement(node));
        }
    }

    public static class isFunctionDeclarationTest {
        @Test
        public void testIsFunctionDeclaration() {
            Node node = new Node("function func() {}");
            assertTrue(NodeUtil.isFunctionDeclaration(node));
        }

        // Add test cases for nodes with various function types and null parents

        @Test
        public void testIsNotFunctionDeclaration() {
            Node node = new Node("1 + 1");
            assertFalse(NodeUtil.isFunctionDeclaration(node));
        }
    }

    public static class isHoistedFunctionDeclarationTest {
        @Test
        public void testIsHoistedFunctionDeclaration() {
            Node script = new Node("function func() {}");
            assertTrue(NodeUtil.isHoistedFunctionDeclaration(script.getChild(0)));
        }

        // Add test cases for nodes with various function types and non-null parents

        @Test
        public void testIsNotHoistedFunctionDeclaration() {
            Node function = new Node("function func() {}");
            Node parent = new Node("function main() { func(); }");
            Node child = function.getChild(0);
            assertFalse(NodeUtil.isHoistedFunctionDeclaration(child));
        }
    }

    // Add test cases for edge cases and exceptions for isForIn, isStatement, isFunctionDeclaration, and isHoistedFunctionDeclaration

    public static void main(String[] args) {
        junit.textui.TestRunner.run(NodeUtilTest.class);
    }
}