import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.VarCheck;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;

public class VarCheckTest {

  @Test
  public void testValidVariableDeclaration() {
    // Given a valid variable declaration
    VarCheck varCheck = new VarCheck(null);
    CompilerInput input = new CompilerInput("Test.java", "package test;\n" +
        "public class Test {\n" +
        "  int a;\n" +
        "}");

    // When the VarCheck pass is run
    Node root = input.getAstRoot(varCheck.compiler);
    varCheck.process(null, root);

    // Then no errors or warnings should be reported
    NodeTraversal.traverse(varCheck.compiler, root, new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.ERROR) {
          fail("Unexpected error: " + n.getString());
        }
      }
    });
  }

  @Test
  public void testUndeclaredVariable() {
    // Given a variable that is not declared
    VarCheck varCheck = new VarCheck(null);
    CompilerInput input = new CompilerInput("Test.java", "package test;\n" +
        "public class Test {\n" +
        "  int b;\n" +
        "  System.out.println(c);\n" +
        "}");

    // When the VarCheck pass is run
    Node root = input.getAstRoot(varCheck.compiler);
    varCheck.process(null, root);

    // Then an error should be reported
    NodeTraversal.traverse(varCheck.compiler, root, new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.ERROR &&
            n.getString().equals("JSC_UNDEFINED_VARIABLE: variable c is undefined")) {
          return;
        }
        fail("Unexpected node: " + n.getType() + " " + n.getString());
      }
    });
  }

  @Test
  public void testDuplicateVariableDeclaration() {
    // Given a variable that is declared twice
    VarCheck varCheck = new VarCheck(null);
    CompilerInput input = new CompilerInput("Test.java", "package test;\n" +
        "public class Test {\n" +
        "  int a;\n" +
        "  int a;\n" +
        "}");

    // When the VarCheck pass is run
    Node root = input.getAstRoot(varCheck.compiler);
    varCheck.process(null, root);

    // Then no errors or warnings should be reported
    NodeTraversal.traverse(varCheck.compiler, root, new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.ERROR || n.getType() == Token.WARNING) {
          fail("Unexpected " + (n.getType() == Token.ERROR ? "error" : "warning") +
              ": " + n.getString());
        }
      }
    });

    // And the variable should only be declared once in the synthesized externs
    NodeTraversal.traverse(varCheck.compiler, getSynthesizedExternsRoot(varCheck), new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.VAR && n.getFirstChild().getString().equals("a")) {
          assertEquals(1, n.getNumChildren());
        }
      }
    });
  }

  @Test
  public void testModuleDependency() {
    // Given a module that references a variable from another module
    VarCheck varCheck = new VarCheck(null);
    CompilerInput moduleAInput = new CompilerInput("ModuleA.java", "package a;\n" +
        "public class ModuleA {\n" +
        "  import b.B;\n" +
        "  B.c;\n" +
        "}");
    CompilerInput moduleBInput = new CompilerInput("ModuleB.java", "package b;\n" +
        "public class B {\n" +
        "  int c;\n" +
        "}");

    // When the VarCheck pass is run
    Node moduleARoot = moduleAInput.getAstRoot(varCheck.compiler);
    Node moduleBRoot = moduleBInput.getAstRoot(varCheck.compiler);
    varCheck.process(null, moduleBRoot);
    varCheck.process(null, moduleARoot);

    // Then no errors or warnings should be reported
    NodeTraversal.traverse(varCheck.compiler, getSynthesizedExternsRoot(varCheck), new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.ERROR || n.getType() == Token.WARNING) {
          fail("Unexpected " + (n.getType() == Token.ERROR ? "error" : "warning") +
              ": " + n.getString());
        }
      }
    });
  }

  @Test
  public void testMissingModuleDependency() {
    // Given a module that references a variable from another module
    // without declaring a dependency on that module
    VarCheck varCheck = new VarCheck(null);
    CompilerInput moduleAInput = new CompilerInput("ModuleA.java", "package a;\n" +
        "public class ModuleA {\n" +
        "  import b.B;\n" +
        "  B.c;\n" +
        "}");
    CompilerInput moduleBInput = new CompilerInput("ModuleB.java", "package b;\n" +
        "public class B {\n" +
        "  int c;\n" +
        "}");

    // When the VarCheck pass is run
    Node moduleARoot = moduleAInput.getAstRoot(varCheck.compiler);
    Node moduleBRoot = moduleBInput.getAstRoot(varCheck.compiler);
    varCheck.process(null, moduleBRoot);
    varCheck.process(null, moduleARoot);

    // Then a warning should be reported
    NodeTraversal.traverse(varCheck.compiler, getSynthesizedExternsRoot(varCheck), new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.WARNING &&
            n.getString().contains("missing module dependency")) {
          return;
        }
        fail("Unexpected node: " + n.getType() + " " + n.getString());
      }
    });
  }

  @Test
  public void testStrictModuleDependency() {
    // Given a module that tries to reference a variable from another module
    // in strict mode
    VarCheck varCheck = new VarCheck(new CompilerPass.ErrorLevel(JSError.make("", 0, 0, VarCheck.STRICT_MODULE_DEP_ERROR)));
    CompilerInput moduleAInput = new CompilerInput("ModuleA.java", "package a;\n" +
        "public class ModuleA {\n" +
        "  import b.B;\n" +
        "  B.c;\n" +
        "}");
    CompilerInput moduleBInput = new CompilerInput("ModuleB.java", "package b;\n" +
        "public class B {\n" +
        "  int c;\n" +
        "}");

    // When the VarCheck pass is run
    Node moduleARoot = moduleAInput.getAstRoot(varCheck.compiler);
    Node moduleBRoot = moduleBInput.getAstRoot(varCheck.compiler);
    varCheck.process(null, moduleBRoot);
    varCheck.process(null, moduleARoot);

    // Then no errors or warnings should be reported
    NodeTraversal.traverse(varCheck.compiler, getSynthesizedExternsRoot(varCheck), new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.ERROR || n.getType() == Token.WARNING) {
          fail("Unexpected " + (n.getType() == Token.ERROR ? "error" : "warning") +
              ": " + n.getString());
        }
      }
    });
  }

  private Node getSynthesizedExternsRoot(VarCheck varCheck) {
    return varCheck.getSynthesizedExternsRoot();
  }
}


// Test cases for VarCheck class
// 