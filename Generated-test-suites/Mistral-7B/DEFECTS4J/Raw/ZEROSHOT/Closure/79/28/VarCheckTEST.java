import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.NodeUtil;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class VarCheckTest {

  private AbstractCompiler mockCompiler;
  private VarCheck varCheck;
  private CompilerInput mockInput;
  private CompilerInput mockExternsInput;
  private Node mockRoot;
  private Node mockExterns;

  @Before
  public void setUp() {
    mockCompiler = mock(AbstractCompiler.class);
    varCheck = new VarCheck(mockCompiler);

    mockInput = Mockito.mock(CompilerInput.class);
    mockExternsInput = Mockito.mock(CompilerInput.class);
    mockRoot = Mockito.mock(Node.class);
    mockExterns = Mockito.mock(Node.class);

    when(mockCompiler.newExternInput("{SyntheticVarsDeclar}"))
        .thenReturn(mockExternsInput);

    when(mockCompiler.getModuleGraph()).thenReturn(new JSModuleGraph() {
      @Override
      public void addDependency(JSModule module, JSModule dep) {
        // no-op in tests
      }

      @Override
      public boolean dependsOn(JSModule module, JSModule dep) {
        return false;
      }
    });
  }

  @Test
  public void testUndefinedVarError() {
    Node undefinedVar = Node.newString(Token.NAME, "undefinedVar");

    when(mockInput.getAstRoot(mockCompiler)).thenReturn(mockRoot);
    when(mockRoot.getFirstChild()).thenReturn(undefinedVar);

    Set<String> expectedVarsToDeclareInExterns = new HashSet<>();
    expectedVarsToDeclareInExterns.add("undefinedVar");

    varCheck.process(mockExterns, mockRoot);

    assertEquals(expectedVarsToDeclareInExterns, varCheck.varsToDeclareInExterns);
  }

  @Test
  public void testDuplicateVarError() {
    String varName = "duplicateVar";

    Node firstVar = Node.newString(Token.NAME, varName);
    Node secondVar = Node.newString(Token.NAME, varName);

    Node function = Node.newFunction(varName, null);
    function.addChildToBack(firstVar);
    function.addChildToBack(secondVar);

    when(mockInput.getAstRoot(mockCompiler)).thenReturn(function);
    when(mockRoot.getParent()).thenReturn(NodeUtil.getScriptNode());

    varCheck.process(mockExterns, mockRoot);

    assertNull(varCheck.varsToDeclareInExterns.iterator().next());
  }

  @Test
  public void testModuleDependencyError() {
    String moduleName = "moduleA";
    String dependentModuleName = "moduleB";
    String varName = "sharedVar";

    Node varNode = Node.newString(Token.NAME, varName);
    Node moduleANode = Node.newName("moduleA");
    Node moduleBNode = Node.newName("moduleB");

    Node moduleA = Node.newBlock(Node.newList(moduleANode));
    Node moduleB = Node.newBlock(Node.newList(moduleBNode));

    varCheck.varsToDeclareInExterns = Sets.newHashSet();

    when(mockInput.getAstRoot(mockCompiler)).thenReturn(moduleA);
    when(mockExterns.getAstRoot(mockCompiler)).thenReturn(moduleB);
    when(mockRoot.getParent()).thenReturn(moduleANode);

    varCheck.process(mockExterns, mockRoot);

    assertEquals(1, varCheck.varsToDeclareInExterns.size());
    assertEquals(dependentModuleName, varCheck.varsToDeclareInExterns.iterator().next());
  }
}