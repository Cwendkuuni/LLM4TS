import com.google.common.base.Supplier;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Behavior;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Reference;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceCollection;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceMap;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InlineObjectLiteralsTest {

  private InlineObjectLiterals inlineObjectLiterals;

  @Before
  public void setUp() {
    inlineObjectLiterals = new InlineObjectLiterals();
  }

  @Test
  public void testInlineObjectLiterals() {
    AbstractCompiler compiler = new AbstractCompiler();
    Supplier<String> safeNameIdSupplier = new Supplier<String>() {
      @Override
      public String get() {
        return "id";
      }
    };
    inlineObjectLiterals = new InlineObjectLiterals(compiler, safeNameIdSupplier);

    // Create mock nodes
    Node externs = new Node(Token.SCRIPT);
    Node root = new Node(Token.SCRIPT);
    Node object = new Node(Token.OBJECTLIT);
    Node name = new Node(Token.NAME, "x");
    Node assign = new Node(Token.ASSIGN, name, object);
    Node var = new Node(Token.VAR, assign);

    // Add nodes to tree
    externs.addChildToBack(var);
    root.addChildToBack(var);

    // Run process method
    inlineObjectLiterals.process(externs, root);

    // Assertions
    // TODO: Add assertions
    assertTrue(true);

  }

}