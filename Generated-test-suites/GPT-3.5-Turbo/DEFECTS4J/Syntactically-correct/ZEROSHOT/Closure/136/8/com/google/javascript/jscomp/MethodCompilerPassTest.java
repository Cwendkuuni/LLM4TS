import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.rhino.Token;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Set;

public class MethodCompilerPassTest {
  
  private class MockCompiler extends AbstractCompiler {
    @Override
    public void process(Node externs, Node root) {
      // Do nothing
    }
    
    @Override
    public boolean isIdeMode() {
      return false;
    }
  }
  
  private MethodCompilerPass compilerPass;
  private MockCompiler compiler;
  
  @Before
  public void setUp() {
    compiler = new MockCompiler();
    compilerPass = new MethodCompilerPass(compiler) {
      @Override
      public Callback getActingCallback() {
        // TODO: Implement this method
        return null;
      }
      
      @Override
      public SignatureStore getSignatureStore() {
        // TODO: Implement this method
        return null;
      }
    };
  }
  
  @Test
  public void testProcess() {
    // TODO: Implement test case
  }
  
  @Test
  public void testAddPossibleSignature() {
    // TODO: Implement test case
  }
  
  @Test
  public void testAddSignature() {
    // TODO: Implement test case
  }
  
  @Test
  public void testGetExternMethods() {
    // TODO: Implement test case
  }
  
  @Test
  public void testGatherSignatures() {
    // TODO: Implement test case
  }
}