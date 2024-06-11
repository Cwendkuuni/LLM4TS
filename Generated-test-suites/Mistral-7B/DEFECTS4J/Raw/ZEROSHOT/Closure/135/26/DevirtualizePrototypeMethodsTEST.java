import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SimpleDefinitionFinder;
import com.google.javascript.jscomp.SimpleDefinitionFinder.DefinitionSite;
import com.google.javascript.jscomp.SimpleDefinitionFinder.Definition;
import com.google.javascript.rhino.NodeUtil;

public class DevirtualizePrototypeMethodsTest {

  @Test
  public void testDevirtualizePrototypeMethods() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CompilerPass devirtualizer = new DevirtualizePrototypeMethods(compiler);

    SimpleDefinitionFinder defFinder = mock(SimpleDefinitionFinder.class);

    Definition definition = new Definition("Object", Node.NEW_EXPR, null, null);
    DefinitionSite definitionSite = new DefinitionSite(definition, null, null);

    Node node = Node.newExprStatement(NodeUtil.isGetProp(Node.newName("Object")),
        Node.newName("prototype"),
        Node.newName("accumulate"));

    when(defFinder.getDefinitionSites()).thenReturn(new DefinitionSite[] { definitionSite });
    when(defFinder.getUseSites(definition)).thenReturn(new UseSite[] {});
    when(defFinder.getDefinitionsReferencedAt(Node.newName("accumulate"))).thenReturn(new Definition[] { definition });

    Node root = Node.newProgram(Node.SCOPE_GLOBAL, Node.newBlock(null, null));
    root.addChildToEnd(node);

    devirtualizer.process(null, root);

    Node expectedNode = Node.newExprStatement(NodeUtil.isCall(
        Node.newName("accumulate"),
        Node.newName("Object"),
        Node.newNumber(2)
    ));

    assertEquals(expectedNode, node);
  }
}