import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Behavior;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Reference;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceCollection;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceMap;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class InlineObjectLiteralsTest {

 private Compiler compiler;
 private CompilerPass inlineObjectLiterals;

 @Before
 public void setUp() {
 compiler = new Compiler();
 inlineObjectLiterals = new InlineObjectLiterals(compiler, () -> "tempVar");
 }

 @Test
 public void testInlineSimpleObjectLiteral() {
 Node root = Node.objectLiteral(
 Node.newProperty("a", Node.newNumber(1)),
 Node.newProperty("b", Node.newNumber(2)));
 ReferenceCollection referenceCollection = new ReferenceCollection();
 inlineObjectLiterals.process(null, root);

 // Check that variables were created for 'a' and 'b'
 assertEquals(2, referenceCollection.size());

 // Check that 'a' and 'b' are no longer object literals
 assertFalse(root.getType() == Token.OBJECTLIT);
 assertFalse(referenceCollection.getReferences(0).get(0).getNode().getType() == Token.OBJECTLIT);
 assertFalse(referenceCollection.getReferences(1).get(0).getNode().getType() == Token.OBJECTLIT);
 }

 @Test
 public void testInlineNestedObjectLiteral() {
 Node root = Node.objectLiteral(
 Node.newProperty("a", Node.objectLiteral(
 Node.newProperty("c", Node.newNumber(1)),
 Node.newProperty("d", Node.newNumber(2)))));
 ReferenceCollection referenceCollection = new ReferenceCollection();
 inlineObjectLiterals.process(null, root);

 // Check that variables were created for 'a.c' and 'a.d'
 assertEquals(2, referenceCollection.size());

 // Check that 'a.c' and 'a.d' are no longer object literals
 assertFalse(root.getType() == Token.OBJECTLIT);
 assertFalse(referenceCollection.getReferences(0).get(0).getNode().getType() == Token.OBJECTLIT);
 assertFalse(referenceCollection.getReferences(1).get(0).getNode().getType() == Token.OBJECTLIT);
 }

 @Test
 public void testInlineEmptyObjectLiteral() {
 Node root = Node.objectLiteral();
 ReferenceCollection referenceCollection = new ReferenceCollection();
 inlineObjectLiterals.process(null, root);

 // Check that no variables were created
 assertEquals(0, referenceCollection.size());
 }

 @Test(expected = AssertionError.class)
 public void testInlineNonObjectLiteral() {
 Node root = Node.newNumber(1);
 ReferenceCollection referenceCollection = new ReferenceCollection();
 inlineObjectLiterals.process(null, root);
 }

 @Test
 public void testInlineSelfReferentialObjectLiteral() {
 Node root = Node.objectLiteral(
 Node.newProperty("a", Node.newObjectLiteral(
 Node.newProperty("b", Node.newString("a.a")),
 Node.newProperty("c", Node.newNumber(1)))));
 ReferenceCollection referenceCollection = new ReferenceCollection();
 inlineObjectLiterals.process(null, root);

 // Check that variables were created for 'a.b' and 'a.c'
 assertEquals(2, referenceCollection.size());

 // Check that 'a.b' and 'a.c' are no longer object literals
 assertFalse(root.getType() == Token.OBJECTLIT);
 assertFalse(referenceCollection.getReferences(0).get(0).getNode().getType() == Token.OBJECTLIT);
 assertFalse(referenceCollection.getReferences(1).get(0).getNode().getType() == Token.OBJECTLIT);
 }

 @Test
 public void testInlineObjectLiteralWithFunctionProperty() {
 Node root = Node.objectLiteral(
 Node.newProperty("a", Node.newFunction(null, null, Node.newBlock(
 Node.newReturn(Node.newString("hello"))))));
 ReferenceCollection referenceCollection = new ReferenceCollection();
 inlineObjectLiterals.process(null, root);

 // Check that a variable was created for 'a'
 assertEquals(1, referenceCollection.size());

 // Check that 'a' is no longer an object literal
 assertFalse(root.getType() == Token.OBJECTLIT);

 // Check that the function property is still a function
 Node function = referenceCollection.getReferences(0).get(0).getNode();
 assertTrue(function.getType() == Token.FUNCTION);
 assertEquals(1, function.getNumChildren());
 }

 @Test
 public void testInlineObjectLiteralWithExportedProperty() {
 Node root = Node.objectLiteral(
 Node.newProperty("exported", Node.newNumber(1)));
 ReferenceCollection referenceCollection = new ReferenceCollection();
 inlineObjectLiterals.process(null, root);

 // Check that 'exported' is still an object literal
 assertTrue(root.getType() == Token.OBJECTLIT);

 // Check that no variables were created
 assertEquals(0, referenceCollection.size());
 }

 @Test
 public void testInlineObjectLiteralWithGlobalProperty() {
 Node root = Node.objectLiteral(
 Node.newProperty("global", Node.newNumber(1)));
 ReferenceCollection referenceCollection = new ReferenceCollection();
 inlineObjectLiterals.process(null, root);

 // Check that 'global' is still an object literal
 assertTrue(root.getType() == Token.OBJECTLIT);

 // Check that no variables were created
 assertEquals(0, referenceCollection.size());
 }

 @Test
 public void testInlineObjectLiteralWithStaleReference() {
 Node root = Node.objectLiteral(
 Node.newProperty("stale", Node.newString("x")));
 ReferenceCollection referenceCollection = new ReferenceCollection();
 inlineObjectLiterals.process(null, root);

 // Check that 'stale' is still an object literal
 assertTrue(root.getType() == Token.OBJECTLIT);

 // Check that no variables were created
 assertEquals(0, referenceCollection.size());

 // Check that stale references to 'x' are ignored
 Node refNode = new Node(Token.GETPROP, Node.newString("stale"), NodeUtil.newUndefinedNode(null));
 inlineObjectLiterals.blacklistVarReferencesInTree(refNode, null);
 assertEquals("x", refNode.getLastChild().getString());
 }
}