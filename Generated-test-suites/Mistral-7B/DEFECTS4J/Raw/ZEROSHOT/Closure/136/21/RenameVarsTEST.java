import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.VariableMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RenameVarsTest {

  private CompilerPass renameVars;
  private AbstractCompiler compilerMock;
  private Node rootMock;
  private Node externsMock;

  @Before
  public void setUp() {
    compilerMock = mock(AbstractCompiler.class);
    rootMock = mock(Node.class);
    externsMock = mock(Node.class);

    renameVars = new RenameVars(compilerMock, "", false, false, false, null, null, null);
  }

  @After
  public void tearDown() {
    renameVars = null;
    compilerMock = null;
    rootMock = null;
    externsMock = null;
  }

  @Test
  public void testProcess_ExternsPass_CollectsAllExterns() {
    when(compilerMock.getExterns()).thenReturn(externsMock);

    Node nameNode1 = createNameNode("a");
    Node nameNode2 = createNameNode("b");
    Node nameNode3 = createNameNode("c");

    when(externsMock.getFirstChild()).thenReturn(nameNode1);
    when(nameNode1.getNextSibling()).thenReturn(nameNode2);
    when(nameNode2.getNextSibling()).thenReturn(nameNode3);

    renameVars.process(externsMock, rootMock);

    Set<String> expectedExterns = Sets.newHashSet("a", "b", "c");
    Set<String> actualExterns = renameVars.getVariableMap().getExterns();

    assertSame(expectedExterns, actualExterns);
  }

  @Test
  public void testProcess_SourcePass_CollectsAllLocalAndGlobalNames() {
    when(compilerMock.getExterns()).thenReturn(externsMock);

    Node root = createNode("function foo(a, b) { var c = function(d, e) { return a; } }", 0);

    Node nameNode1 = createNameNode("a");
    Node nameNode2 = createNameNode("b");
    Node nameNode3 = createNameNode("c");
    Node nameNode4 = createNameNode("d");
    Node nameNode5 = createNameNode("e");

    Node functionNode1 = createFunctionNode("function c(d, e) { return a; }");
    Node functionNode2 = createFunctionNode("foo");

    when(rootMock.getFirstChild()).thenReturn(nameNode1);
    when(nameNode1.getNextSibling()).thenReturn(nameNode2);
    when(nameNode2.getNextSibling()).thenReturn(functionNode1);
    when(functionNode1.getFirstChild()).thenReturn(nameNode3);
    when(nameNode3.getNextSibling()).thenReturn(nameNode4);
    when(nameNode4.getNextSibling()).thenReturn(nameNode5);
    when(nameNode5.getNextSibling()).thenReturn(functionNode2);

    renameVars.process(externsMock, rootMock);

    List<Node> expectedLocalNameNodes = Arrays.asList(nameNode4, nameNode5);
    List<Node> actualLocalNameNodes = new ArrayList<>(renameVars.localNameNodes);
    List<Node> expectedGlobalNameNodes = Arrays.asList(nameNode1, nameNode2, nameNode3);
    List<Node> actualGlobalNameNodes = new ArrayList<>(renameVars.globalNameNodes);

    assertSame(expectedLocalNameNodes, actualLocalNameNodes);
    assertSame(expectedGlobalNameNodes, actualGlobalNameNodes);
  }

  @Test
  public void testProcess_NewNamesAreMappedToOldNames() {
    when(compilerMock.getExterns()).thenReturn(externsMock);

    Node root = createNode("function foo(a, b) { var c = function(d, e) { return a; } }", 0);

    Node nameNode1 = createNameNode("a");
    Node nameNode2 = createNameNode("b");
    Node nameNode3 = createNameNode("c");
    Node nameNode4 = createNameNode("d");
    Node nameNode5 = createNameNode("e");

    Node functionNode1 = createFunctionNode("function c(d, e) { return a; }");
    Node functionNode2 = createFunctionNode("foo");

    when(externsMock.getFirstChild()).thenReturn(nameNode1);
    when(nameNode1.getNextSibling()).thenReturn(nameNode2);
    when(nameNode2.getNextSibling()).thenReturn(functionNode1);
    when(functionNode1.getFirstChild()).thenReturn(nameNode3);
    when(nameNode3.getNextSibling()).thenReturn(nameNode4);
    when(nameNode4.getNextSibling()).thenReturn(nameNode5);
    when(nameNode5.getNextSibling()).thenReturn(functionNode2);

    renameVars.process(externsMock, rootMock);

    Map<String, String> expectedRenameMap = new HashMap<>();
    expectedRenameMap.put("d", "d1");
    expectedRenameMap.put("e", "e1");
    expectedRenameMap.put("a", "a1");
    expectedRenameMap.put("b", "b1");
    expectedRenameMap.put("c", "c1");

    VariableMap variableMap = renameVars.getVariableMap();
    Map<String, String> actualRenameMap = variableMap.getRenameMap();

    assertSame(expectedRenameMap, actualRenameMap);
  }

  @Test
  public void testProcess_NamesAreNotAssignedIfTheyAreReserved() {
    when(compilerMock.getExterns()).thenReturn(externsMock);

    Node root = createNode("function foo(a, b) { var c = function(d, e) { return a; } }", 0);

    Node nameNode1 = createNameNode("a");
    Node nameNode2 = createNameNode("b");
    Node nameNode3 = createNameNode("c");
    Node nameNode4 = createNameNode("d");
    Node nameNode5 = createNameNode("e");

    Node functionNode1 = createFunctionNode("function c(d, e) { return a; }");
    Node functionNode2 = createFunctionNode("foo");

    when(externsMock.getFirstChild()).thenReturn(nameNode1);
    when(nameNode1.getNextSibling()).thenReturn(nameNode2);
    when(nameNode2.getNextSibling()).thenReturn(functionNode1);
    when(functionNode1.getFirstChild()).thenReturn(nameNode3);
    when(nameNode3.getNextSibling()).thenReturn(nameNode4);
    when(nameNode4.getNextSibling()).thenReturn(nameNode5);
    when(nameNode5.getNextSibling()).thenReturn(functionNode2);

    Node reservedNameNode = createNameNode("Object");
    when(rootMock.getLastChild()).thenReturn(reservedNameNode);

    renameVars.process(externsMock, rootMock);

    Node actualNameNode = renameVars.globalNameNodes.get(renameVars.globalNameNodes.size() - 1);
    assertSame(reservedNameNode, actualNameNode);
    assertNull(renameVars.renameMap.get("Object"));
  }

  @Test
  public void testProcess_NamesAreNotAssignedIfTheyAreInExterns() {
    when(compilerMock.getExterns()).thenReturn(externsMock);

    Node root = createNode("function foo(a, b) { var c = function(d, e) { return a; } }", 0);

    Node nameNode1 = createNameNode("a");
    Node nameNode2 = createNameNode("b");
    Node nameNode3 = createNameNode("c");
    Node nameNode4 = createNameNode("d");
    Node nameNode5 = createNameNode("e");

    Node functionNode1 = createFunctionNode("function c(d, e) { return a; }");
    Node functionNode2 = createFunctionNode("foo");

    when(externsMock.getFirstChild()).thenReturn(nameNode1);
    when(nameNode1.getNextSibling()).thenReturn(nameNode2);
    when(nameNode2.getNextSibling()).thenReturn(functionNode1);
    when(functionNode1.getFirstChild()).thenReturn(nameNode3);
    when(nameNode3.getNextSibling()).thenReturn(nameNode4);
    when(nameNode4.getNextSibling()).thenReturn(nameNode5);
    when(nameNode5.getNextSibling()).thenReturn(functionNode2);

    Node externNameNode = createNameNode("a");
    when(externNameNode.getParentNode()).thenReturn(externsMock);

    renameVars.process(externsMock, rootMock);

    assertFalse(renameVars.renameMap.containsKey("a"));
  }

  @Test
  public void testProcess_PreservesAnonymousFunctionNames() {
    when(compilerMock.getExterns()).thenReturn(externsMock);

    Node root = createNode("function foo(a, b) { var c = function() { return a; } }", 0);

    Node nameNode1 = createNameNode("a");
    Node nameNode2 = createNameNode("b");
    Node anonymousFunctionNode = createFunctionNode("function() { return a; }");

    when(externsMock.getFirstChild()).thenReturn(nameNode1);
    when(nameNode1.getNextSibling()).thenReturn(nameNode2);
    when(nameNode2.getNextSibling()).thenReturn(anonymousFunctionNode);

    renameVars.preserveAnonymousFunctionNames = true;
    renameVars.process(externsMock, rootMock);

    assertFalse(renameVars.renameMap.containsKey("(anonymous function)"));
  }

  @Test
  public void testProcess_LogsNameAssignmentsInDebugLog() {
    when(compilerMock.getExterns()).thenReturn(externsMock);

    Node root = createNode("function foo(a, b) { var c = function(d, e) { return a; } }", 0);

    Node nameNode1 = createNameNode("a");
    Node nameNode2 = createNameNode("b");
    Node nameNode3 = createNameNode("c");
    Node nameNode4 = createNameNode("d");
    Node nameNode5 = createNameNode("e");

    Node functionNode1 = createFunctionNode("function c(d, e) { return a; }");
    Node functionNode2 = createFunctionNode("foo");

    when(externsMock.getFirstChild()).thenReturn(nameNode1);
    when(nameNode1.getNextSibling()).thenReturn(nameNode2);
    when(nameNode2.getNextSibling()).thenReturn(functionNode1);
    when(functionNode1.getFirstChild()).thenReturn(nameNode3);
    when(nameNode3.getNextSibling()).thenReturn(nameNode4);
    when(nameNode4.getNextSibling()).thenReturn(nameNode5);
    when(nameNode5.getNextSibling()).thenReturn(functionNode2);

    renameVars.process(externsMock, rootMock);

    String expectedDebugLog = "JS var assignments:\n" +
        "a => a0\n" +
        "b => b0\n" +
        "c => c0\n" +
        "d => d0\n" +
        "e => e0\n";

    String actualDebugLog = renameVars.assignmentLog.toString();
    assertSame(expectedDebugLog, actualDebugLog);
  }

  private Node createNameNode(String name) {
    Node nameNode = new Node(Token.NAME, 0, name);
    return nameNode;
  }

  private Node createFunctionNode(String source) {
    Node functionNode = new Node(Token.FUNCTION, 0, source);
    when(functionNode.getDebugName()).thenReturn("FunctionDeclaration");
    return functionNode;
  }

  private Node createNode(String source, int lineNumber) {
    Node root = new Node(Token.PROGRAM, lineNumber, null);
    when(root.getDebugName()).thenReturn("Program");
    Node scriptNode = new Node(Token.SCRIPT, lineNumber, source);
    root.appendChild(scriptNode);
    return root;
  }
}