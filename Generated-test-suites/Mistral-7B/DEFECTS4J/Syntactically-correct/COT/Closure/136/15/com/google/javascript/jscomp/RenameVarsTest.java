import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.TRAVERSAL_ORDER_PRE_ORDER;
import static com.google.javascript.jscomp.NodeUtil.isAnonymousFunction;

import java.util.*;

import org.junit.*;
import org.junit.runner.*;
import org.mockito.*;

import com.google.common.base.Preconditions;
import com.google.common.collect.*;

import com.google.javascript.jscomp.*;
import com.google.javascript.rhino.*;

@RunWith(MockitoJUnitRunner.class)
public class RenameVarsTest {

  @Test
  public void testRenameVars_emptyCode() {
    RenameVars renameVars = new RenameVars(Mockito.mock(AbstractCompiler.class), "", false, false, false, null, null, null);
    CompilerInput input = Mockito.mock(CompilerInput.class);
    Node root = NodeOperations.createRootNode("", null);

    renameVars.process(NodeOperations.createExternsNode(""), root);

    assertTrue("Assignments log should be empty", renameVars.assignmentLog.isEmpty());
  }

  @Test
  public void testRenameVars_simpleGlobalVariables() {
    String prefix = "prefix_";
    RenameVars renameVars = new RenameVars(Mockito.mock(AbstractCompiler.class), prefix, false, false, false, null, null, null);
    CompilerInput input = Mockito.mock(CompilerInput.class);
    Node externs = NodeOperations.createExternsNode("var a = 1,\nb = 2,\nc = 3,");
    Node root = NodeOperations.createRootNode("function foo() { a = b + c; }", input);

    renameVars.process(externs, root);

    String expectedLog = "a => a\nb => b\nc => c\n";
    assertEquals(expectedLog, renameVars.assignmentLog.toString());
  }

  @Test
  public void testRenameVars_simpleLocalVariables() {
    String prefix = "prefix_";
    boolean localRenamingOnly = true;
    RenameVars renameVars = new RenameVars(Mockito.mock(AbstractCompiler.class), "", localRenamingOnly, false, false, null, null, null);
    CompilerInput input = Mockito.mock(CompilerInput.class);
    Node externs = NodeOperations.createExternsNode("var a = 1,\nb = 2,\nc = 3");
    Node root = NodeOperations.createRootNode("function foo() { var a = b + c; }", input);

    renameVars.process(externs, root);

    String expectedLog = "a => a$$0\nb => b\nc => c$$1\n";
    assertEquals(expectedLog, renameVars.assignmentLog.toString());
  }

  @Test
  public void testRenameVars_anonymousFunctions() {
    String prefix = "prefix_";
    boolean preserveAnonymousFunctionNames = true;
    RenameVars renameVars = new RenameVars(Mockito.mock(AbstractCompiler.class), prefix, false, preserveAnonymousFunctionNames, false, null, null, null);
    CompilerInput input = Mockito.mock(CompilerInput.class);
    Node externs = NodeOperations.createExternsNode("var a = 1,\nb = 2,\nc = 3");
    Node root = NodeOperations.createRootNode("function foo() { var a = function() {}; }", input);

    renameVars.process(externs, root);

    String expectedLog = "a => a\nb => b\nc => c\n";
    assertEquals(expectedLog, renameVars.assignmentLog.toString());
  }

  @Test
  public void testRenameVars_globalAndLocalVariables() {
    String prefix = "prefix_";
    RenameVars renameVars = new RenameVars(Mockito.mock(AbstractCompiler.class), prefix, false, false, false, null, null, null);
    CompilerInput input = Mockito.mock(CompilerInput.class);
    Node externs = NodeOperations.createExternsNode("var a = 1,\nb = 2,\nc = 3");
    Node root = NodeOperations.createRootNode("function foo() { var a = b + c; } var x = a;", input);

    renameVars.process(externs, root);

    String expectedLog = "a => a\nb => b\nc => c\na => a$$2\n";
    assertEquals(expectedLog, renameVars.assignmentLog.toString());
  }

