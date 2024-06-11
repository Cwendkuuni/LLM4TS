import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Set;
import javax.annotation.Nullable;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractCompiler.LifeCycleStage;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.jscomp.Scope;

public class RenamePrototypesTest {
  
  private RenamePrototypes renamePrototypes;
  private AbstractCompiler compiler;
  
  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    renamePrototypes = new RenamePrototypes(compiler, true, null, null);
    compiler.initLifeCycleStage(LifeCycleStage.NORMALIZED);
  }
  
  @Test
  public void testProcess_noPropertiesToRename_expectedNoChange() {
    Node externs = new Node(Token.ROOT);
    Node root = new Node(Token.ROOT);
    
    renamePrototypes.process(externs, root);
    
    // Assert that no changes were made in the code
    assertEquals(0, compiler.getNumChangedFiles());
  }
  
  @Test
  public void testProcess_propertiesToRename_expectedRenamed() {
    Node externs = new Node(Token.ROOT);
    Node root = new Node(Token.ROOT);
    Node objLit1 = new Node(Token.OBJECTLIT); // First object literal
    Node objLit2 = new Node(Token.OBJECTLIT); // Second object literal
    
    // Creating sample properties
    Node property1_1 = createProperty("foo1", "bar1"); // Should be renamed
    Node property1_2 = createProperty("foo1", "bar2"); // Should be renamed
    Node property2_1 = createProperty("foo2", "bar1"); // Should not be renamed
    Node property2_2 = createProperty("foo2", "bar2"); // Should not be renamed
    
    objLit1.addChildToFront(property1_1);
    objLit1.addChildToBack(property1_2);
    objLit2.addChildToFront(property2_1);
    objLit2.addChildToBack(property2_2);
    
    root.addChildToFront(objLit1);
    root.addChildToBack(objLit2);
    
    renamePrototypes.process(externs, root);
    
    // Assert that the renamed properties were modified
    assertEquals("newBar1", property1_1.getString());
    assertEquals("newBar2", property1_2.getString());
    
    // Assert that the properties that should not be renamed remain the same
    assertEquals("bar1", property2_1.getString());
    assertEquals("bar2", property2_2.getString());
    
    // Assert that the code was modified
    assertEquals(1, compiler.getNumChangedFiles());
  }
  
  private Node createProperty(String key, String value) {
    Node property = new Node(Token.STRING);
    property.setString(key);
    property.setParent(null);
    property.setParent(null);
    
    Node valueNode = new Node(Token.STRING);
    valueNode.setString(value);
    valueNode.setParent(null);
    valueNode.setParent(null);
    
    Node pair = new Node(Token.STRING_KEY, property, valueNode);
    pair.setParent(null);
    pair.setParent(null);
    
    return pair;
  }
}