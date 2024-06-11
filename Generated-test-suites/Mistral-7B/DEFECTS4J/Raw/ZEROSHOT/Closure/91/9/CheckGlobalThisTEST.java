import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThis() {
    Checker checker = new Compiler().getCheckFactory().makeChecker(Checker.Type.SIMPLE_CS, new AbstractCompiler() {
      @Override
      public NodeTraversal getSourceTraverser(Node root, Node parent, Node current) {
        return new NodeTraversal(root, new CheckGlobalThis(this, WARNING), false);
      }
    }, null).getCheck(CheckGlobalThis.class);

    Parser parser = new Parser();

    Node script1 = parser.parse("function evil() { this.useful = undefined; }", null, null);
    Node script2 = parser.parse("function evil() { var a = this; a.useful = undefined; }", null, null);
    Node script3 = parser.parse("var evil = function() { this.useful = undefined; }", null, null);
    Node script4 = parser.parse("function evil() { this.prototype.useful = undefined; }", null, null);
    Node script5 = parser.parse("function evil() { var a = {useful: function() {}}; }", null, null);

    Node global = parser.parse("(function() { %s; })();", null, null);
    global.getLastChild().setValue("%s;", script1);

    checker.check(global, null);
    assertEquals(1, checker.getDiagnostics().size());
    assertEquals(CheckGlobalThis.GLOBAL_THIS, checker.getDiagnostics().get(0).getCode());

    checker.resetDiagnostics();
    checker.check(script2, null);
    assertEquals(0, checker.getDiagnostics().size());

    checker.resetDiagnostics();
    checker.check(script3, null);
    assertEquals(0, checker.getDiagnostics().size());

    checker.resetDiagnostics();
    checker.check(script4, null);
    assertEquals(0, checker.getDiagnostics().size());

    checker.resetDiagnostics();
    checker.check(script5, null);
    assertEquals(0, checker.getDiagnostics().size());
  }
}