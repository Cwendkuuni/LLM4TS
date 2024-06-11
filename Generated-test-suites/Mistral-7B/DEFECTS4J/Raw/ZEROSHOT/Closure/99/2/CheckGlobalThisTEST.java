import static org.junit.Assert.*;
import static com.google.javascript.jscomp.Checker.$;
import static com.google.javascript.rhino.NodeTree.*;
import static com.google.javascript.rhino.Token.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CompilationTestBase;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceFile;

@RunWith(JUnit4.class)
public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThis() {
    String input = "function Foo() { this.x = 1; }}\n"
        + "function Bar() { this.x = 1; }}\n"
        + "function Baz() { this.x = 1; }}\n"
        + "function Qux() { this.x = 1; }}\n"
        + "function Qux() { this.x = 1; }\n"
        + "function Evi() { var a = this; }";

    SourceFile sourceFile = new SourceFile("CheckGlobalThisTest.js", input);
    AbstractCompiler compiler = new Compiler();
    Checker checker = new Checker(new Config());

    checker.check(sourceFile, compiler);
    ImmutableList<AbstractDiagnostic> diagnostics = compiler.getDiagnostics();

    assertEquals(4, diagnostics.size());
    // Foo
    Node fooNode = findNode(sourceFile, "Foo");
    assertEquals(TOKEN_FUNCTION, fooNode.getType());
    assertEquals(WARNING, diagnostics.get(0).getSeverity());
    assertEquals("JSC_USED_GLOBAL_THIS", diagnostics.get(0).getType().name());
    // Bar
    Node barNode = findNode(sourceFile, "Bar");
    assertEquals(TOKEN_FUNCTION, barNode.getType());
    assertEquals(WARNING, diagnostics.get(1).getSeverity());
    assertEquals("JSC_USED_GLOBAL_THIS", diagnostics.get(1).getType().name());
    // Baz
    Node bazNode = findNode(sourceFile, "Baz");
    assertEquals(TOKEN_FUNCTION, bazNode.getType());
    assertEquals(WARNING, diagnostics.get(2).getSeverity());
    assertEquals("JSC_USED_GLOBAL_THIS", diagnostics.get(2).getType().name());
    // Qux
    Node quxNode = findNode(sourceFile, "Qux");
    assertEquals(TOKEN_FUNCTION, quxNode.getType());
    assertEquals(WARNING, diagnostics.get(3).getSeverity());
    assertEquals("JSC_USED_GLOBAL_THIS", diagnostics.get(3).getType().name());

    assertEquals(0, findNode(sourceFile, "Evi").getJSDocInfo().getThisType());
  }

  private Node findNode(SourceFile sourceFile, String name) {
    return findChildByProperty(sourceFile.getAST().getBody(), "name", name).asNode();
  }

  private Node findChildByProperty(Node node, String property, String value) {
    if (node.getFirstChild() == null) {
      return null;
    }

    Node child = node.getFirstChild();
    if (node.getType() == TOKEN_GETPROP && child.getString().equals(property)
        && child.getLastChild().getString().equals(value)) {
      return child.getNext();
    }

    return findChildByProperty(child, property, value);
  }
}