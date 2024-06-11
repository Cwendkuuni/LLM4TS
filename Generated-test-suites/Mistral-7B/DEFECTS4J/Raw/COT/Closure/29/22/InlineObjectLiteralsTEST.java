import static org.junit.Assert.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassTestBase;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Behavior;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Reference;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceCollection;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceMap;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Supplier;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InlineObjectLiteralsTest extends CompilerPassTestBase {

  public static class InlineObjectLiteralsTestPass implements CompilerPass {

    @Override
    public void process(Node externs, Node root) {
      // No operation here as we're testing the InlineObjectLiterals class,
      // not its behavior.
    }
  }

  @org.junit.Test
  public void testBasicFunctionality() {
    AbstractCompiler compiler = newAbstractCompiler();
    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, new Supplier<String>() {
      @Override
      public String get() {
        return "";
      }
    });

    Node root = parseTestSrc("InlineObjectLiteralsTest.test.js");
    pass.process(externs, root);
    checkCompiledSrcEqualsExpected("InlineObjectLiteralsTest.test.expected.js", source);
  }

  @org.junit.Test
  public void testObjectLiteralWithSingleProperty() {
    AbstractCompiler compiler = newAbstractCompiler();
    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, new Supplier<String>() {
      @Override
      public String get() {
        return "";
      }
    });

    Node root = parseTestSrc("InlineObjectLiteralsTest.singleProperty.js");
    ReferenceCollectingCallback callback = new ReferenceCollectingCallback(compiler, new InliningBehavior());
    callback.process(externs, root);

    ReferenceCollection referenceCollection = callback.getReferenceCollection();
    assertEquals("a", referenceCollection.getVariableName(referenceCollection.getReferences(0).get(0).getNode().getFirstChild().getString()));
    assertEquals("1", referenceCollection.getVariableName(referenceCollection.getReferences(1).get(0).getNode().getFirstChild().getString()));

    pass.process(externs, root);
    checkCompiledSrcEqualsExpected("InlineObjectLiteralsTest.singleProperty.expected.js", source);
  }

  @org.junit.Test
  public void testObjectLiteralWithMultipleProperties() {
    AbstractCompiler compiler = newAbstractCompiler();
    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, new Supplier<String>() {
      @Override
      public String get() {
        return "";
      }
    });

    Node root = parseTestSrc("InlineObjectLiteralsTest.multipleProperties.js");
    ReferenceCollectingCallback callback = new ReferenceCollectingCallback(compiler, new InliningBehavior());
    callback.process(externs, root);

    ReferenceCollection referenceCollection = callback.getReferenceCollection();
    assertEquals("a", referenceCollection.getVariableName(referenceCollection.getReferences(0).get(0).getNode().getFirstChild().getString()));
    assertEquals("1", referenceCollection.getVariableName(referenceCollection.getReferences(1).get(0).getNode().getFirstChild().getString()));
    assertEquals("b", referenceCollection.getVariableName(referenceCollection.getReferences(2).get(0).getNode().getFirstChild().getString()));
    assertEquals("2", referenceCollection.getVariableName(referenceCollection.getReferences(3).get(0).getNode().getFirstChild().getString()));

    pass.process(externs, root);
    checkCompiledSrcEqualsExpected("InlineObjectLiteralsTest.multipleProperties.expected.js", source);
  }

  @org.junit.Test
  public void testNestedObjectLiteral() {
    AbstractCompiler compiler = newAbstractCompiler();
    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, new Supplier<String>() {
      @Override
      public String get() {
        return "";
      }
    });

    Node root = parseTestSrc("InlineObjectLiteralsTest.nestedObject.js");
    ReferenceCollectingCallback callback = new ReferenceCollectingCallback(compiler, new InliningBehavior());
    callback.process(externs, root);

    ReferenceCollection referenceCollection = callback.getReferenceCollection();
    assertEquals("a.x", referenceCollection.getVariableName(referenceCollection.getReferences(0).get(0).getNode().getFirstChild().getString()));
    assertEquals("1", referenceCollection.getVariableName(referenceCollection.getReferences(1).get(0).getNode().getFirstChild().getString()));
    assertEquals("a.y", referenceCollection.getVariableName(referenceCollection.getReferences(2).get(0).getNode().getFirstChild().getString()));
    assertEquals("2", referenceCollection.getVariableName(referenceCollection.getReferences(3).get(0).getNode().getFirstChild().getString()));

    pass.process(externs, root);
    checkCompiledSrcEqualsExpected("InlineObjectLiteralsTest.nestedObject.expected.js", source);
  }

  @org.junit.Test
  public void testSelfReferentialAssignment() {
    AbstractCompiler compiler = newAbstractCompiler();
    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, new Supplier<String>() {
      @Override
      public String get() {
        return "";
      }
    });

    Node root = parseTestSrc("InlineObjectLiteralsTest.selfReferential.js");
    ReferenceCollectingCallback callback = new ReferenceCollectingCallback(compiler, new InliningBehavior());
    callback.process(externs, root);

    ReferenceCollection referenceCollection = callback.getReferenceCollection();
    assertEquals(0, referenceCollection.references.size());

    pass.process(externs, root);
    checkCompiledSrcEqualsExpected("InlineObjectLiteralsTest.selfReferential.expected.js", source);
  }

  @org.junit.Test
  public void testEdgeCase1() {
    AbstractCompiler compiler = newAbstractCompiler();
    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, new Supplier<String>() {
      @Override
      public String get() {
        return "";
      }
    });

    Node root = parseTestSrc("InlineObjectLiteralsTest.edgeCase1.js");
    ReferenceCollectingCallback callback = new ReferenceCollectingCallback(compiler, new InliningBehavior());
    callback.process(externs, root);

    ReferenceCollection referenceCollection = callback.getReferenceCollection();
    assertEquals("a.x", referenceCollection.getVariableName(referenceCollection.getReferences(0).get(0).getNode().getFirstChild().getString()));
    assertEquals("1", referenceCollection.getVariableName(referenceCollection.getReferences(1).get(0).getNode().getFirstChild().getString()));
    assertEquals("a.y.z", referenceCollection.getVariableName(referenceCollection.getReferences(2).get(0).getNode().getFirstChild().getString()));
    assertEquals("2", referenceCollection.getVariableName(referenceCollection.getReferences(3).get(0).getNode().getFirstChild().getString()));
    assertEquals("3", referenceCollection.getVariableName(referenceCollection.getReferences(4).get(0).getNode().getFirstChild().getString()));

    pass.process(externs, root);
    checkCompiledSrcEqualsExpected("InlineObjectLiteralsTest.edgeCase1.expected.js", source);
  }

  @org.junit.Test
  public void testEdgeCase2() {
    AbstractCompiler compiler = newAbstractCompiler();
    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, new Supplier<String>() {
      @Override
      public String get() {
        return "";
      }
    });

    Node root = parseTestSrc("InlineObjectLiteralsTest.edgeCase2.js");
    ReferenceCollectingCallback callback = new ReferenceCollectingCallback(compiler, new InliningBehavior());
    callback.process(externs, root);

    ReferenceCollection referenceCollection = callback.getReferenceCollection();
    assertEquals("a", referenceCollection.getVariableName(referenceCollection.getReferences(0).get(0).getNode().getFirstChild().getString()));
    assertEquals("b", referenceCollection.getVariableName(referenceCollection.getReferences(1).get(0).getNode().getFirstChild().getString()));
    assertEquals("c", referenceCollection.getVariableName(referenceCollection.getReferences(2).get(0).getNode().getFirstChild().getString()));

    pass.process(externs, root);
    checkCompiledSrcEqualsExpected("InlineObjectLiteralsTest.edgeCase2.expected.js", source);
  }

  @org.junit.Test
  public void testEdgeCase3() {
    AbstractCompiler compiler = newAbstractCompiler();
    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, new Supplier<String>() {
      @Override
      public String get() {
        return "";
      }
    });

    Node root = parseTestSrc("InlineObjectLiteralsTest.edgeCase3.js");
    ReferenceCollectingCallback callback = new ReferenceCollectingCallback(compiler, new InliningBehavior());
    callback.process(externs, root);

    ReferenceCollection referenceCollection = callback.getReferenceCollection();
    assertEquals("a.x", referenceCollection.getVariableName(referenceCollection.getReferences(0).get(0).getNode().getFirstChild().getString()));
    assertEquals("1", referenceCollection.getVariableName(referenceCollection.getReferences(1).get(0).getNode().getFirstChild().getString()));
    assertEquals("a.y", referenceCollection.getVariableName(referenceCollection.getReferences(2).get(0).getNode().getFirstChild().getString()));
    assertEquals("2", referenceCollection.getVariableName(referenceCollection.getReferences(3).get(0).getNode().getFirstChild().getString()));
    assertEquals("a.z", referenceCollection.getVariableName(referenceCollection.getReferences(4).get(0).getNode().getFirstChild().getString()));
    assertEquals("3", referenceCollection.getVariableName(referenceCollection.getReferences(5).get(0).getNode().getFirstChild().getString()));

    pass.process(externs, root);
    checkCompiledSrcEqualsExpected("InlineObjectLiteralsTest.edgeCase3.expected.js", source);
  }
}

class InliningBehavior implements Behavior {
  // Empty since we're only testing the InlineObjectLiterals class, not its behavior.
}