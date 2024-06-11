import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractCompiler.LifeCycleStage;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RenamePrototypes;
import com.google.javascript.jscomp.VariableMap;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.TokenStream;
import com.google.javascript.rhino.TokenType;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class RenamePrototypesTest {

  private AbstractCompiler compiler;
  private NodeFactory nodeFactory;
  private VariableMap prevUsedRenameMap;
  private RenamePrototypes renamePrototypes;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    nodeFactory = mock(NodeFactory.class);
    prevUsedRenameMap = mock(VariableMap.class);
    when(compiler.getNodeFactory()).thenReturn(nodeFactory);
    when(compiler.getCodingConvention()).thenReturn(mock(CodingConvention.class));
    when(compiler.getLifeCycleStage()).thenReturn(LifeCycleStage.NORMALIZED);
    renamePrototypes = new RenamePrototypes(compiler, false, null, prevUsedRenameMap);
  }

  @Test
  public void testRenamePrototypeProperty() {
    String originalName = "oldName";
    String newName = "newName";

    CompilerInput input = createInput(Token.PROGRAM, new Node[]{
        createNode(TokenType.GETPROP, null, originalName),
        createNode(TokenType.ASSIGN, null, createNode(TokenType.STRING, null, "value"))
    });

    when(compiler.getLifeCycleStage()).thenReturn(LifeCycleStage.NORMALIZED_OBFUSCATED);

    renamePrototypes.process(null, input.getRootNode());

    ArgumentCaptor<String> oldNameCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> newNameCaptor = ArgumentCaptor.forClass(String.class);

    renamePrototypes.getPropertyMap().assertRenamedName(oldNameCaptor.capture(), newNameCaptor.capture());

    assertEquals(newName, newNameCaptor.getValue());
    assertEquals(originalName, oldNameCaptor.getValue());
  }

  @Test
  public void testDontRenamePrototypePropertyWithLeadingUnderscore() {
    String originalName = "_oldName";

    CompilerInput input = createInput(Token.PROGRAM, new Node[]{
        createNode(TokenType.GETPROP, null, originalName),
        createNode(TokenType.ASSIGN, null, createNode(TokenType.STRING, null, "value"))
    });

    renamePrototypes.process(null, input.getRootNode());

    ArgumentCaptor<String> oldNameCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> newNameCaptor = ArgumentCaptor.forClass(String.class);

    renamePrototypes.getPropertyMap().assertRenamedName(oldNameCaptor.capture(), newNameCaptor.capture());

    assertNull(newNameCaptor.getValue());
    assertEquals(originalName, oldNameCaptor.getValue());
  }

  @Test
  public void testRenameObjLitProperty() {
    String originalName = "oldName";
    String newName = "newName";

    CompilerInput input = createInput(Token.PROGRAM, new Node[]{
        createNode(TokenType.OBJECTLIT, null, null),
        createNode(TokenType.ASSIGN, null,
            createNode(TokenType.PROPEXPR, null,
                createNode(TokenType.GETPROPERTY, null, "obj"),
                new Node[]{createNode(TokenType.IDENT, null, originalName), createNode(TokenType.COLON, null), createNode(TokenType.STRING, null, "value")})),
        createNode(TokenType.RETURN, null, null)
    });

    when(compiler.getLifeCycleStage()).thenReturn(LifeCycleStage.NORMALIZED_OBFUSCATED);

    renamePrototypes.process(null, input.getRootNode());

    ArgumentCaptor<String> oldNameCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> newNameCaptor = ArgumentCaptor.forClass(String.class);

    renamePrototypes.getPropertyMap().assertRenamedName(oldNameCaptor.capture(), newNameCaptor.capture());

    assertEquals(newName, newNameCaptor.getValue());
    assertEquals(originalName, oldNameCaptor.getValue());
  }

  @Test
  public void testDontRenameObjLitPropertyWithLeadingUnderscore() {
    String originalName = "_oldName";

    CompilerInput input = createInput(Token.PROGRAM, new Node[]{
        createNode(TokenType.OBJECTLIT, null, null),
        createNode(TokenType.ASSIGN, null,
            createNode(TokenType.PROPEXPR, null,
                createNode(TokenType.GETPROPERTY, null, "obj"),
                new Node[]{createNode(TokenType.IDENT, null, originalName), createNode(TokenType.COLON, null), createNode(TokenType.STRING, null, "value")})),
        createNode(TokenType.RETURN, null, null)
    });

    renamePrototypes.process(null, input.getRootNode());

    ArgumentCaptor<String> oldNameCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> newNameCaptor = ArgumentCaptor.forClass(String.class);

    renamePrototypes.getPropertyMap().assertRenamedName(oldNameCaptor.capture(), newNameCaptor.capture());

    assertNull(newNameCaptor.getValue());
    assertEquals(originalName, oldNameCaptor.getValue());
  }

  @Test(expected = IllegalStateException.class)
  public void testDontRenamePrototypePropertyWithSameNameAsBuiltInFunction() {
    String originalName = "indexOf";

    CompilerInput input = createInput(Token.PROGRAM, new Node[]{
        createNode(TokenType.GETPROP, null, originalName),
        createNode(TokenType.ASSIGN, null, createNode(TokenType.STRING, null, "value"))
    });

    renamePrototypes.process(null, input.getRootNode());

    ArgumentCaptor<String> oldNameCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> newNameCaptor = ArgumentCaptor.forClass(String.class);

    renamePrototypes.getPropertyMap().assertRenamedName(oldNameCaptor.capture(), newNameCaptor.capture());

    assertNull(newNameCaptor.getValue());
    assertEquals(originalName, oldNameCaptor.getValue());
  }

  @Test
  public void testRenamePrototypePropertyWithAggressiveRenaming() {
    String originalName = "someFunction";
    String newName = "renamedFunction";

    RenamePrototypes aggressiveRenamePrototypes = new RenamePrototypes(compiler, true, null, prevUsedRenameMap);

    CompilerInput input = createInput(Token.PROGRAM, new Node[]{
        createNode(TokenType.GETPROP, null, originalName),
        createNode(TokenType.ASSIGN, null, createNode(TokenType.STRING, null, "value"))
    });

    when(compiler.getLifeCycleStage()).thenReturn(LifeCycleStage.NORMALIZED_OBFUSCATED);

    aggressiveRenamePrototypes.process(null, input.getRootNode());

    ArgumentCaptor<String> oldNameCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> newNameCaptor = ArgumentCaptor.forClass(String.class);

    aggressiveRenamePrototypes.getPropertyMap().assertRenamedName(oldNameCaptor.capture(), newNameCaptor.capture());

    assertEquals(newName, newNameCaptor.getValue());
    assertEquals(originalName, oldNameCaptor.getValue());
  }

  private Node createNode(int type, Node parent, String value) {
    Node node = nodeFactory.createNode(type, parent);
    if (value != null) {
      node.setString(value);
    }
    return node;
  }

  private Node createNode(int type, Node parent, Node... children) {
    Node node = nodeFactory.createNode(type, parent);
    if (children != null) {
      for (Node child : children) {
        node.appendChild(child);
      }
    }
    return node;
  }

  private CompilerInput createInput(int type, Node... nodes) {
    TokenStream tokenStream = new TokenStream();
    Node root = createNode(type, null, nodes);
    return new CompilerInput(tokenStream, root);
  }
}