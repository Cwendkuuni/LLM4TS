import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RenameLabelsTest {

  private AbstractCompiler compiler;
  private CompilerPass renameLabels;
  private Node rootNode;

  @org.junit.Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    renameLabels = new RenameLabels(compiler);
    rootNode = Node.createTreeFromString(
        "function bar() {"
            + "  a: {"
            + "    b: { foo(); }"
            + "  }"
            + "  a: { break a; }"
            + "  b: { break a; }"
            + " }");
  }

  @org.junit.Test
  public void testRenameLabels() {
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, rootNode);
    renameLabels.process(null, rootNode);
    Node newRootNode = nodeTraversal.getRootNode();

    assertEquals("function bar() {"
        + "  a: {"
        + "    b: {"
        + "      __$0: {}" // unused label
        + "      foo();"
        + "    }"
        + "  }"
        + "  a: { break __$1; }" // renamed label
        + "  __$2: { break __$1; }" // renamed label
        + " }",
        NodeUtil.treeToString(newRootNode));
  }
}

class RenameLabelsTest$$DefaultNameSupplier implements RenameLabels.Supplier<String> {
  int counter = 0;

  @Override
  public String get() {
    return "__$" + (++counter);
  }
}