import org.junit.Assert;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class RenameVarsTest {
  
  @Test
  public void testProcess() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    Node externs = mock(Node.class);
    Node root = mock(Node.class);
    
    RenameVars renameVars = new RenameVars(compiler, "", false, false, false, null, null, null);
    renameVars.process(externs, root);
    
    verify(compiler, times(1)).reportCodeChange();
    verify(compiler, times(1)).addToDebugLog(anyString());
  }
  
  @Test
  public void testGetNewGlobalName() {
    Node n = mock(Node.class);
    
    RenameVars renameVars = new RenameVars(null, "", false, false, false, null, null, null);
    renameVars.renameMap.put("old_name", "new_name");
    
    String result = renameVars.getNewGlobalName(n);
    
    verify(n, times(1)).getString();
    Assert.assertEquals("new_name", result);
  }
  
  @Test
  public void testGetNewLocalName() {
    Node n = mock(Node.class);
    
    RenameVars renameVars = new RenameVars(null, "", false, false, false, null, null, null);
    renameVars.renameMap.put("old_name", "new_name");
    
    String result = renameVars.getNewLocalName(n, 0);
    
    verify(n, times(1)).getString();
    Assert.assertEquals("new_name", result);
  }
  
  @Test
  public void testGetPseudoName() {
    RenameVars renameVars = new RenameVars(null, "", false, false, false, null, null, null);
    
    String result = renameVars.getPseudoName("name");
    
    Assert.assertEquals("$name$$", result);
  }
  
}