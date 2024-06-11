import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class RenameVarsTest {

  @Test
  public void testProcess() {
    String prefix = "test_";
    RenameVars renameVars = new RenameVars(null, prefix, false, false, false, null, null, null);

    Node ast = Node.objectLiteral();
    renameVars.process(null, ast);

    List<Node> globalNameNodes = renameVars.globalNameNodes;
    List<Node> localNameNodes = renameVars.localNameNodes;
    Map<String, String> renameMap = renameVars.renameMap;

    // Check global name nodes
    assertEquals(0, globalNameNodes.size());

    // Check local name nodes
    assertEquals(0, localNameNodes.size());

    // Check rename map is empty
    assertTrue(renameMap.isEmpty());
  }

  @Test
  public void testProcessWithGlobalVariables() {
    String prefix = "test_";
    RenameVars renameVars = new RenameVars(null, prefix, false, false, false, null, null, null);

    Node ast = Node.objectLiteral();
    Node globalVar1 = Node.name("var1");
    Node globalVar2 = Node.name("var2");
    Node globalVar3 = Node.name("var3");
    ast.addChildToProperties(globalVar1);
    ast.addChildToProperties(globalVar2);
    ast.addChildToProperties(globalVar3);

    renameVars.process(Node.objectLiteral(), ast);

    List<Node> globalNameNodes = renameVars.globalNameNodes;
    List<Node> localNameNodes = renameVars.localNameNodes;
    Map<String, String> renameMap = renameVars.renameMap;

    // Check global name nodes
    assertEquals(3, globalNameNodes.size());
    assertEquals("var1", globalNameNodes.get(0).getString());
    assertEquals("var2", globalNameNodes.get(1).getString());
    assertEquals("var3", globalNameNodes.get(2).getString());

    // Check local name nodes
    assertEquals(0, localNameNodes.size());

    // Check rename map
    Map<String, String> expectedRenameMap = new HashMap<>();
    expectedRenameMap.put("var1", "test_var1");
    expectedRenameMap.put("var2", "test_var2");
    expectedRenameMap.put("var3", "test_var3");

    assertEquals(expectedRenameMap, renameMap);
  }

  @Test
  public void testProcessWithLocalVariables() {
    String prefix = "test_";
    RenameVars renameVars = new RenameVars(null, prefix, false, false, false, null, null, null);

    Node ast = Node.functionLiteral(Token.FUNCTION, null, Node.EMPTY_LIST, Node.BLOCK, null);
    Node var1 = Node.var("var1");
    Node var2 = Node.var("var2");
    Node var3 = Node.var("var3");
    Node assignment1 = Node.assignment(Token.ASSIGN, Node.name("var1"), Node.literal(1));
    Node assignment2 = Node.assignment(Token.ASSIGN, Node.name("var2"), Node.literal(2));
    Node assignment3 = Node.assignment(Token.ASSIGN, Node.name("var3"), Node.literal(3));

    ast.addChildToBody(var1);
    ast.addChildToBody(var2);
    ast.addChildToBody(var3);
    ast.addChildToBody(assignment1);
    ast.addChildToBody(assignment2);
    ast.addChildToBody(assignment3);

    renameVars.process(null, ast);

    List<Node> globalNameNodes = renameVars.globalNameNodes;
    List<Node> localNameNodes = renameVars.localNameNodes;
    Map<String, String> renameMap = renameVars.renameMap;

    // Check global name nodes
    assertEquals(0, globalNameNodes.size());

    // Check local name nodes
    assertEquals(3, localNameNodes.size());
    assertEquals("L0", localNameNodes.get(0).getString());
    assertEquals("L1", localNameNodes.get(1).getString());
    assertEquals("L2", localNameNodes.get(2).getString());

    // Check rename map is empty
    assertTrue(renameMap.isEmpty());
  }

  @Test
  public void testProcessWithGlobalAndLocalVariables() {
    String prefix = "test_";
    RenameVars renameVars = new RenameVars(null, prefix, false, false, false, null, null, null);

    Node ast = Node.objectLiteral();
    Node globalVar1 = Node.name("globalVar1");
    Node globalVar2 = Node.name("globalVar2");
    Node var1 = Node.var("var1");
    Node var2 = Node.var("var2");

    ast.addChildToProperties(globalVar1);
    ast.addChildToProperties(globalVar2);
    ast.addChildToProperties(var1);
    ast.addChildToProperties(var2);

    renameVars.process(Node.objectLiteral(), ast);

    List<Node> globalNameNodes = renameVars.globalNameNodes;
    List<Node> localNameNodes = renameVars.localNameNodes;
    Map<String, String> renameMap = renameVars.renameMap;

    // Check global name nodes
    assertEquals(2, globalNameNodes.size());
    assertEquals("globalVar1", globalNameNodes.get(0).getString());
    assertEquals("globalVar2", globalNameNodes.get(1).getString());

    // Check local name nodes
    assertEquals(2, localNameNodes.size());
    assertEquals("L0", localNameNodes.get(0).getString());
    assertEquals("L1", localNameNodes.get(1).getString());

    // Check rename map
    Map<String, String> expectedRenameMap = new HashMap<>();
    expectedRenameMap.put("globalVar1", "test_globalVar1");
    expectedRenameMap.put("globalVar2", "test_globalVar2");
    expectedRenameMap.put("var1", "L0");
    expectedRenameMap.put("var2", "L1");

    assertEquals(expectedRenameMap, renameMap);
  }

  @Test
  public void testProcessWithReservedNames() {
    String prefix = "test_";
    RenameVars renameVars = new RenameVars(null, prefix, false, false, false, null, null, null);

    Node ast = Node.objectLiteral();
    Node globalVar1 = Node.name("const");
    Node globalVar2 = Node.name("let");
    Node var1 = Node.var("var");

    ast.addChildToProperties(globalVar1);
    ast.addChildToProperties(globalVar2);
    ast.addChildToProperties(var1);

    Set<String> reservedNames = renameVars.reservedNames;

    renameVars.process(Node.objectLiteral(), ast);

    List<Node> globalNameNodes = renameVars.globalNameNodes;
    List<Node> localNameNodes = renameVars.localNameNodes;
    Map<String, String> renameMap = renameVars.renameMap;

    // Check global name nodes
    assertEquals(2, globalNameNodes.size());
    assertEquals("const", globalNameNodes.get(0).getString());
    assertEquals("let", globalNameNodes.get(1).getString());

    // Check local name nodes
    assertEquals(1, localNameNodes.size());
    assertEquals("var", localNameNodes.get(0).getString());

    // Check rename map is empty
    assertTrue(renameMap.isEmpty());

    // Check reservedNames contains all the reserved keywords
    List<String> reservedKeywords = Arrays.asList("const", "let", "var");
    assertEquals(reservedKeywords, new ArrayList<>(reservedNames));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testProcessWithNullAst() {
    String prefix = "test_";
    RenameVars renameVars = new RenameVars(null, prefix, false, false, false, null, null, null);
    renameVars.process(null, Node.objectLiteral());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testProcessWithEmptyAst() {
    String prefix = "test_";
    RenameVars renameVars = new RenameVars(null, prefix, false, false, false, null, null, null);
    renameVars.process(Node.objectLiteral(), Node.EMPTY_LIST);
  }
}