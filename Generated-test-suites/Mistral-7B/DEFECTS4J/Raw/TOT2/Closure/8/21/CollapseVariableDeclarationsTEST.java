import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.NodeTraversal;

import java.util.Arrays;
import java.util.List;

public class CollapseVariableDeclarationsTest {

 private Compiler compiler;
 private Scope mockScope;
 private NodeTraversal mockNodeTraversal;

 @Before
 public void setup() {
 compiler = mock(Compiler.class);
 mockScope = mock(Scope.class);
 mockNodeTraversal = new NodeTraversal(compiler, mockScope);
 }

 @Test
 public void testCollapsingSimpleVarDeclarations() {
 // Given
 Node root = new Node(Token.PROGRAM);
 Node varA = new Node(Token.VAR, "a");
 Node varB = new Node(Token.VAR, "b");
 Node varC = new Node(Token.VAR, "c");
 root.addChildToBack(varA);
 root.addChildToBack(varB);
 root.addChildToBack(varC);

 // When
 CompilerPass pass = new CollapseVariableDeclarations(compiler);
 pass.process(null, root);

 // Then
 Node expected = new Node(Token.VAR, "a");
 expected.setJSDocInfo(VarDeclarationTestUtil.getSimpleJSDocInfo("a"));
 Node actual = findVariableDecl(root, "a");
 assertEquals(expected, actual);

 Node expectedB = new Node(Token.VAR, "b, c");
 expectedB.setJSDocInfo(VarDeclarationTestUtil.getSimpleJSDocInfo("b, c"));
 Node actualB = findVariableDecl(root, "b");
 assertEquals(expectedB, actualB);
 }

 @Test
 public void testCollapsingAssignments() {
 // Given
 Node root = new Node(Token.PROGRAM);
 Node varA = new Node(Token.VAR, "a");
 Node assignA = new Node(Token.ASSIGN, new Node(Token.NAME, "a"), new Node(Token.TRUE));
 Node varB = new Node(Token.VAR, "b");
 Node assignB = new Node(Token.ASSIGN, new Node(Token.NAME, "b"), new Node(Token.TRUE));
 Node varC = new Node(Token.VAR, "c");
 root.addChildToBack(varA);
 root.addChildToBack(assignA);
 root.addChildToBack(varB);
 root.addChildToBack(assignB);
 root.addChildToBack(varC);

 // When
 CompilerPass pass = new CollapseVariableDeclarations(compiler);
 pass.process(null, root);

 // Then
 Node expected = new Node(Token.VAR, "a, b, c");
 expected.setJSDocInfo(VarDeclarationTestUtil.getSimpleJSDocInfo("a, b, c"));
 Node actual = findVariableDecl(root, "a");
 assertEquals(expected, actual);
 }

 @Test
 public void testCollapsingVarDeclarationsWithinFunction() {
 // Given
 Node root = new Node(Token.PROGRAM);
 Node functionNode = new Node(Token.FUNCTION, null, null, null);
 Node varA = new Node(Token.VAR, "a");
 Node varB = new Node(Token.VAR, "b");
 Node varC = new Node(Token.VAR, "c");
 functionNode.addChildToBack(varA);
 functionNode.addChildToBack(varB);
 functionNode.addChildToBack(varC);
 root.addChildToBack(new Node(Token.BLOCK, null, Arrays.asList(functionNode)));

 // When
 CompilerPass pass = new CollapseVariableDeclarations(compiler);
 pass.process(null, root);

 // Then
 Node expected = new Node(Token.VAR, "a, b, c");
 expected.setJSDocInfo(VarDeclarationTestUtil.getFunctionJSDocInfo("a, b, c"));
 Node actual = findVariableDecl(root, "a");
 assertEquals(expected, actual);
 }

