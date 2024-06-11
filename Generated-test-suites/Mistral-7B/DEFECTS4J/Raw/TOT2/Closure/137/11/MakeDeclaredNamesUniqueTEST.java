import com.google.javascript.jscomp.MakeDeclaredNamesUnique;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class MakeDeclaredNamesUniqueTest {

 private NodeTraversal nodeTraversal;
 private MakeDeclaredNamesUnique makeDeclaredNamesUnique;
 private Scope scope;

 @Before
 public void setup() {
 nodeTraversal = Mockito.mock(NodeTraversal.class);
 makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
 scope = Mockito.mock(Scope.class);
 }

 @Test
 public void testSimpleVariableDeclaration() {
 Node node = Mockito.mock(Node.class);
 Mockito.when(node.getString()).thenReturn("x");
 Mockito.when(node.getParentNode()).thenReturn(null);
 Mockito.when(scope.getVar("x")).thenReturn(new Var("x", node));

 MakeDeclaredNamesUnique.Renamer renamer = makeDeclaredNamesUnique.newInstance();

 makeDeclaredNamesUnique.enterScope(nodeTraversal);
 renamer.addDeclaredName("x");
 makeDeclaredNamesUnique.exitScope(nodeTraversal);

 String replacementName = makeDeclaredNamesUnique.getReplacementName("x");
 assertEquals("x", replacementName);
 }

 @Test
 public void testVariableDeclarationWithSameNameInDifferentScopes() {
 Node parentNode = Mockito.mock(Node.class);
 Node childNode = Mockito.mock(Node.class);
 Mockito.when(parentNode.getType()).thenReturn(Token.FUNCTION);
 Mockito.when(parentNode.getLastChild()).thenReturn(childNode);
 Mockito.when(childNode.getString()).thenReturn("x");
 Mockito.when(childNode.getParentNode()).thenReturn(parentNode);
 Mockito.when(scope.getVar("x")).thenReturn(new Var("x", childNode));

 MakeDeclaredNamesUnique.Renamer renamer1 = makeDeclaredNamesUnique.newInstance();
 makeDeclaredNamesUnique.enterScope(nodeTraversal);
 renamer1.addDeclaredName("x");
 makeDeclaredNamesUnique.exitScope(nodeTraversal);

 MakeDeclaredNamesUnique.Renamer renamer2 = makeDeclaredNamesUnique.newInstance();
 makeDeclaredNamesUnique.enterScope(nodeTraversal);
 renamer2.addDeclaredName("x");
 makeDeclaredNamesUnique.exitScope(nodeTraversal);

 String replacementName1 = makeDeclaredNamesUnique.getReplacementName("x");
 String replacementName2 = makeDeclaredNamesUnique.getReplacementName("x");
 assertEquals("x$0", replacementName1);
 assertEquals("x$1", replacementName2);
 }

 @Test
 public void testFunctionDeclaration() {
 Node functionNode = Mockito.mock(Node.class);
 Node nameNode = Mockito.mock(Node.class);
 Mockito.when(functionNode.getType()).thenReturn(Token.FUNCTION);
 Mockito.when(functionNode.getFirstChild()).thenReturn(nameNode);
 Mockito.when(nameNode.getString()).thenReturn("foo");
 Mockito.when(nodeTraversal.getScope()).thenReturn(scope);

 MakeDeclaredNamesUnique.Renamer renamer = makeDeclaredNamesUnique.newInstance();
 makeDeclaredNamesUnique.enterScope(nodeTraversal);
 makeDeclaredNamesUnique.addDeclaredName("foo");
 makeDeclaredNamesUnique.exitScope(nodeTraversal);

 String replacementName = makeDeclaredNamesUnique.getReplacementName("foo");
 assertEquals("foo", replacementName);
 }

 @Test(expected = IllegalStateException.class)
 public void testFunctionDeclarationWithSameNameInParentFunction() {
 Node parentFunctionNode = Mockito.mock(Node.class);
 Node childFunctionNode = Mockito.mock(Node.class);
 Node nameNode = Mockito.mock(Node.class);
 Mockito.when(parentFunctionNode.getType()).thenReturn(Token.FUNCTION);
 Mockito.when(parentFunctionNode.getLastChild()).thenReturn(nameNode);
 Mockito.when(nameNode.getString()).thenReturn("foo");
 Mockito.when(childFunctionNode.getType()).thenReturn(Token.FUNCTION);
 Mockito.when(childFunctionNode.getFirstChild()).thenReturn(nameNode);
 Mockito.when(nodeTraversal.getScope()).thenReturn(scope);

 MakeDeclaredNamesUnique.Renamer renamer = makeDeclaredNamesUnique.newInstance();
 makeDeclaredNamesUnique.enterScope(nodeTraversal);
 makeDeclaredNamesUnique.enterScope(nodeTraversal);
 makeDeclaredNamesUnique.addDeclaredName("foo");
 makeDeclaredNamesUnique.exitScope(nodeTraversal);
 makeDeclaredNamesUnique.exitScope(nodeTraversal);

 String replacementName = makeDeclaredNamesUnique.getReplacementName("foo");
 fail("Should have thrown an exception");
 }

 @Test
 public void testCatchClause() {
 Node catchNode = Mockito.mock(Node.class);
 Node nameNode = Mockito.mock(Node.class);
 Mockito.when(catchNode.getType()).thenReturn(Token.CATCH);
 Mockito.when(catchNode.getFirstChild()).thenReturn(nameNode);
 Mockito.when(nameNode.getString()).thenReturn("e");
 Mockito.when(nodeTraversal.getScope()).thenReturn(scope);

 MakeDeclaredNamesUnique.Renamer renamer = makeDeclaredNamesUnique.newInstance();
 makeDeclaredNamesUnique.enterScope(nodeTraversal);
 makeDeclaredNamesUnique.addDeclaredName("e");
 makeDeclaredNamesUnique.exitScope(nodeTraversal);

 String replacementName = makeDeclaredNamesUnique.getReplacementName("e");
 assertEquals("e", replacementName);
 }

 @Test
 public void testVariableDeclarationWithConst() {
 Node node = Mockito.mock(Node.class);
 Mockito.when(node.getString()).thenReturn("constX");
 Mockito.when(node.getParentNode()).thenReturn(null);
 Mockito.when(scope.getVar("constX")).thenReturn(new Var("constX", node));

 MakeDeclaredNamesUnique.Renamer renamer = makeDeclaredNamesUnique.newInstance();
 renamer.addDeclaredName("constX");

 String replacementName = makeDeclaredNamesUnique.getReplacementName("constX");
 assertEquals("constX", replacementName);

 // Const names should not be stripped
 assertFalse(renamer.stripConstIfReplaced());
 }

 @Test
 public void testVariableDeclarationWithConstAndRenamed() {
 Node node = Mockito.mock(Node.class);
 Mockito.when(node.getString()).thenReturn("constX");
 Mockito.when(node.getParentNode()).thenReturn(null);
 Mockito.when(scope.getVar("constX")).thenReturn(new Var("constX", node));

 MakeDeclaredNamesUnique.Renamer renamer = makeDeclaredNamesUnique.newInstance();
 renamer.addDeclaredName("constX");
 String originalName = renamer.getReplacementName("constX");
 assertEquals("constX", originalName);

 MakeDeclaredNamesUnique.Renamer renamer1 = makeDeclaredNamesUnique.newInstance();
 makeDeclaredNamesUnique.enterScope(nodeTraversal);
 renamer1.addDeclaredName("X");
 makeDeclaredNamesUnique.exitScope(nodeTraversal);

 String replacementName1 = renamer1.getReplacementName("X");
 assertEquals("X$0", replacementName1);

 String replacementName2 = renamer.getReplacementName("X");
 assertEquals("constX", replacementName2);
 }

 @Test
 public void testVariableDeclarationWithConstAndRenamedInDifferentScopes() {
 Node parentNode = Mockito.mock(Node.class);
 Node childNode = Mockito.mock(Node.class);
 Node node = Mockito.mock(Node.class);
 Mockito.when(parentNode.getType()).thenReturn(Token.FUNCTION);
 Mockito.when(parentNode.getLastChild()).thenReturn(childNode);
 Mockito.when(childNode.getType()).thenReturn(Token.BLOCK);
 Mockito.when(node.getString()).thenReturn("constX");
 Mockito.when(node.getParentNode()).thenReturn(childNode);
 Mockito.when(scope.getVar("constX")).thenReturn(new Var("constX", node));

 MakeDeclaredNamesUnique.Renamer renamer1 = makeDeclaredNamesUnique.newInstance();
 MakeDeclaredNamesUnique.Renamer renamer2 = makeDeclaredNamesUnique.newInstance();
 makeDeclaredNamesUnique.enterScope(nodeTraversal);
 makeDeclaredNamesUnique.enterScope(nodeTraversal);
 renamer1.addDeclaredName("X");
 renamer2.addDeclaredName("X");
 makeDeclaredNamesUnique.exitScope(nodeTraversal);
 makeDeclaredNamesUnique.exitScope(nodeTraversal);

 String replacementName1 = renamer1.getReplacementName("X");
 assertEquals("X$0", replacementName1);

 String replacementName2 = renamer2.getReplacementName("X");
 assertEquals("constX", replacementName2);
 }

 @Test
 public void testRenameVariableWithSameNameInDifferentScopes() {
 Node parentNode = Mockito.mock(Node.class);
 Node childNode1 = Mockito.mock(Node.class);
 Node childNode2 = Mockito.mock(Node.class);
 Node node = Mockito.mock(Node.class);
 Mockito.when(parentNode.getType()).thenReturn(Token.FUNCTION);
 Mockito.when(parentNode.getLastChild()).thenReturn(childNode1);
 Mockito.when(childNode1.getType()).thenReturn(Token.BLOCK);
 Mockito.when(childNode1.getLastChild()).thenReturn(node);
 Mockito.when(node.getString()).thenReturn("x");
 Mockito.when(childNode2.getType()).thenReturn(Token.BLOCK);
 Mockito.when(childNode2.getLastChild()).thenReturn(node);
 Mockito.when(scope.getVar("x")).thenReturn(new Var("x", node));

 MakeDeclaredNamesUnique.Renamer renamer1 = makeDeclaredNamesUnique.newInstance();
 makeDeclaredNamesUnique.enterScope(nodeTraversal);
 renamer1.addDeclaredName("x");
 makeDeclaredNamesUnique.exitScope(nodeTraversal);

 MakeDeclaredNamesUnique.Renamer renamer2 = makeDeclaredNamesUnique.newInstance();
 makeDeclaredNamesUnique.enterScope(nodeTraversal);
 renamer2.addDeclaredName("x");
 makeDeclaredNamesUnique.exitScope(nodeTraversal);

 String replacementName1 = renamer1.getReplacementName("x");
 String replacementName2 = renamer2.getReplacementName("x");
 assertEquals("x$0", replacementName1);
 assertEquals("x$1", replacementName2);
 }
}