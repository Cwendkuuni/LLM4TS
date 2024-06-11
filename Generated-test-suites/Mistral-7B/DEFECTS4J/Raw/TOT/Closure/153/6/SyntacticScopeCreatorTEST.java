import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.jscomp.ScopeCreator.RedeclarationHandler;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeUtil;

public class SyntheticScopeCreatorTest {

 private AbstractCompiler compiler;
 private ScopeCreator syntacticScopeCreator;
 private RedeclarationHandler redeclarationHandler;

 @Before
 public void setUp() {
 compiler = mock(AbstractCompiler.class);
 redeclarationHandler = new DefaultRedeclarationHandler();
 syntacticScopeCreator = new SyntacticScopeCreator(compiler, redeclarationHandler);
 }

 @Test
 public void testDeclareVar() {
 Node root = Mockito.mock(Node.class);
 Node parent = Mockito.mock(Node.class);
 Node n = Mockito.mock(Node.class);
 Node gramps = Mockito.mock(Node.class);
 Node nodeWithLineNumber = Mockito.mock(Node.class);

 String name = "testVar";
 String sourceName = "testSource";

 String testType = "String";
 JSType declaredType = Mockito.mock(JSType.class);
 JSDocInfo jsDocInfo = Mockito.mock(JSDocInfo.class);

 Mockito.when(n.getString()).thenReturn(name);
 Mockito.when(n.getParentNode()).thenReturn(parent);
 Mockito.when(n.getProp(Node.SOURCENAME_PROP)).thenReturn(sourceName);
 Mockito.when(declaredType.getName()).thenReturn(testType);
 Mockito.when(jsDocInfo.getSuppressions()).thenReturn(new String[]{"duplicate"});
 Mockito.when(parent.getJSDocInfo()).thenReturn(jsDocInfo);
 Mockito.when(nodeWithLineNumber.getParentNode()).thenReturn(n);

 syntacticScopeCreator.declareVar(name, n, parent, gramps, declaredType, nodeWithLineNumber);

 verify(compiler).report(Mockito.never());

 Scope expectedScope = new Scope(parent, root);
 expectedScope.declare(name, n, declaredType, null);
 verify(scope).declare(eq(name), eq(n), eq(declaredType), any());
 }

 @Test
 public void testRedeclarationHandlerOnRedeclaration() {
 Node root = Mockito.mock(Node.class);
 Node parent = Mockito.mock(Node.class);
 Node redeclaredNode = Mockito.mock(Node.class);
 Node gramps = Mockito.mock(Node.class);
 Node nodeWithLineNumber = Mockito.mock(Node.class);

 String name = "testVar";
 String sourceName = "testSource";

 syntacticScopeCreator.redeclarationHandler.onRedeclaration(
     Mockito.mock(Scope.class), name, redeclaredNode, parent, gramps, nodeWithLineNumber);

 // Check that no error is reported when redeclaring a variable at the global level
 verify(compiler, never()).report(any());

 // Check that an error is reported when redeclaring a variable in a local scope
 Node localParent = Mockito.mock(Node.class);
 Mockito.when(localParent.getType()).thenReturn(Token.BLOCK);
 Mockito.when(redeclaredNode.getType()).thenReturn(Token.VAR);

 syntacticScopeCreator.redeclarationHandler.onRedeclaration(
     Mockito.mock(Scope.class), name, redeclaredNode, localParent, gramps, nodeWithLineNumber);

 verify(compiler).report(
     Mockito.atLeastOnce().andReturn(
         JSError.make(sourceName, redeclaredNode,
                      SyntacticScopeCreator.VAR_MULTIPLY_DECLARED_ERROR,
                      name, "testVar")));
 }

 @Test
 public void testRedeclarationHandlerOnArgumentsDeclaration() {
 Node root = Mockito.mock(Node.class);
 Node parent = Mockito.mock(Node.class);
 Node redeclaredNode = Mockito.mock(Node.class);
 Node gramps = Mockito.mock(Node.class);
 Node nodeWithLineNumber = Mockito.mock(Node.class);

 String name = "arguments";
 String sourceName = "testSource";

 syntacticScopeCreator.redeclarationHandler.onRedeclaration(
     Mockito.mock(Scope.class), name, redeclaredNode, parent, gramps, nodeWithLineNumber);

 verify(compiler).report(
     Mockito.atLeastOnce().andReturn(
         JSError.make(sourceName, redeclaredNode,
                      SyntacticScopeCreator.VAR_ARGUMENTS_SHADOWED_ERROR)));
 }
}