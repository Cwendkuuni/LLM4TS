import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isForIn;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isHoistedFunctionDeclaration;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isFunctionDeclaration;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isStatement;

import com.google.common.base.Preconditions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import com.google.javascript.rhino.Node;

@RunWith(JUnit4.class)
public class NodeUtilTest {

 @Test
 public void testIsForIn() {
 Node node = new Node("for"); // FOR token
 Node child1 = new Node("identifier", "i");
 Node child2 = new Node("{");
 Node child3 = new Node("}");
 node.setChildren(child1, child2, child3);

 assertTrue(isForIn(node));
 }

 @Test
 public void testIsForInWithInvalidNode() {
 Node node = new Node("invalid"); // Invalid token

 assertFalse(isForIn(node));
 }

 @Test
 public void testIsForInWithNonForNode() {
 Node node = new Node("if", new Node("condition")); // IF statement

 assertFalse(isForIn(node));
 }

 @Test
 public void testIsStatementWithStatementNode() {
 Node node = new Node("print", new Node("stringLiteral", "Hello World")); // PRINT statement

 assertTrue(isStatement(node));
 }

 @Test
 public void testIsStatementWithExpressionNode() {
 Node node = new Node("1 + 1"); // Arithmetic expression

 assertFalse(isStatement(node));
 }

 @Test
 public void testIsFunctionDeclarationWithFunctionDeclarationNode() {
 Node node = new Node("function", new Node("identifier", "myFunc"), new Node("{});"));

 assertTrue(isFunctionDeclaration(node));
 }

 @Test
 public void testIsFunctionDeclarationWithFunctionExpressionNode() {
 Node node = new Node("function", new Node("identifier", "myFunc"), new Node("{});"), new Node("argumentList", null));

 assertFalse(isFunctionDeclaration(node));
 }

 @Test
 public void testIsFunctionDeclarationWithInvalidNode() {
 Node node = new Node("invalid"); // Invalid token

 assertFalse(isFunctionDeclaration(node));
 }

 @Test
 public void testIsHoistedFunctionDeclarationWithHoistedNode() {
 Node node = new Node("function", new Node("identifier", "myFunc"), new Node("{});"));
 Node parent = new Node("script", null);
 node.setParent(parent);

 assertTrue(isHoistedFunctionDeclaration(node));
 }

 @Test
 public void testIsHoistedFunctionDeclarationWithNotHoistedNode() {
 Node node = new Node("function", new Node("identifier", "myFunc"), new Node("{});"));
 Node parent = new Node("function", new Node("identifier", "myFunction"), new Node("{node}"));
 node.setParent(parent);

 assertFalse(isHoistedFunctionDeclaration(node));
 }
}