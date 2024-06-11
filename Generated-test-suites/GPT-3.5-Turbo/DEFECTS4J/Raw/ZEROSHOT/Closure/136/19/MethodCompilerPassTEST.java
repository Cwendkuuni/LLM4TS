import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;
import java.util.Set;

public class MethodCompilerPassTest {

  private MethodCompilerPass methodCompilerPass;

  @Before
  public void setUp() {
    methodCompilerPass = new MethodCompilerPass() {
      @Override
      Callback getActingCallback() {
          return null;
      }

      @Override
      SignatureStore getSignatureStore() {
          return null;
      }
    };
  }

  @Test
  public void testProcess() {
    Node externs = null;
    Node root = null;
    methodCompilerPass.process(externs, root);
    // Add assertions here
  }

  @Test
  public void testVisit_Gets() {
    NodeTraversal t = null;
    Node n = null;
    Node parent = null;
    methodCompilerPass.new GetExternMethods().visit(t, n, parent);
    // Add assertions here
  }

  @Test
  public void testVisit_ObjectLit() {
    NodeTraversal t = null;
    Node n = null;
    Node parent = null;
    methodCompilerPass.new GetExternMethods().visit(t, n, parent);
    // Add assertions here
  }

  @Test
  public void testVisit_Gets_WithPrototype() {
    NodeTraversal t = null;
    Node n = null;
    Node parent = null;
    methodCompilerPass.new GatherSignatures().visit(t, n, parent);
    // Add assertions here
  }

  @Test
  public void testVisit_Assign() {
    NodeTraversal t = null;
    Node n = null;
    Node parent = null;
    methodCompilerPass.new GatherSignatures().visit(t, n, parent);
    // Add assertions here
  }

  @Test
  public void testProcess() {
    Node externs = null;
    Node root = null;
    methodCompilerPass.process(externs, root);
    // Add assertions here
  }

}