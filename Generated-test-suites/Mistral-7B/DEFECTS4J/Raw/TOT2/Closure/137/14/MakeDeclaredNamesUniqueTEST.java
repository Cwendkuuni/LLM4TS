import com.google.javascript.jscomp.MakeDeclaredNamesUnique;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class MakeDeclaredNamesUniqueTest {

 private NodeTraversal nodeTraversal;
 private MakeDeclaredNamesUnique makeDeclaredNamesUnique;

 @Before
 public void setup() {
 nodeTraversal = mock(NodeTraversal.class);
 makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
 makeDeclaredNamesUnique.setTraversalCallback(makeDeclaredNamesUnique);
 }

 @Test
 public void testBasicFunctionDeclaration() {
 Node functionNode = createNodeFunctionDeclaration("testFunction");
 when(nodeTraversal.traverse(any(Node.class), any(Node.class), any(Node.class)))
 .thenReturn(true);
 makeDeclaredNamesUnique.visit(nodeTraversal, functionNode, null);
 verify(nodeTraversal).traverse(eq(functionNode), any(Node.class), any(Node.class));

 String newName = makeDeclaredNamesUnique.getReplacementName("testFunction");
 assertEquals("testFunction$$0", newName);
 }

 @Test
 public void testBasicVariableDeclaration() {
 Node variableNode = createNodeVariableDeclaration("testVar");
 when(nodeTraversal.traverse(any(Node.class), any(Node.class), any(Node.class)))
 .thenReturn(true);
 makeDeclaredNamesUnique.visit(nodeTraversal, variableNode, null);
 verify(nodeTraversal).traverse(eq(variableNode), any(Node.class), any(Node.class));

 String newName = makeDeclaredNamesUnique.getReplacementName("testVar");
 assertEquals("testVar$$0", newName);
 }

 @Test
 public void testFunctionAndVariableDeclarationWithSameNameInSameScope() {
 Node functionNode = createNodeFunctionDeclaration("testFunction");
 Node variableNode = createNodeVariableDeclaration("testFunction");
 when(nodeTraversal.traverse(any(Node.class), any(Node.class), any(Node.class)))
 .thenReturn(true);
 makeDeclaredNamesUnique.enterScope(nodeTraversal);
 makeDeclaredNamesUnique.visit(nodeTraversal, functionNode, null);
 makeDeclaredNamesUnique.visit(nodeTraversal, variableNode, null);
 makeDeclaredNamesUnique.exitScope(nodeTraversal);

 String functionNewName = makeDeclaredNamesUnique.getReplacementName("testFunction");
 String variableNewName = makeDeclaredNamesUnique.getReplacementName("testFunction");

 assertEquals("testFunction$$0", functionNewName);
 assertEquals("testFunction$$1", variableNewName);
 }

 @Test
 public void testFunctionWithParametersAndVariableWithSameNameInSameScope() {
 Node functionNode = createNodeFunctionDeclarationWithParameters("testFunction", "param1", "param2");
 Node variableNode = createNodeVariableDeclaration("testFunction");
 when(nodeTraversal.traverse(any(Node.class), any(Node.class), any(Node.class)))
 .thenReturn(true);
 makeDeclaredNamesUnique.enterScope(nodeTraversal);
 makeDeclaredNamesUnique.visit(nodeTraversal, functionNode, null);
 makeDeclaredNamesUnique.visit(nodeTraversal, variableNode, null);
 makeDeclaredNamesUnique.exitScope(nodeTraversal);

 String functionNewName = makeDeclaredNamesUnique.getReplacementName("testFunction");
 String variableNewName = makeDeclaredNamesUnique.getReplacementName("testFunction");

 assertEquals("testFunction$$0", functionNewName);
 assertEquals("testFunction$$2", variableNewName);
 }

 @Test
 public void testNestedFunctionAndVariableDeclarationWithSameName() {
 Node outerFunctionNode = createNodeFunctionDeclaration("outerFunction");
 Node outerVariableNode = createNodeVariableDeclaration("outerFunction");
 Node innerFunctionNode = createNodeFunctionDeclaration("innerFunction");
 Node innerVariableNode = createNodeVariableDeclaration("innerFunction");
 when(nodeTraversal.traverse(any(Node.class), any(Node.class), any(Node.class)))
 .thenReturn(true);
 makeDeclaredNamesUnique.enterScope(nodeTraversal);
 makeDeclaredNamesUnique.enterScope(nodeTraversal);
 makeDeclaredNamesUnique.visit(nodeTraversal, outerFunctionNode, outerVariableNode);
 makeDeclaredNamesUnique.visit(nodeTraversal, innerFunctionNode, innerVariableNode);
 makeDeclaredNamesUnique.exitScope(nodeTraversal);
 makeDeclaredNamesUnique.exitScope(nodeTraversal);

 String outerFunctionNewName = makeDeclaredNamesUnique.getReplacementName("outerFunction");
 String outerVariableNewName = makeDeclaredNamesUnique.getReplacementName("outerFunction");
 String innerFunctionNewName = makeDeclaredNamesUnique.getReplacementName("innerFunction");
 String innerVariableNewName = makeDeclaredNamesUnique.getReplacementName("innerFunction");

 assertEquals("outerFunction$$0", outerFunctionNewName);
 assertEquals("outerFunction$$1", outerVariableNewName);
 assertEquals("innerFunction$$3", innerFunctionNewName);
 assertEquals("innerFunction$$4", innerVariableNewName);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullNode() {
 makeDeclaredNamesUnique.visit(nodeTraversal, null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNonFunctionNode() {
 Node nonFunctionNode = createNodeIntLiteral(42);
 makeDeclaredNamesUnique.visit(nodeTraversal, nonFunctionNode, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullParentNode() {
 Node functionNode = createNodeFunctionDeclaration("testFunction");
 makeDeclaredNamesUnique.visit(nodeTraversal, functionNode, null);
 }

 private Node createNodeFunctionDeclaration(String name) {
 return createNodeFunctionDeclaration(name, null, null);
 }

 private Node createNodeFunctionDeclarationWithParameters(String name, String... parameters) {
 Node functionNode = new Node(Token.FUNCTION);
 functionNode.setString("function", name);
 Node fnParams = new Node(Token.NAME);
 fnParams.setString("params", "[" + String.join(",", Arrays.asList(parameters)) + "]");
 Node functionBody = new Node(Token.BLOCK);
 functionNode.addChild(fnParams);
 functionNode.addChild(functionBody);
 return functionNode;
 }

 private Node createNodeVariableDeclaration(String name) {
 return new Node(Token.VAR);
 }

 private Node createNodeVariableDeclarationWithInitializer(String name, String initializer) {
 Node varNode = new Node(Token.VAR);
 Node varInitNode = new Node(Token.ASSIGN);
 varInitNode.setRight(new Node(Token.STRING, initializer));
 varNode.addChild(varInitNode);
 varNode.setString("name", name);
 return varNode;
 }

 private Node createNodeIntLiteral(int value) {
 return new Node(Token.INT_LITERAL, value);
 }
}