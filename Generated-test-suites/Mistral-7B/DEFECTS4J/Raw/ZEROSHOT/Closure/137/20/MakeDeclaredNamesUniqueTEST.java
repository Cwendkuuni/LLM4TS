import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.TokenStream;

import java.util.Map;

public class MakeDeclaredNamesUniqueTest {

  public void testMakeDeclaredNamesUnique() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    TokenStream tokenStream = mock(TokenStream.class);
    when(compiler.getTokenStream()).thenReturn(tokenStream);

    Node rootNode = new Node(Token.START_OBJECT);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, null, rootNode);

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    CompilerPass contextualRenameInverter = MakeDeclaredNamesUnique.getContextualRenameInverter(compiler);

    nodeTraversal.addPass(contextualRenameInverter);
    nodeTraversal.addPass(makeDeclaredNamesUnique);

    nodeTraversal.traverse(rootNode, null);

    // Test that all declared names are unique
    Scope globalScope = nodeTraversal.getScope();
    Map<String, Node> variableMap = Maps.newHashMap();
    for (Node child = rootNode.getFirstChild(); child != null; child = child.getNext()) {
      if (NodeUtil.isVarDeclaration(child)) {
        variableMap.put(child.getString(), child);
      }
    }

    for (Map.Entry<String, Node> entry : variableMap.entrySet()) {
      String name = entry.getKey();
      Node variableNode = entry.getValue();
      String uniqueName = makeDeclaredNamesUnique.getReplacementName(name);
      assertEquals(uniqueName, variableNode.getString());
    }
  }

  // Add more tests here as needed

  // TODO: Test for function recursive names and catch exceptions

}