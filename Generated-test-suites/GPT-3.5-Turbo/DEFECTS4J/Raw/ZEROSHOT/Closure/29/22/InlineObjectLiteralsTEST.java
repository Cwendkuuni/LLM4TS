import com.google.common.base.Supplier;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class InlineObjectLiteralsTest {
  
  private AbstractCompiler compiler;
  private Supplier<String> safeNameIdSupplier;
  private InlineObjectLiterals inlineObjectLiterals;
  
  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    safeNameIdSupplier = mock(Supplier.class);
    inlineObjectLiterals = new InlineObjectLiterals(compiler, safeNameIdSupplier);
  }
  
  @Test
  public void testProcess() {
    Node externs = IR.empty();
    Node root = IR.empty();
    
    ReferenceCollectingCallback callback = mock(ReferenceCollectingCallback.class);
    whenNew(ReferenceCollectingCallback.class).withArguments(compiler, inlineObjectLiterals.new InliningBehavior()).thenReturn(callback);
    
    inlineObjectLiterals.process(externs, root);
    
    verify(callback).process(externs, root);
  }
}