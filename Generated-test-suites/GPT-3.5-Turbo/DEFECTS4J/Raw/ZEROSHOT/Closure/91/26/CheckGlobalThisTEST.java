import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.Token;

import static org.mockito.Mockito.*;

public class CheckGlobalThisTest {

   private CheckGlobalThis checkGlobalThis;
   private AbstractCompiler compiler;
   private CheckLevel level;
   private NodeTraversal t;
   private Node n;
   private Node parent;

   @Before
   public void setUp() {
      compiler = mock(AbstractCompiler.class);
      level = CheckLevel.ERROR;
      t = mock(NodeTraversal.class);
      n = mock(Node.class);
      parent = mock(Node.class);
      checkGlobalThis = new CheckGlobalThis(compiler, level);
   }

   @Test
   public void shouldTraverse_ReturnsFalseForConstructorFunction() {
      int type = Token.FUNCTION;
      NodeTraversal.Callback callback = new CheckGlobalThis(compiler, level);
      JSDocInfo jsDoc = mock(JSDocInfo.class);
      when(n.getType()).thenReturn(type);
      when(n.getJSDocInfo()).thenReturn(jsDoc);
      when(jsDoc.isConstructor()).thenReturn(true);

      boolean result = callback.shouldTraverse(t, n, parent);

      assertFalse(result);
   }

   @Test
   public void shouldTraverse_ReturnsFalseForThisAnnotation() {
      int type = Token.FUNCTION;
      NodeTraversal.Callback callback = new CheckGlobalThis(compiler, level);
      JSDocInfo jsDoc = mock(JSDocInfo.class);
      when(n.getType()).thenReturn(type);
      when(n.getJSDocInfo()).thenReturn(jsDoc);
      when(jsDoc.hasThisType()).thenReturn(true);

      boolean result = callback.shouldTraverse(t, n, parent);

      assertFalse(result);
   }

   @Test
   public void shouldTraverse_ReturnsFalseForOverrideAnnotation() {
      int type = Token.FUNCTION;
      NodeTraversal.Callback callback = new CheckGlobalThis(compiler, level);
      JSDocInfo jsDoc = mock(JSDocInfo.class);
      when(n.getType()).thenReturn(type);
      when(n.getJSDocInfo()).thenReturn(jsDoc);
      when(jsDoc.isOverride()).thenReturn(true);

      boolean result = callback.shouldTraverse(t, n, parent);

      assertFalse(result);
   }

   @Test
   public void shouldTraverse_ReturnsFalseForNonGlobalContext() {
      int type = Token.FUNCTION;
      int parentType = Token.BLOCK;
      NodeTraversal.Callback callback = new CheckGlobalThis(compiler, level);
      JSDocInfo jsDoc = mock(JSDocInfo.class);
      when(n.getType()).thenReturn(type);
      when(parent.getType()).thenReturn(parentType);
      when(n.getJSDocInfo()).thenReturn(jsDoc);
      when(jsDoc.isConstructor()).thenReturn(false);
      when(jsDoc.isInterface()).thenReturn(false);
      when(jsDoc.hasThisType()).thenReturn(false);
      when(jsDoc.isOverride()).thenReturn(false);

      boolean result = callback.shouldTraverse(t, n, parent);

      assertFalse(result);
   }

   @Test
   public void shouldTraverse_ReturnsTrueForGlobalContext() {
      int type = Token.FUNCTION;
      int parentType = Token.SCRIPT;
      NodeTraversal.Callback callback = new CheckGlobalThis(compiler, level);
      JSDocInfo jsDoc = mock(JSDocInfo.class);
      when(n.getType()).thenReturn(type);
      when(parent.getType()).thenReturn(parentType);
      when(n.getJSDocInfo()).thenReturn(jsDoc);
      when(jsDoc.isConstructor()).thenReturn(false);
      when(jsDoc.isInterface()).thenReturn(false);
      when(jsDoc.hasThisType()).thenReturn(false);
      when(jsDoc.isOverride()).thenReturn(false);

      boolean result = callback.shouldTraverse(t, n, parent);

      assertTrue(result);
   }

   @Test
   public void visit_ShouldReportThis() {
      int type = Token.THIS;
      NodeTraversal.Callback callback = new CheckGlobalThis(compiler, level);
      DiagnosticType globalThis = mock(DiagnosticType.class);
      when(n.getType()).thenReturn(type);
      when(n == parent).thenReturn(true);
      when(callback.shouldReportThis(n, parent)).thenReturn(true);

      callback.visit(t, n, parent);

      verify(compiler).report(any());
   }

   @Test
   public void visit_ShouldNotReportThis() {
      int type = Token.THIS;
      NodeTraversal.Callback callback = new CheckGlobalThis(compiler, level);
      DiagnosticType globalThis = mock(DiagnosticType.class);
      when(n.getType()).thenReturn(type);
      when(n == parent).thenReturn(true);
      when(callback.shouldReportThis(n, parent)).thenReturn(false);

      callback.visit(t, n, parent);

      verify(compiler, never()).report(any());
   }
}