import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Node;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class CheckSideEffectsTest {

 private AbstractCompiler compilerMock;
 private CheckSideEffects checkSideEffects;

 @Before
 public void setUp() {
 compilerMock = mock(AbstractCompiler.class);
 checkSideEffects = new CheckSideEffects(compilerMock, CheckLevel.WARNING, false);
 }

 @Test
 public void testCheckSideEffects_simpleExpression() {
 Node root = Node.parseJson("{x: 1 + 2}");
 checkSideEffects.process(null, root);
 verify(compilerMock, times(0)).report(any(Node.class), anyInt(), any(DiagnosticType.class), anyString());
 }

 @Test
 public void testCheckSideEffects_voidStatement() {
 Node root = Node.parseJson("{}");
 checkSideEffects.process(null, root);
 verify(compilerMock, times(0)).report(any(Node.class), anyInt(), any(DiagnosticType.class), anyString());
 }

 @Test
 public void testCheckSideEffects_assignmentExpression() {
 Node root = Node.parseJson("{x = 1 + 2}");
 checkSideEffects.process(null, root);
 verify(compilerMock, atLeastOnce()).report(any(Node.class), anyInt(), eq(CheckSideEffects.USELESS_CODE_ERROR),
 "Suspicious code. {0}assignments without side effects found in this expression tree.");
 }

 @Test
 public void testCheckSideEffects_expressionResult() {
 Node root = Node.parseJson("{return 1 + 2}");
 checkSideEffects.process(null, root);
 verify(compilerMock, times(0)).report(any(Node.class), anyInt(), any(DiagnosticType.class), anyString());
 }

 @Test
 public void testCheckSideEffects_functionCallWithSideEffect() {
 Node root = Node.parseJson("{console.log(1);}");
 checkSideEffects.process(null, root);
 verify(compilerMock, atLeastOnce()).report(any(Node.class), anyInt(), eq(CheckSideEffects.USELESS_CODE_ERROR),
 "Suspicious code. console.log has side effects.");
 }

 @Test
 public void testCheckSideEffects_functionCallWithoutSideEffect() {
 Node root = Node.parseJson("{Math.sqrt(2);}");
 checkSideEffects.process(null, root);
 verify(compilerMock, times(0)).report(any(Node.class), anyInt(), any(DiagnosticType.class), anyString());
 }

 @Test
 public void testCheckSideEffects_complexExpression() {
 Node root = Node.parseJson("{if (1 + 2 > 3) { console.log('Hello World'); } else { console.log('Goodbye World'); }}");
 checkSideEffects.process(null, root);
 verify(compilerMock, atLeastOnce()).report(any(Node.class), anyInt(), eq(CheckSideEffects.USELESS_CODE_ERROR),
 "Suspicious code. The conditional statement's if-branch and else-branch have side effects.");
 }

 @Test
 public void testCheckSideEffects_protectSideEffectFreeCode() {
 Node root = Node.parseJson("{console.log('Protected:')}");
 CheckSideEffects protection = new CheckSideEffects(compilerMock, CheckLevel.NONE, true);
 protection.process(null, root);
 Node protectedNode = protection.problemNodes.get(0);
 assertEquals(Node.CALL, protectedNode.getType());
 Node protectorName = protectedNode.getFirstChild();
 assertEquals("JSCOMPILER_PRESERVE", protectorName.getString());
 protection.protectSideEffects();
 checkSideEffects.process(null, root);
 verify(compilerMock, times(0)).report(any(Node.class), anyInt(), any(DiagnosticType.class), anyString());
 }

 @After
 public void tearDown() {
 reset(compilerMock);
 checkSideEffects.problemNodes.clear();
 }
}