import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.AbstractCallback;
import static com.google.javascript.rhino.NodeType.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RenamePrototypes;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.TokenStream;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RunWith(JUnit4.class)
public class RenamePrototypesTest {

  @Test
  public void testRenamePrototypes() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(CompilerOptions.WarningLevel.QUIET);
    options.setExterns(Collections.emptyMap());

    AbstractCompiler compiler = new AbstractCompiler(options);
    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, null);

    TokenStream input = new TokenStream(new StringBuilder("" +
        "function Foo() {}\n" +
        "Foo.prototype.bar = function() {}\n" +
        "Foo.prototype.baz = function() {}\n" +
        "function Foo2() {}\n" +
        "Foo2.prototype.bar = function() {}\n" +
        "var x = {\n" +
        "  get prototype() {\n" +
        "    return Foo.prototype;\n" +
        "  }\n" +
        "};\n" +
        "x.prototype.qux = function() {}\n" +
        "Foo.prototype.qux = function() {}\n" +
        ""));

    CompilerInput externalSources = new CompilerInput(input);

    Node root = compiler.parseTree(externalSources, null);

    renamePrototypes.process(null, root);

    NodeTraversal.traverse(compiler, root, new AbstractCallback() {
      @Override
      public void visit(NodeTraversal traverser, Node n, Node parent) {
        if (n.getType() == GETPROP && n.getString().equals("prototype")) {
          Node prototype = n.getFirstChild().getNext();
          if (prototype.getType() == OBJECTLIT) {
            for (Node child = prototype.getFirstChild();
                 child != null; child = child.getNext()) {
              if (child.getType() == GETPROP) {
                String oldName = child.getString();
                String newName = renamePrototypes.getPropertyMap().lookupNewName(oldName);
                assertEquals(newName, child.getString());
              }
            }
          }
        }
      }
    });
  }
}