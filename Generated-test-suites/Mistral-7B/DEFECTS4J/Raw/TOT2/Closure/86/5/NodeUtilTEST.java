import static org.junit.Assert.*;

public class NodeUtilTest {
    // isForInTest.java
    // isStatementTest.java
    // isFunctionDeclarationTest.java
    // isHoistedFunctionDeclarationTest.java
    // InvalidNodeTest.java
    // EmptyNodeTest.java
    // MixedNodesTest.java
    // InvalidFunctionNodeTest.java

    @Test
    public void testIsForIn() {
        new isForInTest().testIsForIn();
    }

    @Test
    public void testIsStatement() {
        new isStatementTest().testIsStatement();
    }

    @Test
    public void testIsFunctionDeclaration() {
        new isFunctionDeclarationTest().testIsFunctionDeclaration();
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        new isHoistedFunctionDeclarationTest().testIsHoistedFunctionDeclaration();
    }

    @Test
    public void testInvalidNode() {
        new InvalidNodeTest().testInvalidNode();
    }

    @Test
    public void testEmptyNode() {
        new EmptyNodeTest().testEmptyNode();
    }

    @Test
    public void testMixedNodes() {
        new MixedNodesTest().testMixedNodes();
    }

    @Test
    public void testInvalidFunctionNode() {
        new InvalidFunctionNodeTest().testInvalidFunctionNode();
    }
}

//