import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.RenameVars;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.VariableMap;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class RenameVarsTest {

  private AbstractCompiler compiler;
  private NodeFactory nodeFactory;
  private RenameVars renameVars;
  private Node rootNode;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    nodeFactory = mock(NodeFactory.class);
    rootNode = createTestRootNode();

    renameVars = new RenameVars(compiler, "", false, false, false, null, null, null);
  }

  @Test
  public void testProcess_EmptyInput() {
    renameVars.process(null, rootNode);
    assertNull(renameVars.getVariableMap());
  }

  @Test
  public void testProcess_ProcessGlobalVariables() {
    String oldName1 = "varA";
    String oldName2 = "varB";
    String newName1 = "newNameA";
    String newName2 = "newNameB";

    when(compiler.getCodingConvention()).thenReturn(new CodingConvention() {
      @Override
      public boolean isExported(String name, boolean isLocal) {
        return false;
      }
    });

    Node globalNameNode1 = createNameNode(oldName1);
    Node globalNameNode2 = createNameNode(oldName2);

    NodeTraversal traversal = new NodeTraversal(compiler, rootNode, null);
    traversal.traverse(new ProcessVars(true));
    traversal.traverse(new ProcessVars(false));

    when(compiler.getDebugInfoWriter()).thenReturn(mock(JSDebugInfoWriter.class));

    renameVars.process(null, rootNode);

    ArgumentCaptor<String> oldNameCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> newNameCaptor = ArgumentCaptor.forClass(String.class);

    verify(compiler).addToDebugLog(oldNameCaptor.capture(), eq(" => "), newNameCaptor.capture());

    List<Map.Entry<String, String>> entries = new ArrayList<>(renameVars.getVariableMap().entries());

    assertEquals(2, entries.size());
    Map.Entry<String, String> entry1 = entries.get(0);
    assertEquals(oldName1, entry1.getKey());
    assertEquals(newName1, entry1.getValue());

    Map.Entry<String, String> entry2 = entries.get(1);
    assertEquals(oldName2, entry2.getKey());
    assertEquals(newName2, entry2.getValue());
  }

  @Test
  public void testProcess_ProcessLocalVariables() {
    String oldName1 = "localVarA";
    String oldName2 = "localVarB";
    String newName1 = "newNameA";
    String newName2 = "newNameB";

    when(compiler.getCodingConvention()).thenReturn(new CodingConvention() {
      @Override
      public boolean isExported(String name, boolean isLocal) {
        return isLocal;
      }
    });

    Scope scope = mock(Scope.class);
    when(scope.getVar(oldName1)).thenReturn(createVarNode(oldName1, 0));
    when(scope.getVar(oldName2)).thenReturn(createVarNode(oldName2, 1));

    Node localNameNode1 = createNameNode(oldName1);
    Node localNameNode2 = createNameNode(oldName2);

    NodeTraversal traversal = new NodeTraversal(compiler, rootNode, scope);
    traversal.traverse(new ProcessVars(true));
    traversal.traverse(new ProcessVars(false));

    renameVars.process(null, rootNode);

    ArgumentCaptor<String> oldNameCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> newNameCaptor = ArgumentCaptor.forClass(String.class);

    verify(compiler).addToDebugLog(oldNameCaptor.capture(), eq(" => "), newNameCaptor.capture());

    List<Map.Entry<String, String>> entries = new ArrayList<>(renameVars.getVariableMap().entries());

    assertEquals(2, entries.size());
    Map.Entry<String, String> entry1 = entries.get(0);
    assertEquals(oldName1, entry1.getKey());
    assertEquals(newName1, entry1.getValue());

    Map.Entry<String, String> entry2 = entries.get(1);
    assertEquals(oldName2, entry2.getKey());
    assertEquals(newName2, entry2.getValue());
  }

  @Test
  public void testProcess_PreserveAnonymousFunctionNames() {
    String oldName1 = "anonymousFunctionA";
    String oldName2 = "varB";
    String newName1 = "anonymousFunctionA";
    String newName2 = "newNameB";

    when(compiler.getCodingConvention()).thenReturn(new CodingConvention() {
      @Override
      public boolean isExported(String name, boolean isLocal) {
        return false;
      }

      @Override
      public boolean preserveAnonymousFunctionNames() {
        return true;
      }
    });

    Node anonymousFunctionNode = createAnonymousFunctionNode(oldName1);
    Node globalNameNode = createNameNode(oldName2);

    NodeTraversal traversal = new NodeTraversal(compiler, rootNode, null);
    traversal.traverse(new ProcessVars(true));
    traversal.traverse(new ProcessVars(false));

    renameVars.process(null, rootNode);

    ArgumentCaptor<String> oldNameCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> newNameCaptor = ArgumentCaptor.forClass(String.class);

    verify(compiler).addToDebugLog(oldNameCaptor.capture(), eq(" => "), newNameCaptor.capture());

    List<Map.Entry<String, String>> entries = new ArrayList<>(renameVars.getVariableMap().entries());

    assertEquals(2, entries.size());
    Map.Entry<String, String> entry1 = entries.get(0);
    assertEquals(oldName1, entry1.getKey());
    assertEquals(newName1, entry1.getValue());

    Map.Entry<String, String> entry2 = entries.get(1);
    assertEquals(oldName2, entry2.getKey());
    assertNull(entry2.getValue());
  }

  @Test
  public void testProcess_GeneratePseudoNames() {
    String oldName1 = "varA";
    String oldName2 = "varB";
    String newName1 = "$$varA";
    String newName2 = "$$varB";

    when(compiler.getCodingConvention()).thenReturn(new CodingConvention() {
      @Override
      public boolean isExported(String name, boolean isLocal) {
        return false;
      }

      @Override
      public boolean generatePseudoNames() {
        return true;
      }
    });

    Node globalNameNode1 = createNameNode(oldName1);
    Node globalNameNode2 = createNameNode(oldName2);

    NodeTraversal traversal = new NodeTraversal(compiler, rootNode, null);
    traversal.traverse(new ProcessVars(true));
    traversal.traverse(new ProcessVars(false));

    renameVars.process(null, rootNode);

    ArgumentCaptor<String> oldNameCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> newNameCaptor = ArgumentCaptor.forClass(String.class);

    verify(compiler).addToDebugLog(oldNameCaptor.capture(), eq(" => "), newNameCaptor.capture());

    List<Map.Entry<String, String>> entries = new ArrayList<>(renameVars.getVariableMap().entries());

    assertEquals(2, entries.size());
    Map.Entry<String, String> entry1 = entries.get(0);
    assertEquals(oldName1, entry1.getKey());
    assertEquals(newName1, entry1.getValue());

    Map.Entry<String, String> entry2 = entries.get(1);
    assertEquals(oldName2, entry2.getKey());
    assertEquals(newName2, entry2.getValue());
  }

  @Test
  public void testProcess_LocalRenamingOnly() {
    String oldName1 = "globalVarA";
    String oldName2 = "localVarA";
    String oldName3 = "localVarB";
    String newName1 = null;
    String newName2 = "newNameA";
    String newName3 = "newNameB";

    when(compiler.getCodingConvention()).thenReturn(new CodingConvention() {
      @Override
      public boolean isExported(String name, boolean isLocal) {
        return false;
      }

      @Override
      public boolean localRenamingOnly() {
        return true;
      }
    });

    Node globalNameNode = createNameNode(oldName1);
    Node localNameNode1 = createNameNode(oldName2);
    Node localNameNode2 = createNameNode(oldName3);

    NodeTraversal traversal = new NodeTraversal(compiler, rootNode, null);
    traversal.traverse(new ProcessVars(true));
    traversal.traverse(new ProcessVars(false));

    renameVars.process(null, rootNode);

    ArgumentCaptor<String> oldNameCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> newNameCaptor = ArgumentCaptor.forClass(String.class);

    verify(compiler).addToDebugLog(oldNameCaptor.capture(), eq(" => "), newNameCaptor.capture());

    List<Map.Entry<String, String>> entries = new ArrayList<>(renameVars.getVariableMap().entries());

    assertEquals(2, entries.size());
    Map.Entry<String, String> entry1 = entries.get(0);
    assertEquals(oldName2, entry1.getKey());
    assertEquals(newName2, entry1.getValue());

    Map.Entry<String, String> entry3 = entries.get(1);
    assertEquals(oldName3, entry3.getKey());
    assertEquals(newName3, entry3.getValue());

    assertNull(entry1.getValue());
  }

  private Node createTestRootNode() {
    Node rootNode = nodeFactory.createProgram();
    Node body = nodeFactory.createBlock("", 0);
    rootNode.setFirstChild(body);
    return rootNode;
  }

  private Node createNameNode(String name) {
    return nodeFactory.createName(name, null, 0, null, 0, null);
  }

  private Node createVarNode(String name, int index) {
    Node nameNode = createNameNode(name);
    Node varNode = nodeFactory.createVar(nameNode, null, index, 0, null);
    return varNode;
  }

  private Node createAnonymousFunctionNode(String name) {
    Node functionNode = nodeFactory.createFunction(null, NodeFactory.Anonymous, null, null, 0, null);
    Node nameNode = nodeFactory.createName(name, null, 0, null, 0, null);
    Node body = nodeFactory.createBlock("", 0);
    functionNode.setFirstChild(nameNode);
    functionNode.setLastChild(body);
    return functionNode;
  }

  static class CodingConvention {
    public boolean isExported(String name, boolean isLocal) {
      return false;
    }

    public boolean preserveAnonymousFunctionNames() {
      return false;
    }

    public boolean generatePseudoNames() {
      return false;
    }

    public boolean localRenamingOnly() {
      return false;
    }
  }
}