  @Test
  public void testRenameVars_globalAndLocalVariablesWithReservedNames() {
    String prefix = "prefix_";
    List<String> reservedNames = Arrays.asList("for", "in", "new");
    RenameVars renameVars = new RenameVars(Mockito.mock(AbstractCompiler.class), prefix, false, false, false, null, null, reservedNames);
    CompilerInput input = Mockito.mock(CompilerInput.class);
    Node externs = NodeOperations.createExternsNode("var a = 1,\nb = 2,\nc = 3,\nfor:4,in:5,new:6");
    Node root = NodeOperations.createRootNode("function foo() { var a = b + c;\nfor (a in in) { new new(); } }", input);

    renameVars.process(externs, root);

    String expectedLog = "a => a\nb => b\nc => c\na => a$$2\nin => in$$3\nnew => new$$4\n";
    assertEquals(expectedLog, renameVars.assignmentLog.toString());
  }

  @Test
  public void testRenameVars_complexCode() {
    String prefix = "prefix_";
    RenameVars renameVars = new RenameVars(Mockito.mock(AbstractCompiler.class), prefix, false, false, false, null, null, null);
    CompilerInput input = Mockito.mock(CompilerInput.class);
    Node externs = NodeOperations.createExternsNode("var a = 1,\nb = 2,\nc = 3,\nd = 4,\ne = 5,\nf = 6,\ng = 7,\nh = 8," +
                                                   "i = 9,\ni = 10,\nj = 11,\nk = 12,\nl = 13,\nm = 14,\nod = 15,\np = 16");
    Node root = NodeOperations.createRootNode("function foo() {" +
                                             "var a = b + c,\n" +
                                             "i = 0,\ni = 0,\n" +
                                             "function bar(i, j) {" +
                                             "  var c = d + e,\n" +
                                             "  f = function() {},\n" +
                                             "  g = function() {},\n" +
                                             "  h = function() {},\n" +
                                             "  i++,\ni++,\n" +
                                             "  return a;\n" +
                                             "}", input);

    renameVars.process(externs, root);

    String expectedLog = "a => a\nb => b\nc => c\nd => d\ne => e\nf => f$$0\ng => g$$1\nh => h$$2\ni => i$$3\ni => i$$4\n";
    assertEquals(expectedLog, renameVars.assignmentLog.toString());
  }

  @Test
  public void testRenameVars_complexCodeWithReservedNames() {
    String prefix = "prefix_";
    List<String> reservedNames = Arrays.asList("for", "in", "new", "if", "else", "throw", "try", "catch");
    RenameVars renameVars = new RenameVars(Mockito.mock(AbstractCompiler.class), prefix, false, false, false, null, null, reservedNames);
    CompilerInput input = Mockito.mock(CompilerInput.class);
    Node externs = NodeOperations.createExternsNode("var a = 1,\nb = 2,\nc = 3,\nd = 4,\ne = 5,\nf = 6,\ng = 7,\nh = 8," +
                                                   "i = 9,\ni = 10,\nj = 11,\nk = 12,\nl = 13,\nm = 14,\nod = 15,\np = 16," +
                                                   "for:17,in:18,new:19,if:20,else:21,throw:22,try:23,catch:24");
    Node root = NodeOperations.createRootNode("function foo() {" +
                                             "var a = b + c,\n" +
                                             "i = 0,\ni = 0,\n" +
                                             "function bar(i, j) {" +
                                             "  var c = d + e,\n" +
                                             "  if (for (i in in) { try { throw new Error('test'); } catch (e) {}})" +
                                             "  return a;\n" +
                                             "}", input);

    renameVars.process(externs, root);

    String expectedLog = "a => a\nb => b\nc => c\nd => d\ne => e\ni => i$$3\ni => i$$4\nfor => for$$5\nin => in$$6\ntry => try$$7\ncatch => catch$$8\n";
    assertEquals(expectedLog, renameVars.assignmentLog.toString());
  }

  @Test
  public void testRenameVars_invalidCode() {
    String prefix = "prefix_";
    RenameVars renameVars = new RenameVars(Mockito.mock(AbstractCompiler.class), prefix, false, false, false, null, null, null);
    CompilerInput input = Mockito.mock(CompilerInput.class);
    Node externs = NodeOperations.createExternsNode("var a = 1,\nb = 2,\nc = 3");
    Node root = NodeOperations.createRootNode("function foo() { var a = b + c\nvar a;\n}", input);

    try {
      renameVars.process(externs, root);
      fail("Expected CompilationException");
    } catch (CompilerException e) {
      // Expected
    }
  }

