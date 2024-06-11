import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerPasses.*;
import static com.google.javascript.rhino.NodeTree.*;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.IDeps;
import com.google.javascript.jscomp.JSModuleGraph;
import com.google.javascript.jscomp.Module;
import com.google.javascript.jscomp.NodeTraversingCompiler;
import com.google.javascript.jscomp.parser.bare.Parser;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class CrossModuleMethodMotionTest {

  private static final String SOURCE =
      "var a = {};" +
      "var b = {};" +
      "a.prototype.foo = function() {}";

  private AbstractCompiler compiler;
  private CompilerOptions options;
  private IDeps deps;
  private JSModuleGraph moduleGraph;

  @Before
  public void setUp() {
    options = new CompilerOptions(new CompilerOptions.Defaults());
    deps = new IDeps();
    compiler = new NodeTraversingCompiler(options, deps);
    moduleGraph = compiler.getModuleGraph();
  }

  @Test
  public void testMoveMethod() {
    Node root = Parser.parse(new CharSequenceReader(new StringReader(SOURCE)),
        null, 0, null).getRoot();

    CompilerPass pass = new CrossModuleMethodMotion(compiler,
        new CrossModuleMethodMotion.IdGenerator(), false);

    Diagnostics diagnostics = new Diagnostics();
    pass.process(root, null);

    Module aModule = moduleGraph.getModule("a");
    Module bModule = moduleGraph.getModule("b");

    assertTrue(aModule.getDeclarations().size() == 1);
    assertTrue(bModule.getDeclarations().isEmpty());

    Node aFoo = Iterators.getOnlyElement(aModule.getDeclarations().iterator());
    assertNotNull(aFoo);
    assertEquals(Token.FUNCTION, aFoo.getType());

    Node bFoo = findNode("b.prototype.foo", bModule.getBody());
    assertNotNull(bFoo);
    assertEquals(Token.CALL, bFoo.getType());
    assertEquals(STUB_METHOD_NAME, bFoo.getChild(0).getString());
  }

  private Node findNode(String name, Node node) {
    for (Node child : node.getChildren()) {
      if (name.equals(child.getString())) {
        return child;
      }
      Node found = findNode(name, child);
      if (found != null) {
        return found;
      }
    }
    return null;
  }
}

class CharSequenceReader implements java.io.CharSequence {
  private final CharSequence cs;

  public CharSequenceReader(CharSequence cs) {
    this.cs = cs;
  }

  public int length() {
    return cs.length();
  }

  public char charAt(int index) {
    return cs.charAt(index);
  }

  public CharSequence subSequence(int start, int end) {
    return cs.subSequence(start, end);
  }
}

class StringReader implements java.io.Reader {
  private final CharSequence cs;

  public StringReader(CharSequence cs) {
    this.cs = cs;
  }

  public int read(char[] cbuf, int off, int len) {
    int n = cs.length() - off;
    if (n > len) {
      n = len;
    }
    if (n > 0) {
      System.arraycopy(cs.subSequence(off, off + n).toString().toCharArray(), 0,
          cbuf, off, n);
    }
    return n;
  }

  public void close() {}
}