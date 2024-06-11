import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.testing.TestExternsBuilder;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.testing.BaseJSTypeTestCase;
import java.util.List;
import org.junit.After;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class ScopedAliasesTest extends BaseJSTypeTestCase {

  private static ScopedAliases scopedAliases;

  @BeforeClass
  public static void setUp() {
    scopedAliases = new ScopedAliases();
  }

  @Test
  public void testProcess_SingleScopeCall_Success() {
    // Arrange
    Node script = parseAndProcess("goog.scope(function() {});");
    
    // Act
    scopedAliases.process(null, script);
    
    // Assert
    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    Assert.assertEquals(1, scopeCalls.size());
  }
  
  @Test
  public void testProcess_MultipleScopeCalls_Success() {
    // Arrange
    Node script = parseAndProcess("" +
        "goog.scope(function() {});" +
        "goog.scope(function() {});" +
        "goog.scope(function() {});");
    
    // Act
    scopedAliases.process(null, script);
    
    // Assert
    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    Assert.assertEquals(3, scopeCalls.size());
  }
  
  @Test
  public void testProcess_InvalidScopeCall_Error() {
    // Arrange
    Node script = parseAndProcess("" +
        "var x = 5;" +
        "goog.scope(function() {});" +
        "var y = 10;");
    
    // Act
    scopedAliases.process(null, script);
    
    // Assert
    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    List<JSError> errors = scopedAliases.getErrors();
    Assert.assertEquals(0, scopeCalls.size());
    Assert.assertEquals(1, errors.size());
    Assert.assertEquals("The call to goog.scope must be alone in a single statement.", errors.get(0).description);
  }
  
  @Test
  public void testHotSwapScript_SingleScopeCall_Success() {
    // Arrange
    Node script = parseAndProcess("goog.scope(function() {});");
    
    // Act
    scopedAliases.hotSwapScript(script, null);
    
    // Assert
    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    Assert.assertEquals(1, scopeCalls.size());
  }
  
  @Test
  public void testHotSwapScript_MultipleScopeCalls_Success() {
    // Arrange
    Node script = parseAndProcess("" +
        "goog.scope(function() {});" +
        "goog.scope(function() {});" +
        "goog.scope(function() {});");
    
    // Act
    scopedAliases.hotSwapScript(script, null);
    
    // Assert
    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    Assert.assertEquals(3, scopeCalls.size());
  }
  
  @Test
  public void testHotSwapScript_InvalidScopeCall_Error() {
    // Arrange
    Node script = parseAndProcess("" +
        "var x = 5;" +
        "goog.scope(function() {});" +
        "var y = 10;");
    
    // Act
    scopedAliases.hotSwapScript(script, null);
    
    // Assert
    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    List<JSError> errors = scopedAliases.getErrors();
    Assert.assertEquals(0, scopeCalls.size());
    Assert.assertEquals(1, errors.size());
    Assert.assertEquals("The call to goog.scope must be alone in a single statement.", errors.get(0).description);
  }
  
  @After
  public void tearDown() {
    scopedAliases.reset();
  }

  private Node parseAndProcess(String code) {
    Node script = parseScript(code);
    scopedAliases.processForTesting(null, script);
    return script;
  }
}