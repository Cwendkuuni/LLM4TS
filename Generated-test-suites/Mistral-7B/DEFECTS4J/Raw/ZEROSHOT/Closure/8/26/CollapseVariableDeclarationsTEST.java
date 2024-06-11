import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.JSDocInfo;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

public class CollapseVariableDeclarationsTest {

 private AbstractCompiler compiler;
 private CollapseVariableDeclarations collapseVariableDeclarations;

 @Before
 public void setUp() {
 compiler = mock(AbstractCompiler.class);
 collapseVariableDeclarations = new CollapseVariableDeclarations(compiler);
 }

 @Test
 public void testCollapseVariableDeclarations() {
 // Given
 Node root = NodeUtil.createTestNode("var a; var b = 1; var c = 2;");

 // When
 collapseVariableDeclarations.process(NodeUtil.createNodeObject(), root);

 // Then
 Node expected = NodeUtil.createTestNode("var a, b = 1, c = 2;");
 assertEquals(expected.toString(), root.toString());

 verify(compiler, times(1)).reportCodeChange();
 }

 @Test
 public void testCollapseVariableAssignments() {
 // Given
 Node root = NodeUtil.createTestNode("var a = true; var b = true; var c = true;");

 // When
 collapseVariableDeclarations.process(NodeUtil.createNodeObject(), root);

 // Then
 Node expected = NodeUtil.createTestNode("var c = b = a = true;");
 assertEquals(expected.toString(), root.toString());

 verify(compiler, times(1)).reportCodeChange();
 }

 @Test
 public void testCollapseVariableDeclarationsInsideFunction() {
 // Given
 String code = "function test() {\n" +
               "  var a; \n" +
               "  var b = 1; \n" +
               "  var c = 2; \n" +
               "}";
 Node root = NodeUtil.createTestNode(code);

 // When
 collapseVariableDeclarations.process(NodeUtil.createNodeObject(), root);

 // Then
 String expected = "function test() {\n" +
                   "  var a, b = 1, c = 2;\n" +
                   "}";
 assertEquals(expected, root.toString());

 verify(compiler, times(1)).reportCodeChange();
 }

 @Test
 public void testCollapseVariableDeclarationsInsideLoop() {
 // Given
 String code = "function test() {\n" +
               "  for (var i = 0; i < 10; i++) {\n" +
               "    var a; \n" +
               "    var b = 1; \n" +
               "    var c = 2; \n" +
               "  }\n" +
               "}";
 Node root = NodeUtil.createTestNode(code);

 // When
 collapseVariableDeclarations.process(NodeUtil.createNodeObject(), root);

 // Then
 String expected = "function test() {\n" +
                   "  for (var i = 0; i < 10; i++) {\n" +
                   "    var a, b = 1, c = 2;\n" +
                   "  }\n" +
                   "}";
 assertEquals(expected, root.toString());

 verify(compiler, times(1)).reportCodeChange();
 }

 @Test
 public void testCollapseVariablesWithJSDocInfo() {
 // Given
 String code = "/**\n" +
               "@interface Test {\n" +
               "  var a: number;\n" +
               "  var b: string;\n" +
               "} \n" +
               "function test() {\n" +
               "  var a: number;\n" +
               "  var b: string;\n" +
               "}";
 Node root = NodeUtil.createTestNode(code);

 // When
 collapseVariableDeclarations.process(NodeUtil.createNodeObject(), root);

 // Then
 String expected = "/**\n" +
                   "@interface Test {\n" +
                   "  var a: number, b: string;\n" +
                   "} \n" +
                   "function test() {\n" +
                   "  var a: number, b: string;\n" +
                   "}";
 assertEquals(expected, root.toString());

 verify(compiler, times(1)).reportCodeChange();
 }
}