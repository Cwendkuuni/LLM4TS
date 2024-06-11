import org.junit.Test;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Behavior;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Reference;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceCollection;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceMap;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    String sourceCode = "function f() {\n" +
                        "  var obj = {\n" +
                        "    a: 1,\n" +
                        "    b: function() {}\n" +
                        "  };\n" +
                        "  obj.a = 2;\n" +
                        "  obj.b();\n" +
                        "  var c = obj;\n" +
                        "  obj = null;\n" +
                        "  c.a = 3;\n" +
                        "  delete c.b;\n" +
                        "}";

    Compiler compiler = new Compiler();

    CompilerPass inlineObjectLiterals = new InlineObjectLiterals(
        compiler, () -> "testInlineObjectLiterals_" + System.nanoTime());

    Node root = new Node(Token.PROGRAM, null);
    Node externs = new Node(Token.PROGRAM, null);

    NodeUtil.parse(sourceCode, externs, root);

    inlineObjectLiterals.process(externs, root);

    Scope scope = root.getFirstChild();
    for (int i = 0; i < scope.getNumVars(); i++) {
      Var var = scope.getVar(i);
      ReferenceCollection referenceInfo = compiler.getReferenceCollector()
          .getReferences(var);
      if (referenceInfo != null && referenceInfo.references.size() > 0) {
        Reference declaration = referenceInfo.references.get(0);
        Reference init = referenceInfo.getInitializingReference();
        assertEquals(declaration.getParent().toString(),
                     init.getParent().toString());
        assertEquals(declaration.getNode().toString(),
                     init.getNode().toString());
      }
    }
  }

  @Test(expected = AssertionError.class)
  public void testSelfReferentialObjectLiterals() {
    String sourceCode = "function f() {\n" +
                        "  var obj = {\n" +
                        "    a: {\n" +
                        "      b: obj\n" +
                        "    },\n" +
                        "  };\n" +
                        "}";

    Compiler compiler = new Compiler();

    CompilerPass inlineObjectLiterals = new InlineObjectLiterals(
        compiler, () -> "testSelfReferentialObjectLiterals_" + System.nanoTime());

    Node root = new Node(Token.PROGRAM, null);
    Node externs = new Node(Token.PROGRAM, null);

    NodeUtil.parse(sourceCode, externs, root);

    inlineObjectLiterals.process(externs, root);
  }

  @Test
  public void testInlineObjectLiteralsWithEmptyObject() {
    String sourceCode = "function f() {\n" +
                        "  var obj = {}\n" +
                        "  obj = null;\n" +
                        "}";

    Compiler compiler = new Compiler();

    CompilerPass inlineObjectLiterals = new InlineObjectLiterals(
        compiler, () -> "testInlineObjectLiteralsWithEmptyObject_" + System.nanoTime());

    Node root = new Node(Token.PROGRAM, null);
    Node externs = new Node(Token.PROGRAM, null);

    NodeUtil.parse(sourceCode, externs, root);

    inlineObjectLiterals.process(externs, root);

    Scope scope = root.getFirstChild();
    for (int i = 0; i < scope.getNumVars(); i++) {
      Var var = scope.getVar(i);
      ReferenceCollection referenceInfo = compiler.getReferenceCollector()
          .getReferences(var);
      if (referenceInfo != null && referenceInfo.references.size() > 0) {
        Reference declaration = referenceInfo.references.get(0);
        Reference init = referenceInfo.getInitializingReference();
        assertEquals(declaration.getParent().toString(),
                     init.getParent().toString());
        assertEquals(declaration.getNode().toString(),
                     init.getNode().toString());
      }
    }
  }

  @Test
  public void testInlineObjectLiteralsWithOnlyGetter() {
    String sourceCode = "function f() {\n" +
                        "  var obj = {\n" +
                        "    get a() {}\n" +
                        "  };\n" +
                        "}";

    Compiler compiler = new Compiler();

    CompilerPass inlineObjectLiterals = new InlineObjectLiterals(
        compiler, () -> "testInlineObjectLiteralsWithOnlyGetter_" + System.nanoTime());

    Node root = new Node(Token.PROGRAM, null);
    Node externs = new Node(Token.PROGRAM, null);

    NodeUtil.parse(sourceCode, externs, root);

    inlineObjectLiterals.process(externs, root);

    Scope scope = root.getFirstChild();
    for (int i = 0; i < scope.getNumVars(); i++) {
      Var var = scope.getVar(i);
      ReferenceCollection referenceInfo = compiler.getReferenceCollector()
          .getReferences(var);
      if (referenceInfo != null && referenceInfo.references.size() > 0) {
        Reference declaration = referenceInfo.references.get(0);
        Reference init = referenceInfo.getInitializingReference();
        assertEquals(declaration.getParent().toString(),
                     init.getParent().toString());
        assertEquals(declaration.getNode().toString(),
                     init.getNode().toString());
      }
    }
  }

  @Test
  public void testInlineObjectLiteralsWithNestedObject() {
    String sourceCode = "function f() {\n" +
                        "  var obj1 = {\n" +
                        "    a: 1,\n" +
                        "    b: {\n" +
                        "      c: 2,\n" +
                        "      d: 3\n" +
                        "    }\n" +
                        "  };\n" +
                        "  obj1.b = null;\n" +
                        "}";

    Compiler compiler = new Compiler();

    CompilerPass inlineObjectLiterals = new InlineObjectLiterals(
        compiler, () -> "testInlineObjectLiteralsWithNestedObject_" + System.nanoTime());

    Node root = new Node(Token.PROGRAM, null);
    Node externs = new Node(Token.PROGRAM, null);

    NodeUtil.parse(sourceCode, externs, root);

    inlineObjectLiterals.process(externs, root);

    Scope scope = root.getFirstChild();
    for (int i = 0; i < scope.getNumVars(); i++) {
      Var var = scope.getVar(i);
      ReferenceCollection referenceInfo = compiler.getReferenceCollector()
          .getReferences(var);
      if (referenceInfo != null && referenceInfo.references.size() > 0) {
        Reference declaration = referenceInfo.references.get(0);
        Reference init = referenceInfo.getInitializingReference();
        assertEquals(declaration.getParent().toString(),
                     init.getParent().toString());
        assertEquals(declaration.getNode().toString(),
                     init.getNode().toString());
      }
    }
  }

  @Test
  public void testInlineObjectLiteralsWithFunctionProperty() {
    String sourceCode = "function f() {\n" +
                        "  var obj = {\n" +
                        "    a: function() {},\n" +
                        "    b: 2\n" +
                        "  };\n" +
                        "  obj.a = null;\n" +
                        "}";

    Compiler compiler = new Compiler();

    CompilerPass inlineObjectLiterals = new InlineObjectLiterals(
        compiler, () -> "testInlineObjectLiteralsWithFunctionProperty_" + System.nanoTime());

    Node root = new Node(Token.PROGRAM, null);
    Node externs = new Node(Token.PROGRAM, null);

    NodeUtil.parse(sourceCode, externs, root);

    inlineObjectLiterals.process(externs, root);

    Scope scope = root.getFirstChild();
    for (int i = 0; i < scope.getNumVars(); i++) {
      Var var = scope.getVar(i);
      ReferenceCollection referenceInfo = compiler.getReferenceCollector()
          .getReferences(var);
      if (referenceInfo != null && referenceInfo.references.size() > 0) {
        Reference declaration = referenceInfo.references.get(0);
        Reference init = referenceInfo.getInitializingReference();
        assertEquals(declaration.getParent().toString(),
                     init.getParent().toString());
        assertEquals(declaration.getNode().toString(),
                     init.getNode().toString());
      }
    }
  }

  @Test
  public void testInlineObjectLiteralsWithFunctionValue() {
    String sourceCode = "function f() {\n" +
                        "  var obj = {\n" +
                        "    a: function() {}\n" +
                        "  };\n" +
                        "  obj = null;\n" +
                        "}";

    Compiler compiler = new Compiler();

    CompilerPass inlineObjectLiterals = new InlineObjectLiterals(
        compiler, () -> "testInlineObjectLiteralsWithFunctionValue_" + System.nanoTime());

    Node root = new Node(Token.PROGRAM, null);
    Node externs = new Node(Token.PROGRAM, null);

    NodeUtil.parse(sourceCode, externs, root);

    inlineObjectLiterals.process(externs, root);

    Scope scope = root.getFirstChild();
    for (int i = 0; i < scope.getNumVars(); i++) {
      Var var = scope.getVar(i);
      ReferenceCollection referenceInfo = compiler.getReferenceCollector()
          .getReferences(var);
      if (referenceInfo != null && referenceInfo.references.size() > 0) {
        Reference declaration = referenceInfo.references.get(0);
        Reference init = referenceInfo.getInitializingReference();
        assertEquals(declaration.getParent().toString(),
                     init.getParent().toString());
        assertEquals(declaration.getNode().toString(),
                     init.getNode().toString());
      }
    }
  }

  @Test
  public void testInlineObjectLiteralsWithExportedVariable() {
    String sourceCode = "function f() {\n" +
                        "  export var obj = {\n" +
                        "    a: 1,\n" +
                        "  };\n" +
                        "}";

    Compiler compiler = new Compiler();
    compiler.setCodingConvention(new CodingConvention() {
      @Override
      public String getExportedName(String name) {
        return name;
      }

      @Override
      public boolean isExported(String name) {
        return true;
      }
    });

    CompilerPass inlineObjectLiterals = new InlineObjectLiterals(
        compiler, () -> "testInlineObjectLiteralsWithExportedVariable_" + System.nanoTime());

    Node root = new Node(Token.PROGRAM, null);
    Node externs = new Node(Token.PROGRAM, null);

    NodeUtil.parse(sourceCode, externs, root);

    inlineObjectLiterals.process(externs, root);

    Scope scope = root.getFirstChild();
    for (int i = 0; i < scope.getNumVars(); i++) {
      Var var = scope.getVar(i);
      ReferenceCollection referenceInfo = compiler.getReferenceCollector()
          .getReferences(var);
      if (referenceInfo != null && referenceInfo.references.size() > 0) {
        Reference declaration = referenceInfo.references.get(0);
        Reference init = referenceInfo.getInitializingReference();
        assertEquals(declaration.getParent().toString(),
                     init.getParent().toString());
        assertEquals(declaration.getNode().toString(),
                     init.getNode().toString());
      }
    }
  }
}