 @Test
 public void testCollapsingOverlappingVarDeclarations() {
 // Given
 Node root = new Node(Token.PROGRAM);
 Node varA = new Node(Token.VAR, "a");
 Node varB = new Node(Token.VAR, "a");
 Node varC = new Node(Token.VAR, "c");
 root.addChildToBack(varA);
 root.addChildToBack(varB);
 root.addChildToBack(varC);

 // When
 CompilerPass pass = new CollapseVariableDeclarations(compiler);
 pass.process(null, root);
 // Then
 Node expected = new Node(Token.VAR, "a, c");
 expected.setJSDocInfo(VarDeclarationTestUtil.getOverlappingJSDocInfo("a, c"));
 Node actual = findVariableDecl(root, "a");
 assertEquals(expected, actual);
 }

 @Test(expected = AssertionError.class)
 public void testCollapsingDuplicateVarDeclarations() {
 // Given
 Node root = new Node(Token.PROGRAM);
 Node varA = new Node(Token.VAR, "a");
 Node varA2 = new Node(Token.VAR, "a");
 root.addChildToBack(varA);
 root.addChildToBack(varA2);

 // When
 CompilerPass pass = new CollapseVariableDeclarations(compiler);
 pass.process(null, root);
 }

 @Test
 public void testCollapsingVarDeclarationsWithComment() {
 // Given
 Node root = new Node(Token.PROGRAM);
 Node varA = new Node(Token.VAR, "a");
 Node comment = new Node(Token.COMMENT, "/* this is a comment */");
 Node varB = new Node(Token.VAR, "b");
 root.addChildToBack(varA);
 root.addChildToBack(comment);
 root.addChildToBack(varB);

 // When
 CompilerPass pass = new CollapseVariableDeclarations(compiler);
 pass.process(null, root);

 // Then
 Node expected = new Node(Token.VAR, "a, b");
 expected.setJSDocInfo(VarDeclarationTestUtil.getSimpleJSDocInfo("a, b"));
 Node actual = findVariableDecl(root, "a");
 assertEquals(expected, actual);
 }

 @Test
 public void testCollapsingVarDeclarationsWithJSDoc() {
 // Given
 Node root = new Node(Token.PROGRAM);
 Node varA = new Node(Token.VAR, "a");
 Node jsDocInfoA = VarDeclarationTestUtil.getSimpleJSDocInfo("a");
 varA.setJSDocInfo(jsDocInfoA);
 Node varB = new Node(Token.VAR, "b");
 Node jsDocInfoB = VarDeclarationTestUtil.getSimpleJSDocInfo("b");
 varB.setJSDocInfo(jsDocInfoB);
 root.addChildToBack(varA);
 root.addChildToBack(varB);

 // When
 CompilerPass pass = new CollapseVariableDeclarations(compiler);
 pass.process(null, root);

 // Then
 Node expected = new Node(Token.VAR, "a, b");
 expected.setJSDocInfo(jsDocInfoA);
 Node actual = findVariableDecl(root, "a");
 assertEquals(expected, actual);
 }

 @Test
 public void testCollapsingVarDeclarationsWithJSDocAndComment() {
 // Given
 Node root = new Node(Token.PROGRAM);
 Node varA = new Node(Token.VAR, "a");
 Node jsDocInfoA = VarDeclarationTestUtil.getSimpleJSDocInfo("a");
 varA.setJSDocInfo(jsDocInfoA);
 Node comment = new Node(Token.COMMENT, "/* this is a comment */");
 Node varB = new Node(Token.VAR, "b");
 Node jsDocInfoB = VarDeclarationTestUtil.getSimpleJSDocInfo("b");
 varB.setJSDocInfo(jsDocInfoB);
 root.addChildToBack(varA);
 root.addChildToBack(comment);
 root.addChildToBack(varB);

 // When
 CompilerPass pass = new CollapseVariableDeclarations(compiler);
 pass.process(null, root);

 // Then
 Node expected = new Node(Token.VAR, "a, b");
 expected.setJSDocInfo(jsDocInfoA);
 Node actual = findVariableDecl(root, "a");
 assertEquals(expected, actual);
 }

 private Node findVariableDecl(Node node, String variableName) {
 Node result = null;
 for (Node child = node.getFirstChild(); child != null; child = child.getNext()) {
 if (child.isVar() && child.getString().equals(variableName)) {
 result = child;
 break;
 }
 }
 return result;
 }
}