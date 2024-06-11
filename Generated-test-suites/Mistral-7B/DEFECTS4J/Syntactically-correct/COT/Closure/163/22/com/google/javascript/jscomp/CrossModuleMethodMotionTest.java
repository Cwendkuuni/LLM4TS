import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerPasses.*;
import static com.google.javascript.rhino.NodeTree.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilationResult;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.ErrorCollector;
import com.google.javascript.jscomp.IdGenerator;
import com.google.javascript.jscomp.JSModuleGraph;
import com.google.javascript.jscomp.NodeTraversingCompilerPass;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceName;

@RunWith(JUnit4.class)
public class CrossModuleMethodMotionTest {

  @Test
  public void testCrossModuleMethodMotion() {
    // Given
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new Compiler(options);
    Node root = new Parser(new SourceFile("test.js", null)).parseProgram();
    CompilationResult result = new CompilationResult();

    IdGenerator idGenerator = Mockito.mock(IdGenerator.class);
    JSModuleGraph moduleGraph = Mockito.mock(JSModuleGraph.class);

    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, idGenerator, true);

    ErrorCollector errs = Mockito.mock(ErrorCollector.class);
    compiler.setErrorCollector(errs);

    // When
    pass.process(null, root);

    // Then
    // Clear mocks
    Mockito.verify(errs).addError(null, null);
    Mockito.verifyNoMoreInteractions(errs);

    // Check that no errors were reported
    assertNull(compiler.getErrors().getFirstError());

    // Check that methods were moved correctly
    // (Note: This requires knowledge about the input and expected output code)
    SourceName moduleA = SourceName.create("moduleA.js");
    SourceName moduleB = SourceName.create("moduleB.js");

    Node moduleAAst = getModuleAST(moduleA.getExternalName());
    Node moduleBAst = getModuleAST(moduleB.getExternalName());

    Node methodA = getMethodNode(moduleAAst, "methodA");
    Node methodB = getMethodNode(moduleBAst, "methodA");

    assertEquals("JSCompiler_stubMethod(1)", methodA.toString());
    assertEquals("JSCompiler_unstubMethod(1, function() { return methodB.apply(this, arguments); })", methodB.toString());
  }

  private Node getModuleAST(String fileName) {
    Node root = new Parser(new SourceFile(fileName, null)).parseProgram();
    return root;
  }

  private Node getMethodNode(Node node, String name) {
    for (Node child : node.getChildren()) {
      if (child.isProperty()) {
        if (child.getPropertyName().getText().equals(name)) {
          return child.getFirstChild();
        }
      } else if (child instanceof NodeList) {
        for (Node grandChild : ((NodeList) child).elements()) {
          if (grandChild instanceof Property) {
            if (grandChild.getPropertyName().getText().equals(name)) {
              return grandChild;
            }
          }
        }
      }
    }
    throw new RuntimeException("Method not found: " + name);
  }
}