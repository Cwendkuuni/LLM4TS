import org.junit.Assert;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class CheckSideEffectsTest {

  @Test
  public void testProcess_ValidInput_SideEffectWarningGiven() {
    // Arrange
    AbstractCompiler compilerMock = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    boolean protectSideEffectFreeCode = true;
    CheckSideEffects checkSideEffects = new CheckSideEffects(compilerMock, level, protectSideEffectFreeCode);
    Node externs = mock(Node.class);
    Node root = mock(Node.class);

    // Act
    checkSideEffects.process(externs, root);

    // Assert
    verify(compilerMock, times(1)).report(any(Error.class));
  }

  @Test
  public void testHotSwapScript_ValidInput_SideEffectWarningGiven() {
    // Arrange
    AbstractCompiler compilerMock = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    boolean protectSideEffectFreeCode = true;
    CheckSideEffects checkSideEffects = new CheckSideEffects(compilerMock, level, protectSideEffectFreeCode);
    Node scriptRoot = mock(Node.class);
    Node originalRoot = mock(Node.class);

    // Act
    checkSideEffects.hotSwapScript(scriptRoot, originalRoot);

    // Assert
    verify(compilerMock, times(1)).report(any(Error.class));
  }

  @Test
  public void testVisit_ValidInput_SideEffectWarningGiven() {
    // Arrange
    NodeTraversal tMock = mock(NodeTraversal.class);
    Node nMock = mock(Node.class);
    Node parentMock = mock(Node.class);
    AbstractCompiler compilerMock = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    boolean protectSideEffectFreeCode = true;
    CheckSideEffects checkSideEffects = new CheckSideEffects(compilerMock, level, protectSideEffectFreeCode);

    // Act
    checkSideEffects.visit(tMock, nMock, parentMock);

    // Assert
    verify(compilerMock, times(1)).report(any(Error.class));
  }

  @Test
  public void testProtectSideEffects_ExistingProblemNodes_ProblemNodeReplaced() {
    // Arrange
    AbstractCompiler compilerMock = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    boolean protectSideEffectFreeCode = true;
    CheckSideEffects checkSideEffects = new CheckSideEffects(compilerMock, level, protectSideEffectFreeCode);
    Node problemNode = mock(Node.class);
    checkSideEffects.problemNodes.add(problemNode);

    // Act
    checkSideEffects.protectSideEffects();

    // Assert
    verify(compilerMock, times(1)).reportCodeChange();
    verify(problemNode, times(1)).getParent();
    verify(problemNode, times(1)).addChildToBack(any(Node.class));
  }

  @Test
  public void testProtectSideEffects_NoProblemNodes_NoChangeMade() {
    // Arrange
    AbstractCompiler compilerMock = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    boolean protectSideEffectFreeCode = true;
    CheckSideEffects checkSideEffects = new CheckSideEffects(compilerMock, level, protectSideEffectFreeCode);

    // Act
    checkSideEffects.protectSideEffects();

    // Assert
    verify(compilerMock, never()).reportCodeChange();
  }

  @Test
  public void testAddExtern_ExternAdded() {
    // Arrange
    AbstractCompiler compilerMock = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    boolean protectSideEffectFreeCode = true;
    CheckSideEffects checkSideEffects = new CheckSideEffects(compilerMock, level, protectSideEffectFreeCode);
    Node nameNodeMock = mock(Node.class);
    nameNodeMock.putBooleanProp(Node.IS_CONSTANT_NAME, true);
    Node varNodeMock = mock(Node.class);
    CompilerInput inputMock = mock(CompilerInput.class);
    when(compilerMock.getSynthesizedExternsInput()).thenReturn(inputMock);

    // Act
    checkSideEffects.addExtern();

    // Assert
    verify(inputMock, times(1)).getAstRoot(compilerMock);
  }

  @Test
  public void testStripProtection_CallNodeWithTargetMatched_CallNodeRemoved() {
    // Arrange
    AbstractCompiler compilerMock = mock(AbstractCompiler.class);
    CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(compilerMock);
    NodeTraversal tMock = mock(NodeTraversal.class);
    Node callNodeMock = mock(Node.class);
    Node targetMock = mock(Node.class);
    Node parentMock = mock(Node.class);
    when(callNodeMock.isCall()).thenReturn(true);
    when(callNodeMock.getFirstChild()).thenReturn(targetMock);
    when(targetMock.isName()).thenReturn(true);
    when(targetMock.getString()).thenReturn(CheckSideEffects.PROTECTOR_FN);
    when(callNodeMock.getLastChild()).thenReturn(mock(Node.class));

    // Act
    stripProtection.visit(tMock, callNodeMock, parentMock);

    // Assert
    verify(parentMock, times(1)).replaceChild(callNodeMock, any(Node.class));
  }

  @Test
  public void testStripProtection_CallNodeWithTargetNotMatched_NoChangeMade() {
    // Arrange
    AbstractCompiler compilerMock = mock(AbstractCompiler.class);
    CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(compilerMock);
    NodeTraversal tMock = mock(NodeTraversal.class);
    Node callNodeMock = mock(Node.class);
    Node targetMock = mock(Node.class);
    Node parentMock = mock(Node.class);
    when(callNodeMock.isCall()).thenReturn(true);
    when(callNodeMock.getFirstChild()).thenReturn(targetMock);
    when(targetMock.isName()).thenReturn(false);
    when(targetMock.getString()).thenReturn("someOtherFunction");

    // Act
    stripProtection.visit(tMock, callNodeMock, parentMock);

    // Assert
    verify(parentMock, never()).replaceChild(callNodeMock, any(Node.class));
  }
}