  @Test
  public void testRenameVars_mockedMethods() {
    String prefix = "prefix_";
    RenameVars renameVars = Mockito.spy(new RenameVars(Mockito.mock(AbstractCompiler.class), prefix, false, false, false, null, null, null));
    CompilerInput input = Mockito.mock(CompilerInput.class);
    Node externs = NodeOperations.createExternsNode("var a = 1,\nb = 2,\nc = 3");
    Node root = NodeOperations.createRootNode("function foo() { var a = b + c; }", input);

    Preconditions.checkState(renameVars.assignmentLog == null);

    doCallRealMethod().when(renameVars).process(externs, root);

    renameVars.process(externs, root);

    String expectedLog = "a => a\nb => b\nc => c\n";
    assertEquals(expectedLog, renameVars.assignmentLog.toString());

    verify(renameVars).process(externs, root);
  }

  @Test
  public void testRenameVars_preserveAnonymousFunctionNames() {
    String prefix = "prefix_";
    boolean preserveAnonymousFunctionNames = true;
    RenameVars renameVars = new RenameVars(Mockito.mock(AbstractCompiler.class), prefix, false, preserveAnonymousFunctionNames, false, null, null, null);
    CompilerInput input = Mockito.mock(CompilerInput.class);
    Node externs = NodeOperations.createExternsNode("var a = 1,\nb = 2,\nc = 3");
    Node root = NodeOperations.createRootNode("function foo() { var a = b + c; function x() {} }", input);

    renameVars.process(externs, root);

    String expectedLog = "a => a\nb => b\nc => c\nx => x\n";
    assertEquals(expectedLog, renameVars.assignmentLog.toString());
  }

  @Test
  public void testRenameVars_generatePseudoNames() {
    String prefix = "prefix_";
    boolean generatePseudoNames = true;
    RenameVars renameVars = new RenameVars(Mockito.mock(AbstractCompiler.class), prefix, false, false, generatePseudoNames, null, null, null);
    CompilerInput input = Mockito.mock(CompilerInput.class);
    Node externs = NodeOperations.createExternsNode("var a = 1,\nb = 2,\nc = 3");
    Node root = NodeOperations.createRootNode("function foo() { var a = b + c; }", input);

    renameVars.process(externs, root);

    String expectedLog = "a => $a$$0\nb => b\nc => c$$1\n";
    assertEquals(expectedLog, renameVars.assignmentLog.toString());
  }

  @Test
  public void testRenameVars_localRenamingOnly() {
    String prefix = "prefix_";
    boolean localRenamingOnly = true;
    RenameVars renameVars = new RenameVars(Mockito.mock(AbstractCompiler.class), prefix, localRenamingOnly, false, false, null, null, null);
    CompilerInput input = Mockito.mock(CompilerInput.class);
    Node externs = NodeOperations.createExternsNode("var a = 1,\nb = 2,\nc = 3");
    Node root = NodeOperations.createRootNode("function foo() { var a = b + c; } var x = a;", input);

    renameVars.process(externs, root);

    String expectedLog = "a => a$$0\nb => b\nx => a$$1\n";
    assertEquals(expectedLog, renameVars.assignmentLog.toString());
  }

  @Test
  public void testRenameVars_reusedNamesFromPreviousCompilation() {
    String prefix = "prefix_";
    RenameVars renameVars = new RenameVars(Mockito.mock(AbstractCompiler.class), prefix, false, false, false,
        Mockito.mock(VariableMap.class), null, null);
    CompilerInput input = Mockito.mock(CompilerInput.class);
    Node externs = NodeOperations.createExternsNode("var a = 1,\nb = 2,\nc = 3");
    Node root = NodeOperations.createRootNode("function foo() { var a = b + c; }", input);

    Map<String, String> prevUsedRenameMap = new HashMap<>();
    prevUsedRenameMap.put("b", "newB");
    prevUsedRenameMap.put("c", "newC");

    when(renameVars.prevUsedRenameMap).thenReturn(prevUsedRenameMap);

    renameVars.process(externs, root);

    String expectedLog = "a => a\nb => newB\nc => newC\n";
    assertEquals(expectedLog, renameVars.assignmentLog.toString());
  }
}