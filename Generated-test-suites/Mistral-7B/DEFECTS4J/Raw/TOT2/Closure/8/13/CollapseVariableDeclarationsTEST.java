import com.google.javascript.jscomp.CollapseVariableDeclarations;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class CollapseVariableDeclarationsTest {

 private Compiler compiler;
 private CollapseVariableDeclarations collapseVariableDeclarations;

 @Before
 public void setup() {
 compiler = mock(Compiler.class);
 collapseVariableDeclarations = new CollapseVariableDeclarations(compiler);
 }

 @Test
 public void testSimpleVariableDeclarationCollapse() {
 Node rootNode = new Node(Token.PROGRAM);
 Node var1 = new Node(Token.VAR, "a");
 Node var2 = new Node(Token.VAR, "b");
 Node var3 = new Node(Token.VAR, "c");

 var1.addChildToBack(new Node(Token.SEMICOLON));
 var2.addChildToBack(new Node(Token.ASSIGN, Token.NULL));
 var3.addChildToBack(new Node(Token.ASSIGN, Token.INTEGER, 2));
 rootNode.addChildToBack(var1);
 rootNode.addChildToBack(var2);
 rootNode.addChildToBack(var3);

 collapseVariableDeclarations.process(null, rootNode);

 Node expected = new Node(Token.VAR, "a, b = null, c = 2");
 verify(compiler, times(1)).addCodeChange(eq(expected.toString()), eq(rootNode.getSourcePositionStart()));
 }

 @Test
 public void testSimpleVariableDeclarationWithAssignCollapse() {
 Node rootNode = new Node(Token.PROGRAM);
 Node var1 = new Node(Token.VAR, "a");
 Node assign1 = new Node(Token.ASSIGN, Token.TRUE);
 Node assign2 = new Node(Token.ASSIGN, Token.TRUE);
 Node var2 = new Node(Token.VAR, "b");

 var1.addChildToBack(assign1);
 var1.addChildToBack(assign2);
 var1.addChildToBack(var2);
 var2.addChildToBack(new Node(Token.SEMICOLON));
 rootNode.addChildToBack(var1);

 collapseVariableDeclarations.process(null, rootNode);

 Node expected = new Node(Token.VAR, "a = true, b");
 verify(compiler, times(1)).addCodeChange(eq(expected.toString()), eq(rootNode.getSourcePositionStart()));
 }

 @Test
 public void testVariableDeclarationCollapseWithinFunction() {
 Node rootNode = new Node(Token.PROGRAM);
 Node functionNode = new Node(Token.FUNCTION, null, null, null, null, null, 0, 0, null);
 Node var1 = new Node(Token.VAR, "a");
 Node var2 = new Node(Token.VAR, "b");
 Node var3 = new Node(Token.VAR, "c");
 Node var4 = new Node(Token.VAR, "d");

 var1.addChildToBack(new Node(Token.SEMICOLON));
 var2.addChildToBack(new Node(Token.ASSIGN, Token.STRING, "hello"));
 var3.addChildToBack(new Node(Token.ASSIGN, Token.INTEGER, 1));
 var4.addChildToBack(new Node(Token.ASSIGN, Token.INTEGER, 2));
 functionNode.addChildToBack(var1);
 functionNode.addChildToBack(var2);
 functionNode.addChildToBack(var3);
 functionNode.addChildToBack(var4);
 rootNode.addChildToBack(functionNode);

 collapseVariableDeclarations.process(null, rootNode);

 Node expected = new Node(Token.FUNCTION, null, null, null, null, null, 0, 0,
 new Node(Token.VAR, "a, b = 'hello', c = 1, d = 2"));
 verify(compiler, times(1)).addCodeChange(eq(expected.toString()), eq(rootNode.getSourcePositionStart()));
 }

 @Test(expected = AssertionError.class)
 public void testVariableDeclarationCollapseWithSameIdentifier() {
 Node rootNode = new Node(Token.PROGRAM);
 Node var1 = new Node(Token.VAR, "a");
 Node var2 = new Node(Token.VAR, "a");
 var1.addChildToBack(new Node(Token.SEMICOLON));
 var2.addChildToBack(new Node(Token.SEMICOLON));
 rootNode.addChildToBack(var1);
 rootNode.addChildToBack(var2);
 collapseVariableDeclarations.process(null, rootNode);
 }

 @Test
 public void testExceptionHandling() {
 Node rootNode = new Node(Token.PROGRAM);
 Node functionNode = new Node(Token.FUNCTION, null, null, null, null, null, 0, 0, null);
 Node var1 = new Node(Token.VAR, "a");
 Node var2 = new Node(Token.VAR, "b");
 Node throwStatement = new Node(Token.THROW, new Node(Token.STRING, "An error occurred"));
 Node catchNode = new Node(Token.CATCH, null, new Node(Token.IDENT, "e"), null);
 Node assign1 = new Node(Token.ASSIGN, Token.IDENT, "a");
 Node assign2 = new Node(Token.ASSIGN, Token.IDENT, "b");
 Node assign3 = new Node(Token.ASSIGN, Token.IDENT, "c");
 Node assign4 = new Node(Token.ASSIGN, Token.IDENT, "d");

 var1.addChildToBack(assign1);
 var1.addChildToBack(new Node(Token.SEMICOLON));
 var2.addChildToBack(assign2);
 var2.addChildToBack(catchNode);

 assign3 = new Node(Token.ASSIGN, Token.IDENT, "c");
 assign3.addChildToBack(new Node(Token.ASSIGN, Token.NULL));
 assign4 = new Node(Token.ASSIGN, Token.IDENT, "d");
 assign4.addChildToBack(new Node(Token.ASSIGN, Token.INTEGER, 2));
 catchNode.addChildToBack(assign3);
 catchNode.addChildToBack(assign4);
 catchNode.addChildToBack(new Node(Token.ASSIGN, Token.IDENT, "b"));
 catchNode.addChildToBack(new Node(Token.ASSIGN, Token.IDENT, "a"));
 catchNode.addChildToBack(throwStatement);
 functionNode.addChildToBack(var1);
 functionNode.addChildToBack(var2);
 rootNode.addChildToBack(functionNode);

 collapseVariableDeclarations.process(null, rootNode);

 Node expected = new Node(Token.FUNCTION, null, null, null, null, null, 0, 0,
 new Node(Token.VAR, "a, b = null, c = null, d = 2"));
 verify(compiler, times(1)).addCodeChange(eq(expected.toString()), eq(rootNode.getSourcePositionStart()));
 }
}