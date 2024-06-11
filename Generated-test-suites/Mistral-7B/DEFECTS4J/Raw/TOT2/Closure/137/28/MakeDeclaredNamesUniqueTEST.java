import com.google.javascript.jscomp.MakeDeclaredNamesUnique;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

public class MakeDeclaredNamesUniqueTest {

 private NodeTraversal nodeTraversal;
 private MakeDeclaredNamesUnique makeDeclaredNamesUnique;

 @Before
 public void setUp() {
 nodeTraversal = mock(NodeTraversal.class);
 makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
 }

 @Test
 public void testSimpleFunctionWithUniqueNames() {
 // Given
 Node functionNode = createNode("function test() {}");

 // When
 makeDeclaredNamesUnique.traverse(nodeTraversal, functionNode, null);

 // Then
 verify(nodeTraversal, times(1)).enterScope(any());
 verify(nodeTraversal, never()).exitScope(any());
 }

 @Test
 public void testFunctionWithDuplicateNames() {
 // Given
 Node functionNode = createNode("function test() {" +
 "var a = 1;" +
 "var a = 2;" +
 "}");

 // When
 makeDeclaredNamesUnique.traverse(nodeTraversal, functionNode, null);

 // Then
 verify(nodeTraversal, times(1)).enterScope(any());
 verify(nodeTraversal, times(2)).addDeclaredName(eq("a"));
 verify(nodeTraversal, atLeastOnce()).exitScope(any());
 }

 @Test
 public void testFunctionWithGlobalVariables() {
 // Given
 Node functionNode = createNode("function test() {" +
 "var a = 1;" +
 "var b = 2;" +
 "}");
 Node rootNode = createNode("var a = 3; function test() {}");

 // When
 makeDeclaredNamesUnique.traverse(nodeTraversal, functionNode, rootNode);

 // Then
 verify(nodeTraversal, times(1)).enterScope(any());
 verify(nodeTraversal, times(1)).addDeclaredName(eq("a"));
 verify(nodeTraversal, times(1)).addDeclaredName(eq("b"));
 verify(nodeTraversal, atLeastOnce()).exitScope(any());
 }

 @Test
 public void testFunctionWithCatchBlock() {
 // Given
 Node functionNode = createNode("function test() {" +
 "try {" +
 "  throw new Error('test');" +
 "} catch (e) {" +
 "  var a = e.message;" +
 "}");

 // When
 makeDeclaredNamesUnique.traverse(nodeTraversal, functionNode, null);

 // Then
 verify(nodeTraversal, times(1)).enterScope(any());
 verify(nodeTraversal, times(1)).addDeclaredName(eq("a"));
 verify(nodeTraversal, atLeastOnce()).exitScope(any());
 }

 @Test(expected = AssertionError.class)
 public void testDuplicateFunctionNames() {
 // Given
 Node functionNode1 = createNode("function test() {}");
 Node functionNode2 = createNode("function test() {}");

 // When
 makeDeclaredNamesUnique.traverse(nodeTraversal, functionNode1, null);
 makeDeclaredNamesUnique.traverse(nodeTraversal, functionNode2, null);

 // Then
 fail("Expected an error when trying to rename a function with a name that " +
 "already exists.");
 }

 @Test
 public void testFunctionWithRecursiveCall() {
 // Given
 Node functionNode = createNode("function test() {" +
 "  test();" +
 "}");

 // When
 makeDeclaredNamesUnique.traverse(nodeTraversal, functionNode, null);

 // Then
 verify(nodeTraversal, times(1)).enterScope(any());
 verify(nodeTraversal, times(1)).addDeclaredName(eq("test"));
 verify(nodeTraversal, times(1)).exitScope(any());
 }

 private Node createNode(String code) {
 TokenStream tokenStream = new TokenStream(new StringReader(code));
 Node rootNode = new Token(Token.PROGRAM, 0.0, 0.0, "", tokenStream);
 NodeTraversal.traverse(new Compiler(), rootNode, null);
 return rootNode;
 }
}