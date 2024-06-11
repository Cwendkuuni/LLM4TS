import static org.junit.Assert.*;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler.LifeCycleStage;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.TokenStream;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.annotation.Nullable;
import org.junit.Before;
import org.junit.Test;

public class RenamePrototypesTest {

  private RenamePrototypes renamePrototypes;
  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    renamePrototypes = new RenamePrototypes(compiler, true, null, null);
  }

  @Test
  public void process_ValidPropertiesAndNodes_ShouldUpdateNames() {
    // Create input nodes
    Node externs = new Node(Token.SCRIPT,
        new Node(Token.GETPROP,
            new Node(Token.NAME, new Node(Token.EXTERN)),
            new Node(Token.STRING, "foo")),
        new Node(Token.STRING, "bar"));
    Node root = new Node(Token.SCRIPT,
        new Node(Token.GETPROP,
            new Node(Token.NAME, new Node(Token.EXTERN)),
            new Node(Token.STRING, "foo")),
        new Node(Token.STRING, "bar"));

    // Call the process method
    renamePrototypes.process(externs, root);

    // Assert the names have been correctly updated
    assertEquals("bar", root.getFirstChild().getFirstChild().getNext().getString());
  }
}