
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.jscomp.*;

public class InlineObjectLiteralsTest {
  
  private Compiler compiler;
  
  @Before
  public void setUp() {
    compiler = new Compiler();
  }
  
  @Test
  public void testProcess() {
    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(compiler);
    Node externs = new Node(NodeTokens.EMPTY, "externs");
    Node root = new Node(NodeTokens.EMPTY, "root");
    inlineObjectLiterals.process(externs, root);
    // Add assertions here
  }
}
