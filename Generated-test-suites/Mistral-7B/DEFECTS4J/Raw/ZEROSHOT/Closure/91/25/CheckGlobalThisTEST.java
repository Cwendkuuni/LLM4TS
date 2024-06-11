import static org.junit.Assert.assertThat;
import static com.google.common.collect.Lists.newArrayList;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThis() {
    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public Node parse(String code, int options) {
        Parser parser = new Parser(code, options);
        return parser.parseProgram();
      }
    };

    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

    String[] codes = new String[] {
      "function evil() { this.useful = undefined; }",
      "function evil() { var a = this; a.useful = undefined; }",
      "var evil = function() { this.useful = undefined; }",
      "var evil = function() { var a = this; a.useful = undefined; }",
      "function evil() { this.prototype.useful = undefined; }",
      "function evil() { this.prototype.y = {useful: function() {} }; }",
      "function evil() { this.prototype.y = {useful: function() {} }; this.y = this.prototype.y; }",
      "var a = {x: function evil() { this.useful = undefined; } };",
      "var a = {x: {useful: function() {} } };",
      "function evil(x) { this.x = x; }",
      "function evil() { function foo() { this.useful = undefined; } }",
      "function evil() { new function foo() { this.useful = undefined; } }",
      "function evil() { return this; }",
      "function evil() { return this.prototype; }",
      "function evil() { return this.constructor; }",
      "function evil() { this.constructor = Object; }",
      "function evil() { this.constructor = Function; }",
      "function evil() { this.constructor = null; }",
      "function evil() { this.constructor = this; }",
      "function evil() { this.constructor = this.constructor; }",
      "function evil() { this.constructor = (function() { return this; })(); }",
      "function evil() { this.constructor = (new Function()).constructor; }",
      "function evil() { this.constructor = new Function(); }"
    };

    for (String code : codes) {
      Node tree = compiler.parse(code, 0);
      NodeTraversal traverser = new NodeTraversal(tree, null);
      checker.traverse(traverser, tree);
      List<DiagnosticType> errors = Lists.newArrayList(traverser.getDiagnostics());
      assertThat(errors, not(isEmpty()));
      DiagnosticType error = errors.get(0);
      assertThat(error.getMessage(), equalTo("dangerous use of the global 'this' object"));
      System.out.println("Code: " + code);
      System.out.println("Error: " + error.getMessage());
    }

    String[] goodCodes = new String[] {
      "function good() { this.useful = undefined; } // should be flagged, but isn't",
      "function good() { var a = this; a.useful = undefined; } // should be flagged, but isn't",
      "var good = function() { this.useful = undefined; }",
      "var good = function() { var a = this; a.useful = undefined; }",
      "function good() { this.prototype.useful = undefined; }",
      "function good() { this.prototype.y = {useful: function() {} }; }",
      "function good() { this.prototype.y = {useful: function() {} }; this.y = this.prototype.y; }",
      "var a = {x: function good() { this.useful = undefined; } };",
      "var a = {x: {useful: function() {} } };",
      "function good(x) { this.x = x; }",
      "function good() { function foo() { } }",
      "function good() { new function foo() { } }",
      "function good() { return this; } // constructor",
      "function good() { return this.prototype; } // prototype",
      "function good() { return this.constructor; } // constructor",
      "function good() { this.constructor = Object; } // assignment to constructor",
      "function good() { this.constructor = Function; } // assignment to constructor",
      "function good() { this.constructor = null; } // assignment to constructor",
      "function good() { this.constructor = this; } // assignment to constructor",
      "function good() { this.constructor = this.constructor; } // assignment to constructor",
      "function good() { this.constructor = (function() { return this; })(); } // anonymous constructor",
      "function good() { this.constructor = (new Function()).constructor; } // constructor of a function created with Function",
      "function good() { this.constructor = new Function(); } // constructor of a function created with Function"
    };

    for (String code : goodCodes) {
      Node tree = compiler.parse(code, 0);
      NodeTraversal traverser = new NodeTraversal(tree, null);
      checker.traverse(traverser, tree);
      List<DiagnosticType> errors = Lists.newArrayList(traverser.getDiagnostics());
      assertThat(errors, isEmpty());
      System.out.println("Code: " + code);
      System.out.println("No errors.");
    }
  }

  private static <T> List<T> isEmpty() {
    return Lists.newArrayList();
  }

  private static boolean not(Iterable<?> iterable) {
    return iterable.isEmpty();
  }

  private static boolean equalTo(Object expected, Object actual) {
    return expected.equals(actual);
  }
}