import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MustBeReachingVariableDefTest {

 private MustBeReachingVariableDef analysis;
 private AbstractCompiler compiler;
 private Scope jsScope;
 private ControlFlowGraph<Node> cfg;

 @Before
 public void setup() {
 jsScope = new Scope();
 compiler = new AbstractCompiler(jsScope);
 analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
 }

 @Test
 public void testGetDef_LocalVariable_ReturnsDefinitionNode() {
 String name = "localVariable";
 Node useNode = Node.newLiteral(Token.IDENT, name);
 Node definitionNode = Node.newLiteral(Token.ASSIGN, null);
 analysis.cfg.addNode(definitionNode);
 analysis.cfg.addNode(useNode);
 analysis.getDef(name, useNode).accept(new NodePrinter());
 assertEquals(definitionNode.toString(), analysis.getDef(name, useNode).toString());
 }

 @Test
 public void testGetDef_NonExistentVariable_ReturnsNull() {
 String name = "nonExistentVariable";
 Node useNode = Node.newLiteral(Token.IDENT, name);
 analysis.cfg.addNode(useNode);
 assertNull(analysis.getDef(name, useNode));
 }

 @Test
 public void testGetDef_VariableInDifferentScope_ReturnsNull() {
 String name = "differentScopeVariable";
 Node useNode = Node.newLiteral(Token.IDENT, name);
 Scope otherScope = new Scope();
 analysis.jsScope.setChildScope(otherScope);
 otherScope.setVar(name, Node.newName(name, null));
 analysis.cfg.addNode(useNode);
 assertNull(analysis.getDef(name, useNode));
 }

 @Test
 public void testGetDef_VariableDefinedMultipleTimes_ReturnsLastDefinition() {
 String name = "multipleDefinitionVariable";
 Node firstDefinitionNode = Node.newLiteral(Token.ASSIGN, null);
 Node secondDefinitionNode = Node.newLiteral(Token.ASSIGN, null);
 analysis.cfg.addNode(firstDefinitionNode);
 analysis.cfg.addNode(secondDefinitionNode);
 analysis.cfg.addEdge(firstDefinitionNode, secondDefinitionNode, null);
 Node useNode = Node.newLiteral(Token.IDENT, name);
 analysis.cfg.addNode(useNode);
 assertEquals(secondDefinitionNode.toString(), analysis.getDef(name, useNode).toString());
 }

 @Test
 public void testDependOnOuterScopeVars_LocalVariable_ReturnsFalse() {
 String name = "localVariable";
 Node useNode = Node.newLiteral(Token.IDENT, name);
 Node definitionNode = Node.newLiteral(Token.ASSIGN, null);
 analysis.cfg.addNode(definitionNode);
 analysis.cfg.addNode(useNode);
 assertFalse(analysis.dependsOnOuterScopeVars(name, useNode));
 }

 @Test
 public void testDependOnOuterScopeVars_GlobalVariable_ReturnsTrue() {
 String name = "globalVariable";
 Node useNode = Node.newLiteral(Token.IDENT, name);
 analysis.cfg.addNode(useNode);
 assertTrue(analysis.dependsOnOuterScopeVars(name, useNode));
 }

 @Test
 public void testDependOnOuterScopeVars_VariableInDifferentScope_ReturnsTrue() {
 String name = "differentScopeVariable";
 Node useNode = Node.newLiteral(Token.IDENT, name);
 Scope otherScope = new Scope();
 analysis.jsScope.setChildScope(otherScope);
 otherScope.setVar(name, Node.newName(name, null));
 analysis.cfg.addNode(useNode);
 assertTrue(analysis.dependsOnOuterScopeVars(name, useNode));
 }
}